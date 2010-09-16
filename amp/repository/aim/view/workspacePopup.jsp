<script type="text/javascript">
	var contentLocal;

	YAHOOAmp.namespace("YAHOOAmp.amptab");
	YAHOOAmp.amptab.init = function() {
	    		var tabView = new YAHOOAmp.widget.TabView('tabview_container');
	};

	YAHOOAmp.amptab.handleClose = function() {
		
	}
		
    var myPanelWorkspaceframe = new YAHOOAmp.widget.Panel("newmyPWorkspaceframe", {
		width:"600px",
		height:"700px",
	    fixedcenter: true,
	    constraintoviewport: true,
	    underlay:"none",
	    close:true,
	    visible:false,
	    modal:true,
	    draggable:true,
	    context: ["tl", "bl"]
	    }
	     );

    myPanelWorkspaceframe.beforeHideEvent.subscribe(YAHOOAmp.amptab.handleClose);
	
	function initScriptsWorkspaceframe() {
		//alert('initScriptsWorkspaceframe');
		var msgP8='\n<digi:trn key="aim:memberDetails">Member Details</digi:trn>';
		myPanelWorkspaceframe.setHeader(msgP8);
		myPanelWorkspaceframe.setBody("The Panel is a powerful UI control");
		myPanelWorkspaceframe.render(document.body);
		panelFirstShow = 1;
	}
	
	function showPWorkspaceframe() {
		initScriptsWorkspaceframe();
		//alert('showPWorkspaceframe');
		contentLocal = document.createElement('div');
		contentLocal.setAttribute('id', 'myPWorkspaceframeContent');
		contentLocal.setAttribute('class', 'content');
		contentLocal.innerHTML = '<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><p align="center"><img align="top" src="/repository/aim/view/scripts/ajaxtabs/loading.gif" /><font size="3"><b>Loading...</b></font></p>';
            contentLocal.style.height = "700px";
            contentLocal.style.width = "594px";
            contentLocal.style.overflow="scroll";
			myPanelWorkspaceframe.setBody(contentLocal);
			panelFirstShow = 0;
		myPanelWorkspaceframe.show();
	}
	function hidePWorkspaceframe() {
		myPanelWorkspaceframe.hide();
	}


    var responseSuccessWorkspaceframe = function(o){ 
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
		//var content = document.getElementById("myPWorkspaceframeContent");
		contentLocal.innerHTML = response;
	}
		 
	var responseFailureWorkspaceframe = function(o){ 
	// Access the response object's properties in the 
	// same manner as listed in responseSuccess( ). 
	// Please see the Failure Case section and 
	// Communication Error sub-section for more details on the 
	// response object's properties.
		alert("Connection Failure!"); 
	}  
	var WorkspaceframeCallback = 
	{ 
		success:responseSuccessWorkspaceframe, 
		failure:responseFailureWorkspaceframe 
	};

	function previewWorkspaceframe(action,params)
	{
        showPWorkspaceframe();
		YAHOOAmp.util.Connect.asyncRequest("POST", action+params, WorkspaceframeCallback, '');
	}

	/*var currentWorkspaceframe = window.onload;
	window.onload = function() {
        currentWorkspaceframe.apply(currentWorkspaceframe);
   	};*/
   	addLoadEvent(initScriptsWorkspaceframe);

</script>
<style type="text/css">
	.mask {
	  -moz-opacity: 0.8;
	  opacity:.80;
	  filter: alpha(opacity=80);
	  background-color:#2f2f2f;
	}
</style>
