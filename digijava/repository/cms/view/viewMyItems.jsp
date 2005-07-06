<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ page import="org.digijava.module.cms.form.CMSContentItemForm" %>
<%@ page import="org.digijava.module.common.dbentity.ItemStatus" %>
<logic:present parameter="itemStatus" >
	<bean:parameter id="itemStatus" name="itemStatus" />
</logic:present>

<logic:present parameter="viewMode" >
	<bean:parameter id="viewMode" name="viewMode" />
</logic:present>

<logic:present parameter="nav" >
	<bean:parameter id="nav" name="nav" />
</logic:present>

<digi:errors/>
<digi:instance property="cmsContentItemForm" />
<link rel="stylesheet" href="<digi:file src="module/cms/css/cms.css"/>">
<script language="JavaScript">
var separator = "<%=org.digijava.kernel.util.DgUtil.getParamSeparator()%>";

	function changeItemStatus (newStatus) {
	  <digi:context name="changeItemStatus" property="context/module/moduleinstance/showChangeItemStatus.do" />
      document.cmsContentItemForm.action = "<%= changeItemStatus %>" + separator + "itemStatus=" + newStatus + getViewModeParam() + getNavParam();
      document.cmsContentItemForm.submit();
	}
	function changeItemPerPage() {
	  <digi:context name="changeItemPerPage" property="context/module/moduleinstance/viewContentItems.do" />
      document.cmsContentItemForm.action = "<%= changeItemPerPage %>" +  getStatusParam() + getViewModeParam() + getNavParam();
      document.cmsContentItemForm.submit();	
	}	
	function next() {
	  <digi:context name="next" property="context/module/moduleinstance/viewContentItems.do" />
      document.cmsContentItemForm.action = "<%= next %>"  + separator + "nav=<bean:write name="cmsContentItemForm" property="next" />"  + getStatusParam()  + getViewModeParam();
      document.cmsContentItemForm.submit();	
	}	
	function prev() {
	  <digi:context name="next" property="context/module/moduleinstance/viewContentItems.do" />
      document.cmsContentItemForm.action = "<%= next %>" + separator + "nav=<bean:write name="cmsContentItemForm" property="prev"/>" + getStatusParam() + getViewModeParam();
      document.cmsContentItemForm.submit();	
	}
	
	function getStatusParam() {
	  var param = "";
		<logic:present name="itemStatus" >
		      param = separator + "itemStatus=<bean:write name="itemStatus" />";
		</logic:present>
	  return param;
	}
	
	function getViewModeParam() {
	  var param = "";
		<logic:present name="viewMode" >
		      param = separator + "viewMode=<bean:write name="viewMode" />";
		</logic:present>
	  return param;
	}	
	function getNavParam() {
	  var param = "";
		<logic:greaterThan name="nav" value="0">
		      param = separator + "nav=<bean:write name="nav" />";
		</logic:greaterThan>
	  return param;
	}
	
</script>


<digi:form action="/viewMyContentItems.do">

