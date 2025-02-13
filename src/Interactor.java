import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.random.*;

public class Interactor {
    private final HashMap<String,Vector> dataTable = new HashMap<>();
    private final Scanner Reader = new Scanner(System.in);
    private Scanner Parser = null;
    public boolean terminated = false;
    private final HashMap<String,String> aliases = new HashMap<>();

    public void query(){
        Voicelines.askQuery();
    }

    public void acceptQuery() throws Exception{
        Parser = new Scanner(Reader.nextLine());

        String queryType = Parser.next();

        while (aliases.containsKey(queryType)) { //take care of alias of alias
            queryType = aliases.get(queryType);
        }

        if (dataTable.containsKey(queryType)) {
            alter(queryType);
            return;
        }

        switch(queryType){
            case "terminate" -> terminate();
            case "list" -> list();
            case "vec" -> store();
            case "retrieve" -> retrieve();
            case "thanks" -> thank();
            case "set" -> set();
            case "debug" -> debug();
            case "calculate" -> calc();
            case "desmos" -> desmos();
            case "alias" -> alias();
            case "unalias" -> unalias();
            case "pyplot" -> pyplot();
            default -> unknownQuery();
        }
        // retrieve to print
        // new to vec
        // set to just mentioning the vector name
        // debug as self definable function (for debugging purposes)
        // calculate -> returns a resultant vector and prints it out
        // desmos a single vec or list of vec
        // pyplot a single vec or list of vec
        // a vector argument is either a single vector or a list of vectors
        // vector/list of vector can also include pol
    }

    private void terminate(){
        Voicelines.announceTermination();
        terminated = true;
    }

    private void alter(String name){
        String op = Parser.next();

        if (Objects.equals(op,"=")){

        }
    }

    private void store(){
        this.parserCharMode(true);
        String vecnames = "";
        while (Parser.hasNext()){
            String ch = Parser.next();
            if (Objects.equals(ch,"=")){
                break;
            }
            vecnames += ch;
        }
        vecnames = vecnames.trim();
        ArrayList<String> vecs = new ArrayList<>();
        if (vecnames.charAt(0) != '['){
            vecs.add(vecnames);
        }
        else {
            Scanner myScan = new Scanner(vecnames.substring(1,vecnames.length()-1));
            myScan.useDelimiter(",");
            while (myScan.hasNext()){
                vecs.add(myScan.next());
            }
            myScan.close();
        }

        this.parserCharMode(false);
        String eval = Parser.nextLine();
        Vector res = new Vector(VecRPNEngine.evaluate(eval,dataTable));
        for (String s : vecs){
            dataTable.put(s,res);
        }
        Voicelines.stored(vecnames);
    }

    private void calc(){
        //calculations for velocity and acceleration goes here
    }

    private void desmos(){
        StringBuilder output = new StringBuilder();
        while (Parser.hasNext()) {
            String query = Parser.next();
            if (!dataTable.containsKey(query)) {
                Voicelines.errorNonexistentVector(query);
                continue;
            }
            Vector cur = dataTable.get(query);
            double d = cur.polform.getTheta().getRad(), a = Math.PI / 4 - d, r = cur.polform.getMag();
            output.append(String.format("y\\cos %f+x\\sin %f=\\left(x\\cos %f-y\\sin %f\\right)\\left\\{y\\%ce0\\right\\}\\left\\{x^{2}+y^{2}\\le%f^{2}\\right\\}\n", a, a, a, a, (d <= Math.PI ? 'g' : 'l'), r));
            output.append(String.format("V_{%s}=\\ \\left(%s\\cos %s,%s\\sin %s\\right)\n",query,r,d,r,d));
        }
        Voicelines.desmos();
        System.out.println(output);
    }

