package org.digijava.kernel.startup;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.hibernate.Session;

public class AmpSessionListener implements HttpSessionListener {
    
    private static Logger logger = Logger.getLogger(AmpSessionListener.class);

    public AmpSessionListener() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void sessionCreated(HttpSessionEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent arg0) {
        //deallocate any hibernate sessions opened by wicket for dead conversations
        Session hibernateSession = (Session) arg0.getSession().getAttribute(OnePagerConst.ONE_PAGER_HIBERNATE_SESSION_KEY);
        if(hibernateSession!=null && hibernateSession.isOpen()) 
            {
            try {
                hibernateSession.clear();
            } finally {
                hibernateSession.close();
                arg0.getSession().setAttribute(OnePagerConst.ONE_PAGER_HIBERNATE_SESSION_KEY,null);
            }
                logger.info("Forced close on Wicket Hibernate Conversation "+hibernateSession.toString());              
            }
        

    }

}
