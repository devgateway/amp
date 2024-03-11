<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>


<digi:secure authenticated="false">
	<logic:notPresent name="currentMember" scope="session">


		<script type="text/javascript">
			$(document).ready(function() {
				$("div#show_login_pop_box").mouseup(function() {
					return false
				});

				// AMP-14908: IE 8/9 fix in standards mode
				if ($("form#loginForm").live !== undefined) { // This check is needed after changes for AMP-22515 (jquery versions mismatch?)
					$("form#loginForm").live("keyup", function(ev){
						if ((navigator.appName == 'Microsoft Internet Explorer') && (ev.keyCode == 13))
						{
							$("form#loginForm").submit();
						}
					});
				}


				$(document).mouseup(function(e) {
					if($(e.target).parent("a#show_login_pop").length==0) {
						$("#show_login_pop").removeClass("menu-open");
						$("div#show_login_pop_box").hide();
					}
				});

			});


			var loginFailed = function(data, status) {
				$(".error").remove();
				$('#result').before('<div class="error">Login failed, please try again.</div>');
			};


		</script>
		<div id="show_login_pop_box">
			<form action="/aim/postLogin.do" id="loginForm" method="post" onsubmit="ajaxLogin();return false;">
				<label for="j_username">
					<c:set var="usernameText"><digi:trn>Username</digi:trn></c:set>
					<c:out escapeXml="true" value="${usernameText}"/>:
				</label>

				<input name="j_username" type="text" class="inputx" id="j_username">

				<label for="j_password">
				    <c:set var="passwordText"><digi:trn>Password</digi:trn></c:set>
					<c:out escapeXml="true" value="${passwordText}"/>:
				</label>

				<input name="j_password" type="password" class="inputx" id="j_password">
				<div class="clear">
				    <input type="submit"  class="buttonx_sm_lgn"  onclick="ajaxLogin();return false" value="<digi:trn>Login</digi:trn>">
				</div>

			</form>
			<div id="result" style="display:none;">
				<div id="loader" style="display:none;">
					<img src='/TEMPLATE/ampTemplate/img_2/ajax-loader.gif'>
				</div>
				<div class="error_text_login">
					<img src='/TEMPLATE/ampTemplate/img_2/login_error.gif'>
					<span id="invalid_user_pwd"><digi:trn>Invalid username or password</digi:trn>.</span>
					<span id="unassigned_user"><digi:trn>You can not login into AMP because you are not assigned to a workspace</digi:trn>.</span>
					<span id="banned_user"><digi:trn>Your username has been banned. Please contact the AMP administrator</digi:trn>.</span>
					<span id="invalid_user"><digi:trn>Invalid User</digi:trn>.</span>
					<span id="suspend"></span>
				</div>
			</div>
		</div>
		<div id="logincontainer">
      <feature:display name="Enable New User Registration" module="Login - User Management">
        <c:set var="trn3">
          <digi:trn key="aim:clickforNewUserRegistration">Click here for new user registration</digi:trn>
        </c:set>
        <digi:link href="/showRegisterUser.do?init=true" module="aim" title="${trn3}">
          <digi:trn key="aim:newUserRegistration"> New user registration</digi:trn>
        </digi:link>
        <br/>
      </feature:display>
      <feature:display name="Change Password" module="Login - User Management">
        <c:set var="title">
          <digi:trn>Click here to change your password</digi:trn>
        </c:set>
        <digi:link href="/showChangePassword.do" title="${title}" module="aim">
          <digi:trn> Change Password</digi:trn>
        </digi:link>
        <br/>
      </feature:display>
      <feature:display name="Trouble signing in" module="Login - User Management">
        <c:set var="trn1">
          <digi:trn key="aim:clickifyouForgotYourPassword">Click here if you have problem with login in</digi:trn>
        </c:set>
        <digi:link href="/showEmailForm.do" module="aim" title="${trn1}">
          <digi:trn key="aim:forgotPassword">Trouble signing in?</digi:trn>
        </digi:link>
      </feature:display>
		</div>
		<form action="/aim/index.do" id="selectWorkspaceForm" method="post">
		</form>

	</logic:notPresent>
</digi:secure>
<digi:secure authenticated="true">
	<script type="text/javascript">
		document.getElementsByClassName ('login_nav')[0].style.zIndex = -1;
	</script>
</digi:secure>
