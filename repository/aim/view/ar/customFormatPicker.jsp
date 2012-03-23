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
<digi:instance property="aimReportsFilterPickerForm" />

<digi:form action="/reportsFilterPicker.do"
	name="aimReportsFilterPickerForm3" type="aimReportsFilterPickerForm"  onsubmit="return validateFormat()">

<strong><digi:trn>Number Format</digi:trn></strong>
<div style="border: 1px solid #e5e8e6; margin: 5px;">
	<table width="400px" border="0" cellpadding="2" cellspacing="0" style="margin-left: auto; margin-right: auto;">
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
			<td width="40%" height="18" align="right" nowrap="nowrap">		  <digi:trn key="aim:formatPicket:maxFracDigits">Maximum Fraction Digits</digi:trn>
		    &nbsp;</td>
    <td width="20%" height="18" nowrap="nowrap"><html:select styleClass="inp-text" property="customDecimalPlaces"
				styleId="customDecimalPlaces" onchange="initFormatPopup();">
              <html:option value="-1">
                <digi:trn key="aim:formatPicket:NoLimit">No Limit</digi:trn>
              </html:option>
              <html:option value="0">0</html:option>
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
		<td height="18" align="right" nowrap="nowrap">
			<digi:trn key="aim:formatPicket:OriginalValues">Original Values</digi:trn> &nbsp;
		</td>
		<td height="18" colspan="2" nowrap="nowrap">
		 	<html:radio value="0" property="amountinthousands" styleId="customAmountinThousands"  onchange ="initFormatPopup();" />
		 </td>
	  </tr>
	  <tr>
		<td height="18" align="right" nowrap="nowrap">
			<digi:trn key="aim:formatPicket:Amountinthousands">Amount in thousands</digi:trn> &nbsp;
		</td>
		<td height="18" colspan="2" nowrap="nowrap">
		 	<html:radio value="1" property="amountinthousands" styleId="customAmountinThousands"  onchange ="initFormatPopup();" />
		 </td>
	  </tr>
	  <tr>
		<td height="18" align="right" nowrap="nowrap">
			<digi:trn key="aim:formatPicket:Amountinmillions">Amount in millions</digi:trn> &nbsp;
		</td>
		<td height="18" colspan="2" nowrap="nowrap">
		 	<html:radio value="2" property="amountinthousands" styleId="customAmountinMillions"  onchange ="initFormatPopup();" />
		 </td>
	  </tr>
	<tr>
			<td width="40%" height="18" align="right" nowrap="nowrap" ><digi:trn key="aim:formatPicket:Example">Example</digi:trn>
		  &nbsp;</td>
	    <td height="18" colspan="2" nowrap="nowrap"  style="font-weight:bold;size:11px">
		      <div id="number" style="margin-left: 3px">
		  	  <aim:formatNumber value="123456789.928" />
	          </div></td>
      </tr>
		
	</table>
	
</div>
<br/>
<strong><digi:trn>Other Settings</digi:trn></strong>
<div style="border: 1px solid #e5e8e6; margin: 5px; padding: 2px; ">
	<table cellpadding="2px" width="400px" style="margin-left: auto; margin-right: auto;">
		<tr>
			<td width="40%" style="text-align: right"><digi:trn>Currency</digi:trn>&nbsp;</td>
			<td>
				<html:select property="currency"
								style="width: 200px" styleClass="inp-text">
					<html:optionsCollection property="currencies" value="ampCurrencyId"
						label="currencyName" />
				</html:select>
			</td>
		</tr>
		<tr>
			<td width="40%" style="text-align: right"><digi:trn>Calendar</digi:trn>&nbsp;</td>
			<td>
				<html:select property="calendar" style="width: 200px"
							styleClass="inp-text">
					<html:optionsCollection property="calendars" value="ampFiscalCalId"
						label="name" />
				</html:select>
			</td>
		</tr>
		
	<logic:notEqual name="widget" value="true" scope="request">
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td width="40%" style="text-align: right"><digi:trn>Year Range</digi:trn></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td style="text-align: right"> 
				<digi:trn>From</digi:trn>: &nbsp;
				
			</td>
			<td> 
				<html:select  styleClass="inp-text" property="renderStartYear">
					<html:option value="-1">
						<digi:trn key="rep:filer:All">All</digi:trn>
					</html:option>
					<html:optionsCollection property="fromYears" label="wrappedInstance" value="wrappedInstance" />
				</html:select> &nbsp;
				<digi:trn>To</digi:trn>: &nbsp; 
				<html:select property="renderEndYear" styleClass="inp-text">
					<html:option value="-1">
						<digi:trn key="rep:filer:All">All</digi:trn>
					</html:option>
					<html:optionsCollection property="toYears" label="wrappedInstance" value="wrappedInstance" />
				</html:select>
			</td>
		</tr>
	</logic:notEqual>
	
	</table>
</div>	

<div>
	<div style="margin-right: auto; margin-left: auto; text-align: center;">
			<html:hidden property="ampReportId" /> 
			<html:hidden property="defaultCurrency" />
			<html:submit styleClass="dr-menu"  property="applyFormat">
				<digi:trn key="rep:filer:ApplyFormat">Apply Format</digi:trn>
			</html:submit>&nbsp; 
			
				<input type="hidden" name="apply" value="true">
				<html:hidden property="resetFormat" value="false"/>
				<html:button styleClass="dr-menu" onclick="ResetCustom();" property="applyFormat">
				<digi:trn key="rep:filer:ResetFormat">Reset</digi:trn>
			</html:button>
	</div>
</div>
</digi:form>