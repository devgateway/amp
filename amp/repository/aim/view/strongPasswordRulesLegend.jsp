<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<c:if test="${showPasswordPolicyRules}">
	<ul class="psw_error_msg">
		<li>
			<digi:trn key="pwstrength:lengthRule">Must be greater than or equal to 8 characters</digi:trn>
		</li>
		<li>
			<digi:trn key="pwstrength:characterRuleDigit">Must include both characters (e.g. letters) and at least one number</digi:trn>
		</li>
		<li>
			<digi:trn key="pwstrength:characterRule">Must have both uppercase and lowercase characters</digi:trn>
		</li>
		<li>
			<digi:trn key="pwstrength:usernameRule">Must not be equal to username</digi:trn>
		</li>
	</ul>
</c:if>
