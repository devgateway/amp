package org.digijava.module.translation.entity;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.SiteCache;
import org.digijava.module.aim.util.AmpMath;
import org.digijava.module.translation.jaxb.Language;
import org.digijava.module.translation.jaxb.ObjectFactory;
import org.digijava.module.translation.jaxb.Trn;

import javax.xml.datatype.DatatypeFactory;
import java.sql.Timestamp;
import java.util.*;

/**
 * Group of messages with same translation key.
 * They also should have same hash key but generated preferably from English translation
 * @author Irakli Kobiashvili
 *
 */
public class MessageGroup {
    
    private static Logger logger = Logger.getLogger(MessageGroup.class);    
    
    private String key = null;
    private String prefix = null;
    private String keyWords = null;
    private Long siteId = null;
    /**
     * default text from which key was generated.
     * Not mandatory!
     * Currently {@link Message} has not such property but we are going to add for AMP-6663
     */
    private String defaultText;
    
    /**
     * Hit score for search results and sorting.
     */
    private Float score;
    private Map<String, Message> messages = null;
    
    
    /**
     * Constructs group for particular key.
     * @param key
     */
    public MessageGroup(String key, String prefix){
        this.key = key;
        this.prefix=prefix;
        this.messages = new HashMap<String, Message>();
    }
    
    public MessageGroup(String key){
        this(key,null);
    }

    /**
     * Constructs group using first message which will be added in this group.
     * @param message
     */
    public MessageGroup(Message message){
        this(message.getKey(),message.getPrefix());
        this.setKeyWords(message.getKeyWords());
        this.setSiteId(Long.parseLong(message.getSiteId())); // we can be sure that message.getSiteId() is numeric, because there is a filter in the setter. If not -> it is a bug that SHOULD be fixed elsewhere in AMP
        addMessage(message);
    }

    /**
     * Creates message group from XML trn tag.
     * @param trn tag object
     */
    public MessageGroup(Trn trn){
        this(trn,null);
    }
    
    public Site getSite(String trnSiteId)
    {
        if (AmpMath.isLong(trnSiteId))
            return SiteCache.lookupById(Long.parseLong(trnSiteId));
        return SiteCache.lookupByName(trnSiteId);
    }
    /**
     * Creates message group from XML trn tag for specified site.
     * @param trn tag object.
     * @param currentSiteId site ID in string form. can be retrieved from request.
     */
    public MessageGroup(Trn trn, Long currentSiteId){
        this.messages = new HashMap<String, Message>();
        
        //KeyWords
        this.keyWords = trn.getKeywords();
        
        //SiteID
        this.siteId = (currentSiteId==null) ? Site.getIdOf(getSite(trn.getSiteId())) : currentSiteId;
        if (this.siteId == null){
            Site site = TranslatorWorker.getInstance("").getDefaultSite();
            this.siteId = Site.getIdOf(site);//unfortunately in AMP we use PK instead of string siteID value.
        }
        
        //key
        this.key = trn.getKey();
        if (this.key == null || "".equals(this.key.trim())){
            Language englishLang = getEnglishLanguageTag(trn.getLang());
            if (englishLang == null){
                logger.warn("Trn tag does not cntain key attribute or English lang tag. using first available lang tag contents to generate key.");
                //trn tag should have at least one lang tag. 
                englishLang = trn.getLang().get(0);
            }
            this.key = TranslatorWorker.generateTrnKey(englishLang.getValue());
        }
        
        //Languages
        for (Language lang  : trn.getLang()) {
            Message msg = new Message();
            
            msg.setSiteId(this.getSiteId().toString());
            msg.setKey(this.key);
            msg.setLocale(lang.getCode());
            
            msg.setMessage(lang.getValue());

            if (lang.getUpdated()!=null){
                msg.setCreated(new Timestamp(lang.getUpdated().toGregorianCalendar().getTime().getTime()));
            }
            
            msg.setKeyWords(this.getKeyWords());
            
            this.addMessage(msg);
        }
    }

    
    /**
     * Adds message to the group.
     * If one tries to add message with different key then exception is thrown.
     * @param message
     */
    public void addMessage(Message message){
        if (message==null || !message.getKey().equals(this.key)){
            throw new IllegalArgumentException("Cannot add null message or message with different key");
        }
        doPutMessage(message);
    }
    
