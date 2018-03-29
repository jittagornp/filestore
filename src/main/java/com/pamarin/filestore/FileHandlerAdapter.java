/*
 * Copyright 2018 Pamarin.com
 */
package com.pamarin.filestore;

import com.google.common.io.ByteStreams;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/03/10
 */
public abstract class FileHandlerAdapter {

    @Autowired
    private FileMimeTypeConverter fileMimeTypeConverter;

    protected abstract FileUploader getFileUploader();

    protected abstract String getUserId();

    private FileRequest convert(String path) {
        return getFileUploader().getAccessPathFileRequestConverter().convert(path, getUserId());
    }

    @ResponseBody
    @GetMapping(value = "/{createdDate}/{uuid}/{name}", params = "exist")
    public void existFile(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        httpResp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        FileRequest request = convert(httpReq.getServletPath());
        httpResp.getWriter().print(getFileUploader().getFileManager().exist(request));
    }

    @ResponseBody
    @DeleteMapping("/{createdDate}/{uuid}/{name}")
    public void deleteFile(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        httpResp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        FileRequest request = convert(httpReq.getServletPath());
        httpResp.getWriter().print(getFileUploader().getFileManager().delete(request));
    }

    private void setHeader(File file, String fileName, HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        fileName = URLEncoder.encode(fileName, "utf-8");
        if (httpReq.getParameter("preview") != null) {
            httpResp.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
        } else {
            httpResp.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        }

        httpResp.setCharacterEncoding("utf-8");
        httpResp.setHeader("language", "th-TH");//***
        httpResp.setContentType(fileMimeTypeConverter.convert(file));
    }

    @ResponseBody
    @GetMapping("/{createdDate}/{uuid}/{name}")
    public void loadFile(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        try {
            FileRequest request = convert(httpReq.getServletPath());
            File file = getFileUploader().getFileManager().read(request);
            setHeader(file, request.getBaseName() + "." + request.getExtensionFile(), httpReq, httpResp);
            try (InputStream inputStream = new FileInputStream(file); OutputStream outputStream = httpResp.getOutputStream()) {
                ByteStreams.copy(inputStream, outputStream);
            }
        } catch (IOException ex) {
            httpResp.setContentType("text/html");
            httpResp.setCharacterEncoding("utf-8");
            httpResp.getWriter().print("ไม่พบไฟล์ข้อมูล");
            httpResp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @ResponseBody
    @PostMapping("/upload")
    public UploadFileOutput uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return getFileUploader().upload(convert(file));
    }

    private UploadFileInput convert(MultipartFile file) throws IOException {
        UploadFileInput input = new UploadFileInput();
        input.setFileName(file.getOriginalFilename());
        input.setFileSize(file.getSize());
        input.setInputStream(file.getInputStream());
        input.setMimeType(file.getContentType());
        input.setUserId(getUserId());
        return input;
    }
}
