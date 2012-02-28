 <%@ page pageEncoding="UTF-8" %>
 <%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
 <%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
 <%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
 <%@ taglib uri="/taglib/struts-html" prefix="html" %>
 <%@ taglib uri="/taglib/digijava" prefix="digi" %>
 <%@ taglib uri="/taglib/category" prefix="category" %>
 <%@ taglib uri="/taglib/jstl-core" prefix="c" %>
 <%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
 <%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
 <%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
 <%@ taglib uri="/taglib/aim" prefix="aim" %>
 <%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-calendar.js"/>"></script>

<!-- invoked to close myself and reload my parent (after save was performed) -->
<logic:present name="close">
<script type="text/javascript">
	window.opener.location.href = window.opener.location.href;
	window.close();
</script>	
</logic:present>

<script type="text/javascript">
function clearDefault(editBox)
{
		if(editBox.value=='Amount') editBox.value='';
}	

function selectOrganisation1() {
		openNewWindow(650, 420);
		<digi:context name="selectOrganization" property="context/module/moduleinstance/selectOrganization.do?orgSelReset=true&edit=true&step=3" />
		document.aimEditActivityForm.action = "<%= selectOrganization %>";
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
}
</script>

<!-- code for rendering that nice calendar -->
<bean:define id="langBean" name="org.digijava.kernel.navigation_language" scope="request" type="org.digijava.kernel.entity.Locale" toScope="page" />
<bean:define id="lang" name="langBean" property="code" scope="page" toScope="page" />

<script type="text/javascript">
	var myCalendarModel = new DHTMLSuite.calendarModel();
	
	myCalendarModel.setLanguageCode('<bean:write name="lang" />'); 
	calendarObjForForm = new DHTMLSuite.calendar({callbackFunctionOnDayClick:'getDateFromCalendar',isDragable:false,displayTimeBar:false,calendarModelReference:myCalendarModel}); 
		
	function getDateFromCalendar(inputArray)
	{
		var references = calendarObjForForm.getHtmlElementReferences(); // Get back reference to form field.
		references.dueDate.value = inputArray.year + '-' + inputArray.month + '-' + inputArray.day;
		calendarObjForForm.hide();			
	}	

	function pickDate(buttonObj,inputObject)
	{
		calendarObjForForm.setCalendarPositionByHTMLElement(inputObject,0,inputObject.offsetHeight-80);	// Position the calendar right below the form input
		calendarObjForForm.setInitialDateFromInput(inputObject,'yyyy-mm-dd');	// Specify that the calendar should set it's initial date from the value of the input field.
		calendarObjForForm.addHtmlElementReference('dueDate',inputObject);	// Adding a reference to this element so that I can pick it up in the getDateFromCalendar below(myInput is a unique key)
		if(calendarObjForForm.isVisible()){
			calendarObjForForm.hide();
		}else{
			calendarObjForForm.resetViewDisplayedMonth();	// This line resets the view back to the inital display, i.e. it displays the inital month and not the month it displayed the last time it was open.
			calendarObjForForm.display();
		}		
	}
	
	var enterBinder	= new EnterHitBinder('addEUActBtn');
</script>		

<body onLoad="load()">
<digi:instance property="aimEUActivityForm" />
<digi:form action="/editEUActivity.do" method="post">

<input type="hidden" name="edit" value="true">
<html:hidden property="id"/>

<digi:errors/>

