<%@ page language="java" %>

<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<script language="javascript">
	function navMouseOver(tdObject) {
		tdObject.className="navigarionBarMouseOver";
	}
	function navMouseOut(tdObject) {
		tdObject.className="navigarionBarDefault";
	}
	function navMouseDown(tdObject) {
		tdObject.className="navigarionBarMouseDown";
	}
	function navMouseUp(tdObject) {
		tdObject.className="navigarionBarMouseOver";
	}
	function gotoNavigation (actionUrl) {
		window.location.href=actionUrl;
	}
</script>

<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
	<tr class="green">
		<td><digi:img src="module/admin/images/greenLeftTile.gif" border="0" width="20"/></td>
		<td width="100%" align="center">
			<font class="sectionTitle">
				<digi:link styleClass="admin" href="/showLayout.do"><digi:trn key="admin:adminMenu">Admin Menu</digi:trn></digi:link>			
			</font>
		</td>
	</tr>
	<tr>
		<td colspan="2" class="green" height="100%" valign="top">
			<table border="0" width="100%" cellpadding="0" cellspacing="3">
				<tr>
					<td nowrap class="navigarionBarDefault" 
							   onMouseOver="navMouseOver(this)"
							   onMouseOut="navMouseOut(this)"
							   onMouseDown="navMouseDown(this)"
							   onMouseUp="navMouseUp(this)"
							   onClick="gotoNavigation('<digi:site property="url"/>')">
						&nbsp;<b><digi:site property="name"/></b>
					</td>
				</tr>
				<digi:secure globalAdmin="true">
				<tr>
			      	<digi:context name="createSite" property="context/module/moduleinstance/showCreateSite.do" />
					<td nowrap class="navigarionBarDefault" 
							   onMouseOver="navMouseOver(this)"
							   onMouseOut="navMouseOut(this)"
							   onMouseDown="navMouseDown(this)"
							   onMouseUp="navMouseUp(this)"
							   onClick="gotoNavigation('<%= createSite %>')">
					&nbsp;<digi:trn key="admin:createSite">Create Site</digi:trn>
					</td>
				</tr>
				</digi:secure>
				<tr>
					<digi:context name="editSite" property="context/module/moduleinstance/showEditSite.do"/>
					<td nowrap class="navigarionBarDefault" 
							   onMouseOver="navMouseOver(this)"
							   onMouseOut="navMouseOut(this)"
							   onMouseDown="navMouseDown(this)"
							   onMouseUp="navMouseUp(this)"
							   onClick="gotoNavigation('<%= editSite %>')">
						&nbsp;<digi:trn key="admin:editSite">Edit Site</digi:trn>
					</td>
				</tr>				
				<tr>
					<digi:context name="searchSite" property="context/module/moduleinstance/showSearchSite.do"/>
					<td nowrap class="navigarionBarDefault" 
							   onMouseOver="navMouseOver(this)"
							   onMouseOut="navMouseOut(this)"
							   onMouseDown="navMouseDown(this)"
							   onMouseUp="navMouseUp(this)"
							   onClick="gotoNavigation('<%= searchSite %>')">
						&nbsp;<digi:trn key="admin:searchSite">Search Site</digi:trn>
					</td>
				</tr>
				<tr>
					<digi:context name="showModuleInstances" property="context/module/moduleinstance/showSiteInstances.do"/>
					<td nowrap class="navigarionBarDefault" 
							   onMouseOver="navMouseOver(this)"
							   onMouseOut="navMouseOut(this)"
							   onMouseDown="navMouseDown(this)"
							   onMouseUp="navMouseUp(this)"
							   onClick="gotoNavigation('<%= showModuleInstances %>')">
						&nbsp;<digi:trn key="admin:moduleInstances">Module Instances</digi:trn>
					</td>
				</tr>
				<digi:secure globalAdmin="true">
				<tr>
					<digi:context name="showCommonInstances" property="context/module/moduleinstance/showCommonInstances.do"/>
					<td nowrap class="navigarionBarDefault" 
							   onMouseOver="navMouseOver(this)"
							   onMouseOut="navMouseOut(this)"
							   onMouseDown="navMouseDown(this)"
							   onMouseUp="navMouseUp(this)"
							   onClick="gotoNavigation('<%= showCommonInstances%>')">
						&nbsp;<digi:trn key="admin:commonInstances">Common Instances</digi:trn>
					</td>
				</tr>
				</digi:secure>
				<tr>
					<digi:context name="showGroups" property="context/module/moduleinstance/showGroups.do"/>
					<td nowrap class="navigarionBarDefault" 
							   onMouseOver="navMouseOver(this)"
							   onMouseOut="navMouseOut(this)"
							   onMouseDown="navMouseDown(this)"
							   onMouseUp="navMouseUp(this)"
							   onClick="gotoNavigation('<%= showGroups %>')">
						&nbsp;<digi:trn key="admin:administerGroups">Administer Groups</digi:trn>
					</td>
				</tr>
				<tr>
					<digi:context name="searchUser" property="context/module/moduleinstance/showSearchUser.do"/>
					<td nowrap class="navigarionBarDefault" 
							   onMouseOver="navMouseOver(this)"
							   onMouseOut="navMouseOut(this)"
							   onMouseDown="navMouseDown(this)"
							   onMouseUp="navMouseUp(this)"
							   onClick="gotoNavigation('<%= searchUser %>')">
						&nbsp;<digi:trn key="admin:userAdministration">User Administration</digi:trn>
					</td>
				</tr>
				<tr>
					<digi:context name="allSites" property="context/module/moduleinstance/showAllSites.do"/>
					<td nowrap class="navigarionBarDefault" 
							   onMouseOver="navMouseOver(this)"
							   onMouseOut="navMouseOut(this)"
							   onMouseDown="navMouseDown(this)"
							   onMouseUp="navMouseUp(this)"
							   onClick="gotoNavigation('<%= allSites %>')">
						&nbsp;<digi:trn key="admin:allSites">All Sites</digi:trn>
					</td>
				</tr>
				<digi:secure globalAdmin="true">
				 <tr>
					<digi:context name="locales" property="context/module/moduleinstance/showLocales.do"/>
				 	<td nowrap class="navigarionBarDefault" 
							   onMouseOver="navMouseOver(this)"
							   onMouseOut="navMouseOut(this)"
							   onMouseDown="navMouseDown(this)"
							   onMouseUp="navMouseUp(this)"
							   onClick="gotoNavigation('<%= locales %>')">
						&nbsp;<digi:trn key="admin:locales">Locales</digi:trn>
					</td>
				</tr>
				<tr>
					<digi:context name="caches" property="context/module/moduleinstance/showCaches.do"/>
					<td nowrap class="navigarionBarDefault" 
							   onMouseOver="navMouseOver(this)"
							   onMouseOut="navMouseOut(this)"
							   onMouseDown="navMouseDown(this)"
							   onMouseUp="navMouseUp(this)"
							   onClick="gotoNavigation('<%= caches %>')">
						&nbsp;<digi:trn key="admin:caches">Caches</digi:trn>
					</td>
				</tr>
				<tr>
					<digi:context name="showUnclosedSessions" property="context/module/moduleinstance/showUnclosedSessions.do"/>
					<td nowrap class="navigarionBarDefault" 
							   onMouseOver="navMouseOver(this)"
							   onMouseOut="navMouseOut(this)"
							   onMouseDown="navMouseDown(this)"
							   onMouseUp="navMouseUp(this)"
							   onClick="gotoNavigation('<%= showUnclosedSessions%>')">
						&nbsp;<digi:trn key="admin:unclosedSessions">Unclosed Sessions</digi:trn>
					</td>
				</tr>
				</digi:secure>
				<tr>
					<digi:context name="configuration" property="context/module/moduleinstance/showConfiguration.do"/>
					<td nowrap class="navigarionBarDefault" 
							   onMouseOver="navMouseOver(this)"
							   onMouseOut="navMouseOut(this)"
							   onMouseDown="navMouseDown(this)"
							   onMouseUp="navMouseUp(this)"
							   onClick="gotoNavigation('<%= configuration %>')">
						&nbsp;<digi:trn key="admin:configuration">Configuration</digi:trn>
					</td>
				</tr>				
				<tr>
					<digi:context name="emailTemplates" property="context/module/moduleinstance/showEmailTemplates.do"/>
					<td nowrap class="navigarionBarDefault" 
							   onMouseOver="navMouseOver(this)"
							   onMouseOut="navMouseOut(this)"
							   onMouseDown="navMouseDown(this)"
							   onMouseUp="navMouseUp(this)"
							   onClick="gotoNavigation('<%= emailTemplates %>')">
						&nbsp;<digi:trn key="admin:emailTemplates">Email Templates</digi:trn>
					</td>
				</tr>				
				<tr>
					<td nowrap class="navigarionBarDefault" 
							   onMouseOver="navMouseOver(this)"
							   onMouseOut="navMouseOut(this)"
							   onMouseDown="navMouseDown(this)"
							   onMouseUp="navMouseUp(this)"
							   onClick="gotoNavigation('<digi:site property="url"/>')">
						&nbsp;<b><digi:trn key="admin:mainPage">Main Page</digi:trn></b>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>