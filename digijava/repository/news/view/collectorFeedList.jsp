<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>


<digi:errors/>

<digi:instance property="collectorFeedForm" />
<link rel="stylesheet" href="<digi:file src="module/syndication/css/cms.css"/>">

<bean:define id="itemsPerPage" name="collectorFeedForm" property="itemsPerPage" type="Integer"/>

<logic:present parameter="nav" >
<bean:parameter id="nav" name="nav" />
</logic:present>

<script language="JavaScript">

	function next() {
	  <digi:context name="next" property="context/module/moduleinstance/collectorFeedList.do" />
      document.collectorFeedForm.action = "<%= next %>?nav=<bean:write name="collectorFeedForm" property="next" />";
      document.collectorFeedForm.submit();	
	}
	function prev() {
	  <digi:context name="prev" property="context/module/moduleinstance/collectorFeedList.do" />
      document.collectorFeedForm.action = "<%= prev %>?nav=<bean:write name="collectorFeedForm" property="prev"/>";
      document.collectorFeedForm.submit();	
	}
	function changeItemPerPage() {
	  <digi:context name="changeItemPerPage" property="context/module/moduleinstance/collectorFeedList.do" />
      document.collectorFeedForm.action = "<%= changeItemPerPage %>" + getNavParam();
      document.collectorFeedForm.submit();	

	}	

	function changeItem(val) {
	  if( val == 1 ) {
		if (!confirm("Do you really want to delete selected feed ?")) 
			return;
		}
	  <digi:context name="changeItem" property="context/module/moduleinstance/manageFeed.do" />
      document.collectorFeedForm.action = "<%= changeItem %>" + getNavParam();
      document.collectorFeedForm.submit();
	}

	function getNavParam() {
	  var param = "";
		<logic:greaterThan name="nav" value="0">
		      param = "?nav=<bean:write name="nav" />";
		</logic:greaterThan>
	  return param;

	}
	
	function disableItem (itemId){
		<digi:context name="disableItem" property="context/module/moduleinstance/manageFeed.do" />
		document.collectorFeedForm.action = '<%=disableItem%>?itemId=' + itemId + "&action=0";
	    document.collectorFeedForm.submit();	
	}
	
	function enableItem (itemId){
		<digi:context name="enableItem" property="context/module/moduleinstance/manageFeed.do" />
		document.collectorFeedForm.action = '<%=enableItem%>?itemId=' + itemId + "&action=1";
	    document.collectorFeedForm.submit();	
	}

	function deleteItem (itemId){
		if (confirm("Do you really want to delete selected feed ?")) {
			<digi:context name="deleteItem" property="context/module/moduleinstance/manageFeed.do" />
			document.collectorFeedForm.action = '<%=deleteItem%>?itemId=' + itemId + "&action=3";
		    document.collectorFeedForm.submit();	
		}
	}

</script>





<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
	<td colspan="3" height="1">
		<%-- Table header --%>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td width="1"><digi:img src="module/syndication/images/headerLeftEnd.gif" border="0"/></td>
				<td width="99%" class="mainHeader" align="center" valign="middle">
					<digi:trn key="syndication:collectorFeeds">Collector feeds page</digi:trn>
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
	<td width="100%" align="center" valign="top" bgcolor="#F2F4FC">
<%-- Inner --%>

<digi:form action="/collectorFeedList.do">

			
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td class="rowItem" valign="middle" nowrap>
			&nbsp;			
		</td>
		<td class="rowItem" valign="middle" align="right" nowrap width="99%">
			<digi:img src="module/syndication/images/tree/spacer.gif" height="26" border="0"/>
		</td>		
		<td class="rowItem" valign="middle" align="right" nowrap>
			<font class="cmsSmallText">
				<digi:trn key="syndication:numOfResultsPerPage">Number of results per page:</digi:trn>
			</font>
		</td>
		<td class="rowItem" valign="middle" nowrap>
			<html:select name="collectorFeedForm" property="itemsPerPage" onchange="changeItemPerPage()">
				<html:option value="5">5</html:option>
				<html:option value="10">10</html:option>
				<html:option value="15">15</html:option>
				<html:option value="20">20</html:option>
				<html:option value="25">25</html:option>
			</html:select>
		</td>		
		<td class="rowItem" valign="middle" nowrap width="1%">
			&nbsp;
			<logic:present name="collectorFeedForm" property="prev">
				<a href="javascript:prev()" class="cmsSubcategory">
					&lt;&lt;&nbsp;<digi:trn key="cms:previous">Previous</digi:trn>&nbsp;
				</a>
			</logic:present>
			<logic:present name="collectorFeedForm" property="prev">
				<logic:present name="collectorFeedForm" property="next">
				::
				</logic:present>
			</logic:present>
			
			<logic:present name="collectorFeedForm" property="next">
				<a href="javascript:next()" class="cmsSubcategory">
					&nbsp;<digi:trn key="cms:next">Next</digi:trn>&nbsp;&gt;&gt;&nbsp;
				</a>
			</logic:present>		
		</td>		
	</tr>
