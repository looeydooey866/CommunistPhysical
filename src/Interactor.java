import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.*;

public class Interactor {
    private final HashMap<String,Vector> dataTable = new HashMap<>();
    private final Scanner Reader = new Scanner(System.in);
    private Parser Parsenator = null;
    public boolean terminated = false;
    private final HashMap<String,String> aliases = new HashMap<>();

    public void query(){
        Voicelines.askQuery();
    }

    public void acceptQuery() throws Exception{
        Parsenator = new Parser();
        Parsenator.setInput(Reader.nextLine());

        String queryType = Parsenator.next();

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
        String op = Parsenator.next();

        if (Objects.equals(op,"=")){

        }
    }

    private void store(){
        ArrayList<String> vecnames = Parsenator.takeList("[","]",",","=");
        ArrayList<String> keywords = new ArrayList<String>(){{add("Pol");add("Rect");}};
        VecRPNEngine engine = new VecRPNEngine(Parsenator.tokenize(keywords));
        Vector res = engine.tokenEval(keywords,dataTable);

        for (String s : vecnames){
            dataTable.put(s,res);
        }
        Voicelines.stored("");
    }

    private void calc(){
        //calculations for velocity and acceleration goes here
    }

    private void desmos(){
        StringBuilder output = new StringBuilder();
        String qry = Parsenator.nextLine();
        qry = qry.trim();
        ArrayList<String> queries = new ArrayList<>();
        if (qry.charAt(0) != '['){
            queries.add(qry);
        }
        else {
            Scanner myScan = new Scanner(qry.substring(1,qry.length()-1));
            myScan.useDelimiter(",");
            while (myScan.hasNext()){
                queries.add(myScan.next().trim());
            }
        }
        for (String s : queries) {
            if (!dataTable.containsKey(s)) {
                Voicelines.errorNonexistentVector(s);
                continue;
            }
            Vector cur = dataTable.get(s);
            double d = cur.polform.getTheta().getRad(), a = Math.PI / 4 - d, r = cur.polform.getMag();
            output.append(String.format("y\\cos %f+x\\sin %f=\\left(x\\cos %f-y\\sin %f\\right)\\left\\{y\\%ce0\\right\\}\\left\\{x^{2}+y^{2}\\le%f^{2}\\right\\}\n", a, a, a, a, (d <= Math.PI ? 'g' : 'l'), r));
            output.append(String.format("V_{%s}=\\ \\left(%s\\cos %s,%s\\sin %s\\right)\n",s,r,d,r,d));
        }
        Voicelines.desmos();
        System.out.println(output);
    }


