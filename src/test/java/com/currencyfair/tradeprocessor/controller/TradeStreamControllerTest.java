package com.currencyfair.tradeprocessor.controller;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
public class TradeStreamControllerTest {
	
	private int port=8082;

	private TestRestTemplate template;

	@Before
	public void before() throws Exception {
		template = new TestRestTemplate();
	}

	@Test
	public void tradestream_endpoint_pusblishes_stream() throws Exception {
		URL url = new URL("http://localhost:" + port + TradeStreamController.TRADESTREAM_URI);

		ResponseEntity<String> response = template.getForEntity(url.toString(), String.class);

		assertEquals("200", response.getStatusCode().toString());
		assertEquals("text/event-stream;charset=UTF-8", response.getHeaders().getContentType().toString());
	}

}
