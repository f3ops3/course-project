package springweb.courseproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import springweb.courseproject.config.MapperConfig;
import springweb.courseproject.dto.order.OrderResponseDto;
import springweb.courseproject.model.Order;

@Mapper(config = MapperConfig.class, uses = {OrderItemMapper.class})
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "orderItems", target = "orderItemsDtos",
            qualifiedByName = "toOrderItemDtoSet")
    OrderResponseDto toDto(Order order);

}