</table>

<logic:present name="collectorFeedForm" property="collectorFeedList">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
	<td class="itemRowSeparator" colspan="9"><digi:img src="module/syndication/images/tree/spacer.gif" height="2" border="0"/></td>
</tr>
<tr>
	<th class="itemHeader">&nbsp;</th>
	<th class="itemHeader" align="left"><digi:trn key="syndication:nameDescription">Name/Description</digi:trn></th>
	<th class="itemHeader" align="center"><digi:trn key="syndication:contentType">Content Type</digi:trn></th>
	<th class="itemHeader" align="center"><digi:trn key="syndication:source">Source</digi:trn></th>
	<th class="itemHeader" align="center"><digi:trn key="syndication:creationDate">Creation</digi:trn></th>
	<th class="itemHeader" align="center"><digi:trn key="syndication:firstRunDate">First Run</digi:trn></th>
	<th class="itemHeader" align="center"><digi:trn key="syndication:lastRunDate">Last Run</digi:trn></th>
	<th class="itemHeader" align="center"><digi:trn key="syndication:schedule">Schedule</digi:trn></th>
	<th class="itemHeader" align="center"><digi:trn key="syndication:status">Status</digi:trn></th>
</tr>
<%
	int index2;
	String extension = null;
%>
<logic:iterate indexId="index" id="contentItem" name="collectorFeedForm" property="collectorFeedList" type="org.digijava.module.syndication.dbentity.CollectorFeedItem">
<bean:define id="oddOrEven" value="<%= String.valueOf(index.intValue()%2) %>"/>
<tr>
	<td class="itemRowSeparator" colspan="9"><digi:img src="module/syndication/images/tree/spacer.gif" height="2" border="0"/></td>
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
			<logic:present name="contentItem" property="feed.feedUrl" >
			<html:multibox name="collectorFeedForm" property="selectedItems" value="<%=String.valueOf(contentItem.getId())%>" /> 
			    <a href='<bean:write name="contentItem" property="feed.feedUrl" />' ><bean:write name="contentItem" property="feed.feedTitle" /></a>	    
			</logic:present>
		</font>
	  </td>
	  <td nowrap valign="top" align="center" style="border-left:1px solid #C8CEEA;">
		<font class="cmsSmallText">
			<bean:write name="contentItem" property="contentType" />
		<font>
	  </td>	  
	  <td nowrap align="center" valign="top" style="border-left:1px solid #C8CEEA;">
		<font class="cmsSmallText">
			<logic:present name="contentItem" property="feed.sourceUrl" >
			    <a href='<bean:write name="contentItem" property="feed.sourceUrl" />' ><bean:write name="contentItem" property="feed.sourceName" /></a>
			</logic:present>
		<font>
	  </td>	  	  
	<td nowrap align="center" valign="top" style="border-left:1px solid #C8CEEA;">
		<font class="cmsSmallText">
			<digi:date name="contentItem" property="creationDate" format="dd MMM yyyy  h:mm a"/>
		<font>
	</td>
	
	<td nowrap align="center" valign="top" style="border-left:1px solid #C8CEEA;">
		<font class="cmsSmallText">
			<digi:date name="contentItem" property="firstRun" format="dd MMM yyyy  h:mm a"/>
		<font>
		&nbsp;
	</td>
	<td nowrap align="center" valign="top" style="border-left:1px solid #C8CEEA;">
		<font class="cmsSmallText">
			<digi:date name="contentItem" property="lastRun" format="dd MMM yyyy  h:mm a"/>
		<font>
		&nbsp;
	</td>
	<td nowrap valign="top" align="center" style="border-left:1px solid #C8CEEA;">
		<font class="cmsSmallText">
			<logic:equal name="contentItem" property="schedule" value="0">
				<digi:trn key="syndication:daily">Daily</digi:trn>
			</logic:equal>
			<logic:equal name="contentItem" property="schedule" value="1">
				<digi:trn key="syndication:weekly">Weekly</digi:trn>
			</logic:equal>&nbsp;
			<digi:date name="contentItem" property="scheduleTime" format="(h:mm a)"/>
		<font>
	</td>
	<td nowrap valign="top" align="center" style="border-left:1px solid #C8CEEA;">
		<font class="cmsSmallText">
			<logic:equal name="contentItem" property="status" value="1">
				<digi:trn key="syndication:statusEnable"><b>Enable</b></digi:trn>
			</logic:equal>
			<logic:equal name="contentItem" property="status" value="0">
				<digi:trn key="syndication:statusDisable">Disable</digi:trn>
			</logic:equal>				
		<font>
	</td>
  </tr>
	<logic:equal name="oddOrEven" value="0">
		<tr class="itemRowOdd">
	</logic:equal>
	<logic:notEqual name="oddOrEven" value="0">
		<tr class="itemRowEven">
	</logic:notEqual>  
		<td>&nbsp;</td>
		<td>
		<font class="cmsSmallText">
	    		<bean:write name="contentItem" property="feed.feedDescription" />
		<font>
		</td>
		<td style="border-left:1px solid #C8CEEA;">&nbsp;</td>
		<td style="border-left:1px solid #C8CEEA;" >&nbsp;</td>	
		<td style="border-left:1px solid #C8CEEA;">&nbsp;</td>
		<td style="border-left:1px solid #C8CEEA;">&nbsp;</td>
		<td style="border-left:1px solid #C8CEEA;">&nbsp;</td>
		<td style="border-left:1px solid #C8CEEA;">&nbsp;</td>
		<td style="border-left:1px solid #C8CEEA;">&nbsp;</td>
	</tr>

	<logic:equal name="oddOrEven" value="0">
		<tr class="itemRowOdd">
	</logic:equal>
	<logic:notEqual name="oddOrEven" value="0">
		<tr class="itemRowEven">
	</logic:notEqual>  
	<td>&nbsp;</td>
	<td nowrap>
		<bean:define id="itemId" name="contentItem" property="id" />
		&nbsp;
			<digi:link href="<%= "/showEditNewFeed.do?itemId=" +  itemId%>" styleClass="cmsSubcategory">
				<digi:img src="module/syndication/images/editIcon.gif" border="0" alt="Edit" align="absmiddle"/>&nbsp;Edit
			</digi:link>
			
			<logic:equal name="contentItem" property="status" value="0">
				&nbsp;
				<a href="javascript:enableItem (<%= itemId %>)" styleClass="cmsSubcategory" class="cmsSubcategory">
				<digi:img src="module/syndication/images/approveItem.gif" border="0" alt="Enable" align="absmiddle"/>&nbsp;Enable
				</a>
			</logic:equal>

			<logic:equal name="contentItem" property="status" value="1">
				&nbsp;
				<a href="javascript:disableItem (<%= itemId %>)" styleClass="cmsSubcategory" class="cmsSubcategory">
				<digi:img src="module/syndication/images/approveItem.gif" border="0" alt="Disable" align="absmiddle"/>&nbsp;Disable
				</a>
			</logic:equal>

				&nbsp;
				<a href="javascript:deleteItem (<%= itemId %>)" styleClass="cmsSubcategory" class="cmsSubcategory">
				<digi:img src="module/syndication/images/rejectItem.gif" border="0" alt="Disable" align="absmiddle"/>&nbsp;Delete
				</a>
	</td>
		<td style="border-left:1px solid #C8CEEA;">&nbsp;</td>
		<td style="border-left:1px solid #C8CEEA;" >&nbsp;</td>	
		<td style="border-left:1px solid #C8CEEA;">&nbsp;</td>
		<td style="border-left:1px solid #C8CEEA;">&nbsp;</td>
		<td style="border-left:1px solid #C8CEEA;">&nbsp;</td>
		<td style="border-left:1px solid #C8CEEA;">&nbsp;</td>
		<td style="border-left:1px solid #C8CEEA;">&nbsp;</td>
 </td>
