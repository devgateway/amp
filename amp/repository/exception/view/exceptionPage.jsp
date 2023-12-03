<%@taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@taglib uri="/taglib/struts-html" prefix="html"%>
<%@taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<html>
<body bgcolor="#ffffff">
<digi:form method="post" action="/exceptionReport.do" ampModule="exception">
  <html:hidden property="exceptionInfo.sourceURL"/>
  <html:hidden property="exceptionInfo.exceptionCode"/>
  <html:hidden property="exceptionInfo.errorMessage"/>
  <html:hidden property="exceptionInfo.siteId"/>
  <html:hidden property="exceptionInfo.siteKey"/>
  <html:hidden property="exceptionInfo.siteName"/>
  <html:hidden property="exceptionInfo.stackTrace"/>
  <html:hidden property="name"/>
  <html:hidden property="email"/>
  <table width="100%">
    <tr>
      <td align="center">
        <span style="font-size:20px">
          <digi:trn key="exception:errorScreenCaption">We are sorry. Some error occured while server was processing this page</digi:trn>
        </span>
      </td>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>
        <digi:trn key="exception:errorScreenHint">If you want to report this problem, please, press REPORT button in the end of the following form</digi:trn>
      </td>
    </tr>
    <tr>
      <td>
        <digi:trn key="exception:goToMain">Go to the main page</digi:trn>
      </td>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td width="100%">
        <fieldset>
          <legend>
            <digi:trn key="exception:exceptionDetails">Exception details</digi:trn>
          </legend>
          <table width="100%" border="0">
            <tr>
              <td>Error Message:</td>
              <td>
                <b>
                  <font color="red">
                    <bean:write name="exceptionReportForm" property="exceptionInfo.errorMessage"/>
                  </font>
                </b>
              </td>
            </tr>
            <tr>
              <td>Status code:</td>
              <td><bean:write name="exceptionReportForm" property="exceptionInfo.exceptionCode"/>              </td>
    </tr>
    <tr>
      <td>Site:</td>
      <td>
        <bean:write name="exceptionReportForm" property="exceptionInfo.siteName"/>
        (#
        <bean:write name="exceptionReportForm" property="exceptionInfo.siteId"/>
        /
        <bean:write name="exceptionReportForm" property="exceptionInfo.siteKey"/>
        )
      </td>
    </tr>
    <tr>
      <td>Module instance:</td>
      <td>
        <bean:write name="exceptionReportForm" property="exceptionInfo.moduleName"/>
        :
        <bean:write name="exceptionReportForm" property="exceptionInfo.instanceName"/>
      </td>
    </tr>
    <tr>
      <td width="100%" colspan="2">&nbsp;</td>
    </tr>
<logic:notEmpty name="exceptionReportForm" property="exceptionInfo.stackTrace">
    <tr>
      <td width="100%" colspan="2">
        <fieldset>
          <legend>
            <digi:trn key="exception:stackTrace">Stack trace</digi:trn>
          </legend>
          <table width="100%">
            <tr>
              <td>
                <html:textarea style="width:100%;height:350px" readonly="true" name="exceptionReportForm" property="exceptionInfo.stackTrace"/>
              </td>
            </tr>
          </table>
        </fieldset>
      </td>
    </tr>
</logic:notEmpty>
  </table>
</fieldset></td></tr>  <tr>
    <td width="100%">&nbsp;</td>
  </tr>
  <tr>
    <td width="310px">
      <fieldset>
        <legend>
          <digi:trn key="exception:exceptionReport">Exception report</digi:trn>
        </legend>
        <table width="310px">
          <tr>
            <td valign="top">
              <digi:trn key="exception:name">Name:</digi:trn>
            </td>
            <td>
              <html:text property="name" size="30"/>
            </td>
          </tr>
          <tr>
            <td valign="top">
              <digi:trn key="exception:email">Email:</digi:trn>
            </td>
            <td>
              <html:text property="email" size="30"/>
            </td>
          </tr>
          <tr>
            <td valign="top">
              <digi:trn key="exception:issueId">Issue ID:</digi:trn>
            </td>
            <td>
              <html:text property="issueId" size="30"/>
            </td>
          </tr>
          <tr>
            <td valign="top">
              <digi:trn key="exception:comment">Comment:</digi:trn>
            </td>
            <td>
              <html:textarea style="width:370px;height:200px" property="message"/>
            </td>
          </tr>
          <tr>
            <td colspan="2" align="right">
              <html:submit value="Report"/>
            </td>
          </tr>
        </table>
      </fieldset>
    </td>
  </tr>
</table></digi:form>
</body>
</html>
