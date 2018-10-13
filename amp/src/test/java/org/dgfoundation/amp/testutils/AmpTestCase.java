package org.dgfoundation.amp.testutils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.NiUtils;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.fundingpledges.action.DisableableKeyValue;

import org.junit.Assert;

/**
 * intermediary class for holding various utility methods for testcases
 * @author Dolghier Constantin
 *
 */
public abstract class AmpTestCase extends Assert
{
    final BigDecimal BIG_DECIMAL_EPSI = BigDecimal.valueOf(0.0001d);

    public static final double DELTA_6 = 0.000001d;

    public void shouldFail(AmpRunnable runnable){
        shouldFail(runnable, null);
    }
    
    public void shouldFail(AmpRunnable runnable, Exception correctException) {
        try{
            runnable.run();
            throw new RuntimeException("did not throw exception");
        }
        catch(Throwable thr){
            if ("did not throw exception".equals(thr.getMessage()))
                throw new RuntimeException("code which should have failed did not throw an exception!");
            
            boolean shouldCheckException = correctException != null;
            
            if ((!shouldCheckException) || (shouldCheckException && thr.getClass() == correctException.getClass() && thr.getMessage().equals(correctException.getMessage()))) {
                // it is ok, we want an exception
                //System.out.println("caught an ok exception: " + thr.getMessage());
                return;
            }
            throw new RuntimeException(String.format("code which should have failed with (%s %s) has instead failed with (%s %s)", correctException.getClass(), correctException.getMessage(), thr.getClass(), thr.getMessage()));
        }
    }
    
    /**
     * compares 2 BigDecimal values by value, not by value/scale (as equals() would do)
     * @param a
     * @param b
     */
    public void assertBigDecimalEquals(BigDecimal expected, BigDecimal given) {
        if (expected == null ^ given == null) {
            fail(String.format("expected: %s, given: %s", expected, given));
            return;
        }
        if (expected == null && given == null)
            return;
        BigDecimal delta = given.subtract(expected).abs();
        if (delta.compareTo(BIG_DECIMAL_EPSI) > 0)
            fail(String.format("expected: %s, given: %s", expected, given));
    }
    
    /**
     * sorts the elements of a collection ascendingly and then compares its toString() result with a given cor
     * @param cor
     * @param col
     */
    public void assertColEquals(String cor, Collection<?> col) {
        assertEquals(cor, new TreeSet<>(col).toString());
    }
    
    public void testDisableableKeyValues(Collection<DisableableKeyValue> res, DisableableKeyValue... cor){
        List<DisableableKeyValue> corList = new ArrayList<>(Arrays.asList(cor));
        List<DisableableKeyValue> resList = new ArrayList<>(res);
        Collections.sort(corList);
        Collections.sort(resList);
        assertEquals(corList.size(), resList.size());
        for(int i = 0; i < corList.size(); i++){
            assertEquals(corList.get(i), resList.get(i));
        }
    }
        
    /**
     * returns a toString() of a sorted list of the elements in the input 
     * @param in
     * @return
     */
    public static String sortedString(Collection<?> in) {
        return AmpCollections.sorted(in).toString();
    }
    
    public<K> String digestCellsList(List<K> cells, Function<K, String> digester) {
        return cells.stream().map(digester).collect(Collectors.toList()).toString();
    }

    /**
     * builds an {@link ImmutablePair} of the two given arguments
     * @param k
     * @param v
     * @return
     */
    public<K, V> ImmutablePair<K, V> pair(K k, V v) {
        return new ImmutablePair<>(k, v);
    }
    
    public<K, V> Map<K, V> buildMap(ImmutablePair<K, V> in) {
        return buildMap(Arrays.asList(in));
    }
    
    public<K, V> Map<K, V> buildMap(K k, V v) {
        return buildMap(pair(k, v));
    }
    
    public<K, V> Map<K, V> buildMap(ImmutablePair<K, V> in1, ImmutablePair<K, V> in2) {
        return buildMap(Arrays.asList(in1, in2));
    }
    
    public<K, V> Map<K, V> buildMap(List<ImmutablePair<K, V>> in) {
        Map<K, V> res = new HashMap<>();
        in.forEach(z -> NiUtils.failIf(res.put(z.k, z.v) != null, "same key specified twice"));
        return res;
    }
    
    /**
     * returns an always-yes predicate
     * @return
     */
    public<K> Predicate<K> yes() {
        return a -> true;
    }
    
    /**
     * returns an always-yes predicate
     * @return
     */
    public<K> Predicate<K> no() {
        return a -> false;
    }
    
    public void setLocale(String locale) {
        TLSUtils.offlineSetForcedLanguage(locale);
    }
}
