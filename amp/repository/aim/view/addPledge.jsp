<%@page import="org.digijava.kernel.translator.TranslatorWorker"%>
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm, org.digijava.module.categorymanager.dbentity.AmpCategoryValue, java.util.*, org.digijava.module.aim.dbentity.*, org.springframework.beans.BeanWrapperImpl" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>

<!--<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>-->
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<% int indexFund = 0; 
PledgeForm pledgeForm = (PledgeForm) session.getAttribute("pledgeForm");
%>
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script src="/repository/bootstrap/hacks.js"></script>
<script language="JavaScript" type="text/javascript"><!--

//alert('this module is a work-in-progress and is pending (almost) full rewrite in this week. Do not bugfix or add features to it, because everything will be deleted anyway');
var quitRnot1 = 0;


function cancel(){
	<digi:context name="cancel" property="/savePledge.do" />
	document.pledgeForm.action = "<%=cancel%>?cancel=true";
	document.pledgeForm.target = "_self";

	document.pledgeForm.submit();
}


document.getElementsByTagName('body')[0].className='yui-skin-sam';

--></script>

<digi:instance property="pledgeForm" />

<digi:form action="/addPledge.do" method="post">

<iframe src="/aim/selectPledgeLocation.do?edit=false" width="100%" scrolling="no" seamless="seamless" frameborder="0" marginheight="0" marginwidth="0" name="pledges_locations_name"></iframe>

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="1000" vAlign="top" align="center" border="0">
	
	<tr>
		<td align=left valign="top">
			<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top" align="left">
				<tr><td>
					<span style="font-family: Tahoma;font-size: 11px;"><digi:errors/></span>
					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top" border="0">
						<tr><td width="75%" vAlign="top">
						<table cellpadding="0" cellspacing="0" width="100%" border="0">
							<tr><td width="100%" bgcolor="#FF0000" style="background-color:#F5F5F5; border:1px solid #CCCCCC; padding:15px;">

							<table width="100%"vAlign="top" align="left" bgcolor="#C7D4DB">

							<tr><td bgColor=#ffffff align="center" vAlign="top">
								<!-- contents -->
								<!-- bootstrap part of form -->
<%--								<iframe src="/aim/selectPledgeLocation.do?edit=false" width="100%" scrolling="no" seamless="seamless" frameborder="0" marginheight="0" marginwidth="0" name="pledges_locations_name"></iframe>  --%> 										   
								</td>																					
								<table width="95%" border="0">
									<tr>
										<td align="right" width="50%">
											<html:button styleClass="dr-menu" property="submitButton" onclick="return savePledge()">
		                                         <digi:trn key="btn:savePlegde">Save Pledge</digi:trn>
											</html:button>
										</td>
										<td align="left" width="50%">
											<html:button styleClass="dr-menu" property="submitButton" onclick="return cancel()">
		                                         <digi:trn key="btn:cancel">Cancel</digi:trn>
											</html:button>
										</td>
									</tr>
									<tr><td>&nbsp;</td></tr>
									<tr><td>&nbsp;</td></tr>
								</table>
								<!-- end contents -->
							</td></tr>
							</table>

							</td></tr>
						</table>
						</td>
					</tr>	
					</table>
				</td></tr>
			</table>
		</td>
	</tr>
</table>
<script type="text/javascript">


