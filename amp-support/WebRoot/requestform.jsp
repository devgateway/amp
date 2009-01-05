<%@ taglib prefix="html" uri="/struts-tags"%>

<html>
<head>
<title><html:text name="%{getText('page.tittle')}"/></title>
<link href="Styles/common.css" rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"><style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style></head>
<body>

<html:form action="ShowRequestForm.action" validate="true">
	<table width="780" border="0" cellpadding="0" cellspacing="0" class="table">
      <tr>
        <td height="655">
          <!-- 
          <table width="680" border="0" align="center" cellpadding="0" cellspacing="0">
            <tr>
              <td class="Text">
               <br>
			  	<html:text name="%{getText('message.form.support_1')}" />
			  	<br>
			  	<html:text name="%{getText('message.form.support_2')}" />
			  	<br>
			  	<html:text name="%{getText('message.form.support_3')}" />
			  	<br>
				<html:text name="%{getText('message.form.support_4')}" />
			  </td>
            </tr>
          </table>
          -->
          <table width="500" border="0" align="center" cellpadding="2" cellspacing="1">
          <tr>
            <td height="23" colspan="3" align="left" class="textError">
            	<html:actionerror />            
          </td>
          </tr>
          <tr>
            <td width="8" align="left" valign="top" class="textError">*</td>
            <td width="194" align="right" class="Text">
            	<html:text name="%{getText('label.fulluser')}" />&nbsp;            
            </td>
            <td width="234"><html:textfield name="fullusername"/>
            </td>
          </tr>
          <tr>
            <td align="left" valign="top" class="textError">*</td>
            <td align="right" class="Text">
            	<html:text name="%{getText('label.email')}" />&nbsp;            
            </td>
            <td><html:textfield name="email"/>
            </td>
          </tr>
          <tr>
            <td align="left" valign="top" class="textError">*</td>
            <td align="right" class="Text">
            	<html:text name="%{getText('label.amp_login')}"/>&nbsp;</td>
            <td>
            	<html:textfield name="amplogin"/>
            </td>
          </tr>
          <tr>
            <td align="left" valign="top" class="textError">*</td>
            <td align="right" class="Text"><html:text
				name="%{getText('label.amp_password')}" />&nbsp;
			</td>
            <td>
            	<html:password name="amppassword"/>
            </td>
          </tr>
          <tr>
            <td align="left" valign="top" class="textError">*</td>
            <td align="right" class="Text"><html:text
				name="%{getText('label.browser')}"/>&nbsp;
              </td>
            <td>
            	<html:select name="browser" list="browserlist" listKey="name" listValue="name"/>
            </td>
          </tr>
          <tr>
            <td align="left" valign="top" class="textError">&nbsp;</td>
            <td align="right" class="Text">
            	<html:text name="%{getText('label.browser_version')}"/>&nbsp;            
            </td>
            <td>
            	<html:textfield name="browserversion" />            
            </td>
          </tr>
          <tr>
            <td align="left" valign="top" class="textError">*</td>
            <td align="right" class="Text">
            	<html:text name="%{getText('label.os')}"/>&nbsp;            
            </td>
            <td>
            	<html:select name="operatingsystem" list="os" listValue="name" listKey="name" /></td>
          </tr>
          <tr>
            <td align="left" valign="top" class="textError"></td>
            <td align="right" class="Text">
            	<html:text name="%{getText('label.module')}" />&nbsp;             
			</td>
             <td align="left">
              	<html:select multiple="true" list="mlst" 
            		listValue="name" 
            		name="modules" 
            		size="5" 
            		listKey="name"/>            
           	</td>
          </tr>
          <tr>
            <td align="left" valign="top" class="textError"></td>
            <td align="right" class="Text">
            	<html:text name="%{getText('label.version')}"/>&nbsp;           	
            </td>
            <td>
            	<html:select name="version" list="VLst" listValue="name" listKey="name"/>            
            </td>
          </tr>
          <tr>
            <td align="left" valign="top" class="textError">&nbsp;</td>
            <td align="right" class="Text">
            	<html:text name="%{getText('label.priority')}"/>&nbsp;
            </td>
            <td>
            	<html:select name="level" list="LstLevel"/>
            </td>
          </tr>
          <tr>
            <td align="left" valign="top" class="textError">*</td>
            <td align="right" class="Text">
            	<html:text name="%{getText('label.subject')}"/>&nbsp;</td>
            <td>
            	<html:textfield name="subject" />
            </td>
          </tr>
          <tr>
            <td height="136" align="left" valign="top" class="textError">*</td>
            <td height="136" align="right" valign="top" class="Text">
            	<html:text name="%{getText('label.description')}" />&nbsp;            
            </td>
            <td>
            	<html:textarea rows="10" cols="30" name="details" />            
            </td>
          </tr>
          <tr>
            <td align="left" valign="top" class="textError"></td>
            <td align="right" class="Text">
            	<html:text name="%{getText('label.mail.cc')}" />&nbsp;            
            </td>
            <td>
            	<html:textfield name="mailcc"/>            
            </td>
          </tr>
          <tr>
            <td height="23" colspan="3" align="right" valign="middle" class="Textk">
            	<html:text name="%{getText('message.mail.cc')}"/>&nbsp;            
            </td>
          </tr>
          <tr>
           <td height="35" colspan="3" align="center" valign="middle"><html:submit
				value="%{getText('label.send_request')}" action="saveRequest"/></td>
          </tr>
          <tr>
            <td colspan="3" align="center">&nbsp;</td>
          </tr>
          <tr>
            <td colspan="3" align="center"><span class="textError">*</span> 
            	<span class="Text">
              		<html:text name="%{getText('label.required_field')}" />
            	</span>            
            </td>
          </tr>
          <tr>
            <td colspan="3" align="center">&nbsp;</td>
          </tr>
          <tr>
            <td colspan="3" align="center">
            	<img src="Images/logo_mini.gif" width="91" height="32" longdesc="Development Gateway Foundation ">            
            </td>
          </tr>
        </table>
		<br>
		</td>
      </tr>
    </table>
</html:form>
</body>
</html>
