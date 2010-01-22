

package org.digijava.module.aim.helper;


public class CategoryDatasetHelper implements Comparable<CategoryDatasetHelper> {
    private String fullName;
    private Long id;

    public CategoryDatasetHelper(String fullName,Long id){
        this.fullName=fullName;
        this.id=id;

    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int compareTo(CategoryDatasetHelper o) {
        return this.id.compareTo(o.id);
    }

    @Override
    public String toString(){
        String truncatedName=fullName;
        if(fullName.length()>25){
            truncatedName=fullName.substring(0,22)+"...";
        }
        return truncatedName;
    }

}
