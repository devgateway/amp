/*
*   HighlightLinks.java
*   @Author Maka Kharalashvili maka@digijava.org
*   Created: Oct 10, 2003
*   CVS-ID: $Id: HighlightLinks.java,v 1.1 2005-07-06 10:34:25 rahul Exp $
*
*   This file is part of DiGi project (www.digijava.org).
*   DiGi is a multi-site portal system written in Java/J2EE.
*
*   Confidential and Proprietary, Subject to the Non-Disclosure
*   Agreement, Version 1.0, between the Development Gateway
*   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
*   Gateway Foundation, Inc.
*
*   Unauthorized Disclosure Prohibited.
*
*************************************************************************/

package org.digijava.module.highlights.dbentity;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class HighlightLinks
    implements Serializable {

    /**
     * offset(index) of a link in sequence of Highlight links
     */
    private int offset;

    /**
     * link URL
     */
    private String url;

    /**
     * link name
     */
    private String name;

    /**
     * Instance of Highlight-owner of the links set
     */
    private Highlight highlight;

    public HighlightLinks() {}
    public HighlightLinks(int offset, String url, String name) {
        this.offset = offset;
        this.url = url;
        this.name = name;
    }

    public Highlight getHighlight() {
        return highlight;
    }

    public void setHighlight(Highlight highlight) {
        this.highlight = highlight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) {
        if (! (other instanceof HighlightLinks))
            return false;
        HighlightLinks castOther = (HighlightLinks) other;
        return new EqualsBuilder()
            .append(this.getHighlight() == null ? null :
                    this.getHighlight().getId(),
                    castOther.getHighlight() == null ? null :
                    castOther.getHighlight().getId())
            .append(this.getOffset(), castOther.getOffset())
            .isEquals();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getHighlight() == null ? null :
                    this.getHighlight().getId())
            .append(this.getOffset())
            .toHashCode();
    }

}