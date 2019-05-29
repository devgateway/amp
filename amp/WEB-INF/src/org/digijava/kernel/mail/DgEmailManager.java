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

package org.digijava.kernel.mail;

// System  packages
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.google.common.base.Strings;
import org.apache.commons.validator.EmailValidator;
import org.apache.log4j.Logger;
import org.digijava.kernel.config.ForwardEmails;
import org.digijava.kernel.config.Smtp;
import org.digijava.kernel.dbentity.Mail;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.mail.util.DbUtil;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.module.message.dbentity.AmpMessage;
import org.digijava.module.sdm.dbentity.Sdm;
import org.digijava.module.sdm.dbentity.SdmItem;

import com.sun.istack.ByteArrayDataSource;

public class DgEmailManager {

    private static final Map locale2encoding;
    private static final String DEFAULT_ENCODING_EX = "ISO-8859-1";
    private static String DEFAULT_ENCODING = null;

    private static String[] SCHEMA_ARRAY = {
        "http", "https", "ftp"};
    private static String SCHEMA_DELIMITER = "://";

    private static Logger logger = Logger.getLogger(DgEmailManager.class);
    private static Logger emailLogger = Logger.getLogger("amp-email");
    private static Pattern CarReturnPattern = null;

    /**
     * Email mode can be either 'smtp' or 'log'. If not specified defaults to 'log'. Any value different from 'smtp'
     * will fall back to 'log' option.
     */
    private static final boolean EMAIL_SENDING_ENABLED = "smtp".equalsIgnoreCase(System.getProperty("email.mode"));

    private static final int CONSOLE_LINE_LENGTH = 80;

    static {
        if (!EMAIL_SENDING_ENABLED) {
            logger.warn(Strings.repeat("-", CONSOLE_LINE_LENGTH));
            logger.warn("Emails will be written to log instead of being sent to smtp!");
            logger.warn("In production environment configure java with '-Demail.mode=smtp'");
            logger.warn(Strings.repeat("-", CONSOLE_LINE_LENGTH));
        }

        CarReturnPattern = Pattern.compile("(\r|\n|\r\n|\n\r)");

        locale2encoding = new HashMap();

        InputStream inStream = DgEmailManager.class.getClassLoader().
            getResourceAsStream("org/digijava/kernel/mail/locales.properties");
        Properties props = new Properties();
        try {
            props.load(inStream);
        }
        catch (IOException ex) {
            logger.debug(
                "Unable to locales.properties from the classpath org.digijava.kernel.mail");
        }

        Enumeration enumNames = props.propertyNames();
        while (enumNames.hasMoreElements()) {
            String propName = (String) enumNames.nextElement();
            int keySeparatorPos = propName.lastIndexOf('.');
            if (keySeparatorPos > -1) {
                String locType = propName.substring(keySeparatorPos + 1,
                    propName.length());
                if (locType.equalsIgnoreCase("default")) {
                    DEFAULT_ENCODING = props.getProperty(propName);
                }
                else {
                    String charsetStr = props.getProperty(propName);
                    locale2encoding.put(locType, charsetStr);
                }
            }
            else {
                logger.debug("Locale key '" + propName + "' is invalid");
            }
        }

        if (DEFAULT_ENCODING == null) {
            DEFAULT_ENCODING = DEFAULT_ENCODING_EX;
        }

        /*
         locale2encoding.put("ja", "ISO-2022-jp");
         locale2encoding.put("jp", "ISO-2022-jp"); // Workaround for invalid locales
         locale2encoding.put("tr", "ISO-8859-9");
         locale2encoding.put("ru", "KOI8-R");
         locale2encoding.put("el", "ISO-8859-7");
         locale2encoding.put("ka", "UTF-8");
         locale2encoding.put("ro", "ISO-8859-2");
         locale2encoding.put("zh", "EUC-CN");
         */
    }

    public static void triggerStaticInitializers() {
    }

    /**
     * <p>Title: DgEmailManager.java </p>
     * <p>Description:
     </p>
     * <p>Copyright: Copyright (c) 2003</p>
     * <p>Company: </p>
     * @author not attributable
     * @version 1.0
     */

