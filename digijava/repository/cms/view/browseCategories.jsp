
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<digi:errors/>
<digi:instance property="cmsForm" />

<link rel="stylesheet" href="<digi:file src="module/cms/css/cms.css"/>">

<logic:present name="cmsForm" property="categoryList">

<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
	<td colspan="3" height="1">
		<%-- Table header --%>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td width="1"><digi:img src="module/cms/images/headerLeftEnd.gif" border="0"/></td>
				<td width="99%" class="mainHeader" align="center" valign="middle">
					<digi:trn key="cms:browseCategories">BROWSE CATEGORIES</digi:trn>
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
	<td width="100%" valign="top" align="center" bgcolor="#F2F4FC">
		<%-- Inner --%>



<table width="97%" border="0" cellpadding="5" cellspacing="0">
<bean:define id="catList" name="cmsForm" property="categoryList" type="java.util.List"/>
<bean:define id="listSize" value="<%= String.valueOf(catList.size()-1) %>"/>


<logic:iterate indexId="index" id="categoryList" name="catList" type="org.digijava.module.cms.dbentity.CMSCategory">

<bean:define id="oddOrEven" value="<%= String.valueOf(index.intValue()%2) %>"/>
<bean:define id="strIndex" value="<%= String.valueOf(index.intValue()) %>"/>

<logic:equal name="oddOrEven" value="0">
<tr>
</logic:equal>
<td valign="top" width="50%">
	<table border="0" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td width="1" nowrap>&#9675;&nbsp;</td>
			<td>
	      	  <digi:link styleClass="cmsCategory" href="/browseCategories.do" paramName="categoryList" paramId="categoryId" paramProperty="id"><bean:write name="categoryList" property="name" /></digi:link>
			</td>
		</tr>  
		<tr>
			<td width="1">&nbsp;</td>
			<td>
			  <logic:present name="categoryList" property="subCategories">
				<logic:iterate indexId="index" id="subCategoryList" name="categoryList" property="subCategories" type="org.digijava.module.cms.dbentity.CMSCategory">
				     <digi:link styleClass="cmsSubcategory" href="/browseCategories.do" paramName="subCategoryList"  paramId="categoryId" paramProperty="id"><bean:write name="subCategoryList" property="name" /></digi:link>,
			    </logic:iterate>
			  </logic:present>	
		  ...
			</td>
		</tr>
	</table>	
</td>
<logic:equal name="oddOrEven" value="1">
</tr>
</logic:equal>
<logic:equal name="oddOrEven" value="0">
	<logic:equal name="strIndex" value="<%= listSize %>">
			<td width="50%">&nbsp;</td>
		</tr>
	</logic:equal>
</logic:equal>

</logic:iterate>
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
</logic:present>

<table border="0" cellpadding="3" cellspacing="0">
	<tr>
		<digi:secure authenticated="true">
		<td nowrap>
			<digi:link href="/viewMyContentItems.do"><digi:trn key="cms:viewMyItems">View my items</digi:trn></digi:link>
		</td>
		</digi:secure>
		<digi:secure actions="ADMIN">
		<td nowrap>
				<digi:link href="/viewContentItems.do"><digi:trn key="cms:administer">Administer</digi:trn></digi:link>
		</td>
		</digi:secure>
	</tr>
	<tr>
		<digi:secure authenticated="true">
		<td nowrap>
				<digi:link href="/viewMyContentItems.do?viewMode=pending"><digi:trn key="cms:viewMyPendingItems">View my pending items</digi:trn></digi:link>
		</td>
		</digi:secure>
		<digi:secure actions="ADMIN">
		<td nowrap>
				<digi:link href="/showCMSSettings.do"><digi:trn key="cms:showCMSSettings">Settings</digi:trn></digi:link>
		</td>
		</digi:secure>
	</tr>	
	<tr>
		<digi:secure authenticated="true">
		<td nowrap>
				<digi:link href="/showCreateContentItem.do"><digi:trn key="cms:addUrlOrFile">Add URL or File</digi:trn></digi:link>
		</td>
		</digi:secure>
		<digi:secure actions="ADMIN">
		<td>
				<digi:link href="/showTaxonomyManager.do"><digi:trn key="cms:taxonomyManager">Taxonomy manager</digi:trn></digi:link>
		</td>
		</digi:secure>
	</tr>		
</table>