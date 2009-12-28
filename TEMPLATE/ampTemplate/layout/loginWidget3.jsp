<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
	<div style="margin: 5px 5px 5px 5px" >
	 <digi:secure authenticated="true">
	 <logic:present name="currentMember" scope="session">
              <c:set var="translation">
                <digi:trn key="aim:workspacename">Workspace Name</digi:trn>
              </c:set>
              <span title="${translation}">
                <bean:define id="teamMember" name="currentMember" scope="session" type="org.digijava.module.aim.helper.TeamMember" />
                	<strong style="color:#FFFFFF">${teamMember.teamName}</strong>	
              </span>
              <c:set var="translation">
                <digi:trn key="aim:clickToViewMemberDetails">Click here to view Member Details</digi:trn>
              </c:set>
              
              <span title="${translation}">
                <a href="javascript:showUserProfile('${teamMember.email}')" class="header_text">
                	${teamMember.memberName}
                </a>
              </span>
          </logic:present> 
         </digi:secure> 
         <digi:secure authenticated="false">
          <logic:notPresent name="currentMember" scope="session">
          <div style="background-color:#05528B; border: white 1px solid">
           <form action="/j_acegi_security_check" method="post"">
            
              <table width="100%"  cellspacing="0" cellpadding="0" style=" margin: 5px 5px 5px 5px">
                <tr>
                  <td align="center" vAlign="middle" style="color: white" width="15%">
                    <digi:trn >	Username</digi:trn>
                  </td>
                  <td  width="25%">
                    <input type="text" name="j_username" size="20" class="inp-text"/>
                  </td>
                  <td align="center" vAlign="middle" style="color: white"  width="15%">
                    <digi:trn >	Password</digi:trn>
                  </td>
                  <td  width="25%">
                    <input type="password" name="j_password" size="20" class="inp-text" />
                  </td>
                  <td align="center">
                    <html:submit  styleClass="dr-menu" property="submitButton"><digi:trn key="btn:signIn">Sign In</digi:trn></html:submit>
                  </td>
                </tr>
              </table>
              </form>
               <div style="margin: 0px 5px 0px; 0px;">
                 <%					
					String logoutMessage = (String)session.getAttribute("showLogoutMessage");
					if(logoutMessage != null && !logoutMessage.equals("")){
					%>
					<div align=center class=f-names noWrap style="color: white;">
                    <digi:trn key="aim:ampLogout">
                   		You have been logged out
                    </digi:trn>
                   </div>
                    <%
						session.removeAttribute("showLogoutMessage");
					}
					%>
					
					
						<digi:errors/>
						<c:if test="${param['loginError'] != null}">
						  <bean:message key="errors.header" />
						  <bean:message key="errors.prefix" />

                                                  <c:if test="${param['loginError'] == 'invalidLogin'}">
                                                     <c:set var="errorDisplayed">true</c:set>
           					     <bean:message key="error.aim.invalidLogin" />
                                                  </c:if>

                                                  <c:if test="${param['loginError'] == 'userBanned'}">
                                                     <c:set var="errorDisplayed">true</c:set>
           					     <bean:message key="error.aim.userBanned" />
                                                  </c:if>


                                                  <c:if test="${param['loginError'] == 'noTeamMember'}">
                                                     <c:set var="errorDisplayed">true</c:set>
                                                    
                                                     <digi:trn key="error.aim.userinvalidteammembernoworkplace">You can not login into AMP because you are not assigned to a workspace</digi:trn>
           					     				</c:if>
                                                  
                                                  <c:if test="${param['loginError'] == 'invalidUser'}">
                                                     <c:set var="errorDisplayed">true</c:set>
           					     <bean:message key="error.aim.userInvalid" />
                                                  </c:if>

                                                  <c:if test="${errorDisplayed == null}">
           					     <bean:message key="error.aim.loginFailed" />
                                                  </c:if>

						  <bean:message key="errors.suffix" />
						  <bean:message key="errors.footer" />
						</c:if>
					
					
					
					
                 <c:set var="trn1">
                      <digi:trn key="aim:clickifyouForgotYourPassword">Click here if you have forgot your password</digi:trn>
                    </c:set>
                    <digi:link href="/showEmailForm.do" module="aim" title="${trn1}" style="color:white; ">
                      <digi:trn key="aim:forgotPassword">
                      Forgot Password
                      </digi:trn>
                    </digi:link>
                    |
                    <c:set var="trn2">
                      <digi:trn key="aim:clickToChangeYourPassword">Click here if you want to change your password</digi:trn>
                    </c:set>
                    <digi:link href="/showChangePassword.do" module="aim" title="${trn2}" style="color: white; ">
                      <digi:trn key="aim:changePassword">
                      Change Password
                      </digi:trn>
                    </digi:link>
              </div>
              
          </logic:notPresent>
          </digi:secure>
       </div>
