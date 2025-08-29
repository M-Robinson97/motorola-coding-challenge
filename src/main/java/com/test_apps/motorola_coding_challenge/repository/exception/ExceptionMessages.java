package com.test_apps.motorola_coding_challenge.repository.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessages {
    public static final String GET_ALL_FILES_FAILS = "Could not retrieve file names";
    public static final String GET_FILE_FAILS = "Could not retrieve file";
    public static final String SAVE_FILE_FAILS = "Could not save file";
    public static final String DELETE_FILE_FAILS = "Could not delete file";

    public static final String FILE_NOT_FOUND_ERROR = "File Not Found";
    public static final String FILE_NAME_REQUIRED_ERROR = "File Name Required";
    public static final String FILE_SYSTEM_ERROR = "A storage error occurred";
    public static final String UNEXPECTED_ERROR = "An unexpected error occurred";
    public static final String INVALID_FILE_NAME_ERROR = "Invalid file name";
}
