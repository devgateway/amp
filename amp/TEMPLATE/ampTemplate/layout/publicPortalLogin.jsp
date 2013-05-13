<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Home</title>
<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>
<digi:ref href="/TEMPLATE/ampTemplate/css_2/amp_public_portal_login.css" type="text/css" rel="stylesheet" />
<script type="text/javascript">
var loginFailed = function(data, status) {
    $(".error").remove();
    $('#result').before('<div class="error">Login failed, please try again.</div>');
};


function ajaxLogin() {
$(".error").remove();
$('#result').before("<div class='error'><img src='/TEMPLATE/ampTemplate/img_2/ajax-loader.gif' style='vertical-align:middle;'></div>");


$.ajax({
		url : "/j_spring_security_check",
		type : "POST",
		data : $("#loginForm").serialize(),
		success : function(data) {
			var error = jQuery.trim(data);
			$(".error").remove();
			switch (error) {
			case 'invalidLogin':
				$('#result')
						.before(
								"<div class='error'><digi:trn>Invalid username or password</digi:trn>.</div>");
				break;
			case 'userBanned':
				$('#result')
						.before(
								"<div class='error'><digi:trn>User is banned</digi:trn>.</div>");
				break;
			case 'noTeamMember':
				$('#result')
						.before(
								"<div class='error'><digi:trn>You can not login into AMP because you are not assigned to a workspace</digi:trn>.</div>");
				break;
			case 'invalidUser':
				$('#result')
						.before(
								"<div class='error'><digi:trn>Invalid User</digi:trn>.</div>");
				break;
			case 'noError':
				location.href = '/index.do';
				break;
			}
		}
	});
}
</script>
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

	<div id="container">
	<center>
			<div class="header">
				<div class="login_nav">
					<div id="toplnk"><a href="/publicPortal"><digi:trn>Access AMP as Public User</digi:trn>&nbsp;&raquo;</a></div>
				</div>
				<div class="logo">
					<table width="480" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="50" style="vertical-align: middle;">
								<a href="/" style="text-decoration: none"> 
									<img src="/aim/default/displayFlag.do" border="0" width="50px" height="34px"><br />
								</a>
							</td>
							<td style="padding-left: 10px;text-align: left;"><a href="/"
								style="text-decoration: none">
									<div class="amp_label" style="padding-top: 0px;">
										<digi:trn key="aim:aidManagementPlatform">Aid Management Platform (AMP)</digi:trn>
									</div>
							</a></td>
						</tr>
					</table>
				</div>
			</div>
		</center>
          <div class="clear"></div>
		 
        <div id="loginbox">  
        	<div class="logintitle"><h2><digi:trn>Member Login</digi:trn></h2></div>
        	<form action="/j_spring_security_check" id="loginForm" method="post"
				onsubmit="ajaxLogin();return false;">
            	<p class="userfield">
					<label><digi:trn>Username</digi:trn>:</label> <input
						name="j_username" type="text" id="user"/>
						
				</p>
				<p class="passfield">
					<label><digi:trn>Password</digi:trn>:</label> <input name="j_password" type="password" id="pass">
				</p>
				<div id="result"></div>
                <input type="submit" id="amplogin" value="Login"/>
				<div class="utils">
				<c:set var="trn1">
                		<digi:trn key="aim:clickifyouForgotYourPassword">Click here if you have problem with login in</digi:trn>
                </c:set>
                <c:set var="title">
						<digi:trn>Click here to change your password</digi:trn>
					</c:set>
				<%-- <digi:link href="/showChangePassword.do"  styleId="changePassword" 
					title="${title}">&raquo;&nbsp;<digi:trn>Change Password</digi:trn></digi:link> --%>
				<a href="/aim/showChangePassword.do" id="changePassword" title="${title}">&raquo;&nbsp;<digi:trn>Change Password</digi:trn></a>
				<digi:link href="/showEmailForm.do" styleId="trouble" title="${trn1}">&raquo;&nbsp;<digi:trn key="aim:forgotPassword">Trouble signing in?</digi:trn></digi:link>
				</div>
				<feature:display name="Enable New User Registration"
							module="Login - User Management">
				<div class="addnew">
				<c:set var="trn3">
						<digi:trn key="aim:clickforNewUserRegistration">Click here for new user registration</digi:trn>
					</c:set>
				<digi:link href="/showRegisterUser.do?init=true" styleId="new" title="${trn3}"><digi:trn key="aim:newUserRegistration"> New user registration</digi:trn></digi:link></div>
				</feature:display>
				</form>
            
        <div>

     <div id="footer"></div>
       </div></div>
      
    </div>
	<digi:insert attribute="footer"/>
	
</body>
</html>
