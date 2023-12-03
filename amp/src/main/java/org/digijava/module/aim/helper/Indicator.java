/*
 * Created on 9/03/2006
 * @author akashs
 * 
 */
package org.digijava.module.aim.helper;

import java.util.List;

public class Indicator {

    private String indicatorCode;
    private String name;
    private List question;  // holds collection of Question helper objects
    
    /**
     * @return Returns the indicatorCode.
     */
    public String getIndicatorCode() {
        return indicatorCode;
    }
    /**
     * @param indicatorCode The indicatorCode to set.
     */
    public void setIndicatorCode(String indicatorCode) {
        this.indicatorCode = indicatorCode;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the question.
     */
    public List getQuestion() {
        return question;
    }
    /**
     * @param question The question to set.
     */
    public void setQuestion(List question) {
        this.question = question;
    }

    /*
    Question getQuestion(int index) {
        int size = question.size();
        if (index >= size) {
            for (int i = 0; i <= index - size; i++) {
                question.add(new Indicator());
            }
        }
        return (Question) question.get(index);
    } */

}
