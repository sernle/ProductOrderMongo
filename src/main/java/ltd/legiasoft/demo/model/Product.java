package ltd.legiasoft.demo.model;


import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Objects;

@Document("Product")
public class Product {
	@Id
	@ApiModelProperty(notes = "product code")
	private String productCode;
	@ApiModelProperty(notes = "product name")
	private String productName;
	@ApiModelProperty(notes = "product price")
	private BigDecimal price;

	public Product() {
	}

	public Product(String productCode, String productName, BigDecimal price) {
		this.productCode = productCode;
		this.productName = productName;
		this.price = price;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode =  productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Product product = (Product) o;
		return productCode.equals(product.productCode) &&
				Objects.equals(productName, product.productName) &&
				price.equals(product.price);
	}

	@Override
	public int hashCode() {
		return Objects.hash(productCode, productName, price);
	}
}
