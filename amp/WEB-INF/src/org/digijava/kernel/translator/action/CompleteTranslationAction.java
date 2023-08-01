/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.kernel.translator.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorBean;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.translator.ValueBean;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteCache;
import org.digijava.kernel.util.SiteUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * This class is used for complete/global translations
 */
public class CompleteTranslationAction extends DispatchAction
{

    private static Logger logger = Logger.getLogger(org.digijava.kernel.translator.action.CompleteTranslationAction.class);

    /**
     * Used by all methods to setup data for display
     * @param sm
     * @param request
     * @return
     */
    private List<ValueBean> setupForm(List<TranslatorBean> sm, HttpServletRequest request){

        if (logger.isDebugEnabled()) {
            logger.debug("ActionClass.MethodEnter.db { \"CompleteTranslationAction\", \"setupForm()\" }");
        }

        List<ValueBean> message = new ArrayList<ValueBean>();
        for(TranslatorBean tb:sm)
        {
            Message source = new Message();
            Message target = new Message();

            if(tb.getSrcMsg() != null)
                source = tb.getSrcMsg();

            if(tb.getTragetMsg() != null)
                target = tb.getTragetMsg();

            long time = 0;
            if(source.getCreated()!=null)
            time = source.getCreated().getTime();

            if(request.getParameter("expired") != null && request.getParameter("expired").equals("on")){
                if( source.getMessage() != null && time<0 ){
                    if(target.getMessage() == null){
                        message.add(new ValueBean(source.getKey(),source.getMessage(),"","","",tb.isNeedsUpdate()));
                    }else{
                        message.add(new ValueBean(source.getKey(),source.getMessage(),target.getMessage(),"","",tb.isNeedsUpdate()));
                    }
                }
            }else{
                String targetSiteId = target.getSiteId();
                String sourceSiteId = source.getSiteId();
                if(targetSiteId == null){
                    targetSiteId="";
                }
                if(sourceSiteId == null){
                    sourceSiteId="";
                }
                if((source.getMessage()!=null)&&((time>=0)||(source.getCreated()==null))){
                    if(target.getMessage() == null){
                        message.add(new ValueBean(source.getKey(),source.getMessage(),"",sourceSiteId,targetSiteId,tb.isNeedsUpdate()));
                    }else{
                        message.add(new ValueBean(source.getKey(),source.getMessage(),target.getMessage(),sourceSiteId,targetSiteId,tb.isNeedsUpdate()));
                    }
                }
            }

            //message.add(new ValueBean(message_source.getKey(),message_source.getMessage(),message_target.getMessage(),tb.isNeedsUpdate()));

        }
        if (logger.isDebugEnabled()) {
            logger.debug("ActionClass.MethodReturn.db { \"CompleteTranslationAction\", \"setupForm()\" }");
        }
        return message;

    }

