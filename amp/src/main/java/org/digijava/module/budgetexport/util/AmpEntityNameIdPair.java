package org.digijava.module.budgetexport.util;

/**
 * User: flyer
 * Date: 2/10/12
 * Time: 5:38 PM
 */
public class AmpEntityNameIdPair {
    Long id;
    String name;

    public AmpEntityNameIdPair() {
    }


    public AmpEntityNameIdPair(Long id, String name) {
        this.name = name;
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
