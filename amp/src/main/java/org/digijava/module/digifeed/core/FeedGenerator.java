/**
 * FeedGenerator.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.digifeed.core;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * This class provides means of producing XML output based on a JAXB input tree 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 21, 2005
 *
 */
public class FeedGenerator 
{
    protected Object tree;
    protected FeedInfo feedInfo;
    public FeedGenerator(FeedInfo feedInfo) {
        this.feedInfo=feedInfo;
    }
    
    public void generateXML(Object tree,OutputStream ret) throws JAXBException  {
        JAXBContext jx = JAXBContext.newInstance(feedInfo.getJaxbPackage());
        Marshaller m=jx.createMarshaller();
        m.setProperty(Marshaller.JAXB_ENCODING,feedInfo.getEncoding());
        m.marshal(tree,new BufferedOutputStream(ret));
    }
    
    public void generateDBObjects(InputStream s) throws JAXBException {
        
    }
}
