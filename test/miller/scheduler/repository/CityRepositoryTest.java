package miller.scheduler.repository;

import miller.scheduler.domain.City;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class CityRepositoryTest {

    public static void main(String[] args) {

//        CityRepository repository;
//
//        City entity = new City();
//        entity.setCity("Rochester");
//        entity.setCreateDate(Instant.now());
//        entity.setCreatedBy("test");
//        entity.setLastUpdatedBy("test");
//        entity.setLastUpdate(Instant.now());
//        entity = repository.insert(entity,"test" );
//        Assert.isNotNull(entity.getId());
//
//        City savedEntity = repository.findById(entity.getId())
//                .orElseThrow(RuntimeException::new);
//        System.out.println("Saved = " + savedEntity);
//
//        savedEntity.setCity("Clarkfield");
//        savedEntity = repository.update(savedEntity, "test");
//
//        City updatedEntity = repository.findById(entity.getId())
//                .orElseThrow(RuntimeException::new);
//        System.out.println("Updated = " + updatedEntity);
//        Assert.isEqual(
//                updatedEntity.getCity(),
//                savedEntity.getCity());
//
//        List<City> allEntities = repository.findAll();
//        if (allEntities.isEmpty()) {
//            throw new RuntimeException();
//        }
//
//        repository.delete(entity.getId());
//
//        Optional<City> optional = repository.findById(savedEntity.getId());
//
//        if (optional.isPresent()) {
//            throw new RuntimeException();
//        }
    }
}
