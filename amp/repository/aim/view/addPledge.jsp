<%@page import="org.digijava.kernel.translator.TranslatorWorker"%>
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm, org.digijava.module.categorymanager.dbentity.AmpCategoryValue, java.util.*, org.digijava.module.aim.dbentity.*, org.springframework.beans.BeanWrapperImpl" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>

<!--<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>-->
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<jsp:include page="addSectors.jsp"  />
<jsp:include page="scripts/newCalendar.jsp"  />
<% int indexFund = 0; 
PledgeForm pledgeForm = (PledgeForm) session.getAttribute("pledgeForm");
%>
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script src="/repository/bootstrap/hacks.js"></script>
<script language="JavaScript" type="text/javascript"><!--

//alert('this module is a work-in-progress and is pending (almost) full rewrite in this week. Do not bugfix or add features to it, because everything will be deleted anyway');
var quitRnot1 = 0;

function fnChk(frmContrl, f){
	
	<c:set var="errMsgAddSectorNumericValue">
	  <digi:trn key="aim:addSecorNumericValueErrorMessage">
	  Please enter numeric value only
	  </digi:trn>
	  </c:set>
	  
	  if (isNaN(frmContrl.value)) {
	    alert("${errMsgAddSectorNumericValue}");
	    frmContrl.value = "";
	    //frmContrl.focus();
	    return false;
	  }  
	  if (f == "sector") {
		  var totalValue = 0;
		  $('input[name^=pledgeSectors]').each(function(i, obj) {
			    totalValue += parseFloat(obj.value);
			});
		  if (totalValue > 100) {
			 <c:set var="errMsgAddSumExceed">
			 <digi:trn key="aim:addSecorSumExceedErrorMessage">
			 Sector percentage can not exceed 100
			 </digi:trn>
			 </c:set>
			 alert("${errMsgAddSumExceed}"); 
			 frmContrl.value = "";
		    return false;  
		  }
			
	  }
	  else if (f == "program") {
		  var totalValue = 0;
		  $('input[name^=selectedProgs]').each(function(i, obj) {
			    totalValue += parseFloat(obj.value);
			});
		  if (totalValue > 100) {
			  <c:set var="errMsgAddSumExceed">
			  <digi:trn key="aim:addProgramSumExceedErrorMessage">
			  Program percentage can not exceed 100
			  </digi:trn>
			  </c:set>  
			  alert("${errMsgAddSumExceed}");
			 frmContrl.value = "";
		    return false;  
		  }  
	  }
	  return true;
	}

function addProgram(programType) {

	openNewRsWindow(750, 550);
	<digi:context name="taddProgram" property="context/module/moduleinstance/selectPledgeProgram.do?edit=true"/>
	var url="<%= taddProgram %>&programType="+programType;
     //       alert(programType + " "+url);
  	document.pledgeForm.action =url ;
	document.pledgeForm.target = popupPointer.name;
	document.pledgeForm.submit();
}
function removeProgram() {
	<c:set var="confirmDelete">
	  <digi:trn >Remove selected programs?</digi:trn>
	</c:set>
	<c:set var="selectprogram">
	  <digi:trn >Please, select a program first.</digi:trn>
	</c:set>
	var i = 1;
	var delStr = "deleteProgs=";
	while (document.getElementById("checkProg"+i)!=null){
		if(document.getElementById("checkProg"+i).checked==true){
			delStr = delStr + "_" + i;
		}
		i++;
	}
	if (delStr.length < 14){
		alert ("${selectprogram}");
	} else if (confirm("${confirmDelete}")){
		document.pledgeForm.target = "_self";
		document.pledgeForm.action="/removePledgeProgram.do?"+delStr;
		document.pledgeForm.submit();
	}
}			

function addSectors(editAct,configId) {
/*  openNewWindow(600, 450);
  document.aimEditActivityForm.action = "/selectSectors.do?edit=true&configId="+configId;
  document.aimEditActivityForm.target = popupPointer.name;
  document.aimEditActivityForm.submit();
*/ 	
	initSectorScript();
	 myAddSectors("edit=true&configId=-1");
}

function addSector(param)
{
    
    <digi:context name="addSec" property="context/addPledge.do?addSector=true&edit=param" />
    document.pledgeForm.action = "<%= addSec %>";
    document.pledgeForm.target = "_self";
    document.pledgeForm.submit();
}

