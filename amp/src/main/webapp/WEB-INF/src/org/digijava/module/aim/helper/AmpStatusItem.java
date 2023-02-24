package org.digijava.module.aim.helper ;

public class AmpStatusItem
{
    private String statusCode ;
    private String name ;
    private Long ampStatusId;


    public Long getAmpStatusId() {
        return ampStatusId;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * @param long1
     */
    public void setAmpStatusId(Long long1) {
        ampStatusId = long1;
    }

    /**
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * @param string
     */
    public void setStatusCode(String string) {
        statusCode = string;
    }

}
