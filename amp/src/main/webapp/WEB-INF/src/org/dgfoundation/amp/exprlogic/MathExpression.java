package org.dgfoundation.amp.exprlogic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * @author Sebastian Dimunzio Apr 15, 2009
 */
public class MathExpression {
    /**
     * Enumeration of all operations
     * 
     * @author Sebastian Dimunzio Apr 27, 2009
     */
    public enum Operation {
        ADD, SUBTRACT, DIVIDE, DIVIDE_ROUND_TWO_DECIMALS, MULTIPLY, DIVIDE_ROUND_DOWN, DIVIDE_ROUND_UP, DATE_MONTH_DIFF, DATE_DAY_DIFF, SUM;
    }

    private Operation operation = null;
    private HashMap<String, BigDecimal> values = null;
    private Object oper1;
    private Object oper2;
    private int scale = 10;

    /**
     * Create a new Expression
     * 
     * @param operation
     * @param operand1
     *            (String,BigDecimal or Expression)
     * @param var2
     *            (String,BigDecimal or Expression)
     * @throws Exception
     *             Note: String: The String will be used as variable getting the
     *             value from the values map. BigDecimal: The operand will be
     *             used in the operation with no replacements. MathExpression:
     *             The code will call to the result() method of the operand
     *             passing as arguments the current variables values using the
     *             result value as operand in the the operation.
     */
    public MathExpression(Operation operation, Object operand1, Object operand2) throws Exception {
        this.operation = operation;
        if (!(operand1 instanceof String) && !(operand1 instanceof BigDecimal) && !(operand1 instanceof MathExpression)) {
            throw new Exception("Invalid Parameter Type [operand1], Allowed Types are  String, BigDecimal or Expression ");
        }

        if (!(operand2 instanceof String) && !(operand2 instanceof BigDecimal) && !(operand2 instanceof MathExpression)) {
            throw new Exception("Invalid Parameter Type [operand2], Allowed Types are  String, BigDecimal or Expression ");
        }
        this.oper1 = operand1;
        this.oper2 = operand2;
    }

    /**
     * Returns the mathematical result
     * 
     * @param values
     * @return values: A variable map, (name,value)
     */
    public BigDecimal result(HashMap<String, BigDecimal> values) {
        try {
            this.values = values;
            return result();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Return the expression value
     * 
     * @return BigDecimal
     * @throws Exception
     * 
     */
    private BigDecimal result() throws Exception {
        BigDecimal var1 = null;
        BigDecimal var2 = null;

        if (oper1 instanceof String) {
            var1 = values.get(oper1);
        }
        if (oper2 instanceof String) {
            var2 = values.get(oper2);
        }

        if (oper1 instanceof MathExpression) {
            var1 = ((MathExpression) oper1).result(values);
        }
        if (oper2 instanceof MathExpression) {
            var2 = ((MathExpression) oper2).result(values);
        }

        if (oper1 instanceof BigDecimal) {
            var1 = (BigDecimal) oper1;
        }
        if (oper2 instanceof BigDecimal) {
            var2 = (BigDecimal) oper2;
        }

        if (var1 == null || var2 == null) {
            return null;
        } else {
            return result(var1, var2);
        }
    }

    /**
     * Return the Expression Result
     * 
     * @param oper1
     * @param oper2
     * @return BigDecimal
     * @throws Exception
     */
    private BigDecimal result(BigDecimal oper1, BigDecimal oper2) throws Exception {
        try {
            if ((oper1 == null) || (oper2 == null)) {
                return null;
            }
            switch (this.operation) {
            case ADD:
                return oper1.add(oper2);
            case SUBTRACT:
                return oper1.subtract(oper2);
            case DIVIDE:
                if (oper2.doubleValue() == 0d) {
                    return new BigDecimal(0);
                }
                return oper1.divide(oper2, this.scale, RoundingMode.HALF_UP);
            case DIVIDE_ROUND_DOWN:
                if (oper2.doubleValue() == 0d) {
                    return new BigDecimal(0);
                }
                return oper1.divide(oper2, RoundingMode.DOWN);
            case DIVIDE_ROUND_UP:
                if (oper2.doubleValue() == 0d) {
                    return new BigDecimal(0);
                }
                return oper1.divide(oper2, RoundingMode.UP);
            case DIVIDE_ROUND_TWO_DECIMALS:
                if (oper2.doubleValue() == 0d) {
                    return new BigDecimal(0);
                }
                return oper1.divide(oper2, 2, RoundingMode.HALF_UP);

            case MULTIPLY:
                return oper1.multiply(oper2);
            case DATE_MONTH_DIFF:
                return getMonthDifference(oper1, oper2);
            case DATE_DAY_DIFF:
                return getDayDifference(oper1, oper2);
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        throw new Exception("No Operation Selected");
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    /**
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static BigDecimal getMonthDifference(BigDecimal date1, BigDecimal date2) {
        int count = 0;
        GregorianCalendar fromCalendar = new GregorianCalendar(), toCalendar = new GregorianCalendar();
        fromCalendar.setTimeInMillis(date2.longValue());
        toCalendar.setTimeInMillis(date1.longValue());

        boolean negaitveResult = false;
        if (fromCalendar.after(toCalendar)) {
            negaitveResult = true;
            fromCalendar.setTimeInMillis(date1.longValue());
            toCalendar.setTimeInMillis(date2.longValue());
        }
        for (fromCalendar.add(Calendar.MONTH, 1); fromCalendar.compareTo(toCalendar) <= 0; fromCalendar.add(Calendar.MONTH, 1)) {
            count++;
        }

        return new BigDecimal(count * ((negaitveResult) ? -1 : 1));
    }
    
    public static BigDecimal getDayDifference(BigDecimal date1, BigDecimal date2) {
        /*
         * long fromMillis = date2.longValue(); long toMillis = date1.longValue(); int negative = 1; if (fromMillis > toMillis) { negative = -1; } long diffMillis = toMillis - fromMillis; return new
         * BigDecimal(negative * (diffMillis / 1000 / 60 / 60 / 24));
         */
        DateTime start = new DateTime(date2.longValue());
        DateTime end = new DateTime(date1.longValue());
        Days d = null;
        if (start.isBefore(end) || start.isEqual(end)) {
            d = Days.daysBetween(start, end);
        } else {
            d = Days.daysBetween(end, start);
        }
        int days = d.getDays();
        return new BigDecimal(days);
    }

    public boolean constainsVariable(String var) {
        if (oper1 instanceof String) {
            if (var.equalsIgnoreCase((String) oper1)) {
                return true;
            }

        }
        if (oper2 instanceof String) {
            if (var.equalsIgnoreCase((String) oper2)) {
                return true;
            }
        }

        if (oper1 instanceof MathExpression) {
            MathExpression var1 = ((MathExpression) oper1);
            return var1.constainsVariable(var);
        }

        if (oper2 instanceof MathExpression) {
            MathExpression var1 = ((MathExpression) oper2);
            return var1.constainsVariable(var);
        }

        return false;
    }
    
    @Override
    public String toString()
    {
        if (oper1 == null)
            return operation.name(); 
        if (oper2 == null)          
            return String.format("(%s %s)", operation.name(), oper1.toString());
        
        return String.format("(%s %s %s)", oper1.toString(), operation.name(), oper2.toString());
    }
}
