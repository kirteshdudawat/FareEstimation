package com.kirtesh.fareestimation.utility;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidityUtilityTest {

    @DisplayName("Test ValidityUtility.validateInputs() for command line arguments")
    @Test
    void testIsBetweenWithValidCase() {
        String[] invalidArgs = {"arg1", "arg2"};
        String[] validArgs = {"./arg1", "./arg2", "./arg3"};

        assertFalse(ValidationUtility.validateInputs(invalidArgs));
        assertTrue(ValidationUtility.validateInputs(validArgs));
    }
}
