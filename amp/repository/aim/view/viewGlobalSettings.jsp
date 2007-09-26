<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ page import="java.util.Map"%>


<script langauage="JavaScript"><!--
function saveClicked() {
  <digi:context name="preview" property="context/module/moduleinstance/GlobalSettings.do?action=save" />
  document.aimGlobalSettingsForm.action = "<%= preview %>";
  document.aimGlobalSettingsForm.target = "_self";
  document.aimGlobalSettingsForm.submit();

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

--></script>

<digi:instance property="aimGlobalSettingsForm" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
<html:hidden property="event" value="view"/>

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
  <tr>
    <td class=r-dotted-lg width=14>&nbsp;</td>
    <td align=left class=r-dotted-lg vAlign=top width=750>
      <table cellPadding=5 cellSpacing=0 width="100%" border=0>
        <tr>
          <!-- Start Navigation -->
          <td height=33><span class=crumb>
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
          </td>
          <!-- End navigation -->
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
          <td height=16 vAlign=center width=571>
            <html:errors />
          </td>
        </tr>
        <tr>
          <td noWrap width=100% vAlign="top">
            <table width="100%" cellspacing=1 cellSpacing=1 border=0>
              <tr><td noWrap width=600 vAlign="top">
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
                          <td bgcolor="#fefefe">
                          &nbsp;
                          </td>


                          <logic:notEmpty name="aimGlobalSettingsForm" property="gsfCol">
                            <logic:iterate name="aimGlobalSettingsForm" property="gsfCol" id="globalSett"
                            type="org.digijava.module.aim.dbentity.AmpGlobalSettings	">
                            <tr>
                              <td bgcolor="#ffffff">

                                <c:set var="globaLset">
                                  <bean:write name="globalSett" property="globalSettingsName"/>
                                </c:set>
                                <digi:trn key="aim:Global:${globaLset}">
                                  <bean:write name="globalSett" property="globalSettingsName"/>
                                </digi:trn>

                                <logic:notEmpty name="globalSett" property="globalSettingsDescription">
                                  <img src= "../ampTemplate/images/help.gif" border="0" title="<bean:write name="globalSett" property="globalSettingsDescription"/>">
                                </logic:notEmpty>
                              </td>
                              <td bgcolor="#ffffff">
                                <b>
                                  <bean:define id="thisForm" name="aimGlobalSettingsForm" type="org.digijava.module.aim.form.GlobalSettingsForm" />
                                  <%
                                  Map dictionary	= thisForm.getPossibleValuesDictionary(globalSett.getGlobalSettingsName());
                                  if (dictionary != null) {
                                    String dictionaryValue	=  (String)dictionary.get(globalSett.getGlobalSettingsValue());
                                    if ( dictionaryValue == null )
                                    dictionaryValue	= "n/a";
                                    %>
                                    <c:set var="dic">
                                      <%out.write(dictionaryValue);%>
                                    </c:set>
                                    <digi:trn key='aim:globalSettings:${fn:replace(dic, " ", "_")}'>
                                      <%out.write(dictionaryValue);%>
                                    </digi:trn>

                                    <%}else{%>
                                    <digi:trn key='aim:globalSettings:${fn:replace(globalSett.globalSettingsValue, " ", "_")}'>
                                      <%=globalSett.getGlobalSettingsValue()%>
                                    </digi:trn>
                                    <%}%>
                                </b>
                              </td>


                              <digi:form action="/GlobalSettings.do" method="post" >
                                <td bgcolor="#f4f4f2" >
                                  <html:hidden property="globalId" name="globalSett"/>
                                  <html:hidden property="globalSettingsName" name="globalSett"/>


                                  <% 
                                  	String possibleValues 	= "possibleValues(" + globalSett.getGlobalSettingsName() + ")"; 
                                  	String gsType			= globalSett.getGlobalSettingsPossibleValues();
                                  %>

                                  <logic:notEmpty name="aimGlobalSettingsForm" property='<%= possibleValues %>'>

                                    <%if (globalSett.getGlobalSettingsName().trim().equals("Default Country".trim())) { %>
                                    <html:select property="gsfValue" styleClass="inp-text" value='<%= globalSett.getGlobalSettingsValue() %>'>
                                      <logic:iterate name="aimGlobalSettingsForm" property='<%=possibleValues%>' id="global">
                                        <html:option value="${global.key}">${global.value}</html:option>
                                      </logic:iterate>
                                    </html:select>
                                    <%}else { %>
                                    <html:select property="gsfValue" styleClass="inp-text" value='<%= globalSett.getGlobalSettingsValue() %>'>
                                      <logic:iterate name="aimGlobalSettingsForm" property='<%=possibleValues%>' id="global">
                                      	<c:set var="valueOfSetting">
                                      		<%= globalSett.getGlobalSettingsPossibleValues()%>
                                      	</c:set>
                                        <c:set var="globSettings">
                                          <digi:trn key='aim:globalSettings:${fn:replace(global.value, " ", "_")}'>${global.value}</digi:trn>
                                        </c:set>

                                        <html:option value="${global.key}">${globSettings}</html:option>
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
	                                    		<select id="<%= monthId %>" onchange="populateWithDays('<%=monthId %>','<%=dayId %>');createDateString('<%=monthId %>','<%=dayId %>')">
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
	                                    		<select id="<%= dayId %>" onchange="createDateString('<%=monthId %>','<%=dayId %>');">
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
	                                    	<c:otherwise>
	                                      		<html:text property="gsfValue" styleClass="inp-text" value='<%= globalSett.getGlobalSettingsValue() %>' />
	                                      	</c:otherwise>
                                      	</c:choose>
                                    </logic:empty>
                                </td>
                                <td bgcolor="#f4f4f2" >
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
                      </table>
          </td></tr>
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
                  </tr>
</table>

