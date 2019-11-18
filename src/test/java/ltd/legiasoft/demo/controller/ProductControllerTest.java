package ltd.legiasoft.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ltd.legiasoft.demo.model.Product;
import ltd.legiasoft.demo.repository.ProductRepository;
import org.assertj.core.util.Lists;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ProductControllerTest {
	public static final Product TEST_PRODUCT = new Product("sample1", "productName", BigDecimal.TEN);
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ProductRepository productRepository;


	@Test
	public void createProductWithCorrectFormat() throws Exception {
		//given(productRepository.all()).willReturn(Lists.newArrayList());

		this.mockMvc.perform(post("/api/products")
								.content("{\"productCode\":\"sample1\",\"productName\":\"productName\",\"price\":10}")
								.contentType("application/json")).
				andExpect(status().isOk());
	}

	@Test
	public void testReturnEmptyList() throws Exception {
		given(productRepository.all()).willReturn(Lists.newArrayList());

		this.mockMvc.perform(get("/api/products")).
								andExpect(status().isOk()).
								andExpect(content().json("[]"));
	}


	@Test
	public void testReturnNonEmptyList() throws Exception {
		given(productRepository.all()).willReturn(Lists.newArrayList(TEST_PRODUCT));

		Map<String, Object> testProduct = new HashMap<>();
		testProduct.put("productCode", TEST_PRODUCT.getProductCode());
		testProduct.put("price", TEST_PRODUCT.getPrice());
		testProduct.put("productName", TEST_PRODUCT.getProductName());


		this.mockMvc.perform(get("/api/products")).
				andExpect(status().isOk()).
				andExpect(content().json("[{\"productCode\":\"sample1\",\"productName\":\"productName\",\"price\":10}]"));
	}


}