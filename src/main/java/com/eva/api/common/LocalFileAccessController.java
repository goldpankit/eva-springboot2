package com.eva.api.common;

import com.eva.api.BaseController;
import com.eva.core.trace.Trace;
import com.eva.core.constants.ResponseStatus;
import com.eva.core.exception.BusinessException;
import com.eva.core.utils.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Api(tags = "本地文件访问")
@Trace(exclude = true)
@RestController
@RequestMapping("/resource/local")
public class LocalFileAccessController extends BaseController {

    @GetMapping("/download")
    @ApiOperation("下载")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "文件路径", required = true, dataType = "String"),
    })
    public void download (@RequestParam String path, HttpServletResponse response) throws IOException {
        // 读取文件
        File file = new File(Utils.AppConfig.getLocalFileDirectory()
                .replace("~", new File("").getCanonicalPath()) + File.separator + path);
        if (!file.exists() || !file.isFile()) {
            throw new BusinessException(ResponseStatus.LOCAL_FILE_NOT_EXISTS);
        }
        // 获取文件字节流
        ByteArrayOutputStream os = this.getByteArrayOutputStream(Files.newInputStream(file.toPath()));
        // 下载
        this.downloadByteArray(file.getName(), os, response);
    }
}
