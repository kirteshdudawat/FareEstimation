package com.kirtesh.fareestimation.workflow.farecalculator;

import com.kirtesh.fareestimation.cache.JvmCache;
import com.kirtesh.fareestimation.cache.JvmPreviousEvent;
import com.kirtesh.fareestimation.exception.ServiceException;
import com.kirtesh.fareestimation.models.EventMetadata;
import com.kirtesh.fareestimation.utility.CustomFileWriter;
import com.kirtesh.fareestimation.workflow.framework.Workflow;
import com.kirtesh.fareestimation.workflow.framework.WorkflowExecutor;
import com.kirtesh.fareestimation.workflow.framework.dtos.ExecutorResponse;
import com.kirtesh.fareestimation.workflow.framework.dtos.WorkflowMessage;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FareEstimationWorkflowTest {
    class Fixture {
        EventMetadata eventMetadata = new EventMetadata("-1,28.612894,77.229446,1405594957");
        EventMetadata invalidEventMetadata = new EventMetadata("-1,29.612894,79.229446,1405594958");
        EventMetadata eventMetadata2 = new EventMetadata("-1,28.656473,77.242943,1405595500");
        EventMetadata eventMetadata3 = new EventMetadata("-2,28.612894,77.229446,1405594957");
        EventMetadata eventMetadata2Replace = new EventMetadata("-1,28.612895,77.229447,1405594970");

        Workflow<EventMetadata> workflow = new FareEstimationWorkflow();
        WorkflowExecutor<EventMetadata> executor = new WorkflowExecutor<>(workflow);

        public void resetData() throws ServiceException {
            JvmCache.loadCache();
            JvmPreviousEvent.resetCache();
            try {
                CustomFileWriter.initialize("./success.csv", "./skipped.csv");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void validateParseEventToMessageDataStepForSuccess(ExecutorResponse<EventMetadata> element, long rideId, double lat, double longitude, long epoch) {
            Assertions.assertEquals("ParseEventToMessageDataStep", element.getStepName());
            Assertions.assertEquals("SUCCESS", element.getStatus());
            Assertions.assertEquals(rideId, element.getMetadata().getRideId());
            Assertions.assertEquals(lat, element.getMetadata().getLatitude());
            Assertions.assertEquals(longitude, element.getMetadata().getLongitude());
            Assertions.assertEquals(epoch, element.getMetadata().getEpochTime());
        }

        public void validateParseDateTimeStepForSuccess(ExecutorResponse<EventMetadata> element, LocalTime localTime) {
            Assertions.assertEquals("ParseDateTimeStep", element.getStepName());
            Assertions.assertEquals("SUCCESS", element.getStatus());
            Assertions.assertEquals(localTime, element.getMetadata().getLocalTime());
        }

        public void validateCalculateSpeedStepForSuccess(ExecutorResponse<EventMetadata> element, double speed) {
            Assertions.assertEquals("CalculateSpeedStep", element.getStepName());
            Assertions.assertEquals("SUCCESS", element.getStatus());
            Assertions.assertEquals(speed, element.getMetadata().getSpeedInMetersPerSecond());
        }

        public void validateEventBasedFareCalculatorStepForSuccess(ExecutorResponse<EventMetadata> element, double fare) {
            Assertions.assertEquals("EventBasedFareCalculatorStep", element.getStepName());
            Assertions.assertEquals("SUCCESS", element.getStatus());
            Assertions.assertEquals(fare, element.getMetadata().getTotalFareIncludingCurrentEvent());
        }

        public void validateStepForSuccess(ExecutorResponse<EventMetadata> element, String stepName) {
            Assertions.assertEquals(stepName, element.getStepName());
            Assertions.assertEquals("SUCCESS", element.getStatus());
        }
    }


    @DisplayName("Test FareEstimationWorkflow for happy flow")
    @Test
    @Order(1)
    void testHappyFlowForFareEstimationWorkflow() throws ServiceException {
        Fixture f = new Fixture();
        f.resetData();

        // Create result
        List<ExecutorResponse<EventMetadata>> firstEvent = f.executor.executeWorkflow(new WorkflowMessage<>(f.eventMetadata, UUID.randomUUID().toString(), Optional.empty(), 3, Optional.empty()));
        List<ExecutorResponse<EventMetadata>> secondEvent = f.executor.executeWorkflow(new WorkflowMessage<>(f.eventMetadata2, UUID.randomUUID().toString(), Optional.empty(), 3, Optional.empty()));

        // Verify result
        Assertions.assertEquals(6, firstEvent.size());

        f.validateParseEventToMessageDataStepForSuccess(firstEvent.get(0), -1, 28.612894, 77.229446, 1405594957);
        f.validateParseDateTimeStepForSuccess(firstEvent.get(1), LocalTime.of(16, 32, 37));
        f.validateStepForSuccess(firstEvent.get(2), "IsNewRideStep");
        f.validateCalculateSpeedStepForSuccess(firstEvent.get(3), 0.0);
        f.validateEventBasedFareCalculatorStepForSuccess(firstEvent.get(4), 1.3);
        f.validateStepForSuccess(firstEvent.get(5), "UpdateJvmCache");

        Assertions.assertEquals(6, secondEvent.size());

        f.validateParseEventToMessageDataStepForSuccess(secondEvent.get(0), -1, 28.656473, 77.242943, 1405595500);
        f.validateParseDateTimeStepForSuccess(secondEvent.get(1), LocalTime.of(16, 41, 40));
        f.validateStepForSuccess(secondEvent.get(2), "IsNewRideStep");
        f.validateCalculateSpeedStepForSuccess(secondEvent.get(3), 9.247896831969742);
        f.validateEventBasedFareCalculatorStepForSuccess(secondEvent.get(4), 5.015989905022082);
        f.validateStepForSuccess(secondEvent.get(5), "UpdateJvmCache");
    }

    @DisplayName("Test FareEstimationWorkflow with invalid event flow")
    @Test
    @Order(2)
    void testFlowWithInvalidEventForFareEstimationWorkflow() throws ServiceException {
        Fixture f = new Fixture();
        f.resetData();

        // Create result
        List<ExecutorResponse<EventMetadata>> firstEvent = f.executor.executeWorkflow(new WorkflowMessage<>(f.eventMetadata, UUID.randomUUID().toString(), Optional.empty(), 3, Optional.empty()));
        List<ExecutorResponse<EventMetadata>> invalidEvent = f.executor.executeWorkflow(new WorkflowMessage<>(f.invalidEventMetadata, UUID.randomUUID().toString(), Optional.empty(), 3, Optional.empty()));
        List<ExecutorResponse<EventMetadata>> secondEvent = f.executor.executeWorkflow(new WorkflowMessage<>(f.eventMetadata2, UUID.randomUUID().toString(), Optional.empty(), 3, Optional.empty()));

        // Verify result
        Assertions.assertEquals(6, firstEvent.size());

        f.validateParseEventToMessageDataStepForSuccess(firstEvent.get(0), -1, 28.612894, 77.229446, 1405594957);
        f.validateParseDateTimeStepForSuccess(firstEvent.get(1), LocalTime.of(16, 32, 37));
        f.validateStepForSuccess(firstEvent.get(2), "IsNewRideStep");
        f.validateCalculateSpeedStepForSuccess(firstEvent.get(3), 0.0);
        f.validateEventBasedFareCalculatorStepForSuccess(firstEvent.get(4), 1.3);
        f.validateStepForSuccess(firstEvent.get(5), "UpdateJvmCache");

        Assertions.assertEquals(5, invalidEvent.size());

        f.validateParseEventToMessageDataStepForSuccess(invalidEvent.get(0), -1, 29.612894, 79.229446, 1405594958);
        f.validateParseDateTimeStepForSuccess(invalidEvent.get(1), LocalTime.of(16, 32, 38));
        f.validateStepForSuccess(invalidEvent.get(2), "IsNewRideStep");
        f.validateCalculateSpeedStepForSuccess(invalidEvent.get(3), 223856.22309752164);
        f.validateStepForSuccess(invalidEvent.get(4), "SkippedFailedFileCreationStep");

        Assertions.assertEquals(6, secondEvent.size());

        f.validateParseEventToMessageDataStepForSuccess(secondEvent.get(0), -1, 28.656473, 77.242943, 1405595500);
        f.validateParseDateTimeStepForSuccess(secondEvent.get(1), LocalTime.of(16, 41, 40));
        f.validateStepForSuccess(secondEvent.get(2), "IsNewRideStep");
        f.validateCalculateSpeedStepForSuccess(secondEvent.get(3), 9.247896831969742);
        f.validateEventBasedFareCalculatorStepForSuccess(secondEvent.get(4), 5.015989905022082);
        f.validateStepForSuccess(secondEvent.get(5), "UpdateJvmCache");
    }

    @DisplayName("Test FareEstimationWorkflow with multiple ride event")
    @Test
    @Order(3)
    void testFlowWithMultipleRideEventForFareEstimationWorkflow() throws ServiceException {
        Fixture f = new Fixture();
        f.resetData();

        // Create result
        List<ExecutorResponse<EventMetadata>> firstEvent = f.executor.executeWorkflow(new WorkflowMessage<>(f.eventMetadata, UUID.randomUUID().toString(), Optional.empty(), 3, Optional.empty()));
        List<ExecutorResponse<EventMetadata>> secondEvent = f.executor.executeWorkflow(new WorkflowMessage<>(f.eventMetadata2, UUID.randomUUID().toString(), Optional.empty(), 3, Optional.empty()));
        List<ExecutorResponse<EventMetadata>> thirdEvent = f.executor.executeWorkflow(new WorkflowMessage<>(f.eventMetadata3, UUID.randomUUID().toString(), Optional.empty(), 3, Optional.empty()));

        // Verify result
        Assertions.assertEquals(6, firstEvent.size());

        f.validateParseEventToMessageDataStepForSuccess(firstEvent.get(0), -1, 28.612894, 77.229446, 1405594957);
        f.validateParseDateTimeStepForSuccess(firstEvent.get(1), LocalTime.of(16, 32, 37));
        f.validateStepForSuccess(firstEvent.get(2), "IsNewRideStep");
        f.validateCalculateSpeedStepForSuccess(firstEvent.get(3), 0.0);
        f.validateEventBasedFareCalculatorStepForSuccess(firstEvent.get(4), 1.3);
        f.validateStepForSuccess(firstEvent.get(5), "UpdateJvmCache");

        Assertions.assertEquals(6, secondEvent.size());

        f.validateParseEventToMessageDataStepForSuccess(secondEvent.get(0), -1, 28.656473, 77.242943, 1405595500);
        f.validateParseDateTimeStepForSuccess(secondEvent.get(1), LocalTime.of(16, 41, 40));
        f.validateStepForSuccess(secondEvent.get(2), "IsNewRideStep");
        f.validateCalculateSpeedStepForSuccess(secondEvent.get(3), 9.247896831969742);
        f.validateEventBasedFareCalculatorStepForSuccess(secondEvent.get(4), 5.015989905022082);
        f.validateStepForSuccess(secondEvent.get(5), "UpdateJvmCache");


        Assertions.assertEquals(9, thirdEvent.size());

        f.validateParseEventToMessageDataStepForSuccess(thirdEvent.get(0), -2, 28.612894, 77.229446, 1405594957);
        f.validateParseDateTimeStepForSuccess(thirdEvent.get(1), LocalTime.of(16, 32, 37));
        f.validateStepForSuccess(thirdEvent.get(2), "IsNewRideStep");
        f.validateStepForSuccess(thirdEvent.get(3), "FinalFareCalculationStep");
        f.validateStepForSuccess(thirdEvent.get(4), "SuccessFileCreationStep");
        f.validateStepForSuccess(thirdEvent.get(5), "ResetJvmCacheStep");
        f.validateCalculateSpeedStepForSuccess(thirdEvent.get(6), 0.0);
        f.validateEventBasedFareCalculatorStepForSuccess(thirdEvent.get(7), 1.3);
        f.validateStepForSuccess(thirdEvent.get(8), "UpdateJvmCache");
    }

    @DisplayName("Test FareEstimationWorkflow with ride having less than minimum fare")
    @Test
    @Order(4)
    void testFlowWithLessThanMinimumFareRideEventForFareEstimationWorkflow() throws ServiceException, IOException {
        Fixture f = new Fixture();
        f.resetData();

        // Create result
        List<ExecutorResponse<EventMetadata>> firstEvent = f.executor.executeWorkflow(new WorkflowMessage<>(f.eventMetadata, UUID.randomUUID().toString(), Optional.empty(), 3, Optional.empty()));
        List<ExecutorResponse<EventMetadata>> secondEvent = f.executor.executeWorkflow(new WorkflowMessage<>(f.eventMetadata2Replace, UUID.randomUUID().toString(), Optional.empty(), 3, Optional.empty()));
        List<ExecutorResponse<EventMetadata>> thirdEvent = f.executor.executeWorkflow(new WorkflowMessage<>(f.eventMetadata3, UUID.randomUUID().toString(), Optional.empty(), 3, Optional.empty()));

        // Verify result
        Assertions.assertEquals(6, firstEvent.size());

        f.validateParseEventToMessageDataStepForSuccess(firstEvent.get(0), -1, 28.612894, 77.229446, 1405594957);
        f.validateParseDateTimeStepForSuccess(firstEvent.get(1), LocalTime.of(16, 32, 37));
        f.validateStepForSuccess(firstEvent.get(2), "IsNewRideStep");
        f.validateCalculateSpeedStepForSuccess(firstEvent.get(3), 0.0);
        f.validateEventBasedFareCalculatorStepForSuccess(firstEvent.get(4), 1.3);
        f.validateStepForSuccess(firstEvent.get(5), "UpdateJvmCache");

        Assertions.assertEquals(6, secondEvent.size());

        f.validateParseEventToMessageDataStepForSuccess(secondEvent.get(0), -1, 28.612895, 77.229447, 1405594970);
        f.validateParseDateTimeStepForSuccess(secondEvent.get(1), LocalTime.of(16, 32, 50));
        f.validateStepForSuccess(secondEvent.get(2), "IsNewRideStep");
        f.validateCalculateSpeedStepForSuccess(secondEvent.get(3), 0.011381770128722993);
        f.validateEventBasedFareCalculatorStepForSuccess(secondEvent.get(4), 3.47);
        f.validateStepForSuccess(secondEvent.get(5), "UpdateJvmCache");


        Assertions.assertEquals(9, thirdEvent.size());

        f.validateParseEventToMessageDataStepForSuccess(thirdEvent.get(0), -2, 28.612894, 77.229446, 1405594957);
        f.validateParseDateTimeStepForSuccess(thirdEvent.get(1), LocalTime.of(16, 32, 37));
        f.validateStepForSuccess(thirdEvent.get(2), "IsNewRideStep");
        f.validateStepForSuccess(thirdEvent.get(3), "FinalFareCalculationStep");
        f.validateStepForSuccess(thirdEvent.get(4), "SuccessFileCreationStep");
        f.validateStepForSuccess(thirdEvent.get(5), "ResetJvmCacheStep");
        f.validateCalculateSpeedStepForSuccess(thirdEvent.get(6), 0.0);
        f.validateEventBasedFareCalculatorStepForSuccess(thirdEvent.get(7), 1.3);
        f.validateStepForSuccess(thirdEvent.get(8), "UpdateJvmCache");

        Files.delete(Paths.get("./success.csv"));
        Files.delete(Paths.get("./skipped.csv"));
    }
}
