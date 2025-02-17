import java.util.ArrayList;

public class run{
    public static void main(String[] args){
        Interactor x = new Interactor();
        while (!x.terminated){
            try {
                x.query();
                x.acceptQuery();
            }
            catch (Exception e){
                System.out.println("Error. Caught something");
                System.out.println(e.getMessage());
            }
        }
    }
}