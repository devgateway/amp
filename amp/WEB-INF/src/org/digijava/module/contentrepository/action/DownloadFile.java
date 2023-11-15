package org.digijava.module.contentrepository.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.BoundedList;
import org.digijava.kernel.ampapi.endpoints.resource.ResourceErrors;
import org.digijava.kernel.util.ResponseUtil;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.ws.rs.core.Response;
import java.util.Calendar;
import java.util.Comparator;

/**
 *
 * @author Alex Gartner
 *
 */
public class DownloadFile extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
            throws java.lang.Exception {

        String nodeUUID = request.getParameter("uuid");

        if (nodeUUID != null) {
            Node node = DocumentManagerUtil.getReadNode(nodeUUID, request);
            if (node == null) {
                throw new RuntimeException("node with uuid = " + nodeUUID + " not found!");
            }
            if (!node.hasProperty(CrConstants.PROPERTY_CONTENT_TYPE)) {
                response.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
                response.getWriter().println(ResourceErrors.RESOURCE_NOT_VALID.description);
                return null;
            }

            Property contentType = node.getProperty(CrConstants.PROPERTY_CONTENT_TYPE);
            Property name = node.getProperty(CrConstants.PROPERTY_NAME);
            Property data = node.getProperty(CrConstants.PROPERTY_DATA);

            if (request.getSession().getAttribute(Constants.MOST_RECENT_RESOURCES) == null) {
                Comparator<DocumentData> documentDataComparator = new Comparator<DocumentData>()
                {
                    public int compare(DocumentData a, DocumentData b)
                    {
                        return a.getUuid().compareTo(b.getUuid());
                    }
                };

                request.getSession().setAttribute(Constants.MOST_RECENT_RESOURCES,
                        new BoundedList<DocumentData>(Constants.MAX_MOST_RECENT_RESOURCES, documentDataComparator));
            }

            NodeWrapper nodeWrapper = new NodeWrapper(node);
            DocumentData documentData = DocumentData.buildFromNodeWrapper(nodeWrapper);

            /**
             * We do not save this date to the document repository node
             * Just refresh it for display purposes, indicating that document has just been accessed
             */
            documentData.setDate(Calendar.getInstance().getTime());
            BoundedList<DocumentData> recentUUIDs = (BoundedList<DocumentData>)(request.getSession().getAttribute(Constants.MOST_RECENT_RESOURCES));
            recentUUIDs.add(documentData);

            if (contentType != null && name != null && data != null) {
                ResponseUtil.writeFile(request, response, contentType.getString(), name.getString(), data.getStream());
            }
        }

        DocumentManagerUtil.logoutJcrSessions(request);

        return null;
    }
}
