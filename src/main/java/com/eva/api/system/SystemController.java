package com.eva.api.system;

import com.eva.api.BaseController;
import com.eva.biz.system.SystemBiz;
import com.eva.biz.system.SystemMenuBiz;
import com.eva.biz.system.SystemUserBiz;
import com.eva.core.model.ClientConfig;
import com.eva.core.trace.Trace;
import com.eva.core.model.ApiResponse;
import com.eva.core.model.LoginUserInfo;
import com.eva.biz.system.dto.LoginDTO;
import com.eva.biz.system.dto.UpdatePwdDTO;
import com.eva.dao.system.vo.SystemMenuNodeVO;
import com.eva.biz.system.SystemLoginBiz;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "系统功能")
@Trace(exclude = true)
@Slf4j
@RestController
@RequestMapping("/system")
public class SystemController extends BaseController {

    @Resource
    private SystemUserBiz systemUserBiz;

    @Resource
    private SystemLoginBiz systemLoginBiz;

    @Resource
    private SystemMenuBiz systemMenuBiz;

    @Resource
    private SystemBiz systemBiz;

    @ApiOperation("登录")
    @PostMapping("/login")
    public ApiResponse<String> login (@RequestBody LoginDTO dto, HttpServletRequest request) {
        return ApiResponse.success(systemLoginBiz.loginByPassword(dto, request));
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public ApiResponse<?> logout () {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ApiResponse.success(null);
    }

    @Trace(withRequestParameters = false)
    @ApiOperation("修改当前用户密码")
    @PostMapping("/updatePwd")
    public ApiResponse<?> updatePwd (@RequestBody UpdatePwdDTO dto) {
        systemUserBiz.updatePwd(dto);
        return ApiResponse.success(null);
    }

    @ApiOperation("获取当前登录的用户信息")
    @GetMapping("/getUserInfo")
    public ApiResponse<LoginUserInfo> getUserInfo () {
        return ApiResponse.success(this.getLoginUser());
    }

    @ApiOperation("查询用户菜单")
    @GetMapping("/menus")
    public ApiResponse<List<SystemMenuNodeVO>> getUserMenus () {
        return ApiResponse.success(systemMenuBiz.findUserMenus());
    }

    @GetMapping("/client/config")
    @ApiOperation("获取客户端配置")
    public ApiResponse<ClientConfig> getClientConfig () {
        return ApiResponse.success(systemBiz.getClientConfig());
    }
}
