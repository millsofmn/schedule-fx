package miller.scheduler.service;

import miller.scheduler.domain.Appointment;
import miller.scheduler.service.dto.AppointmentDto;

public interface AppointmentService extends Service<Appointment, Integer>, DtoService<AppointmentDto> {
}
