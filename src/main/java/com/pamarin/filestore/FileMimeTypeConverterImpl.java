/*
 * Copyright 2018 Pamarin.com
 */
package com.pamarin.filestore;

import java.io.File;
import javax.activation.MimetypesFileTypeMap;
import org.springframework.stereotype.Component;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2018/03/28
 */
@Component
public class FileMimeTypeConverterImpl implements FileMimeTypeConverter {

    @Override
    public String convert(File file) {
        return MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(file);
    }

}
