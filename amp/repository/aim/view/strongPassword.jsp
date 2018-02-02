<%@ page import="org.digijava.kernel.security.PasswordPolicyValidator" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/globalsettings" prefix="globalsettings"%>

<script type="text/javascript"
		src="/TEMPLATE/ampTemplate/js_2/pwstrength/pwstrength-bootstrap.min.js"></script>

<script language="JavaScript">
    var STRONG_PASSWORD	= <globalsettings:value name="Strong password" />;

    var translations = {
        "veryWeak": "<digi:trn key="pwstrength:veryWeak">Very Weak</digi:trn>",
        "weak": "<digi:trn key="pwstrength:weak">Weak</digi:trn>",
        "normal": "<digi:trn key="pwstrength:normal">Normal</digi:trn>",
        "medium": "<digi:trn key="pwstrength:medium">Medium</digi:trn>",
        "strong": "<digi:trn key="pwstrength:strong">Strong</digi:trn>",
        "veryStrong": "<digi:trn key="pwstrength:veryStrong">Very Strong</digi:trn>"
    };

	if (STRONG_PASSWORD) {
        $(document).ready(function () {
            "use strict";
            var options = {};
            options.ui = {
                showVerdicts: true,
            	container: "#pwd_container",
                usernameField: "#pwd_username",
                showStatus: true,
                viewports: {
                    progress: ".pwstrength_viewport_progress",
                    verdict: ".pwstrength_viewport_verdict"
                },
                progressBarExtraCssClasses: "amp_progress_bar"
            };
            options.common = {
                minChar: <%=PasswordPolicyValidator.MIN_LENGTH%>
            };
            options.i18n = {
                t: function (key) {
                    var result = translations[key];
                    return result === key ? '' : result;
                }
            };
            options.rules = {
                activated: {
                    wordNotEmail: true,
                    wordLength: true,
                    wordSimilarToUsername: true,
                    wordSequences: false,
                    wordTwoCharacterClasses: false,
                    wordRepetitions: false,
                    wordLowercase: true,
                    wordUppercase: true,
                    wordOneNumber: true,
                    wordThreeNumbers: false,
                    wordOneSpecialChar: false,
                    wordTwoSpecialChar: false,
                    wordUpperLowerCombo: false,
                    wordLetterNumberCombo: false,
                    wordLetterNumberCharCombo: false
                }
            };
            $( ".pwd_container" ).show();
            $("input[name='password']").pwstrength(options);
            $("input[name='newPassword']").pwstrength(options);

        });
    }
	</script>

<style type="text/css">
	<!--
	.pwd_container {
		margin-top: 10px;
	}
	.amp_progress_bar {

	}
	.progress{
		margin-top: 5px;
		height: 5px !important;
		width: 144px !important;
		margin-bottom: 3px; !important;
	}
	.psw_error_msg{
		margin-left: 10px;
		color: #FF0000;
	}
	-->
</style>


