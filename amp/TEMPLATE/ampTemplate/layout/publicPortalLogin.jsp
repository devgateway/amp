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
<style>
	#loginbox{width:390px;height:262px;margin:0 auto;background:url(/TEMPLATE/ampTemplate/img_2/loginbox.jpg) no-repeat top left;margin-top:50px;padding:0px 0px 0px 0px;}
	#loginbox form{width:352px;margin:10px 20px 10px 18px;}
	#loginbox p.userfield{display:block;width:352px;height:37px;background:url(/TEMPLATE/ampTemplate/img_2/login.gif) no-repeat top left;margin:0px 0px 15px 0px;}
	#loginbox p.passfield{display:block;width:352px;height:37px;background:url(/TEMPLATE/ampTemplate/img_2/pass.gif) no-repeat top left;margin:0px 0px 15px 0px;}
	#loginbox p.userfield input, #loginbox p.passfield input{float: right; width:230px;margin:3px 5px 0px 0px; height:29px;background:none;border:none;}
	label{font-size:12px;font-weight:bold;color:#fff;display:block;float:left;line-height:33px;margin:0px 0px 0px 35px;}
	input#amplogin{width:61px;height:32px;background:url(/TEMPLATE/ampTemplate/img_2/submit.gif);line-height:30px;color:#fff;font-size:12px;font-weight:bold;float:left;margin:0px 0px 20px 0px;}
	.logintitle{background:url(/TEMPLATE/ampTemplate/img_2/ttlbg.gif) repeat-x bottom;width:352px;margin:0 auto;margin-bottom:25px;padding-top:15px;	}
	.logintitle h2{padding:5px;display:inline;background:#e6eaed;margin:15px 0px 0px 0px;}
	#loginbox h2{color:#3c5260;font-size:20px;}
	.clear{clear:both;}
	a{font-size:11px;}
</style>
<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>
<digi:ref href="/TEMPLATE/ampTemplate/css_2/amp.css" type="text/css" rel="stylesheet" />
<script type="text/javascript">
var loginFailed = function(data, status) {
    $(".error").remove();
    $('#result').before('<div class="error">Login failed, please try again.</div>');
};


function ajaxLogin() {
$(".error_text_login").remove();
$('#result').before("<div class='error_text_login'><img src='/TEMPLATE/ampTemplate/img_2/ajax-loader.gif' style='vertical-align:middle;'></div>");


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
								"<div class='error_text_login'><digi:trn>Invalid username or password</digi:trn>.</div>");
				break;
			case 'userBanned':
				$('#result')
						.before(
								"<div class='error_text_login'><digi:trn>User is banned</digi:trn>.</div>");
				break;
			case 'noTeamMember':
				$('#result')
						.before(
								"<div class='error_text_login'><digi:trn>You can not login into AMP because you are not assigned to a workspace</digi:trn>.</div>");
				break;
			case 'invalidUser':
				$('#result')
						.before(
								"<div class='error_text_login'><digi:trn>Invalid User</digi:trn>.</div>");
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
					<div id="login_menu"><a href="/publicPortal"><digi:trn>Access AMP as Public User</digi:trn>&nbsp;&raquo;</a></div>
				</div>
				<div class="logo">
					<table width="480" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="50" style="vertical-align: middle;"><a href="/"
								style="text-decoration: none"> <img
									src="/aim/default/displayFlag.do" border="0" width="50px"
									height="34px"><br /></a></td>
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
        	<form action="/j_spring_security_check" id="loginForm" method="post" onsubmit="ajaxLogin();return false;">
            	<p class="userfield">
                	<label><digi:trn>Username</digi:trn>:</label>
                	<input name="j_username" type="text" id="user">
                </p>
                <p class="passfield">
                    <label><digi:trn>password</digi:trn>:</label>
                	<input name="j_password" type="password" id="pass">
                </p>
                <input type="submit" id="amplogin" value='<digi:trn>Login</digi:trn>'/>
            </form>
            <div id="result">
				<div class="error_text_login"></div>
			</div>
            
        <div>
     <div class="clear"></div>
       </div></div>
       <div style="width:390px;margin: 50px auto 0;">
					<feature:display name="Enable New User Registration" module="Login - User Management">
					<c:set var="trn3">
						<digi:trn key="aim:clickforNewUserRegistration">Click here for new user registration</digi:trn>
					</c:set>
					<digi:link href="/showRegisterUser.do?init=true" module="aim" title="${trn3}">
			    		<digi:trn key="aim:newUserRegistration"> New user registration</digi:trn>
			  		</digi:link>	
				 	&nbsp;|&nbsp; 
				 	</feature:display>
					<c:set var="title">
						<digi:trn>Click here to change your password</digi:trn>
					</c:set>
	  				<digi:link href="/showChangePassword.do" title="${title}" module="aim">
	    				<digi:trn> Change Password</digi:trn>
	  				</digi:link>	 
	  				&nbsp;|&nbsp; 
					<c:set var="trn1">
                		<digi:trn key="aim:clickifyouForgotYourPassword">Click here if you have problem with login in</digi:trn>
                	</c:set>
                	<digi:link href="/showEmailForm.do" module="aim" title="${trn1}">
                   		<digi:trn key="aim:forgotPassword">Trouble signing in?</digi:trn>
					</digi:link>
		</div>
		<digi:insert attribute="footer"/>
    </div>	
</body>
</html>
