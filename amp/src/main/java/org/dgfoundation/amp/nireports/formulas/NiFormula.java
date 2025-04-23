package org.dgfoundation.amp.nireports.formulas;

import org.dgfoundation.amp.nireports.output.nicells.NiFormulaicAmountCell;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.dgfoundation.amp.nireports.output.nicells.NiFormulaicAmountCell.UNDEFINED;
import static org.dgfoundation.amp.nireports.output.nicells.NiFormulaicAmountCell.isDefined;

/**
 * An expression to be evaluated as function of a set of variables (also called <i>tokens</i>).
 * The expression offers 2 functions in its interface: one which, given the set of tokens, returns a value ({@link #evaluate(Map)}) 
 * and another one which returns the set of tokens an expression depends on. <br />
 * The result of evaluating a formula is either a number OR one of the error flags. 
 * Any of the following values, when returned, is an error flag: {@link NiFormulaicAmountCell#UNDEFINED}, {@link NiFormulaicAmountCell#MINUS_INFINITY}, {@link NiFormulaicAmountCell#PLUS_INFINITY}
 * (please do the comparisons by identity, not by value - take {@link #evaluateOrUndefined(Map, BigDecimal)} as an example)
 * 
 * NiReports comes packed with a tree model implementation of multiple operations, whereas an individual {@link NiFormula} instance is the root of a tree OR a terminal node.
 * 
 * @author Dolghier Constantin
 *
 */
public interface NiFormula {
    
    /**
     * evaluates this formula given the values of tokens
     * @param datastore the tokens to be used as part of evaluation
     * @return the value. The behaviour is undefined (upto crash or returning {@link NiFormulaicAmountCell#UNDEFINED}) in case the datastore does not contain all the tokens requested by {@link #getDependencies()}
     */
    public BigDecimal evaluate(Map<String, BigDecimal> datastore);
    
    /**
     * returns the tokens this formula depends on
     */
    public Set<String> getDependencies();
    
    /**
     * evaluates an expression. If the result is undefined, returns a given value instead
     */
    public default BigDecimal evaluateOrUndefined(Map<String, BigDecimal> datastore, BigDecimal undefValue) {
        BigDecimal res = evaluate(datastore);
        return isDefined(res) ? res : undefValue;
    }
        
    /**
     * builds an expression whose value is evaluated by a given callback and which asks a given set of dependencies
     * @param func the callback used to calculate the return value
     * @param deps the dependencies to ask of callers
     * @return
     */
    public static NiFormula build(Function<Map<String, BigDecimal>, BigDecimal> func, String...deps) {
        return new NiFormula() {
            @Override
            public Set<String> getDependencies() {
                return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(deps)));
            }
            
