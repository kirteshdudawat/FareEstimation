package com.kirtesh.fareestimation.events.reader.csv;

import com.kirtesh.fareestimation.events.EventReader;
import com.kirtesh.fareestimation.models.EventMetadata;
import com.kirtesh.fareestimation.workflow.framework.WorkflowExecutor;
import com.kirtesh.fareestimation.workflow.framework.dtos.WorkflowMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class CsvEventReader implements EventReader<EventMetadata> {

    private String csvPath;

    public CsvEventReader(String csvPath) {
        this.csvPath = csvPath;
    }

    public void setCsvPath(String csvPath) {
        this.csvPath = csvPath;
    }

    @Override
    public void readEvents(WorkflowExecutor<EventMetadata> executor) {
        try (Stream<String> stream = Files.lines(Paths.get(csvPath))) {
            stream.forEach( data -> {
                EventMetadata metadata = new EventMetadata(data);
                executor.executeWorkflow(new WorkflowMessage<>(metadata, UUID.randomUUID().toString(), Optional.empty(), 3, Optional.empty()));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
