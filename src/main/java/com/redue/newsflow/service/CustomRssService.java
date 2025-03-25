package com.redue.newsflow.service;

import com.redue.newsflow.dto.NewsArticle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CustomRssService {
    private static final String G1_URL = "https://g1.globo.com/";

    public List<NewsArticle> fetchG1News() {
        System.setProperty("webdriver.chromium.driver", "/drivers/chromedriver");

        List<NewsArticle> articlesList = new ArrayList<>();
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try {
            driver.get(G1_URL);

            for (int i = 0; i < 5; i++) {
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
                Thread.sleep(2000);
            }

            Document doc = Jsoup.parse(driver.getPageSource());
            Elements articles = doc.select(".feed-post-body");

            for (int i = 0; i < Math.min(5, articles.size()); i++) {
                String title = articles.get(i).select(".feed-post-link").text();
                String link = articles.get(i).select(".feed-post-link").attr("href");
                String thumbnail = articles.get(i).select(".feed-post-figure-link img").attr("src");

                articlesList.add(new NewsArticle(title, link, thumbnail));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        return articlesList;
    }
}
