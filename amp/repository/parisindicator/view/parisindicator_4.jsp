<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/digijava.tld" prefix="digi"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/aim.tld" prefix="aim" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/globalsettings.tld" prefix="gs" %>

<digi:form action="/parisindicator" type="org.digijava.module.parisindicator.form.PIForm" name="parisIndicatorForm">
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
                  <strong><digi:trn>Volume of technical co-operation for capacity development provided through co-ordinated programmes</digi:trn></strong>
              </div>
            </td>
            <td width="26%" height="33"  class="inside_header" >
              <div align="center">
                  <strong><digi:trn>Total volume of technical co-operation provided</digi:trn></strong>
              </div>
            </td>
            <td width="27%" height="33"  class="inside_header" >
                <div align="center">
                    <strong><digi:trn>% of TC for capacity development provided through coordinated programmes consistent with national development strategies</digi:trn></strong>
                </div>
            </td>
        </tr>
        <logic:empty name="parisIndicatorForm" property="mainTableRows">
            <tr>
                <td width="100%" align="center" height="65" colspan="5" />
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
            type="org.digijava.module.parisindicator.helper.row.PIReport4Row">
               <logic:equal name="element" property="year" value="${parisIndicatorForm.selectedStartYear}">
                   <%/*counter++;*/counter=1;%>
               </logic:equal>
               <%if(counter%2 == 0) color = "bgcolor=#EBEBEB"; else color = "";%>
               <tr <%=color%> >
                   <logic:equal name="element" property="year" value="${parisIndicatorForm.selectedStartYear}">
                       <td align="center" rowspan="${parisIndicatorForm.selectedEndYear + 1 - parisIndicatorForm.selectedStartYear}" height="65">
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
	<font color="orange">&nbsp;*&nbsp;</font>
  	<jsp:include page="/repository/aim/view/utils/amountUnitsUnformatted.jsp">
    	<jsp:param value="" name="amount_prefix"/>
  	</jsp:include>
	<digi:trn><bean:write name="parisIndicatorForm" property="selectedCurrency"/></digi:trn>
</digi:form>