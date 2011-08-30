<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<style>
  .inp-upastext {
    color: grey;
  }
  
  .inp-username {color: grey}
</style>
<script type="text/javascript" src="script/jquery.js"></script>
<script type="text/javascript">

	var usrFlag = false;
	var passFlag = false;
	
  $(document).ready(function(){
	 
//	  $(".inp-username").attr({disabled: ""});
//	  $(".inp-upastext").attr({disabled: ""});
//	  $(".inp-upassword").attr({disabled: ""});

//Init
		var uname = $(".inp-username").attr("value");
		var pass = $(".inp-upassword").attr("value");

		
		if (uname=="") {
			$(".inp-username").attr({value: "Username"});
			$(".inp-username").css({color: "grey"});
		} else if (uname!="Username") {
			$(".inp-username").css({color: "black"});
		}
		
		if (pass=="") {
			$(".inp-upassword").attr({value: "Password"});
			$(".inp-upassword").css({color: "grey"});
		
		} else if (pass!="Password") {
			$(".inp-upassword").css({color: "black"});
		}


	  $(".inp-username").bind("blur", function(){
		var uname = $(".inp-username").attr("value");
		var username = "<digi:trn jsFriendly='true'>Username</digi:trn>"
		if (uname=="") {
			$(".inp-username").attr({value: username});
			$(".inp-username").css({color: "grey"});
		} else if (uname!="Username") {
			$(".inp-username").css({color: "black"});
		}
	});

	  $(".inp-username").keyup(function(){
		  	var uname = $(".inp-username").attr("value");
			//alert(uname.length);
			if (uname.length > 0) {
				usrFlag = true;
			} else {
				usrFlag = false;
			}
		});
			
	
	$(".inp-username").click(function(){
		var uname = $(".inp-username").attr("value");
		if (usrFlag == false) {
		  $(".inp-username").attr({value: ""});
		  $(".inp-username").css({color: "black"});
		}
	});

	$(".inp-upassword").keyup(function(){
	  	var upass = $(".inp-upassword").attr("value");
		//alert(uname.length);
		if (upass.length > 0) {
			passFlag = true;
		} else {
			passFlag = false;
		}
	});
	$(".inp-upassword").bind("blur", function(){
		var upass = $(".inp-upassword").attr("value");
		var password = "<digi:trn jsFriendly='true'>Password</digi:trn>"
		if (upass=="") {
			//$(".inp-upastext").attr({value: password});
			//$(".inp-upastext").css({color: "grey"});
			//$(".inp-upastext").css({display: ""});
			//$(".inp-upassword").css({display: "none"});
			$(".inp-upassword").attr({value: password});
			$(".inp-upassword").css({color: "grey"});
		}
	});

	$(".inp-upassword").click(function(){
		pass = $(".inp-upassword").attr("value");
		if (pass=="Password") {
			$(".inp-upassword").attr({value: ""});
		}
		$(".inp-upassword").css({color: "black"});
	});
	
	$(".inp-upassword").blur(function(){
		pass = $(".inp-upassword").attr("value");
		if (pass=="" || pass=="Password") {
			$(".inp-upassword").attr({value: "Password"});
			$(".inp-upassword").css({color: "grey"});
		
		} else {
			$(".inp-upassword").css({color: "black"});
		}		
		
	});

	$(".inp-upassword").focus(function(){
		pass = $(".inp-upassword").attr("value");
		if (pass=="Password") {
			$(".inp-upassword").attr({value: ""});
		}
		$(".inp-upassword").css({color: "black"});
	});
	
});
</script>
	<div style="margin: 15px 10px 5px 5px" >
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
                <a href="javascript:showUserProfile('${teamMember.memberId}')" class="header_text">
                	${teamMember.memberName}
                </a>
              </span>
          </logic:present> 
         </digi:secure> 
         <digi:secure authenticated="false">
          <logic:notPresent name="currentMember" scope="session">
          <div style="background-color:#376091; border: #9d9da7 1px solid; width: 400px;height:45px;">
           <form action="/j_acegi_security_check" method="post" style="margin:0;">
            <%-- 
              <table width="100%"  cellspacing="0" cellpadding="0" border="0" style=" margin-top: 10px">
                <tr>
                  <td  align="center">
                    <input type="text" name="j_username" size="20" class="inp-text"/>
                  </td>

                  <td align="center">
                    <input type="password" name="j_password" size="20" class="inp-text" />
                  </td>
                  <td align="center">
                    <html:submit  styleClass="dr-menu" property="submitButton"><digi:trn key="btn:signIn">Login</digi:trn></html:submit>
                  </td>
                </tr>
              </table>
              --%>
				<table width="100%" cellspacing="0" cellpadding="0" style="margin-top: 8px;">
					<tr>
					  <td align="center" valign="middle"><input type="text" name="j_username" style="width: 150px;" class="inp-text inp-username" value="<digi:trn>Username</digi:trn>"/></td>
					  <td align="center" valign="middle">
					  	<input type="password" name="j_password" class="inp-text inp-upassword" style="width: 150px;"/>
					  	<!--
					  	<input type="text" name="j_username" class="inp-text inp-upastext" style="width: 150px; -moz-box-sizing:border-box" value="<digi:trn>Password</digi:trn>" disabled="disabled"/>
					  	-->
					  </td>
					  <td><html:submit  styleClass="dr-menu" property="submitButton"><digi:trn key="btn:signIn">Login</digi:trn></html:submit></td>
					</tr>
				</table>
              
              </form>
               <div style="margin: 0px 5px 0px; 0px;">
               <%-- 
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
					--%>
					

						<c:if test="${param['loginError'] != null}">
						<div style="padding:5px 5px 5px 5px;text-align:left;font-weight:bold;">
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
								<font color="red">
									<digi:trn key="error.aim.userinvalidteammembernoworkplace">You can not login. Not assigned to any workspace</digi:trn>
								</font>	
							</c:if>
							<c:if test="${param['loginError'] == 'invalidUser'}">
								<c:set var="errorDisplayed">true</c:set>
								<bean:message key="error.aim.userInvalid" />
							</c:if>
							<c:if test="${errorDisplayed == null}">
								<bean:message key="error.aim.loginFailed" />
							</c:if>
						</div>
						</c:if>
					
				 	

                    
               <%--     | #7536
                    <c:set var="trn2">
                      <digi:trn key="aim:clickToChangeYourPassword">Click here if you want to change your password</digi:trn>
                    </c:set>
                    <digi:link href="/showChangePassword.do" module="aim" title="${trn2}" style="color: white; ">
                      <digi:trn key="aim:changePassword">
                      Change Password
                      </digi:trn>
                    </digi:link>
                    --%> 
              </div>
              
          </logic:notPresent>
          </digi:secure>
       </div>
      <digi:secure authenticated="false" >
        		<module:display name="Login - User Management" parentModule="PUBLIC VIEW">
        			<feature:display name="Enable New User Registration" module="Login - User Management">
		        		<c:set var="trn3">
							<digi:trn key="aim:clickforNewUserRegistration">Click here for new user registration</digi:trn>
						</c:set>
						  <digi:link href="/showRegisterUser.do" module="aim" title="${trn3}" style="color:white; margin-top: 3px;">
						    <digi:trn key="aim:newUserRegistration"> New user registration</digi:trn>
						  </digi:link>	
		                 &nbsp;&nbsp;<span style="color: white;">|</span>&nbsp;&nbsp;
		        	</feature:display>    	
	            </module:display>
        		<c:set var="title">
					<digi:trn>Click here to change your password</digi:trn>
				</c:set>
				  <digi:link href="/showChangePassword.do" title="${title}" module="aim" style="color:white; margin-top: 3px;">
				    <digi:trn> Change Password</digi:trn>
				  </digi:link>	
                 &nbsp;&nbsp;<span style="color: white;">|</span>&nbsp;&nbsp;
                 <c:set var="trn1">
                      <digi:trn key="aim:clickifyouForgotYourPassword">Click here if you have problem with login in</digi:trn>
                    </c:set>
                    <digi:link href="/showEmailForm.do" module="aim" title="${trn1}" style="color:white; ">
                      <digi:trn key="aim:forgotPassword">
                      Trouble signing in?
                      </digi:trn>
                    </digi:link>
      </digi:secure>
       
