/*
 * Copyright 2018 Pamarin.com
 */
package com.pamarin.filestore;

import java.io.InputStream;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2018/03/28
 */
public class UploadFileInput {

    private InputStream inputStream;

    private String fileName;

    private long fileSize;

    private String mimeType;

    private String userId;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
