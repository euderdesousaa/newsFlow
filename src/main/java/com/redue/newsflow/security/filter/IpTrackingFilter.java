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
import java.util.HashMap;
import java.util.Map;

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

        String isoCode = (String) session.getAttribute("userCountry");
        String city = (String) session.getAttribute("userCity");

        if (isoCode == null || city == null) {
            Map<String, String> locationData = getLocationData(ipAddress);
            isoCode = locationData.get("country");
            city = locationData.get("city");

            session.setAttribute("userCountry", isoCode);
            session.setAttribute("userCity", city);
        }

        chain.doFilter(request, response);
    }

    private Map<String, String> getLocationData(String ip) {
        Map<String, String> location = new HashMap<>();
        try {
            String apiUrl = "https://ipinfo.io/" + ip + "/json";
            
            JSONObject json = getJsonObject(apiUrl);
            location.put("city", json.getString("city"));
            location.put("country", json.getString("country"));

        } catch (Exception e) {
            e.printStackTrace();
            location.put("city", "Unknown");
            location.put("country", "Unknown");
        }
        return location;
    }

    private static JSONObject getJsonObject(String apiUrl) throws IOException {
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
        return json;
    }
}
