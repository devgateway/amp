<%@ page language="java" %>
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/resources/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>

<digi:errors/>
  
<digi:form action="/unSubscribeTemporarly.do" method="post">
   <table>
     <tr>
        <td><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<h3><b>Confirm Unsubscribe</b></h3></p></td>
     </tr>
     <tr>
      <td> 
       <p>&nbsp;</p>
      </td>
     </tr>
     <tr>
        <td>that you'd like to unsubscribe from 
        <a href='<digi:site siteId="dgnetwork" />'>Development Gateway</a></td> 
     </tr>  
   </table>
   <table>
   <HR>
   <table width="80%">
   <tr>
     <td>
       If you are interested in this community but wish to stop receiving email then you might want to 
      <ul>
       <li>
          <p>tell the system that you're going on vacation until</p>
             <html:select property="selectedMonth">
                <bean:define id="mid" name="userUnSubscribeForm" property="months"  type="java.util.Collection" />
  		<html:options collection="mid" property="calendarId" labelProperty="calendarText" />
             </html:select>
 	     <html:text name="userUnSubscribeForm" property="day" size="2" />
	     <html:text name="userUnSubscribeForm" property="year" size="4" />
	     <html:submit property="submit" value="Put email on hold" />	
          </li>
       <logic:equal name="userUnSubscribeForm" property="active" value="true">
        <li>
         The system is currently set to send you email notifications. Click here to 
        <digi:link href="/reactivateEmail.do">tell the system not to send you any email notifications.</digi:link>
       </li>
      </logic:equal>
      <logic:equal name="userUnSubscribeForm" property="active" value="false">
        <li>
          The system is currently set to not send you any email notifications. 
          Click here <digi:link href="/unSubscribePermanently.do">
	  to allow system to send you email notifications. </digi:link> 
        </li>
      </logic:equal>
     </ul>	
   </td>  
   </tr>
</table>
</digi:form>