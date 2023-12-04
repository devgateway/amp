<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/struts-nested" prefix="nested"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ taglib uri="/taglib/fmt" prefix="fmt" %>

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
			      <strong><digi:trn>Number of missions to the field that are joint</digi:trn></strong>
			  </div>
			</td>
			<td width="26%" height="33"  class="inside_header" >
			  <div align="center">
			      <strong><digi:trn>Total number of missions to the field</digi:trn></strong>
			  </div>
			</td>
			<td width="27%" height="33"  class="inside_header" >
	            <div align="center">
	                <strong><digi:trn>Proportion of donor missions that are joint</digi:trn></strong>
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
	        type="org.digijava.module.parisindicator.helper.row.PIReport10aRow">
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