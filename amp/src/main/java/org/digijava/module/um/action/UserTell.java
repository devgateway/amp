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

package org.digijava.module.um.action;

import java.io.IOException;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.um.exception.UMException;
import org.digijava.module.um.form.UserContactForm;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class UserTell
    extends Action {

    private static Logger logger = Logger.getLogger(UserTell.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws
        java.lang.Exception, IOException, UMException {

        UserContactForm userForm = (UserContactForm) form;
        User user = RequestUtils.getUser(request);

        String senderEmail = user.getEmail();
        String recipientEmail = userForm.getRecipientEmail();
        String recipientName = userForm.getRecipientName();

        logger.debug("Recepient Email: " + recipientEmail);

        if( recipientName != null ) {
            recipientName = recipientName.trim();
        }
        if( recipientEmail != null ) {
            recipientEmail = recipientEmail.trim();
        }

        ActionMessages errors = new ActionMessages();
        if (senderEmail == null) {
            errors.add(null, new ActionMessage("error.um.userNotLoggedin"));
        }
        if ( (recipientEmail == null || recipientEmail.length() == 0) ||
            (recipientName == null || recipientName.length() == 0)) {
            errors.add(null, new ActionMessage("error.um.fillUserTell"));
        }

        if( !errors.isEmpty() ) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        // send a meil to friend
        sendEmail(user, userForm.getSelectedLanguage(), recipientName,
                  recipientEmail, request);

        return mapping.findForward("success");
    }


    /**
     * Send email alert
     *
     * @param user
     * @throws java.lang.Exception
     */
    public void sendEmail(User user, String langCode, String toUserFullName, String toUserEmail,
                          javax.servlet.http.HttpServletRequest request) throws
        java.lang.Exception {

        Site site = RequestUtils.getSite(request);
//        String domanName = DgUtil.getCurrRootUrl(request);
        Message message;
        String siteName;

        String suggestion;
        String fromUserFullName = user.getName();
        String dear;
        String thoughtYouMight;
        String onTheDevelopment;
        String exchangeIdeas;
        String findDevelopmentProjects;
        String exploreBusinessOpportunities;
        String accessCountryInitiatives;
        String visitUstoday;
        String doNotReply;
        String notAMemberYet;


        TranslatorWorker worker = TranslatorWorker.getInstance("um:");
        Locale currentLocale = new Locale();
        currentLocale.setCode(langCode);


        // get SiteName
        message = worker.getByBody(site.getName(),currentLocale.getCode(), site.getId());
        if (message == null) {
            siteName = site.getName();
        }
        else {
            siteName = message.getMessage();
        }

        // get suggestion
        suggestion = "A suggestion from";//default text
        //try from translation
        message = worker.getByBody(suggestion,currentLocale.getCode(), site.getId());
        if (message != null) {
            suggestion = message.getMessage();
        }


        // get dear
        dear = "Dear";
        message = worker.getByBody(dear,currentLocale.getCode(), site.getId());
        if (message != null) {
            dear = message.getMessage();
        }


        // get thoughtYouMight
        thoughtYouMight = "thought you might be interested in visiting the Development Gateway, a portal for knowledge sharing and resources on sustainable development and poverty reduction.";
        message = worker.getByBody(thoughtYouMight,currentLocale.getCode(), site.getId());
        if (message != null) {
            thoughtYouMight = message.getMessage();
        }

        // get onTheDevelopment
        onTheDevelopment = "On the Development Gateway, you can:";
        message = worker.getByBody(onTheDevelopment,currentLocale.getCode(), site.getId());
        if (message != null) {
            onTheDevelopment = message.getMessage();
        }

        // get exchangeIdeas
        exchangeIdeas = "- Exchange ideas and knowledge\n" +
        "Access development knowledge by Topic (e.g., Gender and Development) and Development Focus (e.g., Afghanistan Reconstruction).\n"+
        "See the full list at";
        message = worker.getByBody(exchangeIdeas,currentLocale.getCode(), site.getId());
        if (message != null) {
            exchangeIdeas = message.getMessage();
        }

        // get findDevelopmentProjects
        findDevelopmentProjects = "- Find development projects\n"+
        "Use AIDA, an extensive directory with over 330,000 programs and projects, with descriptions and funding information.";
        message = worker.getByBody(findDevelopmentProjects,currentLocale.getCode(), site.getId());
        if (message != null) {
            findDevelopmentProjects = message.getMessage();
        }

        // get exploreBusinessOpportunities
        exploreBusinessOpportunities = "- Explore business opportunities\n" +
        "On the dgMarket. 500+ new tender opportunities daily, from Afghanistan to Zimbabwe.\n" +
        "Receive free e-mail alerts when a tender notice matches your business profile.\n" +
        "Visit";
        message = worker.getByBody(exploreBusinessOpportunities,currentLocale.getCode(), site.getId());
        if (message != null) {
            exploreBusinessOpportunities = message.getMessage();
        }

        // get accessCountryInitiatives
        accessCountryInitiatives = "- Access country initiatives\n" +
        "Learn what is happening in 37 countries where projects are underway to address the digital divide and various development needs. Visit";
        message = worker.getByBody(accessCountryInitiatives,currentLocale.getCode(), site.getId());
        if (message != null) {
            accessCountryInitiatives = message.getMessage();
        }

        // get visitUstoday
        visitUstoday = "Visit us today! http://www.developmentgateway.org - where worlds of knowledge meet";
        message = worker.getByBody(visitUstoday,currentLocale.getCode(), site.getId());
        if (message != null) {
            visitUstoday = message.getMessage();
        }

        // get doNotReply
        doNotReply = "Please do not reply to this email.";
        message = worker.getByBody(doNotReply,currentLocale.getCode(), site.getId());
        if (message != null) {
            doNotReply = message.getMessage();
        }


        // get notAMemberYet
        notAMemberYet = "Not a member yet? Register to receive email alerts in your area of interest!";
        message = worker.getByBody(notAMemberYet,currentLocale.getCode(), site.getId());
        if (message != null) {
            notAMemberYet = message.getMessage();
        }

        String subject = suggestion + " " + fromUserFullName;
        String text = dear + " " + toUserFullName + ",\n\n" +
            fromUserFullName + " " + thoughtYouMight + "\n\n" +
            onTheDevelopment + "\n\n" +
            exchangeIdeas + " http://www.developmentgateway.org/all-topics" + "\n\n" +
            findDevelopmentProjects + "\n\n" +
            exploreBusinessOpportunities + " " + "http://www.developmentgateway.org/business\n\n" +
            accessCountryInitiatives + " " + "http://www.developmentgateway.org/node/137849/" + "\n\n" +
            visitUstoday + "\n" +
            "______________________________________________\n" +
            doNotReply + "\n" +
            notAMemberYet + "\n" +
            "http://topics.developmentgateway.org/um/user/showUserRegister.do";

        InternetAddress address = new InternetAddress(toUserEmail);
        DgEmailManager.sendMail(new Address[] {address},
                "\"" + siteName + "\" <" + user.getEmailUsedForNotification() + ">",
                                subject, text, currentLocale,true);
    }

}