    /**
     * Description                  :  Function to convert regular text to the HTML version
     * @param String                :  Text to be passed as body of the message
     * @return                      :  HTML version of the input text
     * Note                         :  Implementation of these conversions needs to be carried out in line with the existing HTML - Text conversion logic
     *   */

    public static String convertTextToHtml(String strText) {

        String strHtml = strText;

        logger.debug("TEXT:" + strHtml);

        //replace \n,\r,\n\r  with <br />
        strHtml = strHtml.replaceAll("\n", "<br />");
//      strHtml = strHtml.replaceAll("[\n\r]", "<br />");

        int startIndex = 0;
        while ( (startIndex = strHtml.indexOf(SCHEMA_DELIMITER, startIndex)) >=
               0) {
            boolean isLink = false;
            int schemaLength = 0;

            for (int i = 0; i < SCHEMA_ARRAY.length; i++) {
                if ( (startIndex - SCHEMA_ARRAY[i].length() >= 0) &&
                    strHtml.substring(startIndex - SCHEMA_ARRAY[i].length(),
                                      startIndex).equals(SCHEMA_ARRAY[i])) {

                    startIndex -= SCHEMA_ARRAY[i].length();
                    schemaLength = SCHEMA_ARRAY[i].length() +
                        SCHEMA_DELIMITER.length();
                    isLink = true;
                }
            }
            if (!isLink || strHtml.startsWith("\"", startIndex - 1) ||
                strHtml.startsWith("'", startIndex - 1)) {
                break;
            }
            //extract internet address
            logger.debug("Searching for links in the string: " + strHtml);
            int minIndex = -1;
            for (int i = startIndex + schemaLength;
                 i < strHtml.length() && minIndex < 0;
                 i++) {
                char c = strHtml.charAt(i);
                switch (c) {
                    case '<':
                    case ' ':
                    case ')':
                    case '(':
                    case '\n':
                        minIndex = i;
                        break;
                }
            }
            if (minIndex < 0) {
                minIndex = strHtml.length();
                logger.debug("terminating character was not found. Assuming end of the string as the end of the link");
            }
            else {
                logger.debug("link terminator character was found at index " +
                             minIndex + "(" + strHtml.charAt(minIndex) + " )");
            }

            String link = strHtml.substring(startIndex, minIndex);
            int linkLength = link.length();
            //convert link
            link = "<a href=\"" + link + "\">" + link + "</a>";

            strHtml = strHtml.substring(0, startIndex) +
                link +
                strHtml.substring(startIndex + linkLength, strHtml.length());

            startIndex += link.length();
        }

        logger.debug("HTML:" + strHtml);

        return strHtml;

    }

    /**
     * Description                  :  Function to convert HTML to text
     * @param String                :  Html Text
     * @return                      :  Plain text
     * Note                         :  Implementation of these conversions needs to be carried out in line with the existing Text - Html conversion logic
     *   */

    String convertHtmlToText(String strHtmlText) {
        return "";
    }

    /**
     * send mail directly
     * @param to
     * @param subject
     * @param text
     * @throws java.lang.Exception
     */
    public static void sendMail(String to, String subject, String text) throws java.lang.Exception {
        // Get SMTP object from configuration file,
        // see digi.xml for more details
        Smtp smtp = DigiConfigManager.getConfig().getSmtp();
        InternetAddress address = new InternetAddress(to);

        sendMail(new Address[] {address}, smtp.getFrom(), subject, text, DEFAULT_ENCODING, false);
    }

    /**
     * send mail directly
     *
     * @param to
     * @param text
     * @throws java.lang.Exception
     */
    public static void sendMail(String to, String subject, String text,Locale locale) throws java.lang.Exception {

        String encoding = (String) locale2encoding.get(locale.getCode());
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }

        // Get SMTP object from configuration file,
        // see digi.xml for more details
        Smtp smtp = DigiConfigManager.getConfig().getSmtp();

        InternetAddress address = new InternetAddress(to);