function removeSector() {
	<c:set var="confirmDelete">
	  <digi:trn>Remove selected sectors?</digi:trn>
	</c:set>
	<c:set var="selectsectorfirst">
	  <digi:trn>Please, select a sector first.</digi:trn>
	</c:set>
	var i = 1;
	var delStr = "deleteSect=";
	while (document.getElementById("checkSect"+i)!=null){
		if(document.getElementById("checkSect"+i).checked==true){
			delStr = delStr + "_" + i;
		}
		i++;
	}
	if (delStr.length < 13){
		alert ("${selectsectorfirst}");
	} else if (confirm("${confirmDelete}")){
		document.pledgeForm.target = "_self";
		document.pledgeForm.action="/removePledgeSector.do?"+delStr;
		document.pledgeForm.submit();
	}	
}

function cancel(){
	<digi:context name="cancel" property="/savePledge.do" />
	document.pledgeForm.action = "<%=cancel%>?cancel=true";
	document.pledgeForm.target = "_self";

	document.pledgeForm.submit();
}

function changeTitle(){
	var title = document.getElementById("pledgeTitleDropDown").value;
			document.getElementById("myTitle").value = title;
	//if (title=="-2"){
	//	document.getElementById("newTitle").style.display = "block";
	//	document.getElementById("myTitle").value = "";
	//} else {
	//	if (title!="-1"){
	//		document.getElementById("newTitle").style.display = "none";
	//		document.getElementById("myTitle").value = title;
	//	} else {
	//		document.getElementById("newTitle").style.display = "none";
	//		document.getElementById("myTitle").value = "";
	//	}
	//}
	
}

document.getElementsByTagName('body')[0].className='yui-skin-sam';

--></script>

<style  type="text/css">
<!--

.contentbox_border{
        border: 1px solid black;
	border-width: 1px 1px 1px 1px; 
	background-color: #ffffff;
}

#my_autoComplete ul {
	list-style: square;
	padding-right: 0px;
	padding-bottom: 2px;
}

#my_autoComplete div {
	padding: 0px;
	margin: 0px; 
}



#my_autoComplete,
#my_autoComplete2 {
    width:15em; /* set width here */
    padding-bottom:2em;
}
#my_autoComplete {
    z-index:3; /* z-index needed on top instance for ie & sf absolute inside relative issue */
}
#my_input,
#my_input2 {
    _position:absolute; /* abs pos needed for ie quirks */
}
.charcounter {
    display: block;
}
#myImage {
    position:absolute; left:320px; margin-left:1em; /* place the button next to the input */
}

-->
</style>

<digi:instance property="pledgeForm" />

<digi:form action="/addPledge.do" method="post">

<html:hidden name="pledgeForm" styleId="event" property="fundingEvent"/>

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="1000" vAlign="top" align="center" border="0">
	
	<tr>
		<td align=left valign="top">
			<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top" align="left">
				<tr><td>
					<span style="font-family: Tahoma;font-size: 11px;"><digi:errors/></span>
					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top" border="0">
						<tr><td width="75%" vAlign="top">
						<table cellpadding="0" cellspacing="0" width="100%" border="0">
							<tr><td width="100%" bgcolor="#FF0000" style="background-color:#F5F5F5; border:1px solid #CCCCCC; padding:15px;">

							<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#C7D4DB">
							<!--<tr>
							<td colspan=4 align=center><b><digi:trn  key="pledgeInformation">Pledge Information</digi:trn></b></td>
							</tr>-->
							<tr><td bgColor=#ffffff align="center" vAlign="top">
								<!-- contents -->
																					