    /**
     * puts message in group.
     * This protected method does not check if keys are same. This is required for hash code grouping
     * @param message
     */
    protected void doPutMessage(Message message){
        getMessages().put(message.getLocale(), message);
    }

    /**
     * Returns key of the group and all messages.
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     * Return key because key is the hash code itself.
     * @return
     */
    public String getHashKey() {
        return key;
    }

    public boolean equals(Object obj) {
        if (obj==null) return false;
        if (!(obj instanceof MessageGroup)) return false;
        MessageGroup anotherGroup = (MessageGroup) obj;
        return this.hashCode()==anotherGroup.hashCode();
    }

    /**
     * Returns hash code of key which itself is hash code of english message.
     * TODO check carefully if this is not buggy.
     */
    public int hashCode() {
        //TODO this is not good idea because key generation may change in TranslatorWorker.generateKey()
        return key.hashCode();
    }

    /**
     * Returns map of messages where keys are their language codes.
     * @return
     */
    protected Map<String, Message> getMessages() {
        return messages;
    }
    
    /**
     * Rertives message by language code from the group.
     * @param locale language code of the message to retrive from this group.
     * @return
     */
    public Message getMessageByLocale(String locale){
        return messages.get(locale);
    }
    
    /**
     * Returns list of all messages in the group.
     * @return
     */
    public Collection<Message> getAllMessages(){
        return this.messages.values();
    }
    /**
     * Returns all messages sorted by message language weight
     * @return list of sorted messages of the group
     * @see TrnUtil.MessageLocaleWeightComparator
     */
    public List<Message> getSortedMessages(){
        List<Message> messageList = new ArrayList<Message>(this.messages.values());
        Collections.sort(messageList, new TrnUtil.MessageLocaleWeightComparator());
        return messageList;
    }
    
    /**
     * Adds all messages from other group to this on.
     * Uses {@link #doPutMessage(Message)} method for adding to run adding logic 
     * of the class or subclasses if they override adding logic.
     * @param group
     */
    public void addMessagesFrom(MessageGroup group){
        Collection<Message> otherMessages = group.getAllMessages();
        for (Message otherMessage : otherMessages) {
            this.doPutMessage(otherMessage);
        }
    }
    
    /**
     * Retrives lang tag with language code set to English.
     * @param languages list of lang tags.
     * @return English lang tag, or null of not found.
     */
    private Language getEnglishLanguageTag(List<Language> languages){
        Language retValue=null;
        for (Language language : languages) {
            if(language.getCode().equalsIgnoreCase("en")){
                retValue=language;
                break;
            }
        }
        return retValue;
    }
    
    public Trn createTrn() throws Exception {
        ObjectFactory of = new ObjectFactory();
        Trn trn = of.createTrn();
        trn.setKey(this.getKey());
        for (Map.Entry<String ,Message> entry : messages.entrySet()) {
            Message msg = entry.getValue();
            if (trn.getKeywords() == null) {
                trn.setKeywords(msg.getKeyWords());
            }
            if (trn.getSiteId() == null) {
                trn.setSiteId(msg.getSiteId());
            }
            //creating Language
            Language lang = of.createLanguage();
            lang.setCode(msg.getLocale());
            lang.setValue(msg.getMessage());
            //created
            GregorianCalendar calUpdated = new GregorianCalendar();
            calUpdated.setTime(msg.getCreated() == null ? new Date() : msg.getCreated());
            lang.setUpdated(DatatypeFactory.newInstance().newXMLGregorianCalendar(calUpdated));
            //last accessed
            if (msg.getLastAccessed() != null) {
                GregorianCalendar lastAccessed = new GregorianCalendar();
                lastAccessed.setTime(msg.getLastAccessed());
                lang.setLastAccessed(DatatypeFactory.newInstance().newXMLGregorianCalendar(lastAccessed));
            }
            trn.getLang().add(lang);
        }
        return trn;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getSiteId() {
        return siteId;
    }
    
    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public String getDefaultText() {
        String result = defaultText;
        if (result==null){
            Message msg = getMessageByLocale("en");
            if (msg != null){
                result = StringUtils.isBlank(msg.getOriginalMessage()) ? msg.getMessage() : msg.getOriginalMessage();
            }else if (this.messages!=null && this.messages.size()>0){
                msg = this.messages.values().iterator().next();
                result = msg.getMessage();
            }else{
                result = "Empty";
            }
        }
        return result;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Float getScore() {
        return score;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    
}
