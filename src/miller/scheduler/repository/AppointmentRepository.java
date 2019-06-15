package miller.scheduler.repository;

import miller.scheduler.domain.Appointment;
import miller.scheduler.repository.mapper.AppointmentMapper;

public class AppointmentRepository extends AbstractRepository<Appointment> {

    public AppointmentRepository(DatabaseConnection databaseConnection) {
        super(databaseConnection);

        DELETE_STATEMENT = "Delete From appointment Where appointmentId = ?";

        NXT_ID_STATEMENT = "Select max(appointmentId) + 1 From appointment";

        INSERT_STATEMENT = "Insert into appointment (customerId, title, description, location, contact, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy, appointmentId) Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        UPDATE_STATEMENT = "Update appointment Set customerId = ?, title = ?, description = ?, location = ?, contact = ?, url = ?, start = ?, end = ?, createDate = ?, createdBy = ?, lastUpdate = ?, lastUpdateBy = ? Where appointmentId = ?";

        SELECT_STATEMENT = "Select * From appointment";

        FIND_BY_ID_STATEMENT = "Select * from appointment Where appointmentId = ?";


        mapper = new AppointmentMapper();
    }

}