    /**
     * Searches on the source/target message and displays the results
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     * @return
     */
    public ActionForward search(ActionMapping mapping,
                                   ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
                                   throws IOException, ServletException{

        if (logger.isDebugEnabled()) {
            logger.debug("ActionClass.MethodEnter.db { \"CompleteTranslationAction\", \"search()\" }");
        }
        Long siteId = getSiteId(request);
        Long rootSiteId = getRootSiteId(request);

        String search = request.getParameter("search");
        request.setAttribute("search",search);

        try{
            java.util.Set<String> ls = new TranslatorWorker().getPrefixesForSite(siteId, rootSiteId);
            request.setAttribute("list",ls);
        }catch(WorkerException we){
            logger.error("ActionClass.Exception.err { \"CompleteTranslationAction\" }");
        }


        List message = new ArrayList();
        boolean searchTarget = false;

        String keyword = request.getParameter("source_message");

        if(request.getParameter("choice").trim().equals("2")){
            keyword = request.getParameter("target_message");
            searchTarget = true;
        }

        request.setAttribute("source_message",keyword);

        int startFrom = 0;
        if(request.getParameter("startFrom") != null)
             startFrom = new Integer(request.getParameter("startFrom")).intValue();
        List<TranslatorBean> sm = new ArrayList<TranslatorBean>();
        try{
            if(searchTarget){
                //search on the target text

                if(request.getParameter("expired") != null && request.getParameter("expired").equals("on")){
                    sm = new TranslatorWorker().searchMessageForPattern(search,siteId,rootSiteId,request.getParameter("radio_locale1"),request.getParameter("radio_locale2"),keyword,request.getParameter("radio_locale2"),true,startFrom,50);
                }else{
                    sm = new TranslatorWorker().searchMessageForPattern(search,siteId,rootSiteId,request.getParameter("radio_locale1"),request.getParameter("radio_locale2"),keyword,request.getParameter("radio_locale2"),false,startFrom,50);
                }

            }else{
                //search on the source text
                if(request.getParameter("expired") != null && request.getParameter("expired").equals("on")){
                    sm = new TranslatorWorker().searchMessageForPattern(search,siteId,rootSiteId,request.getParameter("radio_locale1"),request.getParameter("radio_locale2"),keyword,request.getParameter("radio_locale1"),true,startFrom,50);
                }else{
                    sm = new TranslatorWorker().searchMessageForPattern(search,siteId,rootSiteId,request.getParameter("radio_locale1"),request.getParameter("radio_locale2"),keyword,request.getParameter("radio_locale1"),false,startFrom,50);
                }
            }
        }catch(WorkerException we){
            logger.error("ActionClass.Exception.err { \"CompleteTranslationAction\" }");
        }
        message = setupForm(sm,request);

        request.setAttribute("No_rows",String.valueOf(sm.size()));
        request.setAttribute("Message",message);
        java.util.Set languages = SiteUtils.getTransLanguages(RequestUtils.getSite(request));
        request.setAttribute("languages",languages);
        if(request.getParameter("radio_locale1") == null || request.getParameter("radio_locale1").equals("")){
            request.setAttribute("radio_locale1","en");
        }else{
            request.setAttribute("radio_locale1",request.getParameter("radio_locale1"));
            }

        if(request.getParameter("radio_locale2") == null || request.getParameter("radio_locale2").equals("")){
            request.setAttribute("radio_locale2","en");
        }else{
            request.setAttribute("radio_locale2",request.getParameter("radio_locale2"));

        }
        if (logger.isDebugEnabled()) {
            logger.debug("ActionClass.MethodReturn.db { \"CompleteTranslationAction\", \"search()\" }");
        }
        return null;
}
    /**
     * Searches a given keyword and displays the results
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     * @return
     */
    public ActionForward keywordSearch(ActionMapping mapping,
                                   ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
                                   throws IOException, ServletException{

        if (logger.isDebugEnabled()) {
            logger.debug("ActionClass.MethodEnter.db { \"CompleteTranslationAction\", \"keywordSearch()\" }");
        }
        Long siteId = getSiteId(request);
        Long rootSiteId = getRootSiteId(request);

        String search = request.getParameter("search");
        request.setAttribute("search",search);
        //TODO:Check why is this needed
        try{
            java.util.Set ls =new TranslatorWorker().getPrefixesForSite(siteId,rootSiteId);
            request.setAttribute("list",ls);

        }catch(WorkerException we){
            logger.error("ActionClass.Exception.err { \"CompleteTranslationAction\" }");
        }

        List<ValueBean> message = new ArrayList<ValueBean>();
        String keyword = request.getParameter("keyword");
        request.setAttribute("keyword",keyword);
        int startFrom = 0;
        if(request.getParameter("startFrom") != null)
             startFrom = new Integer(request.getParameter("startFrom")).intValue();

        List<TranslatorBean> sm = new ArrayList<TranslatorBean>();
        try{
             if(request.getParameter("expired") != null && request.getParameter("expired").equals("on")){
                 sm = new TranslatorWorker().searchKeysForPattern(search,siteId,rootSiteId,request.getParameter("radio_locale1"),request.getParameter("radio_locale2"),keyword,true,startFrom,50);
             }else{
                sm = new TranslatorWorker().searchKeysForPattern(search,siteId,rootSiteId,request.getParameter("radio_locale1"),request.getParameter("radio_locale2"),keyword,false,startFrom,50);
              }

        }catch(WorkerException we){
            logger.error("ActionClass.Exception.err { \"CompleteTranslationAction\" }");
        }

        message = setupForm(sm,request);

        request.setAttribute("No_rows",String.valueOf(sm.size()));

        request.setAttribute("Message",message);
        java.util.Set languages = SiteUtils.getTransLanguages(RequestUtils.getSite(request));
        request.setAttribute("languages",languages);


        if(request.getParameter("radio_locale1") == null || request.getParameter("radio_locale1").equals("")){
            request.setAttribute("radio_locale1","en");
        }else{
            request.setAttribute("radio_locale1",request.getParameter("radio_locale1"));
            }

        if(request.getParameter("radio_locale2") == null || request.getParameter("radio_locale2").equals("")){
            request.setAttribute("radio_locale2","en");
        }else{
            request.setAttribute("radio_locale2",request.getParameter("radio_locale2"));

        }
        if (logger.isDebugEnabled()) {
            logger.debug("ActionClass.MethodReturn.db { \"CompleteTranslationAction\", \"keywordSearch()\" }");
        }
        return null;
    }
    /**
     * This method saves a message into the database
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     * @return
     */

    public ActionForward addMessage(ActionMapping mapping,
                                   ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
                                   throws IOException, ServletException{

        if (logger.isDebugEnabled()) {
            logger.debug("ActionClass.MethodEnter.db { \"CompleteTranslationAction\", \"addMessage()\" }");
        }
        String key=request.getParameter("key_list");
        String locale1=request.getParameter("locale");
        String textMessage=request.getParameter("message_text");

        logger.debug(MessageFormat.format("ActionClass.Translator.Values {0} {1} {2}",
                key, locale1, textMessage));

        try{

            updateMsg(request,key,textMessage,new Locale(locale1),getSiteId(request));
        }catch(Exception we){
            logger.error("ActionClass.Exception.err { \"CompleteTranslationAction\" }");
            //handle the same
        }

        ActionForward af = keySearch(mapping,form,request,response);
        if (logger.isDebugEnabled()) {
            logger.debug("ActionClass.MethodReturn.db { \"CompleteTranslationAction\", \"addMessage()\" }");
        }
        return null;
    }


    /**
     * Sets language list
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     * @return
     */

    public ActionForward newMessage(ActionMapping mapping,
                                   ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
                                   throws IOException, ServletException{

        if (logger.isDebugEnabled()) {
            logger.debug("ActionClass.MethodEnter.db { \"CompleteTranslationAction\", \"addMessage()\" }");
        }

        java.util.Set languages = SiteUtils.getTransLanguages(RequestUtils.getSite(request));
        request.setAttribute("languages",languages);

        if (logger.isDebugEnabled()) {
            logger.debug("ActionClass.MethodReturn.db { \"CompleteTranslationAction\", \"addMessage()\" }");
        }

        return mapping.findForward("success");
    }

    /**
     *  expire/unexpire a given key
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward expireKey(ActionMapping mapping,
                                   ActionForm form,
                                   HttpServletRequest request,
                               HttpServletResponse response){


        if (logger.isDebugEnabled()) {
            logger.debug("ActionClass.MethodEnter.db { \"CompleteTranslationAction\", \"expireKey()\" }");
        }
        String mykeys = request.getParameter("mykeys" + request.getParameter("rownum"));

        if(request.getParameter("multi")!=null)
        {
            try{
                if(request.getParameter("multi").equals("0"))
                {
                    new TranslatorWorker().markKeyExpired(mykeys);
                }
                if(request.getParameter("multi").equals("1"))
                {
                    new TranslatorWorker().markKeyUnexpired(mykeys);
                }
            }catch(WorkerException we){
                logger.error("ActionClass.Exception.err { \"CompleteTranslationAction\" }");
            }
        }

        ActionForward af = keySearch(mapping,form,request,response);
        if (logger.isDebugEnabled()) {
            logger.debug("ActionClass.MethodReturn.db { \"CompleteTranslationAction\", \"expireKey()\" }");
        }
        return null;
    }

    /**
     * Updates message
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward update(ActionMapping mapping,
                                   ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response){

        if (logger.isDebugEnabled()) {
            logger.debug("ActionClass.MethodEnter.db { \"CompleteTranslationAction\", \"update()\" }");
        }
        String rownum = request.getParameter("rownum");
        String msgParam = "txt" + rownum;
        String keyLine = "mykeys" + rownum;

        String message = request.getParameter(msgParam);

        String mykeys = request.getParameter(keyLine);

        try{
            updateMsg(request,mykeys,message,new Locale(request.getParameter("radio_locale2")),getSiteId(request));
        }catch(Exception we){
            logger.error("ActionClass.Exception.err { \"CompleteTranslationAction\" }");
        }

        ActionForward af = keySearch(mapping,form,request,response);
        if (logger.isDebugEnabled()) {
            logger.debug("ActionClass.MethodReturn.db { \"CompleteTranslationAction\", \"update()\" }");
        }
        return null;

    }
    /**
     * Saves all messages
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward saveAll(ActionMapping mapping,
                                   ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response){

        if (logger.isDebugEnabled()) {
            logger.debug("ActionClass.MethodEnter.db { \"CompleteTranslationAction\", \"saveAll()\" }");
        }

        if(request.getParameter("total_rows")!=null)
        {
            int totalRownum = new Integer(request.getParameter("total_rows")).intValue();
            for(int x=1;x<=totalRownum;x++)
                {
                    String button="txt" + x;
                    String keyLine="mykeys" + x;
                    String mykeys = request.getParameter(keyLine);
                    String message = request.getParameter(button);

                    try{
                        updateMsg(request,mykeys,message,new Locale(request.getParameter("radio_locale2")),getSiteId(request));
                    }catch(Exception we){
                        logger.error("ActionClass.Exception.err { \"CompleteTranslationAction\" }");
                    }


                }

        }

        ActionForward af = keySearch(mapping,form,request,response);
        if (logger.isDebugEnabled()) {
            logger.debug("ActionClass.MethodReturn.db { \"CompleteTranslationAction\", \"saveAll()\" }");
        }
        return null;
    }


    /**
     * Searches based on the passed key and displays all matching messages
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward keySearch(ActionMapping mapping,
                                       ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response){
        //if(request.getParameter("search")!=null){/*This section is for key search e.g ep:*/
        if (logger.isDebugEnabled()) {
            logger.debug("ActionClass.MethodEnter.db { \"CompleteTranslationAction\", \"keySearch()\" }");
        }
        List message = new ArrayList();
        Long siteId = getSiteId(request);
        Long rootSiteId = getRootSiteId(request);

        //setup languages to be shown selected
        if(request.getParameter("radio_locale1") == null || request.getParameter("radio_locale1").equals("")){
            request.setAttribute("radio_locale1","en");
        }else{
            request.setAttribute("radio_locale1",request.getParameter("radio_locale1"));
            }

        if(request.getParameter("radio_locale2") == null || request.getParameter("radio_locale2").equals("")){
            request.setAttribute("radio_locale2","en");
        }else{
            request.setAttribute("radio_locale2",request.getParameter("radio_locale2"));

        }
//      List expired = new ArrayList();
        String search = request.getParameter("search");
        request.setAttribute("search",search);
        java.util.Set<String> ls = null;
        try{
             ls = new TranslatorWorker().getPrefixesForSite(siteId, rootSiteId);
        }catch(WorkerException we){
            logger.error("ActionClass.Exception.err { \"CompleteTranslationAction\" }");
        }
        int startFrom = 0;
        if(request.getParameter("startFrom") != null)
             startFrom = new Integer(request.getParameter("startFrom")).intValue();

        try{

            List<TranslatorBean> sm = new ArrayList<TranslatorBean>();
            if(request.getParameter("expired") != null && request.getParameter("expired").equals("on")){
                //System.out.println("Reached here expired");
                 sm = new TranslatorWorker().getMessagesForPrefix(search,siteId,rootSiteId,request.getAttribute("radio_locale1").toString(),request.getAttribute("radio_locale2").toString(),true,startFrom,50);
            }else{
                //System.out.println("Reached here non expired");
                 sm = new TranslatorWorker().getMessagesForPrefix(search,siteId,rootSiteId,request.getAttribute("radio_locale1").toString(),request.getAttribute("radio_locale2").toString(),false,startFrom,50);
            }
            //System.out.println("Count " + sm.size());
            message = setupForm(sm, request);
            request.setAttribute("No_rows",String.valueOf(sm.size()));
        }catch(WorkerException we){
            logger.error("ActionClass.Exception.err { \"CompleteTranslationAction\" }");
        }

        request.setAttribute("Message",message);
        request.setAttribute("list",ls);
        java.util.Set languages = SiteUtils.getTransLanguages(RequestUtils.getSite(request));
        request.setAttribute("languages",languages);


        if (logger.isDebugEnabled()) {
            logger.debug("ActionClass.MethodReturn.db { \"CompleteTranslationAction\", \"keySearch()\" }");
        }
        return null;
    }


    private Long getSiteId(HttpServletRequest request){

        if((request.getRequestURL()).indexOf("complete")>0){

            return RequestUtils.getSiteDomain(request).getSite().getId();

        }else{

            return 0L;
            }


    }
    private Long getRootSiteId(HttpServletRequest request){

        if((request.getRequestURL()).indexOf("complete")>0){

            return DgUtil.getRootSite(RequestUtils.getSiteDomain(request).getSite()).getId();

        }else{

            return 0L;
            }


    }
    /**
     * Gets all prefixes and messages related to the first prefix in the lsit
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward getAll(ActionMapping mapping,
                                       ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response){
        if (logger.isDebugEnabled()) {
            logger.debug("ActionClass.MethodEnter.db { \"CompleteTranslationAction\", \"getAll()\" }");
        }
        Long siteId = getSiteId(request);
        Long rootSiteId = getRootSiteId(request);

        if(request.getParameter("radio_locale1") == null || request.getParameter("radio_locale1").equals("")){
            request.setAttribute("radio_locale1","en");
        }else{
            request.setAttribute("radio_locale1",request.getParameter("radio_locale1"));
            }

        if(request.getParameter("radio_locale2") == null || request.getParameter("radio_locale2").equals("")){
            request.setAttribute("radio_locale2","en");
        }else{
            request.setAttribute("radio_locale2",request.getParameter("radio_locale2"));

        }

        List<ValueBean> message = new ArrayList<ValueBean>();
        java.util.Set<String> ls = null;
        try{
            ls = new TranslatorWorker().getPrefixesForSite(siteId,rootSiteId);

        }catch(WorkerException we){
            logger.error("ActionClass.Exception.err { \"CompleteTranslationAction\" }");
        }
        Iterator<String> it = ls.iterator();
        int c=0;
        int startFrom = 0;
        int count = 0;
        while(it.hasNext())
        {
            c=c+1;
            if(c==1)
            {
                String key1 = it.next().toString();

                if(request.getParameter("startFrom") != null)
                     startFrom = new Integer(request.getParameter("startFrom")).intValue();
                else
                  startFrom = 0;
                List<TranslatorBean> sm = new ArrayList<TranslatorBean>();
                try{

                    if(request.getParameter("expired") != null && request.getParameter("expired").equals("on")){
                        sm = new TranslatorWorker().getMessagesForPrefix(key1,siteId,rootSiteId,request.getAttribute("radio_locale1").toString(),request.getAttribute("radio_locale2").toString(),true,startFrom,50);
                    }else{
                        sm = new TranslatorWorker().getMessagesForPrefix(key1,siteId,rootSiteId,request.getAttribute("radio_locale1").toString(),request.getAttribute("radio_locale2").toString(),false,startFrom,50);
                    }

                }catch(WorkerException we){
                    logger.error("ActionClass.Exception.err { \"CompleteTranslationAction\" }");
                }
                for(int i=0 ; i<sm.size();i++)
                {

                    TranslatorBean tb = (TranslatorBean)sm.get(i);

                    Message source = new Message();
                    Message target = new Message();

                    if(tb.getSrcMsg() != null)
                        source = tb.getSrcMsg();

                    if(tb.getTragetMsg() != null)
                        target = tb.getTragetMsg();
                    long time = 0;
                    if(source.getCreated()!=null)
                        time = source.getCreated().getTime();

                    if(target.getMessage() == null){
                        //System.out.println("TARGET MESSAGE IS NULL");
                    }

                    if(source.getMessage() == null){
                        //System.out.println("SOURCE MESSAGE IS NULL");
                    }

                    if((source.getMessage()!=null)&&((time>=0)||(source.getCreated()==null))){
                        String targetSiteId = target.getSiteId();
                        String sourceSiteId = source.getSiteId();

                        if(sourceSiteId == null){
                            sourceSiteId="";
                        }
                        if(targetSiteId == null){
                            targetSiteId="";
                        }

                        if(target.getMessage() == null){
                            message.add(new ValueBean(source.getKey(),source.getMessage(),"",sourceSiteId,targetSiteId, tb.isNeedsUpdate()));
                        }else{
                            message.add(new ValueBean(source.getKey(),source.getMessage(),target.getMessage(),sourceSiteId,targetSiteId,tb.isNeedsUpdate()));
                        }
                    }

                    count++;
                }
                break;
            }
        }
        request.setAttribute("Message",message);
        request.setAttribute("list",ls);
        java.util.Set languages = SiteUtils.getTransLanguages(RequestUtils.getSite(request));
        request.setAttribute("languages",languages);
    //}


        if (logger.isDebugEnabled()) {
            logger.debug("ActionClass.MethodReturn.db { \"CompleteTranslationAction\", \"getAll()\" }");
        }
        return null;
    }

    /**
     * Does an insert or update of a given message
     *
     * @param request
     * @param key
     * @param strMessage
     * @param locale
     * @param siteId
     * @throws WorkerException
     */
    private void updateMsg(HttpServletRequest request, String key,String strMessage, Locale locale, Long siteId) {

        if (logger.isDebugEnabled()) {
            logger.debug("ActionClass.MethodEnter.db { \"CompleteTranslationAction\", \"updateMsg()\" }");
        }
        TranslatorWorker translatorWorker = TranslatorWorker.getInstance(key);
        Message msg = translatorWorker.getByKey(key, locale.toString(), siteId);

        if (msg != null) {
            msg.setMessage(strMessage);
            msg.setKey(key);
            msg.setCreated(new java.sql.Timestamp(System.currentTimeMillis()));
            msg.setLocale(locale.toString());
            msg.setSite(SiteCache.lookupById(siteId));
            translatorWorker.update(msg);

        } else {

            Message message = new Message();
            message.setMessage(strMessage);

            message.setCreated(new java.sql.Timestamp(System.currentTimeMillis()));
            message.setKey(key);
            message.setSite(SiteCache.lookupById(siteId));
            message.setLocale(locale.toString());
            translatorWorker.save(message);

        }

        if (logger.isDebugEnabled()) {
            logger.debug("ActionClass.MethodReturn.db { \"CompleteTranslationAction\", \"updateMsg()\" }");
        }
        }


}
