package miller.scheduler.repository;

import miller.scheduler.domain.Customer;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class CustomerRepositoryTest {

    public static void main(String[] args) {

//        CustomerRepository repository = new CustomerRepository();
//
//        Customer entity = new Customer();
//        entity.setCustomerName("Lyle");
//        entity.setAddressId(0);
//        entity.setActive(true);
//        entity.setCreateDate(Instant.now());
//        entity.setCreatedBy("test");
//        entity.setLastUpdatedBy("test");
//        entity.setLastUpdate(Instant.now());
//        entity = repository.insert(entity, "test");
//        Assert.isNotNull(entity.getId());
//
//        Customer savedEntity = repository.findById(entity.getId())
//                .orElseThrow(RuntimeException::new);
//        System.out.println("Saved = " + savedEntity);
//
//        savedEntity.setCustomerName("Clarkfield");
//        savedEntity = repository.update(savedEntity, "test");
//
//        Customer updatedEntity = repository.findById(entity.getId())
//                .orElseThrow(RuntimeException::new);
//        System.out.println("Updated = " + updatedEntity);
//        Assert.isEqual(
//                updatedEntity.getCustomerName(),
//                savedEntity.getCustomerName());
//
//        List<Customer> allEntities = repository.findAll();
//        if (allEntities.isEmpty()) {
//            throw new RuntimeException();
//        }
//
//        repository.delete(entity.getId());
//
//        Optional<Customer> optional = repository.findById(savedEntity.getId());
//
//        if (optional.isPresent()) {
//            throw new RuntimeException();
//        }
    }
}
