package ch.hearc.jee2024.project.ServiceOrder;

import ch.hearc.jee2024.project.IOC.Beer;
import ch.hearc.jee2024.project.IOC.Order;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IOrderService {
    Order createOrder(Order order);

    Optional<Order> getOrderById(Long id);

    void deleteOrder(Long id);

    List<Order> getAllOrders();

    Order addBeerToOrder(Long orderId, Long beerId);
    Order removeBeerFromOrder(Long orderId, Long beerId);

    Page<Order> getOrders(int page, int size, String SortBy, String direction);

}
