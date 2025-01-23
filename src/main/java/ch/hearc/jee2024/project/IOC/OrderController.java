package ch.hearc.jee2024.project.IOC;

import ch.hearc.jee2024.project.ServiceOrder.OrderService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Créer une nouvelle commande", description = "Crée une nouvelle commande vide.")
    @PostMapping
    public ResponseEntity<Order> createOrder() {
        try {
            Order order = orderService.createOrder(new Order());
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @Operation(summary = "Obtenir une commande", description = "Retourne une commande enregistrée.")
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(order -> ResponseEntity.ok().body(order))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Ajouter une bière à une commande", description = "Ajoute une bière à une commande existante.")
    @PostMapping("/{orderId}/addBeer/{beerId}")
    public ResponseEntity<Order> addBeerToOrder(@PathVariable Long orderId, @PathVariable Long beerId) {
        try {
            Order updatedOrder = orderService.addBeerToOrder(orderId, beerId);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Supprimer une bière d'une commande", description = "Supprime une bière d'une commande existante.")
    @DeleteMapping("/{orderId}/removeBeer/{beerId}")
    public ResponseEntity<Order> removeBeerFromOrder(@PathVariable Long orderId, @PathVariable Long beerId) {
        try {
            Order updatedOrder = orderService.removeBeerFromOrder(orderId, beerId);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtenir toutes les bières d'une commande", description = "Retourne la liste des bières d'une commande.")
    @DeleteMapping("/{id}/checkout")
    public ResponseEntity<Double> deleteOrder(@PathVariable Long id) {
        Order order = orderService.getOrderById(id).orElse(null);
        if(order == null) {
            return ResponseEntity.notFound().build();
        }else {
            orderService.deleteOrder(id);
            return ResponseEntity.ok(order.getTotalPrice());
        }
    }

    @Operation(summary = "Obtenir la liste des fabricants", description = "Retourne tous les fabricants de bières enregistrés.")
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

