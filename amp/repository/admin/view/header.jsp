<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<table border="0" cellpadding="0" cellspacing="0" width="100%" height="40">
	<tr class="headerBackgroung">
		<td valign="top" height="40"><digi:img src="module/admin/images/bgrDots.gif" border="0"/></td>
		<td><digi:img src="module/admin/images/spacer.gif" border="0" width="20"/></td>
		<td valign="center" nowrap height="40">
			<font class="headerTitle">Administrator console</font>
		</td>
		<td valign="top" height="40"><digi:img src="module/admin/images/spacer.gif" border="0" width="20"/></td>
		<td valign="top" height="40"><digi:img src="module/admin/images/bgrDotsNarrow.gif" border="0"/></td>
		<td valign="top" height="40"><digi:img src="module/admin/images/spacer.gif" border="0" width="20" height="40"/></td>
		<digi:secure globalAdmin="true">
		<td valign="bottom" height="40"><digi:link href="/showCreateSite.do"><digi:img src="module/admin/images/icons/createSite.gif" border="0" alt="Create site"/></digi:link></td>
		</digi:secure>
		<td valign="bottom" height="40"><digi:link href="/showEditSite.do"><digi:img src="module/admin/images/icons/editSite.gif" border="0" alt="Edit site"/></digi:link></td>
		<td valign="bottom" height="40"><digi:link href="/showSearchSite.do"><digi:img src="module/admin/images/icons/searchSite.gif" border="0" alt="Search site"/></digi:link></td>
		<td valign="bottom" height="40"><digi:link href="/showSiteInstances.do"><digi:img src="module/admin/images/icons/moduleInstances.gif" border="0" alt="Module instances"/></digi:link></td>
		<td valign="bottom" height="40"><digi:link href="/showGroups.do"><digi:img src="module/admin/images/icons/userGroup.gif" border="0" alt="Administer Groups"/></digi:link></td>
		<td valign="bottom" height="40"><digi:link href="/showSearchUser.do"><digi:img src="module/admin/images/icons/user.gif" border="0" alt="User Administration"/></digi:link></td>
		<td valign="bottom" height="40"><digi:link href="/showAllSites.do"><digi:img src="module/admin/images/icons/allSites.gif" border="0" alt="All sites"/></digi:link></td>
		<td valign="bottom" height="40"><digi:link href="/showLocales.do"><digi:img src="module/admin/images/icons/locales.gif" border="0" alt="Locales"/></digi:link></td>
		<td valign="bottom" height="40"><digi:link href="/showCaches.do"><digi:img src="module/admin/images/icons/caches.gif" border="0" alt="Caches"/></digi:link></td>
		<td valign="top" height="40"><digi:img src="module/admin/images/spacer.gif" border="0" width="20" height="40"/></td>
 		<td><digi:img src="module/admin/images/bgrDotsNarrow.gif" border="0"/></td>
		<td width="100%" height="40">
			&nbsp;
		</td>		
		
	</tr>
</table>