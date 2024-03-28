package com.eva.biz.system;

import com.eva.core.exception.BusinessException;
import com.eva.core.model.LoginUserInfo;
import com.eva.core.constants.ResponseStatus;
import com.eva.core.utils.Utils;
import com.eva.biz.system.dto.LoginDTO;
import com.eva.dao.system.model.SystemLoginLog;
import com.eva.service.common.CaptchaService;
import com.eva.service.system.SystemLoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@Service
public class SystemLoginBiz {

    @Resource
    private CaptchaService captchaService;

    @Resource
    private SystemLoginLogService systemLoginLogService;

    /**
     * 密码登录
     *
     * @param dto 账号信息
     * @param request 请求对象
     * @return 会话ID
     */
    public String loginByPassword(LoginDTO dto, HttpServletRequest request) {
        SystemLoginLog loginLog = new SystemLoginLog();
        loginLog.setLoginUsername(dto.getUsername());
        loginLog.setLoginTime(new Date());
        loginLog.setSystemVersion(Utils.AppConfig.getVersion());
        loginLog.setIp(Utils.User_Client.getIP(request));
        loginLog.setLocation(Utils.Location.getLocationString(loginLog.getIp()));
        loginLog.setPlatform(Utils.User_Client.getPlatform(request));
        loginLog.setClientInfo(Utils.User_Client.getBrowser(request));
        loginLog.setOsInfo(Utils.User_Client.getOS(request));
        loginLog.setServerIp(Utils.Server.getIP());
        // 校验验证码
        try {
            captchaService.check(dto.getUuid(), dto.getCode());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            loginLog.setReason(e.getMessage().length() > 200 ? (e.getMessage().substring(0, 190) + "...") : e.getMessage());
            loginLog.setSuccess(Boolean.FALSE);
            systemLoginLogService.create(loginLog);
            throw e;
        }
        // 校验用户名和密码
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(dto.getUsername(), dto.getPassword());
        try {
            subject.login(token);
            loginLog.setUserId(((LoginUserInfo)subject.getPrincipal()).getId());
            loginLog.setSuccess(Boolean.TRUE);
            systemLoginLogService.create(loginLog);
            return (String)subject.getSession().getId();
        } catch (AuthenticationException e) {
            loginLog.setReason(e.getMessage().length() > 200 ? (e.getMessage().substring(0, 190) + "...") : e.getMessage());
            loginLog.setSuccess(Boolean.FALSE);
            systemLoginLogService.create(loginLog);
            if (e.getCause() instanceof BusinessException) {
                throw (BusinessException) e.getCause();
            }
            throw new BusinessException(ResponseStatus.ACCOUNT_INCORRECT);
        }
    }
}
