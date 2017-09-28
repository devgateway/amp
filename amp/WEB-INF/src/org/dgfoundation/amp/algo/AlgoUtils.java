package org.dgfoundation.amp.algo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.digijava.module.aim.util.Identifiable;


/**
 * various algorithmical utils
 * @author Dolghier Constantin
 *
 */
public class AlgoUtils {
    
    /**
     * recursively get all ancestors (parents) OR descendants of a set by a wave algorithm
     * @param inIds
     * @return
     */
    public static<K> Set<K> runWave(Collection<K> inIds, Waver<K> waver)
    {
        Set<K> result = new HashSet<K>();
        if (inIds == null)
            return result;
        Set<K> currentWave = new HashSet<K>();
        currentWave.addAll(inIds);
        while (currentWave.size() > 0)
        {
            result.addAll(currentWave);
            currentWave = waver.wave(currentWave);
            currentWave.removeAll(result); // in case there is a cycle somewhere in the DB, do not cycle forever
        }
        return result;
    }
    
    /**
     * collects all the ids in a set
     * @param set
     * @param input
     * @return
     */
    public static Set<Long> collectIds(Set<Long> set, Collection<? extends Identifiable> input)
    {
        for(Identifiable elem:input)
            set.add((Long) elem.getIdentifier());
        return set;
    }
    
    /**
     * sorts an array of Identifiable objects by the enclosed ids
     * @param objs
     * @return
     */
    public static<K extends Identifiable> List<K> sortByIds(Collection<K> objs){
        List<K> res = new ArrayList<>(objs);
        Collections.sort(res, new Comparator<Identifiable>(){
            @Override public int compare(Identifiable arg0, Identifiable arg1) {
                Long id0 = (Long) arg0.getIdentifier();
                Long id1 = (Long) arg1.getIdentifier();
                return id0.compareTo(id1);
            }
        });
        return res;
    }
    
    /**
     * returns null if an Identifiable is null, else returns the wrapper identifier
     * @param id
     * @return
     */
    public static Long getIdFrom(Identifiable id) {
        return id == null ? null : (Long)id.getIdentifier();
    }
    
    public static Long getLongFrom(String z) {
        if (z == null) return null;
        try {
            return Long.parseLong(z);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * returns a list containing the result of applying Long.parseLong() to the elements of a list
     * @param in
     * @return
     */
    public static List<Long> collectLongs(Collection<String> in) {
        ArrayList<Long> res = new ArrayList<>();
        for(String s:in)
            res.add(Long.parseLong(s));
        return res;
    }
    
    /**
     * builds an MD5-digester instance
     * @return
     */
    public static MessageDigest getMD5Digester(){
        try{
            return MessageDigest.getInstance("MD5");
        }
        catch(NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }
    
    /**
     * digests a String
     * @param digester - the digester to use. If null, then {@link #getMD5Digester()} is used to get one
     * @param str
     * @return
     */
    public static String digestString(MessageDigest digester, String str)
    {
        MessageDigest messageDigest = digester == null ? getMD5Digester() : digester;
        byte[] hash = messageDigest.digest(str.getBytes());
        // Convert to hex string
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            sb.append(Integer.toHexString(0xff & hash[i]));
        }
        String md5 = sb.toString();
        return md5;
    }
    
    private static double[] POWER_OF_TEN = computePowerOfTen();
    private static double[] computePowerOfTen() {
        double[] res = new double[15];
        res[0] = 1;
        for(int i = 1; i< res.length; i++) {
            res[i] = res[i - 1] / 10;
        }
        return res;
    }
    
    /**
     * discards the nonrelevant decimals of a double. NonRelevant: is >nrDecimals
     * @param in
     * @param nrDecimals
     * @return
     */
    public static double keepNDecimals(double in, int nrDecimals) {
        if (Math.abs(in) < POWER_OF_TEN[nrDecimals] && (Math.abs(in) > Double.MIN_VALUE))
            return POWER_OF_TEN[nrDecimals];
        return Double.parseDouble(String.format(Locale.US, "%." + nrDecimals + "f", in));
    }
    
    /**
     * skips at most N positions in an iterator
     * @param iter
     * @param n
     * @return
     */
    public static<K> Iterator<K> skipNPositions(Iterator<K> iter, int n) {
        while (n > 0 && iter.hasNext()) {
            iter.next();
            n --;
        }
        return iter;
    }
    
    /**
     * encapsulates nonRuntimeException in runtimeerrorexceptions
     * @param e
     */
    public static RuntimeException translateException(Exception e){
        if (e instanceof RuntimeException)
            return (RuntimeException) e;
        return new RuntimeException(e);
    }
    
    
    public static <K, V> Map<V, K> reverseIntoHashMap(Map<K, V> toBeReversed) {
        Map<V, K> res = new HashMap<>();
        toBeReversed.forEach((key, value) -> res.put(value, key));
        return res;
    }
    
    /**
     * use it for inverting parent-child relationships
     * @return
     */
    public static<K, V, Z extends Collection<K>> Map<V, Z> inverseMap(Map<K, V> inMap, Map<V, Z> initMap, Supplier<Z> inner) {
        inMap.forEach((key, value) -> {
            initMap.computeIfAbsent(value, z -> inner.get()).add(key);
        });
        return initMap;
    }
    
    public static<K extends Comparable<K>> K min(K a, K b) {
        if (a.compareTo(b) <= 0)
            return a;
        return b;
    }

    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) > 0 ? a : b;
    }
}
