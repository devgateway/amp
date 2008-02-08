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
  document.aimViewAhSurveyFormulasForm.submit();
}

function changeFormula(){
  document.aimViewAhSurveyFormulasForm.submit();
}

function resetFormula(){
  var textBox=document.getElementById("txtBaseLineValue");
  textBox.value="";

  textBox=document.getElementById("txtConstant");
  textBox.value="";

  textBox=document.getElementById("txtFormula");
  textBox.value="";
}

</script>

<digi:errors/>
<digi:instance property="aimViewAhSurveyFormulasForm" />

<jsp:include page="teamPagesHeader.jsp" flush="true" />
<digi:form action="/viewAhSurveyFormulas.do" >
  <html:hidden property="action" styleId="act" />
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
    <%--
    <tr>
      <td>
      Column
      </td>
      <td>
        <html:select property="selectedColumnIndex" style="font-family:verdana;font-size:8pt;" onchange="changeFormula();">
          <html:option value="-1">-Select column-</html:option>

          <!-- Indicator 3 -->
          <c:if test = "${aimViewAhSurveyFormulasForm.indCode == '3'}">
            <html:option value="0">
              <digi:trn key="aim:aidFlowsGovernmentSectorReported">
              Aid flows to the government sector reported on the government's budget
              </digi:trn>
            </html:option>
            <html:option value="1">
              <digi:trn key="aim:totalAidFlowsDisbursed">
              Total Aid flows disbursed to the government sector
              </digi:trn>
            </html:option>
          </c:if>

          <!--Indicator 4 -->
          <c:if test = "${aimViewAhSurveyFormulasForm.indCode == '4'}">
            <html:option value="0">
              <digi:trn key="aim:VolumeOfTehnicalCoOperation">
              Volume of technical co-operation for capacity development provided through co-ordinated programmes
              </digi:trn>
            </html:option>
            <html:option value="1">
              <digi:trn key="aim:totalVolumeOfTehnicalCoOperation">
              Total volume of technical co-operation provided
              </digi:trn>
            </html:option>
            <html:option value="2">
              <digi:trn key="aim:noOfTC">
              % of TC for capacity development provided through coordinated programmes consistent with national development strategies
              </digi:trn>
            </html:option>
          </c:if>

          <!--Indicator 5a -->
          <c:if test = "${aimViewAhSurveyFormulasForm.indCode == '5a'}">
            <html:option value="0">
              <digi:trn key="aim:aidFlowsToGovernmentSectorBudget">
              Aid flows to the goverment sector that use national budget execution procedures
              </digi:trn>
            </html:option>

            <html:option value="1">
              <digi:trn key="aim:aidFlowsToGovernmentSectorFinancialReporting">
              Aid flows to the goverment sector that use national financial reporting procedures
              </digi:trn>
            </html:option>

            <html:option value="2">
              <digi:trn key="aim:aidFlowsToGovernmentSectorFinancialAuditing">
              Aid flows to the goverment sector that use national financial auditing procedures
              </digi:trn>
            </html:option>

            <html:option value="3">
              <digi:trn key="aim:odaThatUses">
              ODA that uses all 3 national PFM
              </digi:trn>
            </html:option>

            <html:option value="4">
              <digi:trn key="aim:totalAidFlowsDisbursed">
              Total aid flows disbursed to the government sector
              </digi:trn>
            </html:option>

            <html:option value="5">
              <digi:trn key="aim:proportionAidFlowsUsingOne">
              Proportion aid flows to the government sector using one of the 3 country PFM systems
              </digi:trn>
            </html:option>

            <html:option value="6">
              <digi:trn key="aim:proportionOfAidFlowsUsingAll">
              Proportion of aid flows to the government sector using all the 3 country PFM systems
              </digi:trn>
            </html:option>
          </c:if>

          <!--Indicator 5b -->
          <c:if test = "${aimViewAhSurveyFormulasForm.indCode == '5b'}">
            <html:option value="0">
              <digi:trn key="aim:aidFlowsToTheGovernmentSector">
              Aid flows to the government sector that use national procurement procedures
              </digi:trn>
            </html:option>

            <html:option value="1">
              <digi:trn key="aim:totalAidFlowsDisbured">
              Total aid flows disbursed to the government sector
              </digi:trn>
            </html:option>

            <html:option value="2">
              <digi:trn key="aim:proportionOfAidFlowsToTheGovernmentSector">
              Proportion of aid flows to the government sector using national procurement procedures
              </digi:trn>
            </html:option>
          </c:if>

          <!--Indicator 9 -->
          <c:if test = "${aimParisIndicatorReportForm.indCode == '9'}">
            <html:option value="0">
              <digi:trn key="aim:budgetSupportAidFlowsInTheContextOfProgammeBasedApproach">
              Budget support aid flows provided in the context of programme based approach
              </digi:trn>
            </html:option>

            <html:option value="1">
              <digi:trn key="aim:otherAidFlowsProvidedInTheContextOfProgrammeBasedApproach">
              Other aid flows provided in the context of programme based approach
              </digi:trn>
            </html:option>

            <html:option value="2">
              <digi:trn key="aim:totalAidFlowsProvided">
              Total aid flows provided
              </digi:trn>
            </html:option>

            <html:option value="3">
              <digi:trn key="aim:proportionOfAidFlowsInTheContextOfProgrammeBasedApproach">
              Proportion of aid flows provided in the context of programme based approach
              </digi:trn>
            </html:option>
          </c:if>

          <!--Indicator 10a -->
          <c:if test = "${aimViewAhSurveyFormulasForm.indCode == '10a'}">
            <html:option value="0">
              <digi:trn key="aim:numberOfMissions">
              Number of missions to the field that are joint
              </digi:trn>
            </html:option>

            <html:option value="1">
              <digi:trn key="aim:totalNumberOfMissions">
              Total number of missions to the field
              </digi:trn>
            </html:option>
          </c:if>

          <!--Indicator 10b -->
          <c:if test = "${aimViewAhSurveyFormulasForm.indCode == '10b'}">
            <html:option value="0">
              <digi:trn key="aim:numberOfCountryAnalytic">
              Number of country analytic reports that are joint
              </digi:trn>
            </html:option>
            <html:option value="1">
              <digi:trn key="aim:totalNumberOfCountryAnalytic">
              Total number of country analytic reports
              </digi:trn>
            </html:option>
          </c:if>
        </html:select>
      </td>
    </tr>
    --%>
    <tr>
      <td>
      Baseline value
      </td>
      <td>
        <html:text property="baseLineValue" styleId="txtBaseLineValue" style="font-family:verdana;font-size:8pt;" />
      </td>
    </tr>

    <tr>
      <td>
      Target value
      </td>
      <td>
        <html:text property="targetValue" styleId="txtTargetValue" style="font-family:verdana;font-size:8pt;" />
      </td>
    </tr>

    <tr>
      <td>
      Constant
      </td>
      <td>
        <html:text property="constantName" styleId="txtConstant" style="font-family:verdana;font-size:8pt;" />
      </td>
    </tr>

    <tr>
      <td>
      Formula
      </td>
      <td>
        <html:text property="formulaText"  styleId="txtFormula" style="font-family:verdana;font-size:8pt;" />
      </td>
    </tr>

    <tr>
      <td>

      </td>
      <td>
        <input type="button" value="Save" onclick="saveFormula();" style="font-family:verdana;font-size:8pt;">
        <input type="button" value="Reset" onclick="resetFormula();" style="font-family:verdana;font-size:8pt;">
      </td>
    </tr>
  </table>
</digi:form>
