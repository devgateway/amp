<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://digijava.org" prefix="digi"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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