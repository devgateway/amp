<%@ taglib prefix="html" uri="/struts-tags"%>

<html>
<head>
<title><html:text name="%{getText('page.tittle')}"/></title>
<link href="Styles/common.css" rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"><style type="text/css">
<!--
body {
	margin-top: 0px;
}
-->
</style></head>
<body>
<html:form action="login.action" validate="true">
	<br>
	<br>
	<br>
	<table width="343" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td width="285">
        	<img src="Images/logo.gif" alt="AMP Support" width="229" height="85" hspace="0" vspace="0"></td>
      </tr>
    </table>
    <br>
    <table width="343" border="0" align="center" cellpadding="1" cellspacing="1">
	<tr>
        <td colspan="3" align="left" class="textError">
        	<html:actionerror/>        
        </td>
      </tr>
      <tr>
        <td width="39" align="right" class="textError"> * </td>
        <td width="126" align="right" class="Text">
        	<html:text name="%{getText('label.username')}"/>&nbsp; 
        </td>
        <td width="174">
        	<html:textfield name="username" cssStyle=".fields"/>        
        </td>
      </tr>
      <tr>
        <td align="right" class="textError"> * </td>
        <td align="right" class="Text"><html:text name="%{getText('label.password')}"/>
          &nbsp; </td>
        <td>
        	<html:password name="password" cssClass="Styles/common.css" cssStyle="FieldStyle" />        
        </td>
      </tr>
      <tr>
        <td align="right" class="textError"> * </td>
        <td align="right" class="Text"><html:text name="%{getText('label.country')}"/>
          &nbsp; </td>
        <td>
        	<html:select name="countrycode" listKey="code" listValue="name" list="countries" cssClass="Styles/common.css" cssStyle="FieldStyle" />        
        </td>
      </tr>
      <tr>
        <td colspan="2" align="right" class="Text"><html:text name="%{getText('label.language')}"/>
          &nbsp;			
        <td>
        	<html:select name="rlocale" listKey="code" listValue="name" list="languages" cssClass="Styles/common.css" cssStyle="FieldStyle" />        
        </td>
      </tr>
      <tr>
        <td height="42" colspan="3" align="center">
        	<html:submit cssClass="Styles/common.css" cssStyle="FieldStyle" action="postloging" value="%{getText('label.login')}"name="loging" />        
        </td>
      </tr>
      <tr>
        <td colspan="3" align="center">
			<span class="textError">*</span>
			<span class="Text">
				<html:text name="%{getText('label.required_field')}" />
			</span>		
		</td>
      </tr>
  </table>
</html:form>
</body>
</html>

