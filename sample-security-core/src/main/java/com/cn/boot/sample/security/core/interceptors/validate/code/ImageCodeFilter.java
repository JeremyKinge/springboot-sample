package com.cn.boot.sample.security.core.interceptors.validate.code;

import com.cn.boot.sample.security.core.config.properties.SecurityProperties;
import com.cn.boot.sample.security.core.exception.CodeException;
import com.cn.boot.sample.security.core.util.CodeValidateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Chen Nan
 */
@Component
@Slf4j
public class ImageCodeFilter extends OncePerRequestFilter implements InitializingBean {

    private AntPathMatcher matcher = new AntPathMatcher();
    private Set<String> urls = new HashSet<>();

    private AuthenticationFailureHandler failureHandler;
    private SecurityProperties securityProperties;

    public ImageCodeFilter(AuthenticationFailureHandler failureHandler, SecurityProperties securityProperties) {
        this.failureHandler = failureHandler;
        this.securityProperties = securityProperties;
        initUrl();
    }

    private void initUrl() {
        log.info("【ImageCodeFilter】initUrl");
        String url = securityProperties.getCode().getImage().getUrl();
        urls.addAll(Arrays.asList(StringUtils.splitByWholeSeparatorPreserveAllTokens(url, ",")));
        String loginUri = "/authentication/form";
        urls.add(loginUri);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        boolean needCaptcha = false;
        for (String url : urls) {
            if (matcher.match(url, request.getRequestURI())) {
                needCaptcha = true;
                break;
            }
        }

        if (needCaptcha) {
            String captchaParamName = "captcha";
            String captcha = ServletRequestUtils.getStringParameter(request, captchaParamName);
            long expire = securityProperties.getCode().getImage().getExpire();
            if (!CodeValidateUtil.validate(request, captcha, expire)) {
                failureHandler.onAuthenticationFailure(request, response, new CodeException("验证码错误"));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
