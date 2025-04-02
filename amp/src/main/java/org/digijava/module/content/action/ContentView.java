package org.digijava.module.content.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.content.dbentity.AmpContentItem;
import org.digijava.module.content.form.ContentForm;
import org.digijava.module.content.util.DbUtil;

public class ContentView extends TilesAction {
    public ActionForward execute(ComponentContext context,
            ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {

        ContentForm contentForm = (ContentForm) form;
        AmpContentItem contentItem = null;

        String pageCode = request.getParameter("c");

        if (pageCode != null && !pageCode.isEmpty()) {
            contentItem = DbUtil.getContentItemByPageCode(pageCode);
        } else {
            contentItem = DbUtil.getHomePage();
        }

        if (contentItem != null) {
            contentForm.setAmpContentFormId(contentItem.getAmpContentItemId());
            contentForm.setDescription(contentItem.getDescription());
            contentForm.setTitle(contentItem.getTitle());
            contentForm.setPageCode(contentItem.getPageCode());
            contentForm.setContentLayout(contentItem.getLayout());
            contentForm.setHtmlblock_1(contentItem.getHtmlblock_1());
            contentForm.setHtmlblock_2(contentItem.getHtmlblock_2());
            contentForm
                    .setContentThumbnails(contentItem.getContentThumbnails());
        }

        return null;
    }

}
