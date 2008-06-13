<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<logic:notPresent name="currentMember" scope="session">

<table cellpadding="0" cellspacing="0" width="100%" style="background-image:url(module/aim/images/bg-header-1.gif);vertical-align:top;" border=0>
  <tr>
    <td valign="top">
      <%--<html:errors/>--%>
      <table cellpadding="0" cellSpacing="0">
        <tr>
          <td width="10">
          &nbsp;&nbsp;&nbsp;
          </td>
            <td align="left">
              <digi:link href="/reportsPublicView.do" styleClass="header">
                <digi:trn key="aim:publicPortfolio">Public Portfolio</digi:trn>
              </digi:link>
            </td>
            <td align="right">&nbsp;
            </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</logic:notPresent>
