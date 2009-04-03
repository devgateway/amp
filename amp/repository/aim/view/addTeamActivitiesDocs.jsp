<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script type="text/javascript" language="JavaScript" src="<digi:file src="module/message/script/autocomplete.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.charcounter.js"/>"></script>

<style>

<style  type="text/css">
<!--

.contentbox_border{
        border: 1px solid black;
	border-width: 1px 1px 1px 1px; 
	background-color: #ffffff;
}

#myAutoComplete ul {
	list-style: square;
	padding-right: 0px;
	padding-bottom: 2px;
}

#myAutoComplete div {
	padding: 0px;
	margin: 0px; 
}

#myAutoComplete,
#myAutoComplete2 {
    width:15em; /* set width here */
    padding-bottom:2em;
}
#myAutoComplete {
    z-index:9000; /* z-index needed on top instance for ie & sf absolute inside relative issue */
}
#myInput,
#myInput2 {
    _position:absolute; /* abs pos needed for ie quirks */
}
.charcounter {
    display: block;
}

#myAutoComplete {
    width:320px; /* set width here or else widget will expand to fit its container */
    padding-bottom:2em;
}

.yui-skin-sam .yui-ac{position:relative;font-family:arial;font-size: 100%}
.yui-skin-sam .yui-ac-input{position:absolute;width:100%;font-size: 100%}
.yui-skin-sam .yui-ac-container{position:absolute;top:1.6em;width:100%;}
.yui-skin-sam .yui-ac-content{position:absolute;width:100%;border:1px solid #808080;background:#fff;overflow:hidden;z-index:9050;}
.yui-skin-sam .yui-ac-shadow{position:absolute;margin:.3em;width:100%;background:#000;-moz-opacity:0.10;opacity:.10;filter:alpha(opacity=10);z-index:9049;}
.yui-skin-sam .yui-ac-content ul{margin:0;padding:0;width:100%;}
.yui-skin-sam .yui-ac-content li{margin:0;padding:2px 5px;cursor:default;white-space:nowrap;FONT-SIZE: 100%;}
.yui-skin-sam .yui-ac-content li.yui-ac-prehighlight{background:#B3D4FF;}
.yui-skin-sam .yui-ac-content li.yui-ac-highlight{background:#426FD9;color:#FFF;}

#statescontainer .yui-ac-content { 
    max-height:16em;overflow:auto;overflow-x:hidden; /* set scrolling */ 
    _height:16em; /* ie6 */ 
} 

</style>

<style>
.contentbox_border{
	border: 	1px solid #666666;
	width: 		750px;
	background-color: #f4f4f2;
}
</style>
<style>

.tableEven {
	background-color:#dbe5f1;
	font-size:8pt;
	padding:2px;
}

.tableOdd {
	background-color:#FFFFFF;
	font-size:8pt;!important
	padding:2px;
}
 
.Hovered {
	background-color:#a5bcf2;
}
</style>

<script language="javascript">
function setStripsTable(tableId, classOdd, classEven) {
	var tableElement = document.getElementById(tableId);
	if(tableElement)
	{
		rows = tableElement.getElementsByTagName('tr');
		for(var i = 0, n = rows.length; i < n; ++i) {
			if(i%2 == 0)
				rows[i].className = classEven;
			else
				rows[i].className = classOdd;
		}
		rows = null;
	}
}
function setHoveredTable(tableId, hasHeaders) {

	var tableElement = document.getElementById(tableId);
	if(tableElement){
    var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        rows      = tableElement.getElementsByTagName('tr');

		for(var i = 0, n = rows.length; i < n; ++i) {
			rows[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			rows[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		rows = null;
	}
}
</script>

<script type="text/javascript">
	function checkall() {
		var selectbox = document.aimTeamActivitiesForm.checkAll;
		var items = document.aimTeamActivitiesForm.selActDocuments;
		if (items != null) {
			if (document.aimTeamActivitiesForm.selActDocuments.checked == true || 
								 document.aimTeamActivitiesForm.selActDocuments.checked == false) {
					  document.aimTeamActivitiesForm.selActDocuments.checked = selectbox.checked;
			} else {
				for(i=0; i<items.length; i++){
					document.aimTeamActivitiesForm.selActDocuments[i].checked = selectbox.checked;
				}
			}				  
		}
	}

function checkSelActDocuments() {
	if (document.aimTeamActivitiesForm.selActDocuments.checked != null) { 
		if (document.aimTeamActivitiesForm.selActDocuments.checked == false) {
			alert("Please choose a document to add");
			return false;
		}
	} else { // 
		var length = document.aimTeamActivitiesForm.selActDocuments.length;	  
		var flag = 0;
		for (i = 0;i < length;i ++) {
			if (document.aimTeamActivitiesForm.selActDocuments[i].checked == true) {
				flag = 1;
				break;
			}
		}

		if (flag == 0) {
			alert("Please choose a document to add");
			return false;					  
		}
	}
	return true;
}	


function validate() {
	<c:set var="message">
    <digi:trn>Please choose a document to add to the activity</digi:trn>
    </c:set>
    <c:set var="message2">
    <digi:trn>Please choose an activity</digi:trn>
    </c:set>
    if (document.aimTeamActivitiesForm.selectedAct.value == "") {
    	alert("${message2}");
    	document.aimTeamActivitiesForm.selectedAct.focus();
		return false;
    }
	if (document.aimTeamActivitiesForm.selActDocuments.checked != null) {
		if (document.aimTeamActivitiesForm.selActDocuments.checked == false) {
			alert("${message}");
			return false;
		}
	} else {
		var length = document.aimTeamActivitiesForm.selActDocuments.length;
		var flag = 0;
		for (i = 0; i < length; i++) {
			if (document.aimTeamActivitiesForm.selActDocuments[i].checked == true) {
				flag = 1;
				break;
			}
		}
		if (flag == 0) {
			alert("${message}");
			return false;
		}
	}
	return true;
}

	function sortMe(val) {
		<digi:context name="sel" property="context/module/moduleinstance/updateTeamActivity.do~showUnassignedDocs=true" />
			url = "<%= sel %>" ;
			
			var sval = document.aimTeamActivitiesForm.sort.value;
			var soval = document.aimTeamActivitiesForm.sortOrder.value;
			
			if ( val == sval ) {
				if (soval == "asc")
					document.aimTeamActivitiesForm.sortOrder.value = "desc";
				else if (soval == "desc")
					document.aimTeamActivitiesForm.sortOrder.value = "asc";	
			}
			else
				document.aimTeamActivitiesForm.sortOrder.value = "asc";

			document.aimTeamActivitiesForm.sort.value = val;
			document.aimTeamActivitiesForm.action = url;
			document.aimTeamActivitiesForm.submit();
	}

	// don't remove or change this line!!!
	document.getElementsByTagName('body')[0].className='yui-skin-sam';
</script>


<digi:instance property="aimTeamActivitiesForm" />
<digi:form action="/updateTeamActivity.do" method="post">

<html:hidden property="teamId" />
<html:hidden property="removeDocument" value="assign" />
<html:hidden property="sort" />
<html:hidden property="sortOrder" />
<html:hidden property="page" />

<table width="100%" cellPadding=0 cellSpacing=0 vAlign="top" align="left">
<tr><td width="100%" vAlign="top" align="left">
<jsp:include page="teamPagesHeader.jsp" flush="true" />
</td></tr>
<tr><td width="100%" vAlign="top" align="left">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=780>
	<tr>
		<td width=14>&nbsp;</td>
		<td align=left vAlign=top width=750>

			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:portfolio">
						Portfolio
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<c:set var="translation">
							<digi:trn key="aim:clickToConfigureTeam">Click here to Configure Team</digi:trn>
						</c:set>
						<digi:link href="/configureTeam.do" styleClass="comment" title="${translation}" >
							<digi:trn key="aim:configureTeam">Configure Team</digi:trn>
						</digi:link>
						&nbsp;&gt;&nbsp;						
						<c:set var="translation">
							<digi:trn key="aim:clickToViewActivityList">Click here to view Activity List</digi:trn>
						</c:set>
						<digi:link href="/teamActivityList.do" styleClass="comment" title="${translation}" >
							<digi:trn key="aim:activityList">Activity List</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn>
						Add Document
						</digi:trn>
					</td>
				</tr>
				<tr>
					<td height="16" vAlign="center" width="571"><span class=subtitle-blue><digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></span>
					</td>
				</tr>
				<tr>
					<td noWrap width=571 vAlign="top">
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%">
							<tr>
								<td vAlign="top" width="100%">
									<c:set var="selectedTab" value="2" scope="request"/>
									<c:set var="selectedSubTab" value="4" scope="request"/>
									<jsp:include page="teamSetupMenu.jsp" flush="true" />								
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
                                <div class="contentbox_border" style="border-top:0px;padding: 20px 0px 20px 0px;">
                                <div align="center">
									<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="98%">	
										<tr>
											<td bgColor=#ffffff valign="top">
												<table border=0 cellPadding=0 cellSpacing=0 width="100%">
													<tr>
														<td align="left" width="100%" valign="center">
															<table width="100%" cellSpacing=0 cellPadding=2 vAlign="top" align="left"
															bgcolor="#ffffff">
																<tr><td width=3 bgcolor="#999999">
																	<input type="checkbox" name="checkAll" onclick="checkall()">
																</td>
																<td width="20%" bgcolor="#999999" style="color:black">
																	<b><digi:trn>Title</digi:trn></b>
																</td>
																<td valign="center" align="center" bgcolor="#999999">
																	<a style="color:black" href="javascript:sortMe('filename')" title="Click here to sort by file name">
																		<b><digi:trn>File name</digi:trn></b>
																	</a>																
																</td>
																<td bgColor="#999999" align="center" width="20%">
																	<a  style="color:black" href="javascript:sortMe('type')" title="Click here to sort by Type">
																		<b><digi:trn>Type</digi:trn></b>
																	</a>																
																</td></tr>
															</table>
														</td>
													</tr>
													<logic:empty name="aimTeamActivitiesForm" property="documents">
													<tr>
														<td align="center">
															<digi:trn>No documents present</digi:trn>
														</td>
													</tr>	
													</logic:empty>

													<logic:notEmpty name="aimTeamActivitiesForm" property="documents">
													<tr>
														<td align="left" width="100%" valign="center">
															<table width="100%" cellSpacing=0 cellPadding=2 vAlign="top" align="left" id="dataTable">													
																<logic:iterate name="aimTeamActivitiesForm" property="documents" id="documents">
																<tr><td width=3>
																	<html:multibox  property="uuid" >
																		<bean:write name="documents" property="uuid" />
																	</html:multibox>	
																</td>
																<td width="20%">
																	<bean:write name="documents" property="title" />
																</td>
																<td>
																	<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																	<c:set target="${urlParams}" property="activityId">
																		<bean:write name="documents" property="ampActivityId" />
																	</c:set>
																	<c:set target="${urlParams}" property="pageId" value="4"/>
																	<bean:write name="documents" property="fileName" />
																</td>
																<td align="left" width="20%">
																	<bean:write name="documents" property="docType" />
																</td></tr>
																</logic:iterate>
															</table>
														</td>													
													</tr>
													<tr>
														<td>&nbsp;</td>
													</tr>
													<tr>
														<td align="center">
															<table cellspacing="5">
																<tr>
																	<td align="right" nowrap="nowrap" valign="top">
																		<digi:trn>New activity for the assigned documents :</digi:trn> 																	
																	</td>
																	<td align="left">
																		<div id="myAutoComplete">																			
																			<input type="text" style="width:320px;font-size:100%" id="myInput"/>
																		   	<div id="myContainer" style="width:315px;"></div>																		    	
																		</div>																    
																		<input id="myHidden" type="hidden" name="selectedActId">																		
																																
																		<html:submit  styleClass="dr-menu" property="submitButton"  onclick="return validate();">
																			<digi:trn>Add Document To Activity</digi:trn> 
																		</html:submit>																	
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													</logic:notEmpty>
												</table>
											</td>
										</tr>
										<logic:notEmpty name="aimTeamActivitiesForm" property="pages">
											<tr>
												<td>
													<digi:trn key="aim:pages">
														Pages :
													</digi:trn>
													<logic:iterate name="aimTeamActivitiesForm" property="pages" id="pages" 
													type="java.lang.Integer">
														<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParams1}" property="page">
															<%=pages%>
														</c:set>
																			    
														<bean:define id="currPage" name="aimTeamActivitiesForm" property="currentPage" />
														
														<% if (currPage.equals(pages)) { %>
																<%=pages%>
														<%	} else { %>
															<c:set var="translation">
																<digi:trn key="aim:clickToViewNextPage">Click here to goto Next Page</digi:trn>
															</c:set>
															<digi:link href="/updateTeamActivity.do" name="urlParams1" title="${translation}" >
																<%=pages%>
															</digi:link>
														<% } %>
														|&nbsp; 
													</logic:iterate>
												</td>
											</tr>
										</logic:notEmpty>	
									</table>
                                </div>
                                </div>
								</td>
							</tr>
							<tr><td bgColor=#f4f4f2>&nbsp;
								
							</td></tr>							
						</table>			
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</td></tr>
</table>

</digi:form>

<script language="javascript">
setStripsTable("dataTable", "tableEven", "tableOdd");
setHoveredTable("dataTable", false);
</script>

<script type="text/javascript">
var myArray = [
       		<c:forEach var="relAct" items="${aimTeamActivitiesForm.relatedActivities}">
       		 {name: "<bean:write name="relAct" property="name" filter="true"/>",id: <bean:write name="relAct" property="actId" filter="true"/>},
       		</c:forEach>     
       	];

       	YAHOO.example.ItemSelectHandler = function() {
       	    // Use a LocalDataSource
       	    var oDS = new YAHOO.util.LocalDataSource(myArray);
       	    oDS.responseSchema = {fields : ["name", "id"]};

       	    // Instantiate the AutoComplete
       	    var oAC = new YAHOO.widget.AutoComplete("myInput", "myContainer", oDS);
       	    oAC.resultTypeList = false;
       	    
       	    // Define an event handler to populate a hidden form field
       	    // when an item gets selected
       	    var myHiddenField = YAHOO.util.Dom.get("myHidden");
       	    var myHandler = function(sType, aArgs) {
       	        var myAC = aArgs[0]; // reference back to the AC instance
       	        var elLI = aArgs[1]; // reference to the selected LI element
       	        var oData = aArgs[2]; // object literal of selected item's result data
       	        
       	        // update hidden form field with the selected item's ID	        
       	        myHiddenField.value = oData.id;
       	    };	   
       	    oAC.itemSelectEvent.subscribe(myHandler);    
       	    

       	    return {
       	        oDS: oDS,
       	        oAC: oAC
       	    };
       	}();
        
        // attach character counters
        $("#titleMax").charCounter(50,{
	format: " (%1"+ " <digi:trn key="message:charactersRemaining">characters remaining</digi:trn>)",
	pulse: false});
        $("#descMax").charCounter(500,{
	format: " (%1"+ " <digi:trn key="message:charactersRemaining">characters remaining</digi:trn>)",
	pulse: false});
        

</script>
