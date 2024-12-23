package springweb.courseproject.repository.order;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import springweb.courseproject.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"orderItems", "orderItems.book", "user"})
    Page<Order> findAllByUserId(Pageable pageable, Long userId);

    @EntityGraph(attributePaths = {"orderItems", "orderItems.book", "user"})
    Optional<Order> findByIdAndUserId(Long id, Long userId);
}
