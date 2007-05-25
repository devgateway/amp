<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ page import="org.digijava.module.cms.form.CMSForm"%>

<digi:instance property="cmsForm"/>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/cms/scripts/parser.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/cms/scripts/tree.js"/>"></script>
<link rel="stylesheet" href="<digi:file src="module/cms/css/tree.css"/>">
<link rel="stylesheet" href="<digi:file src="module/cms/css/cms.css"/>">

<digi:form action="/showSelectCategory.do">
<html:hidden name="cmsForm" property="parentCategoryId" value="0"/>
<html:hidden name="cmsForm" property="categoryId"/>
<html:hidden name="cmsForm" property="categoryTreeXml"/>
<html:hidden name="cmsForm" property="primaryParentCategoryId"/>
<html:hidden name="cmsForm" property="processingMode"/>


<html:hidden name="cmsForm" property="categoryName"/>
<html:hidden name="cmsForm" property="categoryScopeNote"/>



<logic:present name="cmsForm" property="propertyArray">
	<logic:iterate indexId="index" name="cmsForm" property="propertyArray" id="parentId" type="String">
		<html:hidden name="cmsForm" property="<%= "propertyArray[" + index +  "]" %>"/>
		<html:hidden name="cmsForm" property="<%= "nameArray[" + index +  "]" %>"/>
	</logic:iterate>
</logic:present>

<logic:present name="cmsForm" property="relatedIdArray">
	<logic:iterate indexId="index" name="cmsForm" property="relatedIdArray" id="relatedId" type="String">
				<html:hidden name="cmsForm" property="<%= "relatedIdArray[" + index +  "]" %>"/>
				<html:hidden name="cmsForm" property="<%= "relatedNameArray[" + index +  "]" %>"/>									
	</logic:iterate>
</logic:present>

<digi:errors/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
	<td colspan="3" height="1">
		<%-- Table header --%>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td width="1"><digi:img src="module/cms/images/headerLeftEnd.gif" border="0"/></td>
				<td width="99%" class="mainHeader" align="center" valign="middle">
					<digi:trn key="cms:selectCategory">Select category</digi:trn>
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
			<table border="0" cellpadding="3" cellspacing="0" width="98%">
				<tr>
					<td>
						<div id="treeContainer" style="width:100%; height:300px; border:1px solid #858585; overflow: auto; background-color: White;">tree</div>
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" height="1" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td><digi:img src="module/cms/images/rowItemLeft.gif" height="26" border="0"/></td>
								<td class="rowItem"><digi:img src="module/cms/images/tree/spacer.gif" width="5" border="0"/></td>
								<td class="rowItem" valign="middle" nowrap align="left" width="99%">
									<input name="toolbarItem" type="button" value="Select" class="rowItemButton" disabled onclick="selectCategory()">
									<input type="button" value="Close" class="rowItemButton" onclick="window.close()">
								</td>
								<td class="rowItem" width="50%" valign="middle" nowrap align="right">
									<input type="text" id="searchString" class="rowItemField" size="20">
									<input type="button" value="Find"class="rowItemButton" onclick="searchNode()">
								</td>
								<td class="rowItem"><digi:img src="module/cms/images/tree/spacer.gif" width="5" border="0"/></td>
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
</digi:form>

