<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<link rel="stylesheet" href="<digi:file src="module/cms/css/cms.css"/>">

<digi:errors/>
<digi:form action="/showEditContentItem.do">
<html:hidden name="cmsContentItemForm" property="itemId"/>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
	<td colspan="3" height="1">
		<%-- Table header --%>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td width="1"><digi:img src="module/cms/images/headerLeftEnd.gif" border="0"/></td>
				<td width="99%" class="mainHeader" align="center" valign="middle">
				   	<digi:trn key="cms:contItemDetails">Content Item Details</digi:trn>					
				</td>
				<td width="1"><digi:img src="module/cms/images/headerRightEnd.gif" border="0"/></td>				
			</tr>
		</table>
		<%-- end of Table header --%>
	</td>
</tr>
<tr>
	<td class="leftBorder" width="1">
		<digi:img src="module/cms/images/tree/spacer.gif" width="9" border="0"/>
	</td>
	<td width="100%" align="center" bgcolor="#F2F4FC">
		<%-- Inner --%>	
	
	
		<table border="0" width="100%">
			<tr>
				<td valign="top" class="bold" width="15%">
			      <digi:trn key="cms:title">Title:</digi:trn>
			    </td>
				<td class="dgTitle">
					<font color="hotpink" size="large"><b><bean:write name="cmsContentItemForm" property="title" filter="false"/></b></font>
				</td>
			</tr>
			<tr>
				<td valign="top" class="bold" width="15%">
					<digi:trn key="cms:description">Description:</digi:trn>
				</td>
				<td>
					<bean:write name="cmsContentItemForm" property="description" filter="false"/>
				</td>
			</tr>
			<tr>
				<td valign="top" class="bold" width="15%">
				<logic:notPresent  name="cmsContentItemForm" property="fileName">
					<digi:trn key="cms:url">URL:</digi:trn>
				    <td>
					  <a href='<bean:write name="cmsContentItemForm" property="url" />' ><bean:write name="cmsContentItemForm" property="url" /></a>
   				    </td>
				</logic:notPresent>	
				<logic:present name="cmsContentItemForm" property="fileName">
					<digi:trn key="cms:fileName">File Name:</digi:trn>
				  <td>
					    <bean:define id="fileName" name="cmsContentItemForm" property="fileName" />
					    <%
						
						int index;
						String extension = null;

						index = ((String)fileName).lastIndexOf(".");	
						if( index >= 0 ) {
						   extension = "module/cms/images/extensions/" + ((String)fileName).substring(index + 1,((String)fileName).length()) + ".gif";
						}
					    %>
			 	          <digi:img skipBody="true" src="<%=extension%>" border="0" align="absmiddle"/>
		    		          <digi:link href="/downloadFile.do" paramName="cmsContentItemForm" paramId="itemId" paramProperty="itemId"><bean:write name="cmsContentItemForm" property="fileName" /></digi:link>
   				    </td>
				</logic:present>	
				</td>
			</tr>
			<tr>
				<td valign="top" class="bold" width="15%">
					<digi:trn key="cms:language">Language:</digi:trn>
				</td>
				<td>
					<logic:present name="cmsContentItemForm" property="language">
					<bean:define id="languageKey" name="cmsContentItemForm" property="languageKey" type="java.lang.String"/>
					<digi:trn key="<%=languageKey%>"><%=languageKey%></digi:trn></logic:present>
				</td>
			</tr>
			<tr>
				<td valign="top" class="bold" width="15%">
					<digi:trn key="cms:country">Country:</digi:trn>
				</td>
				<td>
					<logic:present name="cmsContentItemForm" property="country">
					<bean:define id="countryKey" name="cmsContentItemForm" property="countryKey" type="java.lang.String"/>
					<digi:trn key="<%=countryKey%>"><bean:write name="cmsContentItemForm" property="countryName" /></digi:trn></logic:present>
				</td>
			</tr>
			<tr>
				<td valign="top" class="bold" width="15%">
					<digi:trn key="cms:Category">Category:</digi:trn>
				</td>

				<td class="groupContainer" width="85%" align="left">
					<table width="100%" cellpadding="3" cellspacing="0" border="0" width="90%">
						<logic:present name="cmsContentItemForm" property="categoryIdList">
							<logic:iterate indexId="index" name="cmsContentItemForm" property="categoryIdList" id="categoryId" type="String">
								<tr>
									<td width="100%">
										<digi:link href="/browseCategories.do" contextPath="context/module/moduleinstance" paramName="categoryId" paramId="categoryId" >
										<bean:write name="cmsContentItemForm" property="<%= "categoryNameList[" + index +  "]" %>"/></digi:link>
									</td>
								</tr>
							</logic:iterate>
						</logic:present>
					</table>
				</td>				

		
			</tr>
			<digi:secure actions="ADMIN">
			<tr>
			  <td>
			 	&nbsp;
			  </td>
			  <td>
			     <html:submit value="Edit" />
			  </td>
			</tr>
			</digi:secure>
		</table>
		
		
		
<%-- Inner --%>
	</td>
	<td class="rightBorder" width="1">
		<digi:img src="module/cms/images/tree/spacer.gif" width="9" border="0"/>
	</td>	
</tr>
<tr>
	<td height="1"><digi:img src="module/cms/images/leftBottom.gif" width="9" height="9" border="0"/></td>
	<td width="100%" class="bottomBorder" height="1"><digi:img src="module/cms/images/tree/spacer.gif" height="9" border="0"/></td>
	<td height="1"><digi:img src="module/cms/images/rightBottom.gif" width="9" height="9" border="0"/></td>	
</tr>
</table>		
</digi:form>