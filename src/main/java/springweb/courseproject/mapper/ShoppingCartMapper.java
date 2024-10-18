package springweb.courseproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import springweb.courseproject.config.MapperConfig;
import springweb.courseproject.dto.shoppingcart.ShoppingCartResponseDto;
import springweb.courseproject.model.ShoppingCart;

@Mapper(config = MapperConfig.class, uses = {CartItemMapper.class})
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "cartItems", target = "cartItemDtos", qualifiedByName = "toCartItemDtoSet")
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);
}
