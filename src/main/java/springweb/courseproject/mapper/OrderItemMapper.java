package springweb.courseproject.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import springweb.courseproject.config.MapperConfig;
import springweb.courseproject.dto.orderitem.OrderItemResponseDto;
import springweb.courseproject.model.CartItem;
import springweb.courseproject.model.OrderItem;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemResponseDto toDto(OrderItem orderItem);

    @Named(value = "toOrderItemDtoSet")
    default Set<OrderItemResponseDto> toOrderItemDtoSet(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "book.price", target = "price")
    OrderItem toOrderItem(CartItem orderItem);
}
