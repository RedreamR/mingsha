package team.rainfall.mingsha.counter;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.GameCalendar;
import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.util.Objects;

public class ExpressionProcessor {

    private static Expression build(int iCivID, String expStr) {
        expStr = expStr.replace("@turn", String.valueOf(GameCalendar.TURNID));
        expStr = expStr.replace("$", "a$");
        Expression expression = new Expression(expStr);
        for (String variable : expression.getUsedVariables()) {
            if (variable.startsWith("a$")) {
                expression.setVariable(variable, BigDecimal.valueOf(CounterStore.getValue(iCivID, variable.substring(2))));
            }
        }
        return expression;
    }

    public static int compute(int iCivID, String expStr) {
        try {
            return build(iCivID, expStr).eval().intValue();
        } catch (Exception e) {
            CFG.exceptionStack(e);
            return 0;
        }
    }

    public static boolean satisfied(int iCivID, String expStr) {
        try {
            return Objects.equals(build(iCivID, expStr).eval().stripTrailingZeros(), BigDecimal.ONE.stripTrailingZeros());
        } catch (Exception e) {
            CFG.exceptionStack(e);
            return false;
        }
    }
}
