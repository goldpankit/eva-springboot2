package com.eva.api.common;

import com.eva.api.BaseController;
import com.eva.core.trace.Trace;
import com.eva.core.model.ApiResponse;
import com.eva.service.common.CaptchaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "图片验证码接口")
@Trace(exclude = true)
@RestController
@RequestMapping("/common")
public class CaptchaController extends BaseController {

    @Resource
    private CaptchaService captchaService;

    @ApiOperation("获取图片验证码")
    @GetMapping("/captcha")
    public ApiResponse<CaptchaService.Captcha> getCaptcha() {
        return ApiResponse.success(captchaService.genCaptcha());
    }
}