function savePledge() {

	if (validateData()){
		var i = 0;
		var param = "";
		while (i<=numFund){
			if(document.getElementById('fund_'+i)!=null){
				param += document.getElementsByName('fund_'+i+"_0")[0].value + "_";
				if (document.getElementsByName('fund_'+i+"_2")[0] == null){
					param += "-1_";
				}else{
					param +=  trim(""+document.getElementsByName('fund_'+i+"_2")[0].value) + "_";
				}
				if (document.getElementsByName('fund_'+i+"_3")[0] == null){
					param += "-1_";
				}else{
					param += trim(""+document.getElementsByName('fund_'+i+"_3")[0].value) + "_";
				}
				param += trim(""+document.getElementsByName('fund_'+i+"_4")[0].value) + "_";
				param += trim(""+document.getElementsByName('fund_'+i+"_5")[0].value) + "_";
				param += trim(""+document.getElementsByName('fund_'+i+"_6")[0].value) + "_";
				if (document.getElementsByName('fund_'+i+"_7")[0] == null){
					param += "-1_";
				}else{
					param += trim(""+document.getElementsByName('fund_'+i+"_7")[0].value) + "_";
				}
				param += ";";
			}
			i++;
		}
		
		<digi:context name="save" property="/savePledge.do" />
  	 	document.pledgeForm.action = "<%=save%>?fundings="+param;
  	  	document.pledgeForm.target = "_self";

    	document.pledgeForm.submit();
	}
}

function trim(stringToTrim) {
	return stringToTrim.replace(/^\s+|\s+$/g,"");
}

var setFocus = null;

function highligthObject(object,on){
	if (on){
		if (setFocus==null)
			setFocus=object;
		object.style.borderColor="#FF0000";
		object.style.borderWidth="3px";
		setFocus.focus();
	} else {
		object.style.borderColor="#CCCCCC";
		object.style.borderWidth="1px";
	}
}

function validateData(){
	var errors = false;
	setFocus = null;
	if ((document.getElementsByName("pledgeTitleId")[0]==null || document.getElementsByName("pledgeTitleId")[0].value==-1 || 
			document.getElementsByName("pledgeTitleId")[0].value==0)&& (document.getElementsByName("titleFreeText")[0].value==null)){
		highligthObject(document.getElementById("pledgeTitleDropDown"),true);
		errors = true;
	} else {
		if (document.getElementById("pledgeTitleDropDown")){
			highligthObject(document.getElementById("pledgeTitleDropDown"),false);
		}else {
			highligthObject(document.getElementById("titleFreeText"),false);
		}
	}

	if (document.getElementById("org_grp_dropdown_id")==null || document.getElementById("org_grp_dropdown_id").value==-1){
		highligthObject(document.getElementById("org_grp_dropdown_id"),true);
		errors = true;
	} else {
		highligthObject(document.getElementById("org_grp_dropdown_id"),false);
	}
		
	i = 0;
	while (i<=numFund){
		if (document.getElementsByName("fund_"+i+"_4")[0]!=null){
			var temp = 0;
			temp = temp + document.getElementsByName("fund_"+i+"_4")[0].value;
			if (document.getElementsByName("fund_"+i+"_4")[0].value.length==0 || temp==0){
				highligthObject(document.getElementsByName("fund_"+i+"_4")[0],true);
				errors = true;
			} else {
				var tmp = replaceAll(document.getElementsByName("fund_"+i+"_4")[0].value, " ", "");
				if (isNaN(tmp)){
					highligthObject(document.getElementsByName("fund_"+i+"_4")[0],true);
					errors = true;
				} else {
					highligthObject(document.getElementsByName("fund_"+i+"_4")[0],false);
				}
			}
		}
		i++;
	}
	
	i = 0;
	while (i<=numFund){
		if (document.getElementsByName("fund_"+i+"_5")[0]!=null){
			var temp = 0;
			temp = document.getElementsByName("fund_"+i+"_5")[0].value;
			if (temp==-1){
				highligthObject(document.getElementsByName("fund_"+i+"_5")[0],true);
				errors = true;
			} else {
				highligthObject(document.getElementsByName("fund_"+i+"_5")[0],false);
			}
		}
		i++;
	}
	

	<c:set var="checkErrors">
	  <digi:trn>
	  	Please, check values highlighted in red.
	  </digi:trn>
	</c:set>
	if (errors){
		alert ("${checkErrors}")
		return false;
	}
	else
		return true
}

function replaceAll(str, find, replace){
	  while (str.toString().indexOf(find) != -1)
	      str = str.toString().replace(find,replace);
	  return str;
	}


</script>

</digi:form>
