package com.kirtesh.fareestimation;

import com.kirtesh.fareestimation.cache.JvmCache;
import com.kirtesh.fareestimation.cache.JvmPreviousEvent;
import com.kirtesh.fareestimation.enums.EventSource;
import com.kirtesh.fareestimation.exception.ServiceException;
import com.kirtesh.fareestimation.factory.RoutingFactory;
import com.kirtesh.fareestimation.models.EventMetadata;
import com.kirtesh.fareestimation.workflow.farecalculator.FareEstimationWorkflow;
import com.kirtesh.fareestimation.workflow.framework.Workflow;
import com.kirtesh.fareestimation.workflow.framework.WorkflowExecutor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class FareEstimation {

    private final static Logger logger = LogManager.getLogger(FareEstimation.class);
    private final static Logger successLogger = LogManager.getLogger("success");

    public static void main(String[] args) {
        String eventFilePath = "/Users/kirteshdudawat/Desktop/Files/paths.csv";

        try {
            JvmCache.loadCache();
        } catch (ServiceException e) {
            System.exit(1);
        }

        RoutingFactory routingFactory = new RoutingFactory(eventFilePath);

        Workflow<EventMetadata> workflow = new FareEstimationWorkflow();
        WorkflowExecutor<EventMetadata> executor = new WorkflowExecutor<>(workflow);

        long startTime = System.currentTimeMillis();

        routingFactory.getEventReader(EventSource.CSV).readEvents(executor);

        if (!JvmPreviousEvent.isEmpty()) {
            successLogger.info(JvmPreviousEvent.getEventMetadata().getRideId() + "," + Math.max(JvmPreviousEvent.getEventMetadata().getTotalFareIncludingCurrentEvent(), JvmCache.minimumRideFare));
        }
        long endTime = System.currentTimeMillis();

        logger.info("Completed Execution in " + (endTime - startTime) + " ms.");
    }
}
