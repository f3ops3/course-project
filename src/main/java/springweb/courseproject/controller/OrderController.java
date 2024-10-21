package springweb.courseproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springweb.courseproject.dto.order.CreateOrderRequestDto;
import springweb.courseproject.dto.order.OrderResponseDto;
import springweb.courseproject.dto.order.PatchOrderRequestDto;
import springweb.courseproject.dto.orderitem.OrderItemResponseDto;
import springweb.courseproject.model.User;
import springweb.courseproject.service.order.OrderService;

@Tag(name = "Books store", description = "Endpoints for managing orders")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update an order", description = "Update status of specific order")
    @PatchMapping("/{orderId}")
    public OrderResponseDto updateOrder(
            @PathVariable Long orderId,
            @RequestBody @Valid PatchOrderRequestDto patchOrderRequestDto) {
        return orderService.patchOrder(orderId, patchOrderRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create a new order",
            description = "Create a new order with current shopping cart")
    @PostMapping
    public OrderResponseDto createOrder(
            @RequestBody @Valid CreateOrderRequestDto createOrderRequestDto,
            @AuthenticationPrincipal User user) {
        return orderService.createOrder(user.getId(), createOrderRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get all orders",
            description = "Get all orders that belong to current user")
    @GetMapping
    public List<OrderResponseDto> getAllOrders(Pageable pageable,
                                               @AuthenticationPrincipal User user) {
        return orderService.getAllUsersOrders(pageable, user.getId());
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get order items", description = "Get order items from specific order")
    @GetMapping("/{orderId}/items")
    public List<OrderItemResponseDto> getAllOrderItemsFromOrder(
            @PathVariable Long orderId, @AuthenticationPrincipal User user) {
        return orderService.getOrderItemsById(orderId, user.getId());
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get order item",
            description = "Get specific order item from specific order")
    @GetMapping("/{orderId}/items/{orderItemId}")
    public OrderItemResponseDto getOrderItemWithinOrderByIds(
            @PathVariable Long orderId, @PathVariable Long orderItemId,
            @AuthenticationPrincipal User user) {
        return orderService.getOrderItemFromOrderByIds(orderId, user.getId(), orderItemId);
    }
}
