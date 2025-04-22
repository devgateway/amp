/**
 * GenericFeedRequest.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.digifeed.core;

import org.digijava.module.aim.helper.Constants;
import org.digijava.module.digifeed.exception.UnsupportedParamTypeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 23, 2005
 * 
 */
public abstract class GenericFeedRequest implements Cloneable {
    public static SimpleDateFormat sdt = new SimpleDateFormat(Constants.CALENDAR_DATE_FORMAT);
    
    

    protected String feed;

    protected Integer maxBlockSize;

    protected Long startId;
    
    protected String serverPath;
    
    protected Boolean count; 
    
    public boolean isCount() {
        return getCount().booleanValue();
    }
    
    /**
     * @return Returns the count.
     */
    public Boolean getCount() {
        return count;
    }

    /**
     * @param count The count to set.
     */
    public void setCount(Boolean count) {
        this.count = count;
    }

    public GenericFeedRequest() {
        startId = new Long(FeedConstants.defaultStartId);
        maxBlockSize = new Integer(FeedConstants.maxBlockSize);
        count=new Boolean(false);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    
    public String toString() {
        StringBuffer sb=new StringBuffer();
        Method[] m = this.getClass().getMethods();
        
            for (int k = 0; (m != null) && (k < m.length); k++) {
                String mname = m[k].getName();
                if (!mname.startsWith("get") || mname.length() < 4
                        || m[k].getParameterTypes().length != 0)
                    continue;
                try {
                    Object o=m[k].invoke(this,new Object[] {});
                    if (o!=null)
                    sb.append(mname.replaceFirst("get","")).append("=").append(o.toString()).append(", ");
                    ;
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            return sb.toString();
    }
    
    
    public void initialize(Map params) throws IllegalAccessException,
            InvocationTargetException, ParseException,
            UnsupportedParamTypeException {
        Iterator i = params.keySet().iterator();
        Method[] m = this.getClass().getMethods();
        while (i.hasNext()) {
            String element = (String) i.next();

            for (int k = 0; (m != null) && (k < m.length); k++) {
                String mname = m[k].getName();
                if (!mname.startsWith("set") || mname.length() < 4
                        || m[k].getParameterTypes().length != 1)
                    continue;

                if (element.equalsIgnoreCase(mname.substring(3))) {
                    Class ptype = m[k].getParameterTypes()[0];
                    String[] value = (String[]) params.get(element);
                    if (value[0]!=null)
                    if (ptype.equals(String.class))
                        m[k].invoke(this, new Object[] {new String(value[0])});                 
                    else if (ptype.equals(Long.class))
                        m[k].invoke(this, new Object[] { new Long(value[0])});          
                    
                    else if (ptype.equals(Integer.class))
                        m[k].invoke(this, new Object[] { new Integer (value[0])});

                    else if (ptype.equals(Date.class))
                        m[k].invoke(this, new Object[] { sdt.parse(value[0])});
                    else if (ptype.equals(Boolean.class))
                        m[k].invoke(this, new Object[] { new Boolean(value[0])});
                    else
                        throw new UnsupportedParamTypeException(ptype
                                .toString());
                }
            }

        }

    }

    /**
     * @return Returns the feed.
     */
    public String getFeed() {
        return feed;
    }

    /**
     * @param feed
     *            The feed to set.
     */
    public void setFeed(String feed) {
        this.feed = feed;
    }

    /**
     * @return Returns the maxBlockSize.
     */
    public Integer getMaxBlockSize() {
        return maxBlockSize;
    }

    /**
     * @param maxBlockSize
     *            The maxBlockSize to set.
     */
    public void setMaxBlockSize(Integer maxBlockSize) {
        this.maxBlockSize = maxBlockSize;
    }

    /**
     * @return Returns the startId.
     */
    public Long getStartId() {
        return startId;
    }

    /**
     * @param startId
     *            The startId to set.
     */
    public void setStartId(Long startId) {
        this.startId = startId;
    }

    /**
     * @return Returns the serverPath.
     */
    public String getServerPath() {
        return serverPath;
    }

    /**
     * @param serverPath The serverPath to set.
     */
    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

}
