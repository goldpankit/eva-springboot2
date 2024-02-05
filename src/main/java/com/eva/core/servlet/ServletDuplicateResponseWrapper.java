package com.eva.core.servlet;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 * 增加响应流副本
 */
@Slf4j
public class ServletDuplicateResponseWrapper extends HttpServletResponseWrapper {

    private ServletDuplicateOutputStream servletDuplicateOutputStream;

    public ServletDuplicateResponseWrapper(HttpServletResponse httpServletResponse) {
        super(httpServletResponse);
        try {
            servletDuplicateOutputStream = new ServletDuplicateOutputStream(httpServletResponse.getOutputStream());
        } catch (IOException e) {
            log.error("EVA: build ServletDuplicateResponseWrapper throw an exception", e);
        }
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return servletDuplicateOutputStream;
    }

}
