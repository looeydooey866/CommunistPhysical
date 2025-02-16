import java.util.*;
import java.util.regex.Pattern;

class Parser{
    private Scanner scanner = null;
    private ArrayList<String> keywords = null;
    private HashMap<String, Vector> dataTable = null;

    public Parser(Scanner scanner, ArrayList<String> keywords, HashMap<String, Vector> dataTable){
        this.scanner = scanner;
        this.keywords = keywords;
        this.dataTable = dataTable;
    }

    public Parser(String s, ArrayList<String> keywords, HashMap<String, Vector> dataTable){
        this.scanner = new Scanner(s);
        this.keywords = keywords;
        this.dataTable = dataTable;
    }

    public Parser(){
        this((Scanner) null, (ArrayList) null, (HashMap<String, Vector>) null);
    }

    public void setKeywords(ArrayList<String> keywords){
        this.keywords = keywords;
    }

    public void setInput(String s){
        this.scanner = new Scanner(s);
    }

    public void setInputSanitized(String s){
        this.scanner = new Scanner(sanitize(s));
    }

    public ArrayList<String> getKeywords(){
        return this.keywords;
    }

    public HashMap<String, Vector> getDataTable(){
        return this.dataTable;
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

    public boolean hasNext(Pattern p){
        return scanner.hasNext(p);
    }

    public boolean hasNext(){
        this.setDelimiter(" ");
        return scanner.hasNext();
    }

    public boolean hasNextChar(){
        this.setDelimiter("");
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

    public void consumeWhitespace(){
        while (this.hasNextChar() && this.peekChar() == ' '){this.nextSingleString();}
    }

    public void consume(Character until){
        while (this.hasNextChar() && this.nextChar() != until){
        }
    }

    public String takeUntil(String delimiter){
        this.setDelimiter(delimiter);
        String res = this.scanner.next();
        for (int i=0;i<delimiter.length();i++){
            this.nextChar();
        }
        return res;
    }

    public String takeUntil(Character delimiter){
        String res = "";
        while (this.hasNextChar()){
            Character c = this.nextChar();
            if (Objects.equals(c,delimiter)){
                break;
            }
            res += c;
        }
        return res;
    }

    public ArrayList<String> takeList(){
        this.consumeWhitespace();
        ArrayList<String> res = new ArrayList<>();
        if (Objects.equals('[',this.peekChar())){
            this.consume('[');
            String debog = this.takeUntil(']');
            res = splitEncode(debog,',');
        }
        else {
            String s = "";
            while (this.hasNextChar() && Character.isAlphabetic(this.peekChar())){
                s += this.nextChar();
            }
            if (keywords.contains(s)){
                s += this.nextChar();
                int balance = 1;
                while (this.hasNextChar()){
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
            res.add(s);
        }
        return res;
    }


    public static ArrayList<String> split(String operation, String delimiter){
        Scanner myScanner = new Scanner(operation);
        myScanner.useDelimiter(delimiter);
        ArrayList<String> res = new ArrayList<>();
        while (myScanner.hasNext()){
            res.add(myScanner.next().trim());
        }
        return res;
    }

    public static ArrayList<String> split(String operation, Character delimiter){
        Scanner myScanner = new Scanner(operation);
        myScanner.useDelimiter(String.valueOf(delimiter));
        ArrayList<String> res = new ArrayList<>();
        while (myScanner.hasNext()){
            res.add(myScanner.next().trim());
        }
        return res;
    }
    public static ArrayList<String> splitEncode(String operation, Character delimiter){
        Scanner myScanner = new Scanner(encode(operation));
        myScanner.useDelimiter('\uffff' +String.valueOf(delimiter));
        ArrayList<String> res = new ArrayList<>();
        while (myScanner.hasNext()){
            res.add(myScanner.next().trim());
        }
        return res;
    }

    public static String encode(String s){
        String res = "";
        int balance = 0;
        for (int i=0;i<s.length();i++){
            if (s.charAt(i) == '('){
                balance++;
            }
            else if (s.charAt(i) == ')'){
                balance--;
            }

            if (s.charAt(i) == ',' && balance == 0){
                res += '\uffff';
            }
            res += s.charAt(i);
        }
        return res;
    }

    // a🍍b
    // Ωåœ∑ß≈∂´çƒ®√©†∫º¡˙•¡º•™¡£ºª£™¢¢∞£¶•ª˙¥˜∆¨µ˚ˆ≤¬ø
    // d = ∑[a,b,c]
    // d = ∆[a,b]
    // d = delta[a,b]
    public Stack<String> tokenize(){
        Stack<String> res = new Stack<>();
        while (this.hasNextChar()){
            this.consumeWhitespace();
            if (Character.isAlphabetic(this.peekChar())){
                String s = "";
                while (this.hasNextChar() && Character.isAlphabetic(this.peekChar())){
                    s += this.nextChar();
                }
                if (keywords.contains(s)){
                    s += this.nextChar();
                    int balance = 1;
                    while (this.hasNextChar()){
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

//    public static void main(String[] args){
//        String eval = "Pol(3,4) + a + b - abaadbf + Rect(3,3)";
//        ArrayList<String> keywords = new ArrayList<>();
//        keywords.add("Pol");
//        keywords.add("Rect");
//        Parser parser = new Parser(new Scanner(""),keywords, new HashMap<>());
//        parser.setInputSanitized(eval);
//
//        Stack<String> out = parser.tokenize();
//        while (!out.empty()){
//            String cur = out.pop();
//            if (cur.length() != 1){
//                if (cur.contains("(")){
//                    VecRPNEngine engine = new VecRPNEngine(parser);
//                    Vector.print(engine.takeVec(cur));
//                }
//            }
//        }
//    }
}

