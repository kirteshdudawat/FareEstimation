package com.kirtesh.fareestimation.utility;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class ValidationUtility {

    /**
     * Accept 3 paths in argument,
     * 1. for reading event from file.
     * 2. for writing successful ride and fare to a file.
     * 3. for writing skipped events to a file.
     */
    public static boolean validateInputs(String[] paths) {
        if (paths.length != 3) {
            return false;
        }

        for (String path: paths) {
            try {
                Paths.get(path);
            } catch (InvalidPathException | NullPointerException ex) {
                return false;
            }
        }

        return true;
    }
}
