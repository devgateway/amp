<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>
<!-- Dependencies -->
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script>

<script type="text/javascript">
    //'${msg}'
    function canExit() {
        if (typeof quitRnot1 == 'function') {
            return quitRnot1('${msg}');
        } else{
            return true;
        }

    }
</script>


<div id="myUserFilterWrapper" style="display: none;">
	<div id="customUser" class="invisible-item" class="content">
		<jsp:include page="/repository/aim/view/workspacePopup.jsp" />
	</div>
</div>

<c:set var="message">
	<c:choose>
		<c:when test="${sessionScope.currentMember.addActivity == 'true'}">
			<digi:trn>You did not save your activity. Do you want proceed without saving it ?</digi:trn>
		</c:when>
		<c:otherwise>
			<digi:trn>WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
		</c:otherwise>
	</c:choose>
</c:set>
<c:set var="quote">'</c:set>
<c:set var="escapedQuote">\'</c:set>
<c:set var="msg">
${fn:replace(message,quote,escapedQuote)}
</c:set>

<%org.digijava.kernel.request.SiteDomain siteDomain = null;%>

<script type="text/javascript">
function SwitchLanguageMenu(value) {
	if(typeof quitRnot1 != 'function'||quitRnot1('${msg}')!=false){
            var referrer = document.location.href;
            if (referrer.indexOf("wicket") > -1){
                //In Wicket we must erase the pageId
                referrer = referrer.substring(0, referrer.indexOf("?"));
            	var wicketLanguageChange = '<digi:trn jsFriendly="true">If you switch the current language the Activity changes will be lost.</digi:trn>';
                var wantContinue='<digi:trn jsFriendly="true">Are you sure you want to continue?</digi:trn>';
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
        		//change the language
        		window.location.href = newLoc;
        	}

        }
}


	function newAddActivity() {
		selectedLevelId=0; 
		window.location.href="/wicket/onepager/activity/new";	
	}
	
	function addActivity() {
		selectedLevelId=0; 
		window.location.href="/aim/addActivity.do~pageId=1~reset=true~resetMessages=true~action=create~activityLevelId="+selectedLevelId;	
	}

function addMessage(fillTypesAndLevels) {
	window.location.href="/message/messageActions.do?editingMessage=false&actionType="+fillTypesAndLevels;
}


</script>

