<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
<!--

	function validate() {
		var titleFlag = isEmpty(document.aimEditActivityForm.componentTitle.value);
		if(titleFlag == true) {
			alert("Please enter title");			
			document.aimEditActivityForm.componentTitle.focus();
			return false;
		}
		var amtFlag = isEmpty(document.aimEditActivityForm.componentAmount.value);
		if (document.aimEditActivityForm.componentAmount.value != "0.00") {
			if(amtFlag == false) {
				amtFlag = checkAmount(document.aimEditActivityForm.componentAmount.value);
				if(amtFlag != true) {
					alert("Please enter valid amount");	
					document.aimEditActivityForm.componentAmount.focus();
					return false;
				} else {
					if (document.aimEditActivityForm.currencyCode.value == "-1") {
						alert("Please select a currency");	
						document.aimEditActivityForm.currencyCode.focus();
						return false;
					}
				}
			}				  
		}

		return true;
	}

	function addComponent() {
		var flag = validate();
		if (flag == true) {
			document.aimEditActivityForm.target = window.opener.name;
			document.aimEditActivityForm.submit();
			window.opener.document.aimEditActivityForm.currUrl.value="";
			window.close();
			return flag;
		} else {
			return false;
		}
	}	

	function load() {
		document.aimEditActivityForm.componentTitle.focus();
	}

	function unload() {
		window.opener.document.aimEditActivityForm.currUrl.value="";
	}
	function closeWindow() {
		window.opener.document.aimEditActivityForm.currUrl.value="";
		window.close();
	}
-->
</script>

<digi:instance property="aimEditActivityForm" />

<digi:form action="/componentSelected.do" method="post">

<html:hidden property="componentReset" value="false"/>
<html:hidden property="componentId"/>

<input type="hidden" name="selectedDate">

<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border=0>
	<tr><td vAlign="top">
		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left vAlign=top>
					<table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
						<tr bgcolor="#006699">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
							<a title="<digi:trn key="aim:ComponentofProject">A smaller sub project of a donor approved project</digi:trn>">								
							<digi:trn key="aim:addComponent">Add Component</digi:trn></a>
							</td></tr>
						<tr>
							<td align="center" bgcolor=#ECF3FD>
								<table cellSpacing=2 cellPadding=2>
									<tr>
										<td>
											<FONT color=red>*</FONT>
											<a title="<digi:trn key="aim:TitleforComponent">Title of the project component specified</digi:trn>">					
											<digi:trn key="aim:title">Title</digi:trn>
											</a>
										</td>
										<td>
											<a title="<digi:trn key="aim:TitleforComponent">Title of the project component specified</digi:trn>">
											<html:text property="componentTitle" styleClass="inp-text" size="30"
											tabindex="1"/>
											</a>
										</td>
									</tr>								
									<tr>
										<td>
										<a title="<digi:trn key="aim:ComponentDescribe">Descriptive text as to the component objectives and tasks</digi:trn>">							
										<digi:trn key="aim:description">Description</digi:trn>
										</a>
										</td>
										<td>
											<a title="<digi:trn key="aim:ComponentDescribe">Descriptive text as to the component objectives and tasks</digi:trn>">
											<html:textarea property="componentDesc" cols="50" rows="4" styleClass="inp-text" tabindex="2"/>
											</a>
										</td>
									</tr>								
									<tr>
										<td>
											<a title="<digi:trn key="aim:ComponentAmt">Budgeted amount for the component based on total project award amount (Grant, Load and In-Kind)</digi:trn>">
											<digi:trn key="aim:amount">Amount</digi:trn></a>
										</td>
										<td>
											<a title="<digi:trn key="aim:ComponentAmt">Budgeted amount for the component based on total project award amount (Grant, Load and In-Kind)</digi:trn>">
											<html:text property="componentAmount" size="13" styleClass="amt" 
											tabindex="3"/>
											</a>
										</td>
									</tr>
									<tr>
										<td>
											<a title="<digi:trn key="aim:CurrencyToBeUsed">Currency used </digi:trn>">
											<digi:trn key="aim:currency">Currency</digi:trn>
											</a>
										</td>
										<td>
											<html:select property="currencyCode" styleClass="inp-text" tabindex="4">
												<html:option value="-1">Select Currency</html:option>
												<html:optionsCollection name="aimEditActivityForm" property="currencies" value="currencyCode" 
												label="currencyName"/>
											</html:select>										
										</td>
									</tr>									
									<tr>
										<td>
											<a title="<digi:trn key="aim:DateofReporting">Date the activity was initiated</digi:trn>">
											<digi:trn key="aim:reportingDate">Reporting Date</digi:trn></a>
										</td>
										<td>
											<table cellSpacing=0 cellPadding=0 vAlign="top" align="left" border=0>
												<tr>
													<td>
														<html:text property="componentRepDate" readonly="true" size="10" styleClass="inp-text" />
													</td>
													<td>
														&nbsp;
														<a href="javascript:newWindow(8)">
														<img src="../ampTemplate/images/show-calendar.gif" border=0
														tabindex="5"></a>
													</td>													
												</tr>
											</table>										
										</td>
									</tr>
									<tr>
										<td align="center" colspan=2>
											<table cellPadding=5>
												<tr>
													<td>
														<input type="button" value="Add" class="dr-menu" onclick="return addComponent()"
														tabindex="6">
													</td>
													<td>
														<input type="reset" value="Clear" class="dr-menu"
														tabindex="7">
													</td>
													<td>
														<input type="button" value="Close" class="dr-menu" onclick="closeWindow()"
														tabindex="8">
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>					
					</table>
				</td>
			</tr>
		</table>
	</td></tr>
</table>
</digi:form>
