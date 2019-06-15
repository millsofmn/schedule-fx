package miller.scheduler.repository;

import miller.scheduler.domain.Appointment;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class AppointmentRepositoryTest {
    public static void main(String[] args) {

//        AppointmentRepository repository;
//
//        Appointment entity = new Appointment();
//        entity.setCustomerId(0);
//        entity.setUserId(0);
//        entity.setTitle("Title");
//        entity.setDescription("Description");
//        entity.setLocation("Location");
//        entity.setContact("Contact");
//        entity.setType("Type");
//        entity.setUrl("Url:http:");
//        entity.setStart(Instant.now());
//        entity.setEnd(Instant.now());
//        entity.setCreateDate(Instant.now());
//        entity.setCreatedBy("test");
//        entity.setLastUpdatedBy("test");
//        entity.setLastUpdate(Instant.now());
//        entity = repository.insert(entity, "test");
//        Assert.isNotNull(entity.getId());
//
//        Appointment savedEntity = repository.findById(entity.getId())
//                .orElseThrow(RuntimeException::new);
//        System.out.println("Saved = " + savedEntity);
//
//        savedEntity.setDescription("Clarkfield");
//        savedEntity = repository.update(savedEntity, "test");
//
//        Appointment updatedEntity = repository.findById(entity.getId())
//                .orElseThrow(RuntimeException::new);
//        System.out.println("Updated = " + updatedEntity);
//        Assert.isEqual(
//                updatedEntity.getDescription(),
//                savedEntity.getDescription());
//
//        List<Appointment> allEntities = repository.findAll();
//        if (allEntities.isEmpty()) {
//            throw new RuntimeException();
//        }
//
//        repository.delete(entity.getId());
//
//        Optional<Appointment> optional = repository.findById(savedEntity.getId());
//
//        if (optional.isPresent()) {
//            throw new RuntimeException();
//        }
    }
}
