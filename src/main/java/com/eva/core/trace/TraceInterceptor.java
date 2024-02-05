package com.eva.core.trace;

import com.alibaba.fastjson.JSON;
import com.eva.core.model.AppConfig;
import com.eva.core.constants.Constants;
import com.eva.core.model.ApiResponse;
import com.eva.core.model.LoginUserInfo;
import com.eva.core.servlet.ServletDuplicateInputStream;
import com.eva.core.servlet.ServletDuplicateOutputStream;
import com.eva.core.utils.Utils;
import com.eva.dao.system.model.SystemTraceLog;
import com.eva.service.system.SystemTraceLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 跟踪日志处理
 */
@Slf4j
@Component
public class TraceInterceptor implements HandlerInterceptor {

    @Resource
    private AppConfig projectConfig;

    private static final String ATTRIBUTE_TRACE_ID = "eva-trace-id";

    private static final String ATTRIBUTE_TRACE_TIME = "eva-trace-time";

    private static final int MAX_STORE_REQUEST_PARAM_SIZE = 1888;

    private static final int MAX_STORE_REQUEST_RESULT_SIZE = 1888;

    private static final int MAX_STORE_EXCEPTION_STACK_SIZE = 4888;

    private static final String MORE_DETAIL_STRING = "\n\n---------- more content is ignore here ... ----------\n\n";

