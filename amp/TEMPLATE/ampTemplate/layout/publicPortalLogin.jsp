<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
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
$(".error_text_login").remove();



$.ajax({
		url : "/j_spring_security_check",
		type : "POST",
		data : $("#loginForm").serialize(),
		success : function(data) {
			var error = jQuery.trim(data);
			$(".error_text_login").remove();
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
		<div id="header">
			<h1>
			<a href="/" style="text-decoration: none;"> <img
				src="/aim/default/displayFlag.do"  border="0" width="50px"
				height="34px"/></a>
				<digi:trn>Aid Management Platform</digi:trn>
			</h1>
			<div id="toplnk">
				<a href="/publicPortal"><digi:trn>Access AMP as Public User</digi:trn>&raquo;</a>
			</div>
		</div>
		<div class="clear"></div>
		<div id="result"></div>
		<div id="loginbox">
			<div class="logintitle">
				<h2>
					<digi:trn>Member Login</digi:trn>
				</h2>
			</div>
			<form action="/j_spring_security_check" id="loginForm" method="post"
				onsubmit="ajaxLogin();return false;">
				<p class="userfield">
					<label><digi:trn>Username</digi:trn>:</label> <input
						name="j_username" type="text" id="user">
				</p>
				<p class="passfield">
					<label><digi:trn>Password</digi:trn>:</label> <input
						name="j_password" type="password" id="pass">
				</p>
				<input type="submit" id="amplogin"
					value='<digi:trn>Login</digi:trn>' /> <label id="actionslabel"><digi:trn>Use the dropdown below for additional actions</digi:trn></label>
				<select id="actions"
					onChange="if(this.selectedIndex!=0) self.location=this.options[this.selectedIndex].value">
					<option value="" selected><digi:trn>Additional user actions</digi:trn></option>
					<option value="/aim/showChangePassword.do">
						<digi:trn>Change Password</digi:trn>
					</option>
					<option value="/aim/showRegisterUser.do?init=true">
						<feature:display name="Enable New User Registration"
							module="Login - User Management">
							<digi:trn key="aim:newUserRegistration"> New user registration</digi:trn>
						</feature:display>
					</option>
					<option value="/aim/showEmailForm.do">
						<digi:trn key="aim:forgotPassword">Trouble signing in?</digi:trn>
					</option>
				</select>
			</form>

			<div>

				<div id="footer"></div>
			</div>
		</div>
	</div>

	<digi:insert attribute="footer"/>
	
</body>
</html>
