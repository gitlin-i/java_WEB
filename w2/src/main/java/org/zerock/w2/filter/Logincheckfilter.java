package org.zerock.w2.filter;

import org.zerock.w2.dto.MemberDTO;
import org.zerock.w2.service.MemberService;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

import java.util.Arrays;
import java.util.Optional;


@WebFilter(urlPatterns = {"/todo/*"})
@Log4j2
public class Logincheckfilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
        log.info("Login check filter.....");

        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;

        HttpSession session = req.getSession();
        if (session.getAttribute("loginInfo") == null){
            resp.sendRedirect("/login");
            return;
        }



        Cookie cookie = findCookie(req.getCookies(),"remember-me");
        if(cookie == null){
            resp.sendRedirect("/login");
            return;
        }
        log.info("cookie는 존재하는 상황");
        String uuid = cookie.getValue();
        try{
            MemberDTO memberDTO = MemberService.INSTANCE.getByUUID(uuid);
            log.info("쿠키의 값으로 조회한 사용자 정보: " + memberDTO);
            if(memberDTO == null){
                throw new Exception("Cookie value is not valid");

            }

            session.setAttribute("loginInfo",memberDTO);
            chain.doFilter(request,response);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("/login");

        }

    }

    private Cookie findCookie(Cookie[] cookies, String name) {
        if (cookies == null || cookies.length == 0) {
            return null;

        }
        Optional<Cookie> result = Arrays.stream(cookies)
                .filter(ck -> ck.getName().equals(name))
                .findFirst();

        return result.isPresent()?result.get():null;

    }

}
