<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/aim.tld" prefix="aim" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/globalsettings.tld" prefix="gs" %>

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
			<td width="20%" height="33"  class="inside_header" >
			  <div align="center">
			      <strong><digi:trn>Budget support aid flows provided in the context of programme based approach</digi:trn></strong>
			  </div>
			</td>
			<td width="20%" height="33"  class="inside_header" >
			  <div align="center">
			      <strong><digi:trn>Other aid flows provided in the context of programme based approach</digi:trn></strong>
			  </div>
			</td>
			<td width="20%" height="33"  class="inside_header" >
	            <div align="center">
	                <strong><digi:trn>Total aid flows provided</digi:trn></strong>
	            </div>
	        </td>
	        <td width="20%" height="33"  class="inside_header" >
                <div align="center">
                    <strong><digi:trn>Proportion of aid flows provided in the context of programme based approach</digi:trn></strong>
                </div>
            </td>
		</tr>
		<logic:empty name="parisIndicatorForm" property="mainTableRows">
	        <tr>
	            <td width="100%" align="center" height="65" colspan="6" >
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
	        type="org.digijava.module.parisindicator.helper.row.PIReport9Row">
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
                       <aim:formatNumber value="${element.column3}"/>
                   </td>
                   <td align="center" class="inside" style="font-size: 11px; color: #484846;">
                       <fmt:formatNumber type="number" value="${element.column4}" pattern="###" maxFractionDigits="0" />%
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