<feature:display name="Pledge Sector and Location" module="Pledges">
									<table width="95%" bgcolor="#dbdbdb" border=0>
										<tr>
										    <td height=25 align=center>
										        <IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15 />
										        <b><digi:trn key="aim:sectorAndLocation">Sector and Location</digi:trn></b>
										         
										    </td>
								        </tr>
							            <field:display name="Pledge Sector" feature="Pledge Sector and Location">
										<tr>
							                <td>
							                    <table cellPadding=5 cellSpacing=1 border=0 width="100%" bgcolor="#f2f2f2">
							                    	<tr>
							                            <td align=center height=25>
							                                <b>
							                                    <digi:trn key="aim:sector">
							                                        Sector
							                                    </digi:trn>
							                                </b>
							                            </td>
							                        </tr>
												</table>
											</td>
										</tr>
										<tr>
											<td>
										       <table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
	                                             	<tr><td>
														<c:forEach var="pledgeSectors" items="${pledgeForm.pledgeSectors}" varStatus="index">
	                                                            <tr> 
	                                                                   <c:set var="indexSect" value="${indexSect+1}"/>
											                            <td align="center" width="3%">
																			<input type="checkbox" id="checkSect${indexSect}"  >
																		</td>
	                                                                    <td  width="67%" valign="middle" align="left">
	                                                                        
	                                                                        [${pledgeSectors.sectorScheme}]
	                                                                        <c:if test="${!empty pledgeSectors.sectorName}">
	                                                                            [${pledgeSectors.sectorName}]
	                                                                        </c:if>
										                               		<c:if test="${!empty pledgeSectors.subsectorLevel1Name}">
	                                                                            [${pledgeSectors.subsectorLevel1Name}]
	                                                                        </c:if>
																			<c:if test="${!empty pledgeSectors.subsectorLevel2Name}">
	                                                                            [${pledgeSectors.subsectorLevel2Name}]
	                                                                        </c:if>
																			
	                                                                    </td>
	                                                                    <td width="15%" valign="middle" align="right">
	                                                                       
	                                                                    <FONT color="red">*</FONT><digi:trn key="aim:percentage">Percentage</digi:trn>:&nbsp;</td>
	                                                                    <td width="15%" valign="middle" align="left">
	                                                                        <html:text name="pledgeSectors" indexed="true" property="sectorPercentage"size="5" onkeyup="fnChk(this, 'sector')" styleClass="inp-text"/>
	                                                                    </td>
	                                                                </tr>
	                                                                <c:set var="sectorAdded" value="true"/>
	                                                           </c:forEach>
													</td></tr>
													<tr>
														<td colspan="2"> &nbsp;
	                                                    	<field:display name="Add Pledge Sector Button" feature="Pledge Sector and Location">
	                                                           <html:button styleClass="dr-menu"  
	                                                                         property="submitButton" onclick="addSectors();" >
	                                                                <digi:trn key="btn:addSectors">Add Sectors</digi:trn>
	                                                            </html:button>
															</field:display>
															<field:display name="Remove Pledge Sector Button" feature="Pledge Sector and Location">
																 &nbsp;
		                                                 		<logic:notEmpty name="pledgeForm" property="pledgeSectors">
																	<html:button styleClass="dr-menu" property="submitButton" onclick="return removeSector()">
		                                                          	  <digi:trn key="btn:removeSector">Remove Sector</digi:trn>
		                                                        	</html:button>
																</logic:notEmpty>
															</field:display>
		                                                </td>
		                                            </tr>
		                                        </table>
										     
										    </td>
										</tr>
										</field:display>
										<tr>
											<td> <!-- bootstrap part of form -->
										     <iframe src="/aim/selectPledgeLocation.do?edit=false" width="100%" scrolling="no" seamless="seamless" frameborder="0" marginheight="0" marginwidth="0" name="pledges_locations_name"></iframe> 										   
										    </td>
										</tr>
										<field:display name="Pledge Program" feature="Pledge Sector and Location">
										<tr>
							                <td>
							                    <table cellPadding=5 cellSpacing=1 border=0 width="100%"	bgcolor="#f2f2f2">
							                    	<tr>
							                            <td align=center height=25>
							                                <b>
							                                    <digi:trn key="aim:Program">
							                                        Program
							                                    </digi:trn>
							                                </b>
							                            </td>
							                        </tr>
												</table>
											</td>
										</tr>
										<tr>
											<td>
										       <table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
													<tr>
														<td colspan="2"> &nbsp;
															<field:display name="Add Pledge Program Button" feature="Pledge Sector and Location">
	                                                           <html:button styleClass="dr-menu"  
	                                                                         property="submitButton" onclick="addProgram(1);">
	                                                                <digi:trn key="btn:addProgram">Add Program</digi:trn>
	                                                            </html:button>
															</field:display>
		                                                </td>
		                                            </tr>
		                                        </table>
										     
										    </td>
										</tr>
										</field:display>

									</table>
								</feature:display>
								<feature:display name="Pledge Funding" module="Pledges">
									<table width="95%" bgcolor="#dbdbdb" border=0>
										<tr>
										    <td height=25 align=center>
										        <!-- contents -->
										        <IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15 />
										        <b><digi:trn key="aim:pledgeInformation">Pledge Information</digi:trn></b>
										        
										    </td>
								        </tr>
							            <tr>
							                <td>
							                    <div id="fundTitle" style="display:block;">
												<table cellPadding=5 cellSpacing=1 border=0 width="100%" bgcolor="#f2f2f2">
							                    	<tr>
														<td align="center" valign="bottom" width="20" />
														<td align="center" width="240">
						                                	<b><digi:trn key="aim:typeOfPledge">Type Of Pledge</digi:trn></b>
						                            	</td>
														<field:display name="Pledge Funding - Type Of Assistance" feature="Pledge Funding">
															<td align="center" width="150">
								                                <b><digi:trn key="aim:typeOfAssistance">Type Of Assistance</digi:trn></b>
								                            </td>
														</field:display>
														<td align="center" width="150">
							                                <b><digi:trn key="aim:amount">Amount</digi:trn></b>
							                            </td>
														<td align="center" width="170">
							                                <b><digi:trn key="aim:typeOfCurrency">Currency</digi:trn></b>
							                            </td>
														<td align="center" width="100">
							                                <b><digi:trn key="aim:year">Year</digi:trn></b>
							                            </td>
														<field:display name="Pledge Funding - Aid Modality" feature="Pledge Funding">
															<td align="center" width="200">
							                                	<b><digi:trn key="aim:aidModality">Aid Modality</digi:trn></b>
							                            	</td>
														</field:display>
							                        </tr>
												</table>
												</div>
											</td>
										</tr>
										<tr>
											<td>
												<table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
													<tr>
														<td>
			                                             	<div id="fundingDiv">
			                                             	<c:forEach var="fundingPledgesDetails" items="${pledgeForm.fundingPledgesDetails}" varStatus="status">
															
															<% String tNameBase = "fund_" + indexFund + "_"; 
															String divName = "fund_" + indexFund;
															indexFund++;
															String field0 = tNameBase + "0"; 
															 String field1 = tNameBase + "1"; 
															 String field2 = tNameBase + "2"; 
															 String field3 = tNameBase + "3"; 
															 String field4 = tNameBase + "4";
															 String field5 = tNameBase + "5";
															 String field6 = tNameBase + "6";
															 String field7 = tNameBase + "7"; %>
															 <div id="<%=divName%>" >
																<table width='100%' bgcolor='#FFFFFF' cellPadding=5 cellSpacing=1>
																<tr>
										                            <td align="center" valign="bottom" width="30" >
																		<input name="<%=field0%>" type="hidden" id="<%=field0%>" value='${fundingPledgesDetails.id}'/>
											                        	<input type="checkbox" name="<%=field1%>" id="<%=field1%>" >																	</td>
																	<td align="center" valign="bottom" width="170">
																		<select name="<%=field2%>" class="inp-text" style="max-width: 150px;">
																			<option selected="selected" value="-1">-<digi:trn>Select from below</digi:trn>-</option>
																			<c:forEach var="type" items="${pledgeForm.pledgeTypeCategory}">
																				<c:if test="${fundingPledgesDetails.pledgetypeid == type.id}">
																					<option selected="selected" value="<c:out value="${type.id}"/>">																				</c:if>
																				<c:if test="${fundingPledgesDetails.pledgetypeid != type.id}">
																					<option value="<c:out value="${type.id}"/>">																				</c:if>
																				<digi:trn>${type.value}</digi:trn>
																				</option>
																			</c:forEach>
																		</select>										                            </td>
																	<field:display name="Pledge Funding - Type Of Assistance" feature="Pledge Funding">
																		<td align="center" valign="bottom" width="200"><select name="<%=field3%>" class="inp-text" style="max-width: 150px;">
                                                                          <option selected="selected" value="-1">-
                                                                            <digi:trn>Select from below</digi:trn>
                                                                            -</option>
                                                                          <c:forEach var="type" items="${pledgeForm.assistanceTypeCategory}">
                                                                            <c:if test="${fundingPledgesDetails.typeOfAssistanceid == type.id}">
                                                                              <option selected="selected" value="<c:out value="${type.id}"/>                                                                        
                                                                              "> </c:if>
                                                                            <c:if test="${fundingPledgesDetails.typeOfAssistanceid != type.id}">
                                                                              <option value="<c:out value="${type.id}"/>                                                                        
                                                                              "> </c:if>
                                                                            <digi:trn>${type.value}</digi:trn>
                                                                          </c:forEach>
                                                                        </select></td>
																	</field:display>
																	<td align="center" valign="bottom" width="150">
																		
																		<input type="text" name="<%=field4%>" value="<aim:formatNumber value="${fundingPledgesDetails.amount}"/>" style="width:90px" class="inp-text"/>										                            </td>
																	<td align="center" valign="bottom" width="100">
										                                <select name="<%=field5%>" class="inp-text" style="max-width: 150px;">
																			<option selected="selected" value="-1">-<digi:trn>Select from below</digi:trn>-</option>
																			<c:forEach var="currency" items="${pledgeForm.validcurrencies}">
																				<c:if test="${fundingPledgesDetails.currencycode == currency.currencyCode}">
																					<option selected="selected" value="<c:out value="${currency.currencyCode}"/>">																				</c:if>
																				<c:if test="${fundingPledgesDetails.currencycode != currency.currencyCode}">
																					<option value="<c:out value="${currency.currencyCode}"/>">																				</c:if>
																				<c:out value="${currency.currencyName}" />
																				</option>
																			</c:forEach>
																		</select>										                            </td>
																	<td align="center" valign="bottom" width="150">
																		<select name="<%=field6%>" class="inp-text" style="max-width: 150px;">
																			<option value="unspecified"><digi:trn>unspecified</digi:trn></option>	
																			<c:forEach var="year" items="${pledgeForm.years}">
																				<c:if test="${fundingPledgesDetails.fundingYear == year}">
																					<option selected="selected" value="<c:out value="${year}"/>">																				</c:if>
																				<c:if test="${fundingPledgesDetails.fundingYear != year}">
																					<option value="<c:out value="${year}"/>">																				</c:if>
																					${year}
																				</option>
																			</c:forEach>
																		</select>										                                </td>
																	<field:display name="Pledge Funding - Aid Modality" feature="Pledge Funding">
																		<td align="center" valign="bottom" width="200">
											                               <select name="<%=field7%>" class="inp-text" style="max-width: 150px;">
																				<option selected="selected" value="-1">-<digi:trn>Select from below</digi:trn>-</option>
																				<c:forEach var="type" items="${pledgeForm.aidModalityCategory}">
																					<c:if test="${fundingPledgesDetails.aidmodalityid == type.id}">
																						<option selected="selected" value="<c:out value="${type.id}"/>">																					</c:if>
																					<c:if test="${fundingPledgesDetails.aidmodalityid != type.id}">
																						<option value="<c:out value="${type.id}"/>">																					</c:if>
																					<digi:trn>${type.value}</digi:trn>
																					</option>
																				</c:forEach>
																			</select>											                            </td>
																	</field:display>
										                        </tr>
															</table>
														</div>
														</c:forEach>
														</div>
														</td>
													</tr>
													<tr>
														<td colspan="4"> &nbsp;
															<table>
																<tr>
																	<td>
																		<field:display name="Add Pledge Funding Button" feature="Pledge Funding">
				                                                           <html:button styleClass="dr-menu"  
				                                                                         property="submitButton" onclick="addFunding();">
				                                                                <digi:trn key="btn:addFunding">Add Funding</digi:trn>
				                                                            </html:button>
																			&nbsp;
																		</field:display>
																	</td>
																	<td>
																		<field:display name="Remove Pledge Funding Button" feature="Pledge Funding">
																		<div id="remBut" style="display:block;">
																			<html:button styleClass="dr-menu" property="submitButton" onclick="return removeFunding()">
				                                                            <digi:trn key="btn:removeFunding">Remove Funding</digi:trn>
				                                                        	</html:button>
																		</div>
																		</field:display>
																	</td>
																</tr>
															</table>
		                                                </td>
		                                            </tr>
		                                        </table>
										     </td>
										</tr>
									</table>
									<br /><br />
								</feature:display>
								
								<table width="95%" border="0">
									<tr>
										<td align="right" width="50%">
											<html:button styleClass="dr-menu" property="submitButton" onclick="return savePledge()">
		                                         <digi:trn key="btn:savePlegde">Save Pledge</digi:trn>
											</html:button>
										</td>
										<td align="left" width="50%">
											<html:button styleClass="dr-menu" property="submitButton" onclick="return cancel()">
		                                         <digi:trn key="btn:cancel">Cancel</digi:trn>
											</html:button>
										</td>
									</tr>
									<tr><td>&nbsp;</td></tr>
									<tr><td>&nbsp;</td></tr>
								</table>
								<!-- end contents -->
							</td></tr>
							</table>

							</td></tr>
						</table>
						</td>
					</tr>	
					</table>
				</td></tr>
			</table>
		</td>
	</tr>
