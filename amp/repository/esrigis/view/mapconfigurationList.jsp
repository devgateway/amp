<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<style>
.contentbox_border {
	border: 	1px solid #666666;
	width: 		900px;
	background-color: #f4f4f2;
}
.tableEven {
	background-color:#dbe5f1;
	font-size:8pt;
	padding:2px;
}
.tableOdd {
	background-color:#FFFFFF;
	font-size:8pt;
!important padding:2px;
}
.Hovered {
	background-color:#a5bcf2;
}
</style>
<script language="javascript">

function resetFields() {
	var keyword = document.getElementsByName("keyword")[0];
	var queryType = document.getElementsByName("queryType")[0];
	keyword.value ="";
	queryType.value = -1;
	document.getElementById("resultTable").innerHTML="";
	keyword.focus();
}

function popup(mylink, windowname)
{
if (! window.focus)return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;
window.open(href, windowname,'channelmode=no,directories=no,menubar=no,resizable=yes,status=no,toolbar=no,scrollbars=yes,location=yes');
return false;
}

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
	if(tableElement){
    var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        rows      = tableElement.getElementsByTagName('tr');

		for(var i = 0, n = rows.length; i < n; ++i) {
			rows[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			rows[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		rows = null;
	}
	


}

$(document).ready(function(){
	try
	{
		setStripsTable("dataTable", "tableEven", "tableOdd");
		setHoveredTable("dataTable", true);
	}
	catch(e) {}
});

</script>
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=1000 align=center>
  <tr>
    <td width=14>&nbsp;</td>
    <td align=left valign="top" width=750>
	<h1 class="admintitle" style="text-align:left;">Map Configuration</h1>
	<table cellPadding=5 cellspacing="0" width="1000">
        <tr>
          <td height=33 colspan="2">
          	<span class=crumb>
	            <c:set var="translation">
	              <digi:trn>Click here to goto Admin Home</digi:trn>
	            </c:set>
	            <digi:link href="/admin.do" styleClass="comment" title="${translation}" contextPath="/aim">
	              <digi:trn>Admin Home</digi:trn>
	            </digi:link>
	            &nbsp;&gt;&nbsp;
	            <digi:trn>Map Configuration</digi:trn>
            </span>
          </td>
        </tr>
        <!--<tr>
          <td height=16 valign="center" colspan="2"><span class=subtitle-blue>
            <digi:trn>List of Public View Content</digi:trn>
            </span> </td>
        </tr>-->
        <tr>
          <td noWrap width=570 vAlign="top"><table border="0" width="100%">
              <tr>
                <td align="center">
                  <table width="100%" id="dataTable" cellpadding="4" class="inside"> 
                    <tr>
                      <td bgcolor="#c7d4db" align="center" width="10%" class="inside"><strong style="font-size:12px;"><digi:trn>Indicator/Base</digi:trn></strong></td>
                      <td bgcolor="#c7d4db" align="center" width="10%" class="inside"><strong style="font-size:12px;"><digi:trn>Type</digi:trn></strong></td>
                      <td bgcolor="#c7d4db" align="center" width="60%" class="inside"><strong style="font-size:12px;"><digi:trn>URL</digi:trn></strong></td>
                      <!-- <td bgcolor="#c7d4db" align="center" width="30%" class="inside"><strong style="font-size:12px;"><digi:trn>Graphic Type</digi:trn></strong></td> -->
                      <td bgcolor="#c7d4db" align="center" width="20%" class="inside"><strong><digi:trn>Action</digi:trn></strong></td>
                    </tr>
                    <c:if test="${fn:length(requestScope.mapList) eq 0}">
                      <tr bgColor=#f4f4f2>
                        <td align="center" class="inside"><digi:trn>No map configuration found</digi:trn>
                        </td>
						<td class="inside">&nbsp;</td>
                        <td class="inside">&nbsp;</td>
						<td class="inside">&nbsp;</td>
                      </tr>
                    </c:if>
                    <c:if test="${fn:length(requestScope.mapList) gt 0}">
                    <c:set var="confirmationTrn">
                    	<digi:trn jsFriendly="true">Are you sure?</digi:trn>
                    </c:set>
                      <c:forEach  var="mapConfig" items="${requestScope.mapList}">
                        <tr bgColor=#f4f4f2>
                          <td align="center" class="inside"><digi:trn>${mapConfig.mapSubTypeName}</digi:trn></td>
                          <td align="center" class="inside"> 
                          <c:if test="${mapConfig.mapSubType == 2}">
                          	<digi:trn>${mapConfig.mapTypeName}</digi:trn>: ${mapConfig.configName}
                          </c:if>
                          <c:if test="${mapConfig.mapSubType != 2}">
                          	<digi:trn>${mapConfig.mapTypeName}</digi:trn>
                          </c:if>
                          </td>
                          <td align="left" class="inside"> ${mapConfig.mapUrl} </td>
                          <td align="center" class="inside">
                          	<digi:link href="/MapsConfiguration.do?action=edit&id=${mapConfig.id}">
                              <img src="/TEMPLATE/ampTemplate/imagesSource/common/application_edit.png" border="0" title="<digi:trn>Edit</digi:trn>"/>
                            </digi:link>
                          	<digi:link href="/MapsConfiguration.do?action=delete&id=${mapConfig.id}" onclick="return confirm('${confirmationTrn}');">
                              <img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border="0" title="<digi:trn>Delete</digi:trn>"/>
                            </digi:link>
                          </td>
                        </tr>
                      </c:forEach>
                    </c:if>
                  </table>
                  <br />
                  <br />
                </td>
              </tr>
            </table></td>
          <td noWrap width=180 vAlign="top"><table align="center" cellpadding="0" cellspacing="0" width="90%" border="0">
              <tr>
                <td><!-- Other Links -->
                  <table cellpadding="0" cellspacing="0" width="100">
                    <tr>
                      <td bgColor=#c9c9c7 class=box-title><b style="font-size:12px; padding-left:5px;"><digi:trn key="aim:otherLinks">Other links</digi:trn></b>
                      </td>
                      <td background="module/aim/images/corner-r.gif" height="17" width=17></td>
                    </tr>
                  </table></td>
              </tr>
              <tr>
                <td bgColor=#ffffff class=box-border><table cellPadding=5 cellspacing="1" width="100%" class="inside">
                	<tr>
                      <td class="inside"><digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
                      </td>
                      <td class="inside" width=100%>
                      	<c:set var="translation">
								<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
							</c:set> 
							<a href="/admin.do" title="${translation}"><digi:trn key="aim:AmpAdminHome">
									Admin Home
									</digi:trn></a>
                      </td>
                    </tr>
                    <tr>
                      <td class="inside"><digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
                      </td>
                      <td class="inside" width=100%>
                      	<digi:link href="/MapsConfiguration.do?action=add&reset=true">
                          <digi:trn>Add new map configuration</digi:trn>
                        </digi:link>
                      </td>
                    </tr>
                  </table></td>
              </tr>
            </table></td>
        </tr>
      </table></td>
  </tr>
</table>
