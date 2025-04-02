package org.digijava.module.aim.helper ;

public class AmpProjectDonor implements Comparable
{
    private Long ampDonorId;
    private String donorName;
    
    public String getDonorName() 
    {
        return donorName;
    }

    public Long getAmpDonorId() 
    {
        return ampDonorId;
    }
        
    public void setDonorName(String name) 
    {
        this.donorName = name ;
    }

    public void setAmpDonorId(Long donor) 
    {
        this.ampDonorId = donor ;
    }

    public boolean equals(Object obj) {
        if (obj instanceof AmpProjectDonor) {
            AmpProjectDonor temp = (AmpProjectDonor) obj;
            return temp.getAmpDonorId().equals(ampDonorId);
        } else throw new ClassCastException();
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
        if (o instanceof AmpProjectDonor) {
            AmpProjectDonor temp = (AmpProjectDonor) o;
            return temp.getDonorName().toLowerCase().compareTo(donorName.toLowerCase());
        } else throw new ClassCastException();
    }
    
}       
