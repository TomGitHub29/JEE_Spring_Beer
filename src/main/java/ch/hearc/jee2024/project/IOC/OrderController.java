package ch.hearc.jee2024.project.IOC;

import ch.hearc.jee2024.project.ServiceOrder.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder() {
        try {
            Order order = orderService.createOrder(new Order());
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(order -> ResponseEntity.ok().body(order))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{orderId}/addBeer/{beerId}")
    public ResponseEntity<Order> addBeerToOrder(@PathVariable Long orderId, @PathVariable Long beerId) {
        try {
            Order updatedOrder = orderService.addBeerToOrder(orderId, beerId);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{orderId}/removeBeer/{beerId}")
    public ResponseEntity<Order> removeBeerFromOrder(@PathVariable Long orderId, @PathVariable Long beerId) {
        try {
            Order updatedOrder = orderService.removeBeerFromOrder(orderId, beerId);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping
//    public ResponseEntity<List<Order>> getAllOrders() {
//        return ResponseEntity.ok(orderService.getAllOrders());
//    }

    @GetMapping
    public ResponseEntity<Page<Order>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreated") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Page<Order> orders = orderService.getOrders(page, size, sortBy, direction);
        return ResponseEntity.ok(orders);
    }
}

