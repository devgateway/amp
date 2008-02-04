<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/dhtml-suite-for-applications.js"/>"></script>

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
</script>		

<body onload="load()">
<digi:instance property="aimIPAContractForm" />
<digi:form action="/editIPAContract.do" method="post">

<input type="hidden" name="edit" value="true">
<html:hidden property="id"/>
<html:hidden property="indexId"/>

<digi:errors/>

<table width="100%" border="0" cellspacing="2" cellpadding="2" align="center" class="box-border-nopadding">
<tr><td colspan="4" bgcolor="#006699" class="textalb" align="center">
<digi:trn key="aim:IPA:popup:Title">Add/Edit IPA Contract</digi:trn>
</td></tr>
	<tr>
		<td align="right">
		<b><digi:trn key="aim:IPA:popup:name">Contract name:</digi:trn></b>
		</td>
		<td>
		         	<html:text property="contractName" size="30"/> 
		</td>
	
	</tr>

	<tr>
		<td align="right">
		<b><digi:trn key="aim:IPA:popup:description">Description:</digi:trn></b>
		</td>
		<td>
			<html:textarea property="description" rows="3" cols="90" styleClass="inp-text"/>
		</td>
	</tr>
	
	<tr>
		<td align="right">
		<b><digi:trn key="aim:IPA:popup:actCat">Activity Category:</digi:trn></b>
		</td>
		<td>
			<html:select property="activityCategoryId" styleClass="inp-text">
			<option value="-1"><digi:trn key="aim:select">Select</digi:trn></option>
			<html:optionsCollection name="aimIPAContractForm" property="activitiyCategories" value="id" label="value"/>
			</html:select>
		</td>
	</tr>
	
		<tr>
		<td align="right">
		<b><digi:trn key="aim:IPA:popup">Start of Tendering:</digi:trn></b>
		</td>
		<td>
			<html:text readonly="true" property="startOfTendering" styleClass="inp-text"/>
			<input type="button" class="buton" value='<digi:trn key="aim:addEditActivityPickDate">Pick date</digi:trn>' onclick="pickDate(this,document.aimIPAContractForm.startOfTendering);"></td>
		
	</tr>	

		<tr>
		<td align="right">
		<b><digi:trn key="aim:IPA:popup:signatureOfContract">Signature of Contract:</digi:trn></b>
		</td>
		<td>
			<html:text readonly="true" property="signatureOfContract" styleClass="inp-text"/>
			<input type="button" class="buton" value='<digi:trn key="aim:addEditActivityPickDate">Pick date</digi:trn>' onclick="pickDate(this,document.aimIPAContractForm.signatureOfContract);"></td>
		
	</tr>	
          <tr>
	<td align="right">
		<b><digi:trn key="aim:IPA:popup:contractingOrg">Contracting Organisation:</digi:trn></b>
	</td>
         <td>
         <html:select property="contrOrg" styleClass="inp-text">
			<option value="-1"><digi:trn key="aim:selectOrganisation">Select Organisation</digi:trn></option>
			<html:optionsCollection name="aimIPAContractForm" property="organisations" value="ampOrgId" label="name"/>
			</html:select>
                     </td>
	</tr>
	
		<tr>
		<td align="right">
		<b><digi:trn key="aim:IPA:popup:contractCompletion">Contract Completion:</digi:trn></b>
		</td>
		<td>
			<html:text readonly="true" property="contractCompletion" styleClass="inp-text"/>
			<input type="button" class="buton" value='<digi:trn key="aim:addEditActivityPickDate">Pick date</digi:trn>' onclick="pickDate(this,document.aimIPAContractForm.contractCompletion);"></td>
		
	</tr>	
         <tr>
		<td align="right">
		<b><digi:trn key="aim:IPA:popup:status">Status:</digi:trn></b>
		</td>
		<td>
			<html:select property="statusId" styleClass="inp-text">
			<option value="-1"><digi:trn key="aim:select">Select</digi:trn></option>
			<html:optionsCollection name="aimIPAContractForm" property="statuses" value="id" label="value"/>
			</html:select>
                     </td>
	</tr>	
         
        
	

		<tr>
		<td align="right">
		<b><digi:trn key="aim:IPA:popup:totalECContribution">Total EC Contribution:</digi:trn></b>
		</td>
		</tr>
	
		<tr>
		<td align="right">
		<b>IB:</b>
		</td>
		<td>
			<html:text property="totalECContribIBAmount" style="text-align:right"/>
			<html:select property="totalECContribIBCurrency" styleClass="inp-text">
			<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
			<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
			</html:select>
		</td>
	</tr>

		<tr>
		<td align="right">
		<b>INV:</b>
		</td>
		<td>
			<html:text property="totalECContribINVAmount" style="text-align:right"/>
			<html:select property="totalECContribINVCurrency" styleClass="inp-text">
			<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
			<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
			</html:select>
		</td>
	</tr>





		<tr>
		<td align="right">
		<b><digi:trn key="aim:IPA:popup:totalNationalContribution">Total National Contribution:</digi:trn></b>
		</td>
		</tr>
	
		<tr>
		<td align="right">
		<b>Central:</b>
		</td>
		<td>
			<html:text property="totalNationalContribCentralAmount" style="text-align:right"/>
			<html:select property="totalNationalContribCentralCurrency" styleClass="inp-text">
			<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
			<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
			</html:select>
		</td>
	</tr>

		<tr>
		<td align="right">
		<b>Regional:</b>
		</td>
		<td>
			<html:text property="totalNationalContribRegionalAmount" style="text-align:right"/>
			<html:select property="totalNationalContribRegionalCurrency" styleClass="inp-text">
			<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
			<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
			</html:select>
		</td>
		</tr>

	<tr>
		<td align="right">
		<b>IFIs:</b>
		</td>
		<td>
			<html:text property="totalNationalContribIFIAmount" style="text-align:right"/>
			<html:select property="totalNationalContribIFICurrency" styleClass="inp-text">
			<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
			<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
			</html:select>
		</td>
		</tr>



	<tr>
		<td align="right">
		<b><digi:trn key="aim:IPA:popup:totalPrivateContribution">Total Private Contribution:</digi:trn></b>
		</td>
		</tr>
	
		<tr>
		<td align="right">
		<b>IB:</b>
		</td>
		<td>
			<html:text property="totalPrivateContribAmount" style="text-align:right"/>
			<html:select property="totalPrivateContribCurrency" styleClass="inp-text">
			<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
			<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
			</html:select>
		</td>
	</tr>




	<tr>
		<td>&nbsp;</td>
		<td bgcolor="#006699" class="textalb" align="center">
		<b><digi:trn key="aim:IPA:popup:disbursements">Disbursements:</digi:trn></b>
		</td>
	</tr>
	<tr>
		<td>&nbsp;
		</td>
		<td>
		<table>
	
	<logic:notEmpty name="aimIPAContractForm" property="contractDisbursements">
						<c:forEach  items="${aimIPAContractForm.contractDisbursements}" var="contractDisbursement"  varStatus="idx" >
							<tr>
                                                         <td>
                                                            <html:hidden property="${contractDisbursement}" value="${id}"/>
                                                              <html:multibox property="selContractDisbursements" value="${idx.count}"/>
                                                                
                                                         </td>
							<td align="right" valign="top">
							<html:select indexed="true" name="contractDisbursement" property="adjustmentType">
							<html:option value="0">Actual</html:option>
							<html:option value="1">Planned</html:option>							
							</html:select>
							</td>
							<td align="right" valign="top">
							<html:text indexed="true" name="contractDisbursement" property="amount">Amount</html:text>
							</td>
							<td align="right" valign="top">
                                                         <html:select name="contractDisbursement" indexed="true" property="currCode" styleClass="inp-text">
							<html:optionsCollection name="aimIPAContractForm" property="currencies" value="currencyCode" label="currencyName"/>
                                                        </html:select>
							</td>
							<td align="right" valign="top"  nowrap>
							<html:text readonly="true" indexed="true" name="contractDisbursement" property="disbDate" styleClass="inp-text" styleId="date${idx.count}"/>
							<input type="button" class="buton" value='<digi:trn key="aim:addEditActivityPickDate">Pick date</digi:trn>' onclick="pickDate(this,document.getElementById('date${idx.count}'))"/>							
							</td>
							</tr>
						</c:forEach>
	</logic:notEmpty>						
		</table>		
		</td>		
		</tr>		
	
	<tr>
		<td>&nbsp;
		</td>
		<td>
		<tr><td colspan="2" align="center">
				<html:submit styleClass="buton" property="addFields"><digi:trn key="aim:IPA:popup:addDisbursement">Add Disbursement</digi:trn></html:submit>&nbsp;&nbsp;
				<html:submit styleClass="buton" property="removeFields"><digi:trn key="aim:IPA:popup:deleteSelected">Delete Selected</digi:trn></html:submit>				
		</td></tr>
	
	
	


	<tr>
		<td colspan="2" align="center">
		<html:submit styleClass="buton" property="save"><digi:trn key="aim:addEditActivityOK">OK</digi:trn></html:submit>
		
		&nbsp;&nbsp;
		<html:button styleClass="buton" property="cancel" onclick="window.close();">
		<digi:trn key="aim:addEditActivityCancel">Cancel</digi:trn>
		</html:button>
		</td>
	</tr>	

	
</table>

</digi:form>