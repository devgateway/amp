<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/dhtml-suite-for-applications.js"/>"></script>

<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp" flush="true" />
</script>

<jsp:include page="scripts/newCalendar.jsp" flush="true" />

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
function fnChk(frmContrl){
  <c:set var="errMsgAddNumericValue">
  <digi:trn key="aim:addNumericValueErrorMessage">
  Please enter numeric value only
  </digi:trn>
  </c:set>
  if (isNaN(frmContrl.value)) {
    alert("${errMsgAddNumericValue}");
    frmContrl.value = "";
    return false;
  }
  return true;
}

function validate(){
    if (trim(document.aimIPAContractForm.contractName.value) == "") {
        <c:set var="translation">
        <digi:trn key="aim:pleaseEnterContractName">Please enter Contract Name</digi:trn>
        </c:set>
        alert("${translation}");
            document.aimIPAContractForm.contractName.focus();
            return false;
        }
    <c:set var="errMsgSelectCurrency">
    <digi:trn key="aim:PleaseSelectCurrency">
    Please Select Currency
    </digi:trn>
    </c:set>
    if((document.aimIPAContractForm.totalECContribIBAmount.value!=''
        &&document.aimIPAContractForm.totalECContribIBCurrency.value==-1)||
    (document.aimIPAContractForm.totalECContribINVAmount.value!=''
        &&document.aimIPAContractForm.totalECContribINVCurrency.value==-1)||
    (document.aimIPAContractForm.totalNationalContribIFIAmount.value!=''
        &&document.aimIPAContractForm.totalNationalContribIFICurrency.value==-1)||
    (document.aimIPAContractForm.totalNationalContribCentralAmount.value!=''
        &&document.aimIPAContractForm.totalNationalContribCentralCurrency.value==-1)||
    (document.aimIPAContractForm.totalNationalContribRegionalAmount.value!=''
        &&document.aimIPAContractForm.totalNationalContribRegionalCurrency.value==-1)||
    (document.aimIPAContractForm.totalPrivateContribAmount.value!=''
        &&document.aimIPAContractForm.totalPrivateContribCurrency.value==-1)){
        alert("${errMsgSelectCurrency}");
            return false;
        }
        return true;
    }

function selectOrganisation1() {
		openNewWindow(650, 420);
		<digi:context name="selectOrganization" property="context/module/moduleinstance/selectOrganization.do?orgSelReset=true&edit=true&step=3" />
		document.aimEditActivityForm.action = "<%= selectOrganization %>";
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
}

function addDisb() {
	<digi:context name="addFields" property="context/module/moduleinstance/editIPAContract.do?addFields=true" />
	document.aimIPAContractForm.action = "<%=addFields%>";
	document.aimIPAContractForm.target = "_self";
	document.aimIPAContractForm.submit();
}
function delDisb() {
	<digi:context name="addFields" property="context/module/moduleinstance/editIPAContract.do?removeFields=true" />
	document.aimIPAContractForm.action = "<%=addFields%>";
	document.aimIPAContractForm.target = "_self";
	document.aimIPAContractForm.submit();
}
</script>

<!-- code for rendering that nice calendar -->


<body onload="load()">
<digi:instance property="aimIPAContractForm" />
<digi:form action="/editIPAContract.do" method="post">

<input type="hidden" name="edit" value="true">
<html:hidden property="id"/>
<html:hidden property="indexId"/>

<digi:errors/>

<table width="100%" border="0" cellspacing="2" cellpadding="2" align="center" class="box-border-nopadding">
<tr><td colspan="2" bgcolor="#006699" class="textalb" align="center">
<b><digi:trn key="aim:IPA:newPopup:title">Add/Edit Contracting</digi:trn></b>
</td></tr>
<field:display name="Contract Name" feature="Contracting">
	<tr>
		<td align="left" nowrap>
                     <FONT color=red>*</FONT>
		<digi:trn key="aim:IPA:newPopup:name">Contract Name</digi:trn>
		</td>
		<td>
		         	<html:text property="contractName" size="90"/> 
		</td>
	</tr>