<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
	<td colspan="3" height="1">
		<%-- Table header --%>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td width="1"><digi:img src="module/cms/images/headerLeftEnd.gif" border="0"/></td>
				<td width="99%" class="mainHeader" align="center" valign="middle">
					<digi:trn key="cms:myItems">My Items</digi:trn>
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
<br>

		<!-- Tabs start -->
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td nowrap style="border-bottom : 1px solid #979CAC;">
					&nbsp;
					&nbsp;
				</td>
			<bean:define id="tabList" name="cmsContentItemForm" property="tabs" type="java.util.List"/>
			<bean:define id="tabListSize" value="<%= String.valueOf(tabList.size()) %>"/>
			<bean:define id="prevTabIsActive" value="false"/>
			<logic:iterate indexId="index" id="tab" name="tabList" type="org.digijava.module.cms.util.CmsTabItem">
				
					<logic:equal name="tab" property="active" value="true">
						<logic:equal name="index" value="0">	
							<td><digi:img src="module/cms/images/tabs/activeStartLeft.gif" border="0"/></td>
						</logic:equal>	
						<logic:greaterThan name="index" value="0">
							<td><digi:img src="module/cms/images/tabs/activeMidLeft.gif" border="0"/></td>
						</logic:greaterThan>	
						<bean:define id="prevTabIsActive" value="true"/>
					</logic:equal>
					<logic:notEqual name="tab" property="active" value="true">
						<logic:equal name="index" value="0">
							<td><digi:img src="module/cms/images/tabs/passiveStartLeft.gif" border="0"/></td>
						</logic:equal>	
						<logic:greaterThan name="index" value="0">
							<logic:equal name="prevTabIsActive" value="true">
								<td><digi:img src="module/cms/images/tabs/activeMidRight.gif" border="0"/></td>
							</logic:equal>
							<logic:equal name="prevTabIsActive" value="false">
								<td><digi:img src="module/cms/images/tabs/passiveToPassive.gif" border="0"/></td>
							</logic:equal>
						</logic:greaterThan>
						<bean:define id="prevTabIsActive" value="false"/>
					</logic:notEqual>
				
					<logic:equal name="tab" property="active" value="true">
						<td class="activeTab" nowrap>&nbsp;<font class="cmsCategory"><bean:write name="tab" property="caprionKey"/></font>&nbsp;</td>
					</logic:equal>
					<logic:notEqual name="tab" property="active" value="true">
						<td class="inactiveTab" nowrap>&nbsp;<digi:link styleClass="cmsCategory" href="<%= "/viewMyContentItems.do?viewMode=" + tab.getNavigationParam() %>"><bean:write name="tab" property="caprionKey"/></digi:link>&nbsp;</td>
					</logic:notEqual>
					
					<logic:equal name="index" value="<%= String.valueOf(Integer.parseInt(tabListSize) - 1) %>">
							<logic:equal name="tab" property="active" value="true">
								<td><digi:img src="module/cms/images/tabs/activeEndRight.gif" border="0"/></td>
							</logic:equal>
							<logic:notEqual name="tab" property="active" value="true">
								<td><digi:img src="module/cms/images/tabs/passiveEndRight.gif" border="0"/></td>
							</logic:notEqual>
					</logic:equal>
			</logic:iterate>
				<td width="99%" style="border-bottom : 1px solid #979CAC;">
					&nbsp;
				</td>
			
				
			</tr>
			<tr>
				<td class="tabContentArea" colspan="<%= String.valueOf((Integer.parseInt(tabListSize)) *2 + 3) %>">
				<!-- Tab content -->
				<br>
				<table border="0" width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td class="itemRowSeparator" colspan="6" align="right">
					  <table border="0" width="100%" cellpadding="0" cellspacing="0">
		  				    <td class="rowItem" width="90%">&nbsp;</td>
							<td class="rowItem" nowrap>
								<font class="cmsSmallText">
									<digi:trn key="cms:numOfResultsPerPage">Number of results per page:</digi:trn>
								</font>
							</td>
							<td class="rowItem" valign="middle" nowrap>
								<html:select name="cmsContentItemForm" property="itemsPerPage" onchange="changeItemPerPage()">
									<html:option value="5">5</html:option>
									<html:option value="10">10</html:option>
									<html:option value="15">15</html:option>
									<html:option value="20">20</html:option>
									<html:option value="25">25</html:option>
								</html:select>
							</td>		
							<td class="rowItem" valign="middle" nowrap width="1%">
								&nbsp;
								<logic:present name="cmsContentItemForm" property="prev">
									<a href="javascript:prev()" class="cmsSubcategory">
										&lt;&lt;&nbsp;<digi:trn key="cms:previous">Previous</digi:trn>&nbsp;
									</a>
								</logic:present>
								<logic:present name="cmsContentItemForm" property="prev">
									<logic:present name="cmsContentItemForm" property="next">
									::
									</logic:present>
								</logic:present>
								
								<logic:present name="cmsContentItemForm" property="next">
									<a href="javascript:next()" class="cmsSubcategory">
										&nbsp;<digi:trn key="cms:next">Next</digi:trn>&nbsp;&gt;&gt;&nbsp;
									</a>
								</logic:present>		
							</td>		
					  </table>
					</td>
				</tr>				
				<tr>
					<td class="itemRowSeparator" colspan="5"><digi:img src="module/cms/images/tree/spacer.gif" height="2" border="0"/></td>
				</tr>
				 <tr>
				    <th class="itemHeader">
					   &nbsp;
				    </td>				 
				    <th class="itemHeader" width="99%" align="left">
					   <digi:trn key="cms:title">Title</digi:trn>
				    </td>
					<th class="itemHeader" align="left" nowrap>
					   Author
				    </td>
					 <th class="itemHeader" align="left" nowrap>
					   Release date
				    </td>				    
					<th class="itemHeader" nowrap>
					   &nbsp;
				    </td>
				 </tr>
				<tr>
					<td class="itemRowSeparator" colspan="5"><digi:img src="module/cms/images/tree/spacer.gif" height="2" border="0"/></td>
				</tr>
				 <logic:present name="cmsContentItemForm" property="itemsList">
				  <logic:iterate indexId="index" id="itemsList" name="cmsContentItemForm" property="itemsList" type="org.digijava.module.cms.form.CMSContentItemForm.CMSContentItemInfo">
					<bean:define id="oddOrEven" value="<%= String.valueOf(index.intValue()%2) %>"/>				  
					<logic:equal name="oddOrEven" value="0">
						<tr class="itemRowOdd">
					</logic:equal>
					<logic:notEqual name="oddOrEven" value="0">
						<tr class="itemRowEven">
					</logic:notEqual>
				  <td style="border-bottom: 1px solid #C8CEEA;">
				  </td>					
				  <td align="left" style="border-bottom: 1px solid #C8CEEA;">
				     <bean:write name="itemsList" property="title" />
				  </td>
				  <td align="left" style="border-bottom: 1px solid #C8CEEA;" nowrap>
					  &nbsp;
					  <logic:present name="itemsList" property="authorUser">
						<digi:context name="userProfileUrl" property="context/um/user/showUserProfile.do" />		
			    			<a href='<%=userProfileUrl%>?activeUserId=<bean:write name="itemsList" property="authorUser.id" />' ><bean:write name="itemsList" property="authorUser.name" /></a>
						
					  </logic:present>	 
					  <logic:notPresent name="itemsList" property="authorUser">
					     N/A
					  </logic:notPresent>
					  &nbsp;
				  </td>
				  <td style="border-bottom: 1px solid #C8CEEA;" nowrap>
					<digi:date name="itemsList" property="creationDate" format="MM/dd/yyyy"/>					  
				  </td>
				  <td align="center" style="border-bottom: 1px solid #C8CEEA;" nowrap>
				  	&nbsp;
				  	<logic:equal name="itemsList" property="editable" value="true">
				     <digi:link href="/showEditContentItem.do" paramName="itemsList"  paramId="itemId" paramProperty="id">
						 <digi:img src="module/cms/images/editIcon.gif" border="0" alt="Edit"/>
					 </digi:link>
					</logic:equal> 
					<digi:secure actions="ADMIN">				 
				     <digi:link href="/deleteContentItem.do" paramName="itemsList"  paramId="itemId" paramProperty="id">
					 	<digi:img src="module/cms/images/deleteIcon.gif" border="0" alt="Delete"/>
					 </digi:link>
					</digi:secure> 
					&nbsp; 
				  </td>
				  </tr>
				  </logic:iterate>
				 </logic:present>
				</table>		
				
				
				
	<table border="0" cellpadding="3" cellspacing="0" width="100%">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td nowrap class="rowItem" valign="middle" width="100%">
							<digi:img src="module/cms/images/tree/spacer.gif" height="26" border="0"/>
						</td>
						<td class="rowItem" valign="middle" nowrap>
						 	<digi:img src="module/cms/images/tree/spacer.gif" width="2" border="0"/>
						</td>								
					</tr>
				</table>
				
				<!-- Tab content -->
				</td>
			</tr>
		</table>
		<!-- Tabs end -->
					
		
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
		<digi:secure actions="ADMIN">
		<td nowrap colspan="2">
			<digi:link href="/showTaxonomyManager.do"><digi:trn key="cms:taxonomyManager">Taxonomy manager</digi:trn></digi:link>
		</td>
		</digi:secure>
	</tr>
	<tr>
		<digi:secure actions="ADMIN">
		<td nowrap colspan="2">
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
		<td>
			<digi:link href="/index.do"><digi:trn key="cms:showIndex">Show index</digi:trn></digi:link>
		</td>
	</tr>		
</table>

</digi:form>