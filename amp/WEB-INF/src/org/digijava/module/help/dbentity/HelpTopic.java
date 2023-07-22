package org.digijava.module.help.dbentity;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.SiteCache;
import org.digijava.module.aim.util.AmpMath;
import org.digijava.module.help.helper.HelpContent;
import javax.persistence.*;

@Entity
@Table(name = "HELP_HELP_TOPICS")
public class HelpTopic implements Serializable{
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "help_help_topics_seq")
    @SequenceGenerator(name = "help_help_topics_seq", sequenceName = "HELP_HELP_TOPICS_seq", allocationSize = 1)
    @Column(name = "helpTopicId")
    private Long helpTopicId;

    @Column(name = "keywordsTrnKey")
    private String keywordsTrnKey;

    @Column(name = "titleTrnKey")
    private String titleTrnKey;

    @Column(name = "topicKey", unique = true)
    private String topicKey;

    @Column(name = "bodyEditKey")
    private String bodyEditKey;

    @Column(name = "siteId", nullable = false)
    private String siteId;

    @Column(name = "topic_type")
    private Integer topicType;

    @Column(name = "moduleInstance", nullable = false)
    private String moduleInstance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_Id")
    private HelpTopic parent;

    private List<HelpContent> helpContent;
    
    public String getBodyEditKey() {
        return bodyEditKey;
    }
    public void setBodyEditKey(String bodyEditKey) {
        this.bodyEditKey = bodyEditKey;
    }
    public Long getHelpTopicId() {
        return helpTopicId;
    }
    public void setHelpTopicId(Long helpTopicId) {
        this.helpTopicId = helpTopicId;
    }

    public String getTopicKey() {
        return topicKey;
    }
    public void setTopicKey(String topicKey) {
        this.topicKey = topicKey;
    }
    public String getKeywordsTrnKey() {
        return keywordsTrnKey;
    }
    public void setKeywordsTrnKey(String keywordsTrnKey) {
        this.keywordsTrnKey = keywordsTrnKey;
    }
    public String getModuleInstance() {
        return moduleInstance;
    }
    public void setModuleInstance(String moduleInstance) {
        this.moduleInstance = moduleInstance;
    }
    public HelpTopic getParent() {
        return parent;
    }
    public void setParent(HelpTopic parent) {
        this.parent = parent;
    }
    
    /**
     * Not a number, but "siteId" (e.g. 'amp')
     */
    public String getSiteId() {
        return siteId;
    }
    
    /**
     * Not a number, but "siteId" (e.g. 'amp')
     */
    public void setSiteId(String siteId) {
        if ((siteId != null) && AmpMath.isLong(siteId))
        {
            Logger.getLogger(this.getClass()).error("numeric siteId: " + siteId, new RuntimeException());
            this.siteId = SiteCache.lookupById(Long.parseLong(siteId)).getSiteId();
        }
        this.siteId = siteId;
    }
    
    public Site getSite()
    {
        return SiteCache.lookupByName(this.siteId);
    }
    
    public String getTitleTrnKey() {
        return titleTrnKey;
    }
    public void setTitleTrnKey(String titleTrnKey) {
        this.titleTrnKey = titleTrnKey;
    }
    public void setTopicType(Integer topicType) {
        this.topicType = topicType;
    }
    public Integer getTopicType() {
        return topicType;
    }
    public List<HelpContent> getHelpContent() {
        return helpContent;
    }
    public void setHelpContent(List<HelpContent> helpContent) {
        this.helpContent = helpContent;
    }
    
}
