
<!--<div id="myPOverviewframe" class="invisible-item">-->
<!--	<div id="myPOverviewframeContent" class="content">-->
<!--		===== ERROR =====-->
<!--	</div>-->
<!--</div>-->


<script type="text/javascript">
	var contentLocal;

	YAHOOAmp.namespace("YAHOOAmp.amptab_2");
	YAHOOAmp.amptab.init = function() {
	    		var tabView = new YAHOOAmp.widget.TabView('tabview_container_2');
	};

	YAHOOAmp.amptab.handleClose = function() {
		//alert('cierro');
		//this.reload();
		//if(navigator.appName == 'Microsoft Internet Explorer'){
			//window.location.reload();
			//history.go(-1);
		//}
		//document.getElementById("myPOverviewframe").removeChild(contentLocal);
	}
		
    var myPanelOverviewframe = new YAHOOAmp.widget.Panel("newmyPOverviewframe", {
		width:"700px",
		height:"500px",
	    fixedcenter: true,
	    constraintoviewport: true,
	    underlay:"none",
	    close:true,
	    visible:false,
	    modal:true,
	    draggable:true,
	    context: ["showbtn", "tl", "bl"]
	    }
	     );

    myPanelOverviewframe.beforeHideEvent.subscribe(YAHOOAmp.amptab.handleClose);
	
	function initScriptsOverviewframe() {
		//alert('initScriptsOverviewframe');
		myPanelOverviewframe.setHeader('\n');
		myPanelOverviewframe.setBody("The Panel is a powerful UI control");
		myPanelOverviewframe.render(document.body);
		panelFirstShow = 1;
		//contentLocal = document.getElementById("myPOverviewframeContent");
	}
	
	function showPOverviewframe(title) {
		//alert('showPOverviewframe');
		//contentLocal = document.getElementById("myPOverviewframeContent");
		contentLocal = document.createElement('div');
		contentLocal.setAttribute('id', 'myPOverviewframeContent');
		contentLocal.setAttribute('class', 'content');
						
		//var element5 = document.getElementById("myPOverviewframe");
		//element5.appendChild(contentLocal);
		 
		contentLocal.innerHTML = '<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><p align="center"><img align="top" src="/repository/aim/view/scripts/ajaxtabs/loading.gif" /><font size="3"><b>Loading...</b></font></p>';
		//contentLocal.parentElement = element5;
		//if (panelFirstShow == 1){ 
			contentLocal.style.display = "inline";
			myPanelOverviewframe.setHeader('\n' + title);
			myPanelOverviewframe.setBody(contentLocal);
			panelFirstShow = 0;
		//}
		//document.getElementById("myPOverviewframeContent").scrollTop=0;
		//content.scrollTop=0;
		//contentLocal = content;
		//myPanelOverviewframe.render(document.body);
		myPanelOverviewframe.show();
	}
	function hidePOverviewframe() {
		myPanelOverviewframe.hide();
	}


    var responseSuccessOverviewframe = function(o){ 
	/* Please see the Success Case section for more
	 * details on the response object's properties.
	 * o.tId
	 * o.status
	 * o.statusText
	 * o.getResponseHeader[ ]
	 * o.getAllResponseHeaders
	 * o.responseText
	 * o.responseXML
	 * o.argument
	 */
		var response = o.responseText;
		//alert(response);
		//var content = document.getElementById("myPOverviewframeContent");
		contentLocal.innerHTML = response;
	}
		 
	var responseFailureOverviewframe = function(o){ 
	// Access the response object's properties in the 
	// same manner as listed in responseSuccess( ). 
	// Please see the Failure Case section and 
	// Communication Error sub-section for more details on the 
	// response object's properties.
		alert("Connection Failure!"); 
	}  
	var OverviewframeCallback = 
	{ 
		success:responseSuccessOverviewframe, 
		failure:responseFailureOverviewframe 
	};


	//function viewProjectDetails(type,key) {
		//alert('1');
		//openNewWindow(600, 400);
		//<digi:context name="viewProjDetails" property="context/ampModule/moduleinstance/viewProjectDetails.do"/>
		
		//document.aimMainProjectDetailsForm.type.value = type;
		//document.aimMainProjectDetailsForm.description.value = key;
		//document.aimMainProjectDetailsForm.objectives.value = key;
		//document.aimMainProjectDetailsForm.target = popupPointer.name;
		//document.aimMainProjectDetailsForm.submit();
	//}
    
	function previewOverviewframe(type,key, title)
	{
		//alert('previewOverviewframe');
        var postString		= "type="+type+"&description="+key;
        //alert(postString);
        showPOverviewframe(title);
		YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/viewProjectDetails.do", OverviewframeCallback, postString);
	}

	var currentOverviewframe = window.onload;
	window.onload = function() {
        currentOverviewframe.apply(currentOverviewframe);
   	};
	initScriptsOverviewframe();

</script>
<style type="text/css">
	.mask {
	  -moz-opacity: 0.8;
	  opacity:.80;
	  filter: alpha(opacity=80);
	  background-color:#2f2f2f;
	}
	
	#myPOverviewframe .content { 
	    overflow:auto; 
	    height:455px; 
	    background-color:fff; 
	    padding:10px; 
	} 
	
</style>