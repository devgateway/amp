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

            $("#show_login_pop").click(function(e) {          
				e.preventDefault();
                $("div#show_login_pop_box").toggle();
				$("#show_login_pop").toggleClass("menu-open");
				$("#j_username").focus();
            });
			
			$("div#show_login_pop_box").mouseup(function() {
				return false
			});
			
			// AMP-14908: IE 8/9 fix in standards mode
			$("form#loginForm").live("keyup", function(ev){
				if ((navigator.appName == 'Microsoft Internet Explorer') && (ev.keyCode == 13))
				{
					$("form#loginForm").submit();
				}
	        });
			
			
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
				    					<digi:trn>Username</digi:trn>:
				    				</label>
				    			
				    				<input name="j_username" type="text" class="inputx" id="j_username">
				    		
    								<label for="j_password">
    									<digi:trn>Password</digi:trn>:</label>
    							
	    							<input name="j_password" type="password" class="inputx" id="j_password">
	    							<div class="clear">
	    								<input type="submit"  class="buttonx_sm_lgn"  onclick="ajaxLogin();return false" value='<digi:trn>Login</digi:trn>'>
	    							</div>
  									
	    			</form>
			<div id="result" hidden="true">
				<div id="loader" hidden="true">
					<img src='/TEMPLATE/ampTemplate/img_2/ajax-loader.gif'>
				</div>
				<div class="error_text_login">
					<img src='/TEMPLATE/ampTemplate/img_2/login_error.gif'>
					<span id="invalid_user_pwd" hidden="true"><digi:trn>Invalid username or password</digi:trn>.</span>
					<span id="unassigned_user" hidden="true"><digi:trn>You can not login into AMP because you are not assigned to a workspace</digi:trn>.</span>
					<span id="invalid_user" hidden="true"><digi:trn>Invalid User</digi:trn>.</span>
					<span id="suspend" hidden="true"></span>
				</div>
			</div>
		</div>
<div id="logincontainer">
  					<div class="login_here">
  						<div class="login_here_cont">
  							<a id="show_login_pop" style="color:#000000; font-weight:bold;text-decoration: underline;cursor: pointer;" >
  								<digi:trn>Login Here</digi:trn>
  							</a>
  						</div>
                        
  					</div>
  					<div id="login_menu" class="header_links">
					<feature:display name="Enable New User Registration" module="Login - User Management">
					<c:set var="trn3">
						<digi:trn key="aim:clickforNewUserRegistration">Click here for new user registration</digi:trn>
					</c:set>
					<digi:link href="/showRegisterUser.do?init=true" module="aim" title="${trn3}">
			    		<digi:trn key="aim:newUserRegistration"> New user registration</digi:trn>
			  		</digi:link>	
				 	&nbsp;|&nbsp; 
				 	</feature:display>
				 	<feature:display name="Change Password" module="Login - User Management">
					<c:set var="title">
						<digi:trn>Click here to change your password</digi:trn>
					</c:set>
	  				<digi:link href="/showChangePassword.do" title="${title}" module="aim">
	    				<digi:trn> Change Password</digi:trn>
	  				</digi:link>	 
	  				&nbsp;|&nbsp;
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
					</div>
					<!--
	<c:if test="${param['loginError'] != null}">
		<div class="error_login">
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
	-->
					
</logic:notPresent>
</digi:secure>

  <!--<script type="text/javascript">
  function toggle(obj) {
		var el = document.getElementById(obj);
		if (el.style.display == 'none' || el.style.display == '') {
			el.style.display = 'block';
		}
		else {
			el.style.display = 'none';
		}
	}
</script>-->
  
