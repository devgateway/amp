<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@page import="org.digijava.module.help.util.HelpUtil"%>


<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/help/script/dhtmlxtree.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/help/script/dhtmlXTree_xw.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/help/script/dhtmlXTree_ed.js"/>"></script>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/help/script/dhtmlxcommon.js"/>"></script>
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
<digi:context name="url" property="context/module/moduleinstance/helpActions.do?actionType=viewSelectedHelpTopic" />
<div id="content"  class="yui-skin-sam" style="width:100%;">
	<div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
               <ul class="yui-nav">&nbsp;
                          <li class="selected" style="width: 100%">
                          <a title='<digi:trn key="aim:PortfolioOfReports">Portfolio Reports </digi:trn>'>
                          <div style="border-left-width:1px">
                          	<digi:trn key="aim:helpTopic">Help Topics</digi:trn>
                          </div>
                          </a>
                          </li>
                        </ul>
             		 <bean:define id="topic" name="helpForm" property="topicTree" type="java.util.Collection"/>
        <!--
                        <div class="yui-content" style="height:700px;overflow: auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
                        <bean:define id="topic" name="helpForm" property="topicTree" type="java.util.Collection"/>
                        	<%= HelpUtil.renderTopicsTree(topic,request) %>

             		</div>
            -->

  <div id="treeboxbox_tree" class="yui-content" style="height:700px;overflow: auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;"></div>
  <div id="log"></div>
  <div id="xmlString"></div>
  <div id="moduleInstance" style="display:none;">
     <c:forEach var="topins" items="${topic}" begin="1" end="1">
            ${topins.moduleInstance}
     </c:forEach>
  </div>

<script type="text/javascript">


    
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
            tree.enableDragAndDrop(true);

            tree.setOnClickHandler(tonclick);
            var xml = '<?xml version="1.0" encoding="iso-8859-1"?>';
			    xml+='<tree id="0" radio="1">';
			    xml+= '<%= HelpUtil.renderTopicTree(topic,request,false) %>';
			    xml+='</tree>';
		    tree.loadXMLString(xml);

             tree.attachEvent("onDrag",function(sid,tid,sobj,tobj){

            if(sid){

                var contextTreeXml = tree.serializeTree();
                //var xmlobject = (new DOMParser()).parseFromString(contextTreeXml, "text/xml");
                document.getElementById("xmlString").innerHTML = contextTreeXml;

                    return true;
            }});

    function show(str){

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

	 	var urlact="/help/helpActions.do?actionType=getbody"
		urlact=urlact+"&body="+str
		xmlHttp.open("GET",urlact,true)
		xmlHttp.onreadystatechange=stateChange
		xmlHttp.send(null)
}

function stateChange(){
	 if (xmlHttp.readyState==4)
  {
	 document.getElementById("bodyhelp").innerHTML = xmlHttp.responseText.slice(0,xmlHttp.responseText.indexOf("help:"));
     document.getElementById("key").innerHTML = xmlHttp.responseText.slice(xmlHttp.responseText.indexOf("help:"));
     document.getElementById("bodyhelp").style.border="1px solid #A5ACB2";

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
</script>


