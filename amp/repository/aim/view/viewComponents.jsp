<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script langauage="JavaScript">
function onDelete() {
  var flag = confirm('<digi:trn jsFriendly="true" key="aim:deletethiscomponent">Delete this component</digi:trn>');
  return flag;
}

function addComponent()
{
  openNewWindow(600, 400);
  <digi:context name="addCompIndicator" property="context/ampModule/moduleinstance/updateComponents.do?event=add" />
  document.aimUpdateComponentsForm.action = "<%= addCompIndicator %>";
  document.aimUpdateComponentsForm.target = popupPointer.name;
  document.aimUpdateComponentsForm.submit();
  return true;
}

function editComponent(id){
  openNewWindow(600, 400);
  <digi:context name="editComponent" property="context/ampModule/moduleinstance/updateComponents.do?event=edit" />
  document.aimUpdateComponentsForm.action = "<%= editComponent %>&componentId="+id;
  document.aimUpdateComponentsForm.target = popupPointer.name;
  document.aimUpdateComponentsForm.submit();
}

function deleteComponent(id){
  <digi:context name="delComponent" property="context/ampModule/moduleinstance/updateComponents.do?event=delete" />
  document.aimUpdateComponentsForm.action = "<%= delComponent %>&componentId="+id;
  document.aimUpdateComponentsForm.target = "_self";
  document.aimUpdateComponentsForm.submit();
}
</script>

<digi:instance property="aimComponentsForm" />
<digi:form action="/updateComponents.do" method="post">
  <digi:context name="digiContext" property="context" />

  <!--  AMP Admin Logo -->
  <jsp:include page="teamPagesHeader.jsp"  />
  <!-- End of Logo -->


  <table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
    <tr>
      <td class=r-dotted-lg width=14>&nbsp;</td>
      <td align=left class=r-dotted-lg valign="top" width=750>
        <table cellPadding=5 cellspacing="0" width="100%" border="0">
          <tr>
            <!-- Start Navigation -->
            <td height=33><span class=crumb>
              <c:set var="ToViewAdmin">
                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
              </c:set>
              <digi:link href="/admin.do" styleClass="comment" title="${ToViewAdmin}" >
                <digi:trn key="aim:AmpAdminHome">
                Admin Home
                </digi:trn>
              </digi:link>&nbsp;&gt;&nbsp;
              <digi:trn key="aim:componentManager">
              Component Manager
              </digi:trn>
            </td>
            <!-- End navigation -->
          </tr>
          <tr>
           <c:set var="componentsManagerTitle">
           <digi:trn key="aim:componentsManagerTitle">Components Manager</digi:trn>
          </c:set>

            <td height=16 valign="center" width=571><span class=subtitle-blue><c:out value="${componentsManagerTitle}"/></span>
      </td>
          </tr>
          <tr>
            <td height=16 valign="center" width=571>
              <digi:errors />
            </td>
          </tr>
          <tr>
            <td noWrap width="100%" vAlign="top">
              <table width="100%" cellspacing="1" cellspacing="1" border="0">
                <tr><td noWrap width=600 vAlign="top">
                  <table bgColor=#d7eafd cellpadding="1" cellspacing="1" width="100%" valign="top">
                    <tr bgColor=#ffffff>
                      <td vAlign="top" width="100%">

                        <table width="100%" cellspacing="1" cellpadding="1" valign="top" align=left>
                          <tr><td bgColor=#d7eafd class=box-title height="20" align="center">
                            <!-- Table title -->
                            <digi:trn key="aim:components">
                            Components
                            </digi:trn>
                            <!-- end table title -->
                      </td></tr>
                      <tr><td>
                        <table width="100%" cellspacing="1" cellpadding=4 valign="top" align=left bgcolor="#d7eafd">
                          <logic:empty name="aimComponentsForm" property="components">
                            <tr bgcolor="#ffffff">
                              <td colspan="5" align="center"><b>
                                <digi:trn key="aim:noComponents">
                                No Components present
                                </digi:trn>
</b></td>
                            </tr>
                          </logic:empty>
                          <logic:notEmpty name="aimComponentsForm" property="components">
                            <logic:iterate name="aimComponentsForm" property="components" id="componentlist"
                            type="org.digijava.ampModule.aim.dbentity.AmpComponent">
                            <tr>
                              <td bgcolor="#ffffff">
                                <jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
                                <c:set var="ToEditComponents">
                                  <digi:trn key="aim:clickToEditComponents">Click here to Edit</digi:trn>
                                </c:set>
                                <a title="<bean:write name="componentlist" property="title"/>" href="javascript:editComponent(<bean:write name="componentlist" property="ampComponentId"/>)">
                                <bean:write name="componentlist" property="title"/>
