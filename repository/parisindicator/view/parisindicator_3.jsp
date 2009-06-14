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
	<table cellspacing="0" cellpadding="0" border="1" 
	 width="100%" style="font-family: Arial, Helvetica, sans-serif; padding-right:5px; padding-left:5px; padding-top:5px;border-top-style:hidden;border-right-style:hidden;border-left-style:hidden;border-bottom-style:hidden">
	    <tr align="center"  bgcolor="#CCCCFF">
	        <td width="15%" height="33">
	            <div align="center">
	                <strong><digi:trn key="aim:donors">Donor(s)</digi:trn></strong>
	            </div>
	        </td>
	        <td width="5%" height="33">
	            <div align="center">
	                <strong><digi:trn key="aim:disbursmentYear">Disbursement Year</digi:trn></strong>
	            </div>
	        </td>
			<td width="27%" height="33">
			  <div align="center">
			      <strong><digi:trn key="aim:aidFlowsGovernmentSectorReported">Aid flows to the government sector reported on the government's budget</digi:trn></strong>
			  </div>
			</td>
			<td width="26%" height="33">
			  <div align="center">
			      <strong><digi:trn key="aim:totalAidFlowsDisbursed">Total Aid flows disbursed to the government sector</digi:trn></strong>
			  </div>
			</td>
			<td width="27%" height="33">
	            <div align="center">
	                <strong><digi:trn>Proportion of aid flows to the government sector reported on government budget</digi:trn></strong>
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
	    </logic:empty>
	    <logic:notEmpty name="parisIndicatorForm" property="mainTableRows">
	       <logic:iterate id="element" name="parisIndicatorForm" property="mainTableRows" indexId="index" 
	        type="org.digijava.module.parisindicator.helper.PIReport3Row">
	           <tr>
	               <td>
	                   <digi:trn><bean:write name="element" property="donorGroup.orgGrpName"/></digi:trn>
	               </td>
	               <td>
	                   <bean:write name="element" property="year"/>
	               </td>
	               <td>
                       <aim:formatNumber value="${element.column1}"/>
                   </td>
                   <td>
                       <aim:formatNumber value="${element.column2}"/>
                   </td>
                   <td>
                       <fmt:formatNumber type="number" value="${element.column3}" pattern="###" maxFractionDigits="0" />%
                   </td>
	           </tr>
	       </logic:iterate>
	    </logic:notEmpty>
	</table>
</digi:form>