package com.lshwan.hof.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class CrawlingServiceTests {
  @Autowired
  private CrawlingService service;

  @Test
  public void crawlingTest(){
    service.crawling();
  }
}