    @Resource
    private SystemTraceLogService systemTraceLogService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            if (!(handler instanceof HandlerMethod) || !(request instanceof ShiroHttpServletRequest)) {
                return Boolean.TRUE;
            }
            if (!this.allowTrace(request, handler)) {
                return Boolean.TRUE;
            }
            SystemTraceLog traceLog = new SystemTraceLog();
            Date now = new Date();
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            Trace methodTrace = method.getAnnotation(Trace.class);
            Trace classTrace = method.getDeclaringClass().getAnnotation(Trace.class);
            // 获取跟踪类型
            TraceType traceType = methodTrace == null ? null : methodTrace.type();
            if (traceType == null) {
                traceType = this.smartType(request);
            }
            // 用户信息
            LoginUserInfo userInfo = (LoginUserInfo) SecurityUtils.getSubject().getPrincipal();
            if (userInfo != null) {
                traceLog.setUserId(userInfo.getId());
                traceLog.setUsername(userInfo.getUsername());
                traceLog.setUserRealName(userInfo.getRealName());
                traceLog.setUserRoles(StringUtils.join(userInfo.getRoles(), ","));
                traceLog.setUserPermissions(StringUtils.join(userInfo.getPermissions(), ","));
            }
            // 操作信息
            traceLog.setOperaModule(this.getModule(handler));
            traceLog.setOperaType(traceType.getType());
            traceLog.setOperaRemark(this.getOperaRemark(handler, traceType));
            traceLog.setOperaTime(now);
            // 请求信息
            traceLog.setRequestUri(request.getRequestURI());
            traceLog.setRequestMethod(request.getMethod());
            if (methodTrace == null || methodTrace.withRequestParameters() || (classTrace != null && classTrace.withRequestParameters())) {
                String requestParameters = request.getQueryString();
                if (HttpMethod.POST.matches(request.getMethod())) {
                    requestParameters = ((ServletDuplicateInputStream)request.getInputStream()).getBody();
                }
                traceLog.setRequestParams(
                        requestParameters != null && requestParameters.length() > MAX_STORE_REQUEST_PARAM_SIZE
                                ? requestParameters.substring(0, MAX_STORE_REQUEST_PARAM_SIZE) + MORE_DETAIL_STRING
                                : requestParameters
                );
            }
            // 辅助信息
            traceLog.setServerIp(Utils.Server.getIP());
            traceLog.setIp(Utils.User_Client.getIP(request));
            traceLog.setSystemVersion(projectConfig.getVersion());
            traceLog.setPlatform(Utils.User_Client.getPlatform(request));
            traceLog.setClientInfo(Utils.User_Client.getBrowser(request));
            traceLog.setOsInfo(Utils.User_Client.getOS(request));
            systemTraceLogService.create(traceLog);
            request.setAttribute(ATTRIBUTE_TRACE_ID, traceLog.getId());
            request.setAttribute(ATTRIBUTE_TRACE_TIME, now.getTime());
        } catch (Exception e) {
            log.warn("Eva @Trace throw an exception, you can get detail message by debug mode.");
            log.debug(e.getMessage(), e);
        }
        return Boolean.TRUE;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws IOException {
        // 获取跟踪ID
        Object traceId = request.getAttribute(ATTRIBUTE_TRACE_ID);
        Object traceTime = request.getAttribute(ATTRIBUTE_TRACE_TIME);
        request.removeAttribute(ATTRIBUTE_TRACE_ID);
        request.removeAttribute(ATTRIBUTE_TRACE_TIME);
        if (traceId == null) {
            return;
        }
        // 计算操作耗时
        SystemTraceLog traceLog = new SystemTraceLog();
        traceLog.setId(Integer.valueOf(traceId.toString()));
        traceLog.setOperaDuration(Integer.valueOf(String.valueOf(System.currentTimeMillis() - Long.parseLong(traceTime.toString()))));
        // 记录操作日志状态
        String operaType = response.getHeader(Constants.HEADER_OPERA_TYPE);
        // - 下载接口处理，无需记录响应内容
        if ("download".equals(operaType)) {
            handleDownloadResponse(traceLog, ex);
            return;
        }
        // - 返回json的接口处理
        handleJsonResponse(traceLog, response, handler, ex);
    }

    /**
     * JSON响应处理
     */
    private void handleJsonResponse(SystemTraceLog traceLog, HttpServletResponse response, Object handler, Exception ex) throws IOException {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        Trace methodTrace = method.getAnnotation(Trace.class);
        Trace classTrace = method.getDeclaringClass().getAnnotation(Trace.class);
        String responseBody = ((ServletDuplicateOutputStream) response.getOutputStream()).getContent();
        ApiResponse<?> apiResponse = null;
        try {
            apiResponse = JSON.parseObject(responseBody, ApiResponse.class);
        } catch (Exception ignored) {
        }
        // 记录响应结果
        if (methodTrace == null || methodTrace.withRequestResult() || (classTrace != null && classTrace.withRequestResult())) {
            String requestResult = responseBody;
            if (apiResponse != null) {
                // 排除exception信息
                String e = apiResponse.getException();
                apiResponse.setException(null);
                requestResult = JSON.toJSONString(apiResponse);
                apiResponse.setException(e);
            }
            traceLog.setRequestResult(
                    requestResult != null && requestResult.length() > MAX_STORE_REQUEST_RESULT_SIZE
                            ? requestResult.substring(0, MAX_STORE_REQUEST_RESULT_SIZE) + MORE_DETAIL_STRING
                            : requestResult
            );
        }
        // 请求成功
        if (ex == null && (apiResponse != null && apiResponse.isSuccess())) {
            traceLog.setStatus(TraceStatus.SUCCESS.getCode());
            systemTraceLogService.updateById(traceLog);
            return;
        }
        // 请求失败
        traceLog.setStatus(TraceStatus.FAILED.getCode());
        String exceptionStack = null;
        // 全局捕获到的未处理的异常
        if (apiResponse != null && apiResponse.getException() != null) {
            exceptionStack = apiResponse.getException();
            traceLog.setExceptionLevel(Constants.SystemTraceLog.EXCEPTION_LEVEL_DANGER);
        }
        // 其它异常情况
        if (exceptionStack == null) {
            // 未捕获到的未处理的异常
            if (ex != null) {
                StackTraceElement[] trace = ex.getStackTrace();
                StringBuilder exception = new StringBuilder(ex + "\n");
                for (StackTraceElement traceElement : trace) {
                    exception.append("\tat ").append(traceElement).append("\n");
                }
                exceptionStack = exception.toString();
                traceLog.setExceptionLevel(Constants.SystemTraceLog.EXCEPTION_LEVEL_DANGER);
            }
            // 业务异常
            else if (apiResponse != null) {
                traceLog.setExceptionLevel(Constants.SystemTraceLog.EXCEPTION_LEVEL_LOW);
                exceptionStack = apiResponse.getMessage();
            }
            // 响应格式非JSON的异常（在设计角度上，这类接口不应记录操作日志，此处做一个警告处理）
            else {
                traceLog.setExceptionLevel(Constants.SystemTraceLog.EXCEPTION_LEVEL_WARN);
                exceptionStack = responseBody;
            }
        }
        traceLog.setExceptionStack(
                exceptionStack != null && exceptionStack.length() > MAX_STORE_EXCEPTION_STACK_SIZE
                        ? exceptionStack.substring(0, MAX_STORE_EXCEPTION_STACK_SIZE) + MORE_DETAIL_STRING
                        : exceptionStack);
        systemTraceLogService.updateById(traceLog);
    }

    /**
     * 下载响应处理
     *
     * @param traceLog 跟踪日志
     * @param ex 异常
     */
    private void handleDownloadResponse (SystemTraceLog traceLog, Exception ex) {
        if (ex == null) {
            traceLog.setStatus(TraceStatus.SUCCESS.getCode());
            systemTraceLogService.updateById(traceLog);
            return;
        }
        // 出现异常
        traceLog.setStatus(TraceStatus.FAILED.getCode());
        StackTraceElement[] trace = ex.getStackTrace();
        StringBuilder exception = new StringBuilder(ex + "\n");
        for (StackTraceElement traceElement : trace) {
            exception.append("\tat ").append(traceElement).append("\n");
        }
        traceLog.setExceptionStack(exception.length() > MAX_STORE_EXCEPTION_STACK_SIZE
                ? exception.substring(0, MAX_STORE_EXCEPTION_STACK_SIZE) + MORE_DETAIL_STRING
                : exception.toString());
        traceLog.setExceptionLevel(Constants.SystemTraceLog.EXCEPTION_LEVEL_DANGER);
        systemTraceLogService.updateById(traceLog);
    }

    /**
     * 获取跟踪模块
     * 从Trace注解或Api注解中获取
     *
     * @param handler 处理器
     * @return String
     */
    private String getModule (Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 优先读取方法上的@Trace模块配置，如果无注解或无备注，则读取类上@Trace模块配置
        Trace trace = method.getAnnotation(Trace.class);
        if (trace == null || "".equals(trace.module())) {
            trace = method.getDeclaringClass().getAnnotation(Trace.class);
        }
        String module = "";
        if (trace != null && StringUtils.isNotBlank(trace.module())) {
            module = trace.module();
        }
        // 如果方法和类都未添加@Trace注解或没有添加模块配置，则从@Api注解中读取
        if (StringUtils.isBlank(module)) {
            Api api = method.getDeclaringClass().getAnnotation(Api.class);
            if (api != null) {
                module = StringUtils.join(api.tags(), ", ");
            }
        }
        return module;
    }

    /**
     * 获取操作备注
     *
     * @param handler 处理器
     * @param traceType 跟踪类型
     * @return String
     */
    private String getOperaRemark (Object handler, TraceType traceType) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 优先读取方法上的@Trace备注配置，如果无注解或无备注，则读取类上@Trace备注配置
        Trace trace = method.getAnnotation(Trace.class);
        if (trace != null && StringUtils.isNotBlank(trace.remark())) {
            return trace.remark();
        }
        // 如果方法和类都未添加@Trace注解或没有添加备注配置，从@ApiOperation注解中获取
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        if (apiOperation != null && StringUtils.isNotBlank(apiOperation.value())) {
            return apiOperation.value();
        }
        return traceType.getRemark();
    }

    /**
     * 是否允许跟踪
     *
     * @param request 请求对象
     * @param handler 处理器
     * @return Boolean
     */
    private Boolean allowTrace (HttpServletRequest request, Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        Trace methodTrace = method.getAnnotation(Trace.class);
        Trace classTrace = method.getDeclaringClass().getAnnotation(Trace.class);
        // 非智能模式，只有添加了@Trace注解才会允许跟踪
        if (!projectConfig.getTrace().getSmart()) {
            if (methodTrace != null) {
                return !methodTrace.exclude();
            }
            if (classTrace != null) {
                return !classTrace.exclude();
            }
            return Boolean.FALSE;
        }
        // 方法存在注解
        if (methodTrace != null) {
            return !methodTrace.exclude();
        }
        // 类存在注解
        if (classTrace != null) {
            return !classTrace.exclude();
        }
        // 匹配排除路径，如果匹配到，则不允许跟踪
        String[] patterns = projectConfig.getTrace().getExcludePatterns();
        if (patterns.length > 0) {
            String uri = request.getRequestURI();
            for (String pattern : patterns) {
                if (uri.matches(pattern.trim())) {
                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 获取跟踪类型
     *
     * @param request 请求对象
     * @return TraceType
     */
    private TraceType smartType (HttpServletRequest request) {
        // 批量删除
        if (request.getRequestURI().matches(smartPattern("delete/batch"))) {
            return TraceType.DELETE_BATCH;
        }
        // 删除
        if (request.getRequestURI().matches(smartPattern("delete"))) {
            return TraceType.DELETE;
        }
        // 新增
        if (request.getRequestURI().matches(smartPattern("create"))) {
            return TraceType.CREATE;
        }
        // 修改
        if (request.getRequestURI().matches(smartPattern("update"))) {
            return TraceType.UPDATE;
        }
        // 导入
        if (request.getRequestURI().matches(smartPattern("import"))) {
            return TraceType.IMPORT;
        }
        // 导出
        if (request.getRequestURI().matches(smartPattern("exportData"))) {
            return TraceType.EXPORT;
        }
        // 重置
        if (request.getRequestURI().matches(smartPattern("reset"))) {
            return TraceType.RESET;
        }
        return TraceType.UNKNOWN;
    }

    /**
     * 获取自动适配正则
     *
     * @param keyword 路径关键字
     * @return String
     */
    private String smartPattern (String keyword) {
        return ".+/" + keyword + "[a-zA-Z0-9\\-\\_]*$";
    }
}
