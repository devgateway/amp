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
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<%@include file="../documentManagerJsHelper.jsp" %>
<script type="text/javascript">
	var labelFilterCallbackObj	= {
		click: function(e, label) {
			publicListObj.addRemoveLabel(label);
			publicListObj.sendRequest();
		},
		applyClick: function(e, labelArray){
			for (var i=0; i<labelArray.length; i++) {
				publicListObj.addRemoveLabel(labelArray[i]);
			}
			if (labelArray.length == 0) {
				publicListObj.emptyLabels();
			}
			privateListObj.sendRequest();
		}
	}
	var titlePanelObj	= null;
	var fPanel	= new FilterAsYouTypePanel("publicLabelButtonId", labelFilterCallbackObj, "publicLabels");
	fPanel.initLabelArray(false);
	
	function afterPageLoad(e) {
		publicListObj			= new PublicDynamicList(document.getElementById("public_markup"), "publicListObj","publicFilterDivId");
		publicListObj.filterInfoDivId	= "publicFilterInfoDiv";
		publicListObj.sendRequest();
	}
	function showTitlePanel() {
		if ( titlePanelObj == null ) {
			var titlePanelDivEl		= document.getElementById("titlePanel");
			titlePanelDivEl.style.display	= "";
			titlePanelObj	= new YAHOO.widget.Panel("titlePanel", 
					{ 	visible:true,
						width: "400px", 
						constraintoviewport:true, 
						fixedcenter: true, 
						underlay:"shadow", 
						modal: true,
						close:true, 
						visible:false, 
						draggable:true } );
			titlePanelObj.render( );
		}
		titlePanelObj.show();
		titlePanelDivEl.getElementsByTagName("input")[0].focus();
	}
	checkEnter		= function (e) {
		if (e != null) {
			var keyCode	= -1;
			if (e.which != null)
				keyCode	= e.which;
			if (e.keyCode != null)
				keyCode	= e.keyCode;
			if ( keyCode == 13 ) {
				saveFilter();
				return false;
			}
		}
		return true;
	}
	function saveFilter() {
		if ( document.forms["crDocTabManagerForm"].savingFilterName.value != "" ) {
			document.forms["crDocTabManagerForm"].action	= "/contentrepository/publicDocTabManager.do?action=save";
			document.forms["crDocTabManagerForm"].submit();
		}
	}
	
	
	function retrieveFilterData(filterId) {
		YAHOO.util.Connect.asyncRequest('GET', '/contentrepository/publicDocTabManager.do?time='+ new Date().getTime()+'&action=jsonfilter&filterId='+filterId, new RetrieveFilters(publicListObj) );
	}
	
	YAHOO.util.Event.on(window, "load", afterPageLoad); 
</script>
<digi:instance property="crDocTabManagerForm" />
<bean:define id="myForm" toScope="request" name="crDocTabManagerForm" />

<DIV id="TipLayer"
	style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>

