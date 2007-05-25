<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ page import="org.digijava.module.common.dbentity.ItemStatus" %>
<digi:errors/>
<digi:instance property="cmsForm" />
<link rel="stylesheet" href="<digi:file src="module/cms/css/cms.css"/>">

<bean:define id="catId" name="cmsForm" property="categoryId"/>
<bean:define id="itemsPerPage" name="cmsForm" property="itemsPerPage" type="Integer"/>

<logic:present parameter="nav" >
<bean:parameter id="nav" name="nav" />
</logic:present>

<script language="JavaScript">
	function next() {
	  <digi:context name="next" property="context/module/moduleinstance/browseCategories.do" />
      document.cmsForm.action = "<%= next %>?categoryId=<bean:write name="cmsForm" property="categoryId"/>&nav=<bean:write name="cmsForm" property="next" />" + getDecorationParam(false);
      document.cmsForm.submit();	
	}
	function prev() {
	  <digi:context name="prev" property="context/module/moduleinstance/browseCategories.do" />
      document.cmsForm.action = "<%= prev %>?categoryId=<bean:write name="cmsForm" property="categoryId"/>&nav=<bean:write name="cmsForm" property="prev"/>" + getDecorationParam(false);
      document.cmsForm.submit();	
	}
	function changeItemPerPage() {
	  <digi:context name="changeItemPerPage" property="context/module/moduleinstance/browseCategories.do" />
          document.cmsForm.action = "<%= changeItemPerPage %>?categoryId=<bean:write name="cmsForm" property="categoryId"/>" + getDecorationParam(false) + getNavParam();
      document.cmsForm.submit();	

	}	
	function decorationSwitch() {
		<digi:context name="displayOnlyTitle" property="context/module/moduleinstance/browseCategories.do" />
		document.cmsForm.action = '<%=displayOnlyTitle%>?categoryId=<%=request.getParameter("categoryId")%>' + getDecorationParam(true) + getNavParam();
	        document.cmsForm.submit();	
	}

	function getDecorationParam(reverse) {
	  var param = "";
		if( !reverse ) {
		<logic:equal parameter="decoration" value="title" >
		      param = "&decoration=title"
		</logic:equal>
		} else {
		<logic:notEqual parameter="decoration" value="title" >
		      param = "&decoration=title";
		</logic:notEqual>
		}
	  return param;

	}

	function getNavParam() {
	  var param = "";
		<logic:greaterThan name="nav" value="0">
		      param = "&nav=<bean:write name="nav" />";
		</logic:greaterThan>
	  return param;

	}
	
	function publishItem (itemId){
		<digi:context name="publishItem" property="context/module/moduleinstance/changeItemStatus.do" />
		document.cmsForm.action = '<%=publishItem%>?itemId=' + itemId + "&itemStatus=<%= ItemStatus.PUBLISHED %>";
	    document.cmsForm.submit();	
	}

	function rejectItem (itemId){
		<digi:context name="rejectItem" property="context/module/moduleinstance/changeItemStatus.do" />
		document.cmsForm.action = '<%=rejectItem%>?itemId=' + itemId + "&itemStatus=<%= ItemStatus.REJECTED %>";
	    document.cmsForm.submit();	
	}	

</script>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
	<td colspan="3" height="1">
		<%-- Table header --%>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td width="1"><digi:img src="module/cms/images/headerLeftEnd.gif" border="0"/></td>
				<td width="99%" class="mainHeader" align="center" valign="middle">
					<bean:write name="cmsForm" property="categoryName" />&nbsp;<digi:trn key="cms:contBrowserPage">content browser page</digi:trn>
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
	<td width="100%" align="center" valign="top" bgcolor="#F2F4FC">
		<%-- Inner --%>
		
<digi:form action="/browseCategories.do">
<html:hidden name="cmsForm" property="currentOffset"/>
<html:hidden name="cmsForm" property="refPageUrl"/>

