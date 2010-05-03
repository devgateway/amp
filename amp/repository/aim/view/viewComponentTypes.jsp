<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script langauage="JavaScript">
function onDelete() {
  var flag = confirm('<digi:trn key="aim:deletethisType">Delete this Type</digi:trn>');
  return flag;
}

function addType()
{
  openNewWindow(550, 300);
  <digi:context name="addComponentType" property="context/module/moduleinstance/updateComponentType.do?event=add" />
  document.aimComponentsTypeForm.action = "<%= addComponentType %>";
  document.aimComponentsTypeForm.target = popupPointer.name;
  document.aimComponentsTypeForm.submit();
  return true;
}

function editType(id){
  openNewWindow(600, 400);
  <digi:context name="editComponentType" property="context/module/moduleinstance/updateComponentType.do?event=edit" />
  document.aimComponentsTypeForm.action = "<%= editComponentType %>&id="+id;
  document.aimComponentsTypeForm.target = popupPointer.name;
  document.aimComponentsTypeForm.submit();
}

function deleteType(id){
if (onDelete()){
  <digi:context name="delComponentType" property="context/module/moduleinstance/updateComponentType.do?event=delete" />
  document.aimComponentsTypeForm.action = "<%= delComponentType %>&id="+id;
  document.aimComponentsTypeForm.target = "_self";
  document.aimComponentsTypeForm.submit();
}
}
</script>

