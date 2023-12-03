<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@page import="org.digijava.ampModule.help.util.HelpUtil"%>
 <digi:ref href="css/dhtmlxtree.css" type="text/css" rel="stylesheet" />

<script language="JavaScript" type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/help/script/dhtmlxtree.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/help/script/dhtmlXTree_xw.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/help/script/dhtmlXTree_ed.js"/>"></script>

<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/help/script/dhtmlxcommon.js"/>"></script>
<!--
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/dhtmlxcommon_debug.js"/>"></script>
-->

<script language="javascript" type="text/javascript">
	function toggleDiv(id,state){
		if (state==true){
			document.getElementById('uncollapse'+id).style.display='block';
			document.getElementById('collapse'+id).style.display='none';
		}
		if (state==false){
			document.getElementById('collapse'+id).style.display='block';
			document.getElementById('uncollapse'+id).style.display='none';
		}
	}

	 function expandProgram(progId){

	 	var imgId='#img_'+progId;
		var imghId='#imgh_'+progId;
		var divId='#div_theme_'+progId;
		$(imghId).show();
		$(imgId).hide();
		$(divId).show('fast');
	}

	function collapseProgram(progId){

		var imgId='#img_'+progId;
		var imghId='#imgh_'+progId;
		var divId='#div_theme_'+progId;
		$(imghId).hide();
		$(imgId).show();
		$(divId).hide('fast');
	}

</script>

<digi:instance property="helpForm" />
<digi:context name="url" property="context/ampModule/moduleinstance/helpActions.do?actionType=viewSelectedHelpTopic" />
<div id="content"  class="yui-skin-sam" style="width:100%;">
	<div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
               <ul class="yui-nav">&nbsp;
                          <li class="selected" style="width: 100%">
                          <a title='<digi:trn jsFriendly="true" key="aim:PortfolioOfReports">Portfolio Reports </digi:trn>'>
                          <div style="border-left-width:1px">
                          	<digi:trn key="aim:helpTopic">Help Topics</digi:trn>
                          </div>
                          </a>
                          </li>
                        </ul>
             		 <bean:define id="topic" name="helpForm" property="topicTree" type="java.util.Collection"/>
  <div id="treeboxbox_tree" class="yui-content" style="height:700px;overflow: auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;"></div>
  <div id="log"></div>
  <!--<div id="xmlString"></div>-->
  <input type="hidden" id="xmlString"/>
 
<script type="text/javascript">

var selectedTopicId;

// DOMParser for IE
if (typeof DOMParser == "undefined") {
	   DOMParser = function () {}

	   DOMParser.prototype.parseFromString = function (str, contentType) {
	      if (typeof ActiveXObject != "undefined") {
	         var d = new ActiveXObject("MSXML.DomDocument");
	         d.loadXML(str);
	         return d;
	      } else if (typeof XMLHttpRequest != "undefined") {
	         var req = new XMLHttpRequest;
	         req.open("GET", "data:" + (contentType || "application/xml") +
	                         ";charset=utf-8," + encodeURIComponent(str), false);
	         if (req.overrideMimeType) {
	            req.overrideMimeType(contentType);
	         }
	         req.send(null);
	         return req.responseXML;
	      }
	   }
	}



	function tonclick(id){
			if(id != null){
					var id = tree.getSelectedItemId();
					show(id);
			 }
	}
	
            var id = document.getElementById("treeboxbox_tree");
	 		tree = new dhtmlXTreeObject(id,"100%","100%",0);
	 		tree.setImagePath("../../repository/help/view/images/csh_vista/");
	        tree.enableTreeImages(false);
	        <digi:secure group="Help Administrators">
	            tree.enableDragAndDrop(true);
			</digi:secure>
            tree.setDragBehavior("complex");
            tree.setOnClickHandler(tonclick);
            var xml ='<?xml version="1.0" encoding="UTF-8"?>';
			    xml+='<tree id="0" radio="1">';
			    xml+='<%=HelpUtil.renderTopicTree(topic,request,false) %>';
			    xml+='</tree>';
		  
		    tree.loadXMLString(xml);
		    

		 
		  
		    tree.attachEvent("onXLE",function(){
		    contextTreeXml = tree.serializeTree();  
		 	var xmlobject = (new DOMParser()).parseFromString(contextTreeXml, "text/xml");
		    node = xmlobject.getElementsByTagName("tree");
		    id=getFirstChild(node);
		    show(id);
		    });
		    
		    		     
          tree.attachEvent("onDrop",function(sid,tid,sobj,tobj){
	            if(sid){
    	            var contextTreeXml = tree.serializeTree();
        	        document.getElementById("xmlString").value = contextTreeXml;
            		return true;
            	}
           });

           


    function show(str){
		if (str == null){
			return;
		}
	  if (str.length==0){
			 	document.getElementById("bodyhelp").innerHTML="";
				document.getElementById("bodyhelp").style.border="1px";
		 	return
		 }

	 xmlHttp=GetXmlHttpObject();
		if (xmlHttp==null){
	 			alert ("Browser does not support HTTP Request")
	 		return
	 	}
	 	$("#bodyhelp").show();
	 	var timestamp = Number(new Date());
		var urlact="/help/helpActions.do?actionType=getbody"
		urlact=urlact+"&body="+str+"&nocahe="+timestamp ;
		xmlHttp.open("GET",urlact,true)	
		xmlHttp.onreadystatechange=stateChange
		xmlHttp.send(null)
}

function stateChange(){
	 if (xmlHttp.readyState==4)
  {
		
		 document.getElementById("bodyhelp").innerHTML = xmlHttp.responseText.slice(0,xmlHttp.responseText.indexOf("_editor_key_="));
	     document.getElementById("key").innerHTML = xmlHttp.responseText.slice(xmlHttp.responseText.indexOf("_editor_key_=")+12,xmlHttp.responseText.indexOf("_topic_db_id_="));
	     selectedTopicId = xmlHttp.responseText.slice(xmlHttp.responseText.indexOf("_topic_db_id_=")+14);
	     document.getElementById("bodyhelp").style.border="1px solid #A5ACB2";
//	 document.getElementById("bodyhelp").innerHTML = xmlHttp.responseText.slice(0,xmlHttp.responseText.indexOf("help:"));
//     document.getElementById("key").innerHTML = xmlHttp.responseText.slice(xmlHttp.responseText.indexOf("help:"));
//     document.getElementById("bodyhelp").style.border="1px solid #A5ACB2";

   }

   $("#title").hide();
   $("#searchedTitle").hide();
   $("#searchedBody").hide();


}


function GetXmlHttpObject()	{
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

function  getFirstChild(nl){  
   for (i = 0; i< nl.length; i++){
   	nd = nl.item(i);

   	x= nd.firstChild;
   	if (x == null){
   		return null;
   	}
   	while (x.nodeType!=1){
   		x=x.nextSibling;
   	}
   	return x.attributes.getNamedItem("id").nodeValue;
   		
  }
}

</script>


