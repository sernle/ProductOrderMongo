package ltd.legiasoft.demo.controller;

import ltd.legiasoft.demo.model.Order;
import ltd.legiasoft.demo.model.Product;
import ltd.legiasoft.demo.repository.OrderRepository;
import ltd.legiasoft.demo.repository.ProductRepository;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest
public class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ProductRepository productRepository;

	@MockBean
	private OrderRepository orderRepository;


	@MockBean
	private RestTemplate restTemplate;

	@Test
	public void createOrder() throws Exception {
		ResponseEntity responseEntity = mock(ResponseEntity.class);
		given(restTemplate.exchange(any(), any(), isNull(), isA(ParameterizedTypeReference.class))).willReturn(responseEntity);
		Product product = new Product();
		product.setProductCode("fsdfds2");
		product.setProductName("test name");
		product.setPrice(BigDecimal.ONE);

		given(responseEntity.getBody()).willReturn(Lists.newArrayList(product));
		this.mockMvc.perform(post("/api/orders")
				.content("{\n" +
						"  \"buyerEmailAddress\": \"samle@legiasoft.ltd\",\n" +
						"  \"orderCode\": \"aa226704-4565-4aec-8f22-57bb01779a28\",\n" +
						"  \"orderTime\": \"2019-11-18T22:32:59.706Z\",\n" +
						"  \"products\": [\n" +
						"    {\n" +
						"      \"price\": 1,\n" +
						"      \"productCode\": \"fsdfds2\",\n" +
						"      \"productName\": \"test name\"\n" +
						"    }\n" +
						"  ],\n" +
						"  \"totalValue\": 1\n" +
						"}")
				.contentType("application/json"))
				.andExpect(status().isOk())
				.andExpect(mvcResult -> assertTrue("total value correct",
						mvcResult.getResponse().getContentAsString().contains("\"totalValue\":1")));
	}

	@Test
	public void findOrderBetweenOrderTime() throws Exception {
		LocalDateTime end = LocalDateTime.parse("2019-11-18T23:33:59.906", DateTimeFormatter.ISO_DATE_TIME);
		LocalDateTime start = LocalDateTime.parse("2019-11-18T21:30:59.705", DateTimeFormatter.ISO_DATE_TIME);


		final Order expectedOrder = new Order();
		expectedOrder.setOrderCode(UUID.fromString("b14b3ba4-3b4e-43b3-b3da-21f11194c995"));
		expectedOrder.setBuyerEmailAddress("sam.le@legiasoft.ltd");
		expectedOrder.setTotalValue(BigDecimal.ONE);
		expectedOrder.setProducts(Lists.list(new Product("fsdfds2", "Test2 modified2", BigDecimal.ONE)));
		expectedOrder.setOrderTime(LocalDateTime.parse("2019-11-18T23:01:07.326", DateTimeFormatter.ISO_DATE_TIME));
		given(orderRepository.findByOrderTimeBetween(start, end)).willReturn(Lists.list(expectedOrder));
		this.mockMvc.perform(get("/api/orders?end=2019-11-18T23:33:59.906Z&start=2019-11-18T21:30:59.705Z")).
				andExpect(status().isOk()).
				andExpect(content().json("[{\n" +
						"    \"orderCode\": \"b14b3ba4-3b4e-43b3-b3da-21f11194c995\",\n" +
						"    \"orderTime\": \"2019-11-18T23:01:07.326\",\n" +
						"    \"products\": [\n" +
						"      {\n" +
						"        \"productCode\": \"fsdfds2\",\n" +
						"        \"productName\": \"Test2 modified2\",\n" +
						"        \"price\": 1\n" +
						"      }\n" +
						"    ],\n" +
						"    \"totalValue\": 1,\n" +
						"    \"buyerEmailAddress\": \"sam.le@legiasoft.ltd\"\n" +
						"  }]"));
	}

}