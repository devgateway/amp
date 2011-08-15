<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@page import="org.digijava.module.dataExchange.util.ExportHelper"%>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>


<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" contextPath="/aim">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn>Code Chapitre Importer</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 valign="center" width=571><span class=subtitle-blue>
						<digi:trn>Code Chapitre Importer</digi:trn>
						</span>
					</td>
				</tr>
					<tr><td>
					
					<digi:instance property="importChaptersForm" />

<digi:form action="/importChapters.do" enctype="multipart/form-data" method="POST">
            <digi:errors/>
            <p/>
			<html:file property="uploadedFile"/>
			<p/>
			<html:submit property="importPerform"><digi:trn>Import</digi:trn></html:submit>
			
			<logic:equal name="importChaptersForm" property="importPerform" value="true">
			<p></p>
			<b>
			<digi:trn>Import results:</digi:trn>
			</b>
			<table>
			<logic:notEmpty name="importChaptersForm" property="chaptersInserted">
			<tr><td><digi:trn>New Chapters Inserted</digi:trn></td>
			<td>
				<bean:write name="importChaptersForm" property="chaptersInserted"/>
			</td>
			</tr>
			</logic:notEmpty>
			
			<logic:notEmpty name="importChaptersForm" property="chaptersUpdated">
			<tr><td><digi:trn>Chapters Updated</digi:trn></td>
			<td>
				<bean:write name="importChaptersForm" property="chaptersUpdated"/>
			</td>
			</tr>
			</logic:notEmpty>
			
			<logic:notEmpty name="importChaptersForm" property="imputationsInserted">
			<tr><td><digi:trn>New Imputations Inserted</digi:trn></td>
			<td>
				<bean:write name="importChaptersForm" property="imputationsInserted"/>
			</td>
			</tr>
			</logic:notEmpty>
			
			<logic:notEmpty name="importChaptersForm" property="imputationsUpdated">
			<tr><td><digi:trn>Imputations Updated</digi:trn></td>
			<td>
				<bean:write name="importChaptersForm" property="imputationsUpdated"/>
			</td>
			</tr>
			</logic:notEmpty>
			<logic:notEmpty name="importChaptersForm" property="errorNumber">
			<tr><td><digi:trn>Errors</digi:trn></td>
			<td>
				<bean:write name="importChaptersForm" property="errorNumber"/>
			</td>
			</tr>
			</logic:notEmpty>
			</table>
			</logic:equal>
</digi:form>
					
					</td></tr>
			</table>
		</td>
	</tr>

</table>



