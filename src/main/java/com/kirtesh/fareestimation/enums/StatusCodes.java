package com.kirtesh.fareestimation.enums;

import java.util.ArrayList;
import java.util.List;

public enum StatusCodes {
    SUCCESS("SUCCESS"),
    PARSING_ERROR__MISSING_EVENT_METADATA("PARSING_ERROR__MISSING_EVENT_METADATA"),
    PARSING_ERROR__INVALID_EVENT_METADATA_FORMAT("PARSING_ERROR__INVALID_EVENT_METADATA_FORMAT"),
    INVALID_LOCATION_PROVIDED("INVALID_LOCATION_PROVIDED"),
    UNABLE_TO_FETCH_ZONE_ID("UNABLE_TO_FETCH_ZONE_ID"),
    UNKNOWN("UNKNOWN");

    private static List<String> statusCodeList = new ArrayList<>();
    private String _statusCode;

    StatusCodes(String _statusCode) {
        this._statusCode = _statusCode;
    }

    public String getStatusCode() {
        return _statusCode;
    }

    static {
        for(StatusCodes statusCode : StatusCodes.values()) {
            statusCodeList.add(statusCode._statusCode);
        }
    }

    public static StatusCodes fromString(String keyAsString) {
        for (StatusCodes statusKey : StatusCodes.values()) {
            if (statusKey._statusCode.equalsIgnoreCase(keyAsString)) {
                return statusKey;
            }
        }
        return StatusCodes.UNKNOWN;
    }
}