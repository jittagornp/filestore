/*
 * Copyright 2018 Pamarin.com
 */
package com.pamarin.filestore;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2018/03/28
 */
public abstract class FileUploaderAdapter implements FileUploader {

    @Autowired
    private LocalPathFileRequestConverter localPathFileRequestConverter;
    
    @Autowired
    private ApiPathFileRequestConverter apiPathFileRequestConverter;

    protected abstract FileManager getFileManager();

    protected abstract String getUserId();

    private String randomUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public UploadFileOutput upload(UploadFileInput input) throws IOException {
        try (InputStream inputStream = input.getInputStream()) {
            FileRequest request = convert(input);
            getFileManager().write(request, inputStream);
            return buildOutput(request, input);
        }
    }

    private FileRequest convert(UploadFileInput input) {
        FileRequest request = new FileRequest();
        request.setCreatedDate(LocalDate.now());
        request.setExtensionFile(FilenameUtils.getExtension(input.getFileName()));
        request.setUuid(randomUuid());
        request.setUserId(getUserId());
        return request;
    }

    private UploadFileOutput buildOutput(FileRequest request, UploadFileInput input) throws IOException {
        UploadFileOutput output = new UploadFileOutput();
        output.setDisplayName(input.getFileName());
        output.setFileSize(input.getFileSize());
        output.setDisplayFileSize(FileUtils.byteCountToDisplaySize(input.getFileSize()));
        output.setFileUrl(apiPathFileRequestConverter.convert(request));
        output.setFilePath(localPathFileRequestConverter.convert(request));
        output.setMimeType(input.getMimeType());
        output.setCreatedDate(LocalDateTime.now());
        output.setNumberOfPages(getNumberOfPages(request, input));
        return output;
    }

    private boolean isPdfFile(String extensionFile) {
        return "pdf".equalsIgnoreCase(extensionFile);
    }

    private Integer getNumberOfPages(FileRequest request, UploadFileInput input) throws IOException {
        if (isPdfFile(request.getExtensionFile())) {
            try (PDDocument document = PDDocument.load(input.getInputStream())) {
                return document.getNumberOfPages();
            } catch (IOException ex) {
                return null;
            }
        }

        return null;
    }
}
