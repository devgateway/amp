<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ page import="org.digijava.module.cms.form.CMSForm"%>

<digi:instance property="cmsContentItemForm" />

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/cms/scripts/cms.js"/>"></script>
<link rel="stylesheet" href="<digi:file src="module/cms/css/cms.css"/>">

<script language="JavaScript">
  function fnOnPreview() {
      <digi:context name="previewItem" property="context/module/moduleinstance/previewContentItem.do" />
      document.cmsContentItemForm.action = "<%= previewItem%>";
	  document.cmsContentItemForm.target = "_self";
      document.cmsContentItemForm.submit();
  }
  function fnOnCreate() {
      <digi:context name="createItem" property="context/module/moduleinstance/createContentItem.do" />
      document.cmsContentItemForm.action = "<%= createItem%>";
	  document.cmsContentItemForm.target = "_self";	  
      document.cmsContentItemForm.submit();
  }
  function fnOnEdit() {
      <digi:context name="editItem" property="context/module/moduleinstance/editContentItem.do" />
      document.cmsContentItemForm.action = "<%= editItem%>";
	  document.cmsContentItemForm.target = "_self";
      document.cmsContentItemForm.submit();
  }
  
  /*
	function showSelectCategory() {
	  openNewWindow(450, 377);
	     <digi:context name="showSelectCategory" property="context/module/moduleinstance/showSelectCategory.do" />
	     document.cmsContentItemForm.action = "<%= showSelectCategory %>?selType=<%= CMSForm.SELECT_CAT_FOR_CONTENT_ITEM %>";
		 document.cmsContentItemForm.target = popupPointer.name;
	     document.cmsContentItemForm.submit();
	} 
	*/
	
	function showSelectCategory() {
	  openNewWindow(450, 377);
	     <digi:context name="showSelectCategory" property="context/module/moduleinstance/addContentItemCategoryFwd.do" />
	     document.cmsContentItemForm.action = "<%= showSelectCategory %>";
		 document.cmsContentItemForm.target = popupPointer.name;
	     document.cmsContentItemForm.submit();
	} 	
	
	
	function removeCategory(id) {
      <digi:context name="removeCategory" property="context/module/moduleinstance/removeContentItemCategory.do" />
      document.cmsContentItemForm.action = "<%= removeCategory%>?parentCategoryId=" + id;
	  document.cmsContentItemForm.target = "_self";
      document.cmsContentItemForm.submit();	
	}
	
	function selectFile() {
	  openNewWindow(300, 150);
	     <digi:context name="showSelectFile" property="context/module/moduleinstance/showSelectFile.do" />
	     document.cmsContentItemForm.action = "<%= showSelectFile %>";
		 document.cmsContentItemForm.target = popupPointer.name;
	     document.cmsContentItemForm.submit();	
	}
</script>
<digi:errors/>

<digi:form action="/previewContentItem.do" enctype="multipart/form-data">
<html:hidden name="cmsContentItemForm" property="processingMode"/>
<html:hidden name="cmsContentItemForm" property="categoryId"/>

<!-- Preview -->
<logic:equal name="cmsContentItemForm" property="preview" value="true">
<logic:present name="cmsContentItemForm" property="itemPreview">
	<bean:define id="itemPreview" name="cmsContentItemForm" property="itemPreview" type="org.digijava.module.cms.form.CMSContentItemForm.CMSContentItemInfo" />

