package com.kirtesh.fareestimation.utility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CustomFileWriterTest {

    @DisplayName("Test CustomFileWriter.writeToFile() for checking file successfully written")
    @Test
    void testWriteSuccess() throws IOException {
        // Create Data
        String successFile = "./success.csv";
        String skippedFile = "./skipped.csv";

        String successFileExpectedData = "id_ride,fare_estimate\n" +
                "Success,File\n";
        String skippedFileExpectedData = "id_ride,lat,lng,timestamp\n" +
                "Skipped,Event,In,File\n";
        // Process
        CustomFileWriter.initialize(successFile, skippedFile);
        CustomFileWriter.writeToSuccessFile("Success,File");
        CustomFileWriter.writeToSkippedFile("Skipped,Event,In,File");
        CustomFileWriter.close();

        // Verify result
        String successFileData = Files.readString(Paths.get(successFile));
        String skippedFileData = Files.readString(Paths.get(skippedFile));

        Assertions.assertEquals(successFileExpectedData, successFileData);
        Assertions.assertEquals(skippedFileExpectedData, skippedFileData);
    }
}
