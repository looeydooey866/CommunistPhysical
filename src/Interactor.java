import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
// setting class (input, output in radians or degrees)
public class Interactor {
    private final HashMap<String,Vector> dataTable = new HashMap<>();
    private final Scanner Reader = new Scanner(System.in);
    private final ArrayList<String> queries = new ArrayList<>();
    public Boolean terminated = false;

    public void query(){
        Voicelines.askQuery();
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
            case "debug" -> debug();
            default -> unknownQuery();
        }
    }

    private void terminate(){
        Voicelines.announceTermination();
        terminated = true;
    }

    private void store(){
        String storingType = queries.removeFirst();
        String storingName = queries.removeFirst();
        if (storingType.equals("polar")){
            Vector cur = new Vector(0,0,0,0);
            cur.polform.setMag(Double.parseDouble(queries.removeFirst()));
            cur.polform.setThetaInput(Double.parseDouble(queries.removeFirst()));
            cur.recform = Rect.rec(cur.polform);
            dataTable.put(storingName,cur);
        } else if (storingType.equals("rect")){
            Vector cur = new Vector(0,0,0,0);
            cur.recform.setX(Double.parseDouble(queries.removeFirst()));
            cur.recform.setY(Double.parseDouble(queries.removeFirst()));
            cur.polform = Polar.pol(cur.recform);
            dataTable.put(storingName,cur);
        } else if (storingType.equals("equation")){
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
                        s = s.substring(1);
                    }

                    String decipart = "";
                    while ((s.charAt(0) >= '0') && (s.charAt(0) <= '9')){
                        decipart += s.charAt(0);
                        s = s.substring(1);
                    }
                    if (s.charAt(0) == '.'){
                        decipart += s.charAt(0);
                        s = s.substring(1);
                        while (s.charAt(0) >= '0' && s.charAt(0) <= '9'){
                            decipart += s.charAt(0);
                            s = s.substring(1);
                        }
                    }
                    double scalar = Double.parseDouble(decipart);

                    if (s.isEmpty()){
                        Voicelines.errorBadName();
                        return;
                    }

                    if (!minus) {
                        cur.add(dataTable.get(s).scale(scalar));
                    }
                    else {
                        cur.subtract(dataTable.get(s).scale(scalar));
                    }
                    minus = false;
                }
                else {
                    minus = (s.equals("-"));
                }
                sign ^= true;
            }
            dataTable.put(storingName,cur);
        } else {
            Voicelines.errorStorageType(storingType);
            return;
        }

        Voicelines.stored(storingName);
    }

    private void retrieve(){
        String name = queries.removeFirst();
        String type = queries.removeFirst();
        switch(type){
            case "polar" -> Vector.printPol(dataTable.get(name));
            case "rect" ->  Vector.printRec(dataTable.get(name));
            case "both" ->  Vector.print(dataTable.get(name));
            default -> Voicelines.retrievalTypeError(type);
        }
    }

    private void thank(){
        Voicelines.thank();
    }

    private void list(){
        Voicelines.listVector();
        System.out.println(dataTable.keySet());
    }

    private void unknownQuery(){
        Voicelines.queryNotRecognised();
    }

    private void set(){
        String settingName = queries.removeFirst();
        switch(settingName){
            case "precision" -> changePrecision();
            case "style" -> changeStyle();
            case "inputangleformat" -> changeInputAngleFormat();
            case "outputangleformat" -> changeOutputAngleFormat();
        }
    }

    private void changePrecision(){
        int precision = Integer.parseInt(queries.removeFirst());
        Settings.setPrecision(precision);
        Voicelines.changeSetting("precision",Integer.toString(precision));
    }

    private void changeStyle(){
        String style = queries.removeFirst();
        Settings.setStyle(style);
        Voicelines.changeSetting("style",style);
    }

    private void changeInputAngleFormat(){
        String angleformat = queries.removeFirst();
        Settings.setInputAngleFormat(angleformat);
        Voicelines.changeSetting("the input angle format",angleformat);
    }

    private void changeOutputAngleFormat(){
        String angleformat = queries.removeFirst();
        Settings.setOutputAngleFormat(angleformat);
        Voicelines.changeSetting("the output angle format",angleformat);
    }

    private void debug(){
        Voicelines.debug();
    }
}
