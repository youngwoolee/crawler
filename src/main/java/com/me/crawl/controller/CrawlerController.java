package com.me.crawl.controller;

import com.me.crawl.AirBnbCrawler;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/crawl")
@RequiredArgsConstructor
public class CrawlerController {

    private static final String URL = "https://www.airbnb.co.kr/s/강남역/all?refinement_paths%5B%5D=%2Ffor_you&search_type=search_query&place_id=ChIJKxs2jFmhfDURPP--kvKavw0";
    @Autowired
    private CrawlConfig crawlConfig;
    @Autowired
    private WebDriver webDriver;

    @GetMapping("/airbnb")
    public void getCrawlAirbnb(@RequestParam("query") String query) throws Exception{
        int numberOfCrawlers = 10;
        PageFetcher pageFetcher = new PageFetcher(crawlConfig);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(crawlConfig, pageFetcher, robotstxtServer);

        controller.addSeed("https://www.airbnb.co.kr/s/"+ query);

        controller.start(AirBnbCrawler.class, numberOfCrawlers);


    }
    @GetMapping("/hello")
    public void hello(@RequestParam("query") String query) throws IOException {

        webDriver.get(URL + query);

        List<WebElement> elementList = webDriver.findElements(By.cssSelector("._1szwzht a"));
        List<String> hrefList = new ArrayList<>();
        for(WebElement webElement : elementList) {
            hrefList.add(webElement.getAttribute("href"));
        }

        for(String href : hrefList){
            webDriver.get(href);
            //요소 가져오기
            String title = webDriver.findElement(By.cssSelector("._18hrqvin")).getText();
            System.out.println(title);


        }



    }
}
