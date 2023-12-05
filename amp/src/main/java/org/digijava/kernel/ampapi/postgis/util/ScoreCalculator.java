package org.digijava.kernel.ampapi.postgis.util;

public class ScoreCalculator {

    /**
     * Calculates the maximum Levenshtein distance that will be allowed on the search
     * for a given location. That is, the maximum Levenshtein distance between the search term
     * and the actual results up  to where the returned values will be included on the results
     * For instance: with a search term of "Port" and a maximum leventshtein distance of 1
     * values like 'Fort' will be included on the results
     * @param word
     * @return the maximum allowed distance
     */
    public static int getMaxAllowedDistance (String word) {
        int length = word.length();
        return (int)length/3;
    }
    
    /**
     * Calculates the score of the text result obtained from a search term.
     * 
     * @param searchTerm
     * @param returnedTerm
     * @param distance
     * @return
     */
    public static double getScore (String searchTerm, String returnedTerm, int distance) {
    double score = 0;
    String lowerSearchTerm = searchTerm.toLowerCase();
    String lowerReturnedTerm = returnedTerm.toLowerCase();
    if (lowerSearchTerm.equals(lowerReturnedTerm)) {
        score =  100;
    }
    else if (lowerReturnedTerm.contains(lowerSearchTerm)) {
        int difference = returnedTerm.length() - searchTerm.length();
        score = 100 -(difference * 2);
    }
    else {
        score = 90 - (distance * 5);
    }
    return score;
    }
}
