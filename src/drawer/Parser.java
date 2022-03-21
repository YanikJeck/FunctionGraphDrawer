//Created by Yanik Jeck
//March 2022
package drawer;
import java.security.InvalidParameterException;

/**
 * The Parser class calculates the result
 * of a mathematical expression in string form.
 *
 * The parser is able to parse all expressions
 * containing:
 *
 * The basic mathematical symbols:
 * +, -, /, *, (, ) and .
 *
 * The basic trigonometric functions:
 * sin(x), cos(x), tan(x) and cot(x)
 * where x may be any expression.
 *
 * Powers:
 * Where a^x means "a raised to x"
 * where both a and x ay be
 * any valid expression.
 *
 * Logarithms:
 * log(x) returns the natural logarithm of
 * the expression x.
 *
 * The parser does not recognise
 * sqrt as the square root or any root for
 * that matter. The nth root can calculated
 * using x^(1/n) where x is any expression
 * and n the root to be taken.
 */
public class Parser {
    /**
     * Holds the expression
     * that is to be evaluated.
     */
    private static String expr;

    /**
     * Takes in a String representing
     * a mathematical expression and a double value
     * which replaces all occurrences
     * of 'x' in the expression.
     * @param expr String representing the expression to be evaluated.
     * @param val Double value which replaces all occurrences of 'x'
     *            in the expression.
     * @return Returns the result of the expression passed in expr.
     */
    public static double eval(String expr,double val) {
        Parser.expr = expr;
        Parser.expr = Parser.expr.replaceAll("e", "2.718");
        Parser.expr = Parser.expr.replaceAll("pi","3.141");
        Parser.expr = Parser.expr.replaceAll("x",  "(" +val + ")");
        return expression();
    }

    /**
     * Calculates the cotangents of val.
     * @param val Double value to get the cotangents of.
     * @return Returns the cotangents of val.
     */
    private static double cot(double val) {
        return Math.cos(val)/Math.sin(val);
    }

    /**
     * Takes in a char c and returns
     * whether c is the starting letter of any trigonometric
     * or log function.
     * @param c Char to be checked.
     * @return Returns true if c equals 's','c','t' or l
     * and false if not.
     */
    private static boolean isFunction(char c) {
        return c == 's' || c == 'c' || c == 't' || c == 'l';
    }

    /**
     * Takes in char c and determines whether it
     * is a basic operator such as '+', '-', '*' or '/'.
     * @param c Char representing a potential operator.
     * @return Returns -1 if c is not an operator,
     * 0 if it is either '+' or '-' and
     * 1 if it is either '*' or '/'.
     */
    private static byte isBasicOperator(char c) {
        byte state;
        if (c == '+' || c == '-')
            state = 0;
        else if (c == '*' || c == '/')
            state = 1;
        else
            state = -1;
        return state;
    }

    /**
     * The function signedSummand identifies
     * the sign of the following summand and
     * returns it as such.
     *
     * @return Returns the result of summand()
     * according to the appropriate sign.
     */
    private static double signedSummand() {
        char c = expr.charAt(0);
        expr = expr.substring(1);
        return (c == '+') ? summand() : -summand();
    }

    /**
     * The function expression is the entrypoint
     * to parsing the mathematical expression.
     * The function handles everything concerning addition
     * and subtraction.
     *
     * Since the function is the entrypoint and the
     * initial expression might not start with either '+' or '-',
     * the function has to handle this case beforehand as
     * to ensure that any expression is processed properly.
     *
     * @return Returns the final value of the expression
     * passed to Parser.
     * Additionally, returns sub results of summands.
     */
    private static double expression() {
        double result = (isBasicOperator(expr.charAt(0)) == 0) ? signedSummand() : summand();
        while (expr.length() != 0 && isBasicOperator(expr.charAt(0)) == 0)
            result += signedSummand();
        return result;
    }

    /**
     * Evaluates a subexpression of the form [+-] x [+-]
     * where [] contains the symbols that may appear before or
     * after a summand and x may be any expression.
     * @return Returns the double value of this summand.
     */
    private static double summand() {
        double result = factor();
        char c;
        while (expr.length() != 0 && isBasicOperator(expr.charAt(0)) == 1) {
            c = expr.charAt(0);
            expr = expr.substring(1);
            result *= (c == '*') ? factor() : 1.0/factor();
        }
        return result;
    }

    /**
     * Distinguishes between non operators
     * and calls the corresponding functions
     * to match the specific operand.
     *
     * possible operands are subexpressions,
     * mathematical functions such as sin(x)
     * or numbers.
     *
     * Since any term could potentially be raised to another term
     * the result of factor is always passed into the exponent function
     * to ensure all raises are handled properly.
     * @return Returns the value of an operand evaluated in its
     * corresponding function.
     */
    private static double factor() {
        double result;
        if (expr.charAt(0) == '(') {
            expr = expr.substring(1);
            result = expression();
            expr = expr.substring(1);
        }
        else if (isFunction(expr.charAt(0))) {
            result = function();
            expr = expr.substring(1);
        }
        else
            result = number();
        return exponent(result);
    }

    /**
     * Extracts a substring from the expression and
     * matches it with the corresponding mathematical
     * function.
     * @return The double value of the following expression
     * as input to a mathematical function such as sin(x).
     */
    private static double function() {
        double result;
        if (expr.length() < 4)
            throw new InvalidParameterException("cant process function");
        String tmp = expr.substring(0,4);
        expr = expr.substring(4);
        result = switch (tmp) {
            case "sin(" -> Math.sin(expression());
            case "cos(" -> Math.cos(expression());
            case "tan(" -> Math.tan(expression());
            case "cot(" -> cot(expression());
            case "log(" -> Math.log(expression());
            default -> throw new InvalidParameterException("cant process function");
        };
        return result;
    }

    /**
     * Identifies numeric values in the expression
     * and returns them as a double value.
     * @return Returns the double value of a subexpression of the String expr.
     */
    private static double number() {
        double result;
        int pos = 0;
        while (expr.length() != pos && (Character.isDigit(expr.charAt(pos)) || expr.charAt(pos) == '.'))
            ++pos;
        result = Double.parseDouble(expr.substring(0, pos));
        expr = expr.substring(pos);
        return result;
    }

    /**
     * Takes in a double res
     * and raises it to the following subexpression
     * if a '^' is present.
     * @param res Double value to be raised to another expression.
     * @return Returns res raised to the following expression
     * depending on the presence of a '^'.
     * If not present, res is returned.
     */
    private static double exponent(double res) {
        while (expr.length() != 0 && expr.charAt(0) == '^') {
            expr = expr.substring(1);
            res = Math.pow(res, factor());
        }
        return res;
    }
}
