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
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>


<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-min.js'/>" > .</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-dom-event.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/container-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dragdrop-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/event-min.js'/>" >.</script>


<div id="popin" style="display: none">
	<div id="popinContent" class="content">
	</div>
</div>
<div id="popin2" style="display: none">
	<div id="popinContent2" class="content">
	</div>
</div>

<script type="text/javascript">
<!--

		YAHOOAmp.namespace("YAHOOAmp.amp");

		var myPanel = new YAHOOAmp.widget.Panel("newpopins", {
			width:"800px",
			fixedcenter: true,
		    constraintoviewport: false,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    draggable:true,
		    context: ["showbtn", "tl", "bl"]
		    });
		var myPanel2 = new YAHOOAmp.widget.Panel("newpopins", {
			width:"600px",
			fixedcenter: true,
		    constraintoviewport: false,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    draggable:true,
		    context: ["showbtn", "tl", "bl"]
		    });
	var panelStart, panelStart2;
	var checkAndClose2=false;
	var checkAndClose=false;	    
	    
	function initAddIndicatorScript() {
		var msg='\n<digi:trn>Select Indicator</digi:trn>';
		myPanel.setHeader(msg);
		myPanel.setBody("");
		myPanel.beforeHideEvent.subscribe(function() {
			panelStart=1;
		}); 
		myPanel.render(document.body);
		panelStart = 0;
		
		var msgP2='\n<digi:trn>Add Sector</digi:trn>';
		myPanel2.setHeader(msgP2);
		myPanel2.setBody("");
		myPanel2.beforeHideEvent.subscribe(function() {
			panelStart2=1;
		}); 
		myPanel2.render(document.body);
		panelStart2 = 0;
	}
	//this is called from editActivityMenu.jsp
	//window.onload=initSectorScript();
-->	
</script>
<style type="text/css">
	.mask {
	  -moz-opacity: 0.8;
	  opacity:.80;
	  filter: alpha(opacity=80);
	  background-color:#2f2f2f;
	}
	
	#popin .content { 
	    overflow:auto; 
	    height:455px; 
	    background-color:fff; 
	    padding:10px; 
	} 
	.bd a:hover {
  		background-color:#ecf3fd;
		font-size: 10px; 
		color: #0e69b3; 
		text-decoration: none	  
	}
	.bd a {
	  	color:black;
	  	font-size:10px;
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
		checkErrorAndClose();
	}
	function checkErrorAndClose(){
		if(checkAndClose==true){
			if(document.getElementsByName("someError")[0]==null || document.getElementsByName("someError")[0].value=="false"){
				myclose();
				refreshPage();
			}
			checkAndClose=false;			
		}
	}
	function refreshPage(){
		document.aimEditActivityForm.step.value = "10";
		document.aimEditActivityForm.action = "/aim/addActivity.do?edit=true";
		document.aimEditActivityForm.target = "_self";
		document.aimEditActivityForm.submit();
	}

	function myclose(){
		var content = document.getElementById("popinContent");
		content.innerHTML="";
		myPanel.hide();	
		panelStart=1;
	
	}
	function closeWindow() {
		myclose();
	}
	function showPanelLoading(msg){
		myPanel.setHeader(msg);		
		var content = document.getElementById("popinContent");
		content.innerHTML = "<div style='text-align: center'>" + "Loading..." + 
			"... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>";		
		showContent();
	}

	function addIndicator()
	{
		var msg='\n<digi:trn>Add Indicator</digi:trn>';
		showPanelLoading(msg);
		<digi:context name="selCreateInd" property="context/module/moduleinstance/selectCreateIndicators.do" />
		var url = "<%=selCreateInd %>?addIndicatorForStep9=true";
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
		
	}
	var responseSuccess2 = function(o){
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
			var content = document.getElementById("popinContent2");
		    //response = response.split("<!")[0];
			content.innerHTML = response;
		    //content.style.visibility = "visible";
			
			showContent2();
		}
	 
		var responseFailure2 = function(o){ 
		// Access the response object's properties in the 
		// same manner as listed in responseSuccess( ). 
		// Please see the Failure Case section and 
		// Communication Error sub-section for more details on the 
		// response object's properties.
			//alert("Connection Failure!"); 
		}  
	
	var callback2 =
	{ 
		success:responseSuccess2, 
		failure:responseFailure2 
	};

	function showContent2(){
		var element = document.getElementById("popin2");
		element.style.display = "inline";
		if (panelStart2 < 1){
			myPanel2.setBody(element);
		}
		if (panelStart2 < 2){
			document.getElementById("popin2").scrollTop=0;
			myPanel2.show();
			panelStart2 = 2;
		}
		checkErrorAndClose2();
	}
	function checkErrorAndClose2(){
		if(checkAndClose2==true){
			if(document.getElementsByName("someError")[0]==null || document.getElementsByName("someError")[0].value=="false"){
				myclose2();
				refreshPage2();
			}
			checkAndClose2=false;			
		}
	}
	function refreshPage2(){

	}

	function myclose2(){
		var content = document.getElementById("popinContent2");
		content.innerHTML="";
		panelStart2=1;
		myPanel2.hide();
	
	}

	function closeWindow2() {
		myclose2();
	}

	function showPanelLoading2(msg){
		myPanel2.setHeader(msg);		
		var content = document.getElementById("popinContent2");
		content.innerHTML = "<div style='text-align: center'>" + "Loading..." + 
			"... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>";		
		showContent2();
	}
	
	-->

