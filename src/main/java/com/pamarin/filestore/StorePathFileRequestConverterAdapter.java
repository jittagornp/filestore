/*
 * Copyright 2018 Pamarin.com
 */
package com.pamarin.filestore;

import static org.springframework.util.StringUtils.hasText;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/03/10
 */
public abstract class StorePathFileRequestConverterAdapter implements StorePathFileRequestConverter {

    protected abstract String getPrefix();

    @Override
    public String convert(FileRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("require request.");
        }

        String uuid = request.getUuid();
        if (!(hasText(uuid) && uuid.length() == 32)) {
            throw new IllegalArgumentException("invalid uuid.");
        }

        if (request.getCreatedDate() == null) {
            throw new IllegalArgumentException("require createdDate.");
        }

        if (!hasText(request.getExtensionFile())) {
            throw new IllegalArgumentException("require extensionFile.");
        }

        return getPrefix() + FileStore.STORE_PATH_FILE_FORMAT
                .replace("{userId}", request.getUserId())
                .replace("{createdDate}", FileStore.formatDate(request.getCreatedDate()))
                .replace("{uuid}", request.getUuid())
                .replace("{fileName}", FileStore.FILE_NAME)
                .replace("{extensionFile}", request.getExtensionFile());
    }
}
