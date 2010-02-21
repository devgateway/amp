<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<digi:instance property="aimCurrencyForm" />

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/calendar.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>



<digi:form action="/saveCurrency.do" type="aimCurrencyForm" name="aimCurrencyFormPopin">

  <input type="hidden" name="selectedDate">
  <html:hidden property="id"/>
  <html:hidden property="closeFlag"/>

  <table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
    <tr>
      <td align=left vAlign=top>
        <table bgcolor=#aaaaaa cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
          <tr bgcolor="#aaaaaa">
            <td vAlign="center" width="100%" align ="center" class="textalb" height="20">
              <digi:trn key="aim:currencyRateEditor">Currency Editor</digi:trn>
            </td></tr>
            <tr>
              <td vAlign="center" width="100%" align ="center" height="20">
                <c:if test="${!empty aimCurrencyForm.errors}">
                  <table>
                    <c:forEach var="ms" items="${aimCurrencyForm.errors}">
                      <tr>
                        <td style="color:red;">
                        ${ms}
                        </td>
                      </tr>
                    </c:forEach>
                  </table>
                </c:if>
              </td></tr>
              <tr>
                <td align="center">
                  <table border="0" cellpadding="2" cellspacing="1" width="100%">
                    <tr bgcolor="#f4f4f2">
                      <td align="right" valign="middle" width="50%">
                        <FONT color=red>*</FONT>
                        <digi:trn key="aim:currencyCode">Currency Code</digi:trn>&nbsp;
                      </td>
                      <td align="left" valign="middle">
                        <html:text property="currencyCode" styleClass="inp-text" size="7"/>
                      </td>
                    </tr>
                    <tr bgcolor="#f4f4f2">
                      <td align="right" valign="middle" width="50%">
                        <FONT color=red>*</FONT>
                        <digi:trn key="aim:currencyName">Currency Name</digi:trn>&nbsp;
                      </td>
                      <td align="left" valign="middle">
                        <html:text property="currencyName" styleClass="inp-text" size="20"/>
                      </td>
                    </tr>
                    <tr bgcolor="#f4f4f2">
                      <td align="right" valign="middle" width="50%">
                        <FONT color=red>*</FONT>
                        <digi:trn key="aim:country">Country</digi:trn>&nbsp;
                      </td>
                      <td align="left" valign="middle">
                        <html:select property="countryId" styleId="lstCountry" styleClass="inp-text" style="width:250px;">
                          <html:option value="-1"><digi:trn key="aim:selectCountry">Select a country</digi:trn></html:option>
                          <c:if test="${!empty aimCurrencyForm.countries}">
                            <c:forEach var="country" items="${aimCurrencyForm.countries}">
                              <html:option value="${country.id}"><digi:trn>${country.name}</digi:trn></html:option>
                            </c:forEach>
                          </c:if>
                        </html:select>
                      </td>
                    </tr>
                    <%--
                    <c:if test="${aimCurrencyForm.id == -1}">
                      <tr bgcolor="#f4f4f2">
                        <td align="right" valign="middle" width="50%">
                          <FONT color=red>*</FONT>
                          <digi:trn key="aim:exchangeRateDate">Exchange rate date</digi:trn>&nbsp;
                        </td>
                        <td align="left" valign="middle">
                          <table cellPadding=0 cellSpacing=0>
                            <tr>
                              <td>
                                <html:text property="exchangeRateDate" size="10"
                                styleClass="inp-text" readonly="true" styleId="exchangeRateDate"/>
                              </td>
                              <td align="left" vAlign="center">&nbsp;
                                <a href="javascript:calendar('exchangeRateDate')">
                                  <img src="../ampTemplate/images/show-calendar.gif" border=0></a>
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <tr bgcolor="#f4f4f2">
                        <td align="right" valign="middle" width="50%">
                          <FONT color=red>*</FONT>
                          <digi:trn key="aim:exchangeRateInUSD">Exchange rate (in USD)</digi:trn>&nbsp;
                        </td>
                        <td align="left" valign="middle">
                          <html:text property="exchangeRate" styleClass="amt" size="7"/>
                          <FONT color=red>
                            <digi:trn key="aim:USD">USD</digi:trn></FONT>
                        </td>
                      </tr>
                    </c:if>
                    --%>
                    <tr bgcolor="#ffffff">
                      <td colspan="2">
                        <table width="100%" cellpadding="3" cellspacing="3" border="0">
                          <tr>
                            <td align="right">
                              <c:set var="trnSaveBtn">
                                <digi:trn key="aim:btnSave">Save</digi:trn>&nbsp;
                              </c:set>
                              <input type="button" value="${trnSaveBtn}" onclick="return saveCurrency()" class="dr-menu">
                            </td>
                            <td align="left">
                              <c:set var="trnCloseBtn">
                                <digi:trn key="aim:btnClose">Close</digi:trn>&nbsp;
                              </c:set>
                              <input type="button" value="${trnCloseBtn}" onclick="return closePopup()" class="dr-menu">
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
              <tr>
                <td>&nbsp;&nbsp;
                  <digi:trn key="um:allMarkedRequiredField">All fields marked with an <FONT color=red><B><BIG>*</BIG>
</B></FONT> are required.</digi:trn>
                </td>
              </tr>
        </table>
      </td>
            </tr>
  </table>
  <script language="javaScript">
  document.aimCurrencyForm.currencyCode.focus();
  if (document.aimCurrencyForm.closeFlag.value == "true") {
    closePopup();
  }
  </script>
</digi:form>
