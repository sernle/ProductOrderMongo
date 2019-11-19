package ltd.legiasoft.demo.repository;

import ltd.legiasoft.demo.model.Order;
import ltd.legiasoft.demo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.Repository;

import java.math.BigDecimal;
import java.util.List;

@org.springframework.stereotype.Repository
public class ProductRepository implements Repository<Product, String> {


	@Autowired
	private MongoTemplate mongoTemplate;


	public void save(Product product) {
		mongoTemplate.save(product);
	}


	public List<Product> all() {
		return mongoTemplate.findAll(Product.class);
	}

	public List<Product> findByIds(List<String> ids) {
		Query query = new Query().addCriteria(Criteria.where("productCode").in(ids));
		return mongoTemplate.find(query, Product.class);
	}
	public void updatePrice(String productCode, BigDecimal price) {
		Product product = mongoTemplate.findById(productCode, Product.class);
		product.setPrice(price);
		mongoTemplate.save(product);
	}
}