</table>
<script type="text/javascript">

initFund();
var numFund = <%=indexFund%>;
var tempFund = numFund;

function initFund(){
	numFund = <%=indexFund%>;
	tempFund = <%=indexFund%>;
	if (tempFund==0){
		var titles = document.getElementById('fundTitle');
		titles.style.display="none";
		var remBut = document.getElementById('remBut');
		remBut.style.display="none";
	}
}

function addFunding() {
	var ni = document.getElementById('fundingDiv');
	var divname = "fund_" + numFund;
	var newdiv = document.createElement('div');
	newdiv.setAttribute("id",divname);
	var s = "<table width='100%' bgcolor='#FFFFFF' cellPadding=5 cellspacing='1'> <tr> <td align='center' valign='bottom' width='30' >";
	s += "<input name='fund_"+ numFund +"_0' type='hidden' id='fund_"+ numFund +"_0' value=''/> <input type='checkbox' id='fund_"+ numFund +"_1'/></td>";

	s += "<td align='center' valign='bottom' width='170'> <select name='fund_"+ numFund +"_2' class='inp-text' style='max-width: 150px;'>";
	s += "<option selected='true' value='-1'>-<digi:trn>Select from below</digi:trn>-</option>";
	<% Collection col = pledgeForm.getPledgeTypeCategory();
	Iterator itr = col.iterator();
	while (itr.hasNext()) {
		AmpCategoryValue type = (AmpCategoryValue) itr.next();	
		if (type != null){ %>
				s += "<option value='<%=type.getId()%>'><%=type.getValue()%></option>";				  			
		<% }
	 }%>
	 s += "</select> </td>";
	 
	<field:display name="Pledge Funding - Type Of Assistance" feature="Pledge Funding">
	s += "<td align='center' valign='bottom' width='200'> <select name='fund_"+ numFund +"_3' class='inp-text' style='max-width: 150px;'>";
	s += "<option selected='true' value='-1'>-<digi:trn>Select from below</digi:trn>-</option>";
	<% Collection col2 = pledgeForm.getAssistanceTypeCategory();
	Iterator itr2 = col2.iterator();
	while (itr2.hasNext()) {
		AmpCategoryValue type = (AmpCategoryValue) itr2.next();	
		if (type != null){ %>
				s += "<option value='<%=type.getId()%>'><%=TranslatorWorker.translateText(type.getValue())%></option>";				  			
		<% }
	 }%>
	 s += "</select> </td>";
	 </field:display>
	 
	s += "<td align='center' valign='bottom' width='150'> <input type='text' name='fund_"+ numFund +"_4' size='17' style='width:90px' class='inp-text'/> </td>";

	s += "<td align='center' valign='bottom' width='100'> <select name='fund_"+ numFund +"_5' class='inp-text' style='max-width: 150px;'>";
	s += "<option selected='true' value='-1'>-<digi:trn>Select from below</digi:trn>-</option>";
	<% Collection col3 = pledgeForm.getValidcurrencies();
	Iterator itr3 = col3.iterator();
	while (itr3.hasNext()) {
		AmpCurrency currency = (AmpCurrency) itr3.next();	
		if (currency != null){
			if (currency.getCurrencyCode().equals(pledgeForm.getDefaultCurrency())) {%>
				s += "<option selected='true' value='<%=currency.getCurrencyCode()%>'><%=currency.getCurrencyName()%></option>";				  			
		<% } else { %>
				s += "<option value='<%=currency.getCurrencyCode()%>'><%=currency.getCurrencyName()%></option>";
		<%}
		}
	 }%>
	 s += "</select> </td>";

	 s += "<td align='center' valign='bottom' width='100'> <select name='fund_"+ numFund +"_6' class='inp-text' style='max-width: 150px;'> <option value='unspecified'><digi:trn>unspecified</digi:trn></option>";
		<% Collection col5 = pledgeForm.getYears();
		Iterator itr5 = col5.iterator();
		while (itr5.hasNext()) {
			String year = (String) itr5.next();	
			
			if (year != null){
				if (year.equals(pledgeForm.getYear())) {%>
					s += "<option selected='true' value='<%=year%>'><%=TranslatorWorker.translateText(year)%></option>";				  			
			<% } else { %>
					s += "<option value='<%=year%>'><%=TranslatorWorker.translateText(year)%></option>";
			<%}
			}
		 }%>
		 s += "</select> </td>";

	<field:display name="Pledge Funding - Aid Modality" feature="Pledge Funding">
	s += "<td align='center' valign='bottom' width='200'> <select name='fund_"+ numFund +"_7' class='inp-text' style='max-width: 150px;'>";
	s += "<option selected='true' value='-1'>-<digi:trn>Select from below</digi:trn>-</option>";
	<% Collection col4 = pledgeForm.getAidModalityCategory();
	Iterator itr4 = col4.iterator();
	while (itr4.hasNext()) {
		AmpCategoryValue type = (AmpCategoryValue) itr4.next();	
		if (type != null){ %>
				s += "<option value='<%=type.getId()%>'><%=TranslatorWorker.translateText(type.getValue())%></option>";				  			
		<% }
	 }%>
	 s += "</select> </td>";
	 </field:display>

	 s += "</tr> </table>";
	
	newdiv.innerHTML = s;
	ni.appendChild(newdiv);
	numFund++;
	tempFund++;
	var titles = document.getElementById('fundTitle');
	titles.style.display="block";
	var remBut = document.getElementById('remBut');
	remBut.style.display="block";
}

