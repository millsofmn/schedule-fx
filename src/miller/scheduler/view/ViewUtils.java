package miller.scheduler.view;

import javafx.scene.control.Alert;

import java.util.List;
import java.util.stream.Collectors;

public class ViewUtils {
    private ViewUtils() {
    }

    public static Alert showValidationErrors(List<String> errors){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ValidationError");
        alert.setHeaderText("Invalid due to the following:");

        // another lambda that using a collector will conjugate all messages together
        alert.setContentText(errors.stream().collect(Collectors.joining("\n")));

        return alert;
    }

    public static Alert showExceptionErrors(String error){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ValidationError");
        alert.setHeaderText(error);
        alert.setContentText("Select Ok to continue or Cancel to fix.");

        return alert;
    }
}
