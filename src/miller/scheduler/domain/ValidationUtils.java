package miller.scheduler.domain;

public class ValidationUtils {
    private ValidationUtils() {
    }
    public static boolean isInt(String str){
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public static boolean isNullOrEmpty(Integer i){
        boolean invalid = false;
        if(i == null){
            invalid = true;
        }
        return invalid;
    }

    public static boolean isNullOrEmpty(String str){
        boolean invalid = false;

        if(str == null || str.isEmpty()){
            invalid = true;
        }

        return invalid;
    }
}
