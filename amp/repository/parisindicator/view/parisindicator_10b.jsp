<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/taglib/struts-nested" prefix="nested"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/aim.tld" prefix="aim" %>
<%@ taglib uri="/taglib/fmt" prefix="fmt" %>

<digi:form action="/parisindicator" type="org.digijava.module.parisindicator.form.PIForm" name="parisIndicatorForm">
	<table cellspacing="0" cellpadding="0" border="1" class="inside" width="100%" 
	style="font-size:11px; font-family: Arial,sans-serif; background-color: white; font-family: Arial, Helvetica, sans-serif;">
	    <tr align="center" >
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
			      <strong><digi:trn>Number of country analytic reports that are joint</digi:trn></strong>
			  </div>
			</td>
			<td width="26%" height="33"  class="inside_header" >
			  <div align="center">
			      <strong><digi:trn>Total number of country analytic reports</digi:trn></strong>
			  </div>
			</td>
			<td width="27%" height="33"  class="inside_header" >
	            <div align="center">
	                <strong><digi:trn>Proportion of country analytic work that is joint</digi:trn></strong>
	            </div>
	        </td>
		</tr>
		<logic:empty name="parisIndicatorForm" property="mainTableRows">
	        <tr>
	            <td width="100%" align="center" height="65" colspan="5" >
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
	        type="org.digijava.module.parisindicator.helper.row.PIReport10bRow">
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
</digi:form>