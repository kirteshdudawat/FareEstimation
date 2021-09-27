package com.kirtesh.fareestimation.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CustomFileWriter {

    private static File successFile;
    private static File skippedFile;

    // create FileWriter object with file as parameter
    private static FileWriter outputSuccessFile;
    private static FileWriter outputSkippedFile;

    private static BufferedWriter writerSuccessFile;
    private static BufferedWriter writerSkippedFile;

    public static void initialize(String successFilePath, String skippedFilePath) throws IOException {
        successFile = new File(successFilePath);
        skippedFile = new File(skippedFilePath);

        outputSuccessFile = new FileWriter(successFile);
        outputSkippedFile = new FileWriter(skippedFile);

        writerSuccessFile = new BufferedWriter(outputSuccessFile);
        writerSkippedFile = new BufferedWriter(outputSkippedFile);

        writeHeaders();
    }

    public static void writeHeaders() throws IOException {
        writerSuccessFile.write("id_ride,fare_estimate");
        writerSuccessFile.newLine();
        writerSuccessFile.flush();

        writerSkippedFile.write("id_ride,lat,lng,timestamp");
        writerSkippedFile.newLine();
        writerSkippedFile.flush();
    }

    public static void writeToSuccessFile(String data) throws IOException {
        writerSuccessFile.write(data);
        writerSuccessFile.newLine();
        writerSuccessFile.flush();
    }

    public static void writeToSkippedFile(String data) throws IOException {
        writerSkippedFile.write(data);
        writerSkippedFile.newLine();
        writerSkippedFile.flush();
    }

    public static void close() throws IOException {
        writerSkippedFile.close();
        writerSkippedFile.close();
    }
}