    private void pyplot() throws Exception { // please rehaul
        String pythonFileName = "src/grapher.py";

        ArrayList<String> args = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        boolean usename = false;
        String name = null;
        String result = null;
        {
            Scanner takeNames = new Scanner(Parser.nextLine());
            takeNames.useDelimiter("->");
            String current = takeNames.next().trim();
            result = current;
            if (takeNames.hasNext()){
                usename = true;
                name = takeNames.next().trim();
            }
            takeNames.close();
            {
                current = current.trim();
                if (current.charAt(0) != '['){
                    names.add(current);
                }
                else {
                    Scanner scn = new Scanner(current.substring(1,current.length()-1));
                    scn.useDelimiter(",");
                    while (scn.hasNext()){
                        names.add(scn.next().trim());
                    }
                    scn.close();
                }
            }
        }
        for (int i=0;i<names.size();i++){
            String query = names.get(i);
            args.add(query);
            args.add(String.valueOf(dataTable.get(query).recform.getX()));
            args.add(String.valueOf(dataTable.get(query).recform.getY()));
        }
        if (usename){
            args.add(name);
        }

        ProcessBuilder p = new ProcessBuilder("python3",pythonFileName);
        p.command().addAll(args);
        p.redirectErrorStream(true);
        Process proc = p.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.err.println("Python output: " + line);
        }
        int exitCode = proc.waitFor();
        if (exitCode == 0){ //need some voicelines for this
            Voicelines.pyplot(result);
        }
        else {
            Voicelines.pyplotError(exitCode);
        }
    }

    private void retrieve(){
        String name = Parser.next();
        String type = Parser.next();
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
        String settingName = Parser.next();
        switch(settingName){
            case "precision" -> changePrecision();
            case "style" -> changeStyle();
            case "inputangleformat" -> changeInputAngleFormat();
            case "outputangleformat" -> changeOutputAngleFormat();
        }
    }

    private void changePrecision(){
        int precision = Integer.parseInt(Parser.next());
        Settings.setPrecision(precision);
        Voicelines.changeSetting("precision",Integer.toString(precision));
    }

    private void alias(){
        String newalias = Parser.next();
        String to = Parser.next();
    }

    private void unalias(){
        String removal = Parser.next();
        if (aliases.containsKey(removal)) {
            aliases.remove(removal);
        }
    }

    private void changeStyle(){
        String style = Parser.next();
        Settings.setStyle(style);
        Voicelines.changeSetting("style",style);
    }

    private void changeInputAngleFormat(){
        String angleformat = Parser.next();
        Settings.setInputAngleFormat(angleformat);
        Voicelines.changeSetting("the input angle format",angleformat);
    }

    private void changeOutputAngleFormat(){
        String angleformat = Parser.next();
        Settings.setOutputAngleFormat(angleformat);
        Voicelines.changeSetting("the output angle format",angleformat);
    }

    private void debug(){
        Voicelines.debug();
    }

    private void vecCalculation(){
        Vector x = VecRPNEngine.evaluate(Parser.nextLine(),dataTable);
        Vector.print(x);
    }

    private Vector objectifier(String s){
        if (dataTable.containsKey(s)){
            return dataTable.get(s);
        }
        else if (s.startsWith("Pol(")){
            String mag = "";
            String theta = "";
            for (int i=4;i<s.length();i++){
                if (s.charAt(i) == ','){
                    i++;
                    while (i < s.length()-1){
                        theta += s.charAt(i++);
                    }
                    break;
                }
                mag += s.charAt(i);
            }
            return Vector.pol(RPNEngine.evaluate(mag),RPNEngine.evaluate(theta));
        }
        else if (s.startsWith("Rect(")){
            String x = "";
            String y = "";
            for (int i=5;i<s.length();i++){
                if (s.charAt(i) == ','){
                    i++;
                    while (i < s.length()-1){
                        y += s.charAt(i++);
                    }
                    break;
                }
                x += s.charAt(i);
            }
            return Vector.rect(RPNEngine.evaluate(x),RPNEngine.evaluate(y));
        }
        else {
            return null;
        }
    }

    private void parserCharMode(boolean b){
        if (b){
            Parser.useDelimiter("");
        }
        else {
            Parser.useDelimiter(" ");
        }
    }
}
