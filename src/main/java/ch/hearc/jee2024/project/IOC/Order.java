package ch.hearc.jee2024.project.IOC;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "beer_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "order_beers",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "beer_id")
    )
    private List<Beer> beers = new ArrayList<>();

    private double totalPrice;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Order() {
        this.totalPrice = 0.0;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public List<Beer> getBeers() {
        return beers;
    }

    public void addBeer(Beer beer) {
        this.beers.add(beer);
        recalculateTotalPrice();
    }

    public void recalculateTotalPrice() {
        this.totalPrice = beers.stream().mapToDouble(Beer::getPrice).sum();
    }


}
