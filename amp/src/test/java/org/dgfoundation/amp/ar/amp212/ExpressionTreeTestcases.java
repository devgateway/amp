package org.dgfoundation.amp.ar.amp212;

import org.dgfoundation.amp.nireports.formulas.NiFormula;
import org.dgfoundation.amp.nireports.output.nicells.NiFormulaicAmountCell;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.dgfoundation.amp.nireports.formulas.NiFormula.*;


/**
 * 
 * testcases for expression trees
 * @author Constantin Dolghier
 *
 */
public class ExpressionTreeTestcases extends AmpTestCase {
    
    Map<String, BigDecimal> vars = new HashMap<String, BigDecimal>() {{
        put("one", BigDecimal.ONE);
        put("zero", BigDecimal.ZERO);
        put("ten", BigDecimal.TEN);
        put("undefined", NiFormulaicAmountCell.UNDEFINED);
        put("infinity", NiFormulaicAmountCell.PLUS_INFINITY);
    }};
    
    void testDep(String cor, NiFormula formula) {
        assertEquals(cor, sortedString(formula.getDependencies()));
    }
    
    static class MyPair<K, V> {
        final K key;
        final V value;
        
        public MyPair(K k, V v) {
            this.key = k;
            this.value = v;
        }
    }
    
    /**
     * returns MyPair<decimal, should-compare-by-identity>
     * @param str
     * @return
     */
    MyPair<BigDecimal, Boolean> str2bd(String str) {
        switch(str) {
            case "undefined":
                return new MyPair<>(NiFormulaicAmountCell.UNDEFINED, true);

            case "null":
                return new MyPair<>(null, true);
        
            case "infinity":
                return new MyPair<>(NiFormulaicAmountCell.PLUS_INFINITY, true);

            default:
                return new MyPair<>(new BigDecimal(str), false);
        }
    }
    
    void testVal(String cors, NiFormula formula) {
        BigDecimal value = formula.evaluate(vars);
        MyPair<BigDecimal, Boolean> cor = str2bd(cors);
        if (cor.value) {
            assertEquals(cor.key, value);
        } else {
            assertBigDecimalEquals(cor.key, value);
        }
    }
    
    @Test
    public void testDependenciesLeaves() {
        testDep("[]", CONSTANT(2));
        testDep("[var1]", VARIABLE("var1"));
    }
    
    @Test
    public void testDependenciesBinaryExpressions() {
        testDep("[]", ADD(CONSTANT(2), CONSTANT(3)));
        testDep("[var1]", DIVIDE(CONSTANT(2), VARIABLE("var1")));
        testDep("[aaa, var1]", MULTIPLY(VARIABLE("var1"), VARIABLE("aaa")));
        testDep("[aaa, bbb]", PERCENTAGE("bbb", "aaa"));
    }
    
    @Test
    public void testEvalUnitaries() {
        testVal("1", CONSTANT(1));
        testVal("10", VARIABLE("ten"));
    }

    @Test
    public void testEvalExpressions() {
        testVal("3", ADD(CONSTANT(2), CONSTANT(1)));
        testVal("11", ADD(CONSTANT(1), VARIABLE("ten")));
        testVal("11", ADD(VARIABLE("one"), VARIABLE("ten")));
        testVal("21", MULTIPLY(CONSTANT(3), CONSTANT(7)));
        testVal("4", DIVIDE(ADD(CONSTANT(95), CONSTANT(5)), SUBTRACT(CONSTANT(27), CONSTANT(2))));
        testVal("1", SUBTRACTIFGREATER(CONSTANT(2), CONSTANT(1)));
        testVal("0", SUBTRACTIFGREATER(CONSTANT(1), CONSTANT(3)));
        testVal("0", SUBTRACTIFGREATER(CONSTANT(0), CONSTANT(0)));
    }
    
    @Test
    public void testUnknownVars() {
        testVal("undefined", VARIABLE("unexistant"));
        testVal("undefined", ADD(CONSTANT(2), VARIABLE("unexistant")));
        testVal("undefined", ADD(MULTIPLY(CONSTANT(2), CONSTANT(3)), MULTIPLY(CONSTANT(2), VARIABLE("dada"))));
    }
    
    @Test
    public void testPropagation() {
        NiFormula twoByZero = DIVIDE(CONSTANT(2), CONSTANT(0));
        testVal("undefined", twoByZero);
        testVal("undefined", ADD(twoByZero, CONSTANT(15)));
    }
    
    @Test
    public void testDivision() {
        NiFormula oneByThree = DIVIDE(VARIABLE("one"), CONSTANT(3));
        testVal("0.3333333333333333333", oneByThree);
        testVal("1", DIVIDEIFLOWER(CONSTANT(3), CONSTANT(3)));
        testVal("1", DIVIDEIFLOWER(CONSTANT(4), CONSTANT(3)));
        testVal("0.3333333333333333333", DIVIDEIFLOWER(CONSTANT(1), CONSTANT(3)));
        testVal("1", DIVIDEIFLOWER(CONSTANT(0), CONSTANT(0)));
        testVal("0", DIVIDEIFLOWER(CONSTANT(0), CONSTANT(5)));
        testVal("0", DIVIDEIFLOWER(CONSTANT(5), CONSTANT(0)));
    }
    
    @Test
    public void testPercentage() {
        testVal("200", PERCENTAGE(SUBTRACTIFGREATER(CONSTANT(9), CONSTANT(3)), CONSTANT(3)));
        testVal("50", PERCENTAGE(SUBTRACTIFGREATER(CONSTANT(6), CONSTANT(4)), CONSTANT(4)));
        testVal("0", PERCENTAGE(SUBTRACTIFGREATER(CONSTANT(3), CONSTANT(3)), CONSTANT(3)));
        testVal("0", PERCENTAGE(SUBTRACTIFGREATER(CONSTANT(1), CONSTANT(3)), CONSTANT(3)));
        testVal("100", PERCENTAGEIFLOWER(CONSTANT(4), CONSTANT(3)));
        testVal("100", PERCENTAGEIFLOWER(CONSTANT(3), CONSTANT(3)));
        testVal("25", PERCENTAGEIFLOWER(CONSTANT(1), CONSTANT(4)));
        testVal("0", PERCENTAGEIFLOWER(CONSTANT(1), CONSTANT(0)));
        testVal("100", PERCENTAGEIFLOWER(CONSTANT(0), CONSTANT(0)));
    }
    
}
