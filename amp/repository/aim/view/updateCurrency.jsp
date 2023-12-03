<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<digi:instance property="aimCurrencyForm" />

<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/calendar.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/common.js"/>"></script>

<script language="JavaScript">

function validate() {
  if (isEmpty(document.aimCurrencyForm.currencyCode.value) ||
  checkCode(document.aimCurrencyForm.currencyCode)== false) {
    alert("Invalid currency code entered");
    document.aimCurrencyForm.currencyCode.focus();
    return false;
  }
  if (isEmpty(document.aimCurrencyForm.currencyName.value) ||
  checkName(document.aimCurrencyForm.currencyName.value)== false) {
    alert("Invalid currency name entered");
    document.aimCurrencyForm.currencyName.focus();
    return false;
  }
  var cn=document.getElementById("lstCountry");
  if(cn!=null){
    if(cn.value==-1){
      alert("Please select country");
      cn.focus();
      return false;
    }
  }
  return true;
}

function saveCurrency() {
  var valid = validate();
  if (valid != false) {
    <digi:context name="back" property="context/ampModule/moduleinstance/saveCurrency.do" />
    document.aimCurrencyForm.action = "<%= back %>";
    document.aimCurrencyForm.submit();
  }
  return valid;

}

function checkRate(val){
  if(val.match("[^0-9.,]")){
    return false;
  }
  return true;
}

function checkName(val){
  if(val.match("[^a-zA-ZÁ-ÿ ]")){
    return false;
  }
  return true;
}

function checkCode(input){
	var val = input.value.toUpperCase();
	
  if(val.match("[^A-Z]")){
    return false;
  }else if(val.length>3){
    return false;
  }
  input.value = val; 
  return true;
}

function closePopup() {
  <digi:context name="back" property="context/ampModule/moduleinstance/currencyManager.do" />
  document.aimCurrencyForm.action = "<%= back %>";
  document.aimCurrencyForm.target = window.opener.name;
  document.aimCurrencyForm.submit();
  window.close();
}

function load() {
  document.aimCurrencyForm.currencyCode.focus();
  if (document.aimCurrencyForm.closeFlag.value == "true") {
    closePopup();
  }
}

function unload() {
}

</script>


<digi:form action="/saveCurrency.do">

  <input type="hidden" name="selectedDate">
  <html:hidden property="id"/>
  <html:hidden property="closeFlag"/>

  <table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
    <tr>
      <td align=left valign="top">
        <table bgcolor=#aaaaaa cellpadding="0" cellspacing="0" width="100%" class=box-border-nopadding>
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
                        <digi:trn key="aim:country">Administrative Level 0</digi:trn>&nbsp;
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
                          <table cellpadding="0" cellspacing="0">
                            <tr>
                              <td>
                                <html:text property="exchangeRateDate" size="10"
                                styleClass="inp-text" readonly="true" styleId="exchangeRateDate"/>
                              </td>
                              <td align="left" vAlign="center">&nbsp;
                                <a href="javascript:calendar('exchangeRateDate')">
                                  <img src="../ampTemplate/images/show-calendar.gif" border="0"></a>
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
                    <digi:trn>All fields marked with an </digi:trn><FONT color=red><B><BIG>*</BIG></B></FONT><digi:trn> are required. </digi:trn>
       
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