<br>
<logic:present name="cmsForm" property="bradcrampItems">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td nowrap class="bradcrampCell" valign="middle">
			&nbsp;
			<digi:link href="/browseCategories.do" styleClass="cmsSubcategory">
				<digi:trn key="cms:top">Top</digi:trn>
			</digi:link>
		</td>
		<td valign="middle" class="bradcrampCell">
			<digi:img src="module/cms/images/bradcrampArrow.gif" width="9" height="17" border="0"/>
		</td>	
		<logic:iterate indexId="brIndex" name="cmsForm" property="bradcrampItems" id="bradcrampItem" type="org.digijava.module.cms.util.CMSBradcrampItem">
				<td nowrap class="bradcrampCell" valign="middle">
					&nbsp;
					<digi:link href="<%="/browseCategories.do?categoryId=" + String.valueOf(bradcrampItem.getItemId()) %>" styleClass="cmsSubcategory">
						<bean:write name="bradcrampItem" property="itemName"/>
					</digi:link>
				</td>
				<td valign="middle" class="bradcrampCell">
					<digi:img src="module/cms/images/bradcrampArrow.gif" width="9" height="17" border="0"/>
				</td>
		</logic:iterate>
		<td width="100%" class="bradcrampCell">&nbsp;</td>
	</tr>	
</table>
</logic:present>

<table border="0" cellpadding="3" cellspacing="0" width="100%">
	<tr>
		<td>
			
<logic:present name="cmsForm" property="categoryList">
	<table border="0" cellpadding="3" cellspacing="0" width="1">
	<bean:define id="catList" name="cmsForm" property="categoryList" type="java.util.List"/>
	<bean:define id="listSize" value="<%= String.valueOf(catList.size()-1) %>"/>
	
	<logic:iterate indexId="index" id="categoryList" name="catList" type="org.digijava.module.cms.dbentity.CMSCategory">
	<bean:define id="oddOrEven" value="<%= String.valueOf(index.intValue()%2) %>"/>
	<bean:define id="strIndex" value="<%= String.valueOf(index.intValue()) %>"/>
				<logic:equal name="oddOrEven" value="0">
				<tr>
				</logic:equal>
				<td width="1">
					&#9675;
				</td>
				<td valign="top" width="49%" nowrap>
					<digi:link href="/browseCategories.do" paramName="categoryList" paramId="categoryId" paramProperty="id" styleClass="cmsCategory">
						<bean:write name="categoryList" property="name" />
					</digi:link>
					&nbsp;
				</td>
				<logic:equal name="oddOrEven" value="1">
				</tr>
				</logic:equal>
				<logic:equal name="oddOrEven" value="0">
					<logic:equal name="strIndex" value="<%= listSize %>">
							<td width="1">&nbsp;</td>
							<td width="49%">&nbsp;</td>
						</tr>
					</logic:equal>
				</logic:equal>			
		
	</logic:iterate>
	</table> 
	</logic:present>		
		</td>
	</tr>
</table>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td class="rowItem" valign="middle" nowrap>
			&nbsp;			
			<a href="javascript:decorationSwitch()" class="cmsSubcategory">
			<logic:notEqual parameter="decoration" value="title" >
				<digi:trn key="cms:displayOnlyTitles">Display only titles</digi:trn>
			</logic:notEqual>
			<logic:equal parameter="decoration" value="title" >
				<digi:trn key="cms:displayTitleAndDescription">Display title and description</digi:trn>
			</logic:equal>
			</a>
		</td>
		<td class="rowItem" valign="middle" align="right" nowrap width="99%">
			<digi:img src="module/cms/images/tree/spacer.gif" height="26" border="0"/>
		</td>		
		<td class="rowItem" valign="middle" align="right" nowrap>
			<font class="cmsSmallText">
				<digi:trn key="cms:numOfResultsPerPage">Number of results per page:</digi:trn>
			</font>
		</td>
		<td class="rowItem" valign="middle" nowrap>
			<html:select name="cmsForm" property="itemsPerPage" onchange="changeItemPerPage()">
				<html:option value="5">5</html:option>
				<html:option value="10">10</html:option>
				<html:option value="15">15</html:option>
				<html:option value="20">20</html:option>
				<html:option value="25">25</html:option>
			</html:select>
		</td>		
		<td class="rowItem" valign="middle" nowrap width="1%">
			&nbsp;
			<logic:present name="cmsForm" property="prev">
				<a href="javascript:prev()" class="cmsSubcategory">
					&lt;&lt;&nbsp;<digi:trn key="cms:previous">Previous</digi:trn>&nbsp;
				</a>
			</logic:present>
			<logic:present name="cmsForm" property="prev">
				<logic:present name="cmsForm" property="next">
				::
				</logic:present>
			</logic:present>
			
			<logic:present name="cmsForm" property="next">
				<a href="javascript:next()" class="cmsSubcategory">
					&nbsp;<digi:trn key="cms:next">Next</digi:trn>&nbsp;&gt;&gt;&nbsp;
				</a>
			</logic:present>		
		</td>		
	</tr>
</table>

