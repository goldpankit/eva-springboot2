package com.eva.api.common;

import com.eva.api.BaseController;
import com.eva.core.model.ApiResponse;
import com.eva.core.trace.Trace;
import com.eva.core.utils.OSSUtil;
import com.eva.core.utils.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "OSS文件上传")
@Trace(exclude = true)
@RestController
@RequestMapping("/oss/upload")
public class OSSFileUploadController extends BaseController {

    @ApiOperation("上传图片，最大为5M")
    @PostMapping("/image")
    public ApiResponse<OSSUtil.UploadResult> uploadImage(MultipartFile file) {
        return ApiResponse.success(Utils.OSS.setMaxSize(1).uploadImage(file));
    }

    @ApiOperation("上传文件")
    @PostMapping("/attach")
    public ApiResponse<OSSUtil.UploadResult> uploadFile(MultipartFile file) {
        return ApiResponse.success(Utils.OSS.upload(file));
    }
}
