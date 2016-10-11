<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/fmt" prefix="fmt" %>
<%@ taglib uri="/taglib/category" prefix="category" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript" type="text/javascript">
function saveFormula(){
  var a=document.getElementById("act");
  if(a!=null){
    a.value="save"
  }
  var ie=document.getElementById("chkEnabled");
  var he=document.getElementById("hdnEna");
  if(ie!=null && he!=null){
    he.value=ie.checked;
  }
  document.aimViewAhSurveyFormulasForm.submit();
  document.location.href = "/viewAhSurveis.do";
  
}

function changeFormula(){
  document.aimViewAhSurveyFormulasForm.submit();
}

function resetFormula(){
  var textBox=document.getElementById("txtBaseLineValue");
  textBox.value="";

  var textBox=document.getElementById("txtTargetValue");
  textBox.value="";
  
  textBox=document.getElementById("txtConstant");
  textBox.value="";

  textBox=document.getElementById("txtFormula");
  textBox.value="";
}

</script>

<digi:errors/>
<digi:instance property="aimViewAhSurveyFormulasForm" />

<jsp:include page="teamPagesHeader.jsp"  />
<digi:form action="/viewAhSurveyFormulas.do" >
  <html:hidden property="action" styleId="act" />
  <table>
    <tr>
      <td>
  <table>
    <tr>
      <td colspan="2" valign="bottom" class="crumb" >
        <c:set var="translation">
          <digi:trn key="aim:clickToViewMyDesktop">Click here to view admin home page</digi:trn>
        </c:set>
        <digi:link href="/admin.do" styleClass="comment" title="${translation}" >
          <digi:trn key="aim:adminHomeLink">Admin home</digi:trn>
        </digi:link>
        &gt;
        <digi:link href="/viewAhSurveis.do" styleClass="comment" title="${translation}" >
          <digi:trn key="aim:allParisReportsList">Paris Indicator Reports List</digi:trn>
        </digi:link>
        &gt;
        <digi:trn key="aim:surveyFormulas">Survey formulas</digi:trn>
      </td>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td colspan="2" height=16 align="center" vAlign="middle">
        <span class=subtitle-blue>
          <digi:trn key="aim:AhSurveyFormula">AhSurvey Formula</digi:trn>
        </span>
      </td>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>
        <html:hidden property="formulaEnabled" styleId="hdnEna" />
        <c:if test="${aimViewAhSurveyFormulasForm.formulaEnabled}">
          <input type="checkbox" id="chkEnabled" checked="checked" />
        </c:if>
        <c:if test="${!aimViewAhSurveyFormulasForm.formulaEnabled}">
          <input type="checkbox" id="chkEnabled" />
        </c:if>
        <%--Not Working correctly ->  <html:checkbox property="formulaEnabled" styleId="chkEnabled" /> --%>
        &nbsp;<digi:trn key="aim:enable">Enable</digi:trn>
      </td>
      <td>&nbsp;</td>
    </tr>

    <tr>
      <td>
      <digi:trn key="aim:baselinevalue">Baseline value</digi:trn>
      </td>
      <td>
        <html:text property="baseLineValue" styleId="txtBaseLineValue" style="font-family:verdana;font-size:8pt;" />
      </td>
    </tr>

    <tr>
      <td>
      <digi:trn key="aim:targetvalue">Target value</digi:trn>      
      </td>
      <td>
        <html:text property="targetValue" styleId="txtTargetValue" style="font-family:verdana;font-size:8pt;" />
      </td>
    </tr>

    <tr>
      <td>
      <digi:trn key="aim:constant">Constant</digi:trn>      
      </td>
      <td>
        <html:text property="constantName" styleId="txtConstant" style="font-family:verdana;font-size:8pt;" />
      </td>
    </tr>

    <tr>
      <td>
      <digi:trn key="aim:formula">Formula</digi:trn>
      </td>
      <td>
        <html:text property="formulaText"  styleId="txtFormula" style="font-family:verdana;font-size:8pt;" />
      </td>
    </tr>

    <tr>
      <td>

      </td>
      <td>
        <input type="button" value="<digi:trn key="aim:btnsave">Save</digi:trn>" onclick="saveFormula();" style="font-family:verdana;font-size:8pt;">
        <input type="button" value="<digi:trn key="aim:btnreset">Reset</digi:trn>" onclick="resetFormula();" style="font-family:verdana;font-size:8pt;">
      </td>
    </tr>
  </table>
  </td>
  
  
  
  <!-- Other Links -->

 <td width=300 valign="top" align="right">
										<table align="center" cellpadding="0" cellspacing="0"
											width="300" border="0">
											<tr>
												<td>
													<!-- Other Links -->
													<table cellpadding="0" cellspacing="0" width="120">
														<tr>
															<td bgColor=#c9c9c7 class=box-title><digi:trn
																	key="aim:otherLinks">
																	<b style="font-weight: bold; font-size: 12px; padding-left:5px; color:#000000;"><digi:trn>Other links</digi:trn></b>
																</digi:trn></td>
															<td background="module/aim/images/corner-r.gif"
																height="17" width="17">&nbsp;</td>
														</tr>
													</table></td>
											</tr>
											<tr>
												<td bgColor=#ffffff>
													<table cellPadding=0 cellspacing="0" width="100%" class="inside">
														
														<tr>
															<td class="inside"><digi:img
																	src="module/aim/images/arrow-014E86.gif" width="15"
																	height="10" /> <digi:link module="aim" href="/admin.do">
																	<digi:trn key="aim:AmpAdminHome">
																Admin Home
																</digi:trn>
																</digi:link></td>
														</tr>
														
														<!-- end of other links -->
													</table></td>
											</tr>
											<tr>
												<td bgColor=#ffffff>
													<table cellPadding=0 cellspacing="0" width="100%" class="inside">
														
														<tr>
															<td class="inside"><digi:img
																	src="module/aim/images/arrow-014E86.gif" width="15"
																	height="10" /> <digi:link module="aim" href="/viewAhSurveis.do">
																	<digi:trn>
																Paris Indicator Reports
																</digi:trn>
																</digi:link></td>
														</tr>
														
														<!-- end of other links -->
													</table></td>
											</tr>
										</table></td>
  
  
  
  </tr>
  </table>
</digi:form>
