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
<digi:form action="/reportsFilterPicker.do" name="aimReportsFilterPickerForm3" type="aimReportsFilterPickerForm"  onsubmit="return validateFormat()">

<center><b><digi:trn>Number Format</digi:trn></b></center>
<hr/>
<table border="0" cellspacing="2" cellpadding="2" align=center>
	<tr>
		<td align="right" style="font-family: Arial,sans-serif;font-size: 11px">
			<digi:trn key="aim:formatPicket:decimalSymbol">Decimal Separator</digi:trn>
		</td>
        <td style="font-family: Arial,sans-serif;font-size: 11px">
        	<html:select styleClass="dropdwn_sm" onchange="initFormatPopup();" property="customDecimalSymbol" styleId="customDecimalSymbol">
				<html:option value=",">,</html:option>
				<html:option value=".">.</html:option>
				<html:option value="CUSTOM"><digi:trn key="aim:formatPicket:custom">Custom</digi:trn></html:option>
            </html:select>
            <html:text styleClass="inputx" size="3" disabled="true" property="customDecimalSymbolTxt" onchange="initFormatPopup()" /> 
        </td>
	</tr>
	<tr>
		<td align="right" style="font-family: Arial,sans-serif;font-size: 11px">
			<digi:trn key="aim:formatPicket:maxFracDigits">Maximum Fraction Digits</digi:trn>
		</td>
    	<td>
			<html:select styleClass="dropdwn_sm" property="customDecimalPlaces" styleId="customDecimalPlaces" onchange="initFormatPopup();">
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
			</html:select>
			<html:text styleClass="inputx" disabled="true" size="2" maxlength="2" property="customDecimalPlacesTxt" onchange="initFormatPopup()" />
         </td>
	</tr>
	<tr>
		<td align="right" style="font-family: Arial,sans-serif;font-size: 11px">
			<digi:trn key="aim:formatPicket:UseGrouping">Use Grouping Separator</digi:trn>
		</td>
	    <td style="font-family: Arial,sans-serif;font-size: 11px">
	    	<html:checkbox property="customUseGrouping" styleId="customUseGrouping"  onchange ="initFormatPopup();" />
	    </td>
	</tr>
	<tr>
		<td align="right" style="font-family: Arial,sans-serif;font-size: 11px">
			<digi:trn key="aim:formatPicket:GroupingSeparator">Grouping Separator</digi:trn>
		</td>
	    <td>
	    	<html:select styleClass="dropdwn_sm" property="customGroupCharacter" styleId="customGroupCharacter" onchange="initFormatPopup();">
              <html:option value=".">.</html:option>
              <html:option value=",">,</html:option>
              <html:option value="CUSTOM">
                <digi:trn key="aim:formatPicket:custom">Custom</digi:trn>
              </html:option>
            </html:select>
            <html:text styleClass="inputx" disabled="true" size="2" maxlength="1" property="customGroupCharacterTxt" onchange="initFormatPopup()"  />
		</td>
	</tr>
	<tr>
		<td align="right" style="font-family: Arial,sans-serif;font-size: 11px">
			<digi:trn key="aim:formatPicket:GroupSize">Group Size</digi:trn>
		</td>
		<td>
			<html:text disabled="true" property="customGroupSize" size="2" maxlength="1" onchange="initFormatPopup();"/>
		</td>
	</tr>
	<tr>
		<td align="right" style="font-family: Arial,sans-serif;font-size: 11px">
			<digi:trn key="aim:formatPicket:Amountinthousands">Amount in thousands</digi:trn>;
		</td>
		<td>
			<html:checkbox property="amountinthousands" styleId="customAmountinThousands"  onchange ="initFormatPopup();" />
		</td>
	  </tr>
	<tr>
		<td align="right" style="font-family: Arial,sans-serif;font-size: 11px">
			<digi:trn key="aim:formatPicket:Example">Example</digi:trn>
		</td>
	    <td style="font-family: Arial,sans-serif;font-size: 11px">
			<aim:formatNumber value="123456789.928" />
		</td>
	</tr>
</table>
<br/>
<br>
<center><b><digi:trn>Other Settings</digi:trn></b></center>
<hr/>
<table border="0" cellspacing="2" cellpadding="2" align=center>
	<tr>
		<td align=right style="font-family: Arial,sans-serif;font-size: 11px">
			<digi:trn>Currency</digi:trn>
		</td>
		<td>
			<html:select property="currency" styleClass="dropdwn_sm">
				<html:optionsCollection property="currencies" value="ampCurrencyId" label="currencyName" />
			</html:select>
		</td>
	</tr>
	<tr>
		<td align="right" style="font-family: Arial,sans-serif;font-size: 11px">
			<digi:trn>Calendar</digi:trn>
		</td>
		<td>
			<html:select property="calendar" styleClass="dropdwn_sm">
				<html:optionsCollection property="calendars" value="ampFiscalCalId" label="name" />
			</html:select>
		</td>
	</tr>
	
	<logic:notEqual name="widget" value="true" scope="request">
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td align="right" style="font-family: Arial,sans-serif;font-size: 11px">
				<digi:trn>Year Range</digi:trn>
			</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td align="right" style="font-family: Arial,sans-serif;font-size: 11px"> 
				<digi:trn>From</digi:trn>: &nbsp;
			</td>
			<td> 
				<html:select  styleClass="dropdwn_sm" property="renderStartYear">
					<html:option value="-1">
						<digi:trn key="rep:filer:All">All</digi:trn>
					</html:option>
					<html:optionsCollection property="fromYears" label="wrappedInstance" value="wrappedInstance" />
				</html:select> 
				&nbsp; <digi:trn>To</digi:trn>: &nbsp; 
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
<br />
<center>
	<html:hidden property="ampReportId" /> 
	<html:hidden property="defaultCurrency" />
	<html:submit styleClass="buttonx"  property="applyFormat">
		<digi:trn key="rep:filer:ApplyFormat">Apply Format</digi:trn>
	</html:submit>&nbsp; 
	<input type="hidden" name="apply" value="true">
	<html:hidden property="resetFormat" value="false"/>
	<html:button styleClass="buttonx" onclick="ResetCustom();" property="applyFormat">
		<digi:trn key="rep:filer:ResetFormat">Reset</digi:trn>
	</html:button>
</center>
</digi:form>