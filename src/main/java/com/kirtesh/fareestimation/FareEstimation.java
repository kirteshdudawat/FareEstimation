package com.kirtesh.fareestimation;

import com.kirtesh.fareestimation.cache.JvmCache;
import com.kirtesh.fareestimation.cache.JvmPreviousEvent;
import com.kirtesh.fareestimation.enums.EventSource;
import com.kirtesh.fareestimation.exception.ServiceException;
import com.kirtesh.fareestimation.factory.RoutingFactory;
import com.kirtesh.fareestimation.models.EventMetadata;
import com.kirtesh.fareestimation.utility.CustomFileWriter;
import com.kirtesh.fareestimation.utility.ValidationUtility;
import com.kirtesh.fareestimation.workflow.farecalculator.FareEstimationWorkflow;
import com.kirtesh.fareestimation.workflow.framework.Workflow;
import com.kirtesh.fareestimation.workflow.framework.WorkflowExecutor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

public class FareEstimation {

    private final static Logger logger = LogManager.getLogger(FareEstimation.class);

    public static void main(String[] args) throws IOException {

        if (!ValidationUtility.validateInputs(args)) {
            logger.error("Invalid file parameters provided, please provide correct file parameters.");
            System.exit(1);
        }

        String inputEventFilePath = args[0];
        String successEventFilePath = args[1];
        String skippedEventFilePath = args[2];

        try {
            JvmCache.loadCache();
        } catch (ServiceException e) {
            logger.error("Invalid configurations provided.");
            System.exit(1);
        }

        CustomFileWriter.initialize(successEventFilePath, skippedEventFilePath);
        processInputEvents(inputEventFilePath);
        CustomFileWriter.close();
    }

    public static void processInputEvents(String inputFilePath) throws IOException {
        RoutingFactory routingFactory = new RoutingFactory(inputFilePath);
        Workflow<EventMetadata> workflow = new FareEstimationWorkflow();
        WorkflowExecutor<EventMetadata> executor = new WorkflowExecutor<>(workflow);


        long startTime = System.currentTimeMillis();
        logger.info("Starting to process execution  file");

        routingFactory.getEventReader(EventSource.CSV).readEvents(executor);

        if (!JvmPreviousEvent.isEmpty()) {
            CustomFileWriter.writeToSuccessFile(JvmPreviousEvent.getEventMetadata().getRideId() + "," + Math.max(JvmPreviousEvent.getEventMetadata().getTotalFareIncludingCurrentEvent(), JvmCache.minimumRideFare));
        }

        long endTime = System.currentTimeMillis();
        logger.info("Completed Execution in " + (endTime - startTime) + " ms.");
    }
}
