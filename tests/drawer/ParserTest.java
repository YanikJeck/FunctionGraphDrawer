package drawer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserTest {

    @Test
    void eval() {
        double val = 3.0;
        assertEquals(Math.sin(val), Parser.eval("sin(x)",val));
        assertEquals(Math.cos(val), Parser.eval("cos(x)",val));
        assertEquals(Math.tan(val), Parser.eval("tan(x)",val));
        assertEquals(Math.cos(val)/Math.sin(val), Parser.eval("cot(x)",val));
        assertEquals(Math.log(val), Parser.eval("log(x)",val));
        assertEquals(2.718, Parser.eval("e",val),2.718);
        assertEquals(3.141, Parser.eval("pi",val),3.141);
        assertEquals(val+val, Parser.eval("x+x",val),val+val);
        assertEquals(0.0, Parser.eval("x-x",val), 0.0);
        assertEquals(val*val, Parser.eval("x*x",val),val*val);
        assertEquals(1.0, Parser.eval("x/x",val), 1.0);
        assertEquals(val+val*Math.pow(val,val), Parser.eval("x+x*x^x",val));
        assertEquals( val*Math.pow(val,val)+2*val, Parser.eval("x+x*x^x+x",val));
        assertEquals((val+val)*Math.pow(val,val), Parser.eval("(x+x)*x^x",val));
        assertEquals(-val, Parser.eval("-x", val));
        assertEquals(val, Parser.eval("+x", val));
        assertEquals(Math.log(-val)*Math.log(-val), Parser.eval("log(x)^2", -val));
        assertEquals(Math.pow(Math.sin(Math.pow(-val,-val)),-val),Parser.eval("sin(x^x)^x",-val));
        InvalidParameterException exception = Assertions.assertThrows(
                InvalidParameterException.class, () -> Parser.eval("ln",val));
        InvalidParameterException exception2 = Assertions.assertThrows(
                InvalidParameterException.class, () -> Parser.eval("ln(x)",val));
        assertEquals("cant process function",exception.getMessage());
        assertEquals("cant process function",exception2.getMessage());
    }
}