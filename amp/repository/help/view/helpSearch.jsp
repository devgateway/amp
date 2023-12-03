<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ page import="org.digijava.kernel.util.RequestUtils"%>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<digi:instance property="helpForm" />


<script language="JavaScript">

<digi:context name="helpActionUrl" property="context/ampModule/moduleinstance/helpActions.do" />



function showHint(str,event){
	
if(event.keyCode != 13){
  if (str.length==0){ 
		 	document.getElementById("livesearch").innerHTML="";
		 	
		 	document.getElementById("livesearch").style.border="1px";
	 	return
	 }
	 
 xmlHttp=GetXmlHttpObject()
	if (xmlHttp==null){
 			alert ("Browser does not support HTTP Request")
 		return
 	} 
 	$("#livesearch").show();
	var urls="/help/helpActions.do?actionType=vewSearchKey"
	url=urls+"&loadKey="+str
	url+="&instance="+'<%= RequestUtils.getRealModuleInstance(request).getInstanceName()%>'
    xmlHttp.open("GET",url,true)
	xmlHttp.onreadystatechange=stateChanging
	xmlHttp.send(null)
}
}
function stateChanging(){
	 if (xmlHttp.readyState==4)
  {
     document.getElementById("livesearch").innerHTML=xmlHttp.responseText;
	 document.getElementById("livesearch").style.border="1px solid #A5ACB2";
   } 
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




function select(title){

	document.getElementById("selected").value=document.getElementById(title.id).innerHTML;
	$("#livesearch").hide();

}  

function resetKeyword(){
	document.getElementById('selected').value='';
	$('#livesearch').hide();
	xmlHttp=GetXmlHttpObject()
	if (xmlHttp==null){
 			alert ("Browser does not support HTTP Request")
 		return
 	}

var actionUrl = '<%=helpActionUrl%>';
	var key= 'getting started';//reset to start of the helper
	var urls = actionUrl + '?actionType=searchHelpTopicNew&key='+key;
	
    xmlHttp.open("GET",urls,true)
    
	xmlHttp.onreadystatechange=stChang
	xmlHttp.send(null)
}

function search(){
 
	var key = document.getElementById("selected").value;
    if( key.replace(/^\s+|\s+$/g,"").length==0){
        var msg="<digi:trn>Please provide search criteria</digi:trn>";
        alert(msg);
        return;
    }
	xmlHttp=GetXmlHttpObject()
	if (xmlHttp==null){
 			alert ("Browser does not support HTTP Request")
 		return
 	} 
 	
	var baseChars = new Array("Ï ","%cf",
			"Ð","%d0",
			"Ñ","%d1",
			"Ò","%d2",
			"Ó","%d3",
			"Ô","%d4",
			"Õ","%d5",
			"Ö","%d6",
			"Ø","%d8",
			"Ù","%d9",
			"Ú","%da",
			"Û","%db",
			"Ü","%dc",
			"Ý","%dd",
			"Þ","%de",
			"ß","%df",
			"à","%e0",
			"á","%e1",
			"â","%e2",
			"ã","%e3",
			"ä","%e4",
			"å","%e5",
			"æ","%e6",
			"ç","%e7",
			"è","%e8",
			"é","%e9",
			"ê","%ea",
			"ë","%eb",
			"ì","%ec",
			"í","%ed",
			"î","%ee",
			"ï","%ef",
			"ð","%f0",
			"ñ","%f1",
			"ò","%f2",
			"ó","%f3",
			"ô","%f4",
			"õ","%f5",
			"ö","%f6",
			"÷","%f7",
			"ø","%f8",
			"ù","%f9",
			"ú","%fa",
			"û","%fb",
			"ü","%fc",
			"ý","%fd",
			"þ","%fe",
			"ÿ","%ff");
 	
 	var actionUrl = '<%=helpActionUrl%>';
	var urls = actionUrl + '?actionType=searchHelpTopicNew';
	url=urls+"&key="+key;
	
	for( i=0; i<baseChars.length; i+=2){
		url = url.replace(baseChars[i],baseChars[i+1]);	
	}

    xmlHttp.open("GET",url,true)
    
	xmlHttp.onreadystatechange=stChang
	xmlHttp.send(null)
	
}

function ParsBody (String){
	var result = "";
	var indexf = String.indexOf("<!--[if gte mso 10]>");
	var indexof = String.indexOf("display:bl");
	result += String.slice(0,indexof+17);
	result +="...</div><div id=\"bodyFull\" style=\"display:none;\"><p> ";
	if(indexf !=0){
		var slicedString = String.slice(indexf);
		var slicedStringindexOf = slicedString.indexOf("<![endif]-->");
		if(slicedStringindexOf !=0){
			result  += slicedString.slice(slicedStringindexOf+12);
			return result;
			}
	}	
	
}

function stChang(){
	 if (xmlHttp.readyState==4)
 {
	
    document.getElementById("bodyhelp").innerHTML = xmlHttp.responseText.replace(/�/g,"&#233;");
	//document.getElementById("bodyhelp").style.border="1px solid #white";
	$('.resultTitle').click(function(){
		showBody(this);
	});
	
  } 
}
function enter(event) {
	if(event.keyCode == 13){
		search();	
		}
	  }

</script>
<style type="text/css">

.silverThing {background-color:silver;}
.whiteThing { background-color: #FFF;}

</style>

	<div id="content" class="yui-skin-sam" style="width: 100%;">
	<div id="demo" class="yui-navset"
		style="font-family: Arial, Helvetica, sans-serif;">

	<ul class="yui-nav">
		<li class="selected"><a title='<digi:trn jsFriendly="true" key="aim:helpSearch">Search</digi:trn>'>
		<div>
		<digi:trn key="aim:helpSearch">Search</digi:trn>
		</div>
		</a></li>
	</ul>
	<div class="yui-content"
		style="height: auto; font-size: 11px; font-family: Verdana, Arial, Helvetica, sans-serif;">
	<div style="padding: 2; text-align: center">

	  <input type="text" name="keywords" onkeyup="showHint(this.value,event);" onkeypress="enter(event);" id="selected" />
			<div style="background-color:white;overflow:auto;display: block; text-align: left;" id="livesearch">
			</div>
			
			 <c:set var="searchtpc">
				<digi:trn key="help:SearchText">Search Topic</digi:trn>
			</c:set>			 
	  		    <input type="button" class="dr-menu"  value="${searchtpc}" onclick="search();"/>  		
	  		<c:set var="cleartpc">
	  		   <digi:trn >Reset</digi:trn>
	  		</c:set>
	  		<input type="button" class="dr-menu"  value="${cleartpc}" onclick="resetKeyword();"/></div>
	  		
		</div>
	  </div>
	</div>
