package org.digijava.module.dataExchange.utils;
	import java.io.IOException;
	import java.io.InputStream;
	import java.security.InvalidKeyException;
	import java.security.NoSuchAlgorithmException;

	import javax.crypto.Mac;
	import javax.crypto.spec.SecretKeySpec;

	public class HashMessage {
		
		private static final int BUFFER_SIZE = 2048;
		private String key;
		
		public HashMessage(String key) {
			this.key = key;
		}
		
		public byte[] encrypt(InputStream content) throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException, IOException {
			Mac mac = Mac.getInstance("HmacSHA1");
		    SecretKeySpec secret = new SecretKeySpec(key.getBytes(), "HmacSHA1");
		    mac.init(secret);

			byte[] buffer = new byte[BUFFER_SIZE];
			int len;
			
			while ((len = content.read(buffer)) >= 0) {
				mac.update(buffer, 0, len);
			}
			
		    return mac.doFinal();
		}
	}
