package org.dgfoundation.amp.onepager.web.pages;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class AmpExceptionPage extends WebPage{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -700176982783476225L;
	
	private String messageDetails;

	public AmpExceptionPage(Exception e) {  
		 
		add(new ExternalLink("homePage", "/showDesktop.do", "Return to Desktop"));
		 StringBuilder sb = new StringBuilder();  
	        sb.append(e.getClass().toString()+"\n");  
	        sb.append(joinStackTrace(e)); 
	        messageDetails = sb.toString(); 
		add(new TextArea<String>("messageDetails", new PropertyModel<String>( this,"messageDetails" ) ) );  
		
	 }
	
	  public static String joinStackTrace(Throwable e) {  
	        StringWriter writer = null;  
	        try {  
	            writer = new StringWriter();  
	            joinStackTrace(e, writer);  
	            return writer.toString();  
	        }  
	        finally {  
	            if (writer != null)  
	                try {  
	                    writer.close();  
	                } catch (IOException e1) {  
	                    // ignore  
	                }  
	        }  
	    }  
	 public static void joinStackTrace(Throwable e, StringWriter writer) {  
	        PrintWriter printer = null;  
	        try {  
	            printer = new PrintWriter(writer);  
	  
	            while (e != null) {  
	  
	                printer.println(e);  
	                StackTraceElement[] trace = e.getStackTrace();  
	                for (int i = 0; i < trace.length; i++)  
	                    printer.println("\tat " + trace[i]);  
	  
	                e = e.getCause();  
	                if (e != null)  
	                    printer.println("Caused by:\r\n");  
	            }  
	        }  
	        finally {  
	            if (printer != null)  
	                printer.close();  
	        }  
	    }  
	 
	 public String getMessageDetails() {
			return messageDetails;
		}

		public void setMessageDetails(String messageDetails) {
			this.messageDetails = messageDetails;
		}
}
