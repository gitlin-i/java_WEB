package org.zerock.w2.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.log4j.Log4j2;

@WebListener
@Log4j2
public class W2AppListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce){
        log.info("-1---------------init---------");

        ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute("appName","w2");

    }
    @Override
    public void contextDestroyed(ServletContextEvent sce){
        log.info("-1-----------destroyed----------");

    }
}