        sendMail(new Address[] {address}, smtp.getFrom(), subject, text, encoding, false);
    }

    /**
     * send mail directly
     *
     * @param to
     * @param text
     * @throws java.lang.Exception
     */
    public static void sendMail(String to, String subject, String text,Locale locale, boolean asHtml) throws java.lang.Exception {

        String encoding = (String) locale2encoding.get(locale.getCode());
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        // Get SMTP object from configuration file,
        // see digi.xml for more details
        Smtp smtp = DigiConfigManager.getConfig().getSmtp();

        InternetAddress address = new InternetAddress(to);

        sendMail(new Address[] {address} , smtp.getFrom(), subject, text, encoding, asHtml);
    }

    /**
     * send mail directly
     * @param to
     * @param text
     * @throws java.lang.Exception
     */
    public static void sendMail(String to, String from, String subject,String text,Locale locale, boolean asHtml) throws java.lang.Exception {

        String encoding = (String) locale2encoding.get(locale.getCode());
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }

        InternetAddress address = new InternetAddress(to);
        sendMail(new Address[] {address}, from, subject, text, encoding, asHtml);
    }

    public static void sendMail(String to, String from, String cc, String bcc, String subject, String text, Locale locale, boolean asHtml) throws
        java.lang.Exception {
        Address[] address = null;
        Address[] addressCC = null;
        Address[] addressBCC = null;

        String encoding = (String) locale2encoding.get(locale.getCode());
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }

        if (to != null)
            address = new Address[] {new InternetAddress(to)};

        if (cc != null)
            addressCC = new Address[] {new InternetAddress(cc)};

        if (bcc != null)
            addressBCC = new Address[] {new InternetAddress(bcc)};

        sendMail(address , from, addressCC, addressBCC, subject, text, encoding, asHtml);
    }

    /**
     * send mail directly
     * @param to
     * @param from
     * @param subject
     * @param text
     * @throws java.lang.Exception
     */
    public static void sendMail(Address[] to, String from, String subject, String text) throws java.lang.Exception {
        sendMail(to, from, subject, text, DEFAULT_ENCODING, false);
    }

    /**
     * send mail directly
     * @param to
     * @param from
     * @param subject
     * @param text
     * @param locale
     * @param asHtml
     * @throws java.lang.Exception
     */
    public static void sendMail(Address[] to, String from, String subject, String text, Locale locale, boolean asHtml) throws java.lang.Exception {
        String encoding = (String) locale2encoding.get(locale.getCode());
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        sendMail(to, from, subject, text, encoding, asHtml);
    }

    /**
     * send mail directly
     * @param to
     * @param from
     * @param subject
     * @param text
     * @param locale
     * @throws java.lang.Exception
     */
    public static void sendMail(Address[] to, String from, String subject, String text, Locale locale) throws java.lang.Exception {
        String encoding = (String) locale2encoding.get(locale.getCode());
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        sendMail(to, from, subject, text, encoding, false);
    }

    /**
     *
     * @param to
     * @param from
     * @param cc
     * @param bcc
     * @param subject
     * @param text
     * @param locale
     * @param asHtml
     * @throws java.lang.Exception
     */
    public static void sendMail(Address[] to, String from, Address[] cc, Address[] bcc, String subject, String text, Locale locale, boolean asHtml) throws
        java.lang.Exception {

        String encoding = (String) locale2encoding.get(locale.getCode());
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }

        sendMail(to, from, cc, bcc, subject, text, encoding, asHtml);
    }

    /**
     *
     * @param to Address[]
     * @param from String
     * @param cc Address[]
     * @param bcc Address[]
     * @param subject String
     * @param text String
     * @param charset String
     * @param asHtml boolean
     * @throws Exception
     */
    public static void sendMail(Address[] to, String from, Address[] cc, Address[] bcc, String subject, String text, String charset, boolean asHtml) throws
        java.lang.Exception {

        sendMail(to, from, cc, bcc, subject, text, charset, asHtml, DigiConfigManager.getConfig().getSmtp().isLogEmail());
    }



    /**
     *
     * @param to Address[]
     * @param from String
     * @param cc Address[]
     * @param bcc Address[]
     * @param subject String
     * @param text String
     * @param charset String
     * @param asHtml boolean
     * @param log boolean
     * @throws Exception
     */
    public static void sendMail(Address[] to, String from, Address[] cc, Address[] bcc, String subject, String text, String charset, boolean asHtml,
                                boolean log) throws java.lang.Exception {

        sendMail(to, from, cc, bcc, subject, text, charset, asHtml, log, false);
    }



    public static void sendMail(Address[] to, String from, Address[] cc, Address[] bcc, String subject, String text, String charset, boolean asHtml,
                                boolean log, boolean rtl) throws java.lang.Exception {
        String toEmails = "";
        if (to != null) {
            toEmails = "[" + String.join(", ",
                    Arrays.asList(to).stream().map(Address::toString).collect(Collectors.toList())) + "]";
        }
        emailLogger.debug("Sending mail from " + from + " to " + (to != null ? toEmails : "none")
                + " recipient(s). Subject: "
                + subject + ". Encoding: " + charset + ". asHtml: " + asHtml);
        emailLogger.debug("Mail text:\n" + text);

        if (!EMAIL_SENDING_ENABLED) {
            return;
        }

        // see digi.xml for more details
        logger.info("Start Getting Config");
        Smtp smtp = DigiConfigManager.getConfig().getSmtp();

        //If the from address is the default we will use the one configured in amp/repository/digi.xml
        if (from.equals(EmailConstants.DEFAULT_EMAIL_SENDER) && smtp.getFrom() != null) {
            if (EmailValidator.getInstance().isValid(smtp.getFrom())) {
                from = smtp.getFrom();
            } else {
                logger.error("Email Address configured in amp/repository/digi.xml is not valid");
            }
        }

        PlainTextEmailMessage emailMessage = createEmailMessage(to,new InternetAddress(from), cc, bcc, subject, text, charset, asHtml, rtl);

        // Get SMTP object from configuration file,
        


        logger.info("End Getting Config");
        logger.debug("SMTP User Name: " + smtp.getUserName() + " Password: " + smtp.getUserPassword());
        logger.info("Start Getting Forward Emails");
        ForwardEmails forwardEmails = DigiConfigManager.getConfig().getForwardEmails();
        logger.info("End Getting Forward Emails");

        // Mail session needs property,
        // we create default property key and fill it
        Properties props = new Properties();
        props.put("mail.smtp.host", smtp.getHost());
        logger.info("SMTP host resolved:" +props.getProperty("mail.smtp.host"));
        if (smtp.getUserName() != null && smtp.getUserPassword() != null) {
            props.put("mail.smtp.auth", "true");
        }
        logger.info("Getting session default instance");
        javax.mail.Session session = javax.mail.Session.getDefaultInstance( props, null);
        logger.info("Finished getting session default instance");


        Address addresses[] = null;
        if (forwardEmails.isEnabled() && forwardEmails.getEmails() != null) {
            addresses = new Address[forwardEmails.getEmails().size()];
            for (int i = 0; i < forwardEmails.getEmails().size(); i++) {
                addresses[i] = new InternetAddress( (String) forwardEmails.
                    getEmails().get(i));
            }
        }
        
        logger.info("start creating Mime Message");
        
        // We create mime message, recipient,
        // to, subject and message content
        MimeMessage message = createMimeMessage(session, emailMessage, addresses);
        logger.info("end creating Mime Message");
        
        try {
            // send mail directly
            logger.info("before connect: "+ System.currentTimeMillis());
            Transport transport = session.getTransport("smtp");
            transport.connect(smtp.getHost(), smtp.getUserName(),smtp.getUserPassword());
            logger.info("after connect: "+ System.currentTimeMillis());

            message.saveChanges(); // implicit with send()
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            logger.info("after close: "+ System.currentTimeMillis());

            // log mail
            if (log) {
                DbUtil.saveMail(to, from, cc, bcc, subject, emailMessage.getText(), charset, asHtml);
            }
        }
        catch (MessagingException ex) {

            // log mail
            if (log) {
                DbUtil.saveMail(to, from, cc, bcc, subject, emailMessage.getText(), charset,asHtml,ex.getMessage());
            }
            throw ex;
        }
    }
    
    
    public static void sendMail(Address[] to,String from,AmpMessage ampMessage,Sdm attachmentsHolder) throws Exception{     
        // Get SMTP object from configuration file, see digi.xml for more details
        Smtp smtp = DigiConfigManager.getConfig().getSmtp();
        ForwardEmails forwardEmails = DigiConfigManager.getConfig().getForwardEmails();

        // Mail session needs property,
        // we create default property key and fill it
        Properties props = new Properties();
        props.put("mail.smtp.host", smtp.getHost());        
        if (smtp.getUserName() != null && smtp.getUserPassword() != null) {
            props.put("mail.smtp.auth", "true");
        }
        javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, null);

        Address addresses[] = null;
        if (forwardEmails.isEnabled() && forwardEmails.getEmails() != null) {
            addresses = new Address[forwardEmails.getEmails().size()];
            for (int i = 0; i < forwardEmails.getEmails().size(); i++) {
                addresses[i] = new InternetAddress( (String) forwardEmails.getEmails().get(i));
            }
        }       
        
        MimeMessage message = new MimeMessage(session);   
         if (addresses != null) {
             message.addRecipients(Message.RecipientType.TO, addresses);
         }
        message.setFrom(new InternetAddress(from));
        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject(ampMessage.getName(),DEFAULT_ENCODING);
        message.setSentDate(new Date());        
        // Set the email message text.
        MimeBodyPart messagePart = new MimeBodyPart();
        messagePart.setText(ampMessage.getDescription());
        
        
        Multipart multipart = new MimeMultipart();
        //add message text
        multipart.addBodyPart(messagePart);
        if(attachmentsHolder != null){
            Set<SdmItem> attachments=attachmentsHolder.getItems();
            if(attachments!=null && attachments.size()>0){
                for (SdmItem attachment : attachments) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    DataSource fileDataSource = new ByteArrayDataSource(attachment.getContent(),"application/octet-stream");
                    attachmentPart.setDataHandler(new DataHandler(fileDataSource));
                    attachmentPart.setFileName(attachment.getContentTitle());
                    //add message attachments
                    multipart.addBodyPart(attachmentPart);
                }                
            }
        }
        
        message.setContent(multipart);
        
        try {
            // send mail directly
            Transport transport = session.getTransport("smtp");
            transport.connect(smtp.getHost(), smtp.getUserName(),smtp.getUserPassword());
            message.saveChanges(); // implicit with send()
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
        }
        catch (MessagingException ex) {
            throw ex;
        }
    }

    /**
     * send mail directly
     * @param to
     * @param from
     * @param subject
     * @param text
     * @param encoding
     * @param asHtml
     * @throws java.lang.Exception
     */
    public static void sendMail(Address[] to, String from, String subject, String text, String charset, boolean asHtml) throws java.lang.Exception {

        sendMail(to, from, null, null, subject,text, charset, asHtml);
    }

    public static void sendMail(Address[] to, String from, String subject, String text, String charset, boolean asHtml, boolean rtl) throws
        java.lang.Exception {

        sendMail(to, from, null, null, subject, text, charset, asHtml, DigiConfigManager.getConfig().getSmtp().isLogEmail(), rtl);
    }


    /**
     * Description                  :  Simple version of sending a mail
     * @param Mail                  :  Mail Object with all respective values set
     * @param boolean               :  boolean value specifying whether the message needs to be stored in the TRACK_ADDR_MESS_MAP table
     * @return boolean              :  Success / failure
     *   */

    public static boolean sendEmail(Mail objMail, boolean bStore) {
        return true;
    }

    /**
     * Description                  :  This function will be used  for sending email to a collection of users.
     * @param Mail                  :  The Mail Object with text property specifying the placeholders
     * @param Collection            :  Collection of Users ( User Object), User names ( String)
     * @return boolean              :  Success / failure
     *   */

    public static boolean sendEmail(Mail objMail, Collection objCollection) {
        return true;
    }

    /**
     * Description                  :  This function replaces all the occuranaces of the placeholders in the mentioned as keys in the HashMap
     * @param Mail                  :  The Mail Object with text property specifying the placeholders
     * @param HashMap               :  Key Value pair, where key - placeholder, value - the Actual value which should get replaced
     * @return String               :  Email text with the placeholders replaced with the values from the HashMap
     *   */

    public static String prepareEmail(Mail objMail, HashMap hm) {
        return "";
    }

    /**
     * Description                  :  This function identifies the class associated with each of the Object passed as a parameter.
     *                                 Using reflection, identify the class to which the Object.
     *                                 Using reflection, identify the properties of the Object, which will be specified as placeholders.
     * @param Mail                  :  The Mail Object with text property specifying the placeholders
     * @param Object                :  Object of any class
     * @return String               :  Email text with the placeholders replaced with the values from the HashMap
     *   */

    public static String prepareEmail(Mail objMail, Object obj) {
        return "";
    }

    /**
     * Description                  :  This function will use the dgmarket's, Translation Manager class
     *                                 to translate the TRN Tags specified in the Text being sent.
     * @param String                :  email Text with placeholders specified with the TRN Tags
     * @param Object                :  Site Id which will be used by the TRN tags to replace the Keys
     * @return String               :  Email text with the language specific Translation Text embedded
     *                                 into the input Text.
     *   */

    public static String translateEmail(String strText, String siteId) {
        return "";
    }

    /**
     * Description                  :   This function will decipher the mail id, User Id parameter and the Action performed on a URL sent with the message text.
     *                                  These values will be used to update TRACK_ADDR_MESS_MAP table, if found UNIQUE User Id
     *                                  c123456 - c- Create, 123 - MesssageId, 456 - UserId
     *                                  o123456 - o- Open, 123 - MesssageId, 456 - UserId
     *                                  This method might need to be synchornised.
     * @param String                :  String to specify the c123456  parameter
     * @return boolean              :  true/false
     *   */

    public static boolean updateMessageStats(String strText) {
        return true;
    }

    public static int sendEmailMessages(Collection emailMessages, EmailProcessingCallback callback) throws Exception {

        // Get SMTP object from configuration file,
        // see digi.xml for more details
        Smtp smtp = DigiConfigManager.getConfig().getSmtp();
        logger.debug("SMTP User Name: " + smtp.getUserName() + " Password: " + smtp.getUserPassword());
        ForwardEmails forwardEmails = DigiConfigManager.getConfig(). getForwardEmails();

        // Mail session needs property,
        // we create default property key and fill it
        Properties props = new Properties();
        props.put("mail.smtp.host", smtp.getHost());
        if (smtp.getUserName() != null && smtp.getUserPassword() != null) {
            props.put("mail.smtp.auth", "true");
        }

        Address addresses[] = null;
        if (forwardEmails.isEnabled() && forwardEmails.getEmails() != null) {
            addresses = new Address[forwardEmails.getEmails().size()];
            for (int i = 0; i < forwardEmails.getEmails().size(); i++) {
                addresses[i] = new InternetAddress( (String) forwardEmails.getEmails().get(i));
            }
        }

        javax.mail.Session session = javax.mail.Session.getDefaultInstance( props, null);

        // send mail directly
        Transport transport = session.getTransport("smtp");
        transport.connect(smtp.getHost(), smtp.getUserName(), smtp.getUserPassword());

        int numOfSuccesses = 0;
        Iterator iter = emailMessages.iterator();
        while (iter.hasNext()) {
            PlainTextEmailMessage emailMessage = (PlainTextEmailMessage) iter. next();

            // We create mime message, recipient,
            // to, subject and message content
            MimeMessage message = createMimeMessage(session, emailMessage,
                addresses);
            try {
                message.saveChanges(); // implicit with send()
                transport.sendMessage(message, message.getAllRecipients());

                if (callback != null) {
                    callback.sendSuccessed(emailMessage);
                }
                numOfSuccesses ++;
            }
            catch (MessagingException ex) {
                if (callback != null) {
                    callback.sendError(emailMessage, ex.getMessage(), ex);
                }
            }
        }
        transport.close();

        return numOfSuccesses;
    }

    private static MimeMessage createMimeMessage(javax.mail.Session session, PlainTextEmailMessage emailMessage, Address[] addresses) throws MessagingException {
        MimeMessage initialMessage = createMimeMessage(session, (EmailMessage)emailMessage, addresses);

        String bodyHeader = null;
        if (addresses != null) {

            bodyHeader = "----------- Digi Mailer ----------\n";
            if (emailMessage.getTORecipients() != null) {
                bodyHeader += "TO: ";
                bodyHeader += InternetAddress.toString(emailMessage.getTORecipients());
                bodyHeader += "\n";
            }

            if (emailMessage.getCCRecipients() != null) {
                bodyHeader += "CC: ";
                bodyHeader += InternetAddress.toString(emailMessage.getCCRecipients());
                bodyHeader += "\n";
            }

            if (emailMessage.getBCCRecipients() != null) {
                bodyHeader += "BCC: ";
                bodyHeader +=
                    InternetAddress.toString(emailMessage.getBCCRecipients());
                bodyHeader += "\n";
            }
            bodyHeader += "----------------------------------\n\n\n";
        }

        String text = emailMessage.getText();
        if (bodyHeader != null) {
            text = bodyHeader + text;
        }

        String realCharset = getRealCharset(emailMessage);
        if (emailMessage.isHtmlRequired() || emailMessage.isRTL()) {

            Multipart content = new MimeMultipart("alternative");
            MimeBodyPart mbpText = new MimeBodyPart();
            MimeBodyPart mbpHtml = new MimeBodyPart();

            String mimeType = "text/plain; charset=" + realCharset;
            mbpText.setContent(text, mimeType);

            String htmlText = convertTextToHtml(text);
            mimeType = "text/html; charset=" + realCharset;

            if (emailMessage.isRTL()) {
              htmlText = "<HTML dir=rtl><HEAD></HEAD><BODY>" + htmlText + "</BODY></HTML>";
            }
            mbpHtml.setContent(htmlText, mimeType);
            
            content.addBodyPart(mbpText);
            content.addBodyPart(mbpHtml);

            initialMessage.setContent(content);
        }else {
            initialMessage.setText(text, realCharset);
        }
        return initialMessage;
    }

    private static MimeMessage createMimeMessage(javax.mail.Session session,EmailMessage emailMessage, Address[] addresses) throws MessagingException {

        if (emailMessage.getTORecipients() == null & emailMessage.getCCRecipients() == null && emailMessage.getBCCRecipients() == null) {
            throw new IllegalArgumentException("one of the parameter to,cc,bcc must be not-null");
        }

        MimeMessage message = new MimeMessage(session);

        message.setFrom(emailMessage.getSender());

        String subject = emailMessage.getSubject();
        //Remove return and newline characters from subject string
        if (subject != null && subject.length() > 0) {
            Matcher matcher = CarReturnPattern.matcher(subject);
            if (matcher.find()) {
                subject = matcher.replaceAll("");
            }
        }

        if (addresses != null) {
            message.addRecipients(Message.RecipientType.TO, addresses);
        } else {
            if (emailMessage.getTORecipients() != null)
                message.addRecipients(Message.RecipientType.TO, emailMessage.getTORecipients());

            if (emailMessage.getCCRecipients() != null)
                message.addRecipients(Message.RecipientType.CC, emailMessage.getCCRecipients());

            if (emailMessage.getBCCRecipients() != null)
                message.addRecipients(Message.RecipientType.BCC, emailMessage.getBCCRecipients());
        }

        message.setSubject(subject, getRealCharset(emailMessage));

        return message;
    }

    private static String getRealCharset(EmailMessage emailMessage) {
        String realCharset = emailMessage.getCharset();
        if (realCharset == null) {
            realCharset = DEFAULT_ENCODING;
        }
        return realCharset;
    }

    public static PlainTextEmailMessage createEmailMessage(final Address[] to, final Address from, final Address[] cc, final Address[] bcc, final String subject,
        final String text, final String charset, final boolean asHtml) {
      return createEmailMessage(to, from, cc, bcc,  subject,  text,  charset,  asHtml, false);
    }


    public static PlainTextEmailMessage createEmailMessage(final Address[] to,
        final Address from, final Address[] cc,
        final Address[] bcc, final String subject,
        final String text, final String charset, final boolean asHtml, final boolean rtl) {

        PlainTextEmailMessage emailMessage = new PlainTextEmailMessage() {
            public Address getSender() {
                return from;
            }

            public Address[] getTORecipients() {
                return to;
            }

            public Address[] getCCRecipients() {
                return cc;
            }

            public Address[] getBCCRecipients() {
                return bcc;
            }

            public String getSubject() {
                return subject;
            }

            public String getCharset() {
                return charset;
            }

            public String getText() {
                return text;
            }

            public boolean isHtmlRequired() {
                return asHtml;
            }

            public boolean isRTL() {
              return rtl;
            }
        };
        return emailMessage;
    }


} // End of the DgEmailManager
