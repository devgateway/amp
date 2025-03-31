<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@page import="org.digijava.kernel.util.RequestUtils"%>
<digi:instance property="helpForm"/>
<c:set var="request" scope="session"><%= RequestUtils.getRealModuleInstance(request).getInstanceName() %></c:set>

<digi:context name="editTopic" property="context/module/moduleinstance/helpActions.do~actionType=editHelpTopic"/>
<digi:context name="createTopic" property="context/module/moduleinstance/helpActions.do~actionType=createHelpTopic"/>
<digi:context name="removeTopic" property="context/module/moduleinstance/helpActions.do~actionType=deleteHelpTopics"/>
<digi:context name="tree" property="context/module/moduleinstance/helpActions.do~actionType=saved"/>

<script type="text/javascript">
<!--

function validate(topickey){
		if(!topickey){
			alert("Default topic is not tree topic");
		return false;
	}else{
            return true;
        }
}
  function getKey(){

    var topicKey = document.getElementById("key").innerHTML;
    var key = topicKey.slice(topicKey.indexOf("y:")+2);
    return key;
  }
  
function edit(){
    if(validate(getKey())){
        var actUrl = "<%=editTopic%>"+"~helpTopicId="+selectedTopicId+"~wizardStep=0";
        window.location = actUrl;
    }
}
function printPreview (){
	
	if(navigator.appName.indexOf('Microsoft Internet Explorer') > -1){ 
	  var targetWndName = "_blank";  
	   var wnd = window.open("",targetWndName);  
	   var link = document.getElementById("linkPrint");  
	   link.target = targetWndName;  
	   link.href = "/help/helpActions.do?actionType=printPreview&helpTopicId="+selectedTopicId;  
	   link.click();  
	}else{
		window.open("/help/helpActions.do?actionType=printPreview&helpTopicId="+selectedTopicId);
	}
}

function printAllPreview (){
	var mode ="default";
	var winLoc=window.location.href;
	if(winLoc.indexOf('/help/admin/')!=-1){
		mode="admin";
	}
	window.open("/help/helpActions.do?actionType=printPreviewForWholeHelp&mode="+mode);
}

function removeTopic(){
	var confirmMessage = "<digi:trn>Are you sure you want to remove this topic?</digi:trn>";
    if(confirm(confirmMessage) && validate(getKey())){
        var actUrl = "<%=removeTopic%>"+"~helpTopicId="+selectedTopicId+"~wizardStep=0~multi=false";
        window.location = actUrl;
    }
}

function reFresh(){
        window.location = "<%=tree%>~wizardStep=0";
}

function create(){
        window.location = "<%=createTopic%>~wizardStep=0";
}

function saveTreeState(){
	debugger;
     var xmlString = document.getElementById("xmlString").value;
     //======= URL Encoded Character is %26 for &. and we have &amp; that need to replace there are ajax sending problrm ====== //
     
     $.ajax({
		type: "POST",
		url: '/help/helpActions.do?actionType=saveTreeState&Request=${request}',
		data: {
			changedXml: xmlString
		},
		success: stateChanged
	});
     
/*     replacedXmlString = xmlString.replace(/&amp;/,"%26");
     xmlHttp=GetXmlHttpObj();
		if (xmlHttp==null){
	 			alert ("Browser does not support HTTP Request")
	 		return
	 	}
        showProgress('progress');
        var urlact="/help/helpActions.do?actionType=saveTreeState&Request=${request}"
	    xmlHttp.open("POST",urlact,true);
	    xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        xmlHttp.send("&changedXml="+replacedXmlString);
		xmlHttp.onreadystatechange=stateChanged; */
}

function stateChanged(){
	//code to process Ajax request
	hideProgress('progress');
	reFresh();
}

function GetXmlHttpObj()	{
	 var xmlHttp=null;
	 try
 	{
 		// Firefox, Opera 8.0+, Safari
 	xmlHttp=new XMLHttpRequest();
 	}catch (e)

 		{

 		// Internet Explorer
 	 try	{
  		xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
  	}
 		catch (e)
  		{
  			xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
  		}
 	}
	return xmlHttp;
}

   function showProgress(name)
   {
      var prg = '<img src="/TEMPLATE/ampTemplate/images/ajax-loader.gif" alt="Loading..."/>Saving...';
       var progressViewer = document.getElementById(name);
       progressViewer.innerHTML = prg;
   }

   function hideProgress(name)
   {
      var progressViewer = document.getElementById(name);
       progressViewer.innerHTML = "";
   }

 

//-->
</script>


<style type="text/css">

  .link{
      cursor:pointer;
      font-size:10px;
   }


</style>

<div id="content"  class="yui-skin-sam" style="width:100%;">
	<div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">

                        <ul class="yui-nav">
                          <li class="selected">
                          <a title='><digi:trn key="aim:help:editcreate"> Edit/Create</digi:trn>'>
                          <div>
                            <digi:trn>Tools</digi:trn>
                          </div>
                          </a>
                          </li>
                        </ul>
                        <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">

                            <div style="padding:2px;">
                                <c:set var="topicEdit">
                                  <digi:trn>Click here to Edit Help Topic</digi:trn>
                                </c:set>
                                <c:set var="topicCreate">
                                  <digi:trn>Click here to Create Help Topic</digi:trn>
                                </c:set>
                                <c:set var="topicDelete">
                                  <digi:trn>Click here to Delete Help Topic</digi:trn>
                                </c:set>
                                <c:set var="savetreeState">
                                  <digi:trn>Click here to Save Tree State</digi:trn>
                                </c:set>
                                	<a class="link" onclick="printAllPreview();" title="${topicEdit}" ><digi:trn>Print All</digi:trn></a> |
									
									<a class="link" id="linkPrint" onclick="printPreview();" title="${topicEdit}" ><digi:trn>Print</digi:trn></a>
									
									<digi:secure group="Help Administrators"> |
										
                                    <a class="link" onclick="edit();" title="${topicEdit}" ><digi:trn>Edit Topic</digi:trn></a> |

                                    <a class="link" onclick="create();" title="${topicCreate}" > <digi:trn>Create Topic</digi:trn></a> |

                                    <a class="link" onclick="removeTopic();" title="${topicDelete}" ><digi:trn>Remove Topic</digi:trn></a> |

                                    <a class="link" onclick="saveTreeState();" title="${savetreeState}" ><digi:trn>Save Tree State</digi:trn></a>
                                 </digi:secure>


                          </div>
                     </div>
	</div>
</div>