</field:display>
<field:display name="Contract Description" feature="Contracting">
	<tr>
		<td align="left" valign="top" nowrap>
			<digi:trn key="aim:IPA:newPopup:description">Contract Description</digi:trn>
		</td>
		<td>
			<html:textarea property="description" rows="8" cols="87" styleClass="inp-text"/>
		</td>
	</tr>
</field:display>
	<field:display name="Contracting Activity Category" feature="Contracting">
	<tr>
		<td align="left"  nowrap>
			<digi:trn key="aim:IPA:newPopup:actCat">Activity Category</digi:trn>
		</td>
		<td>
			<html:select property="activityCategoryId" styleClass="inp-text">
			<option value="-1"><digi:trn key="aim:select">Select</digi:trn></option>
			<logic:iterate id="actCat" name="aimIPAContractForm" property="activitiyCategories">
				<c:set var="trn">
					<digi:trn key="aim:ipa:popup:${actCat.id}">${actCat.value}</digi:trn>
				</c:set>
			</logic:iterate>			
				<html:option value="${actCat.id}">${trn}</html:option>			
			</html:select>
			
			&nbsp;&nbsp;&nbsp;
			!!!<digi:trn key="aim:IPA:newPopup:contractType">Contract Type</digi:trn>
			&nbsp;&nbsp;
			<html:select property="activityCategoryId" styleClass="inp-text">
			<option value="-1"><digi:trn key="aim:select">Select</digi:trn></option>
			<logic:iterate id="actCat" name="aimIPAContractForm" property="activitiyCategories">
				<c:set var="trn">
					<digi:trn key="aim:ipa:popup:${actCat.id}">${actCat.value}</digi:trn>
				</c:set>
			</logic:iterate>			
				<html:option value="${actCat.id}">${trn}</html:option>			
			</html:select>
		</td>
	</tr>
         </field:display>
         
         <field:display name="Contracting Type" feature="Contracting">
	<tr>
		<td align="left"  nowrap>
			<digi:trn key="aim:IPA:newPopup:actType">Activity Type</digi:trn>
		</td>
		<td>
			<html:select property="typeId" styleClass="inp-text">
			<option value="-1"><digi:trn key="aim:select">Select</digi:trn></option>
			<logic:iterate id="type" name="aimIPAContractForm" property="types">
				<c:set var="trn">
					<digi:trn key="aim:ipa:popup:${type.id}">${type.value}</digi:trn>
				</c:set>
				<html:option value="${type.id}">${trn}</html:option>			
			</logic:iterate>			
			</html:select>
		</td>
	</tr>
         </field:display>
         
         
	<tr><td colspan="2" bgcolor="#006699" class="textalb" align="center">
		<b><digi:trn key="aim:IPA:newPopup:details">Details</digi:trn></b>
	</td></tr>
         
	<tr>
	<td colspan="2">
	<table cellpadding="2" cellspacing="2" width="100%">
	<tr>
		<field:display name="Contracting Start of Tendering" feature="Contracting">
			<td align="left">
				<digi:trn key="aim:IPA:newPopup">Start of Tendering</digi:trn>
			</td>
			<td align="left">
				<html:text readonly="true" property="startOfTendering" styleClass="inp-text" styleId="startOfTendering"/>
				<a id ="startOfTenderingDate" href='javascript:pickDateById("startOfTenderingDate","startOfTendering")'>
					<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
				</a>
				&nbsp;&nbsp;
			</td>
		</field:display>

		<field:display name="Contract Validity Date" feature="Contracting">
			<td align="left">
				<digi:trn key="aim:IPA:newPopup:contractValidityDate">Contract Validity Date</digi:trn>
			</td>
			<td align="left">
				<html:text readonly="true" property="contractValidity" styleClass="inp-text" styleId="contractValidity"/>
				<a id="contractValidityDate" href='javascript:pickDateById("contractValidityDate","contractValidity")'>
					<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
				</a>
				&nbsp;&nbsp;
			</td>
		</field:display>
		
		<field:display name="Contracting Tab Status" feature="Contracting">
			<td align="left">
				<digi:trn key="aim:IPA:newPopup:status">Status</digi:trn>
			</td>
			<td align="left">
				<html:select property="statusId" styleClass="inp-text">
				<option value="-1"><digi:trn key="aim:select">Select</digi:trn></option>
				<logic:iterate id="actstat" name="aimIPAContractForm" property="statuses">
				<c:set var="trn">
				<digi:trn key="aim:ipa:popup:${actstat.id}">${actstat.value}</digi:trn>
				</c:set>
				<html:option value="${actstat.id}">${trn}</html:option>			
				</logic:iterate>			
				</html:select>
				&nbsp;&nbsp;
			</td>
		</field:display>
	</tr>
	<tr>
		<field:display name="Signature of Contract" feature="Contracting">
			<td align="left">
				<digi:trn key="aim:IPA:newPopup:signatureOfContract">Signature of Contract</digi:trn>
			</td>
			<td align="left">
				<html:text readonly="true" property="signatureOfContract" styleClass="inp-text" styleId="signatureOfContract"/>
				<a id="signatureOfContractDate" href='javascript:pickDateById("signatureOfContractDate","signatureOfContract")'>
					<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
				</a>
			</td>
		</field:display>
		<field:display name="Contract Completion" feature="Contracting">
			<td align="left">
				<digi:trn key="aim:IPA:newPopup:contractCompletion">Contract Completion</digi:trn>
			</td>
			<td align="left">
				<html:text readonly="true" property="contractCompletion" styleClass="inp-text" styleId="contractCompletion"/>
				<a id="contractCompletionDate" href='javascript:pickDateById("contractCompletionDate","contractCompletion")'>
					<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
				</a>
			</td>
		</field:display>
	</tr>
	<tr>
		<td colspan="6" align="left">
			<field:display name="Contract Organization" feature="Contracting">
			<a style="cursor:pointer; color: blue; font-size: x-small;" onClick="addOrg()" /> 
				<digi:trn key="aim:IPA:newPopup:addOrganization">Add Organization</digi:trn>
			</a>
			&nbsp;	
			<a style="cursor:pointer; color: blue; font-size: x-small;" onClick="delOrg()" /> 
				<digi:trn key="aim:IPA:newPopup:deleteSelected">Delete Selected</digi:trn>
			</a>
			<!-- 
				<b><digi:trn key="aim:IPA:popup:contractingOrg">Contracting Organisation:</digi:trn></b>
		         <html:select property="contrOrg" styleClass="inp-text">
					<option value="-1"><digi:trn key="aim:selectOrganisation">Select Organisation</digi:trn></option>
					<logic:iterate id="actOrg" name="aimIPAContractForm" property="organisations">
						<html:option value="${actOrg.ampOrgId}">${actOrg.name}</html:option>			
					</logic:iterate>
					</html:select>
			 -->
			</field:display>
		    
		</td>
	</tr>
	<field:display name="Contracting Organization Text" feature="Contracting">
		<tr>
			<td align="left">
				<digi:trn key="aim:IPA:newPopup:contractingOrganizationDescription">Description</digi:trn>
			</td>
			<td colspan="5" align="left">
				<html:textarea property="contractingOrganizationText" rows="5" cols="95" styleClass="inp-text"/>
			</td>
		</tr>				
	</field:display>
	</table>
	</td>
	</tr>	

	<tr><td colspan="2" bgcolor="#006699" class="textalb" align="center">
		<b><digi:trn key="aim:IPA:newPopup:fundingAllocation">Funding Allocation</digi:trn></b>
	</td></tr>

	<tr>
	<td colspan="2">
	<table cellpadding="2" cellspacing="2" width="100%">
		<tr>
			<field:display name="Contracting Total Amount" feature="Contracting">
				<td align="left">
					<digi:trn key="aim:ipa:newPopup:totalAmount">Total Amount</digi:trn>
				</td>
				<td align="left">
					<html:text property="totalAmount" style="text-align:right" onkeyup="fnChk(this)"/>
				</td>
				<td align="left">
					<html:select property="totalAmountCurrency" styleClass="inp-text">
						<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
						<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
					</html:select>
				</td>
			</field:display>
		</tr>
		<field:display name="Total EC Contribution" feature="Contracting">
			<tr>
				<td colspan="6" align="center">
					<b><digi:trn key="aim:ipa:newPopup:totalECContribution">Total EC Contribution</digi:trn></b>
				</td>
			</tr>
			<tr>
			<field:display name="Contracting IB" feature="Contracting">
				<td align="left">
					<digi:trn key="aim:ipa:newPopup:ib">IB</digi:trn>
				</td>
				<td align="left">
					<html:text property="totalECContribIBAmount" style="text-align:right" onkeyup="fnChk(this)"/>
				</td>
				<td align="left">
					<html:select property="totalECContribIBCurrency" styleClass="inp-text">
						<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
						<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
					</html:select>
				</td>
		    </field:display>
		     
			<field:display name="Contracting INV" feature="Contracting">
				<td align="left">
					<digi:trn key="aim:ipa:newPopup:inv">INV</digi:trn>
				</td>
				<td align="left">
					<html:text property="totalECContribINVAmount" style="text-align:right" onkeyup="fnChk(this)"/>
				</td>
				<td align="left">
					<html:select property="totalECContribINVCurrency" styleClass="inp-text">
						<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
						<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
					</html:select>
				</td>
			</field:display>
			</tr>
		</field:display>
		<field:display name="Contracting Total National Contribution" feature="Contracting">
			<tr>
				<td align="center" colspan="6">
					<b><digi:trn key="aim:IPA:newPopup:totalNationalContribution">Total National Contribution</digi:trn></b>
				</td>
			</tr>
			<tr>
				<field:display name="Contracting Central Amount" feature="Contracting">
					<td align="left">
						<digi:trn key="aim:ipa:newPopup:central">Central</digi:trn>
					</td>
					<td align="left">
						<html:text property="totalNationalContribCentralAmount" style="text-align:right" onkeyup="fnChk(this)"/>
					</td>
					<td align="left">
						<html:select property="totalNationalContribCentralCurrency" styleClass="inp-text">
							<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
							<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
						</html:select>
					</td>
 				</field:display>

				<field:display name="Contracting IFIs" feature="Contracting">
					<td align="left">
						<digi:trn key="aim:ipa:newPopup:ifis">IFIs</digi:trn>
					</td>
					<td align="left">
						<html:text property="totalNationalContribIFIAmount" style="text-align:right" onkeyup="fnChk(this)"/>
					</td>
					<td align="left">
						<html:select property="totalNationalContribIFICurrency" styleClass="inp-text">
							<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
							<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
						</html:select>
					</td>
				</field:display>
			</tr>
			
			<tr>
				<field:display name="Contracting Regional Amount" feature="Contracting">
					<td align="left">
						<digi:trn key="aim:ipa:newPopup:regional">Regional</digi:trn>
					</td>
					<td align="left">
						<html:text property="totalNationalContribRegionalAmount" style="text-align:right" onkeyup="fnChk(this)"/>
					</td>
					<td align="left">
						<html:select property="totalNationalContribRegionalCurrency" styleClass="inp-text">
							<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
							<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
						</html:select>
					</td>
				</field:display>
			</tr>
		</field:display>
		
		<field:display name="Total Private Contribution" feature="Contracting">
			<tr>
				<td align="center" colspan="6">
					<b><digi:trn key="aim:IPA:newPopup:totalPrivateContribution">Total Private Contribution</digi:trn></b>
				</td>
			</tr>
			<tr>
				<td align="left">
					<digi:trn key="aim:ipa:newPopup:ib">IB</digi:trn>
				</td>
				<td align="left">
					<html:text property="totalPrivateContribAmount" style="text-align:right" onkeyup="fnChk(this)"/>
				</td>
				<td align="left">
					<html:select property="totalPrivateContribCurrency" styleClass="inp-text">
						<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
						<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
					</html:select>
				</td>
			</tr>
		</field:display>
		
	</table>
	</td>
	</tr>

	<field:display name="Contracting Disbursements" feature="Contracting">
	<tr><td colspan="2" bgcolor="#006699" class="textalb" align="center">
		<b><digi:trn key="aim:IPA:newPopup:disbursements">Disbursements</digi:trn></b>
	</td></tr>
		
	<tr>
		<td colspan="2" align="left">
			<field:display name="Contracting Add Disbursement" feature="Contracting">
				<a style="cursor:pointer; color: blue; font-size: x-small;" onClick="addDisb()" /> 
					<digi:trn key="aim:IPA:newPopup:addDisbursement">Add Disbursement</digi:trn>
				</a>
			</field:display>
			&nbsp;	
			<field:display name="Delete Selected" feature="Contracting">
				<a style="cursor:pointer; color: blue; font-size: x-small;" onClick="delDisb()" /> 
					<digi:trn key="aim:IPA:newPopup:deleteSelected">Delete Selected</digi:trn>
				</a>
			</field:display>				
		</td>
	</tr>
	
	<field:display name="Contracting Disbursements Global Currency" feature="Contracting">
	<tr>
		<td align="left">
			<digi:trn key="aim:ipa:newPopup:dibusrsementsGlobalCurrency">Disbursements Global Currency</digi:trn>
		</td>
		<td>
			<html:select property="dibusrsementsGlobalCurrency" styleClass="inp-text">
				<option value="-1"><digi:trn key="aim:addEditActivityCurrency">Currency</digi:trn></option>
				<html:optionsCollection name="aimIPAContractForm" property="currencies" value="ampCurrencyId" label="currencyName"/>
			</html:select>
		</td>
	</tr>
	</field:display>

	<logic:notEmpty name="aimIPAContractForm" property="contractDisbursements">
		<c:forEach  items="${aimIPAContractForm.contractDisbursements}" var="contractDisbursement"  varStatus="idx" >
			<tr>
			<td align="left" colspan="2">
				<html:hidden property="${contractDisbursement}" value="${id}"/>
				<html:multibox property="selContractDisbursements" value="${idx.count}"/>
				&nbsp;
				<html:select indexed="true" name="contractDisbursement" property="adjustmentType">
					<html:option value="0"><digi:trn key="aim:ipa:popup:actual"> Actual</digi:trn></html:option>
					<html:option value="1"><digi:trn key="aim:ipa:popup:planned">Planned</digi:trn></html:option>							
				</html:select>
				&nbsp;
				<html:text indexed="true" name="contractDisbursement" property="amount" onkeyup="fnChk(this)"><digi:trn key="aim:ipa:popup:amount">Amount</digi:trn></html:text>
				&nbsp;
				<html:select name="contractDisbursement" indexed="true" property="currCode" styleClass="inp-text">
					<html:optionsCollection name="aimIPAContractForm" property="currencies" value="currencyCode" label="currencyName"/>
				</html:select>
				&nbsp;
				<html:text readonly="true" indexed="true" name="contractDisbursement" property="disbDate" styleClass="inp-text" styleId="date${idx.count}"/>
				<a id="image${idx.count}" href='javascript:pickDateById("image${idx.count}","date${idx.count}")'>
					<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
				</a>
			</td>
			</tr>
		</c:forEach>
	</logic:notEmpty>						
	</field:display>
	
	<tr>
		<td colspan="2" align="left">
			<field:display name="Contracting Save Button" feature="Contracting">
				<html:submit styleClass="dr-menu" property="save" onclick="return validate()"><digi:trn key="aim:save">Save</digi:trn></html:submit>
			</field:display>
			&nbsp;&nbsp;
			<field:display name="Contracting Cancel Saving" feature="Contracting">
				<html:button styleClass="dr-menu" property="cancel" onclick="window.close();">
					<digi:trn key="aim:addEditActivityCancel">Cancel</digi:trn>
				</html:button>
			</field:display>
		</td>
	</tr>	
</table>
</digi:form>