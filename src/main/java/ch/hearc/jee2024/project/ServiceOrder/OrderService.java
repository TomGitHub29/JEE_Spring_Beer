package ch.hearc.jee2024.project.ServiceOrder;

import ch.hearc.jee2024.project.IOC.Beer;
import ch.hearc.jee2024.project.IOC.Order;
import ch.hearc.jee2024.project.RepositoryBeer.IRepositoryBeer;
import ch.hearc.jee2024.project.RepositoryOrder.IRepositoryOrder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService implements IOrderService {
    private final IRepositoryOrder orderRepository;
    private final IRepositoryBeer beerRepository;

    public OrderService(@Qualifier("orderRepository") IRepositoryOrder orderRepository, @Qualifier("beerRepository") IRepositoryBeer beerRepository) {
        this.orderRepository = orderRepository;
        this.beerRepository = beerRepository;
    }

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(Math.toIntExact(id));
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(Math.toIntExact(id));
    }

    @Override
    public List<Order> getAllOrders() {
        return (List<Order>) orderRepository.findAll();
    }

    @Override
    public Order addBeerToOrder(Long orderId, Long beerId) {
        Optional<Order> orderOpt = orderRepository.findById(Math.toIntExact(orderId));
        Optional<Beer> beerOpt = beerRepository.findById(beerId);

        if (orderOpt.isPresent() && beerOpt.isPresent()) {
            Order order = orderOpt.get();
            Beer beer = beerOpt.get();
            order.addBeer(beer);
            return orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Order or Beer not found");
        }
    }

    @Override
    public Order removeBeerFromOrder(Long orderId, Long beerId) {
        Optional<Order> orderOpt = orderRepository.findById(Math.toIntExact(orderId));
        Optional<Beer> beerOpt = beerRepository.findById(beerId);

        if (orderOpt.isPresent() && beerOpt.isPresent()) {
            Order order = orderOpt.get();
            Beer beer = beerOpt.get();
            order.getBeers().remove(beer);
            order.recalculateTotalPrice();
            return orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Order or Beer not found");
        }
    }

}
