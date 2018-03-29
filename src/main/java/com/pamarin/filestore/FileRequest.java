/*
 * Copyright 2018 Pamarin.com
 */
package com.pamarin.filestore;

import java.time.LocalDate;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2018/03/28
 */
public class FileRequest {

    private String userId;

    private LocalDate createdDate;

    private String uuid;

    private String extensionFile;

    private String baseName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getExtensionFile() {
        return extensionFile;
    }

    public void setExtensionFile(String extensionFile) {
        this.extensionFile = extensionFile;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

}
