//package ch.hearc.jee2024.project.RepositoryBeer;
//
//import ch.hearc.jee2024.project.IOC.Beer;
//import org.springframework.stereotype.Repository;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
////@Repository("beerRepository")
////implements IRepositoryBeer
//public class RepositoryBeer  {
//    // Simuler une base de données avec une HashMap
//    private final Map<Long, Beer> beerStorage = new HashMap<>();
//    private long currentId = 1;
//
//    @Override
//    public Beer save(Beer beer) {
//        // Simuler la génération d'un ID auto-incrémenté
//        if (beer.getId() == null) {
//            beer.setId(currentId++);
//        }
//        beerStorage.put(beer.getId(), beer);
//        return beer;
//    }
//
//    @Override
//    public List<Beer> findAll() {
//        return beerStorage.values().stream().collect(Collectors.toList());
//    }
//
//    @Override
//    public Optional<Beer> findById(Long id) {
//        return Optional.ofNullable(beerStorage.get(id));
//    }
//}
