package org.digijava.module.dataExchange.action;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

import org.apache.log4j.Logger;
import org.digijava.module.aim.util.FeaturesUtil;

public class ImportValidationEventHandler implements ValidationEventHandler {

	private static Logger logger = Logger.getLogger(ImportValidationEventHandler.class);
	
	public boolean handleEvent(ValidationEvent ve) {
		// TODO Auto-generated method stub
//		if (ve.getSeverity()==ve.FATAL_ERROR)
//			logger.error("!!!fatal error!!!");
//		if  (ve .getSeverity()==ve.ERROR) 
//			logger.error("###ERROR###");
		if (ve.getSeverity()==ve.FATAL_ERROR ||  ve .getSeverity()==ve.ERROR) {
			
            ValidationEventLocator  locator = ve.getLocator();
            //print message from validation event
           // System.out.println("Message is " + ve.getMessage());
            //output line and column number
            //System.out.println("Column is " + locator.getColumnNumber() + " at line number " + locator.getLineNumber());
            
            int index=ve.getMessage().indexOf(":")+1;
            String msg1=ve.getMessage().substring(index);
            int i,j;
            i=msg1.indexOf("'")+1;
            if(i>25) {
            	i=msg1.indexOf("\"")+1;
            	j=1+i+msg1.substring(i+1).indexOf("\"");
            }
            else j=1+i+msg1.substring(i+1).indexOf("'");
            String aux=msg1.substring(i,j);
            //System.out.println(aux+"!!!"+"i="+i+":"+ msg1.charAt(i)+":     j="+j+":"+msg1.charAt(j)+":");
            String msg=msg1.replace(aux, "<font color=\"#ff0000\">"+aux+ "</font>" );
            FeaturesUtil.errorLog += "Line: <font color=\"#ff0000\">" + locator.getLineNumber() + "</font> | Column: <font color=\"#ff0000\">"+locator.getColumnNumber() +"</font> | Message: "+ msg + "<br>";
        }
    return true;
	}

}
