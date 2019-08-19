package com.me.crawl.config;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CrawlerConfig {

    @Bean
    public CrawlConfig crawlConfig() {
        CrawlConfig config = new CrawlConfig();
        config.setMaxDepthOfCrawling(1);
        config.setPolitenessDelay(2000);
        config.setIncludeHttpsPages(true);
        config.setFollowRedirects(true);
        config.setCrawlStorageFolder("data/crawl");
        return config;
    }

    @Bean
    public WebDriver chromeDriver() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        ChromeOptions opt = new ChromeOptions();
        opt.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        return new ChromeDriver(opt);
    }
}
