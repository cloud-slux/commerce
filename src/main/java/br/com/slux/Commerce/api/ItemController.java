package br.com.slux.Commerce.api;

import br.com.slux.Commerce.exception.ResourceNotFoundException;
import br.com.slux.Commerce.model.Item;
import br.com.slux.Commerce.repository.ItemRepository;
import br.com.slux.Commerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/orders/{orderId}/items")
    public List<Item> getItemsByOrderId(@PathVariable Long orderId) {
        return itemRepository.findByOrderId(orderId);
    }

    @PostMapping("/orders/{orderId}/items")
    public Item addItem(@PathVariable Long orderId,
                            @Valid @RequestBody Item item) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    item.setOrder(order);
                    return itemRepository.save(item);
                }).orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));
    }

    @PutMapping("/orders/{orderId}/items/{itemId}")
    public Item updateItem(@PathVariable Long orderId,
                               @PathVariable Long itemId,
                               @Valid @RequestBody Item itemRequest) {
        if(!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("Order not found with id " + orderId);
        }

        return itemRepository.findById(itemId)
                .map(item -> {
                    item.setDescription(itemRequest.getDescription());
                    item.setQuantity(itemRequest.getQuantity());
                    item.setUnitPrice(itemRequest.getUnitPrice());
                    item.setValue(itemRequest.getValue());
                    return itemRepository.save(item);
                }).orElseThrow(() -> new ResourceNotFoundException("Item not found with id " + itemId));
    }

    @DeleteMapping("/orders/{orderId}/items/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable Long orderId,
                                          @PathVariable Long itemId) {
        if(!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("Order not found with id " + orderId);
        }

        return itemRepository.findById(itemId)
                .map(item -> {
                    itemRepository.delete(item);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Item not found with id " + itemId));

    }
}
