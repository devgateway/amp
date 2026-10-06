/**
 * 
 */
package org.dgfoundation.amp.currency;

import org.digijava.kernel.translator.TranslatorWorker;

/**
 * Inflation Rate Frequency
 * 
 * @author Nadejda Mandrescu
 */
public enum IRFrequency {
    Y("Yearly"),
    S("Semiannual"),
    Q("Quarterly"),
    M("Monthly"),
    B("Biweekly"),
    W("Weekly"),
    D("Daily"),
    C("Custom");
    
    private String title;
    IRFrequency(String title) { 
        this.title = title;
    }
    
    public String getTranslatedName() {
        return TranslatorWorker.translateText(title);
    }
    
}
