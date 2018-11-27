package org.digijava.module.contentrepository.helper;

public class TeamMemberMail {

    private Long memberId;
    private Long teamId;
    private String email;
    
    public TeamMemberMail() {
    }
    
    public TeamMemberMail(Long memberId, Long teamId, String email) {
        this.memberId = memberId;
        this.teamId = teamId;
        this.email = email;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    
    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
