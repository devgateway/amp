/*
// $Id: PropertyFormatter.java,v 1.1 2008-11-10 10:06:22 mpostelnicu Exp $
// This software is subject to the terms of the Common Public License
// Agreement, available at the following URL:
// http://www.opensource.org/licenses/cpl.html.
// Copyright (C) 2004-2005 TONBELLER AG
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
*/

package mondrian.olap;

/**
 * this interface provides a user exit to redefine
 *  a member property display string.
 */
public interface PropertyFormatter {
    String formatProperty(Member member, String propertyName, Object propertyValue);
}

// End PropertyFormatter.java

