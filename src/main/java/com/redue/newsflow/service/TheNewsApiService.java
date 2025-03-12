package com.redue.newsflow.service;

import com.redue.newsflow.api.TheNewsApiTopData;
import com.redue.newsflow.api.responses.TheNewsApiData;
import com.redue.newsflow.entities.User;
import com.redue.newsflow.entities.UserLocation;
import com.redue.newsflow.enums.RegionLanguage;
import com.redue.newsflow.repositories.UserLocationRepository;
import com.redue.newsflow.repositories.UserRepository;
import com.redue.newsflow.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Log4j2
public class TheNewsApiService {

    @Value("${thenewsapi.token}")
    private String token;

    private final RestTemplate restTemplate;

    private final UserRepository userRepository;

    private final UserLocationRepository repository;

    private final JwtUtils jwtUtils;

    private static final String BASE_URL = "https://api.thenewsapi.com/v1/news";

    public TheNewsApiData findAllNews(int page) {
        String finalApi = buildAPI("all", page, null, null, null);
        return fetchData(finalApi, TheNewsApiData.class);
    }

    public TheNewsApiData findByCategories(String categories, int page) {
        String finalApi = buildAPI("all", page, null, null, categories);
        return fetchData(finalApi, TheNewsApiData.class);
    }

    public TheNewsApiTopData findByTopNews(int page) {
        String isoCode = getUserIsoCode();
        String language = RegionLanguage.getLanguageForRegion(isoCode); // Obtenha o idioma a partir do isoCode
        String finalApi = buildAPI("top", page, language, isoCode, null);
        return fetchData(finalApi, TheNewsApiTopData.class);
    }

    public TheNewsApiData findBySearch(String search, int page) {
        String finalApi = buildAPI("all", page, null, null, null)
                .concat("&search=").concat(search)
                .concat("&sort=").concat("published_at");
        log.info(finalApi);
        return fetchData(finalApi, TheNewsApiData.class);
    }

    private String buildAPI(String endpoint, int page, String language, String locale, String categories) {
        StringBuilder url = new StringBuilder(BASE_URL)
                .append("/")
                .append(endpoint)
                .append("?api_token=")
                .append(token)
                .append("&page=")
                .append(page);


        if (language != null) {
            url.append("&language=").append(language);
        }

        if (locale != null) {
            url.append("&locale=").append(locale);
        }

        if (categories != null) {
            url.append("&categories=").append(categories);
        }

        return url.toString();
    }


    private String getUserIsoCode() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            log.info("User logged: {}", authentication.getPrincipal());
            return "us";
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado: " + username));

        return repository.findByUser_Id(user.getId())
                .map(UserLocation::getIsoCode)
                .orElse("us");

    }

    private <T> T fetchData(String url, Class<T> responseType) {
        return restTemplate.getForObject(url, responseType);
    }

   /* private String getIsoCodeForLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "US";
        }

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado: " + authentication.getName()));

        UserLocation userLocation = repository.findByUser_Id(user.getId())
                .orElseThrow(() -> new IllegalStateException("Localização não encontrada para o usuário: " + user.getUsername()));

        return userLocation.getIsoCode();
    }
*/

}