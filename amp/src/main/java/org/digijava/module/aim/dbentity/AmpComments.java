/*
 * AmpComments.java
 */

package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.Output;

import java.io.Serializable;
import java.util.*;

public class AmpComments implements Serializable, Cloneable, Versionable {
    
    private Long ampCommentId;
    private AmpActivityVersion ampActivityId;
    private AmpField ampFieldId;
    private AmpTeamMember memberId;
    private Date commentDate;
    private String comment;
    
    /* we will use this field to store teammember's, 
    because team member may be removed from the team,
    so memberId will be useless*/
    private String memberName;

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
    
    /**
     * @return Returns the ampActivityId.
     */
    public AmpActivityVersion getAmpActivityId() {
        return ampActivityId;
    }
    /**
     * @param ampActivityId The ampActivityId to set.
     */
    public void setAmpActivityId(AmpActivityVersion ampActivityId) {
        this.ampActivityId = ampActivityId;
    }
    /**
     * @return Returns the ampCommentId.
     */
    public Long getAmpCommentId() {
        return ampCommentId;
    }
    /**
     * @param ampCommentId The ampCommentId to set.
     */
    public void setAmpCommentId(Long ampCommentId) {
        this.ampCommentId = ampCommentId;
    }
    /**
     * @return Returns the ampFieldId.
     */
    public AmpField getAmpFieldId() {
        return ampFieldId;
    }
    /**
     * @param ampFieldId The ampFieldId to set.
     */
    public void setAmpFieldId(AmpField ampFieldId) {
        this.ampFieldId = ampFieldId;
    }
    /**
     * @return Returns the comment.
     */
    public String getComment() {
        return comment;
    }
    /**
     * @param comment The comment to set.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
    /**
     * @return Returns the commentDate.
     */
    public Date getCommentDate() {
        return commentDate;
    }
    /**
     * @param commentDate The commentDate to set.
     */
    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }
    /**
     * @return Returns the memberId.
     */
    public AmpTeamMember getMemberId() {
        return memberId;
    }
    /**
     * @param memberId The memberId to set.
     */
    public void setMemberId(AmpTeamMember memberId) {
        this.memberId = memberId;
    }
    
    /**
     * 
     * @param list will be populated with all AmpComments for activity with id 'activityId'
     * @param hashMap is a map of lists. Each list contains the AmpComments for a specific field. So the keys are the fields' ids.
     * This map will probably be (or already is) saved as the session attribute ("commentColInSession") which is still needed by previewLogframe 
     * @param activityId
     */
    
    public static void populateWithComments(List<AmpComments> list, HashMap<Long, List<AmpComments>> hashMap, Long activityId) {
        
        List<AmpComments> tempList  = DbUtil.getAllCommentsByActivityId(activityId);
        
        Iterator<AmpComments> iter  = tempList.iterator();
        
        while (iter.hasNext()) {
            AmpComments ampComment  = iter.next();
            list.add(ampComment);
            if (ampComment.getAmpFieldId() != null) {
                Long fieldId            = ampComment.getAmpFieldId().getAmpFieldId();
                
                List<AmpComments> fromHashMapList   = hashMap.get( fieldId );
                if ( fromHashMapList == null ) {
                    fromHashMapList     = new ArrayList<AmpComments>();
                    hashMap.put(fieldId, fromHashMapList);
                }
                fromHashMapList.add( ampComment );
            }
            else
                throw new RuntimeException("AmpComment with id " + ampComment.getAmpCommentId() + " and text '" 
                        + ampComment.getComment() + "' has NO attached field object");
        }
        
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    @Override
    public boolean equalsForVersioning(Object obj) {
        AmpComments aux = (AmpComments) obj;
        if (this.comment.compareTo(aux.getComment()) == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Output getOutput() {
        Output out = new Output();
        out.getOutputs().add(
                new Output(null, new String[] { "Comment" },
                        new Object[] { this.comment != null ? this.comment : "" }));
        return out;
    }

    @Override
    public Object getValue() {
        return this.comment != null ? this.comment : "";
    }
    
    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws CloneNotSupportedException {
        AmpComments aux = (AmpComments) clone(); 
        aux.ampActivityId = newActivity;
        aux.ampCommentId = null;
        return aux;
    }
    
}
