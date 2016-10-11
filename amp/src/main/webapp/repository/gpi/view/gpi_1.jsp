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
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:form action="/gpi" type="org.digijava.module.gpi.form.GPIForm" name="gpiForm">
	<table cellspacing="0" cellpadding="0" border="1" class="inside" width="100%" 
	style="font-size:11px; font-family: Arial,sans-serif; background-color: white; font-family: Arial, Helvetica, sans-serif;">
	    <tr align="center">
	        <td width="15%" height="33" class="inside_header" >
	            <div align="center">
	                <strong><digi:trn key="aim:donors">Donor(s)</digi:trn></strong>
	            </div>
	        </td>
	        <td width="5%" height="33" class="inside_header" >
	            <div align="center">
	                <strong><digi:trn key="aim:disbursmentYear">Disbursement Year</digi:trn></strong>
	            </div>
	        </td>
			<td width="27%" height="33" class="inside_header" >
			  <div align="center">
			      <strong><digi:trn>Number of projects using country results framework</digi:trn></strong>
			  </div>
			</td>
			<td width="26%" height="33" class="inside_header" >
			  <div align="center">
			      <strong><digi:trn>Total number of projects</digi:trn></strong>
			  </div>
			</td>
			<td width="27%" height="33" class="inside_header" >
	            <div align="center">
	                <strong><digi:trn>Indicator 1</digi:trn></strong>
	            </div>
	        </td>
		</tr>
		<logic:empty name="gpiForm" property="mainTableRows">
	        <tr>
	            <td width="100%" align="center" height="65" colspan="5" />
	                <div align="center">
	                    <strong><font color="red"><digi:trn key="aim:noSurveyDataFound">No survey data found.</digi:trn></font></strong>
	                </div>
	            </td>
	        </tr>
	        <html:hidden property="reportIsEmpty" value="true" styleId="reportIsEmpty"/>
	    </logic:empty>
	    <logic:notEmpty name="gpiForm" property="mainTableRows">
	       <%int counter = 0; %>
	       <bean:define id="color" value="" type="String"/>
	       <logic:iterate id="element" name="gpiForm" property="mainTableRows" indexId="index" type="org.digijava.module.gpi.helper.row.GPIReport1Row">
	           <logic:equal name="element" property="year" value="${gpiForm.selectedStartYear}">
                   <%/*counter++;*/counter=1;%>
               </logic:equal>
	           <%if(counter%2 == 0) color = "bgcolor=#EBEBEB"; else color = "";%>
	           <tr <%=color%> >
	               <logic:equal name="element" property="year" value="${gpiForm.selectedStartYear}">
		               <td align="center" rowspan="${gpiForm.selectedEndYear + 1 - gpiForm.selectedStartYear}" height="65" class="inside" style="font-size: 11px; color: #484846;">
		                   <span style="font-weight: bold"><bean:write name="element" property="donorGroup.orgGrpName"/></span>
		               </td>
	               </logic:equal>
	               <td align="center" class="inside" style="font-size: 11px; color: #484846;">
	                   <bean:write name="element" property="year"/>
	               </td>
	               <td align="center" class="inside" style="font-size: 11px; color: #484846;">
                       <c:if test="${element.column1 != null}">
                       		${element.column1}
                    	</c:if>
                    	<c:if test="${element.column1 == null}">
                    		<digi:trn><%=org.digijava.module.gpi.util.GPIConstants.NO_DATA %></digi:trn>
                    	</c:if>
                   </td>
                   <td align="center" class="inside" style="font-size: 11px; color: #484846;">
                       <c:if test="${element.column2 != null}">
                       		${element.column2}
                    	</c:if>
                    	<c:if test="${element.column2 == null}">
                    		<digi:trn><%=org.digijava.module.gpi.util.GPIConstants.NO_DATA %></digi:trn>
                    	</c:if>
                   </td>
                   <td align="center" class="inside" style="font-size: 11px; color: #484846;">
                       <fmt:formatNumber type="number" value="${element.column3}" pattern="###" maxFractionDigits="0" />%
                   </td>
	           </tr>
	       </logic:iterate>
	    </logic:notEmpty>
	</table>
</digi:form>