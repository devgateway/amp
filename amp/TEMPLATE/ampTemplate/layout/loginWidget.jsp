<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/digijava.tld" prefix="digi"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/c.tld" prefix="c"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/fn.tld" prefix="fn" %>
<digi:secure authenticated="true">
    <logic:present name="isUserLogged" scope="session">
        <div class="loginWidget" title='<digi:trn jsFriendly="true" key="aim:clickToLogoutTheSystem">Click here to logout from the system</digi:trn>'>
        <c:set var="quote">'</c:set>
        <c:set var="escapedQuote">\'</c:set>
        <c:set var="msg">
        ${fn:replace(message,quote,escapedQuote)}
        </c:set>
            <digi:link styleClass="loginWidget" href="/j_spring_logout" module="aim">
                <digi:trn key="aim:logout">Logout</digi:trn>
            </digi:link>
        </div>
    </logic:present>
</digi:secure>