<table width="100%" border="0" cellspacing="2" cellpadding="2" align="center" class="box-border-nopadding">
	<tr>
		<td colspan="4" bgcolor="#006699" class="textalb" align="center">
			<digi:trn key="aim:addEditActivity">Add/Edit Activity</digi:trn>
		</td>
	</tr>
	<tr>
		<td align="right">
			<font color="red">*</font>
			<b><digi:trn key="aim:addEditActivityName">Activity name:</digi:trn></b>
		</td>		
		<td>
			<html:text property="name" size="30"/> 
		    <field:display name="Costing Activity Id" feature="Costing">&nbsp;&nbsp;
		    	<b><digi:trn key="aim:addEditActivityID">Activity ID:</digi:trn></b> 
		    	<html:text property="textId" size="10"/> 
			</field:display>
		</td>
	</tr>
	<tr>
		<td align="right">
		<b><digi:trn key="aim:addEditActivityInputs">Inputs:</digi:trn></b>
		</td>
		<td>
			<html:textarea property="inputs" rows="3" cols="90" styleClass="inp-text"/>
		</td>
	</tr>
	
		<tr>
		<td align="right"><font color="red">*</font>
		<b><digi:trn key="aim:addEditActivityTotalCost">Total Cost:</digi:trn></b>
		</td>
		<td>
			<html:text property="totalCost" style="text-align:right"/>
			<html:select property="totalCostCurrencyId" styleClass="inp-text">
			<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
			<html:optionsCollection name="aimEUActivityForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
			</html:select>
		</td>
	</tr>


	<tr>
		<td>&nbsp;</td>		
		<td bgcolor="#006699" class="textalb" align="center">
		<b><digi:trn key="aim:addEditActivityContributions">Contributions</digi:trn></b>
		</td>
	</tr>
	<tr>
		<td>&nbsp;
		</td>
		<td align="center">
		<table width="100%" cellpadding="2" cellspacing="0">
	<tr>
	  <td height="12">&nbsp;</td>
	  <td> <strong>
	    <digi:trn key="aim:addEditActivityAmount">Amount</digi:trn>
	    </strong></td>
	  <td> <strong>
	    <digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn>
	    </strong></td>
	  <td> <strong>
	    <digi:trn key="aim:addEditActivityTypeOfAssistance">Type of Assistance</digi:trn>
	    </strong></td>
	  <field:display name="Contribution Donors" feature="Costing">
        <td>  <strong>
          <digi:trn key="aim:addEditActivityDonor">Donor</digi:trn>
        </strong></td>
	  </field:display>
	  <td><strong>
	    <digi:trn key="aim:addEditActivityFinancingInstrument">Financing Instrument</digi:trn>
	    </strong></td>
	</tr>
						<logic:iterate name="aimEUActivityForm" property="contrCurrId" indexId="idx"
							id="currContr" scope="page">
							<tr>
							<td align="right" valign="top" nowrap>
								<font color="red">*</font>
								<input type="checkbox" name="deleteContrib" value='<bean:write name="idx"/>'>						    
							</td>
							<td align="left" valign="top" nowrap>
								<input name='contrAmount' type='text' style="text-align:right" onClick="clearDefault(this)" value='${aimEUActivityForm.contrAmount[idx]}' size="4">
							</td>
							<td align="left" valign="top" nowrap>
								<select name="contrCurrId" style="width: 100px" class="inp-text">
                              	<option value="-1">
	                               <digi:trn key="aim:addEditActivitySelect">Select</digi:trn>
	                            </option>
                                <logic:iterate name="aimEUActivityForm" property="currencies" id="currency" indexId="cIdx" type="org.digijava.module.aim.dbentity.AmpCurrency"> <option value='<bean:write name="currency" property="ampCurrencyId"/>' 
                                  <c:if test="${ aimEUActivityForm.contrCurrId[idx] == currency.ampCurrencyId }">selected</c:if>>
                                  <bean:write name="currency" property="currencyName"/>
                                  </option>
                                </logic:iterate>
                            	</select>
                            </td>
							<td align="left" valign="top" nowrap>
							<c:set var="translation">
										<digi:trn key="aim:addEditActivitySelect">Select</digi:trn>
									</c:set>
										<c:set var="contrFinTypeIdIndex">contrFinTypeId[${idx}]</c:set>
										<category:showoptions firstLine="${translation}" name ="aimEUActivityForm"  property="${contrFinTypeIdIndex}"  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.TYPE_OF_ASSISTENCE_KEY %>" styleClass="inp-text" />
							</td>
							<td align="left" valign="top" nowrap><field:display name="Contribution Donors" feature="Costing">
                              <c:set var="valueId"> contrDonorId${idx} </c:set>
                              <c:set var="nameId"> nameContrDonorId${idx} </c:set>
                              <input   name='contrDonorId' type="hidden" id="${valueId}" style="text-align:right" value='${aimEUActivityForm.contrDonorId[idx]}' size="4"/>
                              <input name="contrDonorName" type='text' id="${nameId}" style="text-align:right" value='${aimEUActivityForm.contrDonorName[idx]}' size="10" style="background-color:#CCCCCC" onKeyDown="return false"/>
                              <aim:addOrganizationButton useClient="true" htmlvalueHolder="${valueId}" htmlNameHolder="${nameId}" styleClass="dr-menu"><digi:trn key="aim:addEditActivitySelectDonorDots">....</digi:trn></aim:addOrganizationButton>
                            </field:display></td>
							<td align="left" valign="top" nowrap>
                                  <c:set var="translation">
										<digi:trn key="aim:addEditActivitySelect">Select</digi:trn>
									</c:set>
										<c:set var="contrFinInstrIdIndex">contrFinInstrId[${idx}]</c:set>
										<category:showoptions firstLine="${translation}" name ="aimEUActivityForm"   property="${contrFinInstrIdIndex}"  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.FINANCING_INSTRUMENT_KEY %>" styleClass="inp-text" />
                                </td>
							</tr>
						</logic:iterate>			
		</table>
	  <tr><td colspan="2" align="center">
				<html:submit styleClass="dr-menu" property="addFields"><digi:trn key="aim:addEditActivityAddContribution">Add Contribution</digi:trn></html:submit>&nbsp;&nbsp;
				<html:submit styleClass="dr-menu" property="removeFields"><digi:trn key="aim:addEditActivityDeleteSelected">Delete Selected</digi:trn></html:submit>				
		</td></tr>
	
	
	
	<tr>
		<td align="right">
		<b><digi:trn key="aim:addEditActivityAssumptions">Assumptions:</digi:trn></b>
		</td>
		<td>
			<html:textarea property="assumptions" rows="3" cols="90" styleClass="inp-text"/>
		</td>
	</tr>

	<tr>
		<td align="right">
		<b><digi:trn key="aim:addEditActivityProgress">Progress:</digi:trn></b>
		</td>
		<td>
			<html:textarea property="progress" rows="3" cols="90" styleClass="inp-text"/>
		</td>
	</tr>	

	<tr>
		<td align="right"><font color="red">*</font>
		<b><digi:trn key="aim:addEditActivityDueDate">Due Date:</digi:trn></b>
		</td>
		<td>
			<html:text  property="dueDate" styleClass="inp-text"/>
				<a onClick="pickDate(this,document.aimEUActivityForm.dueDate)">
				  <img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
				</a>
		</td>
	</tr>	


	<tr>
		<td colspan="2" align="center">
		<html:submit styleClass="dr-menu" property="save" styleId="addEUActBtn">
		 <digi:trn key="aim:addEditActivitySave">Save</digi:trn>
		</html:submit>
		
		&nbsp;&nbsp;
		<html:button styleClass="dr-menu" property="cancel" onclick="window.close();">
		<digi:trn key="aim:addEditActivitySlose">Close</digi:trn>

		</html:button>
		</td>
		
		
	</tr>
	<tr>
	<td colspan="2">
		<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
		 <font color="red"><digi:trn>All amounts are in thousands (000)</digi:trn></font>
		</gs:test>
		</td>
	</tr>	

	
</table>

</digi:form>