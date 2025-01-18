import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;
// setting class (input, output in radians or degrees)
public class Interactor {
    private HashMap<String,Vector> dataTable = new HashMap<>();
    private Scanner Reader = new Scanner(System.in);
    private ArrayList<String> queries = new ArrayList<>();
    public Boolean terminated = false;

    public void query(){
        System.out.println("Enter your query, good sire. Do not forget to thank me.");
    }

    public void acceptQuery(){
        Scanner Parser = new Scanner(Reader.nextLine());

        queries.clear();
        while (Parser.hasNext()){
            queries.add(Parser.next());
        }

        String queryType = queries.removeFirst();

        switch(queryType){
            case "terminate" -> terminate();
            case "list" -> list();
            case "store" -> store();
            case "retrieve" -> retrieve();
            case "thanks" -> thank();
            case "set" -> set();
            default -> unknownQuery();
        }
    }

    private void terminate(){
        System.out.println("Your wish is my command, Sire. I bid you farewell.");
        terminated = true;
    }

    private void store(){
        String storingType = queries.removeFirst();
        String storingName = queries.removeFirst();
        if (Objects.equals(storingType, "polar")){
            Vector cur = new Vector(0,0,0,0);
            cur.polform.setMag(Double.parseDouble(queries.removeFirst()));
            cur.polform.setThetaInput(Double.parseDouble(queries.removeFirst()));
            cur.recform = Rect.rec(cur.polform);
            dataTable.put(storingName,cur);
        }
        else if (Objects.equals(storingType, "rect")){
            Vector cur = new Vector(0,0,0,0);
            cur.recform.setX(Double.parseDouble(queries.removeFirst()));
            cur.recform.setY(Double.parseDouble(queries.removeFirst()));
            cur.polform = Polar.pol(cur.recform);
            dataTable.put(storingName,cur);
        }
        else if (Objects.equals(storingType, "equation")){
            String equals = queries.removeFirst();
            Vector cur = new Vector(0,0,0,0);
            boolean sign = false;
            boolean minus = false;
            while (!queries.isEmpty()){
                String s = queries.removeFirst();
                if (!sign) {
                    boolean skip = false;
                    if (s.charAt(0) == '-') {
                        minus ^= true;
                        skip = true;
                    }
                    s = s.substring(1);

                    double scalar = 0.0;
                    while (s.charAt(0) >= '0' && s.charAt(0) <= '9'){
                        scalar *= 10;
                        scalar += (s.charAt(0) - '0');
                        s = s.substring(1);
                    }
                    if (s.charAt(0) == '.'){
                        s = s.substring(1);
                        double decipart = 0.0;
                        while (s.charAt(0) >= '0' && s.charAt(0) <= '9'){
                            decipart += (s.charAt(0) - '0');
                            decipart /= 10;
                            s = s.substring(1);
                        }
                        scalar += decipart;
                    }

                    if (s.isEmpty()){
                        System.out.println("Error sire, you hath not specified a suitable vector nameth!");
                        return;
                    }

                    if (!minus) {
                        cur.add(dataTable.get((skip?s.substring(1):s)).scalarMult(scalar));
                    }
                    else {
                        cur.subtract(dataTable.get((skip?s.substring(1):s)).scalarMult(scalar));
                    }
                    minus = false;
                }
                else {
                    minus = (Objects.equals(s, "-"));
                }
                sign ^= true;
            }
            dataTable.put(storingName,cur);
        } else {
            System.out.println("I don't know what you are talking about, sire. You said " + storingType + " and it confused me heavily.");
            return;
        }

        System.out.println("Stored " + storingName + ", sire.");
    }

    private void retrieve(){
        String name = queries.removeFirst();
        String type = queries.removeFirst();
        switch(type){
            case "polar" -> {
                String angle = queries.removeFirst();
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

    private void thank(){
        System.out.println("It is my pleasure, Sire.");
    }

    private void list(){
        System.out.println("Here are all your available vectors, Sire. Please enjoy your delectable selection.");
        System.out.println(dataTable.keySet());
    }

    private void unknownQuery(){
        System.out.println("Sorry, I perchance am not able to fully Digest your Query. Please perchance input another query! Tally ho!");
    }

    private void set(){
        String settingName = queries.removeFirst();
        switch(settingName){
            case "precision" -> changePrecision();
            case "verbosity" -> changeVerbosity();
            case "inputangleformat" -> changeInputAngleFormat();
            case "outputangleformat" -> changeOutputAngleFormat();
        }
    }

    private void changePrecision(){
        int precision = Integer.parseInt(queries.removeFirst());
        Settings.setPrecision(precision);
    }

    private void changeVerbosity(){
        boolean verbose = Boolean.parseBoolean(queries.removeFirst());
        Settings.setVerbose(verbose);
    }

    private void changeInputAngleFormat(){
        String angleformat = queries.removeFirst();
        Settings.setInputAngleFormat(angleformat);
    }

    private void changeOutputAngleFormat(){
        String angleformat = queries.removeFirst();
        Settings.setOutputAngleFormat(angleformat);
    }
}
