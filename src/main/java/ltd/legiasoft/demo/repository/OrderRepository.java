package ltd.legiasoft.demo.repository;

import ltd.legiasoft.demo.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Repository
public class OrderRepository implements Repository<Order, UUID> {

	@Autowired
	private MongoTemplate mongoTemplate;

	public void save(Order order) {
		mongoTemplate.save(order);
	}

	public List<Order> findByOrderTimeBetween(LocalDateTime from, LocalDateTime to) {
		Query query = new Query().addCriteria(Criteria.where("orderTime").gte(from).lte(to));
		return mongoTemplate.find(query, Order.class);
	}
}
