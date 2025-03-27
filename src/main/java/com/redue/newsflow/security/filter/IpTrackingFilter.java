package com.redue.newsflow.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Component
@WebFilter("/*")
public class IpTrackingFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();

        String ipAddress = httpRequest.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            System.out.println(ipAddress);
        }

        // Verifica se já temos o ISO Code salvo na sessão
        String isoCode = (String) session.getAttribute("userCountry");
        if (isoCode == null) {
            isoCode = getCountryIsoCode(ipAddress); 
            session.setAttribute("userCountry", isoCode);
        }

        chain.doFilter(request, response);
    }

    private String getCountryIsoCode(String ip) {
        try {
            String apiUrl = "https://ipinfo.io/" + "45.234.138.189" + "/json";
            //US: 65.29.148.75
            //BR: 45.234.138.189
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject json = new JSONObject(response.toString());
            return json.getString("country");

        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }
}
