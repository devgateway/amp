<%@ taglib prefix="html" uri="/struts-tags"%>
<title><html:text name="%{getText('page.tittle')}"/></title>
<link href="Styles/common.css" rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<style>
.tablesheader{
	background-color:#a5bcf2;
	border:1px solido;
	border-color:#dbe5f1 ;
	font-size:10pt;
}
.tableEven {
	background-color:#dbe5f1;
	font-size:10pt;
	border:1px;
	padding:2px;
}

.tableOdd {
	background-color:#FFFFFF;
	font-size:10pt;!important
	border:1px;
	padding:2px;
}
 
.Hovered {
	background-color:#a5bcf2;
}

</style>
<script language="javascript">
function setStripsTable(tableId, classOdd, classEven) {
	var tableElement = document.getElementById(tableId);
	rows = tableElement.getElementsByTagName('tr');
	for(var i = 0, n = rows.length; i < n; ++i) {
		if(i%2 == 0)
			rows[i].className = classEven;
		else
			rows[i].className = classOdd;
	}
	rows = null;
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
function fillfields(tableId,index){
	var tableElement = document.getElementById(tableId);
	rows = tableElement.getElementsByTagName('tr');
	cols = rows[index];
	alert (cols);
}
</script>
</head>
<html>
<body>
<html:form action="Admin.action" validate="true">

<div style="width:200px;margin-bottom:10px" class="Admintitles">Admin Section</div>
<div style="width:100%;margin-bottom:10px;background-color: #D7D7D7" class="Admintitles">Countries</div>

<table border="0" cellspacing="0" cellpadding="0" class="tablesheader">
  <tr>
    <td width="80" align="center">Code</td>
    <td width="200" align="center">Country Name</td>
    <td width="260" align="center">E-Mail</td>
    <td width="70" align="center"></td>
  </tr>
 </table> 
 
 <table border="0" cellspacing="0" cellpadding="0" id="dataTable">
  <html:iterator value="Countries" id="country">
  <tr onclick="fillfields("dataTable",this.rowIndex);">
    <td width="80" align="center">${country.code}</td>
    <td width="200" align="center">${country.name}</td>
    <td width="260" align="left">${country.mail}</td>
    <td width="70" align="center">
    	<html:url id="url" action="admin" method="DeleteCountry" >
    		<html:param name="ccode">${country.code}</html:param>
    	</html:url>
    	<html:a href="%{url}" title="When you delete a record all related records are delete too">Delete</html:a>
    </td>
   </tr>
  </html:iterator>
  </table>
  <table border="0" cellspacing="0" cellpadding="0" class="tablesheader">
  <tr>
    <td width="80" align="center"><html:textfield  title="Unique Country Code" size="2" maxlength="3" name="countrycode" cssStyle=".fields"/></td>
    <td width="200" align="center"><html:textfield title="Country Name" size="20" name="contryname" cssStyle=".fields"/></td>
    <td width="260" align="left"><html:textfield  title="E-mail" size="35" name="countrymail" cssStyle=".fields"/></td>
    <td width="70" align="center">
    	<html:submit cssClass="Styles/common.css" cssStyle="FieldStyle" action="admin" method="addCountry" value="GO" name="addcountry" title="Insert or Update Country Information"/>
    </td>
  </tr>
</table>

<div style="width:100%;margin-bottom:10px;margin-top:10px;background-color: #D7D7D7" class="Admintitles">Users</div>
<table border="0" cellspacing="0" cellpadding="0" class="tablesheader">
  <tr>
    <td width="90" align="center">Password</td>
    <td width="160" align="center">Name</td>
    <td width="180" align="center">Country</td>
    <td width="80" align="center">Role</td>
    <td width="80" align="center"></td>
   </tr>
 </table>
 <table border="0" cellspacing="0" cellpadding="0" id="usertable">
  <html:iterator value="Users" id="user">
  <tr>
    <td width="90" align="center">${user.password}</td>
    <td width="160" align="center">${user.username}</td>
    <td width="180" align="center">${user.country.name}</td>
    <td width="80" align="center">
    	<html:if test="#user.role==1">
    		User
    	</html:if>
    	<html:if test="#user.role==2">
    		Admin
    	</html:if>
    </td>
    <td width="80" align="center">
    	<html:url id="url" action="admin" method="DeleteUser" >
    		<html:param name="id">${user.loginid}</html:param>
    	</html:url>
    	<html:a href="%{url}" title="When you delete a record all related records are delete too">Delete</html:a>
    </td>
   </tr>
  </html:iterator>
</table>
<table border="0" cellspacing="0" cellpadding="0" class="tablesheader">
  <tr>
    <td width="90" align="center"><html:textfield  title="" size="10" name="userpassword" cssStyle=".fields"/></td>
    <td width="160" align="center"><html:textfield title="" size="20" name="usernname" cssStyle=".fields"/></td>
    <td width="180" align="center">
    	<html:select name="userccode" listKey="code" listValue="name" list="countries" cssClass="Styles/common.css" cssStyle="FieldStyle" /> 
    </td>
    <td width="80" align="center">
     	<html:select name="userrole" listKey="roleid" listValue="name" list="roles" cssClass="Styles/common.css" cssStyle="FieldStyle" /> 
    </td>
    <td width="80" align="center">
    	<html:submit cssClass="Styles/common.css" cssStyle="FieldStyle" action="admin" method="addUser" value="GO" name="adduser" title="Insert or Update User Information"/>
    </td>
  </tr>
</table>

<div style="width:100%;margin-bottom:10px;margin-top:10px;background-color: #D7D7D7" class="Admintitles">AMP Modules</div>
<table border="0" cellspacing="0" cellpadding="0" class="tablesheader">
  <tr>
    <td width="250" align="center">Name English.</td>
    <td width="250" align="center">Name Spanish.</td>
    <td width="250" align="center">Name French.</td>
    <td width="80" align="center"></td>
  </tr>
 </table>
 <table border="0" cellspacing="0" cellpadding="0" id="modulestable">
 <html:iterator value="Modules" id="module">
  <tr>
    <td nowrap="nowrap" width="250" align="left">${module.name_en}</td>
    <td nowrap="nowrap" width="250" align="left">${module.name_es}</td>
    <td nowrap="nowrap" width="250" align="left">${module.name_fr}</td>
    <td width="80" align="center">
    	<html:url id="url" action="admin" method="DeleteModule" >
    		<html:param name="idmodule">${module.idmodule}</html:param>
    	</html:url>
    	<html:a href="%{url}" title="When you delete a record all related records are delete too">Delete</html:a>
    </td>
   </tr>
  </html:iterator>
</table>
<table border="0" cellspacing="0" cellpadding="0" class="tablesheader">
  <tr>
  	<td width="250" align="left">
  		<html:textfield  title="" size="35" name="enname" cssStyle=".fields"/>
  	</td>
  	<td width="250" align="left">
  		<html:textfield  title="" size="35" name="esname" cssStyle=".fields"/>
  	</td>
  	<td width="250" align="left">
  		<html:textfield  title="" size="35" name="frname" cssStyle=".fields"/>
  	<td width="80" align="center">
    	<html:submit cssClass="Styles/common.css" 
    		cssStyle="FieldStyle" 
    		action="admin" method="addModule" 
    		value="GO" name="addmodule" 
    		title="Insert or Update User Information"/>
  	</td> 
  </tr>
 </table> 
 
<div style="width:100%;margin-bottom:10px;margin-top:10px;background-color: #D7D7D7" class="Admintitles">AMP Versions</div>
<table border="0" cellspacing="0" cellpadding="0" class="tablesheader">
  <tr>
    <td width="150" align="center">Name</td>
    <td width="80" align="center"></td>
  </tr>
 </table>
 <table border="0" cellspacing="0" cellpadding="0" id="versiontable">
 <html:iterator value="Versions" id="version">
  <tr>
    <td nowrap="nowrap" width="150" align="left">${version.name}</td>
    <td width="80" align="center">
    	<html:url id="url" action="admin" method="DeleteVersion" >
    		<html:param name="idversion">${version.idversion}</html:param>
    	</html:url>
    	<html:a href="%{url}" title="When you delete a record all related records are delete too">Delete</html:a>
    </td>
   </tr>
  </html:iterator>
</table>
<table border="0" cellspacing="0" cellpadding="0" class="tablesheader">
  <tr>
  	<td width="150" align="left">
  		<html:textfield  title="" size="20" name="vname" cssStyle=".fields"/>
  	</td>
  	<td width="80" align="center">
    	<html:submit cssClass="Styles/common.css" 
    		cssStyle="FieldStyle" 
    		action="admin" method="addVersion" 
    		value="GO" name="addversion" 
    		title="Insert or Update User Information"/>
  	</td> 
  </tr>
 </table> 
 
 <div style="width:100%;margin-bottom:10px;margin-top:10px;background-color: #D7D7D7" class="Admintitles">Operating System</div>
<table border="0" cellspacing="0" cellpadding="0" class="tablesheader">
  <tr>
    <td width="150" align="center">Name</td>
    <td width="80" align="center"></td>
  </tr>
 </table>
 <table border="0" cellspacing="0" cellpadding="0" id="ostable">
 <html:iterator value="os" id="ops">
  <tr>
    <td nowrap="nowrap" width="150" align="left">${ops.name}</td>
    <td width="80" align="center">
    	<html:url id="url" action="admin" method="DeleteOs" >
    		<html:param name="idos">${ops.id}</html:param>
    	</html:url>
    	<html:a href="%{url}" title="When you delete a record all related records are delete too">Delete</html:a>
    </td>
   </tr>
  </html:iterator>
</table>
<table border="0" cellspacing="0" cellpadding="0" class="tablesheader">
  <tr>
  	<td width="150" align="left">
  		<html:textfield  title="" size="20" name="osname" cssStyle=".fields"/>
  	</td>
  	<td width="80" align="center">
    	<html:submit cssClass="Styles/common.css" 
    		cssStyle="FieldStyle" 
    		action="admin" method="addOs" 
    		value="GO" name="addos" 
    		title="Insert or Update User Information"/>
  	</td> 
  </tr>
 </table> 
 
</html:form>
</body>
</html>
<script language="javascript">
	setStripsTable("dataTable", "tableEven", "tableOdd");
	setHoveredTable("dataTable", false);

	setStripsTable("usertable", "tableEven", "tableOdd");
	setHoveredTable("usertable", false)
	
	setStripsTable("modulestable", "tableEven", "tableOdd");
	setHoveredTable("modulestable", false)
	
	setStripsTable("versiontable", "tableEven", "tableOdd");
	setHoveredTable("versiontable", false)
	
	setStripsTable("dataTable", "tableEven", "tableOdd");
	setHoveredTable("dataTable", false);

	setStripsTable("usertable", "tableEven", "tableOdd");
	setHoveredTable("usertable", false)
	
	setStripsTable("modulestable", "tableEven", "tableOdd");
	setHoveredTable("modulestable", false)
	
	setStripsTable("ostable", "tableEven", "tableOdd");
	setHoveredTable("ostable", false)
</script>