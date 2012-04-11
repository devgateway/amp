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

<digi:errors/>
<digi:instance property="aimFinancialOverviewForm" />

<digi:context name="digiContext" property="context"/>

<logic:equal name="aimFinancialOverviewForm" property="sessionExpired" value="true">
	<jsp:include page="../../../repository/aim/view/sessionExpired.jsp"  />
</logic:equal>

<logic:equal name="aimFinancialOverviewForm" property="sessionExpired" value="false">

<script type="text/javascript">

function fnEditProject(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
	   document.location.href="<%=addUrl%>?pageId=1&action=edit&step=3&surveyFlag=true&activityId=" + id;
}


YAHOOAmp.namespace("YAHOOAmp.amp");

var myPanel = new YAHOOAmp.widget.Panel("myPreview", {
	width:"940px",
	fixedcenter: true,
    constraintoviewport: false,
    underlay:"none",
    close:true,
    visible:false,
    modal:true,
    draggable:true,
    context: ["showbtn", "tl", "bl"]
    });
var panelStart=0;

var responseSuccess = function(o){
	/* Please see the Success Case section for more
	* details on the response object's properties.
	* o.tId
	* o.status
	* o.statusText
	* o.getResponseHeader[ ]
	* o.getAllResponseHeaders
	* o.responseText
	* o.responseXML
	* o.argument
	*/
	var response = o.responseText; 
	var content = document.getElementById("myContentContent");
	//response = response.split("<!")[0];
	content.innerHTML = response;
	//content.style.visibility = "visible";
	
	showContent();
}

function showPanelLoading(msg){
	   var content = document.getElementById("myContentContent");
	   content.innerHTML = "<div style='text-align: center'>" + "Loading..." +
	   "... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>";   
	   showContent();
	 }
	 
var responseFailure = function(o){ 
	// Access the response object's properties in the 
	// same manner as listed in responseSuccess( ). 
	// Please see the Failure Case section and 
	// Communication Error sub-section for more details on the 
	// response object's properties.
	//alert("Connection Failure!"); 
}  
var callback = 
{ 
	success:responseSuccess, 
	failure:responseFailure 
};

function showContent(){
	var element = document.getElementById("myContent");
	element.style.display = "inline";
	if (panelStart < 1){
		myPanel.setBody(element);
	}
	if (panelStart < 2){
		document.getElementById("myContent").scrollTop=0;
		myPanel.show();
		panelStart = 2;
	}
}

function preview(id)
{
	showPanelLoading();
	var postString="&pageId=2&activityId=" + id+"&isPreview=2&previewPopin=true";
	//alert(postString);
	<digi:context name="addUrl" property="context/module/moduleinstance/viewActivityPreviewPopin.do" />
	var url = "<%=addUrl %>?"+postString;
	YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
	
}

function initPopin() {
	var msg="\n<digi:trn>Activity Preview</digi:trn>";
	myPanel.setHeader(msg);
	myPanel.setBody("");
	myPanel.beforeHideEvent.subscribe(function() {
		panelStart=1;
	}); 
	
	myPanel.render(document.body);
}

window.onload=initPopin();

function viewChanges(id){
	openNewWindow(650,200);
	<digi:context name="showLog" property="context/module/moduleinstance/showActivityLog.do" />
	popupPointer.document.location.href = "<%= showLog %>?activityId=" + id;
}

function expandAll() {
   
	$("img[id$='_minus']").show();
	$("img[id$='_plus']").hide();	
	$("div[id$='_dots']").hide();
	$("div[id^='act_']").show('fast');
}

function collapseAll() {

	$("img[id$='_minus']").hide();
	$("img[id$='_plus']").show();	
	$("div[id$='_dots']").show();
	$("div[id^='act_']").hide();
}

function toggleGroup(group_id){
	var strId='#'+group_id;
	$(strId+'_minus').toggle();
	$(strId+'_plus').toggle();
	$(strId+'_dots').toggle();
	$('#act_'+group_id).toggle('fast');
}

</script>


<jsp:useBean id="urlSubTabs" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlSubTabs}" property="ampActivityId">
	<bean:write name="aimFinancialOverviewForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlSubTabs}" property="ampFundingId">
	<bean:write name="aimFinancialOverviewForm" property="ampFundingId"/>
</c:set>
<c:set target="${urlSubTabs}" property="tabIndex"  >
	<bean:write name="aimFinancialOverviewForm" property="tabIndex"/>
</c:set>
<c:set target="${urlSubTabs}" property="currency"  >
	<bean:write name="aimFinancialOverviewForm" property="currency"/>
</c:set>
<c:set target="${urlSubTabs}" property="transactionType" value="0"/>

