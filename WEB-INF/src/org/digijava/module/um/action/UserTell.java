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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.module.um.exception.UMException;
import org.digijava.module.um.form.UserContactForm;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.user.User;
import javax.mail.internet.InternetAddress;
import javax.mail.Address;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.config.Smtp;

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

        ActionErrors errors = new ActionErrors();
        if (senderEmail == null) {
            errors.add(null, new ActionError("error.um.userNotLoggedin"));
        }
        if ( (recipientEmail == null || recipientEmail.length() == 0) ||
            (recipientName == null || recipientName.length() == 0)) {
            errors.add(null, new ActionError("error.um.fillUserTell"));
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
        String domanName = DgUtil.getCurrRootUrl(request);
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
        message = worker.get("param:SiteName",
                             currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            siteName = site.getName();
        }
        else {
            siteName = message.getMessage();
        }

        // get suggestion
        message = worker.get("um:tell_a_friend:suggestion",
                             currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            suggestion = "A suggestion from";
        }
        else {
            suggestion = message.getMessage();
        }


        // get dear
        message = worker.get("um:tell_a_friend:dear",
                             currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            dear = "Dear";
        }
        else {
            dear = message.getMessage();
        }


        // get thoughtYouMight
        message = worker.get("um:tell_a_friend:thoughtYouMight",
                             currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            thoughtYouMight = "thought you might be interested in visiting the Development Gateway, a portal for knowledge sharing and resources on sustainable development and poverty reduction.";
        }
        else {
            thoughtYouMight = message.getMessage();
        }

        // get onTheDevelopment
        message = worker.get("um:tell_a_friend:onTheDevelopment",
                             currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            onTheDevelopment = "On the Development Gateway, you can:";
        }
        else {
            onTheDevelopment = message.getMessage();
        }

        // get exchangeIdeas
        message = worker.get("um:tell_a_friend:exchangeIdeas",
                             currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            exchangeIdeas = "- Exchange ideas and knowledge\n" +
                "Access development knowledge by Topic (e.g., Gender and Development) and Development Focus (e.g., Afghanistan Reconstruction).\n"+
                "See the full list at";
        }
        else {
            exchangeIdeas = message.getMessage();
        }

        // get findDevelopmentProjects
        message = worker.get("um:tell_a_friend:findDevelopmentProjects",
                             currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            findDevelopmentProjects = "- Find development projects\n"+
                "Use AIDA, an extensive directory with over 330,000 programs and projects, with descriptions and funding information.";
        }
        else {
            findDevelopmentProjects = message.getMessage();
        }

        // get exploreBusinessOpportunities
        message = worker.get("um:tell_a_friend:exploreBusinessOpportunities",
                             currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            exploreBusinessOpportunities = "- Explore business opportunities\n" +
                "On the dgMarket. 500+ new tender opportunities daily, from Afghanistan to Zimbabwe.\n" +
                "Receive free e-mail alerts when a tender notice matches your business profile.\n" +
                "Visit";
        }
        else {
            exploreBusinessOpportunities = message.getMessage();
        }

        // get accessCountryInitiatives
        message = worker.get("um:tell_a_friend:accessCountryInitiatives",
                             currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            accessCountryInitiatives = "- Access country initiatives\n" +
                "Learn what is happening in 37 countries where projects are underway to address the digital divide and various development needs. Visit";
        }
        else {
            accessCountryInitiatives = message.getMessage();
        }

        // get visitUstoday
        message = worker.get("um:tell_a_friend:visitUstoday",
                             currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            visitUstoday = "Visit us today! http://www.developmentgateway.org - where worlds of knowledge meet";
        }
        else {
            visitUstoday = message.getMessage();
        }

        // get doNotReply
        message = worker.get("um:tell_a_friend:doNotReply",
                             currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            doNotReply = "Please do not reply to this email.";
        }
        else {
            doNotReply = message.getMessage();
        }


        // get notAMemberYet
        message = worker.get("um:tell_a_friend:notAMemberYet",
                             currentLocale.getCode(), site.getId().toString());
        if (message == null) {
            notAMemberYet = "Not a member yet? Register to receive email alerts in your area of interest!";
        }
        else {
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
        DgEmailManager.sendMail(new Address[] {address},"\"" + siteName + "\" <" + user.getEmail() + ">",
                                subject, text, currentLocale,true);
    }

}