<table bgColor="#ffffff" cellPadding="0" cellSpacing="0" width="1000" align=center>
	<tr>
		<td align="left" class="r-dotted-lg" vAlign="top" width="750">
			<table cellPadding="5" cellSpacing="0" width="100%" border="0">
				<tr>
					<!-- Start Navigation -->
					<td height="33"><span class="crumb">
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" contextPath="/aim">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						
						
						<digi:trn>
							Public Document Manager
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height="25" width="1000" align=center bgcolor="#c7d4db">
						
							<digi:trn>
								<b style="font-size:12px;">Public Document Manager</b>
							</digi:trn>
					
					</td>
				</tr>
				<tr>
					<td height="16" vAlign="center" width="571">
						<digi:errors />
					</td>
				</tr>
				<tr>
					<td>
						<table border="0" cellPadding="1" cellSpacing="0" width="100%"style="position: relative; left: 20px" >
								<tr>
									<td>
										<form name="publicViewPositionForm" method="POST" action="/contentrepository/publicDocTabManager.do?action=savePositions">
										<span style="white-space: nowrap;">
											<digi:trn>Position 1</digi:trn>:
											<select name="publicViewPosition" class="inp-text" style="font-size: 10px;">
												<option value="-1"><digi:trn>Please select from below</digi:trn></option>
												<c:forEach var="filter" items="${myForm.availableDocumentFilters}">
													<c:set var="optionSelected"> </c:set>
													<c:if test="${filter.publicViewPosition==0}">
														<c:set var="optionSelected">selected="selected"</c:set>
													</c:if>
													<option value="${filter.id}" ${optionSelected}>${filter.name}</option>
												</c:forEach>
											</select>
										</span>
										<span style="white-space: nowrap;">
											<digi:trn>Position 2</digi:trn>:
											<select name="publicViewPosition" class="inp-text" style="font-size: 10px">
												<option value="-1"><digi:trn>Please select from below</digi:trn></option>
												<c:forEach var="filter" items="${myForm.availableDocumentFilters}">
													<c:set var="optionSelected"> </c:set>
													<c:if test="${filter.publicViewPosition==1}">
														<c:set var="optionSelected">selected="selected"</c:set>
													</c:if>
													<option value="${filter.id}" ${optionSelected}>${filter.name}</option>
												</c:forEach>
											</select>
										</span>
										<span style="white-space: nowrap;">
											<digi:trn>Position 3</digi:trn>:
											<select name="publicViewPosition" class="inp-text" style="font-size: 10px">
												<option value="-1"><digi:trn>Please select from below</digi:trn></option>
												<c:forEach var="filter" items="${myForm.availableDocumentFilters}">
													<c:set var="optionSelected"> </c:set>
													<c:if test="${filter.publicViewPosition==2}">
														<c:set var="optionSelected">selected="selected"</c:set>
													</c:if>
													<option value="${filter.id}" ${optionSelected}>${filter.name}</option>
												</c:forEach>
											</select>
										</span>
										<span style="white-space: nowrap;">
											<digi:trn>Position 4</digi:trn>:
											<select name="publicViewPosition" class="inp-text" style="font-size: 10px">
												<option value="-1"><digi:trn>Please select from below</digi:trn></option>
												<c:forEach var="filter" items="${myForm.availableDocumentFilters}">
													<c:set var="optionSelected"> </c:set>
													<c:if test="${filter.publicViewPosition==3}">
														<c:set var="optionSelected">selected="selected"</c:set>
													</c:if>
													<option value="${filter.id}" ${optionSelected}>${filter.name}</option>
												</c:forEach>
											</select>
										</span>
										<br/><br/>
										<input type="submit" class="buton" value="<digi:trn>Submit</digi:trn>" />
										</form>
										<hr style="width: 97%;margin-left: 0px; margin-right: 15px;" />
									</td>
								</tr>
								<tr>
						        	<td>
								    	<button id="publicFilterButtonId" class="buton" type="button" onclick="publicListObj.getFilterPanel('publicFilterButtonId','publicFilterDivId').show();">
								    		<digi:trn>Filters</digi:trn>
								    	</button>
								    	<button id="publicLabelButtonId" class="buton" type="button" onclick="fPanel.toggleView();">
								    		<digi:trn>Labels</digi:trn>
								    	</button>
								    </td>								    
								</tr>
								<tr><td><hr style="width: 97%;margin-left: 0px; margin-right: 15px;"/></td></tr>
								<tr>
									<td>
										<br />
											<div id="publicFilterInfoDiv"  >
											</div>
											<div id="public_markup" align="left">
											</div>
									</td>
								</tr>
							</table>
							<button class="buton" type="button" onclick="showTitlePanel();"><digi:trn>Save currently applied filter</digi:trn></button>
							<bean:define id="filterDivId" value="publicFilterDivId" toScope="request" />
							<jsp:include page="../filters/filters.jsp"/>	 
					</td>
				</tr>
				<tr>
					<td>
						<logic:notEmpty name="myForm" property="availableDocumentFilters">
							<ul>
							<c:forEach var="filter" items="${myForm.availableDocumentFilters}">
								<li>
									<a onclick="retrieveFilterData(${filter.id})" style="cursor:pointer; text-decoration:none; color: blue">
										${filter.name}
									</a> 
									[<a style="cursor:pointer; text-decoration:none; color: blue" 
									href="/contentrepository/publicDocTabManager.do?action=delete&filterId=${filter.id}"><digi:trn>Delete</digi:trn></a>]
								</li>
							</c:forEach>
							</ul>
						</logic:notEmpty>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

<div id="titlePanel" style="display: none">
		<div class="hd" style="font-size: 8pt">
			<digi:trn>Please enter a name for the filter</digi:trn>
		</div>
		<div class="bd" id="titlePanelBody">
			<digi:form action="/publicDocTabManager.do" method="POST">	
			<html:text onkeypress="return checkEnter(event);" property="savingFilterName" styleClass="inp-text" 
				style="border: 1px solid gray; width: 100%; font-size: 8pt; font-weight: bolder;" />
			</digi:form>
		</div>
		<div class="ft" align="right">
			<button id="save_button" type="button" class="buton" 
				 onclick="saveFilter();">
					<digi:trn>Save</digi:trn>
			</button>
			&nbsp;&nbsp;&nbsp;
		</div>
</div>



	