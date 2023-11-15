package org.dgfoundation.amp.onepager.web.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.digijava.kernel.request.Site;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class AmpExceptionPage extends  AmpHeaderFooter{

    
    /**
     * 
     */
    private static final long serialVersionUID = -700176982783476225L;
    
    private String messageDetails;
    private Site site;

    public AmpExceptionPage(Exception e) {  
         
        
        site =((AmpAuthWebSession) getSession()).getSite();
    //  add(new ExternalLink("homePage", "/showDesktop.do", "Return to Desktop"));
        
        
        final WebMarkupContainer    devInfo = new WebMarkupContainer("devInfo");
        devInfo.setOutputMarkupId(true);
        devInfo.setOutputMarkupPlaceholderTag(true);
        devInfo.setVisible(false);
        final AjaxLink<Void> developerNotesLink = new AjaxLink<Void>("developerNotesLink")
        {
           
            private static final long serialVersionUID = 4688441490878648290L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                devInfo.setVisible(!devInfo.isVisible());
                target.add(devInfo);
            }
        };
        
        add(developerNotesLink);
        
        final WebMarkupContainer    errMsgDiv = new WebMarkupContainer("errMsgDiv");
        errMsgDiv.setOutputMarkupId(true);
        errMsgDiv.setOutputMarkupPlaceholderTag(true);
        errMsgDiv.setVisible(true);
        final AjaxLink<Void> exceptionDetailsLink = new AjaxLink<Void>("exceptionDetailsLink")
                {
                   
                    private static final long serialVersionUID = 4688441777878648290L;

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        errMsgDiv.setVisible(!errMsgDiv.isVisible());
                        target.add(errMsgDiv);
                        
                    }
                };
        
        Label errorMessage = new Label("errorMessage",  new PropertyModel<String>( e,"message" ));
        errMsgDiv.add(errorMessage);
        Label siteLabel = new Label("site",  new PropertyModel<String>( site,"name" ));
        errMsgDiv.add(siteLabel);        
        devInfo.add(errMsgDiv);
        devInfo.add(exceptionDetailsLink);
        
        
        
        
        final WebMarkupContainer    stackDiv = new WebMarkupContainer("stackDiv");
        stackDiv.setOutputMarkupId(true);
        stackDiv.setOutputMarkupPlaceholderTag(true);
        stackDiv.setVisible(false);
        final AjaxLink<Void> stackDivLink = new AjaxLink<Void>("stackDivLink")
                {
                   
                    private static final long serialVersionUID = 4688441777878648290L;

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        stackDiv.setVisible(!stackDiv.isVisible());
                        target.add(stackDiv);
                        //target.add(devInfo);
                    }
                };
        devInfo.add(stackDivLink);
        StringBuilder sb = new StringBuilder();  
        sb.append(e.getClass().toString()+"\n");  
        sb.append(joinStackTrace(e)); 
        messageDetails = sb.toString(); 
        stackDiv.add(new TextArea<String>("messageDetails", new PropertyModel<String>( this,"messageDetails" ) ) );  
        
        devInfo.add(stackDiv);
        add(devInfo);
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
        
        
        
        public boolean isErrorPage()
        {
            return true;
        }
}
