package com.me.crawl;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class AirBnbCrawler extends WebCrawler {

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL();
        logger.info("shouldVisit href: {}", href);
        return href.startsWith("https://tv.zum.com/");
    }

    @Override
    public void visit(Page page) {

        if(page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();

            Set<WebURL> links = htmlParseData.getOutgoingUrls();
            logger.info("number of outgoing links: {}", links.size());
        }
    }
}
