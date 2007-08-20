<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ page import="java.util.Map"%>


<script langauage="JavaScript">
function saveClicked() {
  <digi:context name="preview" property="context/module/moduleinstance/GlobalSettings.do?action=save" />
  document.aimGlobalSettingsForm.action = "<%= preview %>";
  document.aimGlobalSettingsForm.target = "_self";
  document.aimGlobalSettingsForm.submit();

}
</script>

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
                                    <digi:trn key="aim:${dic}">
                                      <%out.write(dictionaryValue);%>
                                    </digi:trn>

                                    <%}else{%>
                                    <digi:trn key="aim:<%=globalSett.getGlobalSettingsValue()%>">
                                      <%=globalSett.getGlobalSettingsValue()%>
                                    </digi:trn>
                                    <%}%>




                                </b>
                              </td>


                              <digi:form action="/GlobalSettings.do" method="post" >
                                <td bgcolor="#f4f4f2" >
                                  <html:hidden property="globalId" name="globalSett"/>
                                  <html:hidden property="globalSettingsName" name="globalSett"/>


                                  <% String possibleValues = "possibleValues(" + globalSett.getGlobalSettingsName() + ")"; %>

                                  <logic:notEmpty name="aimGlobalSettingsForm" property='<%= possibleValues %>'>


                                    <%if (globalSett.getGlobalSettingsName().trim().equals("Default Country".trim())) { %>
                                    <html:select property="gsfValue" styleClass="inp-text" value='<%= globalSett.getGlobalSettingsValue() %>'>
                                      <logic:iterate name="aimGlobalSettingsForm" property='<%=possibleValues%>' id="global">
                                        <c:set var="globSettings">
                                          <digi:trn key="aim:cn:${global.key}">${global.value}</digi:trn>
                                        </c:set>
                                        <option>${globSettings}</option>
                                      </logic:iterate>
                                    </html:select>
                                    <%}else { %>
                                    <html:select property="gsfValue" styleClass="inp-text" value='<%= globalSett.getGlobalSettingsValue() %>'>
                                      <logic:iterate name="aimGlobalSettingsForm" property='<%=possibleValues%>' id="global">
                                        <c:set var="globSettings">
                                          <digi:trn key="aim:globalSettings:${global.key}">${global.value}</digi:trn>
                                        </c:set>
                                        <option>${globSettings}</option>
                                      </logic:iterate>
                                    </html:select>
                                    <%} %>										</logic:notEmpty>
                                    <logic:empty name="aimGlobalSettingsForm" property='<%= possibleValues %>'>
                                      <html:text property="gsfValue" styleClass="inp-text" value='<%= globalSett.getGlobalSettingsValue() %>' />
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

