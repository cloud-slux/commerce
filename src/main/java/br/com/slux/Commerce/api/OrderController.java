package br.com.slux.Commerce.api;

import br.com.slux.Commerce.exception.ResourceNotFoundException;
import br.com.slux.Commerce.model.Order;
import br.com.slux.Commerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/orders")
    public Page<Order> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }


    @PostMapping("/orders")
    public Order createOrder(@Valid @RequestBody Order order) {
        return orderRepository.save(order);
    }

    @PutMapping("/orders/{orderId}")
    public Order updateOrder(@PathVariable Long orderId,
                                   @Valid @RequestBody Order orderRequest) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setClient(orderRequest.getClient());
                    order.setTotal(orderRequest.getTotal());
                    return orderRepository.save(order);
                }).orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));
    }


    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    orderRepository.delete(order);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));
    }
}