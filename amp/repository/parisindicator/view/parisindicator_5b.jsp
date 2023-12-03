<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/struts-nested" prefix="nested"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ taglib uri="/taglib/fmt" prefix="fmt" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<digi:form action="/parisindicator" type="org.digijava.ampModule.parisindicator.form.PIForm" name="parisIndicatorForm">
    <table cellspacing="0" cellpadding="0" border="1" class="inside" width="100%" 
	style="font-size:11px; font-family: Arial,sans-serif; background-color: white; font-family: Arial, Helvetica, sans-serif;">
        <tr align="center">
            <td width="15%" height="33"  class="inside_header" >
                <div align="center">
                    <strong><digi:trn key="aim:donors">Donor(s)</digi:trn></strong>
                </div>
            </td>
            <td width="5%" height="33"  class="inside_header" >
                <div align="center">
                    <strong><digi:trn key="aim:disbursmentYear">Disbursement Year</digi:trn></strong>
                </div>
            </td>
            <td width="27%" height="33"  class="inside_header" >
              <div align="center">
                  <strong><digi:trn>Aid flows to the government sector that use national procurement procedures</digi:trn></strong>
              </div>
            </td>
            <td width="26%" height="33"  class="inside_header" >
              <div align="center">
                  <strong><digi:trn>Total Aid flows disbursed to the government sector</digi:trn></strong>
              </div>
            </td>
            <td width="27%" height="33"  class="inside_header" >
                <div align="center">
                    <strong><digi:trn>Proportion of aid flows to the government sector using national procurement procedures</digi:trn></strong>
                </div>
            </td>
        </tr>
        <logic:empty name="parisIndicatorForm" property="mainTableRows">
            <tr>
                <td width="100%" align="center" height="65" colspan="5"   >
                    <div align="center">
                        <strong><font color="red"><digi:trn key="aim:noSurveyDataFound">No survey data found.</digi:trn></font></strong>
                    </div>
                </td>
            </tr>
            <html:hidden property="reportIsEmpty" value="true" styleId="reportIsEmpty"/>
        </logic:empty>
        <logic:notEmpty name="parisIndicatorForm" property="mainTableRows">
           <%int counter = 0; %>
           <bean:define id="color" value="" type="String"/>
           <logic:iterate id="element" name="parisIndicatorForm" property="mainTableRows" indexId="index" 
            type="org.digijava.ampModule.parisindicator.helper.row.PIReport5bRow">
               <logic:equal name="element" property="year" value="${parisIndicatorForm.selectedStartYear}">
                   <%/*counter++;*/counter=1;%>
               </logic:equal>
               <%if(counter%2 == 0) color = "bgcolor=#EBEBEB"; else color = "";%>
               <tr <%=color%> >
                   <logic:equal name="element" property="year" value="${parisIndicatorForm.selectedStartYear}">
                       <td align="center" rowspan="${parisIndicatorForm.selectedEndYear + 1 - parisIndicatorForm.selectedStartYear}" height="65" class="inside" style="font-size: 11px; color: #484846;">
                           <strong><digi:trn><bean:write name="element" property="donorGroup.orgGrpName"/></digi:trn></strong>
                       </td>
                   </logic:equal>
                   <td align="center" class="inside" style="font-size: 11px; color: #484846;">
                       <bean:write name="element" property="year"/>
                   </td>
                   <td align="center" class="inside" style="font-size: 11px; color: #484846;">
                       <aim:formatNumber value="${element.column1}"/>
                   </td>
                   <td align="center" class="inside" style="font-size: 11px; color: #484846;">
                       <aim:formatNumber value="${element.column2}"/>
                   </td>
                   <td align="center" class="inside" style="font-size: 11px; color: #484846;">
                       <fmt:formatNumber type="number" value="${element.column3}" pattern="###" maxFractionDigits="0" />%
                   </td>
               </tr>
           </logic:iterate>
        </logic:notEmpty>
    </table>
    <br>
	<table>
		<tr><td>
			<font color="orange">&nbsp;*&nbsp;</font>
  			<jsp:include page="/repository/aim/view/utils/amountUnitsUnformatted.jsp">
    			<jsp:param value="" name="amount_prefix"/>
  			</jsp:include>
			<digi:trn><bean:write name="parisIndicatorForm" property="selectedCurrency"/></digi:trn>
		</td></tr>
	</table>		
	<br>
    <table width="100%">
        <tr align="center">
            <td align="center">
			    <table border="0" width="50%" cellspacing="0" cellpadding="0" align="center" style="font-size:11px; font-family: Arial,sans-serif; background-color: white; font-family: Arial, Helvetica, sans-serif;" >
			        <tr>
			            <td align="center" rowspan="2"  class="inside_header" style="background-repeat: repeat-x; font-size: 12px; border-left-width: 1px;" class="inside">
			                <strong><digi:trn>Percent of ODA using national procurement systems</digi:trn></strong>
			            </td>
			            <td align="center" colspan="${parisIndicatorForm.selectedEndYear + 1 - parisIndicatorForm.selectedStartYear}"  class="inside_header" >
			                <strong><digi:trn>Percent of donors that use national procurement systems</digi:trn></strong>    
			            </td>
			        </tr>
			        <bean:define id="years" value="${parisIndicatorForm.selectedEndYear + 1 - parisIndicatorForm.selectedStartYear}"/>
			        <bean:define id="miForm" name="parisIndicatorForm" type="org.digijava.ampModule.parisindicator.form.PIForm"/>
			        <tr>
			        <%for(int jj = 0; jj < Integer.valueOf(years).intValue(); jj++) {%>
			            <td class="inside" style="font-size: 11px; color: #484846;"><strong><%=miForm.getSelectedStartYear()+jj%></strong></td>
			        <%}%>
			        </tr>
			        <tr>
			            <td class="inside" style="font-size: 11px; color: #484846; border-left-width: 1px;">
			                <strong><digi:trn>Less than 10%</digi:trn></strong>
			            </td>
			            <nested:iterate name="parisIndicatorForm" property="miniTable[0]" id="element">
			                <td class="inside" style="font-size: 11px; color: #484846;">
			                    <bean:write name="element"/>
			                </td>
			            </nested:iterate>
			        </tr>
			        <tr>
			            <td class="inside" style="font-size: 11px; color: #484846; border-left-width: 1px;">
			                <strong><digi:trn>From 10 to 50%</digi:trn></strong>
			            </td>
			            <nested:iterate name="parisIndicatorForm" property="miniTable[1]" id="element">
			                <td class="inside" style="font-size: 11px; color: #484846;">
			                    <bean:write name="element"/>
			                </td>
			            </nested:iterate>
			        </tr>
			        <tr>
			            <td class="inside" style="font-size: 11px; color: #484846; border-left-width: 1px;">
			                <strong><digi:trn>From 50 to 90%</digi:trn></strong>
			            </td>
			            <nested:iterate name="parisIndicatorForm" property="miniTable[2]" id="element">
			                <td class="inside" style="font-size: 11px; color: #484846;">
			                    <bean:write name="element"/>
			                </td>
			            </nested:iterate>
			        </tr>
			        <tr>
			            <td class="inside" style="font-size: 11px; color: #484846; border-left-width: 1px; border-bottom-width: 1px;">
			                <strong><digi:trn>More than 90%</digi:trn></strong>
			            </td>
			            <nested:iterate name="parisIndicatorForm" property="miniTable[3]" id="element">
			                <td class="inside" style="font-size: 11px; color: #484846; border-bottom-width: 1px;">
			                    <bean:write name="element"/>
			                </td>
			            </nested:iterate>
			        </tr>
			    </table>
            </td>
        </tr>
    </table>
</digi:form>