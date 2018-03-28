/*
 * Copyright 2018 Pamarin.com
 */
package com.pamarin.filestore;

import java.time.LocalDateTime;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/03/10
 */
public class UploadFileOutput {

    private String displayName;

    private String mimeType;

    private Long fileSize;

    private String displayFileSize;

    private String fileUrl;

    private String filePath;

    private LocalDateTime createdDate;

    private Integer numberOfPages;

    private Integer numberOfPictures;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getDisplayFileSize() {
        return displayFileSize;
    }

    public void setDisplayFileSize(String displayFileSize) {
        this.displayFileSize = displayFileSize;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getNumberOfPages() {
        if (numberOfPages == null) {
            numberOfPages = 0;
        }
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public Integer getNumberOfPictures() {
        if (numberOfPictures == null) {
            numberOfPictures = 0;
        }
        return numberOfPictures;
    }

    public void setNumberOfPictures(Integer numberOfPictures) {
        this.numberOfPictures = numberOfPictures;
    }

}