function removeFunding()
{
	<c:set var="confirmDelete">
	  <digi:trn key="aim:removeSelectedFundingMessage">
	 	 Remove selected fundings?
	  </digi:trn>
	</c:set>
	if (confirm("${confirmDelete}")){
		var d = document.getElementById('fundingDiv');
		var i = 0;
		var flag = false;
		while (i<=numFund){
			if(document.getElementById("fund_"+i+"_1")!=null && document.getElementById("fund_"+i+"_1").checked==true){
				var olddiv = document.getElementById("fund_"+i);
				d.removeChild(olddiv);
				tempFund--;
				flag = true;
			}
			i++;
		}
		if (!flag){
			alert ("Please, select a funding first.");
		}
	}
	if (tempFund==0){
		var titles = document.getElementById('fundTitle');
		titles.style.display="none";
		var remBut = document.getElementById('remBut');
		remBut.style.display="none";
	}
}

function savePledge() {

	if (validateData()){
		var i = 0;
		var param = "";
		while (i<=numFund){
			if(document.getElementById('fund_'+i)!=null){
				param += document.getElementsByName('fund_'+i+"_0")[0].value + "_";
				if (document.getElementsByName('fund_'+i+"_2")[0] == null){
					param += "-1_";
				}else{
					param +=  trim(""+document.getElementsByName('fund_'+i+"_2")[0].value) + "_";
				}
				if (document.getElementsByName('fund_'+i+"_3")[0] == null){
					param += "-1_";
				}else{
					param += trim(""+document.getElementsByName('fund_'+i+"_3")[0].value) + "_";
				}
				param += trim(""+document.getElementsByName('fund_'+i+"_4")[0].value) + "_";
				param += trim(""+document.getElementsByName('fund_'+i+"_5")[0].value) + "_";
				param += trim(""+document.getElementsByName('fund_'+i+"_6")[0].value) + "_";
				if (document.getElementsByName('fund_'+i+"_7")[0] == null){
					param += "-1_";
				}else{
					param += trim(""+document.getElementsByName('fund_'+i+"_7")[0].value) + "_";
				}
				param += ";";
			}
			i++;
		}
		
		<digi:context name="save" property="/savePledge.do" />
  	 	document.pledgeForm.action = "<%=save%>?fundings="+param;
  	  	document.pledgeForm.target = "_self";

    	document.pledgeForm.submit();
	}
}