<logic:present name="cmsForm" property="contentItemList">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
	<td class="itemRowSeparator" colspan="4"><digi:img src="module/cms/images/tree/spacer.gif" height="2" border="0"/></td>
</tr>
<tr>
	<th class="itemHeader">&nbsp;</th>
	<th class="itemHeader" width="99%" align="left"><digi:trn key="cms:title">Title</digi:trn></th>
	<th class="itemHeader" align="left"><digi:trn key="cms:date">Date</digi:trn></th>
	<th class="itemHeader" align="left" nowrap><digi:trn key="cms:submitedBy">Submited by</digi:trn></th>
</tr>
<%
	int index2;
	String extension = null;
%>
<logic:iterate indexId="index" id="contentItem" name="cmsForm" property="contentItemList" type="org.digijava.module.cms.dbentity.CMSContentItem">
<bean:define id="oddOrEven" value="<%= String.valueOf(index.intValue()%2) %>"/>
<tr>
	<td class="itemRowSeparator" colspan="4"><digi:img src="module/cms/images/tree/spacer.gif" height="2" border="0"/></td>
</tr>
<logic:equal name="oddOrEven" value="0">
	<tr class="itemRowOdd">
</logic:equal>
<logic:notEqual name="oddOrEven" value="0">
	<tr class="itemRowEven">
</logic:notEqual>
	  <td width="25" valign="top" align="left" nowrap>
	    <%= index.intValue() + 1 %>
	  </td>
	  <td align="left" nowrap>
	  	<font class="cmsTitleText">		
			<logic:present name="contentItem" property="url" >
			    <a href='<bean:write name="contentItem" property="url" />' ><bean:write name="contentItem" property="title" /></a>	    
			</logic:present>
			<logic:notPresent name="contentItem" property="url" >
			    <bean:define id="fileName" name="contentItem" property="fileName" />
			    <%
				
				index2 = ((String)fileName).lastIndexOf(".");	
				if( index2 >= 0 ) {
				   extension = "module/cms/images/extensions/" + ((String)fileName).substring(index2 + 1,((String)fileName).length()) + ".gif";
				}
			    %>
			    <digi:img skipBody="true" src="<%=extension%>" border="0" align="absmiddle"/>
	   		    <digi:link href="/downloadFile.do" paramName="contentItem" paramId="itemId" paramProperty="id"><bean:write name="contentItem" property="title" /></digi:link>
			</logic:notPresent>
		<font>
		<font class="cmsSmallText">
		  	<digi:link href="/showContentItemDetails.do" paramName="contentItem" paramId="itemId" paramProperty="id"><digi:trn key="cms:seeDetails">See Details</digi:trn></digi:link>
		</font>
	  </td>
	<td nowrap valign="top" style="border-left:1px solid #C8CEEA;">
		<font class="cmsSmallText">
			<digi:date name="contentItem" property="submissionDate" format="dd MMM yyyy"/>
		<font>
	</td>
	<td style="border-left:1px solid #C8CEEA;" valign="top" align="center">
		<font class="cmsSmallText">
		<logic:present name="contentItem" property="authorUser" >
			<digi:context name="userProfileUrl" property="context/um/user/showUserProfile.do" />		
		    	<a href='<%=userProfileUrl%>?activeUserId=<bean:write name="contentItem" property="authorUser.id" />' ><bean:write name="contentItem" property="authorUser.name" /></a>
		</logic:present>
	    	<logic:notPresent name="contentItem" property="authorUser">
				N/A
			</logic:notPresent>			
		<font>
	</td>	
  </tr>
	<logic:notEqual parameter="decoration" value="title" >
	<logic:equal name="oddOrEven" value="0">
		<tr class="itemRowOdd">
	</logic:equal>
	<logic:notEqual name="oddOrEven" value="0">
		<tr class="itemRowEven">
	</logic:notEqual>  
		<td>&nbsp;</td>
		<td>
		<font class="cmsSmallText">
	    		<bean:write name="contentItem" property="description" />
		<font>
		</td>
		<td nowrap valign="top" style="border-left:1px solid #C8CEEA;">&nbsp;</td>
		<td style="border-left:1px solid #C8CEEA;">&nbsp;</td>	
	</tr>
	</logic:notEqual>

	<logic:equal name="oddOrEven" value="0">
		<tr class="itemRowOdd">
	</logic:equal>
	<logic:notEqual name="oddOrEven" value="0">
		<tr class="itemRowEven">
	</logic:notEqual>  
	<td>&nbsp;</td>
	<td nowrap>
		<digi:context name="cmsSendEmail" property="context/module/moduleinstance/showSendEmail.do" />
		<bean:define id="itemId" name="contentItem" property="id" />
		<a href="javascript:newWindow('<%=cmsSendEmail%>?itemId=<%=itemId%>','Email', 'width=600,height=450,status=no,menusbar=no,toolbar=no')" class="cmsSubcategory">
		<digi:img src="module/cms/images/emailIcon.gif" border="0" align="absmiddle"/> <digi:trn key="cms:emailThisItem">email this item</digi:trn>
		</a>
		&nbsp;
			<digi:secure actions="ADMIN">
	
			<digi:link href="<%= "/showEditContentItem.do?itemId=" +  itemId%>" styleClass="cmsSubcategory">
				<digi:img src="module/cms/images/editIcon.gif" border="0" alt="Edit" align="absmiddle"/>&nbsp;Edit
			</digi:link>
			
			<logic:equal name="contentItem" property="rejected" value="true">
				&nbsp;
				<a href="javascript:publishItem (<%= itemId %>)" styleClass="cmsSubcategory" class="cmsSubcategory">
				<digi:img src="module/cms/images/approveItem.gif" border="0" alt="Approve" align="absmiddle"/>&nbsp;Approve
				</a>
			</logic:equal>
			
			<logic:equal name="contentItem" property="rejected" value="false">
				<logic:equal name="contentItem" property="published" value="false">
					&nbsp;
					<a href="javascript:publishItem (<%= itemId %>)" styleClass="cmsSubcategory" class="cmsSubcategory">
					<digi:img src="module/cms/images/approveItem.gif" border="0" alt="Approve" align="absmiddle"/>&nbsp;Approve
					</a>
					&nbsp;
					<a href="javascript:rejectItem (<%= itemId %>)" styleClass="cmsSubcategory" class="cmsSubcategory">
						<digi:img src="module/cms/images/rejectItem.gif" border="0" alt="Reject" align="absmiddle"/>&nbsp;Reject
					</a>					
				</logic:equal>
			</logic:equal>
	
			<logic:equal name="contentItem" property="published" value="true">
				<logic:equal name="contentItem" property="rejected" value="false">
				&nbsp;
					<a href="javascript:rejectItem (<%= itemId %>)" styleClass="cmsSubcategory" class="cmsSubcategory">
						<digi:img src="module/cms/images/rejectItem.gif" border="0" alt="Reject" align="absmiddle"/>&nbsp;Reject
					</a>
				</logic:equal>
			</logic:equal>
			
			</digi:secure>
	</td>
	<td style="border-left:1px solid #C8CEEA;">&nbsp;</td>
	<td style="border-left:1px solid #C8CEEA;">&nbsp;</td>
 </td>
