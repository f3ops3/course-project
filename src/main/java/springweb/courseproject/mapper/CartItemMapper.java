package springweb.courseproject.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import springweb.courseproject.config.MapperConfig;
import springweb.courseproject.dto.cartitem.CartItemResponseDto;
import springweb.courseproject.dto.cartitem.CreateCartItemRequestDto;
import springweb.courseproject.dto.cartitem.UpdateCartItemRequestDto;
import springweb.courseproject.model.CartItem;

@Mapper(config = MapperConfig.class, uses = {BookMapper.class})
public interface CartItemMapper {
    @Mapping(source = "bookId", target = "book", qualifiedByName = "bookFromId")
    CartItem toEntity(CreateCartItemRequestDto createCartItemRequestDto);

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemResponseDto toDto(CartItem cartItem);

    @Named(value = "toCartItemDtoSet")
    default Set<CartItemResponseDto> toCartItemDtoSet(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }

    void updateCartItem(@MappingTarget CartItem cartItem,
                        UpdateCartItemRequestDto createCartItemRequestDto);
}
