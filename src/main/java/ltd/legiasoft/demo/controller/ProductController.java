package ltd.legiasoft.demo.controller;

import ltd.legiasoft.demo.model.Product;
import ltd.legiasoft.demo.repository.ProductRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class ProductController {
	private final static Logger LOGGER = Logger.getLogger(ProductController.class.getName());

	private ProductRepository productRepository;

	@Autowired
	public ProductController(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@ApiOperation(value = "Add an product")
	@PostMapping("/products")
	public ResponseEntity<Object> createProduct(@RequestBody Product product) {
		LOGGER.fine(String.format("Create new product %s", product));
		productRepository.save(product);
		return new ResponseEntity<>("successful create product ", HttpStatus.OK);
	}


	@ApiOperation(value = "View a list of available products", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
	})
	@GetMapping("/products")
	public List<Product> all() {
		return productRepository.all();
	}

	@ApiOperation(value = "Get an product by Id")
	@GetMapping("/products/{id}")
	public ResponseEntity<Object> get(@PathVariable(value = "id") String id) {
		return new ResponseEntity<>(productRepository.findById(id), HttpStatus.OK);
	}


	@ApiOperation(value = "Update an product")
	@PutMapping("/product/{productCode}")
	public ResponseEntity < Object > updateEmployee(
			@ApiParam(value = "Product code to update product object", required = true) @PathVariable(value = "productCode") String productCode,
			@ApiParam(value = "Update product", required = true) @Valid @RequestBody Product product) {
		productRepository.updatePrice(productCode, product.getPrice());
		return new ResponseEntity<>("successful update product price", HttpStatus.OK);
	}
}

