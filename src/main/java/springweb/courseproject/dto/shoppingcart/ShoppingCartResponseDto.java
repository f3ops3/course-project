package springweb.courseproject.dto.shoppingcart;

import java.util.Set;
import lombok.Data;
import springweb.courseproject.dto.cartitem.CartItemResponseDto;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItemDtos;
}
