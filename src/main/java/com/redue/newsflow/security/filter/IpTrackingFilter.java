package com.redue.newsflow.security.filter;

import com.redue.newsflow.component.SessionIpStore;
import com.redue.newsflow.service.AccessLogService;
import com.redue.newsflow.service.GeoLocationService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Component
@WebFilter("/*")
public class IpTrackingFilter implements Filter {

    private final SessionIpStore sessionIpStore;

    private final AccessLogService accessLogService;
    
    
    private static final Logger log = Logger.getLogger(IpTrackingFilter.class.getName());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String ipAddress = httpRequest.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        sessionIpStore.saveIp(httpRequest.getRemoteAddr(), ipAddress);

        accessLogService.saveAccessLog(ipAddress, ipAddress, "Desconhecido"); // Pode integrar com API para pegar país

        chain.doFilter(request, response);
    }
}
