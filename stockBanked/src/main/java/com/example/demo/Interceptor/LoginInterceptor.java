package com.example.demo.Interceptor;

import com.example.demo.util.JWT;
import com.example.demo.util.ThreadLocalUntil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Slf4j
@Component  //交给spring容器管理
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");

        log.info(token);

        try {
            //校验token是否合法
            if(token==null){
                response.getWriter().write("NOT_LOGIN");
                return false;
            }
            Map<String, Object> claims = JWT.parseJWT(token);
            log.info(claims.toString());
//            Integer id = (Integer) claims.get("id");
            String username = (String) claims.get("username");
//            log.info("id:{}",id);
            log.info("username:{}",username);

//            System.out.println(claims.toString());
            ThreadLocalUntil.set(claims);
            return true;
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("token error.....");
//            response.setStatus(401);
            response.getWriter().write("NOT_LOGIN");
            //令牌异常
            return false;
        }
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}