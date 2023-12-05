package org.digijava.module.content.action;

import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.content.dbentity.AmpContentItem;
import org.digijava.module.content.dbentity.AmpContentItemThumbnail;
import org.digijava.module.content.form.ContentForm;
import org.digijava.module.content.util.DbUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class ContentManager extends DispatchAction {

    public ActionForward unspecified(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {
        Collection<AmpContentItem> contents = new ArrayList<AmpContentItem>();
        contents = DbUtil.getAllContents();
        request.setAttribute("contentList", contents);
        return mapping.findForward("list");
    }

    /**
     * list of extensions of files which are safe to be imported/exported for direct inclusion as a <src> file
     * a lame, poor man's, way of checking that a file is an image
     */
    public static Set<String> imageFileExtensions = new HashSet<String>(){{add("jpg"); add("jpeg"); add("gif"); add("png");}};

    
    public ActionForward upload(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        // Thumbnail upload
        
        ContentForm contentForm = (ContentForm) form;
        //int placeholder = contentForm.getPlaceholder();
        FormFile thumbnail = contentForm.getTempContentThumbnail();
        FormFile file = contentForm.getTempContentFile();
        String label = contentForm.getTempContentThumbnailLabel();

        String fileExtension = thumbnail.getFileName().substring(thumbnail.getFileName().lastIndexOf('.') + 1).toLowerCase();
        if (!imageFileExtensions.contains(fileExtension))
            return mapping.findForward("addEdit"); // file is not a supported image file - skip the DB serialization part
        
        AmpContentItemThumbnail thumb = new AmpContentItemThumbnail();
        thumb.setThumbnail(thumbnail.getFileData());
        thumb.setThumbnailContentType(thumbnail.getContentType());

        thumb.setOptionalFile(file.getFileData());
        thumb.setOptionalFileName(file.getFileName());
        thumb.setOptionalFileContentType(file.getContentType());

        thumb.setThumbnailLabel(label);
        if (contentForm.getContentThumbnails() == null || contentForm.getContentThumbnails().size() == 0) {
            contentForm
                    .setContentThumbnails(new HashSet<AmpContentItemThumbnail>());
            thumb.setPlaceholder(0);
        } else {
            AmpContentItemThumbnail tempContentItemThumbnail = contentForm
                    .getSortedContentThumbnails().last();
            thumb.setPlaceholder(tempContentItemThumbnail.getPlaceholder() + 1);

        }
        contentForm.getContentThumbnails().add(thumb);
        request.getSession().setAttribute("contentThumbnails",
                contentForm.getContentThumbnails());

        // try {
        // if (thumbnail != null && thumbnail.getFileSize() > 0) {
        // AmpContentItemThumbnail currentThumbnail = null;
        // byte[] image = thumbnail.getFileData();
        // homeThumbnail.setThumbnail(image);
        // homeThumbnail.setThumbnailContentType(thumbnail.getContentType());
        // homeThumbnail.setPlaceholder(wpForm.getPlaceholder());
        // homeThumbnail.setThumbnailLabel(wpForm.getThumbnailLabel());
        // if (wpForm.getOptionalFile()!=null &&
        // wpForm.getOptionalFile().getFileSize()>0){
        // homeThumbnail.setOptionalFile(wpForm.getOptionalFile().getFileData());
        // homeThumbnail.setOptionalFileName(wpForm.getOptionalFile().getFileName());
        // homeThumbnail.setOptionalFileContentType(wpForm.getOptionalFile().getContentType());
        // } else {
        // homeThumbnail.setOptionalFile(null);
        // homeThumbnail.setOptionalFileName(null);
        // homeThumbnail.setOptionalFileContentType(null);
        // }
        // DbUtil.update(homeThumbnail);
        // } else {
        // errorMsg = "error.aim.uploadFlag.noFlagSelected";
        // }
        // } catch (Exception e) {
        // errorMsg = "error.aim.serverError";
        // e.printStackTrace(System.out);
        // } finally {
        // if (errorMsg != null) {
        // ActionMessages errors = new ActionMessages();
        // errors.add(ActionMessages.GLOBAL_MESSAGE, new
        // ActionMessage(errorMsg));
        // saveErrors(request, errors);
        // }
        // }

        return mapping.findForward("addEdit");
    }

    public ActionForward moveup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        Integer currentPosition = Integer.parseInt(request
                .getParameter("index"));
        ContentForm contentForm = (ContentForm) form;
        Set<AmpContentItemThumbnail> list = contentForm
                .getSortedContentThumbnails();
        Object[] array = list.toArray();

        if (currentPosition > 0 && currentPosition < list.size()) {
            AmpContentItemThumbnail currentElement = (AmpContentItemThumbnail) array[currentPosition];
            AmpContentItemThumbnail otherElement = (AmpContentItemThumbnail) array[currentPosition - 1];

            otherElement.setPlaceholder(currentPosition);
            currentElement.setPlaceholder(currentPosition - 1);
        }

        return mapping.findForward("addEdit");
    }

    public ActionForward movedown(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        Integer currentPosition = Integer.parseInt(request
                .getParameter("index"));
        ContentForm contentForm = (ContentForm) form;
        Set<AmpContentItemThumbnail> list = contentForm
                .getSortedContentThumbnails();
        Object[] array = list.toArray();

        if (currentPosition >= 0 && currentPosition < list.size()) {
            AmpContentItemThumbnail currentElement = (AmpContentItemThumbnail) array[currentPosition];
            AmpContentItemThumbnail otherElement = (AmpContentItemThumbnail) array[currentPosition + 1];

            otherElement.setPlaceholder(currentPosition);
            currentElement.setPlaceholder(currentPosition + 1);
        }
        return mapping.findForward("addEdit");
    }

    public ActionForward list(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        Collection<AmpContentItem> contents = new ArrayList<AmpContentItem>();
        contents = DbUtil.getAllContents();
        request.setAttribute("contentList", contents);
        return mapping.findForward("list");
    }

    public ActionForward homepage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        if (request.getParameter("id") != null) {
            Long contentItemId = Long.parseLong(request.getParameter("id"));
            AmpContentItem currentHome = DbUtil.getHomePage();
            if(currentHome != null){
                currentHome.setIsHomepage(false);
                DbUtil.save(currentHome);
            }
            AmpContentItem contentItem = DbUtil.getContentItem(contentItemId);
            contentItem.setIsHomepage(true);
            DbUtil.save(contentItem);
        }
        return mapping.findForward("homepage");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        if (request.getParameter("id") != null) {
            Long contentItemId = Long.parseLong(request.getParameter("id"));
            AmpContentItem contentItem = DbUtil.getContentItem(contentItemId);
            DbUtil.delete(contentItem);
        }
        return mapping.findForward("delete");
    }

    public ActionForward deleteThumb(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {
        Integer currentPosition = Integer.parseInt(request
                .getParameter("index"));
        ContentForm contentForm = (ContentForm) form;
        TreeSet<AmpContentItemThumbnail> list = contentForm
                .getSortedContentThumbnails();
        AmpContentItemThumbnail contentItemThumbnailRemoved = (AmpContentItemThumbnail) list.toArray()[currentPosition];
        contentForm.getContentThumbnails().remove(contentItemThumbnailRemoved);
        contentForm.getContentThumbnailsRemoved().add(contentItemThumbnailRemoved);
        // Reindex the remaining thumbnails
        int reIndex = 0;
        for (AmpContentItemThumbnail current : contentForm.getSortedContentThumbnails()) {
            current.setPlaceholder(reIndex);
            reIndex++;
        }
        
        return mapping.findForward("addEdit");
    }

    public ActionForward add(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // content:System.currentTimeMillis():htmlblock_1
        ContentForm contentForm = (ContentForm) form;
        Boolean isReset = Boolean.valueOf(request.getParameter("reset"));

        if (isReset) {
            contentForm.setReset(true);
            contentForm.reset(mapping, request);
            request.getSession().removeAttribute("contentThumbnails");
            contentForm.setReset(false);
        }

        // If it's the first time in, create the keys for the text editors
        if (contentForm.getEditKey() == null) {
            if (contentForm.getHtmlblock_1() == null
                    && contentForm.getHtmlblock_2() == null) {
                contentForm.setHtmlblock_1("content-"
                        + System.currentTimeMillis() + "-htmlblock_1");
                contentForm.setHtmlblock_2("content-"
                        + System.currentTimeMillis() + "-htmlblock_2");
            }
        } else {
            String url = "/editor/showEditText.do?id="
                    + contentForm.getEditKey() + "&lang="
                    + RequestUtils.getNavigationLanguage(request).getCode()
                    + "&referrer=/content/contentManager.do?action=add";
            // Reset it to avoid circular redirect
            contentForm.setEditKey(null);
            response.sendRedirect(url);
        }

        return mapping.findForward("addEdit");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception

    {

        ContentForm contentForm = (ContentForm) form;
        if (contentForm.getContentThumbnailsRemoved() != null) {
            contentForm.getContentThumbnailsRemoved().clear();
        }

        if (request.getParameter("id") != null) {
            Long contentItemId = Long.parseLong(request.getParameter("id"));
            AmpContentItem contentItem = DbUtil.getContentItem(contentItemId);
            contentForm.setAmpContentFormId(contentItemId);
            contentForm.setDescription(contentItem.getDescription());
            contentForm.setTitle(contentItem.getTitle());
            contentForm.setPageCode(contentItem.getPageCode());
            contentForm.setContentLayout(contentItem.getLayout());
            contentForm.setHtmlblock_1(contentItem.getHtmlblock_1());
            contentForm.setHtmlblock_2(contentItem.getHtmlblock_2());
            contentForm
                    .setContentThumbnails(contentItem.getContentThumbnails());
            request.getSession().setAttribute("contentThumbnails",
                    contentForm.getContentThumbnails());
        }
        return mapping.findForward("addEdit");

    }

    public ActionForward save(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        

        ContentForm contentForm = (ContentForm) form;
        //Check if pageCode already exists before saving
        if(DbUtil.pageCodeExists(contentForm.getPageCode(), contentForm.getAmpContentFormId()))
        {
            ActionErrors errors=new ActionErrors();
            errors.add("pageCodeExists", new ActionMessage("error.content.pageCodeExists"));
            if(!errors.isEmpty()){
                saveErrors(request, errors);
                return mapping.findForward("addEdit");
            }
        }

        AmpContentItem contentItem;
        if (contentForm.getAmpContentFormId() != null
                && contentForm.getAmpContentFormId() > 0) {
            contentItem = DbUtil.getContentItem(contentForm
                    .getAmpContentFormId());
        } else {
            contentItem = new AmpContentItem();
        }

        contentItem.setLayout(contentForm.getContentLayout());
        contentItem.setDescription(contentForm.getDescription());
        contentItem.setTitle(contentForm.getTitle());
        contentItem.setPageCode(contentForm.getPageCode().trim());
        contentItem.setHtmlblock_1(contentForm.getHtmlblock_1());
        contentItem.setHtmlblock_2(contentForm.getHtmlblock_2());
        if(contentItem.getContentThumbnails()==null){
            contentItem.setContentThumbnails(new HashSet<AmpContentItemThumbnail>());
        }
        if (contentForm.getContentThumbnails() != null && !contentForm.getContentThumbnails().isEmpty()) {
            for(AmpContentItemThumbnail thumbnail:contentForm.getContentThumbnails()){
                if(thumbnail.getAmpContentItemThumbnailId()!=null){
                    for(AmpContentItemThumbnail oldThumbnail:contentItem.getContentThumbnails()){
                        if( oldThumbnail.getAmpContentItemThumbnailId()!=null&&oldThumbnail.getAmpContentItemThumbnailId().equals(thumbnail.getAmpContentItemThumbnailId())){
                            oldThumbnail.setPlaceholder(thumbnail.getPlaceholder());
                            break;
                        }
                    }
                }
                else{
                    thumbnail.setContentItem(contentItem);
                    contentItem.getContentThumbnails().add(thumbnail);
                }
            }
        }
        if(contentForm.getContentThumbnailsRemoved() != null && contentForm.getContentThumbnailsRemoved().size() > 0)
            DbUtil.removeThumbnails(contentItem, contentForm.getContentThumbnailsRemoved());
        DbUtil.save(contentItem);

        return mapping.findForward("save");

    }
}
