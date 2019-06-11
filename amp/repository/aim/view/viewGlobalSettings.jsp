<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ page import="java.util.Map,java.util.List,java.util.ArrayList"%>
<%@ page import="org.digijava.kernel.util.RequestUtils"%>
<%@ page import="org.digijava.kernel.util.UserUtils"%>

<%@page import="org.digijava.module.aim.helper.GlobalSettings"%>
<%@page import="org.digijava.module.aim.form.GlobalSettingsForm"%>
<%@page import="org.digijava.module.aim.util.FeaturesUtil"%>
<%@page import="java.util.Date"%>
<%@page import="org.digijava.module.aim.services.auditcleaner.AuditCleaner"%>

<%@page import="org.digijava.module.aim.util.DbUtil"%>
<%@page import="org.digijava.module.aim.helper.GlobalSettingsConstants"%>
<%@page import="org.digijava.module.aim.helper.EasternArabicService" %>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<!-- dynamic tooltip -->
<!-- JavaScript Dependencies for Tabview: -->
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo-dom-event/yahoo-dom-event.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script> 

<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/common/TranslationManager.js"></script>
<digi:ref href="css/tabview.css" type="text/css" rel="stylesheet" />
 
 
<style type="text/css"> 
	#demo .yui-nav li {
		margin-right:0;
		margin-top:2pt;
 	}
	#demo .yui-content {
	min-height:200px;
	_height:200px;
	padding:10px 10px 10px 10px;
 	background-color: #FFFFFF;
}
</style>


<style>
.tableEven {
	background-color:#dbe5f1;
	font-size:8pt;
	padding:2px;
}
.tableOdd {
	background-color:#FFFFFF;
	font-size:8pt;!important
	padding:2px;
}
.Hovered {
	background-color:#a5bcf2;
}
</style>

<jsp:useBean id="sections" class="java.util.ArrayList" scope="page" />

<%
	sections.add("general");
	sections.add("funding");
	sections.add("date");
	sections.add("user");
%>


<script langauage="JavaScript"><!--
function saveClicked() {

  <digi:context name="preview" property="context/module/moduleinstance/GlobalSettings.do?action=save" />
  document.aimGlobalSettingsForm.action = "<%=preview%>";
  document.aimGlobalSettingsForm.target = "_self";
  document.aimGlobalSettingsForm.submit();

}

function validateChangeRangeDefaultValue(){

	var startYear = $("#gfsv_Change_Range_Default_Start_Value").val();
	var startYearTranslated = $("#gstv_Change_Range_Default_Start_Value").val();

	

	var endYear = $("#gfsv_Change_Range_Default_End_Value").val();
	var endYearTranslated = $("#gstv_Change_Range_Default_End_Value").val();

    if(endYear<startYear){
    	alert(endYearTranslated +' <digi:trn key="aim:Global:validation">should be greater than</digi:trn> ' + startYearTranslated);
    	return false;
    }
    return true;
}

function validateCustomFields(form) {	
	if (typeof form.gsfValue != "undefined") {
 		if (form.gsfValue.value=='' && isRequired(form)) {
 	 		alert('<digi:trn jsFriendly="true" key="aim:Global:validation">You must provide a value for</digi:trn>: '+form.globalSettingsNameTranslated.value);        
 	 		return false;
 		}
 	} else {
 	 	if (form.listOfValues.value=='') {
 	 		alert('<digi:trn jsFriendly="true" key="aim:Global:validation">You must provide a value for</digi:trn>: '+form.globalSettingsNameTranslated.value);
			return false;
		}
	}
	return true;
}

function isRequired(form){
	if( form.globalSettingsNameTranslated.id=="gstv_Public_Portal_URL" ) {
		return $("#gstv_Public_Portal").parent().find(".inp-text").val() == "true";
	}
	return true;
}

function boolean_field_changed(sel)
{
	// do nothing in the meantime
	//$(sel).parent().find("input[@name='globalId']")
}

