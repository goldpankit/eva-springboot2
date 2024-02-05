package com.eva.core.servlet;

import lombok.Getter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 增加请求流副本
 */
@Getter
public class ServletDuplicateRequestWrapper extends HttpServletRequestWrapper {

    private ServletDuplicateInputStream servletDuplicateInputStream;

    private final Map<String, String[]> parameters = new HashMap<>();

    public ServletDuplicateRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * 添加参数
     */
    public void addParameter (String name, Object value) {
        if (value == null) {
            return;
        }
        if (value instanceof String[]) {
            this.parameters.put(name, (String[]) value);
        } else if (value instanceof String) {
            this.parameters.put(name, new String[]{(String) value});
        } else {
            this.parameters.put(name, new String[]{String.valueOf(value)});
        }
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        if (this.parameters.isEmpty()) {
            return super.getParameterMap();
        }
        return this.parameters;
    }

    @Override
    public String[] getParameterValues(String name) {
        if (this.parameters.isEmpty()) {
            return super.getParameterValues(name);
        }
        return this.parameters.get(name);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        if (this.parameters.isEmpty()) {
            return super.getParameterNames();
        }
        return Collections.enumeration(this.parameters.keySet());
    }

    @Override
    public ServletInputStream getInputStream() throws IOException{
        if (servletDuplicateInputStream == null) {
            servletDuplicateInputStream = new ServletDuplicateInputStream(super.getInputStream());
        }
        return servletDuplicateInputStream;
    }

}
