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
public abstract class AccessPathFileRequestConverterAdapter implements AccessPathFileRequestConverter {

    protected abstract String getContext();

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

        if (!hasText(request.getBaseName())) {
            throw new IllegalArgumentException("require displayName.");
        }

        return getContext() + FileStore.ACCESS_PATH_FILE_FORMAT
                .replace("{createdDate}", FileStore.formatDate(request.getCreatedDate()))
                .replace("{uuid}", request.getUuid())
                .replace("{baseName}", request.getBaseName())
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

        newPath = newPath.substring(getContext().length());
        String[] fileSpit = org.apache.commons.lang.StringUtils.split(newPath, "/");
        if (fileSpit == null || fileSpit.length < 3) {
            throw new IllegalArgumentException("invalid path.");
        }

        return buildRequest(fileSpit, userId);
    }

    private FileRequest buildRequest(String[] fileSpit, String userId) {
        String createdDate = fileSpit[fileSpit.length - 3];
        if (!FileStore.isValidDate(createdDate)) {
            throw new IllegalArgumentException("invalid path, createdDate.");
        }

        String uuid = fileSpit[fileSpit.length - 2];
        if (!(hasText(uuid) && uuid.length() == 32)) {
            throw new IllegalArgumentException("invalid path, uuid.");
        }

        String name = fileSpit[fileSpit.length - 1];
        String[] nameSplit = org.apache.commons.lang.StringUtils.split(name, ".");
        if (nameSplit == null || nameSplit.length < 2) {
            throw new IllegalArgumentException("invalid path, uuid and extensionFile.");
        }

        try {
            FileRequest request = new FileRequest();
            request.setUuid(uuid);
            request.setCreatedDate(FileStore.parseDate(createdDate));
            request.setBaseName(nameSplit[0]);
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
