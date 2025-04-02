package org.digijava.module.aim.helper;

public class Country {
    
    private Long id;
    private String name;
    
    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(Long id) {
        this.id = id;
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
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if (obj instanceof Country) {
            Country c = (Country) obj;
            return (c.getId().longValue() == id.longValue());
        } else throw new ClassCastException();

    }
    
    
}
