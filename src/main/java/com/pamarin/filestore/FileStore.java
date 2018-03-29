/*
 * Copyright 2018 Pamarin.com
 */
package com.pamarin.filestore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2018/03/28
 */
public final class FileStore {

    public static final String FILE_NAME = "file";

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String STORE_PATH_FILE_FORMAT = "/{userId}/{createdDate}/{uuid}/{fileName}.{extensionFile}";

    public static final String ACCESS_PATH_FILE_FORMAT = "/{createdDate}/{uuid}.{extensionFile}";

    
    private FileStore(){
        
    }
    
    public static String formatDate(LocalDate createdDate) {
        if (createdDate == null) {
            throw new IllegalArgumentException("require createdDate.");
        }
        return createdDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static LocalDate parseDate(String createdDate) {
        if (createdDate == null) {
            throw new IllegalArgumentException("require createdDate.");
        }
        return LocalDate.parse(createdDate, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
}