<script language="JavaScript">

	function searchNode() {
		val = document.getElementById("searchString").value;
		nodeId = searchTree("name", val, skipCount);
		if (nodeId != null) {
			skipCount ++;
			expandByRealId (nodeId);
			setSelectedByRealId (nodeId);
		} else {
			skipCount = 0;
			setSelectedById (1);
			alert ("Search finished");
		}
	}


	function selectCategory() {
		<logic:equal name="cmsForm" property="selType" value="<%= String.valueOf(CMSForm.SELECT_CAT_FOR_PARENT) %>">
		  <digi:context name="addParentCategory" property="context/module/moduleinstance/addParentCategory.do" />
	      document.cmsForm.action = "<%= addParentCategory %>";
		  document.cmsForm.target = window.opener.name;
	      document.cmsForm.submit();
		  window.close();
		 </logic:equal>
		<logic:equal name="cmsForm" property="selType" value="<%= String.valueOf(CMSForm.SELECT_CAT_FOR_RELATED) %>">
		  <digi:context name="addRelatedCategory" property="context/module/moduleinstance/addRelatedCategory.do" />
	      document.cmsForm.action = "<%= addRelatedCategory %>";
		  document.cmsForm.target = window.opener.name;
	      document.cmsForm.submit();
		  window.close();
		 </logic:equal>
		<logic:equal name="cmsForm" property="selType" value="<%= String.valueOf(CMSForm.SELECT_CAT_FOR_CONTENT_ITEM) %>">
		  <digi:context name="addContentItemCategory" property="context/module/moduleinstance/addContentItemCategory.do" />
	      document.cmsForm.action = "<%= addContentItemCategory %>";
		  document.cmsForm.target = window.opener.name;
	      document.cmsForm.submit();
		  window.close();
		 </logic:equal>		 
	}	

	function userSelect (selNode){
		if (selNode.getNamedAttribute("id") != null) {
			document.getElementsByName("parentCategoryId")[0].value = selNode.getNamedAttribute("id").value;
			enableToolbarItems (true);
		} else {
			document.getElementsByName("parentCategoryId")[0].value = 0;
			enableToolbarItems (false);
		}
	}	
	
	function enableToolbarItems(enable){
		items = document.getElementsByName("toolbarItem");
		if (items != null && items.length > 0) {
			for (itIndex = 0; itIndex < items.length; itIndex++){
				items[itIndex].disabled = !enable;
			}
		}
	}

	xmlDoc = new XMLDocument (document.getElementsByName("categoryTreeXml")[0].value);
	xmlDoc.docRules.addRule(new XMLParserRuleItem(1, "primary"));
	xmlDoc.docRules.addRule(new XMLParserRuleItem(2, "top-level"));	
	xmlDoc.docRules.addRule(new XMLParserRuleItem(3, "copy"));
	xmlDoc.docRules.addRule(new XMLParserRuleItem(4, "top-copy"));	
	xmlDoc.parse();

	var categoryType = new Type(1, "<digi:file src="module/cms/images/tree/icons/categoryIcon.gif"/>");
	categoryType.addNameExtra("name");
	categoryType.skipName=true;
	categoryType.nameExtraSeparator = "";
	categoryType.useExtraDifferentStyle = false;
	addType(categoryType);	

	var topLevelType = new Type(2, "<digi:file src="module/cms/images/tree/icons/topLevelIcon.gif"/>");
	topLevelType.addNameExtra("name");
	topLevelType.skipName=true;
	topLevelType.nameExtraSeparator = "";
	topLevelType.useExtraDifferentStyle = false;
	addType(topLevelType);
	
	var categoryCopyType = new Type(3, "<digi:file src="module/cms/images/tree/icons/categoryCopyIcon.gif"/>");
	categoryCopyType.addNameExtra("name");
	categoryCopyType.skipName=true;
	categoryCopyType.nameExtraSeparator = "";
	categoryCopyType.useExtraDifferentStyle = false;
	addType(categoryCopyType);
	
	var topCopyType = new Type(4, "<digi:file src="module/cms/images/tree/icons/topLevelCopyIcon.gif"/>");
	topCopyType.addNameExtra("name");
	topCopyType.skipName=true;
	topCopyType.nameExtraSeparator = "";
	topCopyType.useExtraDifferentStyle = false;
	addType(topCopyType);	
	
	setCollapseIconSrc ("<digi:file src="module/cms/images/tree/collapse.gif"/>");
	setExpandIconSrc ("<digi:file src="module/cms/images/tree/expand.gif"/>");
	setSpacerSrc ("<digi:file src="module/cms/images/tree/spacer.gif"/>");
	setRootIconSrc ("<digi:file src="module/cms/images/tree/icons/rootIcon.gif"/>");
	setCommentIconSrc ("<digi:file src="module/cms/images/tree/icons/comment.gif"/>");
	setDefaultIconSrc ("<digi:file src="module/cms/images/tree/icons/default.gif"/>");	
	
	buldTree(xmlDoc, document.getElementById("treeContainer"));
</script>