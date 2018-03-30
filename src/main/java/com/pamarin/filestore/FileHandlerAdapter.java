/*
 * Copyright 2018 Pamarin.com
 */
package com.pamarin.filestore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/03/10
 */
public abstract class FileHandlerAdapter {

    @Autowired
    private FileMimeTypeConverter fileMimeTypeConverter;

    protected abstract FileUploader getFileUploader();

    protected abstract String getUserId(HttpServletRequest httpReq);

    private String decideUserId(HttpServletRequest httpReq) {
        if (httpReq.getParameter("token") != null) {
            try {
                return verifyGrantToken(httpReq.getParameter("token"));
            } catch (Exception ex) {
                return getUserId(httpReq);
            }
        }

        return getUserId(httpReq);
    }

    public String signGrantToken(String userId) {
        throw new UnsupportedOperationException("not support signGrantToken(String userId).");
    }

    public String verifyGrantToken(String token) {
        throw new UnsupportedOperationException("not support verifyGrantToken(String token).");
    }

    private FileRequest convert(HttpServletRequest httpReq, String userId) {
        return getFileUploader().getAccessPathFileRequestConverter()
                .convert(httpReq.getServletPath(), userId);
    }

    private String data(String attribute, Object value) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put(attribute, value);
        return new ObjectMapper().writeValueAsString(map);
    }

    @ResponseBody
    @GetMapping(value = "/{createdDate}/{uuid}/{name}", params = "exist")
    public void existFile(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        httpResp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        FileRequest request = convert(httpReq, decideUserId(httpReq));//allow for token
        httpResp.getWriter().print(data("existed", getFileUploader().getFileManager().exist(request)));
    }

    @ResponseBody
    @DeleteMapping("/{createdDate}/{uuid}/{name}")
    public void deleteFile(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        httpResp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        FileRequest request = convert(httpReq, getUserId(httpReq));
        httpResp.getWriter().print(data("deleted", getFileUploader().getFileManager().delete(request)));
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
    public void getFile(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        try {
            FileRequest request = convert(httpReq, decideUserId(httpReq));//allow for token
            File file = getFileUploader().getFileManager().read(request);
            setHeader(file, request.getBaseName() + "." + request.getExtensionFile(), httpReq, httpResp);
            try (InputStream inputStream = new FileInputStream(file); OutputStream outputStream = httpResp.getOutputStream()) {
                ByteStreams.copy(inputStream, outputStream);
            }
        } catch (IOException ex) {
            httpResp.setContentType(MediaType.TEXT_HTML_VALUE);
            httpResp.setCharacterEncoding("utf-8");
            httpResp.getWriter().print("ไม่พบไฟล์ข้อมูล");
            httpResp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @ResponseBody
    @PostMapping(value = "/{createdDate}/{uuid}/{name}", params = "share")
    public void shareFile(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        httpResp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        httpResp.getOutputStream().print(data("link", httpReq.getRequestURI() + "?token=" + signGrantToken(getUserId(httpReq))));
    }

    @ResponseBody
    @PostMapping("/upload")
    public UploadFileOutput uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest httpReq) throws IOException {
        return getFileUploader().upload(convert(file, httpReq));
    }

    private UploadFileInput convert(MultipartFile file, HttpServletRequest httpReq) throws IOException {
        UploadFileInput input = new UploadFileInput();
        input.setFileName(file.getOriginalFilename());
        input.setFileSize(file.getSize());
        input.setInputStream(file.getInputStream());
        input.setMimeType(file.getContentType());
        input.setUserId(getUserId(httpReq));
        return input;
    }
}
