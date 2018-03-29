/*
 * Copyright 2018 Pamarin.com
 */
package com.pamarin.filestore;

import java.io.IOException;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt;
 * create : 2017/06/05
 */
public interface FileUploader {

    UploadFileOutput upload(UploadFileInput input) throws IOException;
    
    FileManager getFileManager();
    
    AccessPathFileRequestConverter getAccessPathFileRequestConverter();
}
