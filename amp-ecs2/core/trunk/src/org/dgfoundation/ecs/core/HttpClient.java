package org.dgfoundation.ecs.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Queue;
import java.util.StringTokenizer;

import org.dgfoundation.ecs.exceptions.ECSException;
import org.dgfoundation.ecs.keeper.ErrorKeeperItem;

public class HttpClient {
	private URL url; 
	public HttpClient() throws ECSException {
		try {
			url = new URL("http://localhost/ecs.php");
			//TODO: prox support
			//url.openConnection(proxy);
		} catch (Exception e) {
			throw new ECSException(e);
		}
	}		
		
	public boolean sendError(String serverName, ErrorKeeperItem eki) throws ECSException{
		boolean result = false;
		try{
			String data="{" +
			ECS.t("ecsMethod", "sendError") +
			ECS.t("ServerName", serverName) +
			eki.toJson("eki") +
			"}";
			
	        URLConnection conn = url.openConnection();
	        conn.setUseCaches(false);
	        conn.setDoOutput(true);
	        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	        wr.write(data);
	        wr.flush();
	        
	        // Get the response
	        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        String line = rd.readLine();
	        if (line == null || line.compareTo("success") != 0)
	        	throw new Exception("error while sending! response["+ rd.toString() + "]");
	        result = true;
		}catch (Exception e) {
			ECSException ae = new ECSException(e);
			throw ae;
		}
		return result;
	}

	public boolean sendErrorList(String serverName, Queue<ErrorKeeperItem> errList) throws ECSException{
		boolean result = false;
		try{
			for (ErrorKeeperItem i: errList){
				sendError(serverName, i);
			}
		}catch (Exception e) {
			ECSException ae = new ECSException(e);
			throw ae;
		}
		return result;
	}
	
	public String[] getParameters(String serverName, String report) throws ECSException{
		String[] result = null;
		try{
			String data="{" +
			ECS.t("ecsMethod", "getParameters") +
			ECS.t("ServerName", serverName) +
			ECS.to("report", report) +
			"}";
			
	        URLConnection conn = url.openConnection();
	        conn.setUseCaches(false);
	        conn.setDoOutput(true);
	        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	        wr.write(data);
	        wr.flush();
	        
	        // Get the response
	        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        String line = rd.readLine();
	        if (line == null || line.compareTo("success") != 0)
	        	throw new Exception("error while sending");
	        line = rd.readLine();
	        StringTokenizer st = new StringTokenizer(line, ",");
	        result = new String[5];
	        int i = 0;
	        while (st.hasMoreTokens()){
	        	result[i] = st.nextToken();
	        	i++;
	        	if (i > 4)
	        		break;
	        }
	        
		}catch (Exception e) {
			ECSException ae = new ECSException(e);
			throw ae;
		}
		return result;
	}
}
