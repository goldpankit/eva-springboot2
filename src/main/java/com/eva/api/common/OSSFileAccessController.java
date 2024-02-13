package com.eva.api.common;

import com.eva.api.BaseController;
import com.eva.core.utils.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Api(tags = "OSS文件")
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
    public void downloadFile(@RequestParam(name = "f") String fileKey, HttpServletResponse response) throws IOException {
        InputStream is = Utils.OSS.download(fileKey);
        ByteArrayOutputStream os = this.getByteArrayOutputStream(is);
        this.downloadByteArray(fileKey, os, response);
    }
}
