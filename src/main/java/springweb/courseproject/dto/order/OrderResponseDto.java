package springweb.courseproject.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import springweb.courseproject.dto.orderitem.OrderItemResponseDto;
import springweb.courseproject.model.Status;

@Data
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private Set<OrderItemResponseDto> orderItemsDtos;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private Status status;
}
