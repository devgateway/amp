<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>


<div id="popin" class="invisible-item">
	<div id="popinContent" class="content">
	</div>
</div>


<script type="text/javascript" defer="defer">
<!--

		YAHOOAmp.namespace("YAHOO.amp");

		var myPanel = new YAHOOAmp.widget.Panel("newpopins", {
			width:"320px",
			fixedcenter: true,
		    constraintoviewport: false,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    draggable:true,
		    context: ["showbtn", "tl", "bl"]
		    });
		var panelStart=0;

		function initCurrencyScripts() {
			var msg='\n<digi:trn>Color Palete</digi:trn>';
			myPanel.setHeader(msg);
			myPanel.setBody("");
			myPanel.beforeHideEvent.subscribe(function() {
				panelStart=1;
			}); 
			
			myPanel.render(document.body);
		}
		addLoadEvent(initCurrencyScripts);
-->	
</script>

<style type="text/css">
	.mask {
	  -moz-opacity: 0.8;
	  opacity:.80;
	  filter: alpha(opacity=80);
	  background-color:#2f2f2f;
	}
	
	#myStep2 .content { 
	    overflow:auto; 
	    height:455px; 
	    background-color:fff; 
	    padding:10px; 
	} 

	
</style>



<script language="JavaScript">
    <!--	
	
    //DO NOT REMOVE THIS FUNCTION --- AGAIN!!!!
    function mapCallBack(status, statusText, responseText, responseXML){
       window.location.reload();
    }
    
    
    var responseSuccess = function(o){
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
		var content = document.getElementById("popinContent");
		//response = response.split("<!")[0];
		content.innerHTML = response;
	    //content.style.visibility = "visible";
		showColorPalete();
		showContent();
	}
 
	var responseFailure = function(o){ 
	// Access the response object's properties in the 
	// same manner as listed in responseSuccess( ). 
	// Please see the Failure Case section and 
	// Communication Error sub-section for more details on the 
	// response object's properties.
		//alert("Connection Failure!"); 
	}  
	var callback = 
	{ 
		success:responseSuccess, 
		failure:responseFailure 
	};

	function showPaleteContent(objectId,hexColorObject){
		objId=objectId;
		hexColor=hexColorObject;
		var msg='\n<digi:trn jsFriendly="true" key="aim:colorpalete">Color Palette</digi:trn>';
		showPanelLoading(msg);
		<digi:context name="showPalete" property="context/calendar/showColorPalete.do" />	  
		var url = "<%=showPalete %>";
		YAHOO.util.Connect.asyncRequest("POST", url, callback);
	}

	function showPanelLoading(msg){
		myPanel.setHeader(msg);		
		var content = document.getElementById("popinContent");
		content.innerHTML = '<div style="text-align: center">' + 
		'<img src="/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader-darkblue.gif" border="0" height="17px"/>&nbsp;&nbsp;' + 
		'<digi:trn jsFriendly="true">Loading...</digi:trn><br/><br/></div>';
		showContent();
	}

	function showContent(){
		var element = document.getElementById("popin");
		element.style.display = "inline";
		if (panelStart < 1){
			myPanel.setBody(element);
		}
		if (panelStart < 2){
			document.getElementById("popin").scrollTop=0;
			myPanel.show();
			panelStart = 2;
		}
	}

	function showColorPalete() {
		var colors = new Array("#000000", "#000033", "#000066", "#000099",
	                           "#0000CC", "#0000FF", "#330000", "#330033",
	                           "#330066", "#330099", "#3300CC", "#3300FF",
	                           "#660000", "#660033", "#660066", "#660099",
	                           "#6600CC", "#6600FF", "#990000", "#990033",
	                           "#990066", "#990099", "#9900CC", "#9900FF",
	                           "#CC0000", "#CC0033", "#CC0066", "#CC0099",
	                           "#CC00CC", "#CC00FF", "#FF0000", "#FF0033",
	                           "#FF0066", "#FF0099", "#FF00CC", "#FF00FF",
	                           "#003300", "#003333", "#003366", "#003399",
	                           "#0033CC", "#0033FF", "#333300", "#333333",
	                           "#333366", "#333399", "#3333CC", "#3333FF",
	                           "#663300", "#663333", "#663366", "#663399",
	                           "#6633CC", "#6633FF", "#993300", "#993333",
	                           "#993366", "#993399", "#9933CC", "#9933FF",
	                           "#CC3300", "#CC3333", "#CC3366", "#CC3399",
	                           "#CC33CC", "#CC33FF", "#FF3300", "#FF3333",
	                           "#FF3366", "#FF3399", "#FF33CC", "#FF33FF",
	                           "#006600", "#006633", "#006666", "#006699",
	                           "#0066CC", "#0066FF", "#336600", "#336633",
	                           "#336666", "#336699", "#3366CC", "#3366FF",
	                           "#666600", "#666633", "#666666", "#666699",
	                           "#6666CC", "#6666FF", "#996600", "#996633",
	                           "#996666", "#996699", "#9966CC", "#9966FF",
	                           "#CC6600", "#CC6633", "#CC6666", "#CC6699",
	                           "#CC66CC", "#CC66FF", "#FF6600", "#FF6633",
	                           "#FF6666", "#FF6699", "#FF66CC", "#FF66FF",
	                           "#009900", "#009933", "#009966", "#009999",
	                           "#0099CC", "#0099FF", "#339900", "#339933",
	                           "#339966", "#339999", "#3399CC", "#3399FF",
	                           "#669900", "#669933", "#669966", "#669999",
	                           "#6699CC", "#6699FF", "#999900", "#999933",
	                           "#999966", "#999999", "#9999CC", "#9999FF",
	                           "#CC9900", "#CC9933", "#CC9966", "#CC9999",
	                           "#CC99CC", "#CC99FF", "#FF9900", "#FF9933",
	                           "#FF9966", "#FF9999", "#FF99CC", "#FF99FF",
	                           "#00CC00", "#00CC33", "#00CC66", "#00CC99",
	                           "#00CCCC", "#00CCFF", "#33CC00", "#33CC33",
	                           "#33CC66", "#33CC99", "#33CCCC", "#33CCFF",
	                           "#66CC00", "#66CC33", "#66CC66", "#66CC99",
	                           "#66CCCC", "#66CCFF", "#99CC00", "#99CC33",
	                           "#99CC66", "#99CC99", "#99CCCC", "#99CCFF",
	                           "#CCCC00", "#CCCC33", "#CCCC66", "#CCCC99",
	                           "#CCCCCC", "#CCCCFF", "#FFCC00", "#FFCC33",
	                           "#FFCC66", "#FFCC99", "#FFCCCC", "#FFCCFF",
	                           "#00FF00", "#00FF33", "#00FF66", "#00FF99",
	                           "#00FFCC", "#00FFFF", "#33FF00", "#33FF33",
	                           "#33FF66", "#33FF99", "#33FFCC", "#33FFFF",
	                           "#66FF00", "#66FF33", "#66FF66", "#66FF99",
	                           "#66FFCC", "#66FFFF", "#99FF00", "#99FF33",
	                           "#99FF66", "#99FF99", "#99FFCC", "#99FFFF",
	                           "#CCFF00", "#CCFF33", "#CCFF66", "#CCFF99",
	                           "#CCFFCC", "#CCFFFF", "#FFFF00", "#FFFF33",
	                           "#FFFF66", "#FFFF99", "#FFFFCC", "#FFFFFF");
	    var total = colors.length;
	    var width = 18;
	    var cp_contents = "";
	    var windowRef = "";
	   
	    cp_contents += "<TABLE border="1" cellspacing="0" cellpadding="0">";
	    var use_highlight = (document.getElementById || document.all) ? true : false;
	    for(var i = 0; i < total; i++){
	        if((i % width) == 0) {
	            cp_contents += "<TR>";
	        }
	        
	        cp_contents += "<TD BGCOLOR = '"+colors[i]+"' width=15 height=20 onMouseOver= highLightColor('"+colors[i]+"'); onClick = pickColor('"+colors[i]+"');>&nbsp;&nbsp;</TD>";
	            if(((i + 1) >= total) || (((i + 1) % width) == 0)){
	            cp_contents += "</TR>";
	        }
	    }
		if(document.getElementById) {
	        var width1 = Math.floor(width / 2);
	        var width2 = width = width1;
	        cp_contents += "<TR><TD height=20 COLSPAN='" + width1 + "' BGCOLOR='#ffffff' ID='colorPickerSelectedColor'>&nbsp;</TD><TD height=20 COLSPAN='" + width2 + "' ALIGN='CENTER' ID='colorPickerSelectedColorValue'>#FFFFFF</TD></TR>";
	    }
	    cp_contents += "</TABLE>";
		var displayColorPalete = document.getElementById('contentPalete');
		displayColorPalete.innerHTML = cp_contents;
	}
	function highLightColor(c) {
	    var d = document.getElementById("colorPickerSelectedColor");
	    d.style.backgroundColor = c;
	    d = document.getElementById("colorPickerSelectedColorValue");
	    d.innerHTML = c;
	}

	var objId=null;
	var hexColor=null;
	function pickColor(color) {
	  if(objId!=null){
	    document.getElementById(hexColor).value = color;
	    var cl=document.getElementById(objId);
	    cl.style.cssText ="width:25px;font-family:verdana;font-size:9pt;background:"+color+";";
	  }
	  myPanel.hide();
	  //panelStart=1;
	}

	
-->	
</script>