function trim(stringToTrim) {
	return stringToTrim.replace(/^\s+|\s+$/g,"");
}

var setFocus = null;

function highligthObject(object,on){
	if (on){
		if (setFocus==null)
			setFocus=object;
		object.style.borderColor="#FF0000";
		object.style.borderWidth="3px";
		setFocus.focus();
	} else {
		object.style.borderColor="#CCCCCC";
		object.style.borderWidth="1px";
	}
}

function validateData(){
	var errors = false;
	setFocus = null;
	if ((document.getElementsByName("pledgeTitleId")[0]==null || document.getElementsByName("pledgeTitleId")[0].value==-1 || 
			document.getElementsByName("pledgeTitleId")[0].value==0)&& (document.getElementsByName("titleFreeText")[0].value==null)){
		highligthObject(document.getElementById("pledgeTitleDropDown"),true);
		errors = true;
	} else {
		if (document.getElementById("pledgeTitleDropDown")){
			highligthObject(document.getElementById("pledgeTitleDropDown"),false);
		}else {
			highligthObject(document.getElementById("titleFreeText"),false);
		}
	}

	if (document.getElementById("org_grp_dropdown_id")==null || document.getElementById("org_grp_dropdown_id").value==-1){
		highligthObject(document.getElementById("org_grp_dropdown_id"),true);
		errors = true;
	} else {
		highligthObject(document.getElementById("org_grp_dropdown_id"),false);
	}
	
	var i = 0;
	var j = 0;
	var percent = 100;
	while (document.getElementsByName("pledgeSectors["+i+"].sectorPercentage")[0]!=null){
		var temp = 0;
		temp = temp + document.getElementsByName("pledgeSectors["+i+"].sectorPercentage")[0].value;
		if (document.getElementsByName("pledgeSectors["+i+"].sectorPercentage")[0].value.length==0 || temp==0){
			highligthObject(document.getElementsByName("pledgeSectors["+i+"].sectorPercentage")[0],true);
			errors = true;
		} else {
			highligthObject(document.getElementsByName("pledgeSectors["+i+"].sectorPercentage")[0],false);
		}
		i++;
		percent = percent - temp;
	}
	if(percent!=0 && percent!=100){
		while (document.getElementsByName("pledgeSectors["+j+"].sectorPercentage")[0]!=null){
			highligthObject(document.getElementsByName("pledgeSectors["+j+"].sectorPercentage")[0],true);
			errors = true;
			j++;
		}
	}	

	i=0;
	percent = 100;
	while (document.getElementsByName("selectedLocs["+i+"].locationpercentage")[0]!=null){
		var temp = 0;
		temp = temp + document.getElementsByName("selectedLocs["+i+"].locationpercentage")[0].value;
		if (document.getElementsByName("selectedLocs["+i+"].locationpercentage")[0].value.length==0 || temp==0){
			highligthObject(document.getElementsByName("selectedLocs["+i+"].locationpercentage")[0],true);
			errors = true;
		} else {
			highligthObject(document.getElementsByName("selectedLocs["+i+"].locationpercentage")[0],false);
		}
		i++;
		percent = percent - temp;
	}
	if(percent!=0 && percent!=100){
		j = 0;
		while (document.getElementsByName("selectedLocs["+j+"].locationpercentage")[0]!=null){
			highligthObject(document.getElementsByName("selectedLocs["+j+"].locationpercentage")[0],true);
			errors = true;
			j++;
		}
	}	

	i = 0;
	percent = 100;
	while (document.getElementsByName("selectedProgs["+i+"].programpercentage")[0]!=null){
		var temp = 0;
		temp = temp + document.getElementsByName("selectedProgs["+i+"].programpercentage")[0].value;
		if (document.getElementsByName("selectedProgs["+i+"].programpercentage")[0].value.length==0 || temp==0){
			highligthObject(document.getElementsByName("selectedProgs["+i+"].programpercentage")[0],true);
			errors = true;
		} else {
			highligthObject(document.getElementsByName("selectedProgs["+i+"].programpercentage")[0],false);
		}
		i++;
		percent = percent - temp;
	}
	if(percent!=0 && percent!=100){
		j = 0;
		while (document.getElementsByName("selectedProgs["+j+"].programpercentage")[0]!=null){
			highligthObject(document.getElementsByName("selectedProgs["+j+"].programpercentage")[0],true);
			errors = true;
			j++;
		}
	}
	
	i = 0;
	while (i<=numFund){
		if (document.getElementsByName("fund_"+i+"_4")[0]!=null){
			var temp = 0;
			temp = temp + document.getElementsByName("fund_"+i+"_4")[0].value;
			if (document.getElementsByName("fund_"+i+"_4")[0].value.length==0 || temp==0){
				highligthObject(document.getElementsByName("fund_"+i+"_4")[0],true);
				errors = true;
			} else {
				var tmp = replaceAll(document.getElementsByName("fund_"+i+"_4")[0].value, " ", "");
				if (isNaN(tmp)){
					highligthObject(document.getElementsByName("fund_"+i+"_4")[0],true);
					errors = true;
				} else {
					highligthObject(document.getElementsByName("fund_"+i+"_4")[0],false);
				}
			}
		}
		i++;
	}
	
	i = 0;
	while (i<=numFund){
		if (document.getElementsByName("fund_"+i+"_5")[0]!=null){
			var temp = 0;
			temp = document.getElementsByName("fund_"+i+"_5")[0].value;
			if (temp==-1){
				highligthObject(document.getElementsByName("fund_"+i+"_5")[0],true);
				errors = true;
			} else {
				highligthObject(document.getElementsByName("fund_"+i+"_5")[0],false);
			}
		}
		i++;
	}
	

	<c:set var="checkErrors">
	  <digi:trn>
	  	Please, check values highlighted in red.
	  </digi:trn>
	</c:set>
	if (errors){
		alert ("${checkErrors}")
		return false;
	}
	else
		return true
}

function replaceAll(str, find, replace){
	  while (str.toString().indexOf(find) != -1)
	      str = str.toString().replace(find,replace);
	  return str;
	}


</script>

</digi:form>