</tr>
</logic:iterate>
<tr>
	<td class="itemRowSeparator" colspan="4"><digi:img src="module/cms/images/tree/spacer.gif" height="2" border="0"/></td>
</tr>
</table>

</logic:present>
<logic:empty name="cmsForm" property="contentItemList">
  <digi:trn key="cms:noContItemsToView">There are no content items to view for this category</digi:trn>
</logic:empty >


<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td nowrap class="rowItem" valign="middle" width="100%">
			<digi:img src="module/cms/images/tree/spacer.gif" height="26" border="0"/>
		</td>
		<digi:secure authenticated="true">
		<td class="rowItem" valign="middle" nowrap>
		<bean:define id="catId" name="cmsForm" property="categoryId" />
			<digi:link href="<%= "/showCreateContentItem.do?catId=" + String.valueOf(catId) %>" styleClass="cmsSubcategory"><digi:trn key="cms:addItem">Add item</digi:trn></digi:link>&nbsp;
		</td>
		</digi:secure>		
	</tr>
</table>

</digi:form>

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
		<td nowrap>
			<digi:secure authenticated="true">
				<digi:link href="<%= "/showCreateContentItem.do?catId=" + String.valueOf(catId) %>"><digi:trn key="cms:addItem">Add URL or File</digi:trn></digi:link>
			</digi:secure>
		</td>
		<digi:secure actions="ADMIN">
		<td>
				<digi:link href="/showTaxonomyManager.do"><digi:trn key="cms:taxonomyManager">Taxonomy manager</digi:trn></digi:link>
		</td>
		</digi:secure>
	</tr>		
</table>

<script language="javascript">
<!--
function newWindow(file,window,features) {
    msgWindow=open(file,window,features);
    if (msgWindow.opener == null) msgWindow.opener = self;
//-->
}
</script>