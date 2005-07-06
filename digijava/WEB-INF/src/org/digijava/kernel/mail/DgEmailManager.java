/*
 *   DgEmailManager.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created:
 *   CVS-ID: $Id: DgEmailManager.java,v 1.1 2005-07-06 10:34:32 rahul Exp $
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
package org.digijava.kernel.mail;

// System  packages
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;// Application Specific Packages
import java.util.Map;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.digijava.kernel.config.ForwardEmails;
import org.digijava.kernel.config.Smtp;
// Application Specific Packages
import org.digijava.kernel.dbentity.Mail;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.util.DigiConfigManager;


public class DgEmailManager {


    private static final Map locale2encoding;
    private static final String DEFAULT_ENCODING_EX = "ISO-8859-1";
    private static String DEFAULT_ENCODING = null;

    private static Logger logger = Logger.getLogger(DgEmailManager.class);

    static {
        locale2encoding = new HashMap();

        InputStream inStream = DgEmailManager.class.getClassLoader().
          getResourceAsStream("org/digijava/kernel/mail/locales.properties");
        Properties props = new Properties();
        try {
          props.load(inStream);
        }
        catch (IOException ex) {
          logger.debug("Unable to locales.properties from the classpath org.digijava.kernel.mail");
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
          } else {
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
     * Description					:  Function to convert regular text to the HTML version
     * @param String				:  Text to be passed as body of the message
     * @return						:  HTML version of the input text
     * Note							:  Implementation of these conversions needs to be carried out in line with the existing HTML - Text conversion logic
     * 	 */

    public static String convertTextToHtml(String strText) {

        String strHtml = strText;

        logger.debug("TEXT:" + strHtml);

        //replace \n,\r,\n\r  with <br />
        strHtml = strHtml.replaceAll("\n", "<br />");
//        strHtml = strHtml.replaceAll("[\n\r]", "<br />");

        int startIndex = 0;
        while ( (startIndex = strHtml.indexOf("http://", startIndex)) >= 0 &&
                (!strHtml.startsWith("\"", startIndex - 1) &&
                 !strHtml.startsWith("'", startIndex - 1))) {
            //extract internet address
            logger.debug("Searching for links in the string: " + strHtml);
            logger.debug("start index is " + startIndex);
            int minIndex = -1;
            for (int i = startIndex + 7; i < strHtml.length() && minIndex < 0;
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
            logger.debug("LINK BEFORE CONVERSION:" + link);
            link = "<a href=\"" + link + "\">" + link + "</a>";
            logger.debug("LINK AFTER CONVERSION:" + link);

            strHtml = strHtml.substring(0, startIndex) +
                link +
                strHtml.substring(startIndex + linkLength, strHtml.length());

            startIndex += link.length();
        }

        logger.debug("HTML:" + strHtml);

        return strHtml;

    }

    /**
     * Description					:  Function to convert HTML to text
     * @param String				:  Html Text
     * @return						:  Plain text
     * Note							:  Implementation of these conversions needs to be carried out in line with the existing Text - Html conversion logic
     * 	 */

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
    public static void sendMail(String to, String subject, String text) throws
        java.lang.Exception {
        // Get SMTP object from configuration file,
        // see digi.xml for more details
        Smtp smtp = DigiConfigManager.getConfig().getSmtp();
        InternetAddress address = new InternetAddress(to);

        sendMail(new Address[] {address}
                 , smtp.getFrom(), subject, text, DEFAULT_ENCODING, false);
    }

    /**
     * send mail directly
     *
     * @param to
     * @param text
     * @throws java.lang.Exception
     */
    public static void sendMail(String to, String subject, String text,
                                Locale locale) throws
        java.lang.Exception {

        String encoding = (String) locale2encoding.get(locale.getCode());
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }

        // Get SMTP object from configuration file,
        // see digi.xml for more details
        Smtp smtp = DigiConfigManager.getConfig().getSmtp();

        InternetAddress address = new InternetAddress(to);

        sendMail(new Address[] {address}
                 , smtp.getFrom(), subject, text, encoding, false);
    }

    /**
     * send mail directly
     *
     * @param to
     * @param text
     * @throws java.lang.Exception
     */
    public static void sendMail(String to, String subject, String text,
                                Locale locale, boolean asHtml) throws
        java.lang.Exception {

        String encoding = (String) locale2encoding.get(locale.getCode());
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        // Get SMTP object from configuration file,
        // see digi.xml for more details
        Smtp smtp = DigiConfigManager.getConfig().getSmtp();

        InternetAddress address = new InternetAddress(to);

        sendMail(new Address[] {address}
                 , smtp.getFrom(), subject, text, encoding, asHtml);
    }

    /**
     * send mail directly
     *
     * @param to
     * @param text
     * @throws java.lang.Exception
     */
    public static void sendMail(String to, String from, String subject,
                                String text,
                                Locale locale, boolean asHtml) throws
        java.lang.Exception {

        String encoding = (String) locale2encoding.get(locale.getCode());
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }

        InternetAddress address = new InternetAddress(to);

        sendMail(new Address[] {address}
                 , from, subject, text, encoding, asHtml);
    }


    public static void sendMail(String to, String from, String cc, String bcc, String subject,
                                String text,
                                Locale locale, boolean asHtml) throws
        java.lang.Exception {
        Address []address = null;
        Address []addressCC = null;
        Address []addressBCC = null;

        String encoding = (String) locale2encoding.get(locale.getCode());
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }

        if( to != null )
            address = new Address[] {new InternetAddress(to)};

        if( cc != null )
        addressCC = new Address[] {new InternetAddress(cc)};

        if( bcc != null )
        addressBCC = new Address[] {new InternetAddress(bcc)};

        sendMail(address
                 , from, addressCC
                 , addressBCC
                 , subject, text, encoding, asHtml);
    }

    /**
     * send mail directly
     * @param to
     * @param from
     * @param subject
     * @param text
     * @throws java.lang.Exception
     */
    public static void sendMail(Address[] to, String from, String subject,
                                String text) throws
        java.lang.Exception {
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
    public static void sendMail(Address[] to, String from, String subject,
                                String text, Locale locale, boolean asHtml) throws
        java.lang.Exception {
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
    public static void sendMail(Address[] to, String from, String subject,
                                String text, Locale locale) throws
        java.lang.Exception {
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
    public static void sendMail(Address[] to, String from, Address[] cc,
                                Address[] bcc, String subject,
                                String text, Locale locale, boolean asHtml) throws
        java.lang.Exception {

        String encoding = (String) locale2encoding.get(locale.getCode());
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }

        sendMail(to, from, cc, bcc, subject, text, encoding, asHtml);
    }



    /**
     *
     * @param to
     * @param from
     * @param cc
     * @param bcc
     * @param subject
     * @param text
     * @param charset
     * @param asHtml
     * @throws java.lang.Exception
     */
    public static void sendMail(Address[] to, String from, Address[] cc,
                                Address[] bcc, String subject,
                                String text, String charset, boolean asHtml) throws
        java.lang.Exception {
        String bodyHeader = null;


        logger.debug("Sending mail from " + from + " to " +
                     (to != null ? to.length : 0) + " recipient(s). Subject: " +
                     subject + ". Encoding: " + charset + ". asHtml: " + asHtml);
        logger.debug("Mail text:\n" + text);

        String realCharset = charset;
        if (realCharset == null) {
            realCharset = DEFAULT_ENCODING;
        }

        if( to == null & cc == null && bcc == null ) {
            throw new IllegalArgumentException("one of the parameter to,cc,bcc must be not-null");
        }

        // Get SMTP object from configuration file,
        // see digi.xml for more details
        Smtp smtp = DigiConfigManager.getConfig().getSmtp();
        logger.debug("SMTP User Name: " + smtp.getUserName() + " Password: " + smtp.getUserPassword());
        ForwardEmails forwardEmails = DigiConfigManager.getConfig().
            getForwardEmails();


        // Mail session needs property,
        // we create default property key and fill it
        Properties props = new Properties();
        props.put("mail.smtp.host", smtp.getHost());
        if(  smtp.getUserName() != null && smtp.getUserPassword() != null ) {
            props.put("mail.smtp.auth", "true");
        }
        javax.mail.Session session = javax.mail.Session.getDefaultInstance(
            props, null);



        // We create mime message, recipient,
        // to, subject and message content
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));

        if (forwardEmails.isEnabled() && forwardEmails.getEmails() != null) {

            bodyHeader = "----------- Digi Mailer ----------\n";
            Address addresses[] = new Address[forwardEmails.getEmails().size()];
            for (int i = 0; i < forwardEmails.getEmails().size(); i++) {
                addresses[i] = new InternetAddress( (String) forwardEmails.
                    getEmails().get(i));
            }
            if( to != null ) {
                message.addRecipients(Message.RecipientType.TO, addresses);
                bodyHeader += "TO: ";
                for( int i = 0; i < to.length; i++ ) {
                    bodyHeader += ( (InternetAddress) to[i]).getAddress() + ";";
                }
                bodyHeader += "\n";
            }

            if (cc != null) {
                bodyHeader += "CC: ";
                for( int i = 0; i < cc.length; i++ ) {
                    bodyHeader += ( (InternetAddress) cc[i]).getAddress() + ";";
                }
                bodyHeader += "\n";
            }

            if (bcc != null) {
                bodyHeader += "BCC: ";
                for( int i = 0; i < bcc.length; i++ ) {
                    bodyHeader += ( (InternetAddress) bcc[i]).getAddress() + ";";
                }
                bodyHeader += "\n";

            }

            bodyHeader += "----------------------------------\n\n\n";

        }
        else {
            if( to != null )
                message.addRecipients(Message.RecipientType.TO, to);

            if (cc != null)
                message.addRecipients(Message.RecipientType.CC, cc);

            if (bcc != null)
                message.addRecipients(Message.RecipientType.BCC, bcc);
        }

        message.setSubject(subject, realCharset);

        if (asHtml) {

            Multipart content = new MimeMultipart("alternative");
            MimeBodyPart mbpText = new MimeBodyPart();
            MimeBodyPart mbpHtml = new MimeBodyPart();

            String mimeType = "text/plain; charset=" + realCharset;
            mbpText.setContent(text, mimeType);

            if( bodyHeader != null ) {
                text = bodyHeader + text;
            }

            String htmlText = convertTextToHtml(text);
            mimeType = "text/html; charset=" + realCharset;
            mbpHtml.setContent(htmlText, mimeType);

            content.addBodyPart(mbpText);
            content.addBodyPart(mbpHtml);

            message.setContent(content);
        }
        else {
            message.setText(text, realCharset);
        }

        // send mail directly
        Transport transport = session.getTransport("smtp");
        transport.connect(smtp.getHost(), smtp.getUserName(), smtp.getUserPassword());
        message.saveChanges(); // implicit with send()
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();

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
    public static void sendMail(Address[] to, String from, String subject,
                                String text, String charset, boolean asHtml) throws
        java.lang.Exception {

        sendMail(to, from, null, null, subject,
                 text, charset, asHtml);
    }

    /**
     * Description					:  Simple version of sending a mail
     * @param Mail 					:  Mail Object with all respective values set
     * @param boolean				:  boolean value specifying whether the message needs to be stored in the TRACK_ADDR_MESS_MAP table
     * @return boolean 				:  Success / failure
     * 	 */

    public static boolean sendEmail(Mail objMail, boolean bStore) {
        return true;
    }

    /**
     * Description					:  This function will be used  for sending email to a collection of users.
     * @param Mail 					:  The Mail Object with text property specifying the placeholders
     * @param Collection			:  Collection of Users ( User Object), User names ( String)
     * @return boolean 				:  Success / failure
     * 	 */

    public static boolean sendEmail(Mail objMail, Collection objCollection) {
        return true;
    }

    /**
     * Description					:  This function replaces all the occuranaces of the placeholders in the mentioned as keys in the HashMap
     * @param Mail 					:  The Mail Object with text property specifying the placeholders
     * @param HashMap 				:  Key Value pair, where key - placeholder, value - the Actual value which should get replaced
     * @return String 				:  Email text with the placeholders replaced with the values from the HashMap
     * 	 */

    public static String prepareEmail(Mail objMail, HashMap hm) {
        return "";
    }

    /**
     * Description					:  This function identifies the class associated with each of the Object passed as a parameter.
     * 								   Using reflection, identify the class to which the Object.
     * 								   Using reflection, identify the properties of the Object, which will be specified as placeholders.
     * @param Mail 					:  The Mail Object with text property specifying the placeholders
     * @param Object 				:  Object of any class
     * @return String 				:  Email text with the placeholders replaced with the values from the HashMap
     * 	 */

    public static String prepareEmail(Mail objMail, Object obj) {
        return "";
    }

    /**
     * Description					:  This function will use the dgmarket's, Translation Manager class
     * 								   to translate the TRN Tags specified in the Text being sent.
         * @param String				:  email Text with placeholders specified with the TRN Tags
     * @param Object 				:  Site Id which will be used by the TRN tags to replace the Keys
     * @return String 				:  Email text with the language specific Translation Text embedded
     * 								   into the input Text.
     * 	 */

    public static String translateEmail(String strText, String siteId) {
        return "";
    }

    /**
     * Description					:   This function will decipher the mail id, User Id parameter and the Action performed on a URL sent with the message text.
     * 									These values will be used to update TRACK_ADDR_MESS_MAP table, if found UNIQUE User Id
     * 									c123456 - c- Create, 123 - MesssageId, 456 - UserId
     * 									o123456 - o- Open, 123 - MesssageId, 456 - UserId
     * 									This method might need to be synchornised.
     * @param String				:  String to specify the c123456  parameter
     * @return boolean 				:  true/false
     * 	 */

    public static boolean updateMessageStats(String strText) {
        return true;
    }

} // End of the DgEmailManager