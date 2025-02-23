import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

public class Interactor {
    private final HashMap<String,Vector> dataTable = new HashMap<>();
    private final Scanner reader = new Scanner(System.in);
    private final Deque<String> queries = new ArrayDeque<>();
    public boolean terminated = false;
    private final HashMap<String,String> aliases = new HashMap<>();
    public final ArrayList<String> keywords = new ArrayList<String>(){{add("Pol");add("Rect");}};
    public final ArrayList<Character> multiVectorOperators = new ArrayList<Character>(){{add('Σ');}};
    private final Parser parsenator = new Parser(new Scanner(""),this.keywords,this.dataTable,this.multiVectorOperators);
    private History history = new History();

    public Interactor() {
    }

    public void query(){
        Voicelines.askQuery();
    }

    public void read(){
        this.queries.addLast(this.reader.nextLine());
    }

    public void acceptQuery() throws Exception{
        if (queries.isEmpty()){
            read();
        }
        String query = queries.removeFirst();
        parsenator.setInput(query);

        String queryType = parsenator.next();

        while (aliases.containsKey(queryType)) { //take care of alias of alias
            queryType = aliases.get(queryType);
        }

        if (dataTable.containsKey(queryType)) {
            alter(queryType);
            return;
        }

        try {
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
                case "history" -> history();
                case "setfile" -> setfile();
                case "readfile" -> readfile();
                case "writefile" -> writefile();
                default -> unknownQuery();
            }
            switch(queryType){
                case "terminate" -> dn();
                case "history" -> dn();
                case "setfile" -> dn();
                case "readfile" -> dn();
                case "writefile" -> dn();
                default -> history.log(query);
            }
        }
        catch (Exception e) {
            //Need voicelines. Someone pls help
            System.err.println(e);
        }
    }

    private void dn(){
        //lol
    }


    private void terminate(){
        Voicelines.announceTermination();
        terminated = true;
    }

    private void alter(String name){
        String op = parsenator.next();

        if (Objects.equals(op,"=")){

        }
    }

    private void store(){
        ArrayList<String> vecnames = parsenator.takeList();
        parsenator.takeUntil('=');
        VecRPNEngine engine = new VecRPNEngine(parsenator);
        Vector res = engine.evaluate();
        for (String s : vecnames){
            dataTable.put(s,res);
        }
        Voicelines.stored(vecnames.toString());
    }

    private void calc(){
        //calculations for velocity and acceleration goes here
    }

    private void desmos(){
        StringBuilder output = new StringBuilder();
        parsenator.consumeWhitespace();
        ArrayList<String> queries = parsenator.takeList();
        for (String s : queries) {
            Vector cur = takeVec(s); double d = cur.polform.getTheta().getRad(), a = Math.PI / 4 - d, r = cur.polform.getMag();
            output.append(String.format("y\\cos %f+x\\sin %f=\\left(x\\cos %f-y\\sin %f\\right)\\left\\{y\\%ce0\\right\\}\\left\\{x^{2}+y^{2}\\le%f^{2}\\right\\}\n", a, a, a, a, (d <= Math.PI ? 'g' : 'l'), r));
            output.append(String.format("V_{%s}=\\ \\left(%s\\cos %s,%s\\sin %s\\right)\n",s,r,d,r,d));
        }
        Voicelines.desmos();
        System.out.println("========================================\n");
        System.out.println(output);
        System.out.println("\n========================================");
    }


    private void pyplot() throws Exception { // please rehaul
        String que = parsenator.next();
        final String pythonFileName = "grapher.py";
        // num of vectors, vector data, display/save, save name <<NO_NAME>> if default

        ArrayList<String> args = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        boolean usename = false;
        String name = null;
        {
            Parser takeNames = new Parser(parsenator.nextLine(),this.keywords,this.dataTable,this.multiVectorOperators);
            takeNames.consumeWhitespace();
            names = takeNames.takeList();
            takeNames.consumeWhitespace();
            if (takeNames.hasNext() && takeNames.peek("\n").startsWith("->")) {
                takeNames.consume('>'); //lol
                name = takeNames.next("\n").trim();
                usename = true;
            }
        }
        args.add(Integer.toString(names.size()));
        for (String query : names) {
            args.add(query);
            Vector cur = takeVec(query);
            args.add(String.valueOf(cur.recform.getX()));
            args.add(String.valueOf(cur.recform.getY()));
        }

        args.add(que);
        if (Objects.equals(que,"save")) {
            if (usename)
                args.add(name);
            else
                args.add("<<NO_NAME>>");
        }
        // size - 2, if size - 1 divisible by 3

        ProcessBuilder p = new ProcessBuilder("python",pythonFileName);
        p.command().addAll(args);
        p.redirectErrorStream(true);
        Process proc = p.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null)
            System.err.println("Python output: " + line);

        int exitCode = proc.waitFor();
        if (exitCode == 0){ //need some voicelines for this
            if (Objects.equals(que, "save")) {
                if (usename)
                    Voicelines.pyplot(name);
                else
                    Voicelines.pyplotDefaultName();
            }
            else if (Objects.equals(que, "display"))
                Voicelines.pyplotDisplayed();
        }
        else
            Voicelines.pyplotError(exitCode);
    }

    private void retrieve(){
        ArrayList<String> queries = parsenator.takeList();
        for (String s : queries){
            System.out.println("======= Vector " + s + " =======");
            Vector.print(takeVec(s));
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
        String settingName = parsenator.next();
        switch(settingName){
            case "precision" -> changePrecision();
            case "style" -> changeStyle();
            case "inputangleformat" -> changeInputAngleFormat();
            case "outputangleformat" -> changeOutputAngleFormat();
            case "ospathdelimiter" -> changeOSPathDelimiter();
        }
    }

    private void changePrecision(){
        int precision = Integer.parseInt(parsenator.next());
        Settings.setPrecision(precision);
        Voicelines.changeSetting("precision",Integer.toString(precision));
    }

    private void changeOSPathDelimiter(){
        String delim = parsenator.next();
        Settings.setOSPathDelimiter(delim);
        Voicelines.changeSetting("ospathdelimiter",delim);
    }

    private void alias(){
        String newalias = parsenator.next();
        String to = parsenator.next();
        aliases.put(newalias,to);
    }

    private void unalias(){
        String removal = parsenator.next();
        if (aliases.containsKey(removal)) {
            aliases.remove(removal);
        }
    }

    private void history() {
        Voicelines.historyRevealed();
        for (String s : this.history.logs){
            System.out.println(s);
        }
    }

    private void setfile(){
        String filepath = parsenator.next();
        this.history = new History(filepath);
        Voicelines.setFilePath(filepath);
    }

    private void readfile(){
        Scanner x = new Scanner(this.history.totalPoll());
        while (x.hasNextLine()){
            String line = x.nextLine();
            System.out.println(line);
            queries.addLast(line);
        }
    }

    private void writefile(){
        this.history.write();
    }
    
    private void changeStyle(){
        String style = parsenator.next();
        Settings.setStyle(style);
        Voicelines.changeSetting("style",style);
    }

    private void changeInputAngleFormat(){
        String angleformat = parsenator.next();
        Settings.setInputAngleFormat(angleformat);
        Voicelines.changeSetting("the input angle format",angleformat);
    }

    private void changeOutputAngleFormat(){
        String angleformat = parsenator.next();
        Settings.setOutputAngleFormat(angleformat);
        Voicelines.changeSetting("the output angle format",angleformat);
    }

    private void debug(){
        Voicelines.debug();
    }
    public Vector takeVec(String s){
        String str = "";
        for (int i=0;i<s.length();i++){
            if (!Character.isAlphabetic(s.charAt(i))){
                break;
            }

            str += s.charAt(i);
        }
        if (keywords.contains(str)){
            ArrayList<String> split = Parser.split(s.substring(str.length()+1,s.length()-1),',');
            if (Objects.equals(str,"Pol")){
                return Vector.pol(RPNEngine.evaluate(split.get(0)), RPNEngine.evaluate(split.get(1)));
            }
            else if (Objects.equals(str,"Rect")){
                return Vector.rect(RPNEngine.evaluate(split.get(0)), RPNEngine.evaluate(split.get(1)));
            }
        }
        else {
            return dataTable.get(str);
        }
        return null;
    }
}