</script>
<script language="JavaScript">
    <!--
    function getParams(){
        var ret=""
        ret="&activityId="+document.getElementsByName('activityId')[0].value+
        	"&addswitch="+document.getElementsByName('addswitch')[0].value+
        	"&sectorName="+document.getElementsByName('sectorName')[0].value;
    	if(document.getElementsByName('selectedIndicators')[0]!=null){
	    	var indicators = document.getElementsByName('selectedIndicators')[0].options.length;
	    	for(var i=0; i<indicators; i++ ){
	    		if(document.getElementsByName('selectedIndicators')[0].options[i].selected){
	        		ret+="&selectedIndicators="+document.getElementsByName('selectedIndicators')[0].options[i].value;
	        	}
	        }
    	}
    	if(document.getElementsByName('indicatorName')[0]!=null){
        	ret += "&indicatorName="+document.getElementsByName('indicatorName')[0].value+
        	"&indicatorDesc="+document.getElementsByName('indicatorDesc')[0].value+
        	"&indicatorName="+document.getElementsByName('indicatorName')[0].value+
        	"&ascendingInd="+document.getElementsByName('ascendingInd')[0].value;
        }

    	return ret;
    }
    function searchIndicators(){	
       <digi:context name="searchInd" property="context/module/moduleinstance/searchIndicators.do?showAddIndPage=false" />
		var url = "<%=searchInd %>";
		url += getParams();
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
       
    }
    function addIndicatorTL(addbutton){
        var emptychk = false;
        if(addbutton == '1')
            emptychk = doesItHaveValue1();
        if(addbutton == '2')
            emptychk = doesItHaveValue2()
        if(emptychk == true) {
		   <digi:context name="addInd" property="context/module/moduleinstance/addIndicatorsTL.do"/>
           var url = "<%=addInd%>?forStep9=true";
           url += getParams();
           
           checkAndClose=true
           YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
        }        
    }


    function addNewIndicatorTL(selectedSectsSize)
    {	
        var valid = validateSectorForm(selectedSectsSize);
        if (valid == true) {
            <digi:context name="addNewInd" property="context/module/moduleinstance/addNewIndicatorTL.do?"/>
            var url = "<%=addNewInd%>";
            url+= "&indicatorName="+document.getElementsByName('indicatorName')[0].value+
  	              "&indicatorDesc="+document.getElementsByName('indicatorDesc')[0].value+
	              "&indicatorName="+document.getElementsByName('indicatorName')[0].value+
                  "&ascendingInd="+document.getElementsByName('ascendingInd')[0].value+
                  "&creationDate="+document.getElementsByName('creationDate')[0].value;
            
        	if(document.getElementsByName('selActivitySector')!=null){
    	    	var indicators = document.getElementsByName('selActivitySector').length;
    	    	for(var i=0; i<indicators; i++ ){
   	        		url+="&selActivitySector="+document.getElementsByName('selActivitySector')[i].value;
    	        }
        	}
       
            YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
        }
        return valid;
    }

    function validateSectorForm(selectedSectsSize) {

        var values = document.getElementsByTagName("forEach");

        if (document.aimIndicatorFormIndicator.indicatorName.value.length == 0) {
            alert("Please enter indicator name");
            document.aimIndicatorFormIndicator.indicatorName.focus();
            return false;
        }

        if(selectedSectsSize!=1) {
            alert("Please add Sectors");
            return false;
        }

        return true;
    }

    function isSearchKeyGiven(){
        if(trim(document.aimIndicatorFormIndicator.searchkey.value).length == 0){
            alert("Please give a Keyword to search");
            document.aimIndicatorFormIndicator.searchkey.focus();
            return false;
        }
        return true;
    }
    function doesItHaveValue1(){
        if(document.aimIndicatorFormIndicator.selectedIndicators.value == ''){
            alert("Please select an Indicator");
            document.aimIndicatorFormIndicator.selectedIndicators.focus();
            return false;
        }
        return true;
    }
    function doesItHaveValue2(){
        if(document.aimIndicatorFormIndicator.selIndicators.value == null){
            alert("Please select an Indicator");
            return false;
        }
        return true;
    }

    function clearform() {
         <digi:context name="searchInd" property="context/module/moduleinstance/searchIndicators.do?action=clear"/>
          var url = "<%= searchInd%>";
          YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
      }

    function addSectors() {		
        <digi:context name="addSector" property="context/module/moduleinstance/sectorActions.do?actionType=loadSectors&sectorReset=true" />
        var urladd = "<%=addSector%>";  
        YAHOOAmp.util.Connect.asyncRequest("POST", urladd, callback2);
        

        <digi:context name="justSubmit" property="context/module/moduleinstance/sectorActions.do?actionType=justSubmit" /> 
        var url = "<%=justSubmit%>";  
        url += getParams();
        YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
        
    }

    function removeSelSectors() {	
        if (validateSector()) {
            <digi:context name="remSec" property="context/module/moduleinstance/sectorActions.do?actionType=removeSelectedSectors" />
            var url = "<%= remSec%>";
            url+= "&indicatorName="+document.getElementsByName('indicatorName')[0].value+
  	              "&indicatorDesc="+document.getElementsByName('indicatorDesc')[0].value+
	              "&indicatorName="+document.getElementsByName('indicatorName')[0].value+
                  "&ascendingInd="+document.getElementsByName('ascendingInd')[0].value+
                  "&creationDate="+document.getElementsByName('creationDate')[0].value;
            if(document.getElementsByName('selActivitySector')!=null){
            	var sec = document.getElementsByName('selActivitySector').length
            	for(var i=0;i<sec;i++){
                	if(document.getElementsByName('selActivitySector')[i].checked){
                    	url += "&selActivitySector="+document.getElementsByName('selActivitySector')[i].value;
                	}
            	}
            }
            YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
            return true;			
        }
        else {
            return false;
        }
    }

    function validateSector(){
        if (document.aimIndicatorFormIndicator.selActivitySector.checked != null) {
            if (document.aimIndicatorFormIndicator.selActivitySector.checked == false) {
               alert("Please choose a sector to remove");
               return false;
            }
        } 
        else {
            var length = document.aimIndicatorFormIndicator.selActivitySector.length;
            var flag = 0;
            for (i = 0;i < length;i ++) {
               if (document.aimIndicatorFormIndicator.selActivitySector[i].checked == true) {
                  flag = 1;
                  break;
               }
            }
            if (flag == 0) {
                alert("Please choose a sector to remove");
                return false;
            }
        }
        return true;	
    }

   function gotoCreateIndPage() {
       <digi:context name="addIndPage" property="context/module/moduleinstance/searchIndicators.do?clear=true&addInd=true"/>
       var url = "<%=addIndPage%>";		
       url += getParams();
       YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
   }

   -->
</script>

<script language="JavaScript">

	<!--

	function selectSector() {		
		<digi:context name="selSector" property="context/module/moduleinstance/sectorActions.do?actionType=addsectorToindicator"/>
	    var url = "<%= selSector %>";
	    url += "&sectorReset="+document.getElementsByName('sectorReset')[0].value+
	           "&sectorScheme="+document.getElementsByName('sectorScheme')[0].value+
	           "&sector="+document.getElementsByName('sector')[0].value;
	    myclose2();
	    YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
	}	
	
	function reloadSector(value) {		
		if (value == 1) {
			document.aimIndicatorFormSector.sector.value = -1;
		}	
		<digi:context name="selSector" property="context/module/moduleinstance/sectorActions.do?actionType=loadSectors"/>
	    var url = "<%= selSector %>";
	    url += "&sectorReset="+document.getElementsByName('sectorReset')[0].value+
	           "&sectorScheme="+document.getElementsByName('sectorScheme')[0].value+
	           "&sector="+document.getElementsByName('sector')[0].value;
	    YAHOOAmp.util.Connect.asyncRequest("POST", url, callback2);									
	}	
	
	-->

</script>
