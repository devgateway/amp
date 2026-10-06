package org.digijava.module.message.helper;



public class ReciverName {

    
    private String teamName;
    private String userNeme;
    
    
    public String getTeamName() {
        return teamName;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    public String getUserNeme() {
        return userNeme;
    }

    public String getUserNameFiltered() {
        return userNeme.replaceAll("<", "&lt;").replaceAll(">", "&gt");
    }

    public void setUserNeme(String userNeme) {
        this.userNeme = userNeme;
    }
    
    
}
