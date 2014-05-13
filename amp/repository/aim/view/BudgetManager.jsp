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
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>
<%@ taglib uri="/taglib/aim" prefix="aim"%>

<c:set var="translation_valid_chars">
			<digi:trn>Please only use letters, digits, '_', () and space !</digi:trn>
</c:set>
<c:set var="translation_only_numeric">
			<digi:trn>Name and code values can't be only numeric</digi:trn>
</c:set>

<c:set var="translation_sector_empty">
			<digi:trn>Sector field can't be empty</digi:trn>
</c:set>
<c:set var="translation_code_empty">
			<digi:trn>Code field can't be empty</digi:trn>
</c:set>

<script language="JavaScript" type="text/javascript">

function addBudgetsector(){
        <digi:context name="Url" property="context/module/moduleinstance/BudgetManager.do?new=true" />
        document.BudgetManagerForm.target = "_self";
    	document.BudgetManagerForm.action = "<%=Url%>";
    	document.BudgetManagerForm.submit();
}

function validatesector(){
	var secName = ''+document.getElementById('secname').value;
	var bCode = ''+document.getElementById('bcode').value;
	var errors = '';
	if (secName==''){
		errors = errors + '<li>' + "${translation_sector_empty}" + '</li>' ;
	}
	if (bCode==''){
		errors = errors + '<li>' + "${translation_code_empty}" + '</li>' ;
	}
	if ((secName!='' && bCode!='') && (!(isNaN(bCode)) || !(isNaN(secName)))){
		errors = errors + '<li>' + "${translation_only_numeric}" + '</li>' ;
	}
	var regexp	= new RegExp("[a-zA-Z0-9_ÀÁÃÄÇÈÉËÌÍÏÑÒÓÕÖÙÚÜàáãäçèéëìíïñòóõöùúü%&' ()]+");
	var secNameOK = regexp.exec(secName);
	var bCodeOK = regexp.exec(bCode);
	if ((secName!='' && bCode!='') && (secNameOK!=secName || bCodeOK!=bCode)) {
		errors = errors + '<li>' + "${translation_valid_chars}" + '</li>' ;
	}
	document.getElementById('errors').innerHTML	= errors;
	if (errors.length == 0){
		addBudgetsector();
	}
}

function editsector(id) {
	openNewWindow(450, 200);
	<digi:context name="edit" property="context/module/moduleinstance/editbudgetsector.do" />
	document.BudgetManagerForm.action = "<%=edit%>~id="+id;
	document.BudgetManagerForm.target = popupPointer.name;
	document.BudgetManagerForm.submit();
}

function setprogram() {
	openNewWindow(450, 150);
	<digi:context name="program" property="context/module/moduleinstance/setbudgetprogram.do" />
	document.BudgetManagerForm.action = "<%=program%>";
	document.BudgetManagerForm.target = popupPointer.name;
	document.BudgetManagerForm.submit();
}

var enterBinder	= new EnterHitBinder('saveBudgetBtn');

function onDelete() {
	<c:set var="translation">
		<digi:trn key="aim:ConfirmDeleteSector">Delete this Sector ?</digi:trn>
	</c:set>
		var flag = confirm("${translation}");
		return flag;
	}