            @Override
            public BigDecimal evaluate(Map<String, BigDecimal> datastore) {
                return func.apply(datastore);
            }
        };
    }
    
    /**
     * builds the reunion of the set of dependencies of a given array of {@link NiFormula} instances
     * @return
     */
    public static Set<String> discoverDeps(NiFormula...deps) {
        Set<String> res = new HashSet<>();
        for(NiFormula t:deps)
            res.addAll(t.getDependencies());
        return Collections.unmodifiableSet(res);
    }
    
    public static NiFormula CONSTANT(BigDecimal v) {
        return build(ds -> v);
    }

    public static NiFormula CONSTANT(int v) {
        return CONSTANT(BigDecimal.valueOf(v));
    }

    public static NiFormula VARIABLE(String name) {
        return build(ds -> Optional.ofNullable(ds.get(name)).orElse(UNDEFINED), name);
    }

    /**
     * builds a {@link NiFormula} which returns the result of calling a callback on two {@link BigDecimal} instances (the operands).
     * These operands are evaluated, at runtime, from two given {@link NiFormula} instances. In case any of the operands returns an undefined value, the constructed formula 
     * will return UNDEFINED instead of calling the supplied callback.
     * @param left the left operand
     * @param right the right operand
     * @param func the function to operate on the results of the 2 operands
     * @return
     */
    public static NiFormula ifDefined(NiFormula left, NiFormula right, BiFunction<BigDecimal, BigDecimal, BigDecimal> func) {
        return new BinaryOperation(left, right, (a, b) -> isDefined(a) && isDefined(b) ? func.apply(a, b) : UNDEFINED); 
    }

    /**
     * the same as {@link #ifDefined(NiFormula, NiFormula, BiFunction)}, with the addition that the given function will be used to evaluate the node iff a given condition returns true
     * @param left the left operand
     * @param right the right operand
     * @param cond the addition condition to impose on the results of the operands before forwarding them to the calculating callback
     * @param func the function to operate on the results of the 2 operands
     * @return
     */
    public static NiFormula condition(NiFormula left, NiFormula right, BiFunction<BigDecimal, BigDecimal, Boolean> cond, BiFunction<BigDecimal, BigDecimal, BigDecimal> func) {
        return new BinaryOperation(left, right, (a, b) -> isDefined(a) && isDefined(b) && cond.apply(a, b) ? func.apply(a, b) : UNDEFINED); 
    }
    
    public static NiFormula ADD(NiFormula left, NiFormula right) {
        return ifDefined(left, right, BigDecimal::add);
    }

    public static NiFormula SUBTRACT(NiFormula left, NiFormula right) {
        return ifDefined(left, right, BigDecimal::subtract);
    }
    
    public static NiFormula SUBTRACTIFGREATER(NiFormula left, NiFormula right) {
        return condition(left, right, (a, b) -> a.compareTo(b) >= 0, BigDecimal::subtract);
    }

    public static NiFormula MULTIPLY(NiFormula left, NiFormula right) {
        return ifDefined(left, right, BigDecimal::multiply);
    }

    public static NiFormula DIVIDE(NiFormula left, NiFormula right) {
        return condition(left, right, (a, b) -> b.compareTo(BigDecimal.ZERO) != 0, (a, b) -> a.divide(b, DIVISION_MC));
    }
    
    /**
     * 
     * the result of DIVIDEIFLOWER(A, B) is
     * 1, if A >= B OR (A == 0 AND B == 0)
     * A/B, if A <= B AND B != 0 
     * 0, if (B == 0 AND A <> 0) OR (A == 0 AND B <> 0)
     * 
     * @param left
     * @param right
     * @return formula
     */
    public static NiFormula DIVIDEIFLOWER(NiFormula left, NiFormula right) {
        return condition(left, right, (a, b) -> b.compareTo(BigDecimal.ZERO) != 0 || (a.compareTo(BigDecimal.ZERO) == 0 && b.compareTo(BigDecimal.ZERO) == 0), 
                (a, b) -> a.compareTo(b) >= 0 ? BigDecimal.ONE : a.divide(b, DIVISION_MC));
    }

    public static NiFormula MINIMUM(NiFormula left, NiFormula right) {
        return ifDefined(left, right, BigDecimal::min);
    }

    public static NiFormula MAXIMUM(NiFormula left, NiFormula right) {
        return ifDefined(left, right, BigDecimal::max);
    }

    /**
     * builds an expression which returns the left operand as a percentage of the right operand
     */
    public static NiFormula PERCENTAGE(NiFormula left, NiFormula right) {
        return MULTIPLY(DIVIDE(left, right), CONSTANT(100));
    }

    /**
     * builds an expression which returns the left token as a percentage of the right token
     */
    public static NiFormula PERCENTAGE(String left, String right) {
        return PERCENTAGE(VARIABLE(left), VARIABLE(right));
    }
    
    /**
     * See {@link NiFormula.DIVIDEIFLOWER)
     * 
     */
    public static NiFormula PERCENTAGEIFLOWER(NiFormula left, NiFormula right) {
        return MULTIPLY(DIVIDEIFLOWER(left, right), CONSTANT(100));
    }
    
    public static NiFormula ZERO = CONSTANT(BigDecimal.ZERO);
    public static NiFormula ONE = CONSTANT(BigDecimal.ONE);
    public static MathContext DIVISION_MC = new MathContext(100, RoundingMode.HALF_EVEN);
}
