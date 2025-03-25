package com.redue.newsflow.security.filter;

import com.redue.newsflow.component.SessionIpStore;
import com.redue.newsflow.service.AccessLogService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.logging.Logger;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@WebFilter("/*")
public class IpTrackingFilter implements Filter {

    private final SessionIpStore sessionIpStore;

    private final AccessLogService accessLogService;
    
    private static final Logger log = Logger.getLogger(IpTrackingFilter.class.getName());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();

        String ip = httpRequest.getRemoteAddr(); // Ou pegar do "X-Forwarded-For"
        log.info("IP Capturado: " + ip);
        
        sessionIpStore.saveIp(session.getId(), ip);

        accessLogService.saveAccessLog(session.getId(), ip, "Desconhecido"); // Pode integrar com API para pegar país

        chain.doFilter(request, response);
    }
}
