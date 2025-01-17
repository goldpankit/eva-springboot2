package com.eva.core.servlet;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 包含副本的输入流
 */
public class ServletDuplicateInputStream extends ServletInputStream {

    private final ServletInputStream stream;

    private ByteArrayInputStream duplicate;

    private String body;

    public ServletDuplicateInputStream (ServletInputStream servletInputStream) {
        this.stream = servletInputStream;
        this.setBody(this.getBodyString());
    }

    @Override
    public int read() {
        return duplicate.read();
    }

    @Override
    public boolean isFinished() {
        return stream.isFinished();
    }

    @Override
    public boolean isReady() {
        return stream.isReady();
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        stream.setReadListener(readListener);
    }

    /**
     * 获取请求体参数字符串
     */
    private String getBodyString() {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = cloneInputStream(this.stream);
            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 复制输入流
     */
    private InputStream cloneInputStream(ServletInputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buffer)) > -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byteArrayOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
        this.duplicate = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
    }
}
