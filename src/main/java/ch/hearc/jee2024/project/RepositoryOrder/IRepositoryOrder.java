package ch.hearc.jee2024.project.RepositoryOrder;

import ch.hearc.jee2024.project.IOC.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository("orderRepository")
public interface IRepositoryOrder extends PagingAndSortingRepository<Order, Long>, CrudRepository<Order, Long> {
}
