package springweb.courseproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springweb.courseproject.dto.cartitem.CartItemResponseDto;
import springweb.courseproject.dto.cartitem.CreateCartItemRequestDto;
import springweb.courseproject.dto.cartitem.UpdateCartItemRequestDto;
import springweb.courseproject.dto.shoppingcart.ShoppingCartResponseDto;
import springweb.courseproject.model.User;
import springweb.courseproject.service.shoppingcart.ShoppingCartService;

@Tag(name = "Books store", description = "Endpoints for shopping cart")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Get shopping cart",
            description = "Get all cart items from current user's shopping cart")
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ShoppingCartResponseDto getShoppingCart(
            Pageable pageable, @AuthenticationPrincipal User user) {
        return shoppingCartService.getShoppingCartForCurrentUser(user.getId());
    }

    @Operation(summary = "Add new cart item",
            description = "Add a new cart item to current user's shopping cart")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CartItemResponseDto addCartItem(
            @RequestBody @Valid CreateCartItemRequestDto requestDto,
            @AuthenticationPrincipal User user) {
        return shoppingCartService.addCartItem(requestDto, user.getId());
    }

    @Operation(summary = "Update cart item",
            description = "Update cart item by id from current user's shopping cart")
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/items/{cartItemId}")
    public CartItemResponseDto updateCartItem(
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemRequestDto requestDto,
            @AuthenticationPrincipal User user) {
        return shoppingCartService.updateCartItemById(cartItemId, requestDto, user.getId());
    }

    @Operation(summary = "Delete cart item",
            description = "Delete cart item by id from current user's shopping cart")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/items/{cartItemId}")
    public void deleteCartItem(@PathVariable Long cartItemId, @AuthenticationPrincipal User user) {
        shoppingCartService.removeCartItemById(cartItemId, user.getId());
    }
}