function populateWithDays(monthId, targetId) {
	var monthElement	= document.getElementById(monthId);
	var month			= monthElement.value;
	//alert ('Month is:' + month);
	var maxDays	= 0;
	switch (parseInt(month)) {
		case 1: ;
		case 3: ;
		case 5: ;
		case 7: ;
		case 8: ;
		case 10: ;
		case 12:
			maxDays	= 31;
			break;
		case 4: ;
		case 6: ;
		case 9: ;
		case 11: 
			maxDays = 30;
			break;
		case 2: 
			maxDays = 28;
			break;
	}
	
	selectElement = document.getElementById(targetId);
	var numDays	= selectElement.length;
	for (i=0; i< numDays; i++) {
		selectElement.remove(0);
	}
	for (i=1; i<=maxDays; i++){ 
		newOption		= document.createElement('option');
		newOption.text	= i;
		newOption.value	= i;
		selectElement.add(newOption, null);
	}
	
}
function createHourString(hourId, minId, ampmId){
	hourElement		= document.getElementById(hourId);
	minElement		= document.getElementById(minId);
	ampmElement		= document.getElementById(ampmId);
	
	hourValue		= hourElement.value;
	if (hourValue.length == 1)
		hourValue	= '' + 0 + hourValue;
	
	minValue		= minElement.value;
	if (minValue.length == 1)
		minValue	= '' + 0 + minValue;
		
	ampmValue       =ampmElement.value==0?"AM":"PM";	
	var hour		= hourValue + ":" + minValue + " " + ampmValue ;
	
	els				= hourElement.form.elements;
	
	for (j=0; j<els.length; j++ ) {
		if (els[j].name != null && els[j].name == 'gsfValue') {
			els[j].value	= hour;
		}
	}

}
function createDateString(monthId, dayId) {
	monthElement	= document.getElementById(monthId);
	dayElement		= document.getElementById(dayId);
	
	monthValue		= monthElement.value;
	if (monthValue.length == 1)
		monthValue	= '' + 0 + monthValue;
	
	dayValue		= dayElement.value;
	if (dayValue.length == 1)
		dayValue	= '' + 0 + dayValue;
	var date		= dayValue + "/" + monthValue;
	
	els				= monthElement.form.elements;
	
	for (j=0; j<els.length; j++ ) {
		if (els[j].name != null && els[j].name == 'gsfValue') {
			els[j].value	= date;
		}
	}
}

//oh $DEITY$.... what a code
function saveAllSettings(){
    var allvalues='';
    for (i = 0;i < document.aimGlobalSettingsForm.length - 1 ; i++) {
    	var gsElement = document.aimGlobalSettingsForm[i];
   		if (window.warnOnBaseCurrencyChange && (gsElement.globalSettingsName) && (gsElement.globalSettingsName.value == 'Base Currency')) {
   			var newValue = gsElement.gsfValue.value;
   			var oldValue = window.oldGsValues[gsElement.globalSettingsName.value];
   			if (newValue != oldValue) {
   				if (!confirm('you are changing the Base Currency. You are going to lose all the inflation rate data if you enter the Currency Deflator window.\nAre you sure you want to change the base currency?')) 
   					return false;
   				}
   			}
    	//console.log('settings name = ' + document.aimGlobalSettingsForm[i].globalSettingsName.value + ', settings value = ' + document.aimGlobalSettingsForm[i].gsfValue.value);
		if (gsElement.globalId) {
        	if (!validateCustomFields(gsElement)) {
            	return false;
			}
            var id = gsElement.globalId.value;
            if (typeof gsElement.gsfValue != "undefined") {
            	var opt = gsElement.gsfValue;
                var val = gsElement.gsfValue.value;
                allvalues = allvalues + id + "=" + encodeURIComponent(val) + "&";
                console.log('allvalues = ' + allvalues);
			} else {
            	// Code for multiselect.
                var opt = gsElement.listOfValues;
                var selected = new Array();
                var index = 0;
                var val = id + '=';
                for (var intLoop=0; intLoop < opt.length; intLoop++) {
                	if (opt[intLoop].selected) {
                    	index = selected.length;
                        selected[index] = new Object;
                        selected[index].value = opt[intLoop].value;
                        selected[index].index = intLoop;
                        val = val + selected[index].value + ';';
					}
				}
                val = encodeURIComponent(val) + "&";
                allvalues = allvalues + val;
        	}
    	}
    }
    if(!validateChangeRangeDefaultValue()){
    	return false;
    }
    //alert(allvalues);
    document.aimGlobalSettingsForm[document.aimGlobalSettingsForm.length -1].allValues.value=allvalues;
	return true;
}

function setIndex(index){
	if(document.aimGlobalSettingsForm && document.aimGlobalSettingsForm.length > 0) {
		document.aimGlobalSettingsForm[document.aimGlobalSettingsForm.length - 1].indexTab.value = index;
	} else {
		return false;
	}
}
	