</script>
<div style="margin:0 auto;width:1000px;">
<digi:form action="/BudgetManager.do" method="post">
	<!--  AMP Admin Logo -->
	<jsp:include page="teamPagesHeader.jsp"  />
	<!-- End of Logo -->
	<html:hidden property="event" value="view" />
	<h1 class="admintitle" style="text-align:left;">Budget Manager</h1>
	<font color="red"><ul id="errors"></ul></font>
	<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=1000>
		<tr>
			<td style="font-size:12px;" align=left class=r-dotted-lg valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%" border="0">
				<!-- <tr> -->
					<!-- Start Navigation -->
					<!-- <td style="font-size:12px;" height=33 bgcolor="#f2f2f2"><span class=crumb> <c:set
						var="clickToViewAdmin">
						<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
					</c:set> <digi:link href="/admin.do" styleClass="comment"
						title="${clickToViewAdmin}">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
					</digi:link>&nbsp;&gt;&nbsp; <digi:trn key="aim:budgetManager"><b>Budget Manager</b></digi:trn></td> -->
					<!-- End navigation -->
				<!-- </tr> -->
				<!-- <tr>
					<td style="font-size:12px;" height=16 valign="center" width=571><span
						class=subtitle-blue> <digi:trn key="aim:budgetManager">Budget Manager</digi:trn></span>
					</td>
				</tr> -->
				<tr>
					<td style="font-size:12px;">
						<table width=100% style="margin-top:10px; margin-bottom:10px; padding:7px; border:1px solid #CCCCCC;" bgcolor="#f2f2f2">
							<tr>
								<td style="font-size:12px;"><font color="red" >*</font></td>
								<td style="font-size:12px;" width=100><digi:trn>Sector Name</digi:trn>:</td>
								<td style="font-size:12px;"><html:text property="budgetsectorname" styleClass="inputx" styleId="secname" size="30"></html:text></td>
							</tr>
							<tr>
								<td style="font-size:12px;"><font color="red" >*</font></td>
								<td style="font-size:12px;"><digi:trn>Sector Code</digi:trn>:</td>
								<td style="font-size:12px;">
									<html:text property="budgetsectorcode" size="5" styleClass="inputx" styleId="bcode"></html:text>
								</td>
							</tr>
							<tr>
								<td style="font-size:12px;" colspan="3" align="center"> 
									<html:button property="submitButton" styleClass="buttonx" onclick="validatesector();" styleId="saveBudgetBtn">
										<digi:trn>Add Sector</digi:trn>
									</html:button>
								</td>	
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td style="font-size:12px;" noWrap width="100%" vAlign="top">
					<table width="100%" cellspacing="1" border="0">
						<tr>
							<td style="font-size:12px;" noWrap width=750 vAlign="top">
							<table cellpadding="1" cellspacing="1" width="100%" valign="top">
								<tr bgColor=#ffffff>
									<td style="font-size:12px;" vAlign="top" width="100%">
									<table cellSpacing="1" cellPadding="5" class="inside" id="selectedSectors" align="left" width="100%">		
										<tr>
										  <td style="font-size:12px;" class=inside bgcolor="#c7d4db" height="25" align="center" colspan="3">
										  	<!-- Table title --> 
										  	<digi:trn><b>Budget Sectors</b></digi:trn>
										    <!-- end table title -->
											</td>
										</tr>
										<logic:empty name="BudgetManagerForm" property="budgetsectors">
											<tr bgcolor="#ffffff">
												<td style="font-size:12px;" colspan="5" align="center" class="inside">
													<b>
														<digi:trn> No Sector present</digi:trn>
													</b>
												</td>
											</tr>
										</logic:empty>
										<logic:notEmpty name="BudgetManagerForm" property="budgetsectors">
										<logic:iterate name="BudgetManagerForm" property="budgetsectors" id="sector" type="org.digijava.module.budget.dbentity.AmpBudgetSector">
											<tr> 
												<td style="font-size:12px;" bgcolor="#ffffff" class="inside">
													<bean:write name="sector" property="code"/> - <bean:write name="sector" property="sectorname"/>
												</td>
												<td style="font-size:12px;" class="inside" bgcolor="#ffffff" width="75" align="center">
													<c:set var="clickToEditSector">
														<digi:trn key="aim:clickToEditSector">Click here to Edit Sector</digi:trn>
													</c:set>
													<c:set var="edittext">
														<digi:trn key="aim:edit">Edit</digi:trn>
													</c:set>
													 [<a title="${clickToEditSector}" href="javascript:editsector('${sector.idsector}')">
														${edittext}
													  </a>
													 ]
												</td>
												<td style="font-size:12px;" class="inside" bgcolor="#ffffff" width="75" align="center">
													<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParams2}" property="sectorid">
															<bean:write name="sector" property="idsector" />
														</c:set>
														<c:set target="${urlParams2}" property="delete" value="true"/>
														<c:set var="clickToDeleteSector">
															<digi:trn key="aim:clickToDeleteSector">Click here to Delete Sector</digi:trn>
														</c:set>
														<c:set var="deletetext">
															<digi:trn key="aim:delete">Delete</digi:trn>
														</c:set>
														[ <digi:link href="/BudgetManager.do" name="urlParams2" 
																title="${clickToDeleteSector}" onclick="return onDelete()">${deletetext}</digi:link> ]
												</td>
											</tr>
											</logic:iterate>
											</logic:notEmpty>
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
			</td>
            <td style="font-size:12px; padding-top:37px;" noWrap width=250 vAlign="top">
							<table align="center" cellpadding="0" cellspacing="0" width="230"
								border="0">
								<tr>
									<td style="font-size:12px;"><!-- Other Links -->
									<table cellpadding="0" cellspacing="0" width="125">
										<tr>
											<td style="font-size:12px;" bgColor=#c9c9c7 class=box-title>
												<b style="padding-left:5px;"><digi:trn key="aim:otherLinks">Other links</digi:trn></b>
											</td>
											<td style="font-size:12px;" background="module/aim/images/corner-r.gif" height="17" width=17></td>
										</tr>
									</table>
									</td>
								</tr>
								<tr>
									<td style="font-size:12px;" bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellspacing="1" width="230" class="inside">
										<tr>
											<td style="font-size:12px;" class="inside"><digi:img src="module/aim/images/arrow-014E86.gif"
												width="15" height="10" /> <c:set var="trnViewAdmin">
												<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
											</c:set> <digi:link href="/admin.do" title="${trnViewAdmin}">
												<digi:trn key="aim:AmpAdminHome">
												Admin Home
												</digi:trn>
											</digi:link></td>
										</tr>
										<field:display name="Add Scheme Link" feature="Sectors">
											<tr>
												<td style="font-size:12px;" class="inside">
													<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10" /> 
													<a href="javascript:setprogram();">
													<digi:trn>
                                                  		Set Budget Program
                                                  	</digi:trn>
                                                  </a>
												</td>
											</tr>
										</field:display>
										<!-- end of other links -->
									</table>
									</td>
								</tr>
							</table>
							</td>
		</tr>
	</table>
</digi:form>
</div>