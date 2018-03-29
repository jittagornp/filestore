/*
 * Copyright 2018 Pamarin.com
 */
package com.pamarin.filestore;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/03/10
 */
@FunctionalInterface
public interface StorePathFileRequestConverter {

    Output convert(FileRequest request);

    public static class Output {

        private String fullPath;

        private String parentPath;

        public String getFullPath() {
            return fullPath;
        }

        public void setFullPath(String fullPath) {
            this.fullPath = fullPath;
        }

        public String getParentPath() {
            return parentPath;
        }

        public void setParentPath(String parentPath) {
            this.parentPath = parentPath;
        }

    }

}