var enterBinder	= new EnterHitBinder('gsSaveAllBtn');
--></script>


<digi:instance property="aimGlobalSettingsForm" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->
<html:hidden property="event" value="view"/>
<h1 class="admintitle teamPagesHeader">General Settings</h1>
<digi:errors/>
<logic:notEmpty name="aimGlobalSettingsForm" property="gsfCol">
	<logic:iterate name="aimGlobalSettingsForm" property="gsfCol" id="globalSett" type="org.digijava.module.aim.dbentity.AmpGlobalSettings ">
		<script type="text/javascript">
			// haha Alexei, here is some nice global vars. I know you like them
			if (typeof(window.oldGsValues) == 'undefined') {
				window.oldGsValues = {};
			}
			window.oldGsValues['<c:out value="${globalSett.globalSettingsName}" />'] = '<c:out value="${globalSett.globalSettingsValue}" />';
		</script>
	</logic:iterate>
</logic:notEmpty>
<logic:greaterThan name="aimGlobalSettingsForm" property="relevantInflationRateEntries" value="0">
	<script type="text/javascript">
		window.warnOnBaseCurrencyChange = true;
	</script>
</logic:greaterThan>

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=1000 align=center>
	<tr>
		<td align=left valign="top" width=750px>
		<table cellPadding=5 cellspacing="0" width="100%" border="0">
			<!-- <tr>
				<td height=33>
					<span class=crumb> 
					<c:set var="clickToViewAdmin">
						<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
					</c:set> 
					<digi:link href="/admin.do" styleClass="comment" title="${clickToViewAdmin}">
						<digi:trn key="aim:AmpAdminHome"> Admin Home </digi:trn>
					</digi:link>
					&nbsp;&gt;&nbsp; 
					<digi:trn key="aim:globalSettingsManager">Global Settings Manager</digi:trn> 
					</span>
				</td>
			</tr> -->
				
			<tr>
          		<td height=16 valign="center">
			            <div id="demo" class="yui-navset" style="width:1000px;">
			                <ul class="yui-nav">
								<c:set var="indexTab" value="0"/>
								<logic:iterate name="sections"  id="sectionName">
								 <li>
									<a indexTab="${indexTab}" onclick="setIndex(${indexTab})" style="cursor: pointer">
										<div style="text-transform: capitalize;">
											<digi:trn key="aim:globalsettings:sectionname:${sectionName}">${sectionName}</digi:trn>
										</div>
									</a>
								</li>
								<c:set var="indexTab" value="${indexTab + 1}"/>
								</logic:iterate>
							</ul>
			                <div class="yui-content" > 

									<!-- Start - Sorting settings based on its name  -->
									<jsp:useBean id="aimGlobalSettingsForm" class="org.digijava.module.aim.form.GlobalSettingsForm" scope="page"/>
									<jsp:useBean id="sortedglobalSett" class="java.util.TreeMap" scope="page" />
									<logic:notEmpty name="aimGlobalSettingsForm" property="gsfCol">
		                            <logic:iterate name="aimGlobalSettingsForm" property="gsfCol" id="globalSett" type="org.digijava.module.aim.dbentity.AmpGlobalSettings ">
										<c:set var="key" scope="page"><digi:trn key="aim:Global:${globalSett.globalSettingsName}"><bean:write name="globalSett" property="globalSettingsName"/></digi:trn></c:set>
										<jsp:useBean id="key" class="java.lang.String" scope="page"/>
										<%
										   boolean isSuperAdmin = UserUtils.isSuperAdmin(RequestUtils.getUser(request), RequestUtils.getSite(request));
										   if (isSuperAdmin || !globalSett.isInternal()) {
										       sortedglobalSett.put(key, globalSett);
										   }
										%>
									</logic:iterate>
									</logic:notEmpty>
									<jsp:setProperty name="aimGlobalSettingsForm" property="gsfCol" value="<%=sortedglobalSett.values()%>"/>
									<!-- End - Sorting settings based on its name  -->

								<logic:iterate name="sections"  id="sectionName">
								<div>
									<font style="color:black;font-size:14pt;font-weight:bold">									
									<center><digi:trn key="aim:global:section:${sectionName}">${sectionName}</digi:trn></center>
									</font>
									<br />
									<table width="975" border="0" id="${sectionName}" align=center style="font-size:11px;" class="inside">
										<tr>
			                               <td width="50%" bgcolor="#c7d4db" style="color:black;font-size:10pt;font-weight:bold" class="inside gs-setting-label">
												<digi:trn key="aim:globalsettings:setting">Setting</digi:trn>
										   </td>
			                               <td width="50%" bgcolor="#c7d4db" style="color:black;font-size:10pt;font-weight:bold" class="inside">
												<digi:trn key="aim:globalsettings:value">Value</digi:trn>
										   </td>
										</tr>
									<logic:notEmpty name="aimGlobalSettingsForm" property="gsfCol">
		                            <logic:iterate name="aimGlobalSettingsForm" property="gsfCol" id="globalSett"
		                            			   type="org.digijava.module.aim.dbentity.AmpGlobalSettings ">
		 
									<logic:equal name="globalSett" property="section" value="${sectionName}">
									    <%
									    	int g_range = 0, g_year = 0;
									    %>
			                            <tr style='display:<%= GlobalSettingsConstants.SETTINGS_TO_HIDE.contains(globalSett.getGlobalSettingsName()) ? "none" : "auto" %>' >
			                               <td width="50%" class="inside gs-setting-label">
			                                 <logic:notEmpty name="globalSett" property="globalSettingsDescription">
			                                   <img src= "../ampTemplate/images/help.gif" border="0" title="<digi:trn key="aim:Global:Help:${globalSett.globalSettingsName}"><bean:write name="globalSett" property="globalSettingsDescription"/></digi:trn>">                              
			                                 </logic:notEmpty>
			                                 <digi:trn key="aim:Global:${globalSett.globalSettingsName}"><bean:write name="globalSett" property="globalSettingsName"/></digi:trn>                              
										   </td>
										<digi:form action="/GlobalSettings.do" method="post" onsubmit="return validateCustomFields(this)" >
			                                <td width="50%" class="inside">
			                                  <html:hidden property="globalId" name="globalSett"/>
			                                  <html:hidden property="globalSettingsName" name="globalSett"/>
											<%String gfId=globalSett.getGlobalSettingsName().replace(' ', '_'); %>
											 <input type="hidden" id="gstv_<%=gfId%>" name="globalSettingsNameTranslated" value='<digi:trn jsFriendly="true" key="aim:Global:${globalSett.globalSettingsName}"><bean:write name="globalSett" property="globalSettingsName"/></digi:trn>'>
										
			                                  <%String possibleValues = "possibleValues("+ globalSett.getGlobalSettingsName()+ ")";
												String gsType = globalSett.getGlobalSettingsPossibleValues();
											  %>
			
			
			                                  <logic:notEmpty name="aimGlobalSettingsForm" property='<%= possibleValues %>'>
											
			                                    <%if (globalSett.getGlobalSettingsName().trim().equalsIgnoreCase("Default Country".trim())) {%>
			                                    <html:select property="gsfValue" styleClass="inp-text;width:100%" value='<%=globalSett.getGlobalSettingsValue()%>' >
			                                      <logic:iterate name="aimGlobalSettingsForm" property='<%=possibleValues%>' id="global">
			                                        <html:option value="${global.key}">${global.value}</html:option>
			                                      </logic:iterate>
			                                    </html:select>
			                                    <%
			                                    	} else if (globalSett.getGlobalSettingsName().trim().equalsIgnoreCase("Default Number Format".trim())) {
			                                    %>
			                                  	
			                                  	<select name="options"  onchange="if(this.value!='noselection'){gsfValue.value=this.value}" styleClass="inp-text;width:100%" 
			                                  	value='<%=globalSett.getGlobalSettingsValue()%>'>
			                                      <option value="noselection"><digi:trn key="aim:gloablSetting:selectFormat">(Select Format)</digi:trn> </option>
	                                                 <c:set var="selectedValue" value="<%=globalSett.getGlobalSettingsValue()%>"/>
					                                  <logic:iterate name="aimGlobalSettingsForm" property='<%=possibleValues%>' id="global">
					                                 
					                                  <c:choose>
					                                      <c:when test="${global.value == selectedValue}">
					                                          <option selected="selected" value="${global.key}"><digi:easternArabicNumber>${global.value}</digi:easternArabicNumber></option>
					                                      </c:when>
					                                  <c:otherwise>
					                                      <option value="${global.key}"><digi:easternArabicNumber>${global.value}</digi:easternArabicNumber>  </option>
					                                  </c:otherwise>
					                                  
					                                  </c:choose>
					                                  
	 	 	 	                                      </logic:iterate>
		
			                                    </select>
			                                    <digi:trn key="aim:gloablSetting:predefinedFormat">(Predefined Format)</digi:trn> <br>
			                                    
			                                    <html:text property="gsfValue" value="<%= globalSett.getGlobalSettingsValue()%>"></html:text> 
			                                    <digi:trn key="aim:gloablSetting:customFormat">(Custom Format)</digi:trn>

			                                    <%
			                                        } else {
 				                                %>
			                                    
			                                    <html:select  property="gsfValue" alt="prueba" style="max-width: 750px;" styleClass="inp-text" value='<%= globalSett.getGlobalSettingsValue() %>'>
			                                      <logic:iterate name="aimGlobalSettingsForm" property='<%=possibleValues%>' id="global" type="org.digijava.module.aim.helper.KeyValue">
			                                      	<%
			                                      		String key2 = "aim:globalSettings:"+ globalSett.getGlobalSettingsName()+ ":"+ global.getValue();
			                                      	%>
			                                      	<%
			                                      		if (key2.length() > 100) {
			                                      				key2 = key2.substring(0, 50);
			                                      		}
			                                      	%>
			                                     

			                                      	<c:set var="newKey"><%=key2%></c:set>
			                                        
																				<html:option value="${global.key}">
																					<c:choose>
																						<c:when test="${globalSett.valueTranslatable}">
																							<digi:trn>
																								<c:out value="${global.value}" />
																							</digi:trn>
																						</c:when>
																						<c:otherwise>
                                                                                            <digi:easternArabicNumber><c:out value="${global.value}" /></digi:easternArabicNumber>
																						</c:otherwise>
																					</c:choose>
																				</html:option>


																			</logic:iterate>
			                                    </html:select>
			                                    <%
			                                    	}
			                                    %>										
			                                    </logic:notEmpty>
			                                    <logic:empty name="aimGlobalSettingsForm" property='<%= possibleValues %>'>
			                                    	<c:set var="type" value="<%=gsType %>" />
			                                    	<c:choose>
				                                    	<c:when test='${type == "t_Date_No_Year"}'>
				                                    		<%
				                                    			String monthId = "month"+ globalSett.getGlobalId();
				                                    			String dayId = "day"+ globalSett.getGlobalId();
				                                    			String[] dateValues = globalSett.getGlobalSettingsValue().split("/");
				                                    			int monthNum = Integer.parseInt(dateValues[1]);
				                                    		%>
				                                    		<html:hidden property="gsfValue" value='<%= globalSett.getGlobalSettingsValue() %>'/>
				                                    		<digi:trn key="aim:globalSettings:month">Month</digi:trn>: 
				                                    		<select styleClass="inp-text" id="<%=monthId%>" onchange="populateWithDays('<%=monthId%>','<%=dayId%>');createDateString('<%=monthId%>','<%=dayId%>')">
				                                    			<%
				                                    				for (int k = 1; k <= 12; k++) {if (k == monthNum) {
				                                    			%>
                                                                <option selected="selected" value="<%=k%>"><digi:easternArabicNumber><%=k%></digi:easternArabicNumber></option>
				                                    			<%
				                                    				} else {
				                                    			%>
					                                    				<option  value="<%=k%>"><digi:easternArabicNumber><%=k%></digi:easternArabicNumber></option>
					                                    		<%
					                                    			}
					                                    											}
					                                    		%>
				                                    		</select>
				                                    		<digi:trn key="aim:globalSettings:day">Day</digi:trn>: 
				                                    		<select styleClass="inp-text" id="<%=dayId%>" onchange="createDateString('<%=monthId%>','<%=dayId%>');">
				                                    			<%
				                                    				for (int k = 1; k <= org.digijava.module.aim.action.GlobalSettings
				                                    														.numOfDaysInMonth(monthNum); k++) {
				                                    													if (k == Integer
				                                    															.parseInt(dateValues[0])) {
				                                    			%>
				                                    					<option value="<%=k%>" selected="selected"><%=k%></option>
				                                    			<%
				                                    				} else {
				                                    			%>
					                                    				<option value="<%=k%>"><%=k%></option>
					                                    		<%
					                                    			}
					                                    											}
					                                    		%>
				                                    		</select>
				                                    	</c:when>	
				                                    	<c:when test='${type == "t_daily_currency_update_hour"}'>
				                                    		<%
				                                    			String hourId = "hour"
				                                    													+ globalSett.getGlobalId();
				                                    											String minId = "min"
				                                    													+ globalSett.getGlobalId();
				                                    											String ampmId = "ampm"
				                                    													+ globalSett.getGlobalId();
				                                    											String[] timeValues = globalSett
				                                    													.getGlobalSettingsValue()
				                                    													.split(" ");
				                                    											String[] hourValues = timeValues[0]
				                                    													.split(":");
				                                    											int hourNum = Integer
				                                    													.parseInt(hourValues[0]);
				                                    											int minNum = Integer
				                                    													.parseInt(hourValues[1]);
				                                    											String ampmNum = timeValues[1];
				                                    		%>
				                                    		<html:hidden property="gsfValue" value='<%= globalSett.getGlobalSettingsValue() %>'/>
				                                    		<digi:trn key="aim:globalSettings:hour">Hour</digi:trn>: 
				                                    		<select styleClass="inp-text" id="<%=hourId%>" onchange="createHourString('<%=hourId%>','<%=minId%>','<%=ampmId%>');">
				                                    			<%
				                                    				for (int k = 1; k <= 12; k++) {
				                                    													String val = (k < 10) ? "0" + k
				                                    															: String.valueOf(k);
				                                    													if (k == hourNum) {
				                                    			%>
				                                    					<option selected="selected" value="<%=k%>"><digi:easternArabicNumber><%=val%></digi:easternArabicNumber></option>
				                                    			<%
				                                    				} else {
				                                    			%>
					                                    				<option  value="<%=k%>"><digi:easternArabicNumber><%=val%></digi:easternArabicNumber></option>
					                                    		<%
					                                    			}
					                                    											}
					                                    		%>
				                                    		</select>
				                                    		<digi:trn key="aim:globalSettings:min">Min</digi:trn>: 
				                                    		<select styleClass="inp-text" id="<%=minId%>" onchange="createHourString('<%=hourId%>','<%=minId%>','<%=ampmId%>');">
				                                    			<%
				                                    				for (int k = 0; k < 60; k += 5) {
				                                    													String val = (k < 10) ? "0" + k
				                                    															: String.valueOf(k);
				                                    													if (k == minNum) {
				                                    			%>
				                                    					<option value="<%=k%>" selected="selected"><digi:easternArabicNumber><%=val%></digi:easternArabicNumber></option>
				                                    			<%
				                                    				} else {
				                                    			%>
					                                    				<option value="<%=k%>"><digi:easternArabicNumber><%=val%></digi:easternArabicNumber></option>
					                                    		<%
					                                    			}
					                                    											}
					                                    		%>
				                                    		</select>
				                                    		<select styleClass="inp-text" id="<%=ampmId%>" onchange="createHourString('<%=hourId%>','<%=minId%>','<%=ampmId%>');">
				                                    			<%
				                                    				String[] ampm = { "AM", "PM" };
				                                    												for (int k = 0; k <= 1; k++) {
				                                    													if (ampm[k]
				                                    															.compareToIgnoreCase(ampmNum) == 0) {
				                                    			%>
																<option value="<%=k%>" selected="selected"><digi:trn><%=ampm[k]%></digi:trn></option>
				                                    			<%
				                                    				} else {
				                                    			%>
					                                    				<option value="<%=k%>"><digi:trn><%=ampm[k]%></digi:trn></option>
					                                    		<%
					                                    			}
					                                    											}
					                                    		%>
				                                    		</select>
				                                    		<br/>
				                                    		<digi:trn key="aim:globalSettings:ServerTime">Server Time</digi:trn>:&nbsp; 
				                                    		<%
 				                                    			java.text.DateFormat formatter = new java.text.SimpleDateFormat(
 				                                    													"hh:mm:ss a");
 				                                    											String sdate = org.digijava.module.common.util.DateTimeUtil
 				                                    													.formatDate(new java.util.Date());
 				                                    		%>
															<digi:easternArabicNumber><%=sdate + " " + formatter.format(new java.util.Date())%></digi:easternArabicNumber>
				                                    	
				                                    	</c:when>
				                                    	<c:when test='${type == "t_timeout_currency_update"}'>
				                                            <%
				                                            	String timeoutValue = globalSett
				                                            											.getGlobalSettingsValue();
				                                            									int timeout = Integer
				                                            											.parseInt(timeoutValue);
				                                            									//g_range=timeout;
				                                            %>
				                                    		<select styleClass="inp-text" name="gsfValue" >
				                                    			<%
				                                    				int[] min_array = { 1, 2, 3, 4, 5,
				                                    														6, 7, 8, 9, 10, 15, 20, 25,
				                                    														30 };
				                                    												for (int k : min_array) {
				                                    													if (k == timeout) {
				                                    			%>
                                                                <option value="<%=k%>" selected="selected"><digi:easternArabicNumber><%=k%></digi:easternArabicNumber></option>
				                                    			<%
				                                    				} else {
				                                    			%>
                                                                <option value="<%=k%>"><digi:easternArabicNumber><%=k%></digi:easternArabicNumber></option>
					                                    		<%
					                                    			}
					                                    											}
					                                    		%>
				                                    			
				                                    		</select>
				                                    	    <digi:trn key="aim:globalSettings:minutes">(Minutes)</digi:trn>
				                                    	</c:when>
				                                    	<c:when test='${type == "t_static_range"}'>
				                                    		<%
				                                    			String dateValues = globalSett.getGlobalSettingsValue();
				                                    			int range = Integer.parseInt(dateValues);
				                                    			g_range = range;
				                                    		%>
				                                    		<select styleClass="inp-text" name="gsfValue" >
				                                    			<%
				                                    				for (int k = 10; k <= 100; k += 10) {
				                                    				if (k == range) {
				                                    			%>
				                                    					<option value="<%=k%>" selected="selected"><digi:easternArabicNumber><%=k%></digi:easternArabicNumber></option>
				                                    			<%
				                                    				} else {
				                                    			%>
					                                    				<option value="<%=k%>"><digi:easternArabicNumber><%=k%></digi:easternArabicNumber></option>
					                                    		<%
					                                    			}
					                                    											}
					                                    		%>
				                                    		</select>
				                                    	</c:when>
				                                    	
				                                    	<c:when test='${type == "t_static_year"}'>
				                                    		<%
				                                    			String dateValues = globalSett
				                                    													.getGlobalSettingsValue();
				                                    											int year = Integer
				                                    													.parseInt(dateValues);
				                                    											g_year = year;
				                                    		%>
				                                    		<select styleClass="inp-text" name="gsfValue" >
				                                    			<%
				                                    				for (int k = 1980; k <= 2020; k++) {
				                                    													if (k == year) {
				                                    			%>
																<option value="<%=k%>" selected="selected"><digi:easternArabicNumber><%=k%></digi:easternArabicNumber></option>
				                                    			<%
				                                    				} else {
				                                    			%>
					                                    				<option value="<%=k%>"><digi:easternArabicNumber><%=k%></digi:easternArabicNumber></option>
					                                    		<%
					                                    			}
					                                    											}
					                                    		%>
				                                    		</select>
				                                    	</c:when>
														
														<c:when test='${type == "t_year_default_start" || type == "t_year_default_end"}'>
				                                    		<%
				                                    			g_year = Integer
				                                    													.parseInt(FeaturesUtil
				                                    															.getGlobalSettingValue(GlobalSettingsConstants.YEAR_RANGE_START));
				                                    											g_range = Integer
				                                    													.parseInt(FeaturesUtil
				                                    															.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_OF_YEARS_IN_RANGE));
				                                    											int default_year = Integer
				                                    													.parseInt(globalSett
				                                    															.getGlobalSettingsValue());

				                                    		%>
				                                    		<select styleClass="inp-text" name="gsfValue" id="gfsv_<%=gfId%>">
				                                    		    <option value="-1"><digi:trn key="aim:globalSettings:Disabled">Disabled</digi:trn></option>
				                                    			<%
				                                    				for (int k = g_year; k <= g_year
				                                    														+ g_range; k++) {
				                                    													if (k == default_year) {
				                                    			%>
																<option value="<%=k%>" selected="selected"><digi:easternArabicNumber><%=k%></digi:easternArabicNumber></option>
				                                    			<%
				                                    				} else {
				                                    			%>
					                                    				<option value="<%=k%>"><digi:easternArabicNumber><%=k%></digi:easternArabicNumber></option>
					                                    		<%
					                                    			}
					                                    											}
					                                    		%>
				                                    		</select>
				                                    	</c:when>
				                                    	<c:when test='${type == "t_audit_trial_clenaup"}'>
				                                    	<%
				                                    		String peridiodvalues = globalSett
				                                    												.getGlobalSettingsValue();
				                                    										int selected = Integer
				                                    												.parseInt(peridiodvalues);
				                                    	%>
															<select styleClass="inp-text" name="gsfValue" >
															<option value="-1"><digi:trn key="aim:globalSettings:Disabled">Disabled</digi:trn></option>
															<%
																for (int k = 30; k <= 90; k += 30) {
																									if (k == selected) {
															%>
				                                    					<option value="<%=k%>" selected="selected">
																			<digi:easternArabicNumber><%=k%></digi:easternArabicNumber>
				                                    						<digi:trn key="aim:globalSettings:Days"> 
				                                    							Days
				                                    						</digi:trn>
				                                    					</option>
				                                    						 
				                                    			<%
				                                    						 				                                    				} else {
				                                    						 				                                    			%>
					                                    				<option value="<%=k%>">
																			<digi:easternArabicNumber><%=k%></digi:easternArabicNumber>
					                                    					<digi:trn key="aim:globalSettings:Days"> 
				                                    							Days
				                                    						</digi:trn>
					                                    				</option>
					                                    		<%
					                                    			}
					                                    											}
					                                    		%>
															</select>
															<%
																if (!globalSett
																										.getGlobalSettingsValue()
																										.equalsIgnoreCase("-1")) {
																									String sdate = org.digijava.module.common.util.DateTimeUtil
																											.formatDate(AuditCleaner
																													.getInstance()
																													.getNextcleanup());
															%>
																<br>
																<digi:trn key="aim:globalSettings:NextCleanUp">Next Audit Cleanup:</digi:trn>
																<%=" " + sdate%>
															<%
																}
															%>
														</c:when>
				                                    	
														<c:when test='${type == "t_components_sort"}'>
															<html:select property="gsfValue" styleClass="inp-text" value='<%= globalSett.getGlobalSettingsValue() %>'>
																<html:option value="default"><digi:trn key="aim:globalSettings:default">Default</digi:trn></html:option>
																<html:option value="code"><digi:trn key="aim:globalsettings:components_sort_order:by_code">Code</digi:trn></html:option>
																<html:option value="date"><digi:trn key="aim:globalsettings:components_sort_order:by_date">Date</digi:trn></html:option>
																<html:option value="title"><digi:trn key="aim:globalsettings:components_sort_order:by_title">Title</digi:trn></html:option>
																<html:option value="type"><digi:trn key="aim:globalsettings:components_sort_order:by_type">Type</digi:trn></html:option>
															</html:select>
														</c:when>
														
				                                    	<c:when test='${type == "t_Boolean"}'>
				                                    		<html:select property="gsfValue" styleClass="inp-text" onchange='boolean_field_changed(this);' value='<%= globalSett.getGlobalSettingsValue() %>'>
				                                    			<html:option value="true"><digi:trn key="aim:globalsettings:true">true</digi:trn></html:option>
				                                    			<html:option value="false"><digi:trn key="aim:globalsettings:false">false</digi:trn></html:option>
				                                    		</html:select>
				                                    	</c:when>
														<c:otherwise>
				                                      		<html:text property="gsfValue" styleClass="inp-text" value = '<%= EasternArabicService.getInstance().convertToEasternArabicBasedOnCurrentLocale(globalSett.getGlobalSettingsValue()) %>' />
				                                      	</c:otherwise>
			                                      	</c:choose>
			                                    </logic:empty>                                
			                                   </td>
											</digi:form>
			                            </tr>
									</logic:equal>
		                            </logic:iterate>
		                            </logic:notEmpty>
									</table>
									<br /> <br />
								</div>
								</logic:iterate>
									<digi:form  action="/GlobalSettings.do?" method="post" onsubmit="return saveAllSettings()" >	
										<table width="100%" border="0">
	  			                            <tr>
				                              <td colspan="2" align="center" valign="bottom" >
					                              	<html:hidden property="allValues"/>
					                              	<html:hidden property="indexTab"/>
					                              	<html:submit property="saveAll" styleClass="buttonx" styleId="gsSaveAllBtn">
					                                    <digi:trn key="aim:saveAll">
					                                    	Save All                                    
					                                    </digi:trn>
					                                </html:submit>			                                
				                              </td>			                           
				                           </tr>
										</table>
									</digi:form>
			                </div>
			        	</div>	
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<script type="text/javascript">
	var myTabs = new YAHOO.widget.TabView("demo");
	myTabs.set('activeIndex',<%=aimGlobalSettingsForm.getIndexTab()%>);	
</script>