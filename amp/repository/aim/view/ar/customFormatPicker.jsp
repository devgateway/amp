<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>

<bean:define id="reportMeta" name="reportMeta"
	type="org.digijava.module.aim.dbentity.AmpReports" scope="session"
	toScope="page" />


<script type="text/javascript">

function myReset() {
	aimReportsFilterPickerForm3.customDecimalSymbol.value = ",";
	aimReportsFilterPickerForm3.customDecimalSymbolTxt.value = "";
	aimReportsFilterPickerForm3.customDecimalSymbolTxt.disabled = "true";
	aimReportsFilterPickerForm3.customDecimalPlaces.value = "-1";
	aimReportsFilterPickerForm3.customDecimalPlacesTxt.value = "";
	aimReportsFilterPickerForm3.customDecimalPlacesTxt.disabled = "true"
	aimReportsFilterPickerForm3.customUseGrouping.checked = "true";
	aimReportsFilterPickerForm3.customGroupCharacter.value = ".";
	aimReportsFilterPickerForm3.customGroupCharacterTxt.value = "";
	aimReportsFilterPickerForm3.customGroupSize.value = 3;
	initFormatPopup();
}
</script>

<digi:instance property="aimReportsFilterPickerForm" />

<digi:form action="/reportsFilterPicker.do"
	name="aimReportsFilterPickerForm3" type="aimReportsFilterPickerForm"  onsubmit="return validateFormat()">

	<table width="400" border="0" cellpadding="2" cellspacing="0">
<tr>
			<td colspan="3">&nbsp;</td>
	  </tr>
		<tr>
			<td width="40%" height="18" align="right" nowrap="nowrap"><digi:trn key="aim:formatPicket:decimalSymbol">Decimal Separator</digi:trn>&nbsp;</td>
        <td width="20%" height="18" nowrap="nowrap"><html:select styleClass="inp-text" onchange="initFormatPopup();" 
				property="customDecimalSymbol" styleId="customDecimalSymbol">
              <html:option value=",">,</html:option>
              <html:option value=".">.</html:option>
              <html:option value="CUSTOM">
                <digi:trn key="aim:formatPicket:custom">Custom</digi:trn>
              </html:option>
            </html:select></td>
      <td width="20%" height="18" align="left" nowrap="nowrap"><label>
        <html:text styleClass="inp-text" disabled="true" size="2" maxlength="1" property="customDecimalSymbolTxt" onchange="initFormatPopup()" /> </label></td>
	  </tr>
		<tr>
			<td width="40%" height="18" align="right" nowrap="nowrap">		  <digi:trn key="aim:formatPicket:maxFracDigits">Maximun Fraction Digits</digi:trn>
		    &nbsp;</td>
    <td width="20%" height="18" nowrap="nowrap"><html:select styleClass="inp-text" property="customDecimalPlaces"
				styleId="customDecimalPlaces" onchange="initFormatPopup();">
              <html:option value="-1">
                <digi:trn key="aim:formatPicket:Disable">Disable</digi:trn>
              </html:option>
              <html:option value="1">1</html:option>
              <html:option value="2">2</html:option>
              <html:option value="3">3</html:option>
              <html:option value="4">4</html:option>
              <html:option value="5">5</html:option>
              <html:option value="CUSTOM">
                <digi:trn key="aim:formatPicket:custom">Custom</digi:trn>
              </html:option>
            </html:select></td>
          <td width="20%" height="18" align="left" nowrap="nowrap"><html:text styleClass="inp-text" disabled="true" size="2" maxlength="2" property="customDecimalPlacesTxt" onchange="initFormatPopup()" /></td>
	  </tr>
		<tr>
			<td width="40%" height="18" align="right" nowrap="nowrap"><digi:trn key="aim:formatPicket:UseGrouping">Use Grouping Separator</digi:trn>&nbsp;</td>
	      <td width="20%" height="18" nowrap="nowrap"><html:checkbox property="customUseGrouping"
				styleId="customUseGrouping"  onchange ="initFormatPopup();" /></td>
	      <td width="20%" height="18" align="left" nowrap="nowrap">&nbsp;</td>
	  </tr>
		<tr>
			<td width="40%" height="18" align="right" nowrap="nowrap"><digi:trn key="aim:formatPicket:GroupingSeparator">Grouping Separator</digi:trn>&nbsp;</td>
	    <td width="20%" height="18" nowrap="nowrap"><html:select styleClass="inp-text" property="customGroupCharacter"
				styleId="customGroupCharacter" onchange="initFormatPopup();">
              <html:option value=".">.</html:option>
              <html:option value=",">,</html:option>
              <html:option value="CUSTOM">
                <digi:trn key="aim:formatPicket:custom">Custom</digi:trn>
              </html:option>
            </html:select></td>
          <td width="20%" height="18" align="left" nowrap="nowrap">
          <html:text styleClass="inp-text" disabled="true" size="2" maxlength="1" property="customGroupCharacterTxt" onchange="initFormatPopup()"  /></td>
	  </tr>
		<tr>
		  <td height="18" align="right" nowrap="nowrap"><digi:trn key="aim:formatPicket:GroupSize">Group Size</digi:trn>&nbsp;</td>
		  <td height="18" colspan="2" nowrap="nowrap"><html:text disabled="true" property="customGroupSize" size="2" maxlength="1" onchange="initFormatPopup();"/></td>
	  </tr>
		<tr>
			<td width="40%" height="18" align="right" nowrap="nowrap" ><digi:trn key="aim:formatPicket:Example">Example</digi:trn>
		  &nbsp;</td>
	    <td height="18" colspan="2" nowrap="nowrap"  style="font-weight:bold;size:11px">
		      <div id="number" style="margin-left: 3px">
		  	  <aim:formatNumber value="123456789.928" />
	          </div></td>
      </tr>
			<tr>
			<td height="40" colspan="6" align="center"><html:hidden
				property="ampReportId" /> 
				
			<html:submit styleClass="dr-menu"  property="applyFormat">
				<digi:trn key="rep:filer:ApplyFormat">Apply Format</digi:trn>
			</html:submit>&nbsp; 
			
				<input type="hidden" name="apply" value="true">
				<html:hidden property="resetFormat" value="false"/>
				<html:button styleClass="dr-menu" onclick="myReset();" property="applyFormat">
				<digi:trn key="rep:filer:ResetFormat">Reset</digi:trn>
			</html:button></td>
		</tr>
	</table>
</digi:form>