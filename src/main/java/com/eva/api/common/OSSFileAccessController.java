package com.eva.api.common;

import com.eva.api.BaseController;
import com.eva.core.trace.Trace;
import com.eva.core.utils.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Api(tags = "OSS文件访问")
@Trace(exclude = true)
@RestController
@RequestMapping("/resource/oss")
public class OSSFileAccessController extends BaseController {

    @ApiOperation("访问图片")
    @GetMapping("/image")
    public void accessImage (@RequestParam(name = "f") String fileKey, HttpServletResponse response) throws IOException {
        InputStream is = Utils.OSS.download(fileKey);
        ByteArrayOutputStream os = this.getByteArrayOutputStream(is);
        response.setContentType("image/jpeg");
        response.getOutputStream().write(os.toByteArray());
    }

    @ApiOperation("下载文件")
    @GetMapping("/attach")
    public void downloadFile(
            @RequestParam(name = "f") String fileKey,
            @RequestParam(name = "fn") String filename,
            HttpServletResponse response) throws IOException {
        InputStream is = Utils.OSS.download(fileKey);
        ByteArrayOutputStream os = this.getByteArrayOutputStream(is);
        this.downloadByteArray(StringUtils.isBlank(filename) ? fileKey : filename, os, response);
    }
}
