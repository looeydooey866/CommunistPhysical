import java.util.*;

public class RPNEngine {
    private static final Stack<Character> operators = new Stack<>();
    private static final Stack<Double> values = new Stack<>();

    public static boolean isUnary(Character c){
        return (c == '#' || c == '=');
    }

    public static boolean ignore(Character c){
        return switch(c){
            case ' ' -> true;
            default -> false;
        };
    }

    public static boolean isOperator(Character c){
        return (
            c == '+' ||
            c =='-' ||
            c == '/' ||
            c == '*' ||
            c == '#' ||
            c == '='
        );
    }

    public static void operate(Character operator){
        if (isUnary(operator)){
            Double left = values.pop();
            switch(operator){
                case '#' -> values.push( left);
                case '=' -> values.push(-left);
            }
        }
        else {
            Double right = values.pop();
            Double left = values.pop();
            switch (operator) {
                case '+' -> values.push(left + right);
                case '-' -> values.push(left - right);
                case '*' -> values.push(left * right);
                case '/' -> values.push(left / right);
            }
        }
    }

    public static int priority(Character operator){
        if (isUnary(operator)){
            return 3;
        }
        else if (operator == '*' || operator == '/'){
            return 2;
        }
        else if (operator == '+' || operator == '-'){
            return 1;
        }
        else {
            return -1;
        }
    }

    public static double evaluate(String s) {
        String temp = s;
        s = "";
        for (int i=0;i<temp.length();i++){
            if (ignore(temp.charAt(i))){
                continue;
            }
            s += temp.charAt(i);
            if (i + 1 >= temp.length()){
                continue;
            }
            if ((temp.charAt(i) == ')'||Character.isDigit(temp.charAt(i))) && temp.charAt(i+1) == '('){
                s += '*';
            }
            if (i + 2 >= temp.length()){
                continue;
            }
            if (Character.isDigit(temp.charAt(i)) && temp.charAt(i+1) == 'p' && temp.charAt(i+2) == 'i'){
                s += "*pi";
                i+=2;
            }
        }
        operators.empty();
        values.empty();
        final int n = s.length();
        boolean unary = true;
        HashMap<String, Double> specialValues = new HashMap<>();
        specialValues.put("pi",Math.PI);
        specialValues.put("e",Math.E);
        specialValues.put("tau",Math.TAU);

        for (int i=0;i<n;i++){
            if (Character.isDigit(s.charAt(i))){
                String current = "" + s.charAt(i);
                i++;
                while (i < s.length() && (Character.isDigit(s.charAt(i))||s.charAt(i) == '.')){
                    current += s.charAt(i);
                    i++;
                }
                i--;
                values.push(Double.parseDouble(current));
                unary = false;
            }
            else if (Character.isAlphabetic(s.charAt(i))){
                String current = "" + s.charAt(i);
                i++;
                while (i < s.length() && Character.isAlphabetic(s.charAt(i))){
                    current += s.charAt(i);
                    i++;
                }
                i--;
                if (specialValues.containsKey(current)){
                    values.push(specialValues.get(current));
                }
                unary = false;
            }
            else if (isOperator(s.charAt(i))){
                while (!operators.empty() &&
                        (
                                (!unary && priority(operators.peek()) >= priority(s.charAt(i))) ||
                                ( unary && priority(operators.peek()) >  priority(s.charAt(i)))
                        )
                ) {
                    operate(operators.pop());
                }
                if (!unary) {
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
                while (!operators.empty()&&!(operators.peek() == '(')){
                    operate(operators.pop());
                }
                operators.pop();
                unary = false;
            }
        }
        while (!operators.empty()){
            operate(operators.pop());
        }
        return values.peek();
    }

    public static void reveal(){
        for (Character c : operators){
            System.out.printf("%c ",c);
        }
        System.out.println();
        for (Double v : values){
            System.out.printf("%f ",v);
        }
        System.out.println();
    }


}