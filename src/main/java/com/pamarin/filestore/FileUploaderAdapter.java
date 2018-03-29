/*
 * Copyright 2018 Pamarin.com
 */
package com.pamarin.filestore;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import static org.springframework.util.StringUtils.hasText;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2018/03/28
 */
public abstract class FileUploaderAdapter implements FileUploader {

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

    private FileRequest convert(UploadFileInput input) throws UnsupportedEncodingException {
        FileRequest request = new FileRequest();
        request.setCreatedDate(LocalDate.now());
        request.setBaseName(encodeBaseName(FilenameUtils.getBaseName(input.getFileName())));
        request.setExtensionFile(FilenameUtils.getExtension(input.getFileName()));
        request.setUuid(randomUuid());
        request.setUserId(input.getUserId());
        return request;
    }

    private UploadFileOutput buildOutput(FileRequest request, UploadFileInput input) throws IOException {
        UploadFileOutput output = new UploadFileOutput();
        output.setDisplayName(input.getFileName());
        output.setFileSize(input.getFileSize());
        output.setDisplayFileSize(FileUtils.byteCountToDisplaySize(input.getFileSize()));
        output.setAccessPath(getAccessPathFileRequestConverter().convert(request));
        output.setStorePath(getFileManager().getStorePathFileRequestConverter().convert(request).getFullPath());
        output.setMimeType(input.getMimeType());
        output.setCreatedDate(LocalDateTime.now());
        output.setNumberOfPages(getNumberOfPages(request, input));
        output.setNumberOfPictures(getNumberOfPictures(input));
        output.setUserId(input.getUserId());
        return output;
    }

    private boolean isPdfFile(String mimeType) {
        return "application/pdf".equalsIgnoreCase(mimeType);
    }

    private Integer getNumberOfPages(FileRequest request, UploadFileInput input) throws IOException {
        if (isPdfFile(input.getMimeType())) {
            try (PDDocument document = PDDocument.load(getFileManager().read(request))) {
                return document.getNumberOfPages();
            } catch (IOException ex) {
                return null;
            }
        }

        return null;
    }

    private Integer getNumberOfPictures(UploadFileInput input) {
        if (!hasText(input.getMimeType())) {
            return null;
        }

        if (input.getMimeType().startsWith("image/")) {
            return 1;
        }
        return null;
    }

    private String encodeBaseName(String baseName) throws UnsupportedEncodingException {
        return baseName.replace(".", " ")
                .replace("(", " ")
                .replace(")", " ")
                .replace("[", " ")
                .replace("]", " ")
                .replace("{", " ")
                .replace("}", " ")
                .replaceAll("\\s+", "_");
    }
}
