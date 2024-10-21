package springweb.courseproject.repository.orderitem;

import org.springframework.data.jpa.repository.JpaRepository;
import springweb.courseproject.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
