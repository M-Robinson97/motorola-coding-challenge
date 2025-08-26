package com.test_apps.motorola_coding_challenge.utility;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessages {
    public static final String GET_ALL_FILES_FAILS = "Could not retrieve file names";
    public static final String GET_FILE_FAILS = "Could not retrieve files";
    public static final String SAVE_FILE_FAILS = "Could not save file";
    public static final String DELETE_FILE_FAILS = "Could not delete file";

    public static final String FILE_NAME_REQUIRED = "File name is required";
    public static final String FILE_NOT_FOUND = "File not found or not readable";
}
