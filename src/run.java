import java.util.*;

public class run{
    public static void main(String[] args){
        Interactor x = new Interactor();
        while (!x.terminated){
            try {
                x.query();
                x.acceptQuery();
            }
            catch (Exception e){
            }
        }
    }
}
