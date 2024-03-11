<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>

<script type="javascript">
function login()
{
	<digi:context name="addUrl" property="context/module/moduleinstance/login.do" />
    document.aimKnowledgeForm.action = "<%=addUrl%>";
    document.aimKnowledgeForm.submit();
}
</script>


<digi:errors/>
<digi:instance property="aimKnowledgeForm" />
<logic:equal name="aimKnowledgeForm" property="validLogin" value="false">
<digi:form action="/login.do" name="aimKnowledgeForm" type="org.digijava.module.aim.form.KnowledgeForm"
method="post">
<h3 align="center"> Invalid Login. Please Login Again. </h3><p align="center"><html:submit styleClass="dr-menu" value="Log In" onclick="login()" /></p>
</digi:form>
</logic:equal>

<logic:equal name="aimKnowledgeForm" property="validLogin" value="true">
<table width="100%" >
<TR bgColor=#f4f4f2>
            		<TD align=left>
						&nbsp;&nbsp;
						<jsp:useBean id="urlKnowledge" type="java.util.Map" class="java.util.HashMap"/>
						<c:set target="${urlKnowledge}" property="ampActivityId">
							<bean:write name="aimKnowledgeForm" property="id"/>
						</c:set>
						<c:set target="${urlKnowledge}" property="tabIndex" value="3"/>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewKnowledge">Click here to view Knowledge</digi:trn>
						</c:set>
						<digi:link href="/viewKnowledge.do" name="urlKnowledge" styleClass="comment" title="${translation}" >
							<digi:trn key="aim:knowledge">Knowledge</digi:trn>
						</digi:link>
						&gt; Note &gt; <bean:write name="aimKnowledgeForm" property="perspective"/> Perspective
					</TD></tr>
	<tr>
	<tr>
		<td><table width="80%" align="center" height="100%">
	<tr>
		<td>

		<h5 align="center">Description</h5>	</td>
	</tr>
	<tr>
		<td><br />
		<bean:write name="aimKnowledgeForm" property="description" /></td>
	</tr>
	<tr> <td> &nbsp; </td> </tr>
	<tr><td>&nbsp;</td></tr>
</table>
</td></tr></table>
</logic:equal>















