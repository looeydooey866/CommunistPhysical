import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Stack;

public class VecRPNEngine {
    public Stack<String> tokens = new Stack<>();
    private final Stack<Character> operators = new Stack<Character>();
    private final Stack<Vector> vectors = new Stack<>();
    private Parser parsenator;
    private ArrayList<String> keywords;
    private HashMap<String,Vector> dataTable;

    public VecRPNEngine(){

    }

    public VecRPNEngine(Parser parser){
        this.parsenator = parser;
        this.keywords = parsenator.getKeywords();
        this.dataTable = parsenator.getDataTable();
    }

    public boolean isUnary(Character c){
        return (c == '#' || c == '=');
    }

    public  boolean ignore(Character c){
        return switch(c){
            case ' ' -> true;
            default -> false;
        };
    }

    public  boolean isSymbol(Character c){
        return isOperator(c) || c=='('||c==')';
    }

    public boolean isSymbol(String s){
        return (s.length() == 1 && isSymbol(s.charAt(0)));
    }

    public  boolean isOperator(Character c){
        return (
                c == '+' ||
                        c == '-' ||
                        c == '#' ||
                        c == '='
        );
    }

    public  void operate(Character operator){
        if (isUnary(operator)) {
            Vector left = vectors.pop();
            switch(operator){
                case '#' -> vectors.push(left);
                case '=' -> vectors.push(new Vector(new Rect(-left.recform.getX(),-left.recform.getY())));
            }
        }
        else {
            Vector right = vectors.pop();
            Vector left = vectors.pop();
            switch(operator){
                case '+' -> vectors.push(Vector.sum(left, right));
                case '-' -> vectors.push(Vector.minus(left, right));
            }
        }
    }

    public  int priority(Character operator){
        if (isUnary(operator)){
            return 2;
        }
        else if (operator == '+' || operator == '-'){
            return 1;
        }
        else {
            return -1;
        }
    }

    public  boolean isVecOp(Character operator){
        return (operator == '+' || operator == '-' || operator == '#' || operator == '=');
    }

    public  Vector evaluate(){
        parsenator.consumeWhitespace();
        tokens = parsenator.tokenize();
        operators.clear();
        vectors.clear();
        final int n = tokens.size();
        boolean unary = true;
        while (!tokens.empty()) {

            String s = tokens.pop();
            if (isSymbol(s)){
                Character c = s.charAt(0);
                if (isOperator(c)) {
                    while (!operators.empty() &&
                        ((!unary && priority(operators.peek()) >= priority(c))) ||
                        (unary && priority(operators.peek()) > priority(c))
                    ) {
                        operate(operators.pop());
                    }
                    if (!unary) {
                        operators.push(c);
                    } else {
                        operators.push(switch (c) {
                            case '+' -> '#';
                            case '-' -> '=';
                            default -> ' ';
                        });
                    }
                    unary = true;
                } else if (c == '(') {
                    operators.push(c);
                    unary = true;
                } else if (c == ')') {
                    while (!operators.empty() && !(operators.peek() == '(')) {
                        operate(operators.pop());
                    }
                    operators.pop();
                    unary = false;
                }
            }
            else {
                vectors.push(takeVec(s));
                unary = false;
            }
        }
        while (!operators.empty()){
            operate(operators.pop());
        }
        return vectors.peek();
    }

    public  Vector eval(String s){
        String str = s;
        s = "";
        for (int i=0;i<str.length();i++){
            if (ignore(str.charAt(i))){
                continue;
            }
            s += str.charAt(i);
        }

        operators.clear();
        vectors.clear();
        final int n = s.length();
        boolean unary = true;
        for (int i=0;i<n;i++){
            if (isOperator(s.charAt(i))){
                while (!operators.empty() &&
                        ((!unary && priority(operators.peek()) >= priority(s.charAt(i))) ||
                                (unary && priority(operators.peek()) > priority(s.charAt(i))))
                ) {
                    operate(operators.pop());
                }
                if (!unary){
                    operators.push(s.charAt(i));
                }
                else {
                    operators.push(switch(s.charAt(i)){
                        case '+' -> '#';
                        case '-' -> '=';
                        default -> ' ';
                    });
                }
                unary = true;
            }
            else if (s.charAt(i) == '('){
                operators.push(s.charAt(i));
                unary = true;
            }

            else if (s.charAt(i) == ')'){
                while (!operators.empty()&&!(operators.peek()=='(')){
                    operate(operators.pop());
                }
                operators.pop();
                unary = false;
            }
            else {
                if (s.startsWith("Pol(",i)){
                    String l = "", r = "";
                    int balance = 1;
                    i+=4;
                    boolean comma = false;
                    while (i < n){
                        if (s.charAt(i) == '('){
                            balance++;
                        }
                        else if (s.charAt(i) == ')'){
                            balance--;
                            if (balance <= 0){
                                break;
                            }
                        }
                        else if (s.charAt(i) == ','){
                            comma = true;
                        }
                        else {
                            if (!comma){
                                l += s.charAt(i);
                            }
                            else {
                                r += s.charAt(i);
                            }
                        }
                        i++;
                    }
                    vectors.push(Vector.pol(RPNEngine.evaluate(l),RPNEngine.evaluate(r)));
                    unary = false;
                }
                else if (s.startsWith("Rect(",i)){
                    int balance = 1;
                    i+=5;
                    boolean comma = false;
                    String x =  "", y = "";
                    while (i < n){
                        if (s.charAt(i) == '('){
                            balance++;
                        }
                        else if (s.charAt(i) == ')'){
                            balance--;
                            if (balance <= 0){
                                break;
                            }
                        }
                        else if (s.charAt(i) == ','){
                            comma = true;
                        }
                        else{
                            if (!comma){
                                x += s.charAt(i);
                            }
                            else {
                                y += s.charAt(i);
                            }
                        }
                        i++;
                    }
                    vectors.push(Vector.rect(RPNEngine.evaluate(x),RPNEngine.evaluate(y)));
                    unary = false;
                }
                else {
                    String vec = "";
                    while (i<n&&!isSymbol(s.charAt(i))){
                        vec+=s.charAt(i++);
                    }
                    i--;
                    if (dataTable.containsKey(vec)){
                        vectors.push(dataTable.get(vec));
                    }
                    unary = false;
                }
            }
        }
        while (!operators.empty()){
            operate(operators.pop());
        }
        return vectors.peek();
    }
    // a + Pol(3,4) + Rect(-1,-1) -> Vector(a,b,c,d)

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
