package com.eva.core.config.shiro;

import com.eva.core.model.AppConfig;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置
 */
@Configuration
public class ShiroConfig {

    @Value("${app.session.expire}")
    private int sessionExpire;

    @Resource
    private AppConfig projectConfig;

    @Resource
    private ShiroCredentialsMatcher shiroCredentialsMatcher;

    @Resource
    private ShiroSessionDAO shiroSessionDAO;

    @Resource
    private ShiroRealm shiroRealm;

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator autoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        autoProxyCreator.setProxyTargetClass(true);
        return autoProxyCreator;
    }

    @Bean
    public SessionManager sessionManager() {
        ShiroSessionManager sessionManager = new ShiroSessionManager();
        sessionManager.setSessionDAO(shiroSessionDAO);
        sessionManager.setGlobalSessionTimeout(sessionExpire * 1000L);
        return sessionManager;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm);
        securityManager.setSessionManager(this.sessionManager());
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> map = new LinkedHashMap<>();
        // 添加允许匿名访问的路径
        for (String pattern : projectConfig.getSession().getInterceptor().getExcludePathPatterns()) {
            map.put(pattern, "anon");
        }
        // 需要认证的接口
        for (String pattern : projectConfig.getSession().getInterceptor().getPathPatterns()) {
            map.put(pattern, "authc");
        }
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        // 添加认证过滤器
        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("authc", new ShiroAuthFilter());
        shiroFilterFactoryBean.setFilters(filters);
        return shiroFilterFactoryBean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public ShiroSessionDAO getShiroSessionDAO () {
        return shiroSessionDAO;
    }

    @Bean
    public ShiroRealm getShiroRealm () {
        shiroRealm.setCredentialsMatcher(shiroCredentialsMatcher);
        return shiroRealm;
    }
}
