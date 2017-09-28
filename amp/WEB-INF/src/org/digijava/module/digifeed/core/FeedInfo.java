/**
 * FeedInfo.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.digifeed.core;



/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 20, 2005
 *
 */
public class FeedInfo {
    private String id;
    private String name;
    private String description;
    private String url;
    private String version;
    boolean gzip;
    private String authorName;
    private String authorEmail;
    private String encoding;
    private String jaxbPackage;
    
    public String toString() {
        StringBuffer b=new StringBuffer("Feed Information:");
        b.append(" id="+id);
        b.append("; name="+name);
        b.append("; url="+url);
        b.append("; version="+version);
        b.append("; encoding="+encoding);
        b.append("; JAXBPackage="+jaxbPackage);
        b.append("; gzip"+gzip);
        return b.toString();
    }
    
    /**
     * @return Returns the authorEmail.
     */
    public String getAuthorEmail() {
        return authorEmail;
    }
    /**
     * @param authorEmail The authorEmail to set.
     */
    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }
    /**
     * @return Returns the authorName.
     */
    public String getAuthorName() {
        return authorName;
    }
    /**
     * @param authorName The authorName to set.
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return Returns the gzip.
     */
    public boolean isGzip() {
        return gzip;
    }
    /**
     * @param gzip The gzip to set.
     */
    public void setGzip(boolean gzip) {
        this.gzip = gzip;
    }
    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the url.
     */
    public String getUrl() {
        return url;
    }
    /**
     * @param url The url to set.
     */
    public void setUrl(String url) {
        this.url = url;
    }
    /**
     * @return Returns the version.
     */
    public String getVersion() {
        return version;
    }
    /**
     * @param version The version to set.
     */
    public void setVersion(String version) {
        this.version = version;
    }
    /**
     * @return Returns the encoding.
     */
    public String getEncoding() {
        return encoding;
    }
    /**
     * @param encoding The encoding to set.
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
    /**
     * @return Returns the jaxbPackage.
     */
    public String getJaxbPackage() {
        return jaxbPackage;
    }
    /**
     * @param jaxbPackage The jaxbPackage to set.
     */
    public void setJaxbPackage(String jaxbPackage) {
        this.jaxbPackage = jaxbPackage;
    }   
}
