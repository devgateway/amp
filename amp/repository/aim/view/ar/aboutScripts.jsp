<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<script language="JavaScript" type="text/javascript" src="/repository/aim/view/scripts/util.js"></script>
<script language="JavaScript" type="text/javascript" src="/repository/aim/view/scripts/common.js"></script>
<script language="JavaScript1.2" type="text/javascript" src="/repository/aim/view/scripts/dscript120.js"></script>
<script language="JavaScript1.2" type="text/javascript" src="/repository/aim/view/scripts/dscript120_ar_style.js"></script>
<script language="JavaScript" type="text/javascript" src="/repository/aim/view/scripts/arFunctions.js"></script>
<script language="JavaScript" type="text/javascript" src="/repository/aim/view/scripts/ajaxtabs/ajaxtabs.js"></script>
<script type="text/javascript" src="/repository/aim/view/scripts/ajax.js"></script>

<script type="text/javascript" src="/repository/aim/view/scripts/separateFiles/dhtmlSuite-common.js"></script>
<script type="text/javascript" src="/repository/aim/view/scripts/separateFiles/dhtmlSuite-dynamicContent.js"></script>
<script type="text/javascript" src="/repository/aim/view/scripts/separateFiles/dhtmlSuite-modalMessage.js"></script>


<script type="text/javascript">
	messageObj = new DHTMLSuite.modalMessage(); // We only create one object of this class
	messageObj.setWaitMessage('Loading message - please wait....');
	messageObj.setShadowOffset(5); // Large shadow

	DHTMLSuite.commonObj.setCssCacheStatus(false);

	function displayMessage(url) {
		messageObj.setSource(url);
		messageObj.setCssClassMessageBox(false);
		messageObj.setSize(400, 200);
		messageObj.setShadowDivVisible(true); // Enable shadow for these boxes
		messageObj.display();
	}

	function displayStaticMessage(messageContent, cssClass, width, height) {
		messageObj.setHtmlContent(messageContent);
		messageObj.setSize(width, height);
		messageObj.setCssClassMessageBox(cssClass);
		messageObj.setSource(false); // no html source since we want to use a static message here.
		messageObj.setShadowDivVisible(true); // Disable shadow for these boxes
		messageObj.display();
	}

	function closeMessage() {
		messageObj.close();
	}
</script>

<script language="JavaScript" type="text/javascript" src="/TEMPLATE/ampTemplate/script/tooltip/wz_tooltip.js"></script>

<script type="text/javascript">
	YAHOO.namespace("YAHOO.amptab");
	YAHOO.amptab.init = function() {
		var tabView = new YAHOO.widget.TabView('tabview_container');
	};

	YAHOO.amptab.handleCloseAbout = function() {
		if (navigator.appName == 'Microsoft Internet Explorer') {
			//window.location.reload();
			//history.go(-1);
		}
	}

	var myPanel5 = new YAHOO.widget.Panel("new5", {
		width :"480px",
		fixedcenter :true,
		constraintoviewport :true,
		underlay :"none",
		close :true,
		visible :false,
		modal :true,
		draggable :true
	});

	myPanel5.beforeHideEvent.subscribe(YAHOO.amptab.handleCloseAbout);

	var bandera = 0;

	YAHOO.amptab.initScripts	= function () {
		var msgP5 = '\n<digi:trn key="aim:aboutamp">About AMP</digi:trn>';
		myPanel5.setHeader(msgP5);
		myPanel5.setBody("Example");
		myPanel5.render(document.body);
	};

	function showAbout() {
		YAHOO.amptab.init();
		var element = document.getElementById("customAbout");
		element.style.display = "inline";
		if(bandera == 0){
			myPanel5.setBody(element);
			bandera = 1;
		}
		myPanel5.center();
		myPanel5.show();
	}
	YAHOO.util.Event.addListener(window, "load", YAHOO.amptab.initScripts) ;
</script>

<style type="text/css">
.mask {
	-moz-opacity: 0.8;
	opacity: .80;
	filter: alpha(opacity = 80);
	background-color: #2f2f2f;
}
</style>
