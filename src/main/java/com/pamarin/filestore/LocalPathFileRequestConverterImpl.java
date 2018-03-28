/*
 * Copyright 2018 Pamarin.com
 */
package com.pamarin.filestore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;
import static org.springframework.util.StringUtils.hasText;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/03/10
 */
@Component
public class LocalPathFileRequestConverterImpl implements LocalPathFileRequestConverter {

    @Override
    public String convert(FileRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("require request.");
        }

        String uuid = request.getUuid();
        if (!(hasText(uuid) && uuid.length() == 32)) {
            return null;
        }

        if (request.getCreatedDate() == null) {
            return null;
        }

        return FileConf.LOCAL_PATH_FILE_FORMAT.replace("{userId}", request.getUserId())
                .replace("{createdDate}", FileConf.formatDate(request.getCreatedDate()))
                .replace("{uuid}", request.getUuid());
    }
}
