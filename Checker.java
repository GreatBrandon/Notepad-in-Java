import java.util.Stack;

public class Checker {
    // when entering any infix expression, make sure there is NO SPACES in the infix otherwise stuff like this: "7.0  8.0 -( 2+3 -)" will work when it shouldn't, removing spaces seems to solve the problem
    public static String toPostfix(String infix) {
        Stack<Character> s = new Stack<>();
        String postfix = "";
        for (char c: infix.toCharArray()) {
            if (Character.isDigit(c) || c == '.') {
                postfix += c;
            } else if (c == '(') {
                s.push(c);
                postfix += " ";
            } else if (c == ')') {
                while (!s.isEmpty() && s.peek() != '(') {
                    postfix = postfix.trim();
                    postfix += " "+ s.pop();
                }
                if (!s.isEmpty() && s.peek() == '(') {
                    s.pop();
                } else {
                    System.out.println("No ( bracket found or stack is empty!"); // no left bracket found
                    return "-1";
                }
            } else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '%') {
                while (!s.isEmpty() && (getPriority(c) <= getPriority(s.peek()))) {
                    postfix = postfix.trim();
                    postfix += " " + s.pop();
                }
                s.push(c);
                postfix += " ";
            }
        }
        while (!s.isEmpty()) {
            postfix += " " + s.pop();
        }
        return postfix;
    }

    public static Double evaluatePostfix(String postfix) { // returns result of calculation, if error return null
        Stack<Double> s = new Stack<>();
        String currentNo = "";
        for (char c: postfix.toCharArray()) {
            try {
                if (Character.isDigit(c) || c == '.') {
                    currentNo+= c;
                } else if (c == ' ') {
                    if (currentNo != "") {
                        try {
                            s.push(Double.parseDouble(currentNo));
                            currentNo = "";
                        } catch (Exception e) {
                            System.out.println("Invalid expression, consider removing extra decimal points"); // multiple decimal points in a single value
                            return null;
                        }
                    }
                } else if (c == '+') {
                    s.push(s.pop()+s.pop());
                } else if (c == '*') {
                    s.push(s.pop()*s.pop()); 
                } else if (c == '-' || c == '/' || c == '%') {
                    double d1 = s.pop();
                    double d2 = s.pop();
                    switch (c) {
                        case '-': s.push(d2-d1); break;
                        case '%': s.push(d2%d1); break;
                        case '/': 
                            if (d1 != 0) {
                                s.push(d2/d1);
                            } else {
                                System.out.println("Cannot divide by 0"); // error
                                return null;
                            }
                    }   
                } else {
                    System.out.println("Unknown error detected"); // Omitting right bracket throws this error
                    return null;
                }
            } catch (Exception e) {
                System.out.println("Empty stack, not enough operands/too many operators"); // error
                return null;
            }
        }
        double result = s.pop();
        if (s.isEmpty()) {
            return result;
        } else {
            System.out.println("More than 1 value left at the end, not enough operators"); // self explanatory
            return null;
        }
    }

    private static int getPriority(char c) {
        if (c == '*' || c == '/' || c == '%') return 2; 
        else if (c == '+' || c == '-') return 1;
        else if (c == '(') return 0;
        else System.out.println("Unknown error occured"); return -1; // this should never be called
    }
}
