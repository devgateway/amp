<%@ page language="java" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ page import="org.digijava.module.poll.entity.PollOption" %>

<digi:instance property="pollForm" />
<P>POLL PIRVELI <bean:write name="pollForm" property="worker" /></P>
<!------------------------ POLL QUESTIONS -------------------------->
<logic:notEqual name="pollForm" property="voted" value="true">
<digi:form action="/pollAction.do" >
<table>
<tr><td>
<bean:write name="pollForm" property="poll.question" />
</tr></td>
<logic:iterate name="pollForm" id="option" property="poll.pollOptions" type="PollOption">
<tr><td>
<html:radio property="selectedOption" value="<%= Long.toString(option.getPollOptionId()) %>" />
<bean:write name="option" property="name" />
</tr></td>
</logic:iterate>
<tr><td>
</tr></td>
</table>
<html:submit property="submit" value="GO"/>
</digi:form>
</logic:notEqual>


<!------------------------ POLL ANSWERS -------------------------->
<logic:equal name="pollForm" property="voted" value="true">
<table>
<tr><td>
<bean:write name="pollForm" property="poll.question" />
</tr></td>
<logic:iterate id="option" name="pollForm" property="poll.pollOptions" type="PollOption">
<tr><td>
<bean:write name="option" property="name" />&nbsp; - &nbsp;<bean:write name="option" property="votes" />
</tr></td>
</logic:iterate>
<tr><td>
</tr></td>
</table>
</logic:equal>


