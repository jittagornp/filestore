/*
 * Copyright 2018 Pamarin.com
 */
package com.pamarin.filestore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt;  
 * create : 2018/03/28
 */
public interface FileManager {

    boolean exist(FileRequest request);

    File read(FileRequest request) throws IOException;

    void write(FileRequest request, InputStream inputStream) throws IOException;

    boolean delete(FileRequest request) throws IOException;
    
    StorePathFileRequestConverter getStorePathFileRequestConverter();
}
