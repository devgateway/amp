<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ page import="java.util.Map"%>


<%@page import="org.digijava.module.aim.helper.GlobalSettings"%>
<%@page import="org.digijava.module.aim.util.FeaturesUtil"%>
<%@page import="java.util.Date"%>
<%@page import="org.digijava.module.aim.services.auditcleaner.AuditCleaner"%>
<script langauage="JavaScript"><!--
function saveClicked() {

  <digi:context name="preview" property="context/module/moduleinstance/GlobalSettings.do?action=save" />
  document.aimGlobalSettingsForm.action = "<%= preview %>";
  document.aimGlobalSettingsForm.target = "_self";
  document.aimGlobalSettingsForm.submit();

}
function validateCustomFields(form){
	if (form.gsfValue.value==''){
		alert('<digi:trn key="aim:Global:validation">You must provide a value for</digi:trn>: '+form.globalSettingsNameTranslated.value)	
		return false;
	}
	return true;
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

function saveAllSettings(){
	var allvalues='';
	for (i=0;i < document.aimGlobalSettingsForm.length -1;i++){
	  if (document.aimGlobalSettingsForm[i].globalId){
			if (!validateCustomFields(document.aimGlobalSettingsForm[i])){
			return false;
		}
		    var id=document.aimGlobalSettingsForm[i].globalId.value;
	    	var val=document.aimGlobalSettingsForm[i].gsfValue.value;
		    allvalues=allvalues+id+"="+val+"&";
		}
	  
	}
	document.aimGlobalSettingsForm[document.aimGlobalSettingsForm.length -1].allValues.value=allvalues;
	return true;
}
--></script>

<digi:instance property="aimGlobalSettingsForm" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
<html:hidden property="event" value="view"/>

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=760>
  <tr>
    <td class=r-dotted-lg width=14>&nbsp;</td>
    <td align=left class=r-dotted-lg vAlign=top width=750>
      <table cellPadding=5 cellSpacing=0 width="100%" border=0>
        <tr><td height=33>
          <span class=crumb>
             <c:set var="clickToViewAdmin">
                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
             </c:set>
             <digi:link href="/admin.do" styleClass="comment" title="${clickToViewAdmin}" >
               <digi:trn key="aim:AmpAdminHome">
                 Admin Home
               </digi:trn>
             </digi:link>&nbsp;&gt;&nbsp;
             <digi:trn key="aim:globalSettingsManager">
              Global Settings Manager
             </digi:trn>
          </span></td>
        </tr>
        <tr>
          <td height=16 vAlign=center width=571>
            <span class=subtitle-blue>
              <digi:trn key="aim:globalSettingsManager">
              Global Settings Manager
              </digi:trn>
            </span>
          </td>
        </tr>
        <tr>
          <td height=16 vAlign=center>
            <digi:errors />
            <table bgColor=#d7eafd cellPadding=1 cellSpacing=1 width="100%" valign="top">
                  <tr bgColor=#ffffff>
                    <td vAlign="top" width="100%">

                      <table width="100%" cellspacing=0 cellpadding=0 valign=top align=left>
                        <tr><td bgColor=#d7eafd class=box-title height="20" align="center">
                          <!-- Table title -->
                          <digi:trn key="aim:systemSettings">
                          System Settings
                          </digi:trn>
                          <!-- end table title -->
                    </td></tr>
                    <tr><td>
                      <table width="100%" cellspacing=1 cellpadding=1 valign=top align=left bgcolor="#d7eafd">
                        <tr>
                          <td bgcolor="#fefefe">
                            <b>
                              <digi:trn key="aim:systemName">
                              Setting Name                              
                              </digi:trn>
                            </b>                          
                           </td>
                          <td bgcolor="#fefefe">
                            <b>
                              <digi:trn key="aim:currentValue">
                              Current Value                              
                              </digi:trn>
                            </b>                          
                           </td>
                          <td bgcolor="#fefefe">
                            <b>
                              <digi:trn key="aim:newValue">
                              New Value                              
                              </digi:trn>
                            </b>                          
                            </td>
                          <td bgcolor="#fefefe">&nbsp;                          
                          </td>
							</tr>
                           <%int g_range=0, g_year=0; %>
                          <logic:notEmpty name="aimGlobalSettingsForm" property="gsfCol">
                            <logic:iterate name="aimGlobalSettingsForm" property="gsfCol" id="globalSett"
                            type="org.digijava.module.aim.dbentity.AmpGlobalSettings ">
 
                            <tr>
                              <td bgcolor="#ffffff">
								<c:set var="globaLset">
                                  <bean:write name="globalSett" property="globalSettingsName"/>
                                </c:set>
                                <digi:trn key="aim:Global:${globaLset}"><bean:write name="globalSett" property="globalSettingsName"/></digi:trn>

                                <logic:notEmpty name="globalSett" property="globalSettingsDescription">
                                  <img src= "../ampTemplate/images/help.gif" border="0" title="<bean:write name="globalSett" property="globalSettingsDescription"/>">                                </logic:notEmpty>                              </td>
                              <td bgcolor="#ffffff">
                                <b>
                                  <bean:define id="thisForm" name="aimGlobalSettingsForm" type="org.digijava.module.aim.form.GlobalSettingsForm" />
                                  <%
                                  Map dictionary		= thisForm.getPossibleValuesDictionary(globalSett.getGlobalSettingsName());
                                  String currentValue	= globalSett.getGlobalSettingsValue();
                                  if (dictionary != null) {
                                    currentValue		=  (String)dictionary.get( currentValue );
                                    //if not  the value is into dictionary set current for custom value fields 
                                    if (currentValue==null && globalSett.getGlobalSettingsValue()!=null){
                                		currentValue=globalSett.getGlobalSettingsValue();
                                     }
                                    
                                    if ( currentValue == null )
                                    	currentValue	= "n/a";
                                  }
                                  String key			= "aim:globalSettings:" + globalSett.getGlobalSettingsName() + ":" + currentValue;
                                  %>
                                  	<c:set var="newKey"><%out.write( key );%></c:set>
                                    <digi:trn key='${fn:replace(newKey, " ", "_")}'>
                                      <%out.write(currentValue);%>
                                    </digi:trn>
                                </b>                              
                              </td>

								<digi:form action="/GlobalSettings.do" method="post" onsubmit="return validateCustomFields(this)" >
                                <td bgcolor="#ffffff" >
                                  <html:hidden property="globalId" name="globalSett"/>
                                  <html:hidden property="globalSettingsName" name="globalSett"/>
								 <input type="hidden" name="globalSettingsNameTranslated" value='<digi:trn key="aim:Global:${globaLset}"><bean:write name="globalSett" property="globalSettingsName"/></digi:trn>'>
									
                                  <% 
                                  	String possibleValues 	= "possibleValues(" + globalSett.getGlobalSettingsName() + ")"; 
                                  	String gsType			= globalSett.getGlobalSettingsPossibleValues();
                                  %>

                                  <logic:notEmpty name="aimGlobalSettingsForm" property='<%= possibleValues %>'>
								
                                    <%if (globalSett.getGlobalSettingsName().trim().equalsIgnoreCase("Default Country".trim())) { %>
                                    <html:select property="gsfValue" styleClass="inp-text" value='<%= globalSett.getGlobalSettingsValue() %>'>
                                      <logic:iterate name="aimGlobalSettingsForm" property='<%=possibleValues%>' id="global">
                                        <html:option value="${global.key}">${global.value}</html:option>
                                      </logic:iterate>
                                    </html:select>
                                    <%}else if(globalSett.getGlobalSettingsName().trim().equalsIgnoreCase("Default Number Format".trim())) {%>
                                  	
                                  	<select name="options"  onchange="if(this.value!='noselection'){gsfValue.value=this.value}" styleClass="inp-text" 
                                  	value='<%= globalSett.getGlobalSettingsValue()%>'>
                                      <option value="noselection"><digi:trn key="aim:gloablSetting:selectFormat">(Select Format)</digi:trn> </option>
                                      <logic:iterate name="aimGlobalSettingsForm" property='<%=possibleValues%>' id="global">
                                        <option value="${global.key}">${global.value}</option>
                                      </logic:iterate>
                                    </select>
                                    <digi:trn key="aim:gloablSetting:predefinedFormat">(Predefined Format)</digi:trn> <br>
                                    
                                    <html:text property="gsfValue" value="<%= globalSett.getGlobalSettingsValue()%>"></html:text> 
                                    <digi:trn key="aim:gloablSetting:customFormat">(Custom Format)</digi:trn> 	
                                    <%}else { %>
                                    	
                                    
                                    <html:select   property="gsfValue" alt="prueba"  style="width:180px"  styleClass="inp-text" value='<%= globalSett.getGlobalSettingsValue() %>'>
                                      <logic:iterate name="aimGlobalSettingsForm" property='<%=possibleValues%>' id="global" type="org.digijava.module.aim.helper.KeyValue">
                                      	<% String key2	= "aim:globalSettings:" + globalSett.getGlobalSettingsName() + ":" + global.getValue(); %>
                                      	<c:set var="newKey"><%= key2 %></c:set>
                                        <c:set var="globSettings">
                                          <digi:trn key='${fn:replace(newKey, " ", "_")}'>${global.value}</digi:trn>
                                        </c:set>

                                        <html:option value="${global.key}">${global.value}</html:option>
                                      </logic:iterate>
                                    </html:select>
                                    <%} %>										
                                    </logic:notEmpty>
                                    <logic:empty name="aimGlobalSettingsForm" property='<%= possibleValues %>'>
                                    	<c:set var="type" value="<%=gsType %>" />
                                    	<c:choose>
	                                    	<c:when test='${type == "t_Date_No_Year"}'>
	                                    		<% 
	                                    			String monthId		= "month" + globalSett.getGlobalId() ;
		                                    		String dayId		= "day" + globalSett.getGlobalId() ;
		                                    		String [] dateValues	= globalSett.getGlobalSettingsValue().split("/") ;
		                                    		int monthNum		= Integer.parseInt(dateValues[1]);
	                                    		%>
	                                    		<html:hidden property="gsfValue" value='<%= globalSett.getGlobalSettingsValue() %>'/>
	                                    		<digi:trn key="aim:globalSettings:month">Month</digi:trn>: 
	                                    		<select styleClass="inp-text" id="<%= monthId %>" onchange="populateWithDays('<%=monthId %>','<%=dayId %>');createDateString('<%=monthId %>','<%=dayId %>')">
	                                    			<% for (int k=1; k<=12; k++) {
	                                    					if ( k == monthNum ) {
	                                    			%>
	                                    					<option selected="selected" value="<%=k %>"><%=k %></option>
	                                    			<%
		                                    				}
		                                    				else {
		                                    		%>
		                                    				<option  value="<%=k %>"><%=k %></option>
		                                    		<%
		                                    				}
	                                    				} 
	                                    			%>
	                                    		</select>
	                                    		<digi:trn key="aim:globalSettings:day">Day</digi:trn>: 
	                                    		<select styleClass="inp-text" id="<%= dayId %>" onchange="createDateString('<%=monthId %>','<%=dayId %>');">
	                                    			<% for (int k=1; k<=org.digijava.module.aim.action.GlobalSettings.numOfDaysInMonth(monthNum); k++) {
	                                    					if ( k == Integer.parseInt(dateValues[0]) ) {
	                                    			%>
	                                    					<option value="<%=k %>" selected="selected"><%=k %></option>
	                                    			<%
		                                    				}
		                                    				else {
		                                    		%>
		                                    				<option value="<%=k %>"><%=k %></option>
		                                    		<%
		                                    				}
	                                    				} 
	                                    			%>
	                                    		</select>
	                                    	</c:when>	
	                                    	<c:when test='${type == "t_daily_currency_update_hour"}'>
	                                    		<% 
	                                    			String hourId		= "hour" + globalSett.getGlobalId() ;
		                                    		String minId		= "min" + globalSett.getGlobalId() ;
		                                    		String ampmId       = "ampm"+ globalSett.getGlobalId() ;
		                                    		String [] timeValues    = globalSett.getGlobalSettingsValue().split(" ") ;
		                                    		String [] hourValues	= timeValues[0].split(":") ;
		                                    		int hourNum		= Integer.parseInt(hourValues[0]);
		                                    		int minNum      = Integer.parseInt(hourValues[1]);
		                                    		String ampmNum  = timeValues[1];
	                                    		%>
	                                    		<html:hidden property="gsfValue" value='<%= globalSett.getGlobalSettingsValue() %>'/>
	                                    		<digi:trn key="aim:globalSettings:hour">Hour</digi:trn>: 
	                                    		<select styleClass="inp-text" id="<%= hourId %>" onchange="createHourString('<%=hourId %>','<%=minId %>','<%=ampmId %>');">
	                                    			<% for (int k=1; k<=12; k++) {
	                                    					String val = (k<10)? "0"+k : String.valueOf(k);
	                                    					if ( k == hourNum ) {
	                                    			%>
	                                    					<option selected="selected" value="<%=k %>"><%=val %></option>
	                                    			<%
		                                    				}
		                                    				else {
		                                    		%>
		                                    				<option  value="<%=k %>"><%=val %></option>
		                                    		<%
		                                    				}
	                                    				} 
	                                    			%>
	                                    		</select>
	                                    		<digi:trn key="aim:globalSettings:min">Min</digi:trn>: 
	                                    		<select styleClass="inp-text" id="<%= minId %>" onchange="createHourString('<%=hourId %>','<%=minId %>','<%=ampmId %>');">
	                                    			<% for (int k=0; k<60; k+=5) {
	                                    				    String val = (k<10)? "0"+k : String.valueOf(k);
	                                    					if ( k == minNum ) {
	                                    			%>
	                                    					<option value="<%=k %>" selected="selected"><%=val %></option>
	                                    			<%
		                                    				}
		                                    				else {
		                                    		%>
		                                    				<option value="<%=k %>"><%=val %></option>
		                                    		<%
		                                    				}
	                                    				} 
	                                    			%>
	                                    		</select>
	                                    		<select styleClass="inp-text" id="<%= ampmId %>" onchange="createHourString('<%=hourId %>','<%=minId %>','<%=ampmId %>');">
	                                    			<%
	                                    				String[] ampm={"AM","PM"}; 
	                                    				for (int k=0; k<=1; k++) {
	                                    					if ( ampm[k].compareToIgnoreCase(ampmNum)==0 ) {
	                                    			%>
	                                    					<option value="<%=k %>" selected="selected"><%=ampm[k] %></option>
	                                    			<%
		                                    				}
		                                    				else {
		                                    		%>
		                                    				<option value="<%=k %>"><%=ampm[k] %></option>
		                                    		<%
		                                    				}
	                                    				} 
	                                    			%>
	                                    		</select>
	                                    		<br/>
	                                    		<digi:trn key="aim:globalSettings:ServerTime">Server Time</digi:trn>:&nbsp; 
	                                    		<% java.text.DateFormat formatter = new java.text.SimpleDateFormat("hh:mm:ss a"); 
	                                    		   String sdate = org.digijava.module.common.util.DateTimeUtil.formatDate(new java.util.Date());
			                                    %> 
			                                    <%= sdate +" "+formatter.format( new java.util.Date() ) %>  
	                                    	
	                                    	</c:when>
	                                    	<c:when test='${type == "t_static_range"}'>
	                                    		<% 
		                                    		String dateValues	= globalSett.getGlobalSettingsValue();
		                                    		int range		= Integer.parseInt(dateValues);
		                                    		g_range=range;
	                                    		%>
	                                    		<select styleClass="inp-text" name="gsfValue">
	                                    			<% for (int k=10; k<=100; k+=10) {
	                                    					if ( k == range ) {
	                                    			%>
	                                    					<option value="<%=k %>" selected="selected"><%=k %></option>
	                                    			<%
		                                    				}
		                                    				else {
		                                    		%>
		                                    				<option value="<%=k %>"><%=k %></option>
		                                    		<%
		                                    				}
	                                    				} 
	                                    			%>
	                                    		</select>
	                                    	</c:when>
	                                    	
	                                    	<c:when test='${type == "t_static_year"}'>
	                                    		<% 
		                                    		String dateValues	= globalSett.getGlobalSettingsValue();
	                                    		    int year				= Integer.parseInt(dateValues);
	                                    		    g_year = year;
	                                    		%>
	                                    		<select styleClass="inp-text" name="gsfValue">
	                                    			<% for (int k=1980; k<=2020; k++) {
	                                    					if ( k == year ) {
	                                    			%>
	                                    					<option value="<%=k %>" selected="selected"><%=k %></option>
	                                    			<%
		                                    				}
		                                    				else {
		                                    		%>
		                                    				<option value="<%=k %>"><%=k %></option>
		                                    		<%
		                                    				}
	                                    				} 
	                                    			%>
	                                    		</select>
	                                    	</c:when>
											
											<c:when test='${type == "t_year_default_start" || type == "t_year_default_end"}'>
	                                    		<% 
		                                    		String dateValues	= globalSett.getGlobalSettingsValue();
		                                    		int default_year		= Integer.parseInt(dateValues);
	                                    		%>
	                                    		<select styleClass="inp-text" name="gsfValue">
	                                    		    <option value="-1"><digi:trn key="aim:globalSettings:Disabled">Disabled</digi:trn></option>
	                                    			<% for (int k=g_year; k<=g_year+g_range; k++) {
	                                    					if ( k == default_year ) {
	                                    			%>
	                                    					<option value="<%=k %>" selected="selected"><%=k %></option>
	                                    			<%
		                                    				}
		                                    				else {
		                                    		%>
		                                    				<option value="<%=k %>"><%=k %></option>
		                                    		<%
		                                    				}
	                                    				} 
	                                    			%>
	                                    		</select>
	                                    	</c:when>
	                                    	<c:when test='${type == "t_audit_trial_clenaup"}'>
	                                    	<% 
		                                    	String peridiodvalues	= globalSett.getGlobalSettingsValue();
		                                    	int selected		= Integer.parseInt(peridiodvalues);
	                                    	%>
												<select styleClass="inp-text" name="gsfValue">
												<option value="-1"><digi:trn key="aim:globalSettings:Disabled">Disabled</digi:trn></option>
												<% for (int k=30; k<=90; k+=30) {
	                                    					if ( k == selected ) {
	                                    			%>
	                                    					<option value="<%=k %>" selected="selected"><%=k %>
	                                    						<digi:trn key="aim:globalSettings:Days"> 
	                                    							Days
	                                    						</digi:trn>
	                                    					</option>
	                                    						 
	                                    			<%
		                                    				}
		                                    				else {
		                                    		%>
		                                    				<option value="<%=k %>"><%=k %>
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
												if (!globalSett.getGlobalSettingsValue().equalsIgnoreCase("-1")){
													String sdate = org.digijava.module.common.util.DateTimeUtil.formatDate(AuditCleaner.getInstance().getNextcleanup()); %>
													<br>
													<digi:trn key="aim:globalSettings:NextCleanUp">Next Audit Cleanup:</digi:trn>
													<%=" " + sdate%>
												<%}%>
											</c:when>
	                                    	
											
	                                    	<c:when test='${type == "t_Boolean"}'>
	                                    		<html:select property="gsfValue" styleClass="inp-text" value='<%= globalSett.getGlobalSettingsValue() %>'>
	                                    			<html:option value="true">true</html:option>
	                                    			<html:option value="false">false</html:option>
	                                    		</html:select>
	                                    	</c:when>
	                                    	<c:otherwise>
	                                      		<html:text property="gsfValue" styleClass="inp-text" value='<%= globalSett.getGlobalSettingsValue() %>' />
	                                      	</c:otherwise>
                                      	</c:choose>
                                    </logic:empty>                                
                                   </td>
                                <td bgcolor="#f4f4f2">
                                  <html:submit property="save">
                                    <digi:trn key="aim:save">
                                    	save                                    
                                    </digi:trn>
                                  </html:submit>                                
                                  </td>
                              </digi:form>
                            </tr>
                            </logic:iterate>
                          </logic:notEmpty>
                          <!-- end page logic -->
                          <tr>
                              <td colspan="4" align="right" bgcolor="#ffffff">
                              	<digi:form  action="/GlobalSettings.do" method="post" onsubmit="return saveAllSettings()" >
                              
                              <html:hidden property="allValues"/>
                              	<html:submit property="saveAll">
                                    <digi:trn key="aim:saveAll">
                                    	Save All                                    
                                    </digi:trn>
                                  </html:submit> 
                              </digi:form>
                              </td>
                           
                            </tr>
                      </table>
          </td></tr>
                      </table>

    </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td noWrap width=100% vAlign="top">
          
				</td>
                  </tr>	
	</table>
</td>
</tr>
</table>
