/**
 * 
 */
package org.digijava.module.contentrepository.action;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.version.Version;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;


/**
 * @author Alex Gartner
 *
 */
public class GetVersionsForDocumentManager extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {
		
		String nodeUUID		= request.getParameter("uuid"); 
		PrintWriter out		= response.getWriter();
		Collection versions	= DocumentManagerUtil.getVersions(nodeUUID, request);
		if (versions != null) {
			
			out.println("<div id='versions_markup'>");
			out.println("<table id='versions'>");
			out.println("<thead>" +
							"<tr>" +
								"<th>File Name</th>" +
								"<th>Resource Title</th>" +
								"<th>Date</th>" +
								"<th>Description</th>" + 
								"<th>Actions</th>" +
							"</tr>" +
						"</thead>");
			
			Iterator iter	= versions.iterator();
			while ( iter.hasNext() ) {
				Version v			= (Version)iter.next();
				//String testVersionUUID	= v.getUUID();
				NodeIterator nIter	= v.getNodes();
				while (nIter.hasNext()) {
					Node n			= nIter.nextNode();
					String testUUID	= n.getUUID();
					System.out.println(testUUID);
					out.println( this.generateTableRow(n) );
				}
			}
			
			out.println("</table>");
			out.println("</div>");
		}
		return null;
	}
	
	private String generateTableRow (Node n) throws UnsupportedRepositoryOperationException, RepositoryException {
		String name 		= "";
		String title 		= "";
		String date			= "";
		String description	= "";
		
		String ret			= "";
		
		try{
			name		= n.getProperty("ampdoc:name").getString();
		}
		catch(Exception E) {
			return ret;
		}
		try{
			title		= n.getProperty("ampdoc:title").getString();
		}
		catch(Exception E) {
			;
		}
		try{
			description	= n.getProperty("ampdoc:description").getString();
		}
		catch(Exception E) {
			;
		}
		try{
			date	= DocumentManagerUtil.calendarToString( n.getProperty("ampdoc:addingDate").getDate() );
		}
		catch(Exception E) {
			;
		}

		ret		+= "<tr>";
		ret		+= "<td>" + name + "</td>";
		ret		+= "<td>" + title + "</td>";
		ret		+= "<td>" + date + "</td>";
		ret		+= "<td>" + description + "</td>";
		ret		+= "<td>" +
			"<a style=\"cursor:pointer; text-decoration:underline; color: blue\" " +
			"onClick=\"window.location='/contentrepository/downloadFile.do?uuid="+n.getUUID()+"'\" " +
			"href=\"/downloadFile.do?uuid="+n.getUUID()+"\" /> " +
			 "[Download]" +
			 "</a>" +
				"</td>";
		ret		+= "</tr>";
		
		return ret;
	}
}

