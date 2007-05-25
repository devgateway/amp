<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ page import="org.digijava.module.syndication.form.CollectorFeedForm"%>

<digi:instance property="collectorFeedForm" />

<link rel="stylesheet" href="<digi:file src="module/syndication/css/cms.css"/>">

<script language="JavaScript">
  function fnOnPreview() {
      <digi:context name="previewItem" property="context/module/moduleinstance/confirmNewFeed.do" />
      document.collectorFeedForm.action = "<%= previewItem%>";
	  document.collectorFeedForm.target = "_self";
      document.collectorFeedForm.submit();
  }
</script>

<digi:errors/>

<digi:form action="/confirmNewFeed.do">
<html:hidden name="collectorFeedForm" property="processingMode"/>
<html:hidden name="collectorFeedForm" property="itemId"/>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
	<td colspan="3" height="1">
		<%-- Table header --%>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td width="1"><digi:img src="module/syndication/images/headerLeftEnd.gif" border="0"/></td>
				<td width="99%" class="mainHeader" align="center" valign="middle">
					<logic:equal name="collectorFeedForm" property="processingMode" value="1">
					   	<digi:trn key="syndication:createNewFeed">Create New Feed</digi:trn>
					</logic:equal>   
					<logic:equal name="collectorFeedForm" property="processingMode" value="2">
					   	<digi:trn key="syndication:editFeed">Edit Feed</digi:trn>
					</logic:equal>
				</td>
				<td width="1"><digi:img src="module/syndication/images/headerRightEnd.gif" border="0"/></td>				
			</tr>
		</table>
		<%-- end of Table header --%>
	</td>
