package miller.scheduler.repository;

import miller.scheduler.domain.Appointment;
import miller.scheduler.repository.mapper.AppointmentMapper;

public class AppointmentRepository extends AbstractRepository<Appointment> {

    public AppointmentRepository(DatabaseConnection databaseConnection) {
        super(databaseConnection);

        deleteStatement = "Delete From appointment Where appointmentId = ?";

        nxtIdStatement = "Select max(appointmentId) + 1 From appointment";

        insertStatement = "Insert into appointment (customerId, title, description, location, contact, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy, appointmentId) Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        updateStatement = "Update appointment Set customerId = ?, title = ?, description = ?, location = ?, contact = ?, url = ?, start = ?, end = ?, createDate = ?, createdBy = ?, lastUpdate = ?, lastUpdateBy = ? Where appointmentId = ?";

        selectStatement = "Select * From appointment";

        findByIdStatement = "Select * from appointment Where appointmentId = ?";


        mapper = new AppointmentMapper();
    }

}
