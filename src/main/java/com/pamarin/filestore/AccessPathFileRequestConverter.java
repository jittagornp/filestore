/*
 * Copyright 2018 Pamarin.com
 */
package com.pamarin.filestore;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt;
 * create : 2017/03/10
 */
public interface AccessPathFileRequestConverter {

    String convert(FileRequest request);

    FileRequest convert(String path, String userId);

}
