package miller.scheduler.repository;

import miller.scheduler.domain.Address;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class AddressRepositoryTest {

    public static void main(String[] args) {

//        AddressRepository repository;
//
//        Address entity = new Address();
//        entity.setAddress("1631 13th St");
//        entity.setAddress2("Suite 1012");
//        entity.setPostalCode("55908");
//        entity.setPhone("444-999-8989");
//        entity.setCreateDate(Instant.now());
//        entity.setCreatedBy("test");
//        entity.setLastUpdatedBy("test");
//        entity.setLastUpdate(Instant.now());
//        entity = repository.insert(entity, "test");
//        Assert.isNotNull(entity.getId());
//
//        Address savedEntity = repository.findById(entity.getId())
//                .orElseThrow(RuntimeException::new);
//        System.out.println("Saved = " + savedEntity);
//
//        savedEntity.setAddress("12 Oak St.");;
//        savedEntity = repository.update(savedEntity, "test");
//
//        Address updatedEntity = repository.findById(entity.getId())
//                .orElseThrow(RuntimeException::new);
//        System.out.println("Updated = " + updatedEntity);
//        Assert.isEqual(
//                updatedEntity.getAddress(),
//                savedEntity.getAddress());
//
//        List<Address> allEntities = repository.findAll();
//        if (allEntities.isEmpty()) {
//            throw new RuntimeException();
//        }
//
//        repository.delete(entity.getId());
//
//        Optional<Address> optional = repository.findById(savedEntity.getId());
//
//        if (optional.isPresent()) {
//            throw new RuntimeException();
//        }
    }
}
