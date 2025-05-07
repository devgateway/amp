package org.digijava.module.translation.entity;

import org.digijava.kernel.entity.Message;
import org.digijava.kernel.translator.TranslatorWorker;

import java.util.Collection;

/**
 * Group of messages with same translation key.
 * They also should have same hash key but generated preferably from English translation.
 * Unlike its parent this class is used to patch messages with generated key.
 * @author Irakli Kobiashvili
 * @see HashKeyPatch
 *
 */
public class PatcherMessageGroup extends MessageGroup{

    private String hashKey = null;
    private boolean overwriteIncoming = false;

    public PatcherMessageGroup(String key) {
        super(key);
    }

    public PatcherMessageGroup(String key, boolean overwriteIncoming) {
        super(key);
        this.overwriteIncoming = overwriteIncoming;
    }

    public PatcherMessageGroup(Message message) {
        super(message);
    }
    
    /**
     * Calling this getter before all messages are added to group is very bad idea.
     * @see #addMessage(Message)
     * @return
     */
    public String getHashKey() {
        return hashKey;
    }

    /**
     * Puts message in the group.
     * This overridden method also generates hash key from message body. 
     * For this it uses first message added which may be 
     * overwritten only by English message later. So if there is English translation it will be used for 
     * key generation, or if there is no English then any other language message will be used. 
     * @param message
     */
    protected void doPutMessage(Message message){
        if (
                (getHashKey() == null) || 
                (getHashKey() != null && "en".equals(message.getLocale().toLowerCase()))
        )
        {
            this.hashKey = TranslatorWorker.generateTrnKey(message.getMessage());
        }
        if (overwriteIncoming){
            //put without care about existing
            getMessages().put(message.getLocale(), message);
        }else{
            //check to not overwrite existing 
            if (null == getMessages().get(message.getLocale())){
                getMessages().put(message.getLocale(), message);
            }
        }
        ////System.out.println("doPut "+hashKey);
    }
    
    /**
     * Patches all messages in the group with new hash key before returning them.
     * @return
     */
    public Collection<Message> patcheAll(){
        Collection<Message> myMessages = getMessages().values();
        if (myMessages != null && myMessages.size() >0){
            for (Message message : myMessages) {
                message.setKey(getHashKey());
            }
        }
        return myMessages;
    }

}
