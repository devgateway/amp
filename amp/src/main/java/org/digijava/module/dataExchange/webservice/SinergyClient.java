package org.digijava.module.dataExchange.webservice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.codec.binary.Base64;
import org.digijava.module.dataExchange.utils.DEConstants;
import org.digijava.module.dataExchange.utils.HashMessage;

public class SinergyClient {

	private String endpoint;

	public SinergyClient(String url) {
		this.endpoint = url;
	}

	/**
	 * 
	 * @param user
	 * @param paswword
	 * @return
	 */
	
	public OutputStream getFileFromService(String user, String paswword,String xsltfile) {

		try {
			// Creating current date, which will be used in encryption key.
			// Date's format must be MM/dd/yyyy.
			Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy",
					new DateFormatSymbols(Locale.ENGLISH));
			String dateStr = format.format(now);

			URL url = new URL(endpoint + "projects/" + dateStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			// Accept XML stream
			conn.setRequestProperty("accept", "application/xml");
			conn.setRequestProperty("Date", dateStr);

			// key of encryption
			String key = url.toString() + "\n" + dateStr;

			// encrypting password.
			InputStream passwordStream = new ByteArrayInputStream(
					paswword.getBytes());
			HashMessage hmac = new HashMessage(key);
			byte[] hmac_sha1 = null;
			try {
				hmac_sha1 = hmac.encrypt(passwordStream);
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}

			// Creating Base64 for encrypted password
			String base64 = new String(Base64.encodeBase64(hmac_sha1));
			conn.setRequestProperty("Authorization", "Custom " + user + ":"
					+ base64);
			conn.setUseCaches(false);
			conn.connect();
			InputStream in;
			// if response is OK then
			if (conn.getResponseCode() == 200) {
				in = conn.getInputStream();
				return synergyIatiToIdml(in,xsltfile);
			} else {
				conn.disconnect();
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Take the received Xml and transform it to IDML format
	 * @param in
	 * @return
	 */
	
	private OutputStream synergyIatiToIdml(InputStream in, String path) {
		OutputStream out = new ByteArrayOutputStream();
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer;
		URL rootUrl = this.getClass().getResource("/");
		/*String path = "";
		try {
			path = rootUrl.toURI().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		*/
		try {
			transformer = tFactory.newTransformer(new StreamSource(path));
			transformer.setOutputProperty(OutputKeys.INDENT,"yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(new StreamSource(in), new StreamResult(out));

		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	/**
	 * Convert from OutputStream to InputStream 
	 * @param OutputStream
	 * @return
	 */
	
	public InputStream OutToIn(OutputStream arg) {
		ByteArrayOutputStream bout = (ByteArrayOutputStream) arg;
		ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
		return bin;
	}
}
