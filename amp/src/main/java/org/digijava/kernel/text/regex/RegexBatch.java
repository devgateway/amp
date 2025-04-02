package org.digijava.kernel.text.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Batch of regular expression processing.
 * Allows reduce memory and CPU usage by splitting too complex regex into small simple steps of text processing.  
 * It is useful if you want to process text(s) with several regex in 
 * some special order and use special flags which are not used in String.replaceAll() method.
 * Also this class allows to have some kind of tree of batches. For this reason batch can be created
 * in two modes: Normal and Parent mode.
 * In normal mode it operates on regex strings and processes texts with those regex patterns. 
 * Patterns are compiled using flags at construction time.
 * In parent mode batch operates on list of children (nested) batches which themselves can be in any of these two modes.
 * Again order of children (nested) batches are preserved when processing texts.
 * NOTE: Currently only {@link #replaceAll(String, String)} text processing is implemented but later this class can be improved.  
 * @author Irakli Kobiashvili
 *
 */
public class RegexBatch {

    private final String[] REGEXPS;
    private final List<Pattern> PATTERNS;
    private final RegexBatch[] NASTED_BATCHES;
    
    /**
     * Creates batch in normal mode for specified regex array and flags.
     * When processing texts regexs will be use din same order as specified here. 
     * @param regularExpressions
     * @param flags
     */
    public RegexBatch(String[] regularExpressions, int flags){
        REGEXPS = regularExpressions;
        PATTERNS = new ArrayList<Pattern>(regularExpressions.length);
        NASTED_BATCHES = null;
        for (String regex : REGEXPS) {
            Pattern pattern = Pattern.compile(regex);
            PATTERNS.add(Pattern.compile(regex, flags));
            PATTERNS.add(pattern);
        }
    }

    /**
     * Creates batch in parent mode for specified children batches.
     * This allows to build trees of batches.
     * @param regexBatches
     */
    public RegexBatch(RegexBatch[] regexBatches){
        REGEXPS = null;
        PATTERNS = null;
        NASTED_BATCHES = regexBatches;
    }
    
    /**
     * Replace all matching 
     * @param text
     * @param replacemant
     * @return
     */
    public String replaceAll(String text, String replacemant){
        if (NASTED_BATCHES == null) {
            return doLocal(text, replacemant);
        }
        return doNastedGroups(text, replacemant);
    }
    
    private String doLocal(String text, String replacemant){
        for (Pattern pattern : PATTERNS) {
            text = pattern.matcher(text).replaceAll(replacemant); 
        }
        return text;
    }
    
    private String doNastedGroups(String text, String replacemant){
        for (RegexBatch batch : NASTED_BATCHES) {
            text = batch.replaceAll(text, replacemant);
        }
        return text;
    }

    /**
     * Return regular expressions of the batch.
     * @return null if in parent mode.
     */
    public String[] getRegexArray() {
        return REGEXPS;
    }
}
