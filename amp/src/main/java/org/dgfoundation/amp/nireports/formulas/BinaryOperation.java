package org.dgfoundation.amp.nireports.formulas;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * a {@link NiFormula} which has 2 operands. The operator is supplied via a constructor
 * @author Dolghier Constantin
 *
 */
class BinaryOperation implements NiFormula {

    final NiFormula left, right;
    final BiFunction<BigDecimal, BigDecimal, BigDecimal> calculator;
    final Set<String> deps;
    
    public BinaryOperation(NiFormula left, NiFormula right, BiFunction<BigDecimal, BigDecimal, BigDecimal> calculator) {
        this.left = left;
        this.right = right;
        this.calculator = calculator;
        this.deps = NiFormula.discoverDeps(left, right);
    }
    
    @Override
    public BigDecimal evaluate(Map<String, BigDecimal> datastore) {
        BigDecimal l = left.evaluate(datastore);
        BigDecimal r = right.evaluate(datastore);
        return calculator.apply(l, r);
    }

    @Override
    public Set<String> getDependencies() {
        return deps;
    }

}
