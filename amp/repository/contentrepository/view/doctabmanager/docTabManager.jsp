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
<style>
.yui-skin-sam a.yui-pg-page{
margin-left: 2px;
padding-right: 7px;
font-size: 11px;
border-right: 1px solid rgb(208, 208, 208);
}

.yui-skin-sam .yui-pg-pages{
border: 0px;
padding-left: 0px;
}
.yui-pg-current-page {
    background-color: #FFFFFF;
    color: rgb(208, 208, 208);
    padding: 0px;
}
.current-page {
    background-color: #FF6000;
    color: #FFFFFF;
    padding: 2px;
    font-weight: bold;
}



.yui-skin-sam span.yui-pg-first,
.yui-skin-sam span.yui-pg-previous,
.yui-skin-sam span.yui-pg-next,
.yui-skin-sam span.yui-pg-last {
display: none;
}

.yui-skin-sam a.yui-pg-first {
margin-left: 2px;
padding-right: 7px;
border-right: 1px solid rgb(208, 208, 208);
}

</style>
<script type="text/javascript">
var trnObj		= {
		labels: "<digi:trn>Labels</digi:trn>",
		filters: "<digi:trn>Filters</digi:trn>",
		keywords: "<digi:trn>Keywords</digi:trn>",
		apply: "<digi:trn>Apply</digi:trn>",
		close: "<digi:trn>Close</digi:trn>",
		none : "<digi:trn>none</digi:trn>"
};
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
			publicListObj.sendRequest();
		}
	}
	var titlePanelObj	= null;
	var fPanel=null;
	
	function afterPageLoad(e) {
		fPanel	= new FilterAsYouTypePanel("publicLabelButtonId", labelFilterCallbackObj, "publicLabels",trnObj);
		fPanel.initLabelArray(false);
		publicListObj			= new PublicDynamicList(document.getElementById("public_markup"), "publicListObj","publicFilterDivId",trnObj);
		publicListObj.filterInfoDivId	= "publicFilterInfo";
		publicListObj.sendRequest();
	}
	function showTitlePanel() {
		var titlePanelDivEl		= document.getElementById("titlePanel");
		if ( titlePanelObj == null ) {
			titlePanelDivEl.style.display	= "block";
			titlePanelObj	= new YAHOO.widget.Panel("titlePanel", 
					{ 	visible:true,
						width: "400px", 
						constraintoviewport:true, 
						fixedcenter: true, 
						underlay:"shadow", 
						modal: true,
						close:true, 
						draggable:true } );
			titlePanelObj.render();
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
		} else {
			alert ("<digi:trn>Please enter a valid name for the filter</digi:trn>");
			document.forms["crDocTabManagerForm"].savingFilterName.focus();
		}
	}
	
	
	function retrieveFilterData(filterId) {
		YAHOO.util.Connect.asyncRequest('GET', '/contentrepository/publicDocTabManager.do?time='+ new Date().getTime()+'&action=jsonfilter&filterId='+filterId, new RetrieveFilters(publicListObj) );
	}
	
	YAHOO.util.Event.on(window, "load", afterPageLoad);
	
	
	function savePositions () {
		var params ='';
		var positions = $("select[id^='pos_']");
		if(positions!=null){
        	for(var i=0;i < positions.length; i++){
        		params+= "&publicViewPosition="+positions[i].value;
        	}
    	}
		
		document.forms["crDocTabManagerForm"].action	= "/contentrepository/publicDocTabManager.do?action=savePositions"+params;
		document.forms["crDocTabManagerForm"].submit();
		
	}
</script>
<h1 class="admintitle">Public Document Manager</h1>
<digi:instance property="crDocTabManagerForm" />
<bean:define id="myForm" toScope="request" name="crDocTabManagerForm" />

<DIV id="TipLayer"
	style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>

<table bgColor="#ffffff" cellPadding="0" cellSpacing="0" width="1000" align=center>
	<tr>
		<td align="left" class="r-dotted-lg" vAlign="top" width="750">
			<table cellPadding="5" cellSpacing="0" width="100%" border="0">
				<!--<tr>-->
					<!-- Start Navigation -->
					<!--<td height="33"><span class="crumb">
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
					</td>-->
					<!-- End navigation -->
				<!--</tr> -->
			
				<tr>
					<td height="16" vAlign="center" width="1000">
						<digi:errors />
					</td>
				</tr>
				<tr>
					<td style="border:1px solid #CCCCCC;">
						<table border="0" cellPadding="1" cellSpacing="0" width="980" style="position: relative; font-size:12px;" align=center>
								<tr>
									<td>
										<c:forEach var="iterateIndex" begin="1" end="12">
											<span style="white-space: nowrap;">
												<digi:trn>Position</digi:trn>${iterateIndex}:
												<select name="select" class="inp-text" style="font-size: 10px; margin-right:20px;" id="pos_${iterateIndex-1}">
	                                              <option value="-1">
	                                                <digi:trn>Please select from below</digi:trn>
	                                              </option>
	                                              <c:forEach var="filter" items="${myForm.availableDocumentFilters}">
	                                                <c:set var="optionSelected"> </c:set>
	                                                <c:if test="${filter.publicViewPosition==iterateIndex-1}">
	                                                  <c:set var="optionSelected">selected="selected"</c:set>
	                                                </c:if>
	                                                <option value="${filter.id}" ${optionSelected}>${filter.name}</option>
	                                              </c:forEach>
	                                            </select>
											</span>&nbsp;											
										</c:forEach>
										<br/><br/>
										<center><input type="button" class="buttonx" value="<digi:trn>Submit</digi:trn>" onclick="savePositions()"/></center>
										<hr style="width: 97%;margin-left: 0px; margin-right: 15px;" />									
									</td>
								</tr>
								<tr>
						        	<td>
								    	<button id="publicFilterButtonId" class="buttonx" type="button" onclick="publicListObj.getFilterPanel('publicFilterButtonId','publicFilterDivId', false).show();">
								    		<digi:trn>Filters</digi:trn>
								    	</button>
								    	<button id="publicLabelButtonId" class="buttonx" type="button" onclick="fPanel.toggleView();">
								    		<digi:trn>Labels</digi:trn>
								    	</button>
								    </td>
								</tr>
								<tr>
									<td>
										<div style="width:1000px;" class="yui-skin-sam" id="content"> 
											<div id="demo" class="yui-navset">
												<div class="yui-content" style="border-color: #d0d0d0">
													<div id="publicFilterInfo"  ></div>
													<div id="public_markup" align="left">
													</div>
												</div>
											</div>
										</div>
									</td>
								</tr>
							</table>
							<center><button class="buttonx" type="button" onclick="showTitlePanel();"><digi:trn>Save currently applied filter</digi:trn></button></center>
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

<div id="titlePanel" class="invisible-item">
<digi:form action="/publicDocTabManager.do" method="POST">	
	<div class="hd" style="font-size: 8pt">
		<digi:trn>Please enter a name for the filter</digi:trn>
	</div>
	<div class="bd" id="titlePanelBody">		
		<html:text onkeypress="return checkEnter(event);" property="savingFilterName" styleClass="inp-text" 
			style="border: 1px solid gray; width: 100%; font-size: 8pt; font-weight: bolder;" />		
	</div>
	<div class="ft" align="right">
		<button id="save_button" type="button" class="buton" onclick="saveFilter();">
			<digi:trn>Save</digi:trn>
		</button>
		&nbsp;&nbsp;&nbsp;
	</div>
</digi:form>
</div>



	