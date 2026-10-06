package org.dgfoundation.amp.error;

import java.util.LinkedList;

public interface AMPTaggedExceptions {

    public LinkedList<String> getTags();
    public void addTag(String tag);
    public Throwable getMainCause();
}
