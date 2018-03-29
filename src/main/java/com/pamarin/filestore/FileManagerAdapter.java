/*
 * Copyright 2018 Pamarin.com
 */
package com.pamarin.filestore;

import com.google.common.io.ByteStreams;
import org.apache.commons.io.FileUtils;

import java.io.*;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/03/10
 */
public abstract class FileManagerAdapter implements FileManager {

    protected abstract StorePathFileRequestConverter getStorePathFileRequestConverter();

    protected abstract String getRootPath();

    private File getStore() {
        File store = new File(getRootPath());
        if (!store.exists()) {
            store.mkdirs();
        }
        return store;
    }

    private File getDirectory(FileRequest request) throws IOException {
        String path = getStorePathFileRequestConverter().convert(request).getParentPath();
        File store = getStore();
        if (!store.exists()) {
            store.mkdirs();
        }
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(store, path);
    }

    private boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    @Override
    public boolean exist(FileRequest request) {
        try {
            return read(request) != null;
        } catch (IOException ex) {
            return false;
        }
    }

    @Override
    public File read(FileRequest request) throws IOException {
        File directory = getDirectory(request);
        if (!directory.exists()) {
            throw new FileNotFoundException("Not found file.");
        }
        File[] files = directory.listFiles();
        if (isEmpty(files)) {
            throw new FileNotFoundException("Not found file.");
        }
        return files[0];
    }

    @Override
    public void write(FileRequest request, InputStream inputStream) throws IOException {
        File directory = getDirectory(request);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, FileStore.FILE_NAME + "." + request.getExtensionFile());
        try (OutputStream outputStream = new FileOutputStream(file)) {
            ByteStreams.copy(inputStream, outputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    @Override
    public boolean delete(FileRequest request) throws IOException {
        File directory = getDirectory(request);
        if (!directory.exists()) {
            return false;
        }

        try {
            FileUtils.deleteDirectory(directory);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
