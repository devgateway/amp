<%@page pageEncoding="UTF-8"%>
<%@page contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<style>
    /* TODO. I know these code smells. Need to move these styles out of this jsp page to global stylesheet.
    All tables in admin use individual styles now */
    #dataTable thead tr td {
        vertical-align: middle;
        background-color: #C7D4DB;
        text-align: center;
        height: 22px;
        font-weight: bold;
        border: 1px solid #b8b7b7;
    }
</style>

<digi:instance property="suspendLoginManagerForm"/>

<script type="text/javascript">
    function removeSuspendRecord(id){
        <c:set var="confirmDelete">
            <digi:trn jsFriendly="true">
                Are you sure to remove the suspend record?
            </digi:trn>
        </c:set>
        if (confirm("${confirmDelete}")){
            document.location.href = '/um/suspendLoginManager.do~action=delete~objId=' + id;
        }
    }
</script>


<table width="100%" cellpadding="0">
	<tr>
		<td align="center">	
	
			<table id="dataTable" class="inside" width="100%">
                <thead>
                    <tr>
                        <td>
                            <digi:trn>Name</digi:trn>
                        </td>
                        <td>
                            <digi:trn>Text</digi:trn>
                        </td>
                        <td>
                            <digi:trn>Active</digi:trn>
                        </td>
                        <td>
                            <digi:trn>Expires</digi:trn>
                        </td>
                        <td>
                            <digi:trn>Date</digi:trn>
                        </td>
                        <td>
                            <digi:trn>Actions</digi:trn>
                        </td>
                    </tr>
                </thead>
				<logic:iterate name="suspendLoginManagerForm" property="suspendLoginObjects" id="suspendLoginObject">
                    <c:set var="currentId"><bean:write name="suspendLoginObject" property="id"/></c:set>
					<tr>
						<td nowrap="nowrap" class="inside">
							<bean:write name="suspendLoginObject" property="name"/>
						</td>
						<td nowrap="nowrap" class="inside">
							<bean:write name="suspendLoginObject" property="reasonText"/>
						</td>
						<td nowrap="nowrap" class="inside">
							<c:set var="activeVar">
                                <bean:write name="suspendLoginObject" property="active"/>
                            </c:set>
                            <c:choose>
                                <c:when test="${activeVar}">
                                    <digi:trn>Yes</digi:trn>
                                </c:when>
                                <c:otherwise>
                                    <digi:trn>No</digi:trn>
                                </c:otherwise>
                            </c:choose>
						</td>
						<td nowrap="nowrap" class="inside">
						    <c:set var="expiresVar">
						        <bean:write name="suspendLoginObject" property="expires"/>
						    </c:set>
						    <c:choose>
						        <c:when test="${expiresVar}">
						            <digi:trn>Yes</digi:trn>
						        </c:when>
						        <c:otherwise>
						            <digi:trn>No</digi:trn>
						        </c:otherwise>
						    </c:choose>
						</td>
						<td nowrap="nowrap" class="inside">
							<bean:write name="suspendLoginObject" property="formatedDate"/>
						</td>
						<td nowrap="nowrap" class="inside">
							<a href="/um/suspendLoginManager.do~action=users~objId=${currentId}">
                                <digi:trn>Users</digi:trn>
                            </a>&nbsp;|&nbsp;

                            <a href="/um/suspendLoginManager.do~action=edit~objId=${currentId}">
                                <img border="0" src="../ampTemplate/images/application_edit.png" />
                            </a>&nbsp;|&nbsp;
							<a href="javascript:removeSuspendRecord('${currentId}')">
                                <img border="0" src="../ampTemplate/images/trash_12.gif"/>
                            </a>
						</td>
					</tr>
				</logic:iterate>
			</table>
			
		</td>
	</tr>
	<tr><td><a href="/um/suspendLoginManager.do~action=add"><digi:trn>Add</digi:trn></a></td></tr>
</table>
<td width=20>&nbsp;</td>
    <td width=300 valign=top>
		<table align="center" cellpadding="0" cellspacing="0"
			width="300" border="0">
			<tr>
				<td>
					<!-- Other Links -->
					<table cellpadding="0" cellspacing="0" width="100">
						<tr>
							<td bgColor=#c9c9c7 class=box-title><digi:trn
									key="aim:otherLinks">
									<b style="font-weight: bold; font-size: 12px; padding-left:5px; color:#000000;"><digi:trn>Other links</digi:trn></b>
								</digi:trn></td>
							<td background="module/aim/images/corner-r.gif"
								height="17" width=17>&nbsp;</td>
						</tr>
					</table></td>
			</tr>
			<tr>
				<td bgColor=#ffffff>
					<table cellPadding=0 cellspacing="0" width="100%" class="inside">
						<tr>
							<td class="inside"><digi:img
									src="module/aim/images/arrow-014E86.gif" width="15"
									height="10" /> <digi:link module="aim" href="/admin.do">
									<digi:trn key="aim:AmpAdminHome">
								Admin Home
								</digi:trn>
								</digi:link></td>
						</tr>
						<tr>
							<td class="inside"><digi:img
									src="module/aim/images/arrow-014E86.gif" width="15"
									height="10" /> <digi:link module="aim"
									href="/workspaceManager.do~page=1">
									<digi:trn key="aim:WorkspaceManager">
								Workspace Manager
								</digi:trn>
								</digi:link></td>
						</tr>
						<tr>
							<td class="inside"><digi:img
									src="module/aim/images/arrow-014E86.gif" width="15"
									height="10" /> <digi:link module="aim"
									href="/../um/viewAllUsers.do~reset=true">
									<digi:trn>
										User Manager
									</digi:trn>
								</digi:link></td>
						</tr>
						<!-- end of other links -->
					</table></td>
			</tr>
	</table>
	</td>

<%--
        TODO
        There's a number of methods setStripsTable, setHoveredTable
        scattered all through the project. Need to do a refactoring and move these methods to one place
--%>
<script language="javascript">
    setStripsTable("dataTable", "tableEven", "tableOdd");
    setHoveredTable("dataTable", true);

    function setStripsTable(tableId, classOdd, classEven) {
        var tableElement = document.getElementById(tableId);
        if (tableElement) {
	        rows = tableElement.getElementsByTagName('tr');
	        for(var i = 0, n = rows.length; i < n; ++i) {
	            if(i%2 == 0)
	            rows[i].className = classEven;
	            else
	            rows[i].className = classOdd;
	        }
	        rows = null;
        }
    }

    function setHoveredTable(tableId, hasHeaders) {

        var tableElement = document.getElementById(tableId);
        if(tableElement) {
            var className = 'Hovered',
            pattern = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
            rows = tableElement.getElementsByTagName('tr');

            for (var i = 0, n = rows.length; i < n; ++i) {
                rows[i].onmouseover = function() {
                    this.className += ' ' + className;
                };

                rows[i].onmouseout = function() {
                    this.className = this.className.replace(pattern, ' ');
                };
            }
        }
        rows = null;
    }
</script>
