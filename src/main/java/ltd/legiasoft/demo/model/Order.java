package ltd.legiasoft.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Document("Order")
public class Order {

	@Id
	private UUID orderCode;

	@Indexed
	private LocalDateTime orderTime;

	@Field("products")
	private List<Product> products;

	private BigDecimal totalValue;

	private String buyerEmailAddress;

	public Order() {

	}

	public Order(String buyerEmailAddress, List<Product> products) {
		this.orderCode = UUID.randomUUID();
		this.orderTime = LocalDateTime.now();
		this.totalValue = products.stream().map(product -> product.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
		this.buyerEmailAddress = buyerEmailAddress;
		this.products = products;
	}


	public UUID getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(UUID orderCode) {
		this.orderCode = orderCode;
	}

	public LocalDateTime getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(LocalDateTime orderTime) {
		this.orderTime = orderTime;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public BigDecimal getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}

	public String getBuyerEmailAddress() {
		return buyerEmailAddress;
	}

	public void setBuyerEmailAddress(String buyerEmailAddress) {
		this.buyerEmailAddress = buyerEmailAddress;
	}
}