</tr>
<tr>
	<td class="leftBorder" width="1">
		<digi:img src="module/syndication/images/tree/spacer.gif" width="9" border="0"/>
	</td>
	<td width="100%" align="center" bgcolor="#F2F4FC">
		<%-- Inner --%>
		
		

		<table width="100%" border="0" cellspacing="0" cellpadding="3">
			<tr>
				<td nowrap>
					<digi:trn key="syndication:feedTitle">Feed Title</digi:trn>
				</td>
				<td>
				<logic:equal name="collectorFeedForm" property="processingMode" value="2">
					<html:text name="collectorFeedForm" property="feedTitle" size="70" styleClass="rowItemField" disabled="true" />
					<html:hidden name="collectorFeedForm" property="feedTitle"/>
				</logic:equal>				
				<logic:equal name="collectorFeedForm" property="processingMode" value="1">
					<html:text name="collectorFeedForm" property="feedTitle" size="70" styleClass="rowItemField" />
				</logic:equal>				
				</td>		
			</tr>
			<tr>
				<td nowrap align="top">
					<digi:trn key="syndication:feedDescription">Feed Description</digi:trn>
				</td>
				<td>
				<logic:equal name="collectorFeedForm" property="processingMode" value="2">
					<html:textarea name="collectorFeedForm" property="feedDescription" cols="30" rows="5" disabled="true" />
					<html:hidden name="collectorFeedForm" property="feedDescription"/>
				</logic:equal>
				<logic:equal name="collectorFeedForm" property="processingMode" value="1">
					<html:textarea name="collectorFeedForm" property="feedDescription" cols="30" rows="5" />
				</logic:equal>
				</td>
			</tr>
			<tr>
				<td nowrap>
					<digi:trn key="syndication:feedUrl">Feed URL</digi:trn>
				</td>
				<td>
				<logic:equal name="collectorFeedForm" property="processingMode" value="2">
					<html:text name="collectorFeedForm" property="feedUrl" size="70" styleClass="rowItemField" disabled="true" />
					<html:hidden name="collectorFeedForm" property="feedUrl"/>					
				</logic:equal>
				<logic:equal name="collectorFeedForm" property="processingMode" value="1">
					<html:text name="collectorFeedForm" property="feedUrl" size="70" styleClass="rowItemField" />
				</logic:equal>
				</td>				
			</tr>
			<tr>
				<td nowrap>
					<digi:trn key="syndication:sourceName">Source Name</digi:trn>
				</td>
				<td>
				<logic:equal name="collectorFeedForm" property="processingMode" value="2">
					<html:text name="collectorFeedForm" property="sourceName" size="70" styleClass="rowItemField" disabled="true" />
					<html:hidden name="collectorFeedForm" property="sourceName"/>					
				</logic:equal>
				<logic:equal name="collectorFeedForm" property="processingMode" value="1">
					<html:text name="collectorFeedForm" property="sourceName" size="70" styleClass="rowItemField" />
				</logic:equal>
				</td>				
			</tr>
			<tr>
				<td nowrap>
					<digi:trn key="syndication:sourceUrl">Source URL</digi:trn>
				</td>
				<td>
				<logic:equal name="collectorFeedForm" property="processingMode" value="2">
					<html:text name="collectorFeedForm" property="sourceUrl" size="70" styleClass="rowItemField" disabled="true" />
					<html:hidden name="collectorFeedForm" property="sourceUrl"/>					
				</logic:equal>
				<logic:equal name="collectorFeedForm" property="processingMode" value="1">
					<html:text name="collectorFeedForm" property="sourceUrl" size="70" styleClass="rowItemField" />
				</logic:equal>
				</td>				
			</tr>
			<tr>
				<td nowrap>
					<digi:trn key="syndication:schedule">Schedule</digi:trn>
				</td>
				<td>
					<html:select property="selectedSchedule">
					<bean:define id="sid" name="collectorFeedForm" property="schedules" type="java.util.Collection"/>
					<html:options collection="sid" property="scheduleId" labelProperty="scheduleText"/></html:select>
					&nbsp;
					<html:select property="selectedTime">
					<bean:define id="tid" name="collectorFeedForm" property="scheduleTimes" type="java.util.Collection"/>
					<html:options collection="tid" property="scheduleId" labelProperty="scheduleText"/></html:select>
				</td>		
			</tr>
			<tr>
				<td nowrap>
					<digi:trn key="syndication:status">Status</digi:trn>
				</td>
				<td>
						<table border="0" cellpadding="2" cellspacing="1">
							<tr>
								<td width="51">
									<html:radio name="collectorFeedForm" property="status" value="true"/>
									<digi:trn key="syndication:enabled">Enabled</digi:trn>
								</td>
								<td width="44">
									<html:radio name="collectorFeedForm" property="status" value="false"/>
									<digi:trn key="syndication:disabled">Disabled</digi:trn>
								</td>
							</tr>
						</table>				
				</td>		
			</tr>

			
			<tr>
				<td colspan="2" align="center">
				
				
					<table width="100%" height="1" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td><digi:img src="module/syndication/images/rowItemLeft.gif" height="26" border="0"/></td>
							<td class="rowItem" width="99%" valign="middle" nowrap align="center">
							<input type="Button" value="Preview" onclick="javascript:fnOnPreview()" class="rowItemButton">					</td>
							<td><digi:img src="module/syndication/images/rowItemRight.gif" height="26" border="0"/></td>
						</tr>
					</table>
				
				</td>		
			</tr>	
		</table>







<%-- Inner --%>
	</td>
	<td class="rightBorder" width="1">
		<digi:img src="module/syndication/images/tree/spacer.gif" width="9" border="0"/>
	</td>	
</tr>
<tr>
	<td height="1"><digi:img src="module/syndication/images/leftBottom.gif" width="9" height="9" border="0"/></td>
	<td width="100%" class="bottomBorder" height="1"><digi:img src="module/syndication/images/tree/spacer.gif" height="9" border="0"/></td>
	<td height="1"><digi:img src="module/syndication/images/rightBottom.gif" width="9" height="9" border="0"/></td>	
</tr>
</table>
</digi:form>