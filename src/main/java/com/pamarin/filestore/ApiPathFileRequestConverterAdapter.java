/*
 * Copyright 2018 Pamarin.com
 */
package com.pamarin.filestore;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.format.DateTimeParseException;
import static org.springframework.util.StringUtils.hasText;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2018/03/28
 */
public abstract class ApiPathFileRequestConverterAdapter implements ApiPathFileRequestConverter {
    
    protected abstract String getApiPrefix();

    @Override
    public String convert(FileRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("require request.");
        }

        if (request.getCreatedDate() == null) {
            throw new IllegalArgumentException("require createdDate.");
        }

        if (!hasText(request.getUuid())) {
            throw new IllegalArgumentException("require uuid.");
        }

        if (!hasText(request.getExtensionFile())) {
            throw new IllegalArgumentException("require extensionFile.");
        }

        return getApiPrefix() + FileConf.API_PATH_FILE_FORMAT
                .replace("{createdDate}", FileConf.formatDate(request.getCreatedDate()))
                .replace("{uuid}", request.getUuid())
                .replace("{extensionFile}", request.getExtensionFile());
    }

    @Override
    public FileRequest convert(String path, String userId) {
        String newPath = path;
        if (!hasText(newPath)) {
            throw new IllegalArgumentException("invalid path.");
        }

        if (newPath.startsWith("http")) {
            newPath = getHttpPath(newPath);
        }

        newPath = newPath.substring(getApiPrefix().length());
        String[] fileSpit = org.apache.commons.lang.StringUtils.split(newPath, "/");
        if (fileSpit == null || fileSpit.length < 2) {
            throw new IllegalArgumentException("invalid path.");
        }

        return buildRequest(fileSpit, userId);
    }

    private FileRequest buildRequest(String[] fileSpit, String userId) {
        String createdDateString = fileSpit[fileSpit.length - 2];
        String name = fileSpit[fileSpit.length - 1];
        String[] nameSplit = org.apache.commons.lang.StringUtils.split(name, ".");
        if (nameSplit == null || nameSplit.length < 2) {
            throw new IllegalArgumentException("invalid path, uuid and extensionFile.");
        }

        String uuid = nameSplit[0];
        if (!(hasText(uuid) && uuid.length() == 32)) {
            throw new IllegalArgumentException("invalid path, uuid.");
        }

        try {
            FileRequest request = new FileRequest();
            request.setUuid(uuid);
            request.setCreatedDate(FileConf.parseDate(createdDateString));
            request.setExtensionFile(nameSplit[1]);
            request.setUserId(userId);
            return request;
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("invalid path, createdDate.");
        }
    }

    public static String getHttpPath(String url) {
        try {
            return new URL(url).getPath();
        } catch (MalformedURLException ex) {
            return null;
        }
    }
}
