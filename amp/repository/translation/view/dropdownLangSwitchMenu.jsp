<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<c:set var="message">
	<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
</c:set>
<c:set var="quote">'</c:set>
<c:set var="escapedQuote">\'</c:set>
<c:set var="msg">
	${fn:replace(message,quote,escapedQuote)}
</c:set>

	<digi:instance property="translationForm"/>
	<logic:present name="translationForm" property="languages">
			<logic:iterate id="languages" name="translationForm" property="languages" type="org.digijava.module.translation.form.TranslationForm.TranslationInfo">
				<bean:define id="langReferUrl" name="languages" property="referUrl" type="java.lang.String"/>
                <c:choose>
                  <c:when test="${translationForm.referUrl == langReferUrl}">
                    <li class="yuiampmenuitem_drop">
                  </c:when>
                  <c:otherwise>
                    <li class="yuiampmenuitem_drop">
                  </c:otherwise>
                </c:choose>
				<a class="yuiampmenuitemlabel"  href="#" onclick="SwitchLanguageMenu('<%= langReferUrl %>')">
					<digi:trn key="aim:${languages.langName}"><bean:write name="languages" property="langName"/></digi:trn>
				</a>
                </li>
			</logic:iterate>
	</logic:present>

<script>
function
SwitchLanguageMenu(value) {
		if(typeof quitRnot1 != 'function'||quitRnot1('${msg}')!=false){
                var referrer = document.location.href;
                if (referrer.indexOf("wicket") > -1){
                    //In Wicket we must erase the pageId
                    referrer = referrer.substring(0, referrer.indexOf("?"));
                	var wicketLanguageChange = "<digi:trn>If you switch the current language the Activity changes will be lost.</digi:trn>";
                    var wantContinue="<digi:trn>Are you sure you want to continue?</digi:trn>";
                   	 window.onbeforeunload = null;
                   	 confirm (wicketLanguageChange+"\n"+wantContinue);

                }
                var newLoc = value + referrer;

                if(navigator.appName.indexOf('Microsoft Internet Explorer') > -1){ //Workaround to allow HTTP REFERER to be sent in IE (AMP-12638)
            		var referLink = document.createElement('a');
            		referLink.href = newLoc;
            		referLink.target = "_self";
            		document.body.appendChild(referLink);
            		referLink.click();

            	}
            	else{
            		 document.location.href = newLoc;
            	}

            }
    }
</script>
