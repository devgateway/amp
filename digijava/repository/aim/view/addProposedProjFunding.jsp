<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/calendar.js"/>"></script>
<script language="JavaScript" type="text/javascript">
	function addPropFunding() {
      var fna=aimEditActivityForm.funAmount.value;
      if(fna==""){
        alert("Please enter amount");
        return false;
      }else if(fna.match("[^0-9]")){
        alert("Invalid amount value");
        return false;
      }
      if(funDate.value==""){
        alert("Please select date");
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
</script>
<digi:instance property="aimEditActivityForm" />
<digi:form action="/addProposedFunding.do?edit=true" method="post">
  <input type="hidden" name="edit" value="true">
  <table width="100%" border="0" cellspacing="2" cellpadding="2" align="center" class=box-border-nopadding>
    <tr>
      <td>
        <FONT color=blue><B><BIG>*</BIG>
          <digi:trn key="aim:pleaseEnterTheAmountInThousands">
          Please enter amount in thousands (000)
          </digi:trn>
      </td>
    </tr>

    <tr>
      <td width="100%" vAlign="top">
        <table width="100%" cellpadding=0 cellspacing=1 vAlign="top" align="left" bgcolor="#006699">
          <tr>
            <td>
              <table width="100%" cellpadding=0 cellspacing=0>
                <tr>
                  <td width="100%" bgcolor="#006699" class="textalb" height="20" align="center">
                    <a title="<digi:trn key="aim:Commitmentsmade">A firm obligation expressed in writing and backed by the necessary funds, undertaken by an official donor to provide specified assistance to a recipient country</digi:trn>">Commitments</a>
                  </td>
                </tr>
                <tr>
                  <td>
                    <table width="100%" border="0" bgcolor="#f4f4f2" cellspacing="1" cellpadding="0" class=box-border-nopadding>
                      <tr bgcolor="#003366" class="textalb">
                        <td align="center" valign="middle" width="75">
                          <b><font color="white">Planned</font></b>
                        </td>
                        <td align="center" valign="middle" width="120">
                          <b><font color="white">Amount</font></b>
                        </td>
                        <td align="center" valign="middle" width="120">
                          <b><font color="white">Currency</font></b>
                        </td>
                        <td align="center" valign="middle" width="120" colspan="2">
                          <b><font color="white">Planned<br>Commitment Date</font></b>
                        </td>
                      </tr>
                      <tr>
                        <td valign="center" align="center">
                        Planned
                        </td>
                        <td valign="center" align="center">
                          <html:text property="proProjCost.funAmount" styleId="funAmount" style="width:100%;"/>
                        </td>
                        <td valign="center" align="center">
                          <html:select property="proProjCost.currencyCode" styleClass="inp-text">
                            <html:optionsCollection name="aimEditActivityForm" property="currencies" value="currencyCode" label="currencyName" style="width:100%;"/>
                          </html:select>
                        </td>
                        <td valign="center" align="center">
                          <html:text property="proProjCost.funDate" styleId="funDate" readonly="true" style="width:100%;"/>
                        </td>
                        <td valign="center" align="center">
                          <a href='javascript:calendar("funDate")'><img  align="right" src="../ampTemplate/images/show-calendar.gif" border="0"></a>
                        </td>
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
      <table width="100%" cellpadding=0 cellspacing=0>
        <tr>
          <td align="center">
            <table cellPadding=3>
              <tr>
                <td>
                  <input type="button" value="Save" class="inp-text" onclick="addPropFunding();">
                </td>
                <td>
                  <input type="reset" value="Reset" class="inp-text">
                </td>
                <td>
                  <input type="button" value="Close" class="inp-text" onclick="window.close();">
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
