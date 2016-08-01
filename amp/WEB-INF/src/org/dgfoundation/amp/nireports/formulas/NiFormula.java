package org.dgfoundation.amp.nireports.formulas;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.dgfoundation.amp.nireports.output.nicells.NiFormulaicAmountCell.isDefined;
import static org.dgfoundation.amp.nireports.output.nicells.NiFormulaicAmountCell.UNDEFINED;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * an expression to be evaluated as function of a set of variables
 * @author Dolghier Constantin
 *
 */
public interface NiFormula {
	
	public BigDecimal evaluate(Map<String, BigDecimal> datastore);
	public Set<String> getDependencies();
		
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

	public static NiFormula ifDefined(NiFormula left, NiFormula right, BiFunction<BigDecimal, BigDecimal, BigDecimal> func) {
		return new BinaryOperation(left, right, (a, b) -> isDefined(a) && isDefined(b) ? func.apply(a, b) : UNDEFINED); 
	}

	public static NiFormula condition(NiFormula left, NiFormula right, BiFunction<BigDecimal, BigDecimal, Boolean> cond, BiFunction<BigDecimal, BigDecimal, BigDecimal> func) {
		return new BinaryOperation(left, right, (a, b) -> isDefined(a) && isDefined(b) && cond.apply(a, b)? func.apply(a, b) : UNDEFINED); 
	}

	public static NiFormula ADD(NiFormula left, NiFormula right) {
		return ifDefined(left, right, BigDecimal::add);
	}

	public static NiFormula SUBTRACT(NiFormula left, NiFormula right) {
		return ifDefined(left, right, BigDecimal::subtract);
	}

	public static NiFormula MULTIPLY(NiFormula left, NiFormula right) {
		return ifDefined(left, right, BigDecimal::multiply);
	}

	public static NiFormula DIVIDE(NiFormula left, NiFormula right) {
		return condition(left, right, (a, b) -> b.compareTo(BigDecimal.ZERO) != 0, (a, b) -> a.divide(b, DIVISION_MC));
	}

	public static NiFormula MINIMUM(NiFormula left, NiFormula right) {
		return ifDefined(left, right, BigDecimal::min);
	}

	public static NiFormula MAXIMUM(NiFormula left, NiFormula right) {
		return ifDefined(left, right, BigDecimal::max);
	}

	public static NiFormula PERCENTAGE(NiFormula left, NiFormula right) {
		return MULTIPLY(DIVIDE(left, right), CONSTANT(100));
	}

	public static NiFormula PERCENTAGE(String left, String right) {
		return PERCENTAGE(VARIABLE(left), VARIABLE(right));
	}
	
	public static NiFormula ZERO = CONSTANT(BigDecimal.ZERO);
	public static NiFormula ONE = CONSTANT(BigDecimal.ONE);
	public static MathContext DIVISION_MC = new MathContext(100, RoundingMode.HALF_EVEN);
}