<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
	<td colspan="3" height="1">
		<%-- Table header --%>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td width="1"><digi:img src="module/cms/images/headerLeftEnd.gif" border="0"/></td>
				<td width="99%" class="mainHeader" align="center" valign="middle">
					
				   	<digi:trn key="cms:prevContItem">Preview Content Item</digi:trn>
					
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
				<td class="bold" colspan="5" align="left">
					<h1><digi:trn key="cms:reviewContentItem">Please review the new content item before publishing it:</digi:trn></h>
				</td>
			</tr>
			<tr>
			    <td>&nbsp;</td>
				<td class="dgTitle">
					<font color="hotpink" size="large"><b><bean:write name="itemPreview" property="title" filter="false"/></b></font>
				</td>
			</tr>
			<tr>
				<td valign="top" class="bold" width="15%">
					<digi:trn key="cms:description">Description:</digi:trn>
				</td>
				<td>
					<bean:write name="itemPreview" property="description" filter="false"/>
				</td>
			</tr>
			<tr>
				<td valign="top" class="bold" width="15%">
				<logic:notPresent  name="itemPreview" property="fileName">
					<digi:trn key="cms:url">URL:</digi:trn>
				    <td>
					  <bean:write name="itemPreview" property="url"/>
   				    </td>
				</logic:notPresent>	
				<logic:present name="itemPreview" property="fileName">
					<digi:trn key="cms:fileName">File Name:</digi:trn>
				    <td>
					    <bean:define id="fileName" name="cmsContentItemForm" property="formFile.fileName" />
					    <%
							int index2;
							String extension = null;
							index2 = ((String)fileName).lastIndexOf(".");	
		 					if( index2 >= 0 ) {
							   extension = "module/cms/images/extensions/" + ((String)fileName).substring(index2 + 1,((String)fileName).length()) + ".gif";
						}
					    %>
				        <digi:img skipBody="true" src="<%=extension%>" border="0" align="absmiddle"/>
					<bean:write name="itemPreview" property="fileName"/>
   				    </td>
				</logic:present>	
				</td>
			</tr>
			<tr>
				<td valign="top" class="bold" width="15%">
					<digi:trn key="cms:language">Language:</digi:trn>
				</td>
				<td>
					<logic:present name="itemPreview" property="language">
					<bean:define id="languageKey" name="itemPreview" property="languageKey" type="java.lang.String"/>
					<digi:trn key="<%=languageKey%>"><%=languageKey%></digi:trn></logic:present>
				</td>
			</tr>
			<tr>
				<td valign="top" class="bold" width="15%">
					<digi:trn key="cms:country">Country:</digi:trn>
				</td>
				<td>
					<logic:present name="itemPreview" property="country">
					<bean:define id="countryKey" name="itemPreview" property="countryKey" type="java.lang.String"/>
					<digi:trn key="<%=countryKey%>"><bean:write name="itemPreview" property="countryName" /></digi:trn></logic:present>
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
			<tr>
				<td colspan="2">
				 <logic:equal name="cmsContentItemForm" property="processingMode" value="2">
					 <table width="100%" height="1" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td><digi:img src="module/cms/images/rowItemLeft.gif" height="26" border="0"/></td>
								<td class="rowItem" width="99%" valign="middle" nowrap align="center">
								<input type="button" value="Confirm" onclick="javascript:fnOnCreate()" class="rowItemButton">
							    <digi:context name="goBack" property="context/module/moduleinstance/showCreateContentItem.do?reset=false"/>
							    <html:hidden property="itemId"/>
							    <input type="button" value="Edit" onclick="location.href='<%= goBack%>'" class="rowItemButton">										
								</td>
								<td><digi:img src="module/cms/images/rowItemRight.gif" height="26" border="0"/></td>
							</tr>
						</table>
				 </logic:equal>   
				 <logic:equal name="cmsContentItemForm" property="processingMode" value="1">
					 <table width="100%" height="1" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td><digi:img src="module/cms/images/rowItemLeft.gif" height="26" border="0"/></td>
								<td class="rowItem" width="99%" valign="middle" nowrap align="center">
								<input type="button" value="Update" onclick="javascript:fnOnEdit()" class="rowItemButton">
							    <digi:context name="goBack" property="context/module/moduleinstance/showEditContentItem.do?reset=false"/>
							    <html:hidden property="itemId"/>
							    <input type="button" value="Edit" onclick="location.href='<%= goBack%>'" class="rowItemButton">
								</td>
								<td><digi:img src="module/cms/images/rowItemRight.gif" height="26" border="0"/></td>
							</tr>
						</table>				 
				 </logic:equal>   
				</td>
			</tr>
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
		
		
		</logic:present></logic:equal>

<!-- end preview -->
<logic:equal name="cmsContentItemForm" property="preview" value="false">


