package com.me.crawl.controller;

import com.me.crawl.AirBnbCrawler;
import com.me.crawl.dto.Room;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @ResponseBody
    public List<Room> hello(@RequestParam("query") String query) throws IOException {

        webDriver.get(URL + query);
        List<Room> roomList = new ArrayList<>();

        List<WebElement> elementList = webDriver.findElements(By.cssSelector("div._1dp4576 div div._e296pg div._13ky0r6y div a"));
        List<String> hrefList = new ArrayList<>();
        for(WebElement webElement : elementList) {
            hrefList.add(webElement.getAttribute("href"));
        }

        for(String href : hrefList){
            webDriver.get(href);
            //요소 가져오기
            String title = webDriver.findElement(By.cssSelector("#summary div div div div div div._1hpgssa1 div div span h1 span")).getText();
            String host = webDriver.findElement(By.cssSelector("#summary div div div div div div._10pi7ah2 div div div._8b6uza1")).getText();
            String address = webDriver.findElement(By.cssSelector("#summary div div div div div div._1hpgssa1 div div a div")).getText();
            String roomInfo = webDriver.findElement(By.cssSelector("#room div._mwt4r90 div div._1k6i3d4 div._mkyacvg div div div div div div._hgs47m div._n5lh69r div._36rlri")).getText();
            String link = href;

            //숙박요금 계산
            WebElement firstDayElement = webDriver.findElement(By.cssSelector("#room div._mwt4r90 div div._1k6i3d4 div._mkyacvg div div div div div div div section div._17fnibt div div._si4jfmp div td._12fun97"));
            System.out.println(firstDayElement.getText());
            firstDayElement.click();

            WebElement secondDayElement = webDriver.findElement(By.cssSelector("#room div._mwt4r90 div div._1k6i3d4 div._mkyacvg div div div div div div div section div._17fnibt div div._si4jfmp div td._12fun97"));
            System.out.println(secondDayElement.getText());
            secondDayElement.click();
            String price = webDriver.findElement(By.cssSelector("#room div._mwt4r90 div div._1k6i3d4 div._1oiaje8l div div div div._gor68n div div div._hauh0a div div div div._10ejfg4u div div div._12cv0xt div span._tw4pe52 span")).getText();

//            List<WebElement> elementDaySubList = elementDayList.subList(0, 1);
//            for(WebElement elementDay : elementDaySubList) {
//                String day = elementDay.findElement(By.cssSelector("span.notranslate div._mqakwe div._12x1o4t div._1pkjn54 div._1cjf199r")).getText();
//                System.out.println(day);
//            }

            String reviewCount = webDriver.findElement(By.cssSelector("#reviews div div div section div div div._2h22gn div._1ers5f61 div div div div h2 span")).getText();
            String star = webDriver.findElement(By.cssSelector("#reviews div div div section div div div._2h22gn div._1ers5f61 div div div div div div div")).getAttribute("content");

            Room room = new Room(title, host, address, roomInfo, reviewCount, star, link, price);
            roomList.add(room);
        }

        return roomList;

    }
}
