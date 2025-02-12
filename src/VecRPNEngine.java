import java.util.HashMap;
import java.util.Stack;

public class VecRPNEngine {
    private static final Stack<Character> operators = new Stack<Character>();
    private static final Stack<Vector> vectors = new Stack<>();

    public static boolean isUnary(Character c){
        return (c == '#' || c == '=');
    }

    public static boolean ignore(Character c){
        return switch(c){
            case ' ' -> true;
            default -> false;
        };
    }

    public static boolean isSymbol(Character c){
        return isOperator(c) || c=='('||c==')';
    }

    public static boolean isOperator(Character c){
        return (
            c == '+' ||
            c == '-' ||
            c == '#' ||
            c == '='
        );
    }

    public static void operate(Character operator){
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

    public static int priority(Character operator){
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

    public static boolean isVecOp(Character operator){
        return (operator == '+' || operator == '-' || operator == '#' || operator == '=');
    }



    public static Vector evaluate(String s, HashMap<String,Vector> dataTable){
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
}
