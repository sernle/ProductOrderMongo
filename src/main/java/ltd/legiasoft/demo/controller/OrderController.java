package ltd.legiasoft.demo.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ltd.legiasoft.demo.model.Order;
import ltd.legiasoft.demo.model.Product;
import ltd.legiasoft.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class OrderController {

	private final static Logger LOGGER = Logger.getLogger(OrderController.class.getName());

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private Environment env;
	@Autowired
	private RestTemplate restTemplate;

	@ApiOperation(value = "Add an order")
	@PostMapping("/orders")
	public ResponseEntity<Order> createOrder(@RequestBody Order order) {
		LOGGER.fine(String.format("Create new order %s", order));
		List<Product> products = getCurrentProducts(order);
		// We may not need this part actually. The order could be saved and the check could happen afterward.
		if (!productsValid(products, order.getProducts())) {
			return new ResponseEntity<>(order, HttpStatus.BAD_REQUEST);
		}
		Order newOrder = new Order(order.getBuyerEmailAddress(), products);
		orderRepository.save(newOrder);
		return new ResponseEntity<>(newOrder, HttpStatus.OK);
	}



	@ApiOperation(value = "View a list of available orders between start and end datetime", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
	})
	@GetMapping("/orders")
	public List<Order> findOrderBetweenOrderTime(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
	                                             @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
		return orderRepository.findByOrderTimeBetween(start, end);
	}

	private List<Product> getCurrentProducts(@RequestBody Order order) {
		LOGGER.info(String.format("Product search api %s", env.getProperty("api.search.product.uri")));
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(env.getProperty("api.search.product.uri"));
		order.getProducts().forEach(product ->
				builder.queryParam("id", product.getProductCode())
		);
		ResponseEntity<List<Product>> responseEntity = restTemplate.exchange(
				builder.build().encode().toUri(),
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<Product>>() {
				});
		return responseEntity.getBody();
	}

	private boolean productsValid(List<Product> products, List<Product> products1) {
		return products.equals(products1);
	}

}
