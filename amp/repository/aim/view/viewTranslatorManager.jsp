<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ page import="java.util.Map"%>
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script langauage="JavaScript">
	function enableChkBox(chkBox) {
		
		alert(chkBox);
			 
	}
</script>

<digi:instance property="aimTranslatorManagerForm" />
<digi:context name="digiContext" property="context" />
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->


<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%" border=0>
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</bean:define>
						<digi:link href="/admin.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:translationManager">
						Translation Manager
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>Translation Manager</span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<html:errors />
					</td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1 border=0>
					<logic:empty name="aimTranslatorManagerForm" property="importedLanguages">
					<logic:notEmpty name="aimTranslatorManagerForm" property="languages">
					<digi:form action="/translationManager.do" method="post" >
							<tr>
								<td>
									The following languages where found on this site:
								</td>
							</tr>
						<logic:iterate name="aimTranslatorManagerForm" property="languages" id="lang"
																	type="java.lang.String">
							<tr>
								<td>
									<html:checkbox property="selectedLanguages" value="<%=lang %>"/>
									<bean:write name="lang" /> <br/>
 								</td>
	 						</tr>
						 </logic:iterate>
							 <tr>
							 	<td><html:submit style="dr-menu" value="Export" property="export"/></td>
							 </tr>
							 <td>
									<br/>Please select the languages you want to export
							</td>
					 </digi:form>
					</logic:notEmpty>
					
					<tr>
						<td><br/><br/><br/>
							</td>
					</tr>
						 
					<digi:form action="/translationManager.do" method="post" enctype="multipart/form-data">
						<tr>
							<td>
								<html:file property="fileUploaded"></html:file>
							</td>
						</tr>
							 <tr>
							 	<td><html:submit style="dr-menu" value="Import" property="import"/></td>
							 </tr>
					</digi:form>
					
					</logic:empty>
					<logic:notEmpty name="aimTranslatorManagerForm" property="importedLanguages">
						<digi:form action="/translationManager.do" method="post" >
							<tr>
								<td colspan="2">
									The following languages where found in the file you imported:<br/>
								</td>
							</tr>
							<logic:iterate name="aimTranslatorManagerForm" property="importedLanguages" id="lang"
																	type="java.lang.String">
								<tr>
									<td width="30%">
										
										<html:hidden property="selectedImportedLanguages" value="<%=lang %>" />
										<bean:write name="lang" /> 
										</td>
										<td>
										<select name='<%="LANG:"+lang%>' >
											<option value="-1" selected>-- Please select --</option>
											<option value="update">Update local translations</option>											
											<option value="overwrite">Overwrite local translations</option>											
										</select>
	 								</td>
		 						</tr>
							 </logic:iterate>
								 <tr>
								 	<td colspan="2"><br/><html:submit style="dr-menu" value="Import" property="importLang"/></td>
								 </tr>
								 <tr>
								<td colspan="2">
									<br/>Please select the languages you want to update or to insert
								</td>
							</tr>
						 </digi:form>
					</logic:notEmpty>
					</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

