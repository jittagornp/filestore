/*
 * Copyright 2018 Pamarin.com
 */
package com.pamarin.filestore;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt;
 * create : 2017/03/10
 */
@FunctionalInterface
public interface LocalPathFileRequestConverter {

    String convert(FileRequest request);

}
