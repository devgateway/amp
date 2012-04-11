<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<%@page import="org.digijava.module.aim.helper.FormatHelper"%>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp"  />
</script>
<jsp:include page="scripts/newCalendar.jsp"  />

<script language="JavaScript" type="text/javascript">
	function addPropFunding() {
	var fna=aimEditActivityForm.funAmount.value;
      var fnd=aimEditActivityForm.funDate.value;
      if(fna==""){
        <c:set var="message">
        <digi:trn key="aim:enterAmount">Please enter amount</digi:trn>
        </c:set>
        alert("${message}");
        return false;
      }else if(!checkAmountUsingSymbol(fna,"<%=FormatHelper.getDecimalSymbol()%>","<%=FormatHelper.getGroupSymbol()%>")){
        <c:set var="message">
        <digi:trn key="aim:invalidAmountValue">Invalid amount value</digi:trn>
        </c:set>
        alert("${message}");
        return false;
      }
      if(fnd==""){
        <c:set var="message">
        <digi:trn key="aim:selectDate">Please select date</digi:trn>
        </c:set>
        alert("${message}");
        return false;
      }
      <digi:context name="fundAdded" property="context/module/moduleinstance/addProposedFunding.do" />;
      document.aimEditActivityForm.action = "<%= fundAdded %>";
      document.aimEditActivityForm.target=window.opener.name;
      document.aimEditActivityForm.submit();
      window.close();
      return true;
	}
    function load(){
      return true;
    }
    function unload(){
      return true;
    }
    
   function checkAmountUsingSymbol(amount,decimalSymbol,groupSymbol){
	var validChars= "0123456789"+decimalSymbol+groupSymbol;
	for (i = 0;  i < amount.length;  i++) {
		var ch = amount.charAt(i);
		if (validChars.indexOf(ch)==-1){
			return false;
			break
		}
	}
		return true;
}
   
   var enterBinder	= new EnterHitBinder('addPropFundingBtn');
</script>
<digi:instance property="aimEditActivityForm" />
<digi:form action="/addProposedFunding.do?edit=true" method="post">
  <input type="hidden" name="edit" value="true">
  	<c:set var="translation">
		<digi:trn key="aim:currencieswithexchange">Only currencies having exchange rate are listed here</digi:trn>
	</c:set>
  <table width="100%" border="0" cellspacing="2" cellpadding="2" align="center" class=box-border-nopadding>
    <tr>
      <td>
		<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
        <FONT color=blue><B><BIG>*</BIG>
          <digi:trn key="aim:pleaseEnterTheAmountInThousands">
          Please enter amount in thousands (000)
          </digi:trn>
          </B>
          </FONT>
		</gs:test>
      </td>
    </tr>

    <tr>
      <td width="100%" vAlign="top">
        <table width="100%" cellpadding="0" cellspacing="1" vAlign="top" align="left" bgcolor="#006699">
          <tr>
            <td>
              <table width="100%" cellpadding="0" cellspacing="0">
                <tr>
                  <td width="100%" bgcolor="#006699" class="textalb" height="20" align="center">
                    <a title="<digi:trn key="aim:Commitmentsmade">A firm obligation expressed in writing and backed by the necessary funds, undertaken by an official donor to provide specified assistance to a recipient country</digi:trn>"><digi:trn key="aim:commitments">Commitments</digi:trn></a>
                  </td>
                </tr>
                <tr>
                  <td>
                    <table width="100%" border="0" bgcolor="#f4f4f2" cellspacing="1" cellpadding="0" class=box-border-nopadding>
                      <tr bgcolor="#003366" class="textalb">
                        <td align="center" valign="middle" width="75">
                          <b><font color="white"><digi:trn key="aim:PlannedFIE">Planned</digi:trn></font></b>                        
                         </td>
                        <td align="center" valign="middle" width="100">
                          <b><font color="white"><digi:trn key="aim:AmountFIE">Amount</digi:trn></font></b>
                        </td>
                        <td align="center" valign="middle" width="100">
                          <b><font color="white"><digi:trn key="aim:CurrencyFIE">Currency</digi:trn></font></b>
                          <img src= "../ampTemplate/images/help.gif" border="0" align="absmiddle" title="${translation}" />
                        </td>
                        <field:display name="Proposed Project Date" feature="Proposed Project Cost">
                        <td align="center" valign="middle" width="120" colspan="2">
                          <b><font color="white"><digi:trn key="aim:PlannedCommitmentDate">Planned Commitment Date</digi:trn></font></b>
                        </td>
                        </field:display>
                      </tr>
                      <tr>
                        <td valign="center" align="center">
                        <digi:trn key="aim:PlannedFIE">Planned</digi:trn>
                        </td>
                        <td valign="center" align="center">
                          <html:text property="funding.proProjCost.funAmount" styleId="funAmount" style="width:100px;"/>
                        </td>
                        <td valign="center" align="center">
                          	<html:select property="funding.proProjCost.currencyCode" styleClass="inp-text">
                            	<html:optionsCollection name="aimEditActivityForm" property="funding.validcurrencies" value="currencyCode" label="currencyName" style="width:100%;"/>
                        	</html:select>
                        </td>
                        <field:display name="Proposed Project Date" feature="Proposed Project Cost">
	                        <td valign="center" align="center">
	                          <html:text property="funding.proProjCost.funDate" styleId="funDate"  style="width:100px;"/>
	                        </td>
	                        <td valign="center" align="center">
	            				<a id="date1" href='javascript:pickDateByIdDxDy("date1","funDate",210,30)'>
									<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
								</a>
	                        </td>
                        </field:display>            
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
    <td width="100%" vAlign="top">
      <table width="100%" cellpadding="0" cellspacing="0">
        <tr>
          <td align="center">
            <table cellPadding=3>
              <tr>
                <td>
                <c:set var="translationSave"><digi:trn key="btn:addRegionalFundingSave">Save</digi:trn></c:set>
				<c:set var="translationReset"><digi:trn key="btn:addRegionalFundingReset">Reset</digi:trn></c:set>
				<c:set var="translationClose"><digi:trn key="btn:addRegionalFundingClose">Close</digi:trn></c:set>
                  <input type="button" value="${translationSave}" class="inp-text" onclick="addPropFunding();" id="addPropFundingBtn">
                </td>
                <td>
                  <input type="reset" value="${translationReset}" class="inp-text">
                </td>
                <td>
                  <input type="button" value="${translationClose}" class="inp-text" onclick="window.close();">
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
     </td>
    </tr>
  </table>
</digi:form>
