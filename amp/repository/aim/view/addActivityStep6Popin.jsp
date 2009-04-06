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
<c:set var="translation_validation_title_chars">
			<digi:trn key="contentrepository:plsTitleChars">Please only use letters, digits, '_' and space !</digi:trn>
</c:set>
<c:set var="translation_validation_url">
			<digi:trn key="contentrepository:plsSpecifyUrl;">Please specify a Url !</digi:trn>
</c:set>
<c:set var="translation_validation_title">
			<digi:trn key="contentrepository:plsSpecifyTitle">Please specify a title !</digi:trn>
</c:set>
<c:set var="translation_validation_filedata">
			<digi:trn key="contentrepository:plsSpecifyPath">Please select a file path !</digi:trn>
</c:set>
<c:set var="translation_validation_url_invalid">
			<digi:trn key="contentrepository:urlInvalid">URL format invalid !</digi:trn>
</c:set>

<div id="popin" style="display: none">
	<div id="popinContent" class="content">
	</div>
</div>

<script type="text/javascript">
<!--

		YAHOOAmp.namespace("YAHOOAmp.amp");

		var myPanel = new YAHOOAmp.widget.Panel("newpopins", {
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
	var panelStart=0;
	var checkAndClose=false;
	function initDocumentsScript() {
		var msg='\n<digi:trn key="aim:addSector">Add Sectors</digi:trn>';
		myPanel.setHeader(msg);
		myPanel.setBody("");
		myPanel.beforeHideEvent.subscribe(function() {
			panelStart=1;
		}); 
		
		myPanel.render(document.body);
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
<script type="text/javascript">
	var W3CDOM = (document.createElement && document.getElementsByTagName);

	function initFileUploads() {
		if (!W3CDOM) return;
		var fakeFileUpload = document.createElement('div');
		fakeFileUpload.className = 'fakefile';
		fakeFileUpload.appendChild(document.createElement('input'));

		var fakeFileUpload2 = document.createElement('div');
		fakeFileUpload2.className = 'fakefile2';


		var button = document.createElement('input');
		button.type = 'button';

		button.value = '<digi:trn key="aim:browse">Browse...</digi:trn>';
		fakeFileUpload2.appendChild(button);

		fakeFileUpload.appendChild(fakeFileUpload2);
		var x = document.getElementsByTagName('input');
		for (var i=0;i<x.length;i++) {
			if (x[i].type != 'file') continue;
			if (x[i].parentNode.className != 'fileinputs') continue;
			x[i].className = 'file hidden';
			var clone = fakeFileUpload.cloneNode(true);
			x[i].parentNode.appendChild(clone);
			x[i].relatedElement = clone.getElementsByTagName('input')[0];

 			x[i].onchange = x[i].onmouseout = function () {
				this.relatedElement.value = this.value;
			}
		}
	}
</script>



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
				addSector();
			}
			checkAndClose=false;			
		}
	}

	function myclose(){
		myPanel.hide();	
		panelStart=1;
	
	}
	function closeWindow() {
		myclose();
	}

	function myAddDocuments(params) {
		//alert(params);
		var msg='\n<digi:trn key="aim:addDocuments">Add Documents</digi:trn>';
		showPanelLoading(msg);
		initFileUploads();
		<digi:context name="commentUrl" property="/contentrepository/addTemporaryDocument.do" />
		var url = "<%=commentUrl %>";
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback, params);
	}
	function myAddDocumentsDM(params) {
		var msg='\n<digi:trn key="aim:addDocuments">Add Documents</digi:trn>';
		showPanelLoading(msg);
		<digi:context name="selectLoc" property="/contentrepository/selectDocumentDM.do" />	  
		var url = "<%=selectLoc %>";
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback, params);
	}
	function myAddLinks(params){
		var msg='\n<digi:trn key="aim:addLinks">Add LInks</digi:trn>';
		showPanelLoading(msg);
		<digi:context name="selPrg" property="/contentrepository/addTemporaryDocument.do" />	  
		var url = "<%=selPrg %>";
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback, params);
	}
	function showPanelLoading(msg){
		myPanel.setHeader(msg);		
		var content = document.getElementById("popinContent");
		content.innerHTML = "<div style='text-align: center'>" + "Loading..." + 
			"... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>";		
		showContent();
	}
	function generateFields(){
		var ret="";
		ret+="docTitle="+document.getElementsByName('docTitle')[0].value+
			 "&docDescription="+document.getElementsByName('docDescription')[0].value+
			 "&docNotes="+document.getElementsByName('docNotes')[0].value+
			 "&docType="+document.getElementsByName('docType')[0].value+
			 "&fileData="+document.getElementsByName('fileData')[0].value+
			 "&webResource="+document.getElementsByName('webResource')[0].value+
			 "&pageCloseFlag="+document.getElementsByName('pageCloseFlag')[0].value;
		return ret;
	}
	function addNewDocument(){
		var resourceFlag, docFlag;
		if(document.crDocumentManagerForm.webResource.value == "false")	{
			docFlag = validateDocument();
				
		}
		else
			resourceFlag = validateResource();		
		if(docFlag == true || resourceFlag == true){
			//checkAndClose=true;
			//<digi:context name="commentUrl" property="/contentrepository/addTemporaryDocument.do" />
			//var url = "<%=commentUrl %>";
			//YAHOOAmp.util.Connect.asyncRequest("POST", url, callback, generateFields());
			document.crDocumentManagerForm.action = "/contentrepository/addTemporaryDocument.do";	
		    document.crDocumentManagerForm.submit();

		    closeWindow();
		    //document.aimEditActivityForm.step.value = value;
			//document.aimEditActivityForm.action = "";
		    //document.aimEditActivityForm.target = "_self";
		    //document.aimEditActivityForm.submit();
		}
	}
	function validateDocument(){
		var titleFlag = isEmpty(document.crDocumentManagerForm.docTitle.value);
		var fileFlag = isEmpty(document.crDocumentManagerForm.fileData.value);
		if(titleFlag == true && fileFlag == true){
			alert(" ${translation_validation_title} ${translation_validation_filedata} ");
			document.crDocumentManagerForm.docTitle.focus();
			return false;
			
		}
		else{
			if(titleFlag == true){
				alert(" ${translation_validation_title} ");
				document.crDocumentManagerForm.docTitle.focus();
				return false;
			}
			
			if ( !usesAllowedCharacters(document.crDocumentManagerForm.docTitle.value) ) {
				alert(" ${translation_validation_title_chars} ");
				document.crDocumentManagerForm.docTitle.focus();
				return false;
			}
			
			if(fileFlag == true){
				alert(" ${translation_validation_filedata} ");
				document.crDocumentManagerForm.fileData.focus();
				return false;
			}
		}
		return true;
	}
	function usesAllowedCharacters(str) {
		//var regexp	= new RegExp("[a-zA-Z0-9_/-/ ]+");
		var regexp	= new RegExp("[A-Za-zÀÁÃÄÇÈÉËÌÍÏÑÒÓÕÖÙÚÜàáãäçèéëìíïñòóõöùúü0-9_/-/ ]+");
		var found	= regexp.exec(str);
		if (found != str)
			return false;
		return true;
	}
	function validateResource(){
		var titleFlag = isEmpty(document.crDocumentManagerForm.docTitle.value);
		var urlFlag = isEmpty(document.crDocumentManagerForm.webLink.value);
		if(titleFlag == true && urlFlag == true){
			alert(" ${translation_validation_title} ${translation_validation_url}");
			document.crDocumentManagerForm.docTitle.focus();
			return false;
		}
		else{
			if(titleFlag == true){
				alert(" ${translation_validation_title} ");
				document.crDocumentManagerForm.docTitle.focus();
				return false;
			}
			if(urlFlag == true){
				alert(" ${translation_validation_url} ");
				document.crDocumentManagerForm.webLink.focus();
				return false;
			}
		}
		return true;
	}

	-->

</script>
