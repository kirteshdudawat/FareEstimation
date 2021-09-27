package com.kirtesh.fareestimation.enums;

import java.util.HashMap;
import java.util.Map;

/*
 * author : kirtesh
 */
public enum ErrorCodes {
	INVALID(1001,"Some unknown error occuured"),
	INVALID_CONFIG_FILE(1002,"Config file is invalid"),

	UNABLE_TO_FETCH_TIMEZONE(1101, "Unable to fetch time zone from received coordignates in event");

	private static Map<Integer, ErrorCodes> codeToValueMap = new HashMap<Integer, ErrorCodes>();
	private Integer code;
	private String message;

	private ErrorCodes(int code, String message){
		this.code = code;
		this.message = message;
	}

	public Integer code() {
		return null == code ? INVALID.code() : code;
	}

	public String message() {
		return message;
	}

    static {
        for (ErrorCodes errorCode : ErrorCodes.values()) {
            codeToValueMap.put(errorCode.code, errorCode);
        }
    }
    
    public static ErrorCodes getValueof(Integer code) {
        if (codeToValueMap.containsKey(code))
            return codeToValueMap.get(code);
        return ErrorCodes.INVALID;
    }
}

