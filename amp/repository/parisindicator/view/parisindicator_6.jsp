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
	        <bean:define id="piForm" name="parisIndicatorForm" type="org.digijava.module.parisindicator.form.PIForm"></bean:define>
	        <bean:define id="auxRow" name="parisIndicatorForm" property="mainTableRows[0]" type="org.digijava.module.parisindicator.helper.row.PIReport6Row"></bean:define>
	        <%for(int i = 0; i < auxRow.getYears().length; i++) {%>
	           <td width="10%" height="33">
                    <div align="center">
                        <strong><%=piForm.getSelectedStartYear() + i%></strong>
                    </div>
                </td>
	        <%}%>
		</tr>
		<logic:empty name="parisIndicatorForm" property="mainTableRows">
	        <tr>
	            <td width="100%" align="center" height="55" colspan="${parisIndicatorForm.selectedEndYear + 2 - parisIndicatorForm.selectedStartYear}" />
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
             type="org.digijava.module.parisindicator.helper.row.PIReport6Row">
                <%if(index%2 == 0) color = "bgcolor=#EBEBEB"; else color = "";%>
                <tr <%=color%> >
                    <td align="center" height="55">
                        <strong><bean:write name="element" property="donorGroup.orgGrpName"/></strong>
                    </td>
                    <%for(int i = 0; i < auxRow.getYears().length; i++) {%>
                        <td align="center">
                            <div align="center">
                                <%=element.getYears()[i]%>
                            </div>
                        </td>
                    <%}%>
               </tr>
           </logic:iterate>
        </logic:notEmpty>
	</table>
</digi:form>