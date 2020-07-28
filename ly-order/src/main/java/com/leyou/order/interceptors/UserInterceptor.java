package com.leyou.order.interceptors;

import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import com.leyou.order.config.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UserInterceptor implements HandlerInterceptor {


    private JwtProperties prop;

    private static final ThreadLocal<UserInfo> TL=new ThreadLocal<>();

    public UserInterceptor(JwtProperties prop) {
        this.prop=prop;
    }

    public static UserInfo getUser() {
        return TL.get();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取cookie中的token
        String token= CookieUtils.getCookieValue(request,prop.getCookieName());
        try{
            //解析token
            UserInfo user= JwtUtils.getInfoFromToken(token,prop.getPublicKey());
            //传递user
           TL.set(user);
            //放行
            return true;
        }catch(Exception e){
            log.error("[购物车服务] 解析用户身份失败",e);
            return false;

        }



    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //数据用完后，一定要清空
        TL.remove();
    }
}
