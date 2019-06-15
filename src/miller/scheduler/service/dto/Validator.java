package miller.scheduler.service.dto;

import java.util.List;

public interface Validator {

    boolean isValid();

    List<String> violations();
}
