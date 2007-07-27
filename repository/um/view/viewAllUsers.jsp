<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="umViewAllUsersForm" />
<digi:context name="digiContext" property="context" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<digi:form action="/viewAllUsers.do" method="post">
  <table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=757>
    <tr>
      <td class=r-dotted-lg width=14>&nbsp;</td>
      <td align=left class=r-dotted-lg vAlign=top>
        <table cellPadding=5 cellSpacing=0 width="100%">
          <tr>
            <!-- Start Navigation -->
            <td height=33>
              <span class=crumb>
                <c:set var="translation">
                  <digi:trn key="um:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
                </c:set>
                <digi:link module="aim" href="/admin.do" styleClass="comment" title="${translation}" >
                  <digi:trn key="um:AmpAdminHome">
                  Admin Home
                  </digi:trn>
                </digi:link>&nbsp;&gt;&nbsp;

                <digi:trn key="um:users">
                Users
                </digi:trn>
              </span>
            </td>
            <!-- End navigation -->
          </tr>
          <tr>
            <td>
              <span class=subtitle-blue>
                <digi:trn key="um:viewAllUsers:ListOfUsers">
                List of users
                </digi:trn>
              </span>
            </td>
          </tr>
          <tr style="width:50%;">
            <td height=16 vAlign="center">
            <digi:trn key="um:viewAllUsers:filter">
            Filter by:
            </digi:trn>
            <html:select property="type" style="font-family:verdana;font-size:11px;">
                <c:set var="translation">
                  <digi:trn key="um:viewAllUsers:all">
                  -All-
                  </digi:trn>
                </c:set>
                <html:option value="-1">${translation}</html:option>

                <c:set var="translation">
                  <digi:trn key="um:viewAllUsers:registred">
                  Registred
                  </digi:trn>
                </c:set>
                <html:option value="0">${translation}</html:option>

                <c:set var="translation">
                  <digi:trn key="um:viewAllUsers:teamMembers">
                  Team members
                  </digi:trn>
                </c:set>
                <html:option value="1">${translation}</html:option>
            </html:select>

            <digi:trn key="um:viewAllUsers:keyword">
            keyword:
            </digi:trn>
            <html:text property="keyword" style="font-family:verdana;font-size:11px;"/>

            <c:set var="translation">
              <digi:trn key="um:viewAllUsers:showButton">
              Show
              </digi:trn>
            </c:set>
            <input type="submit" value="${translation}" style="font-family:verdana;font-size:11px;" />
            </td>
          </tr>
          <tr>
            <td noWrap width=100% vAlign="top" colspan="3">
              <table width="100%" cellspacing=1 cellSpacing=1>
                <tr>
                  <td noWrap width=600 vAlign="top">
                    <table  bgcolor="#d7eafd" cellPadding=0 cellSpacing=1 width="100%" border=0>
                      <tr bgColor=#ffffff>
                        <td vAlign="top" width="100%">
                          <table width="100%" cellspacing=1 cellpadding=1 valign=top align=left>
                            <tr>
                              <td bgColor=#d7eafd class=box-title height="20" align="center">
                                <!-- Table title -->
                                <digi:trn key="um:users">Users</digi:trn>

                                <!-- end table title -->
                              </td>
                            </tr>
                            <tr>
                              <td>
                                <table width="100%" cellspacing=1 cellpadding=4 valign=top align=left bgcolor="#d7eafd">
                                  <tr bgcolor="#ffffff">
                                    <td>
                                      <table bgcolor="#ffffff">
                                        <c:if test="${empty umViewAllUsersForm.users}">
                                          <tr bgcolor="#ffffff">
                                            <td colspan="3" align="center">
                                              <b><digi:trn key="um:viewAllUsers:NoUsers">No users present</digi:trn></b>
                                            </td>
                                          </tr>
                                        </c:if>
                                        <c:if test="${!empty umViewAllUsersForm.users}">
                                          <tr>
                                            <td>
                                              <span style="font-size:13px;">
                                                <b><digi:trn key="um:viewAllUsers:UsersNames">Name</digi:trn></b>
                                              </span>
                                            </td>
                                            <td>
                                              <span style="font-size:13px;">
                                                <b><digi:trn key="um:viewAllUsers:UsersEmails">Email</digi:trn></b>
                                              </span>
                                            </td>
                                            <td>

                                            </td>
                                          </tr>
                                          <tr>
                                            <td>
                                            &nbsp;
                                            </td>
                                          </tr>
                                          <c:forEach var="us" items="${umViewAllUsersForm.users}">
                                            <tr>
                                              <td>
                                              ${us.firstNames}&nbsp;${us.lastName}
                                              </td>
                                              <td>
                                              ${us.email}
                                              </td>
                                              <td>
                                                <c:set var="translation">
                                                  <digi:trn key="um:viewAllUsers:EditUserLink">Edit user</digi:trn>
                                                </c:set>
                                                <digi:link href="/viewEditUser.do?id=${us.id}">${translation}</digi:link>
                                              </td>
                                            </tr>
                                          </c:forEach>
                                        </c:if>
                                      </table>
                                    </td>
                                  </tr>
                                  <!-- end page logic -->
                                </table>
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                    </table>
                  </td>
                  <td noWrap width=100% vAlign="top">
                    <table align=center cellPadding=0 cellSpacing=0 width="90%" border=0>
                      <tr>
                        <td>
                          <!-- Other Links -->
                          <table cellPadding=0 cellSpacing=0 width=100>
                            <tr>
                              <td bgColor=#c9c9c7 class=box-title>
                                <digi:trn key="um:otherLinks">
                                Other links
                                </digi:trn>
                              </td>
                              <td background="module/aim/images/corner-r.gif" height="17" width=17>
                              &nbsp;
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <tr>
                        <td bgColor=#ffffff class=box-border>
                          <table cellPadding=5 cellSpacing=1 width="100%">
                            <tr>
                              <td>
                                <digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
                                <c:set var="translation">
                                  <digi:trn key="um:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
                                </c:set>
                                <digi:link module="aim" href="/admin.do" title="${translation}" >
                                  <digi:trn key="um:AmpAdminHome">Admin Home</digi:trn>
                                </digi:link>
                              </td>
                            </tr>
                            <!-- end of other links -->
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
</digi:form>

