package com.me.crawl.dto;

import lombok.Getter;

@Getter
public class Room {
    private String title;
    private String host;
    private String address;
    private String roomInfo;
    private String reviewCount;
    private String star;
    private String link;
    private String price;

    public Room(String title, String host, String address, String roomInfo, String reviewCount, String star, String link, String price) {
        this.title = title;
        this.host = host;
        this.address = address;
        this.roomInfo = roomInfo;
        this.reviewCount = reviewCount;
        this.star = star;
        this.link = link;
        this.price = price;
    }
}
