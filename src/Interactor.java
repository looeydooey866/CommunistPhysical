import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class Interactor {
    HashMap<String,Vector> dataTable = new HashMap<String,Vector>();
    Scanner read = new Scanner(System.in);
    Boolean terminated = false;
    public void query(){
        System.out.println("Enter your query, good sire. Do not forget to thank me.");
    }

    public void acceptQuery(){
        String query = read.nextLine();
        Scanner queryScanner = new Scanner(query);

        ArrayList<String> queryArray = new ArrayList<>();
        while (queryScanner.hasNext()){
            queryArray.add(queryScanner.next());
        }


        String queryType = queryScanner.next();
        switch(queryType){
            case "terminate" -> {
                System.out.println("Your wish is my command, Sire. I bid you farewell.");
                terminated = true;
            }
            case "list" -> {
                list();
            }
            case "store" -> {
                query = queryScanner.nextLine();
                store(query);
            }
            case "retrieve" -> {
                query = queryScanner.nextLine();
                retrieve(query);
            }

            case "thanks" -> {
                System.out.println("It is my pleasure, Sire.");
            }

            default -> System.out.println("Sorry, I perchance am not able to fully Digest your Query. Please perchance input another query! Tally ho!");
        }
    }

    public void store(String query){
        Scanner queryScanner = new Scanner(query);
        String type = queryScanner.next();
        String name = queryScanner.next();
        if (Objects.equals(type, "polar")){

            String angle = queryScanner.next();
            double mag = queryScanner.nextDouble();
            double theta = queryScanner.nextDouble();
            if (Objects.equals(angle, "deg")){
                theta = Angle.rad(theta);
            }
            dataTable.put(name,new Vector(new Polar(mag,theta)));
            System.out.println("Stored " + name + ", sire.");
        }
        else if (Objects.equals(type, "coord")){
            double x = queryScanner.nextDouble();
            double y = queryScanner.nextDouble();

            dataTable.put(name,new Vector(new Rect(x,y)));
            System.out.println("Stored " + name + ", sire.");
        }
        else if (Objects.equals(type, "equation")){
            String equals = queryScanner.next();
            Vector cur = new Vector(0,0,0,0);
            boolean sign = false;
            boolean minus = false;
            while (queryScanner.hasNext()){
                String s = queryScanner.next();
                if (!sign) {
                    boolean skip = false;
                    if (s.charAt(0) == '-') {
                        minus ^= true;
                        skip = true;
                    }

                    if (!minus) {
                        cur.add(dataTable.get((skip?s.substring(1):s)));
                    }
                    else {
                        cur.subtract(dataTable.get((skip?s.substring(1):s)));
                    }
                    minus = false;
                }
                else {
                    minus = (Objects.equals(s, "-"));
                }
                sign ^= true;
            }
            dataTable.put(name,cur);
            System.out.println("Stored " + name + ", sire.");
        }
        else {
            System.out.println("I don't know what you are talking about, sire. You said " + type + " and it confused me heavily.");
        }
    }

    public void retrieve(String query){
        Scanner queryScanner = new Scanner(query);
        String name = queryScanner.next();
        String type = queryScanner.next();
        switch(type){
            case "polar" -> {
                String angle = queryScanner.next();
                System.out.println(angle);
                if (Objects.equals(angle, "deg")){
                    Vector.printPolDeg(dataTable.get(name));
                }
                else {
                    Vector.printPol(dataTable.get(name));
                }
            }
            case "rect" ->  Vector.printRec(dataTable.get(name));
            case "both" ->  Vector.print(dataTable.get(name));
            default -> System.out.println("Query not recognised!");
        }
    }

    public void list(){
        System.out.println("Here are all your available vectors, Sire. Please enjoy your delectable selection.");
        System.out.println(dataTable.keySet());
    }

}
