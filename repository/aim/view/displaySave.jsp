<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %> 
<%@ taglib uri="/taglib/category" prefix="category" %>
            


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/dhtml-suite-for-applications.js"/>"></script>


<script type="text/javascript">
<!--

function validate(){
	mySaveEngine.saveContract();
	return true;
}



function addDisb() {
	var postString		= "addFields=true&"+generateFields();
	YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/editIPAContract.do", callback, postString);
}

function orgsAdded() {
	var postString		= generateFields();
	YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/editIPAContract.do", callback, postString);
}

function delOrgs() {
	var postString		= "removeOrgs=true&" + getCheckedFields("selOrgs")+"&"+generateFields();
	YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/editIPAContract.do", callback, postString);	
}

function getCheckedFields(name) {
	var ret			= "";
	//var ulEl		= document.getElementById( ulId );
	//var fields		= ulEl.getElementsByTagName( "input" );
	
	var elems = document.getElementsByName(name);
	
	for ( var i=0; i<elems.length; i++ ) {
		if (elems[i].checked){
			ret += name+"=";
			ret +=elems[i].value;//"true";
			if ( i < elems.length-1 )
				ret += "&";
		}
		//else
		//    ret +="false";
	}
	return ret;	
}

function initScripts(){
		
}

window.onload=initScripts;
-->
</script>

<!-- code for rendering that nice calendar -->


<body onload="initScripts()">
<digi:instance property="aimEditActivityForm" />

<input type="hidden" name="edit" value="true">

<digi:errors/>

<table width="100%" border="0" cellspacing="2" cellpadding="2" align="center" class="box-border-nopadding">
	<tr>
		<td>
			<br/><br/><br/><br/><br/><br/><p align="center"><img align="top" src="/repository/aim/view/scripts/ajaxtabs/save-loader.gif" /></p>
			<p align="center"><b><digi:trn key="aim:savePopup:title">Saving</digi:trn>...</b></p>
			<br/><br/><br/><br/><br/><br/>
		</td>
	</tr>
</table>