<digi:instance property="aimComponentsTypeForm" />
<digi:form action="/updateComponentType.do" method="post">
 
 <digi:context name="digiContext" property="context" />

  <!--  AMP Admin Logo -->
  <jsp:include page="teamPagesHeader.jsp" flush="true" />
  <!-- End of Logo -->


  <table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
    <tr>
      <td class=r-dotted-lg width=14>&nbsp;</td>
      <td align=left class=r-dotted-lg vAlign=top width=750>
        <table cellPadding=5 cellSpacing=0 width="100%" border=0>
          <tr>
            <!-- Start Navigation -->
            <td height=33><span class="crumb">
              <c:set var="ToViewAdmin">
                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
              </c:set>
              <digi:link href="/admin.do" styleClass="comment" title="${ToViewAdmin}" >
                <digi:trn key="aim:AmpAdminHome">
                Admin Home
                </digi:trn>
              </digi:link>&nbsp;&gt;&nbsp;
              <digi:trn key="aim:componentTypeManager">
              Component Type Manager
              </digi:trn>
            </td>
            <!-- End navigation -->
          </tr>
          <tr>
           <c:set var="title">
           <digi:trn key="aim:componentTypesTitle">Component Types Manager</digi:trn>
          </c:set>

            <td height=16 vAlign=center width=571><span class=subtitle-blue><c:out value="${componentTypesTitle}"/></span>
      </td>
          </tr>
          <tr>
            <td height=16 vAlign=center width=571>
              <digi:errors />
            </td>
          </tr>
          <tr>
            <td noWrap width=100% vAlign="top">
            
          <tr>
            <td noWrap width=600 vAlign="top"><table bgcolor="#d7eafd" cellpadding="3" cellspacing="0" width="100%" valign="top">
              <tr bgcolor="#ffffff">
                <td valign="top" width="100%"><table width="100%" cellspacing="1" cellpadding="1" valign="top" align="left">
                    <tr>
                      <td bgcolor="#d7eafd" class="box-title" height="20"><!-- Table title -->
                          <digi:trn key="aim:componentTypes">
                            <h3>Component Types </h3>
                          </digi:trn>
                          <!-- end table title -->                      </td>
                    </tr>
                    <tr>
                      <td><table width="100%" cellspacing="2" cellpadding="2" valign="top" align="left" bgcolor="#d7eafd">
                          <logic:empty name="aimComponentsTypeForm" property="componentTypesList">
                            <tr bgcolor="#ffffff">
                              <td colspan="5" align="center"><b>
                                <digi:trn key="aim:noTypes"> No types present </digi:trn>
                              </b></td>
                            </tr>
                          </logic:empty>
                          <logic:notEmpty name="aimComponentsTypeForm" property="componentTypesList">
                            <logic:iterate name="aimComponentsTypeForm" property="componentTypesList" id="componentTypesList"
                            type="org.digijava.module.aim.dbentity.AmpComponentType">
                              <tr>
                                <td height="20" bgcolor="#ffffff">
                                
                            <jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
	                                    <c:set var="ToEditComponents">
	                                      <digi:trn key="aim:clickToEdit">Click here to Edit</digi:trn>
	                                    </c:set>
	                                    <bean:write  name="componentTypesList" property="code"/>                                     </td>
                                <td height="20" bgcolor="#ffffff" td>
                                <bean:write  name="componentTypesList" property="name"/>                                </td>
                                <td width="100" height="20" align="center" nowrap="nowrap" bgcolor="#ffffff">
                                  <c:set var="ToEditType">
                                  <digi:trn key="aim:clickToEditType">Click here to Edit Type</digi:trn>
                                </c:set>
                                <c:set var="edit">
                                  <digi:trn key="aim:edit">Edit</digi:trn>
                                </c:set>
                                [ <a href="javascript:editType(<bean:write name="componentTypesList" property="type_id"/>)"> <c:out value="${edit}"/></a> ]
								<c:set var="delete">
                                    <digi:trn key="aim:deleteType">Delete</digi:trn>
                                  </c:set>
								  <c:set var="ForDelete">
                                    <digi:trn key="aim:cantDeleteType">Can't delete type because it's assigned to an Compoenent</digi:trn>
                                  </c:set>
                                  <c:if test="${!empty componentTypesList.components}">
                                    [<a title="${ForDelete}"  style="text-decoration:none;color:#999999">${delete}</a>]
                                  </c:if>
                                 <c:set var="ForDelete">
                                   <digi:trn key="aim:clickToDeleteType">Click here to delete Type</digi:trn>
                                 </c:set>
                                  <c:if test="${empty componentTypesList.components}">
                                    [<a title="${ForDeleteComponent}"  href="javascript:deleteType(${componentTypesList.type_id});">${delete}</a>]                                  </c:if>                                
                                 </td>
                              </tr>
                            </logic:iterate>
                          </logic:notEmpty>
                          <!-- end page logic -->
                      </table></td>
                    </tr>
                </table></td>
              </tr>
              <tr>
                <td height="20" bgcolor="#d7eafd" class="box-title" align="center"><B>
                    <c:set var="translation">
                      <digi:trn key="btn:componentTypeManagerAddType">Add Type</digi:trn>
                    </c:set>
                    <html:button styleClass="dr-menu" value="${translation}" property="submitButton"  onclick="addType()">
                      <digi:trn key="aim:clickToAddComponent">Click here to add a type</digi:trn>
                    </html:button></td>
              </tr>
            </table></td>

<td noWrap vAlign="top">
  <table align=center cellPadding=0 cellSpacing=0 width="90%" border=0>
    <tr>
      <td>
        <!-- Other Links -->
        <table cellPadding=0 cellSpacing=0 width=100>
          <tr>
            <td bgColor=#c9c9c7 class=box-title>
              <digi:trn key="aim:otherLinks">
              Other links
              </digi:trn>
            </td>
            <td background="module/aim/images/corner-r.gif" height="17" width=17>&nbsp;
            
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td bgColor=#ffffff class=box-border>
        <table cellPadding=5 cellSpacing=1 width="100%">
          <tr>
            <td nowrap="nowrap">
              <digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
              <c:set var="ToViewAdmin">
                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
              </c:set>

              <digi:link href="/admin.do" title="${ToViewAdmin}" >
                <digi:trn key="aim:AmpAdminHome">
                Admin Home                </digi:trn>
              </digi:link>            </td>
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
                </tr></digi:form>

