package com.example.farmwork.filter;

import cn.hutool.json.JSONUtil;
import com.example.common.constants.RedisConstants;
import com.example.common.utils.JwtUtils;
import com.example.farmwork.utils.SecurityUtils;
import com.example.base.domain.LoginDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @Classname JwtAuthticationFilter
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/20 9:17
 * @Created by 16537
 */

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    StringRedisTemplate redisTemplate;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> token = Optional.ofNullable(request.getHeader("token"));
        if(token.isPresent()){

            String parseToken = JwtUtils.parseToken(token.get());
            String key = RedisConstants.LOGIN_USER_PREFIX+parseToken;
            Optional<String> optionalS = Optional.ofNullable(redisTemplate.opsForValue().get(key));
            if(!optionalS.isPresent()){
                filterChain.doFilter(request,response);
                return;
            }
            redisTemplate.expire(key,RedisConstants.LOGIN_EXPIRATION_TIME, TimeUnit.MINUTES);
            LoginDetails loginDetails = (LoginDetails)JSONUtil.toBean(optionalS.get(), LoginDetails.class, true);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginDetails,null,loginDetails.getAuthorities());
            SecurityUtils.setAuthentication(usernamePasswordAuthenticationToken);

        }
        filterChain.doFilter(request,response);

    }
}
