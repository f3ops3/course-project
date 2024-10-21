package springweb.courseproject.service.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import springweb.courseproject.dto.order.CreateOrderRequestDto;
import springweb.courseproject.dto.order.OrderResponseDto;
import springweb.courseproject.dto.order.PatchOrderRequestDto;
import springweb.courseproject.dto.orderitem.OrderItemResponseDto;
import springweb.courseproject.exception.DataProcessingException;
import springweb.courseproject.exception.EntityNotFoundException;
import springweb.courseproject.mapper.OrderItemMapper;
import springweb.courseproject.mapper.OrderMapper;
import springweb.courseproject.model.Order;
import springweb.courseproject.model.OrderItem;
import springweb.courseproject.model.ShoppingCart;
import springweb.courseproject.model.Status;
import springweb.courseproject.repository.order.OrderRepository;
import springweb.courseproject.repository.orderitem.OrderItemRepository;
import springweb.courseproject.repository.shoppingcart.ShoppingCartRepository;
import springweb.courseproject.repository.user.UserRepository;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDto createOrder(Long userId, CreateOrderRequestDto createOrderRequestDto) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Shopping cart with user id "
                        + userId + " not found")
        );
        if (cart.getCartItems().isEmpty()) {
            throw new DataProcessingException("You can't place an order with empty shopping cart");
        }
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus(Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(createOrderRequestDto.getShippingAddress());
        order.setOrderItems(cart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = orderItemMapper.toOrderItem(cartItem);
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toSet())
        );
        order.setTotal(cart.getCartItems().stream()
                .map(cartItem -> cartItem.getBook().getPrice().multiply(
                        BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderResponseDto> getAllUsersOrders(Pageable pageable, Long userId) {
        return orderRepository.findAllByUserId(pageable, userId).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDto patchOrder(Long orderId, PatchOrderRequestDto patchOrderRequestDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Order with order id " + orderId + " not found")
        );
        order.setStatus(patchOrderRequestDto.getStatus());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderItemResponseDto> getOrderItemsById(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Order with order id " + orderId + " not found")
        );
        checkIfOrderBelongsToUser(order, userId);
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemResponseDto getOrderItemFromOrderByIds(Long orderId,
                                                      Long userId, Long orderItemId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Order with order id " + orderId + " not found")
        );
        checkIfOrderBelongsToUser(order, userId);
        Optional<OrderItem> first = order.getOrderItems().stream()
                .filter(item -> item.getId().equals(orderItemId))
                .findFirst();
        return orderItemMapper.toDto(first.orElseThrow(
                () -> new EntityNotFoundException("Order item with id "
                        + orderItemId + " not found in order with id " + orderId)
        ));
    }

    private void checkIfOrderBelongsToUser(Order order, Long userId) {
        if (!order.getUser().getId().equals(userId)) {
            throw new DataProcessingException("Order with id = " + order.getId()
                    + " doesn't belong to user with id " + userId);
        }
    }
}