    private void pyplot() throws Exception { // please rehaul
        String pythonFileName = String.format("src%sgrapher.py", File.separator);

        ArrayList<String> args = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        boolean usename = false;
        String name = null;
        String result = null;
        {
            Scanner takeNames = new Scanner(Parsenator.nextLine());
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
        String quer = Parsenator.nextLine().trim();
        ArrayList<String> queries = new ArrayList<>();
        if (quer.charAt(0) != '['){
            queries.add(quer);
        }
        else {
            Scanner myScan = new Scanner(quer.substring(1,quer.length()-1));
            myScan.useDelimiter(",");
            while (myScan.hasNext()){
                queries.add(myScan.next().trim());
            }
            myScan.close();
        }

        for (String s : queries){
            System.out.println("======= Vector " + s + " =======");
            Vector.print(dataTable.get(s));
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
        String settingName = Parsenator.next();
        switch(settingName){
            case "precision" -> changePrecision();
            case "style" -> changeStyle();
            case "inputangleformat" -> changeInputAngleFormat();
            case "outputangleformat" -> changeOutputAngleFormat();
            case "ospathdelimiter" -> changeOSPathDelimiter();
        }
    }

    private void changePrecision(){
        int precision = Integer.parseInt(Parsenator.next());
        Settings.setPrecision(precision);
        Voicelines.changeSetting("precision",Integer.toString(precision));
    }

    private void changeOSPathDelimiter(){
        String delim = Parsenator.next();
        Settings.setOSPathDelimiter(delim);
        Voicelines.changeSetting("ospathdelimiter",delim);
    }

    private void alias(){
        String newalias = Parsenator.next();
        String to = Parsenator.next();
        aliases.put(newalias,to);
    }

    private void unalias(){
        String removal = Parsenator.next();
        if (aliases.containsKey(removal)) {
            aliases.remove(removal);
        }
    }

    private void changeStyle(){
        String style = Parsenator.next();
        Settings.setStyle(style);
        Voicelines.changeSetting("style",style);
    }

    private void changeInputAngleFormat(){
        String angleformat = Parsenator.next();
        Settings.setInputAngleFormat(angleformat);
        Voicelines.changeSetting("the input angle format",angleformat);
    }

    private void changeOutputAngleFormat(){
        String angleformat = Parsenator.next();
        Settings.setOutputAngleFormat(angleformat);
        Voicelines.changeSetting("the output angle format",angleformat);
    }

    private void debug(){
        Voicelines.debug();
    }
}

class Parser{
    private Scanner scanner = null;

    public Parser(Scanner scanner){
        this.scanner = scanner;
    }

    public Parser(String s){
        this.scanner = new Scanner(s);
    }

    public Parser(){
        this((Scanner) null);
    }

    public void setInput(String s){
        this.scanner = new Scanner(s);
    }

    public void setInputSanitized(String s){
        this.scanner = new Scanner(sanitize(s));
    }

    public static String sanitize(String s){
        String res = "";
        for (int i=0;i<s.length();i++){
            if (s.charAt(i) != ' '){
                res += s.charAt(i);
            }
        }
        return res;
    }

    public void setDelimiter(String delimiter){
        this.scanner.useDelimiter(delimiter);
    }

    public int nextInt(String delimiter){
        this.setDelimiter(delimiter);
        return scanner.nextInt();
    }

    public int nextInt(){
        this.setDelimiter(" ");
        return scanner.nextInt();
    }

    public double nextDouble(String delimiter){
        this.setDelimiter(delimiter);
        return scanner.nextDouble();
    }

    public double nextDouble(){
        this.setDelimiter(" ");
        return scanner.nextDouble();
    }

    public float nextFloat(String delimiter){
        this.setDelimiter(delimiter);
        return scanner.nextFloat();
    }

    public float nextFloat(){
        this.setDelimiter(" ");
        return scanner.nextFloat();
    }

    public boolean hasNext(String delimiter){
        this.setDelimiter(delimiter);
        return scanner.hasNext();
    }

    public boolean hasNext(){
        this.setDelimiter(" ");
        return scanner.hasNext();
    }

    public String next(String delimiter){
        this.setDelimiter(delimiter);
        return scanner.next();
    }

    public String next(){
        this.setDelimiter(" ");
        return scanner.next();
    }

    public Character nextChar(){
        this.setDelimiter("");
        return scanner.next().charAt(0);
    }

    public String nextSingleString(){
        this.setDelimiter("");
        return scanner.next();
    }

    public String peek(String delimiter){
        this.setDelimiter(delimiter);
        this.scanner.hasNext(".*");
        return this.scanner.match().group(0);
    }

    public Character peekChar(){
        this.setDelimiter("");
        this.scanner.hasNext(".*");
        return this.scanner.match().group(0).charAt(0);
    }

    public String nextLine(){
        return this.scanner.nextLine();
    }

    public String takeUntil(String delimiter, boolean consume){
        if (delimiter == null){
            return this.scanner.nextLine();
        }
        this.setDelimiter(delimiter);
        String res = this.scanner.next();
        if (consume){
            this.setDelimiter("");
            for (int i=0;i<delimiter.length();i++){
                this.scanner.next();
            }
        }
        return res;
    }

    public ArrayList<String> takeList(String opener, String closener, String separator, String delimiter){
        String operation = this.takeUntil(delimiter,true).trim();
        ArrayList<String> res = new ArrayList<>();
        if (operation.startsWith(opener) && operation.endsWith(closener)){
            res = split(operation.substring(opener.length(),operation.length()-closener.length()),separator);
        }
        else {
            res.add(operation);
        }
        return res;
    } // abcd      =  Pol(3,((((pi))))


    public static ArrayList<String> split(String operation, String delimiter){
        Scanner myScanner = new Scanner(operation);
        myScanner.useDelimiter(delimiter);
        ArrayList<String> res = new ArrayList<>();
        while (myScanner.hasNext()){
            res.add(myScanner.next().trim());
        }
        return res;
    }

    // a🍍b
    // Ωåœ∑ß≈∂´çƒ®√©†∫º¡˙•¡º•™¡£ºª£™¢¢∞£¶•ª˙¥˜∆¨µ˚ˆ≤¬ø
    // d = ∑[a,b,c]
    // d = ∆[a,b]
    // d = delta[a,b]
    public Stack<String> tokenize(ArrayList<String> keywords){
        Stack<String> res = new Stack<>();
        while (this.hasNext("")){
            if (Character.isAlphabetic(this.peekChar())){
                String s = "";
                while (this.hasNext("") && Character.isAlphabetic(this.peekChar())){
                    s += this.nextChar();
                }
                if (keywords.contains(s)){
                    s += this.nextChar();
                    int balance = 1;
                    while (this.hasNext("")){
                        if (this.peekChar() == '('){
                            balance++;
                        }
                        else if (this.peekChar() == ')'){
                            balance--;
                        }
                        s += this.nextChar();
                        if (balance <= 0){
                            break;
                        }
                    }
                }
                res.push(s);
            }
            else {
                res.push(this.nextSingleString());
            }
        }
        return res;
    }

    public static void main(String[] args){
        String eval = "Pol(3,4) + a + b - abaadbf + Rect(3,3)";
        Parser parser = new Parser();
        parser.setInputSanitized(eval);
        ArrayList<String> keywords = new ArrayList<>();
        keywords.add("Pol");
        keywords.add("Rect");
        Stack<String> out = parser.tokenize(keywords);
        while (!out.empty()){
            System.err.println(out.pop());
        }
    }
}