/*
 * Copyright 2018 Pamarin.com
 */

package com.pamarin.filestore;

import java.io.File;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt;  
 * create : 2018/03/28
 */
public interface FileMimeTypeConverter {

    String convert(File file);
    
}