</tr>
</logic:iterate>
<tr>
	<td class="itemRowSeparator" colspan="9"><digi:img src="module/syndication/images/tree/spacer.gif" height="2" border="0"/></td>
</tr>
</table>

</logic:present>
<logic:empty name="collectorFeedForm" property="collectorFeedList">
  <digi:trn key="syndication:noFeedItems">There are no feeds to view</digi:trn>
</logic:empty >


<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td class="rowItem" valign="middle" nowrap>
	    <digi:trn key="syndication:doTheFollowing">Do the following to the selected feeds</digi:trn>:
		</td>
		<td class="rowItem" valign="middle">
			<html:select name="collectorFeedForm" property="itemOperation" onchange="changeItem(this.value)">
				<html:option value="0"><digi:trn key="syndication:select">select</digi:trn></html:option>
				<html:option value="4"><digi:trn key="syndication:manualHarvest">Manual Harvest</digi:trn></html:option>
				<html:option value="2"><digi:trn key="syndication:enableFeed">Enable Feed</digi:trn></html:option>
				<html:option value="3"><digi:trn key="syndication:disableFeed">Disable Feed</digi:trn></html:option>
				<html:option value="1"><digi:trn key="syndication:deleteFeed">Delete Feed</digi:trn></html:option>
			</html:select> 
		</td>
		<td nowrap class="rowItem" valign="middle" width="100%">
			<digi:img src="module/syndication/images/tree/spacer.gif" height="26" border="0"/>
		</td>
		<td class="rowItem" valign="middle" nowrap>
			<digi:link href="/showCreateNewFeed.do" styleClass="cmsSubcategory"><digi:trn key="syndication:addNewFeed">Add new feed</digi:trn></digi:link>&nbsp;
		</td>
	</tr>
	<logic:equal name="collectorFeedForm" property="activateAggregator" value="false">
		<tr>
			<td colspan="4">
			<font color=red>
			Please Note:<br>
			The aggregator manager is disabled from digi.xml, to enable aggregator please insert the line &lt;aggregation&gt;true&lt;/aggregation&gt; in digi.xml
			</font>
			</td>
		</tr>
	</logic:equal>
</table>

</digi:form>

		

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

