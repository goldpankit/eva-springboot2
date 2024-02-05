package com.eva.api.common;

import com.eva.api.BaseController;
import com.eva.core.constants.Constants;
import com.eva.core.trace.Trace;
import com.eva.core.constants.ResponseStatus;
import com.eva.core.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Api(tags = "本地文件")
@Trace(exclude = true)
@RestController
@RequestMapping("/resource/local")
public class LocalFileAccessController extends BaseController {

    @Value("${resources.import-template:~/files}")
    private String resourcesPath;

    @GetMapping("/download")
    @ApiOperation("下载")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "文件路径", required = true, dataType = "String"),
    })
    public void download (@RequestParam String path, HttpServletResponse response) throws IOException {
        File file = new File(resourcesPath.replace("~", new File("").getCanonicalPath()) + path);
        if (!file.exists() || !file.isFile()) {
            throw new BusinessException(ResponseStatus.LOCAL_FILE_NOT_EXISTS);
        }
        ByteArrayOutputStream os = this.getOutputStream(Files.newInputStream(file.toPath()));
        String encodeFileName = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.toString());
        response.setHeader("Content-Disposition","attachment;filename=" + encodeFileName);
        response.setContentType("application/octet-stream");
        response.setHeader(Constants.HEADER_OPERA_TYPE, "download");
        response.setHeader(Constants.HEADER_DOWNLOAD_FILENAME, encodeFileName);
        response.getOutputStream().write(os.toByteArray());
    }
}
