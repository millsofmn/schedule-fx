package miller.scheduler.repository;

public class Assert {

    public static boolean isEqual(String s1, String s2){
        if(s1.equals(s2)){
            return true;
        } else {
            throw new RuntimeException();
        }
    }

    public static boolean isNotNull(Integer i1) {
        if(!i1.equals(null)){
            return true;
        } else {
            throw new RuntimeException();
        }
    }
}
