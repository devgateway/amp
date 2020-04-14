<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Home</title>
<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>
<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery.class.min.js"/>" ></script>
<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jdigestauth/md5-min.js"/>" ></script>
<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jdigestauth/digest-auth.js"/>" ></script>
<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jdigestauth/sha1.js"/>" ></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/node_modules/amp-boilerplate/dist/amp-boilerplate.js"></script>
<link rel="stylesheet" href="/TEMPLATE/ampTemplate/tabs/css/bootstrap.css">
<digi:ref href="/TEMPLATE/ampTemplate/css_2/amp_public_portal_login.css" type="text/css" rel="stylesheet" />
<script type="text/javascript">
var loginFailed = function(data, status) {
    $(".error").remove();
    $('#result').before('<div class="error">Login failed, please try again.</div>');
};


</script>
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

	<div id="container">
	<center>
			<div class="header">
				<div class="login_nav">
					<div id="toplnk">
                        <c:if test="${empty fn:trim(publicPortalUrl)}">
                            <c:url var="publicPortalUrl" value="/"/>
                        </c:if>
                        <a href="${publicPortalUrl}"><digi:trn>Access AMP as Public User</digi:trn>&nbsp;&raquo;</a></div>
				</div>
				<div class="logo">
					<table width="480" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="50" style="vertical-align: middle;">
								<a href="/" style="text-decoration: none">
									<img src="/aim/default/displayFlag.do" border="0" width="30px" height="20px"><br />
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
        	<form action="/aim/postLogin.do" id="loginForm" method="post"
				onsubmit="ajaxLogin();return false;">
            	<p class="userfield">
					<label><digi:trn>Username</digi:trn>:</label> <input
						name="j_username" type="text" id="j_username"/>

				</p>
				<p class="passfield">
					<label><digi:trn>Password</digi:trn>:</label> <input name="j_password" type="password" id="j_password">
				</p>
				<div id="result" style="display:none;">
					<div id="loader" style="display:none;">
						<img src='/TEMPLATE/ampTemplate/img_2/ajax-loader.gif'>
					</div>
					<div class="error_text_login">
						<img src='/TEMPLATE/ampTemplate/img_2/login_error.gif'> <span
							id="invalid_user_pwd"><digi:trn>Invalid username or password</digi:trn>.</span>
							<span id="unassigned_user"><digi:trn>You can not login into AMP because you are not assigned to a workspace</digi:trn>.</span>
							<span id="banned_user"><digi:trn>Your username has been banned. Please contact the AMP administrator</digi:trn>.</span>
							<span id="invalid_user"><digi:trn>Invalid User</digi:trn>.</span>
							<span id="suspend"></span>
					</div>
				</div>
				<input type="submit" id="amplogin" value="Login"/>
				<div class="utils">
				<c:set var="trn1">
                		<digi:trn key="aim:clickifyouForgotYourPassword">Click here if you have problem with login in</digi:trn>
                </c:set>
                <c:set var="title">
						<digi:trn>Click here to change your password</digi:trn>
					</c:set>
				<a href="/aim/showChangePassword.do" id="changePassword" title="${title}">&raquo;&nbsp;<digi:trn>Change Password</digi:trn></a>
				<digi:link href="/showEmailForm.do" module="aim" styleId="trouble" title="${trn1}">&raquo;&nbsp;<digi:trn key="aim:forgotPassword">Trouble signing in?</digi:trn></digi:link>
				</div>
				<feature:display name="Enable New User Registration"
							module="Login - User Management">
				<div class="addnew">
				<c:set var="trn3">
						<digi:trn key="aim:clickforNewUserRegistration">Click here for new user registration</digi:trn>
					</c:set>
				<digi:link module="aim" href="/showRegisterUser.do?init=true" styleId="new" title="${trn3}"><digi:trn key="aim:newUserRegistration"> New user registration</digi:trn></digi:link></div>
				</feature:display>
				</form>
          <form action="/aim/index.do" id="selectWorkspaceForm" method="post">
	          	<input type="hidden" id="generateToken" name="generateToken"/>
	          	<input type="hidden" id="callbackUrl" name="callbackUrl"/>
		</form>
        <div>

     <div id="footer"></div>
       </div>

	</div>
    </div>
	<jsp:include page="broswerDetection.jsp" />

	<digi:insert attribute="footer"/>
</body>
</html>
