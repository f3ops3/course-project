package springweb.courseproject.service.shoppingcart;

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springweb.courseproject.dto.cartitem.CreateCartItemRequestDto;
import springweb.courseproject.dto.cartitem.UpdateCartItemRequestDto;
import springweb.courseproject.dto.shoppingcart.ShoppingCartResponseDto;
import springweb.courseproject.exception.DataProcessingException;
import springweb.courseproject.exception.EntityNotFoundException;
import springweb.courseproject.mapper.CartItemMapper;
import springweb.courseproject.mapper.ShoppingCartMapper;
import springweb.courseproject.model.Book;
import springweb.courseproject.model.CartItem;
import springweb.courseproject.model.ShoppingCart;
import springweb.courseproject.model.User;
import springweb.courseproject.repository.book.BookRepository;
import springweb.courseproject.repository.cartitem.CartItemRepository;
import springweb.courseproject.repository.shoppingcart.ShoppingCartRepository;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto getShoppingCartForCurrentUser(Long userId) {
        return shoppingCartMapper.toDto(shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Couldn't find ")
                )
        );
    }

    @Transactional
    @Override
    public ShoppingCartResponseDto addCartItem(
            CreateCartItemRequestDto createCartItemRequestDto, Long userId) {
        Book book = bookRepository.findById(createCartItemRequestDto.getBookId()).orElseThrow(
                () -> new EntityNotFoundException("Couldn't find a book with id = "
                        + createCartItemRequestDto.getBookId())
        );
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Couldn't find shopping cart with "
                        + "user id = " + userId)
        );
        Optional<CartItem> existingCartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook()
                        .getId()
                        .equals(createCartItemRequestDto.getBookId()))
                .findFirst();
        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + createCartItemRequestDto.getQuantity());
        } else {
            cartItem = cartItemMapper.toEntity(createCartItemRequestDto);
            cartItem.setShoppingCart(shoppingCart);
            shoppingCart.getCartItems().add(cartItem);
        }
        cartItem.setBook(book);
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto updateCartItemById(
            Long cartId, UpdateCartItemRequestDto updateCartItemRequestDto, Long userId) {
        CartItem cartItem = getCartItemIfExists(cartId);
        ShoppingCart shoppingCart = checkIfCartItemBelongsToCurrentUser(cartId, cartItem, userId);
        cartItemMapper.updateCartItem(cartItem, updateCartItemRequestDto);
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(updateCartItemInShoppingCart(shoppingCart, cartItem));
    }

    @Override
    public void removeCartItemById(Long cartId, Long userId) {
        CartItem cartItem = getCartItemIfExists(cartId);
        checkIfCartItemBelongsToCurrentUser(cartId, cartItem, userId);
        cartItemRepository.deleteById(cartId);
    }

    private CartItem getCartItemIfExists(Long cartItemId) {
        return cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Couldn't find cart item with id = " + cartItemId));
    }

    private ShoppingCart checkIfCartItemBelongsToCurrentUser(
            Long cartId, CartItem cartItem, Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Couldn't find cart with id = " + cartId)
        );
        if (shoppingCart.getCartItems().stream()
                .noneMatch(item -> item.getId().equals(cartItem.getId()))) {
            throw new DataProcessingException("Cart item with id = " + cartId
                    + " does not belong to this user.");
        }
        return shoppingCart;
    }

    private ShoppingCart updateCartItemInShoppingCart(ShoppingCart shoppingCart,
            CartItem cartItem) {
        shoppingCart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItem.getId()))
                .forEach(item -> item.setQuantity(cartItem.getQuantity()));
        return shoppingCart;
    }
}
