<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script type="javascript">
function login()
{
	<digi:context name="addUrl" property="context/ampModule/moduleinstance/login.do" />
    document.aimKnowledgeForm.action = "<%=addUrl%>";
    document.aimKnowledgeForm.submit();
}
</script>


<digi:errors/>
<digi:instance property="aimRelatedLinksForm" />
<logic:equal name="aimRelatedLinksForm" property="validLogin" value="false">
<digi:form action="/login.do" name="aimRelatedLinksForm" type="org.digijava.ampModule.aim.form.RelatedLinksForm"
method="post">
<h3 align="center"> Invalid Login. Please Login Again. </h3><p align="center"><html:submit styleClass="dr-menu" value="Log In" onclick="login()" /></p>
</digi:form>
</logic:equal>

<logic:equal name="aimRelatedLinksForm" property="validLogin" value="true">
<table width="100%" >
<TR bgColor=#f4f4f2>
            		<TD align=left>
						&nbsp;&nbsp;
						<jsp:useBean id="urlKnowledge" type="java.util.Map" class="java.util.HashMap"/>
						<c:set target="${urlKnowledge}" property="ampActivityId">
							<bean:write name="aimRelatedLinksForm" property="id"/>
						</c:set>
						<c:set target="${urlKnowledge}" property="tabIndex" value="3"/>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewKnowledge">Click here to view Knowledge</digi:trn>
						</c:set>
						<digi:link href="/viewKnowledge.do" name="urlKnowledge" styleClass="comment" title="${translation}" >
							<digi:trn key="aim:knowledge">Knowledge</digi:trn>
						</digi:link>
						&gt; Document &gt; <bean:write name="aimKnowledgeForm" property="aimRelatedLinksForm"/> Perspective
					</TD></tr>
	<tr>
							<td height=16 valign="center" width=650><span class=subtitle-blue>
								<digi:trn key="aim:documentDetails">
									Document Details
								</digi:trn></span>
							</td>
						</tr>
						<tr>
							<td noWrap width=650 vAlign="top">
								<jsp:include page="viewDocumentDetails.jsp"  />
							</td>
						</tr>
						<tr><td>&nbsp;</td></tr>
	<tr><td>&nbsp;</td></tr>
</table>
</td></tr></table>
</logic:equal>















