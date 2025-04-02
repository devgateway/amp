/**
 * FeedBinder.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.digifeed.core;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 22, 2005 Binds business beans with the XML beans. Business beans
 *        are the ones that can be persisted (saved into DB). XML beans are used
 *        to create XML output by the marshaller. The binding process can be
 *        bidirectional.
 * @see FeedGenerator
 * 
 */
public abstract class GenericFeedBinder implements Cloneable {

    private static Logger logger = Logger.getLogger(GenericFeedBinder.class);
    
    private class FeedBinderWorker extends Thread {
        protected int start;
        protected int end;

        public FeedBinderWorker(int start, int end) {
            this.start = start;
            this.end = end;

        }

        public void run() {
            for (int i = start; i <= end; i++) {
                Object sobj = src.get(i);
                try {
                    Object dobj = bindObject(sobj);
                    dst.add(i, dobj);
                } catch (JAXBException e) {
                    logger.info(e.getMessage(), e);
                } catch (ParseException e) {
                    logger.info(e.getMessage(), e);
                }
            }
            if (++finishedThreads == workers.size())
                this.notifyAll();
        }

    };

    protected List src;
    protected boolean last;
    protected String nextStartWidth;
    protected List dst;

    protected Object finalTree;

    protected boolean xmlSource;

    private int finishedThreads;

    private List workers;
    
    protected GenericFeedRequest request;

    /**
     * @return Returns the request.
     */
    public GenericFeedRequest getRequest() {
        return request;
    }

    /**
     * @param request The request to set.
     */
    public void setRequest(GenericFeedRequest request) {
        this.request = request;
    }

    public GenericFeedBinder() {
        xmlSource = false;

    }

    public void bind() throws InterruptedException, JAXBException {
        dst = new ArrayList(src.size());
        
        /*
        workers = new ArrayList();
        finishedThreads = 0;

        
        
        for (int i = 0; i < src.size(); i += FeedConstants.workerListSize) {
            FeedBinderWorker w = new FeedBinderWorker(i, i
                    + FeedConstants.workerListSize);
            w.start();
        }

        synchronized (this) {
            wait();
            createFinalTree();
        }
        */
        
        //dumb iteration, no threads for the moment!
        
        Iterator i=src.iterator();
        while (i.hasNext()) {
            Object sobj = (Object) i.next();
            try {
                Object dobj = bindObject(sobj);
                dst.add(dobj);
            } catch (Exception e) {
                logger.info(e.getMessage(), e);
            }
        }
        createFinalTree();

    }

    protected Object bindObject(Object src) throws JAXBException,
            ParseException {
        if (xmlSource)
            return createDBBean(src);
        else
            return createXMLBean(src);
    }

    protected abstract Object createXMLBean(Object dbb) throws JAXBException,
            ParseException;

    protected abstract Object createDBBean(Object xmlb);

    protected abstract void createFinalTree() throws JAXBException;

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

        /**
     * @return Returns the src.
     */
    public List getSrc() {
        return src;
    }

    /**
     * @param src
     *            The src to set.
     */
    public void setSrc(List src) {
        this.src = src;
    }

    /**
     * @return Returns the xmlSource.
     */
    public boolean isXmlSource() {
        return xmlSource;
    }

    /**
     * @param xmlSource
     *            The xmlSource to set.
     */
    public void setXmlSource(boolean xmlSource) {
        this.xmlSource = xmlSource;
    }

    /**
     * @return Returns the finalTree.
     */
    public Object getFinalTree() {
        return finalTree;
    }

    /**
     * @param finalTree
     *            The finalTree to set.
     */
    public void setFinalTree(Object finalTree) {
        this.finalTree = finalTree;
    }

    /**
     * @return Returns the last.
     */
    public boolean isLast() {
        return last;
    }

    /**
     * @param last The last to set.
     */
    public void setLast(boolean last) {
        this.last = last;
    }

    /**
     * @return Returns the nextStartWidth.
     */
    public String getNextStartWidth() {
        return nextStartWidth;
    }

    /**
     * @param nextStartWidth The nextStartWidth to set.
     */
    public void setNextStartWidth(String nextStartWidth) {
        this.nextStartWidth = nextStartWidth;
    }


}
