package org.dgfundation.amp.support.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.dgfundation.amp.support.dbentity.RequestSupport;

public class MailSender {
	/**
	 * 
	 * @param srequest
	 * @return boolean
	 */
	
	public static boolean sendSupportMail(RequestSupport srequest) {
		String to = srequest.getCountry().getMail();
		String from = "ampsupport@amp.org";
		String host = "localhost";
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.debug", "true");
		String[] mailcc = srequest.getMailcc().split(";");
		String mails="";
		Session session = Session.getInstance(props);
		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			InternetAddress[] address = { new InternetAddress(to) };
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject("New Support Request");
			msg.setSentDate(new Date());
			for (int i = 0; i < mailcc.length; i++) {
				if ("".equalsIgnoreCase(mails)){
					mails = mailcc[i] + "\n";
				}
				else{
					mails = mails + "   " + mailcc[i] + "\n"; 
				}
			}
			msg.setText("Support Request ID: " + srequest.getId() + "\n"
					+ "Submitted: " + srequest.getDate() + "\n" 
					+ "Country: " + srequest.getLogin().getCountry().getName() + "\n"
					+ "Username: " + srequest.getFullusername() + "\n"
					+ "AMP User: " + srequest.getAmplogin()+ "\n"
					+ "AMP Password: " + srequest.getAmppassword()+ "\n"
					+ "Email: " + srequest.getEmail() + "\n" 
					+ "Browser: " + srequest.getBrowser() + "\n" 
					+ "Browser Version: " + srequest.getBrowserversion() + "\n"
					+ "OS: " + srequest.getOperatingsystem() + "\n" 
					+ "Module AMP: " + srequest.getModule() + "\n" 
					+ "AMP Version:" + srequest.getVersion() + "\n"
					+ "Priority:" + srequest.getLevel() + "\n"
					+ "Subject: " + srequest.getSubject() + "\n"
					+ "CC: " + mails
					+ "Details:" + srequest.getDetails() + "\n");
			Transport.send(msg);
			return true;
		} catch (MessagingException mex) {
			mex.printStackTrace();
			return false;
		}
	}

	public static boolean sendFailureMail(RequestSupport srequest) {
		String to = "amp@code.ro";
		String from = "ampsupport@amp.org";
		String host = "localhost";
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.debug", "true");
		Session session = Session.getInstance(props);
		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			InternetAddress[] address = { new InternetAddress(to) };
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject("Email Failure");
			msg.setSentDate(new Date());
			msg.setText("Body: The email response sent to "
							+ srequest.getFullusername()
							+ "\n"
							+ "at "
							+ srequest.getEmail()
							+ " was unsuccessful. Their Support Request ID is "
							+ srequest.getId()
							+ ". \n"
							+ "This user has not been sent the automated response and therefore you will"
							+ "\n"
							+ "need to follow up with them. The user’s support request was submitted at ");
			Transport.send(msg);
			return true;
		} catch (MessagingException mex) {
			mex.printStackTrace();
			return false;
		}
	}

	public static boolean sendCustomerMail(RequestSupport srequest,String loc) {
		String to = srequest.getEmail();
		String from = "ampsupport@amp.org";
		String host = "localhost";
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.debug", "true");
		Session session = Session.getInstance(props);
		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			InternetAddress[] address = { new InternetAddress(to) };
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject("AMP Request Support");
			msg.setSentDate(new Date());
			msg.setText(generateCustomerText(srequest.getCountry().getName(), loc));
			Transport.send(msg);
			return true;
		} catch (MessagingException mex) {
			mex.printStackTrace();
			return false;
		}
	}
	
	static String generateCustomerText(String country,String location){
		String text = null;
		if (location.equalsIgnoreCase("en")){
			text = "Dear AMP " + country
			+ " Customer "
			+ "\n\n"
			+ "Thank you for your continued work with the AMP software.  We have received your email request for AMP support. The next steps in the process are: "
			+ "\n\n\n"
			+ "         1. This issue will be entered in our bug tracking software for "
			+ country
			+ "\n"
			+ "         2. The AMP "
			+ country
			+ " Support Team will analyze and prioritize the issue in jira."
			+ "\n"
			+ "         3. The AMP "
			+ country
			+ " Support Team will respond with an email to provide an update on the issue and the next steps."
			+ "\n"
			+ "\n\n\n"
			+ "NOTE: The response time for AMP Startup Support is the next business day. The response time for Basic Support is two business days."
			+ "\n" + "Thanks again" + "\n" 
			+ "AMP Support Team";
		}
		if(location.equalsIgnoreCase("fr")){
			text = "Cher Client de la PGA en  " + country
			+ "\n\n"
			+ "Merci pour vos efforts constants dans l'utilisation de la PGA. Nous avons bien reçu votre requête d'assistance technique pour la PGA. Les prochaines étapes du processus sont: "
			+ "\n\n\n"
			+ "         1. L'incident sera saisi dans notre système de suivi des défaillances pour " 
			+ country
			+ "\n"
			+ "         2. L'équipe d'assistance technique de la PGA pour "
			+ country
			+ " analysera et établira un niveau de priorité dans le système JIRA."
			+ "\n"
			+ "         3. L'équipe d'assistance technique répondra à votre courrier électronique pour vous tenir informé des mises à jour et des prochaines étapes."
			+ "\n\n\n"
			+ "REMARQUE : Le temps de réponse pour l'assistance à la PGA selon la formule initiale est d'un (1) jour ouvrable. Le temps de réponse selon la formule de base est de deux (2) jours ouvrables."
			+ "\n" 
			+ "Cordialement," 
			+ "\n" 
			+ "L'équipe d'assistance technique de la PGA";
		}
		if(location.equalsIgnoreCase("es")){
			text = "Estimado Cliente AMP " + country
			+ "\n\n"
			+ "Gracias por su continuo trabajo con el software de AMP.  Hemos recibido su requerimiento de soporte. Los siguientes pasos en el proceso son: "
			+ "\n\n\n"
			+ "         1. Su requerimiento será ingresado en nuestro software de seguimiento de errores para "
			+ country
			+ "\n"
			+ "         2. El equipo de soporte técnico de AMP para "
			+ country
			+ " analizará y establecerá el nivel de prioridad en el sistema JIRA."
			+ "\n"
			+ "\n"
			+ "         3. El equipo de soporte técnico de AMP responderá a su correo electrónico para mantenerlo informado sobre los avances de este tema."
			+ "\n\n\n"
			+ "NOTA: El tiempo de respuesta para el soporte inicial sera el primer día hábil después de recibido su requerimiento." 
			+" El tiempo de respuesta para el soporte básico es de dos días hábiles después de recibido su requerimiento."
			+ "\n" + "Cordialmente" + "\n" 
			+ "El equipo de soporte de AMP";
		}
		return text;
	}
}
