package miller.scheduler.service;

import miller.scheduler.domain.City;
import miller.scheduler.service.dto.CityDto;

public interface CityService extends Service<City, Integer>, DtoService<CityDto> {
}