<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
	<td colspan="3" height="1">
		<%-- Table header --%>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td width="1"><digi:img src="module/cms/images/headerLeftEnd.gif" border="0"/></td>
				<td width="99%" class="mainHeader" align="center" valign="middle">
					<logic:equal name="cmsContentItemForm" property="processingMode" value="2">
					   	<digi:trn key="cms:createContItem">Create Content Item</digi:trn>
					</logic:equal>   
					<logic:equal name="cmsContentItemForm" property="processingMode" value="1">
						<digi:trn key="cms:editContItem">Edit Content Item</digi:trn>
					</logic:equal>
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
		
		
		
		
		
		

		<table width="100%" border="0" cellspacing="0" cellpadding="3">
			<tr>
				<td nowrap>
					<digi:trn key="cms:title">Title</digi:trn>
				</td>
				<td>
					<html:text name="cmsContentItemForm" property="title" size="70" styleClass="rowItemField"/>
				</td>		
			</tr>
			<tr>
				<td nowrap align="top">
					<digi:trn key="cms:description">Description</digi:trn>
				</td>
				<td>
					<html:textarea name="cmsContentItemForm" property="description" cols="30" rows="5" />
				</td>
			</tr>
			<tr>
				<td nowrap>
					<digi:trn key="cms:url">URL</digi:trn>
				</td>
				<td nowrap>
					<table width="100%" height="1" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td><digi:img src="module/cms/images/rowItemLeft.gif" height="26" border="0"/></td>
							<td class="rowItem" valign="middle" nowrap>
								<html:text name="cmsContentItemForm" property="url" size="30" styleClass="rowItemField"/> <digi:trn key="cms:or">OR</digi:trn> 
							</td>
							<td class="rowItem"><digi:img src="module/cms/images/tree/spacer.gif" width="5" border="0"/></td>
							<td class="rowItem" valign="middle" nowrap>
								<input type="Button" onclick="selectFile()" value="Select file" class="rowItemButton">
							</td>
							<td class="rowItem"><digi:img src="module/cms/images/tree/spacer.gif" width="5" border="0"/></td>
							<td class="rowItem" width="100%" valign="middle" nowrap align="right">
								<logic:present name="cmsContentItemForm" property="formFile">
									    <bean:define id="fileName" name="cmsContentItemForm" property="formFile.fileName" />
									    <%
											int index2;
											String extension = null;
											index2 = ((String)fileName).lastIndexOf(".");	
											if( index2 >= 0 ) {
											   extension = "module/cms/images/extensions/" + ((String)fileName).substring(index2 + 1,((String)fileName).length()) + ".gif";
											}
									    %>
									    <digi:img skipBody="true" src="<%=extension%>" border="0" align="absmiddle"/>					
									<bean:write name="cmsContentItemForm" property="formFile.fileName"/>
								</logic:present>
								<logic:notPresent name="cmsContentItemForm" property="formFile">
									<digi:trn key="cms:fileNotSelected">File not selected</digi:trn>
								</logic:notPresent>								
							</td>
							<td><digi:img src="module/cms/images/rowItemRight.gif" height="26" border="0"/></td>
						</tr>
					</table>					
				</td>
			</tr>
			<tr>
				<td nowrap>
					<digi:trn key="cms:language">Language</digi:trn>
				</td>
				<td>
					<logic:present name="cmsContentItemForm" property="languages">
					<html:select property="language">
					<bean:define id="lid" name="cmsContentItemForm" property="languages" type="java.util.List"/>
					<html:options collection="lid" property="code" labelProperty="name"/></html:select>
				    </logic:present>			
				</td>		
			</tr>
			<tr>
				<td nowrap>
					<digi:trn key="cms:country">Country</digi:trn>
				</td>
				<td>
		   		    <logic:present name="cmsContentItemForm" property="countries">
						<html:select property="country">
						<bean:define id="countries" name="cmsContentItemForm" property="countries" type="java.util.List"/>
						<html:options collection="countries" property="iso" labelProperty="name"/></html:select>
				    </logic:present>
				</td>		
			</tr>
			
			<tr>
				<td valign="top" width="10%" nowrap>
					<digi:trn key="cms:relatedCategories">Related categories</digi:trn>
				</td>
				<td class="groupContainer" width="90%">
					<table width="100%" cellpadding="3" cellspacing="0" border="0">
						<tr>
							<td colspan="2">
								<table width="100%" height="1" cellpadding="0" cellspacing="0" border="0">
									<tr>
										<td><digi:img src="module/cms/images/rowItemLeft.gif" height="26" border="0"/></td>
										<td class="rowItem" width="99%" valign="middle" nowrap align="left">
											<input type="Button" value="Add category" onclick="showSelectCategory()" class="rowItemButton">
										</td>
										<td><digi:img src="module/cms/images/rowItemRight.gif" height="26" border="0"/></td>
									</tr>
								</table>
							</td>
						</tr>
						<logic:present name="cmsContentItemForm" property="categoryIdList">
							<logic:iterate indexId="index" name="cmsContentItemForm" property="categoryIdList" id="categoryId" type="String">
								<tr>
									<td width="100%">
										<digi:link href="/browseCategories.do" contextPath="context/module/moduleinstance" paramName="categoryId" paramId="categoryId" >
										<bean:write name="cmsContentItemForm" property="<%= "categoryNameList[" + index +  "]" %>"/></digi:link>
									</td>
									<td width="1">
										<a href="javascript:removeCategory(<bean:write name="cmsContentItemForm" property="<%= "categoryIdList[" + index +  "]" %>"/>)">
										<digi:img src="module/cms/images/removeIcon.gif" border="0"/>
										</a>
									</td>									
								</tr>
							</logic:iterate>
						</logic:present>
					</table>
				</td>
			</tr>	
			
			<tr>
				<td colspan="2" align="center">
				
				
					<table width="100%" height="1" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td><digi:img src="module/cms/images/rowItemLeft.gif" height="26" border="0"/></td>
							<td class="rowItem" width="99%" valign="middle" nowrap align="center">
								<input type="Button" value="Preview" onclick="javascript:fnOnPreview()" class="rowItemButton">					</td>
							<td><digi:img src="module/cms/images/rowItemRight.gif" height="26" border="0"/></td>
						</tr>
					</table>
				
				</td>		
			</tr>	
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
</logic:equal>
</digi:form>