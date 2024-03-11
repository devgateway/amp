<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
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