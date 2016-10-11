<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<div id="myPLogframe" class="invisible-item">
	<div id="myPLogframeContent" class="content">
		===== ERROR =====
	</div>
</div>


<script type="text/javascript">
	YAHOOAmp.namespace("YAHOOAmp.amptab");
	YAHOOAmp.amptab.init = function() {
	    		var tabView = new YAHOOAmp.widget.TabView('tabview_container');
	};
		
    var myPanelLogframe = new YAHOOAmp.widget.Panel("newmyPLogframe", {
		width:"800px",
		height:"400px",
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
	
	function initScriptsLogframe() {
		var msgP5='\n<digi:trn jsFriendly="true" key="aim:previewLogframe">Preview Logframe</digi:trn>';
		myPanelLogframe.setHeader(msgP5);
		myPanelLogframe.setBody("");
		myPanelLogframe.render(document.body);
		panelFirstShow = 1;
	}
	
	function showPLogframe() {
		var content = document.getElementById("myPLogframeContent");
		var element5 = document.getElementById("myPLogframe"); 
		var loading='\n<digi:trn jsFriendly="true">Loading...</digi:trn>';
        content.innerHTML = '<div style="height:400px"><p align="center"><img align="top" src="/repository/aim/view/scripts/ajaxtabs/loading.gif" /><font size="3"><b>'+loading+'</b></font></p></div>';
		//if (panelFirstShow == 1){
			myPanelLogframe.setBody(element5.innerHTML);
			panelFirstShow = 0;
		//}
		document.getElementById("myPLogframeContent").scrollTop=0;
		myPanelLogframe.show();
	}
	function hidePLogframe() {
		myPanelLogframe.hide();
	}


    var responseSuccessLogframe = function(o){ 
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
		var content = document.getElementById("myPLogframeContent");
		content.innerHTML = response;
	}
		 
	var responseFailureLogframe = function(o){ 
	// Access the response object's properties in the 
	// same manner as listed in responseSuccess( ). 
	// Please see the Failure Case section and 
	// Communication Error sub-section for more details on the 
	// response object's properties.
		alert("Connection Failure!"); 
	}  
	var logframeCallback = 
	{ 
		success:responseSuccessLogframe, 
		failure:responseFailureLogframe 
	};
    
	function previewLogframe(id)
	{
        var postString		= "pageId=1&step=1&action=edit&surveyFlag=true&logframepr=true&activityId=" + id + "&actId=" + id;
        showPLogframe();
		YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/editActivity.do", logframeCallback, postString);
	}
	
	function previewLogFrameClicked() {
		var flag = validateForm();
		if (flag == true) {
	        var postString		= "edit=true&logframe=true&currentlyEditing=true&step=9&pageId=1";
	        showPLogframe();
			YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/previewActivity.do", logframeCallback, postString);
		}
	}

	var currentLogframe = window.onload;
	window.onload = function() {
        currentLogframe.apply(currentLogframe);
   	};
	initScriptsLogframe();

</script>
<style type="text/css">
	.mask {
	  -moz-opacity: 0.8;
	  opacity:.80;
	  filter: alpha(opacity=80);
	  background-color:#2f2f2f;
	}
	
	#myPLogframe .content { 
	    overflow:auto; 
	    height:455px; 
	    background-color:fff; 
	    padding:10px; 
	} 
	
</style>