</a>
                              </td>
                              <td bgcolor="#ffffff" align="center" nowrap="nowrap">
                                <c:set var="ToEditComponent1">
                                  <digi:trn key="aim:clickToEditComponent">Click here to Edit Component</digi:trn>
                                </c:set>
                                <c:set var="edit">
                                  <digi:trn key="aim:edit">Edit</digi:trn>
                                </c:set>
                                [ <a href="javascript:editComponent(<bean:write name="componentlist" property="ampComponentId"/>)"> <c:out value="${edit}"/></a> ]


                              </td>

                              <%--<logic:equal name="aimAddSectorForm" property="deleteSchemeFlag" value="true">--%>
                                <td bgcolor="#ffffff" width="55" align="center" nowrap="nowrap">
                                  <c:set var="delete">
                                    <digi:trn key="aim:deleteComponent">Delete</digi:trn>
                                  </c:set>

                                  <c:set var="ForDeleteComponent">
                                    <digi:trn key="aim:cantDeleteComponent">Can't delete component because it's assigned to activity</digi:trn>
                                  </c:set>
                                  <c:if test="${!empty componentlist.activities}">
                                    [<a title="${ForDeleteComponent}" >${delete}</a>]
                                  </c:if>

                                  <c:set var="ForDeleteComponent">
                                    <digi:trn key="aim:clickToDeleteComponent">Click here to delete component</digi:trn>
                                  </c:set>
                                  <c:if test="${empty componentlist.activities}">
                                    [<a title="${ForDeleteComponent}" href="javascript:deleteComponent(${componentlist.ampComponentId});">${delete}</a>]
                                  </c:if>
                                </td>
                            </tr>
                            </logic:iterate>
                          </logic:notEmpty>
                          <!-- end page logic -->
                        </table>
                              </td>
                      </tr>
                      <tr>
                        <td height="20" bgColor=#d7eafd class=box-title align="center"><B>
                          <c:set var="translation">
                            <digi:trn key="btn:componentManagerAddComponent">Add Component</digi:trn>
                          </c:set>
                          <html:button styleClass="dr-menu" value="${translation}" property="submitButton"  onclick="addComponent()">
                            <digi:trn key="aim:clickToAddComponent">Click here to Add a Component</digi:trn>
                          </html:button>
                        </td>
                      </tr>
                        </table>

            </td>
                          </tr>
                  </table>
</td>

<td noWrap width="100%" vAlign="top">
  <table align="center" cellpadding="0" cellspacing="0" width="90%" border="0">
    <tr>
      <td>
        <!-- Other Links -->
        <table cellpadding="0" cellspacing="0" width="120">
          <tr>
            <td bgColor=#c9c9c7 class=box-title>
              <digi:trn key="aim:otherLinks">
              Other links
              </digi:trn>
            </td>
            <td background="ampModule/aim/images/corner-r.gif" height="17" width="17">&nbsp;
            
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td bgColor=#ffffff class=box-border>
        <table cellPadding=5 cellspacing="1" width="100%">
<!--        the following comments will be removed further-->
<!--          <tr>-->
<!--            <td nowrap>-->
<!--              <digi:img src="ampModule/aim/images/arrow-014E86.gif" width="15" height="10"/>-->
<!--              <c:set var="toCompIndicatorManager">-->
<!--                <digi:trn key="aim:clickforCompIndManager">Click here to goto Component Indicator Manager</digi:trn>-->
<!--              </c:set>-->
<!--              <digi:link href="/componentIndicatorManager.do"  title="${toCompIndicatorManager}" >-->
<!--                <digi:trn key="aim:componentsIndicatorManager">-->
<!--                Component Indicator Manager-->
<!--                </digi:trn>-->
<!--              </digi:link>-->
<!--            </td>-->
<!--          </tr>-->
          <tr>
            <td class="inside">
              <digi:img src="ampModule/aim/images/arrow-014E86.gif" width="15" height="10"/>
              <c:set var="ToViewAdmin">
                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
              </c:set>

              <digi:link href="/admin.do" title="${ToViewAdmin}" >
                <digi:trn key="aim:AmpAdminHome">
                Admin Home
                </digi:trn>
              </digi:link>
            </td>
          </tr>
          <!-- end of other links -->
        </table>
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
  </table>
</digi:form>

