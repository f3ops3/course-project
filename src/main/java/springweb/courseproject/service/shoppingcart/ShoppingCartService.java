package springweb.courseproject.service.shoppingcart;

import springweb.courseproject.dto.cartitem.CartItemResponseDto;
import springweb.courseproject.dto.cartitem.CreateCartItemRequestDto;
import springweb.courseproject.dto.cartitem.UpdateCartItemRequestDto;
import springweb.courseproject.dto.shoppingcart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCartForCurrentUser(Long userId);

    CartItemResponseDto addCartItem(
            CreateCartItemRequestDto createCartItemRequestDto, Long userId);

    CartItemResponseDto updateCartItemById(
            Long cartId, UpdateCartItemRequestDto updateCartItemRequestDto, Long userId);

    void removeCartItemById(Long cartId, Long userId);
}