<jsp:useBean id="urlAll" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlAll}" property="ampActivityId">
	<bean:write name="aimFinancialOverviewForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlAll}" property="ampFundingId">
	<bean:write name="aimFinancialOverviewForm" property="ampFundingId"/>
</c:set>
<c:set target="${urlAll}" property="tabIndex">
	<bean:write name="aimFinancialOverviewForm" property="tabIndex"/>
</c:set>

<jsp:useBean id="urlDiscrepancy" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${urlDiscrepancy}" property="ampActivityId">
	<bean:write name="aimFinancialOverviewForm" property="ampActivityId"/>
</c:set>
<c:set target="${urlDiscrepancy}" property="tabIndex">
	<bean:write name="aimFinancialOverviewForm" property="tabIndex"/>
</c:set>

<c:set target="${urlDiscrepancy}" property="transactionType" value="0"/>
<div id="myContent" style="display: none;">
	<div id="myContentContent" class="content" style="overflow: scroll; height: 500px;">
	</div>
</div>
<TABLE cellspacing="0" cellpadding="0" align="center" vAlign="top" border="0" width="100%">
<TR>
	<TD vAlign="top" align="center">
		<!-- contents -->
			<TABLE width="99%" cellspacing="0" cellpadding="0" vAlign="top" align="center" bgcolor="#f4f4f4" class="box-border-nopadding">
			<TR><TD bgcolor="#f4f4f4">
			<TABLE width="100%" cellSpacing=3 cellPadding=3 vAlign="top" align="center" bgcolor="#f4f4f4">
				<TR height="20"><TD height="20">
				<div id="subtabsFinancial">
				<!-- logic:notEqual name="aimFinancialOverviewForm" property="ampActivityId" value="0"-->
	            	<span>
	              		<digi:trn key="aim:overview">OVERVIEW</digi:trn>
	              	</span> | 
	              	<!-- /logic:notEqual-->
					<feature:display module="Funding" name="Commitments"> 
					<c:set var="translation">
						<digi:trn key="aim:clickToViewCommitments">Click here to view Commitments</digi:trn>
					</c:set>
					<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" title="${translation}" >
					<digi:trn key="aim:commitments">COMMITMENTS</digi:trn>
					</digi:link> | 
					</feature:display>
                     <field:display feature="Disbursement Orders" name="Disbursement Orders Tab">
                     <c:set target="${urlSubTabs}" property="transactionType" value="4"/>
					<c:set var="translation">
						<digi:trn key="aim:clickToViewDisbursementOrders">Click here to view Disbursement Orders</digi:trn>
					</c:set>
					<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" title="${translation}" >
					<digi:trn key="aim:disbursementOrders">DISBURSEMENT ORDERS</digi:trn>
					</digi:link> | 
                                        </field:display>
					<feature:display module="Funding" name="Disbursement">
					<c:set target="${urlSubTabs}" property="transactionType" value="1"/>
					<c:set var="translation">
						<digi:trn key="aim:clickToViewDisbursements">Click here to view Disbursements</digi:trn>
					</c:set>
					<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" title="${translation}" >
					<digi:trn key="aim:disbursements">DISBURSEMENTS</digi:trn>
					</digi:link> | 
					<c:set target="${urlSubTabs}" property="transactionType" value="2"/>
					<c:set var="translation">
						<digi:trn key="aim:clickToViewExpenditures">Click here to view Expenditures</digi:trn>
					</c:set>
					</feature:display>
					
                    <feature:display module="Funding" name="Expenditures">
                    	<digi:link href="/viewYearlyInfo.do" name="urlSubTabs" title="${translation}" >
							<digi:trn key="aim:expenditures">EXPENDITURES</digi:trn>
						</digi:link> | 
					</feature:display>
					<c:set var="translation">
						<digi:trn key="aim:clickToViewAll">Click here to view All</digi:trn>
					</c:set>
					<digi:link href="/viewYearlyComparisons.do" name="urlAll" title="${translation}" >
					<digi:trn key="aim:all">ALL</digi:trn>
					</digi:link>
                    
                    </div>
				</TD></TR>
				<TR bgColor=#f4f4f2>
            	<TD align=left><html:hidden property="tabIndex" />
            		<!-- 
						<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left" vAlign="top">
							<TR>
								<TD align="left">
									<SPAN class=crumb>
								  		<jsp:useBean id="urlFinancingBreakdown" type="java.util.Map" class="java.util.HashMap"/>
										<c:set target="${urlFinancingBreakdown}" property="ampActivityId">
											<bean:write name="aimFinancialOverviewForm" property="ampActivityId"/>
										</c:set>
										<c:set target="${urlFinancingBreakdown}" property="tabIndex" >
											<bean:write name="aimFinancialOverviewForm" property="tabIndex"/>
										</c:set>
										<c:set var="translation">
											<digi:trn key="aim:clickToViewFinancialProgress">Click here to view Financial Progress</digi:trn>
										</c:set>
										<digi:link href="/viewFinancingBreakdown.do" name="urlFinancingBreakdown" styleClass="comment"
										title="${translation}" >
										<digi:trn key="aim:financialProgress">Financial Progress</digi:trn>
										</digi:link> &gt; 
										<digi:trn key="aim:overview">Overview</digi:trn>
									</SPAN>
								</TD>
								<TD align="right">&nbsp;
									
								</TD>
							</TR>
						</TABLE>
						 -->
					</TD>
				</TR>
				<TR bgColor=#f4f4f2>
					<TD vAlign="top" align="center" width="750">
						<TABLE width="100%" cellspacing="1" cellpadding="1" vAlign="top" align="center" bgcolor="#f4f4f4">
							<TR>
								<TD width="100%">
        							<TABLE cellspacing="0" cellpadding="0" width="99%" align="center" bgColor=#f4f4f2 border="0">
                					<TR bgColor=#f4f4f2>
					            		<TD width=400 bgColor=#f4f4f2>
					                    	<TABLE cellspacing="0" cellpadding="0" bgColor=#f4f4f2 border="0">
			      	                		<TR bgColor=#f4f4f2>
			                        			<TD width="7" height="17">
                                                <img src="/TEMPLATE/ampTemplate/css/boxleftcorner.gif" border="0"  /><br />
                                                </TD>
			                        			<TD bgColor=#ccdbff height="17" style="padding:4px 4px 4px 4px;">
												<strong>
                                                	<digi:trn key="aim:referenceInformation">Reference Information</digi:trn>
                                                </strong>
			                          			</TD>
			                        			<TD width="7" height="17">
                                                <img src="/TEMPLATE/ampTemplate/css/boxrightcorner.gif" border="0"  /><br />
                                                </TD>
			                          		</TR>
			                  			</TABLE>
			                  		</TD>
          							</TR>
		                			<TR>
											<TD>
												<TABLE width="100%" cellSpacing=2 cellPadding=5 vAlign="top" align="left" style="border:1px solid #ccdbff;">
													<TR>
														<TD width="150" bgcolor="#f4f4f2">
															<b><digi:trn key="aim:fundingOrganization">
															Funding Organization</digi:trn></b>
														</TD>
														<TD bgcolor="#f4f4f2">
															<bean:write name="aimFinancialOverviewForm" property="donor" />
														</TD>
													</TR>
													<TR>
														<TD width="150" bgcolor="#f4f4f2">
															<b><digi:trn key="aim:fundingOrgId">
															Funding Organization Id</digi:trn></b>
														</TD>
														<TD bgcolor="#f4f4f2">
															<bean:write name="aimFinancialOverviewForm" property="donorFundingId" />
														</TD>
													</TR>
													<TR>
														<TD width="150" bgcolor="#f4f4f2">
															<b><digi:trn key="aim:typeOfAssistance">
															Type of Assistance</digi:trn></b>
														</TD>
														<TD bgcolor="#f4f4f2">
															<bean:write name="aimFinancialOverviewForm" property="termsOfAssistance" />
														</TD>
													</TR>
													<%--
													<TR>
														<TD width="150" bgcolor="#f4f4f2">
															<b><digi:trn key="aim:signatureDate">
															Signature Date</digi:trn></b>
														</TD>
														<TD bgcolor="#f4f4f2">
															<bean:write name="aimFinancialOverviewForm" property="signatureDate" />
														</TD>
													</TR>
													--%>
													<TR>
														<TD width="150" bgcolor="#f4f4f2">
															<b><digi:trn key="aim:conditions">
															Conditions</digi:trn></b>
														</TD>
														<TD bgcolor="#f4f4f2">
															<bean:write name="aimFinancialOverviewForm" property="conditions" />
														</TD>
													</TR>
													<field:display name="Donor Objective" feature="Funding Information">
														<TR>
															<TD width="150" bgcolor="#f4f4f2">
																<b><digi:trn key="aim:donorobjective">
																Donor Objective</digi:trn></b>
															</TD>
															<TD bgcolor="#f4f4f2">
																<bean:write name="aimFinancialOverviewForm" property="donorObjective"/>
															</TD>
														</TR>
													</field:display>
												</TABLE>
											</TD>
										</TR>
									</TABLE>
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
			</div>
			</TD></TR>

			</TABLE>
		<!-- end -->
	</TD>
</TR>
<TR><TD>&nbsp;</TD></TR>
</TABLE>
</logic:equal>
<script>
if(document.getElementById('showBottomBorder').value=='1'){
	document.write('</table><tr><td class="td_bottom1">&nbsp;</td></tr></table>&nbsp');
}
</script>



