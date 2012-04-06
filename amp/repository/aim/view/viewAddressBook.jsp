<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="<digi:file src="module/aim/css/amptabs.css"/>"/>

<digi:instance property="addressbookForm" />
<digi:context name="digiContext" property="context" />

<style type="text/css">
	.jcol{												
		padding-left:10px;												 
	}
	.jlien{
		text-decoration:none;
	}
	.tableEven {
		background-color:#dbe5f1;
		font-size:8pt;
		padding:2px;
	}

	.tableOdd {
		background-color:#FFFFFF;
		font-size:8pt !important;
		padding:2px;
	}
		 
	.Hovered {
		background-color:#a5bcf2;
	}
    .pagination {
           float:left;   padding:3px;border:1px solid #999999;
    }	
</style>

<!-- for auto complete -->
<style type="text/css">
<!--

.yui-skin-sam .yui-ac{position:relative;font-family:arial;font-size:100%;}
.yui-skin-sam .yui-ac-input{position:absolute;width:100%;}
.yui-skin-sam .yui-ac-container{position:absolute;top:1.6em;width:100%;}
.yui-skin-sam .yui-ac-content{position:absolute;width:100%;border:1px solid #808080;background:#fff;overflow:hidden;z-index:9050;}
.yui-skin-sam .yui-ac-shadow{position:absolute;margin:.3em;width:100%;background:#000;-moz-opacity:.10;opacity:.10;filter:alpha(opacity=10);z-index:9049;}
.yui-skin-sam .yui-ac iframe{opacity:0;filter:alpha(opacity=0);padding-right:.3em;padding-bottom:.3em;}
.yui-skin-sam .yui-ac-content ul{margin:0;padding:0;width:100%;}
.yui-skin-sam .yui-ac-content li{margin:0;padding:2px 5px;cursor:default;white-space:nowrap;list-style:none;zoom:1;}
.yui-skin-sam .yui-ac-content li.yui-ac-prehighlight{background:#B3D4FF;}
.yui-skin-sam .yui-ac-content li.yui-ac-highlight{background:#426FD9;color:#FFF;}


#myContainer .yui-ac-content { 
    max-height:16em;overflow:auto;overflow-x:hidden; /* set scrolling */ 
    _height:16em; /* ie6 */ 
}

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
    z-index:3; /* z-index needed on top instance for ie & sf absolute inside relative issue */
}
#myInput,
#myInput2 {
    _position:absolute; /* abs pos needed for ie quirks */
}

#myAutoComplete {
    width:320px; /* set width here or else widget will expand to fit its container */
    padding-bottom:2em;
}
#myImage {
    position:absolute; left:320px; margin-left:1em; /* place the button next to the input */
}
-->
</style>

<!-- tabs styles -->
<style type="text/css">


#tabs {
	font-family: Arial,Helvetica,sans-serif;
	font-size: 8pt;
	clear: both;
	text-align: center;
}

#tabs ul {
	display: inline;
	list-style-type: none;
	margin: 0;
	padding: 0;
}

#tabs li { 
	 float: left;
}



#tabs a, #tabs span { 
	font-size: 8pt;
}

#tabs ul li a { 
	background:#222E5D url(/TEMPLATE/ampTemplate/images/tableftcorner.gif) no-repeat scroll left top;
	color:#FFFFFF;
	float:left;
	margin:0pt 0px 0pt 0pt;
	position:relative;
	text-decoration:none;
	top:0pt;

}

#tabs ul li a div { 
	background: url(/TEMPLATE/ampTemplate/images/tabrightcorner.gif) right top no-repeat;
	padding: 4px 10px 4px 10px;
}

#tabs ul li span a { 
	background:#3754A1 url(/TEMPLATE/ampTemplate/images/tableftcornerunsel.gif) no-repeat scroll left top;
	color:#FFFFFF;
	float:left;
	margin:0pt 0px 0pt 0pt;
	position:relative;
	text-decoration:none;
	top:0pt;

}

#tabs ul li span a div { 
	background: url(/TEMPLATE/ampTemplate/images/tabrightcornerunsel.gif) right top no-repeat;
	padding: 4px 10px 4px 10px;
}

#tabs a:hover {
    background: #455786 url(/TEMPLATE/ampTemplate/images/tableftcornerhover.gif) left top no-repeat;  
}

#tabs a:hover span {
    background: url(/TEMPLATE/ampTemplate/images/tabrightcornerhover.gif) right top no-repeat;  
}
#tabs a:hover div {
    background: url(/TEMPLATE/ampTemplate/images/tabrightcornerhover.gif) right top no-repeat;  
}

#tabs a.active {
	position: relative;
	top: 0;
	margin: 0 2px 0 0;
	float: left;
	background: #FFF3B3;
	padding: 4px 10px 4px 10px;
	text-decoration: none;
	color: #333;
}

#tabs a.active:hover {
	position: relative;
	top: 0;
	margin: 0 2px 0 0;
	float: left;
	background: #FFF3B3;
	padding: 6px 10px 6px 10px;
	text-decoration: none;
	color: #333;
}

#main {
	clear:both;
	text-align: left;
	border-top: 2px solid #222E5D;
	border-left: 1px solid #222E5D;
	border-right: 1px solid #222E5D;
    border-bottom: 1px solid #222E5D;
	padding: 2px 4px 2px 4px;
}
</style>

<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/datatable.css" />
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/paginator.css" />


<!-- Individual YUI JS files --> 

	<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/new/yahoo-dom-event.js"></script>
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/new/connection-min.js"></script>
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/new/element-min.js"></script>
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/new/yahoo-min.js"></script>
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/new/datatable-min.js"></script>
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/new/json-min.js"></script>
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/new/event-min.js"></script>
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/new/paginator-min.js"></script>
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/new/container-min.js"></script>
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/new/datasource-min.js"></script>
  

	<style>
		.yui-skin-sam .yui-dt th, .yui-skin-sam .yui-dt th a {
		color:#000000;
		font-weight:bold;
		font-size: 11px;
		text-decoration:none;
		vertical-align:bottom;
		}
		.yui-skin-sam th.yui-dt-asc, .yui-skin-sam th.yui-dt-desc {
		background:#B8B8B0;
		}
		.yui-skin-sam .yui-dt th {
		background:#B8B8B0;
		}
		.yui-skin-sam th.yui-dt-asc .yui-dt-liner {
		background:transparent url(/repository/aim/images/up.gif) no-repeat scroll right center;
		}
		.yui-skin-sam th.yui-dt-desc .yui-dt-liner {
		background:transparent url(/repository/aim/images/down.gif) no-repeat scroll right center;
		}

	</style>
	
<script language="JavaScript">
	var msgDataError = '<digi:trn>Data error</digi:trn>';
	var msgLoading	 = '<digi:trn>Loading...</digi:trn>';
	var noData               = '<digi:trn>No Records found</digi:trn>';
	
	YAHOO.util.Event.addListener(window, "load", initDynamicTable1);
		function initDynamicTable1() {	
				
		    YAHOO.example.XHR_JSON = new function() {

		    	this.formatActions = function(elCell, oRecord, oColumn, sData) {
		        	elCell.innerHTML =
		        		"<a href=/aim/addressBook.do?actionType=editContact&contactId=" +oRecord.getData( 'ID' )+" title='<digi:trn>Click here to Edit Workspace</digi:trn>'>" + "<img vspace='2' border='0' src='/TEMPLATE/ampTemplate/imagesSource/common/application_edit.png'/>" + "</a>&nbsp;&nbsp;&nbsp;&nbsp;"+
		            	"<a onclick='return confirmDelete()' href=/aim/addressBook.do?actionType=deleteContact&contactId=" +oRecord.getData( 'ID' )+" title='<digi:trn>Click here to Delete Workspace</digi:trn>'>" + "<img vspace='2' border='0' src='/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif'/>" + "</a>" 		        	
			        	
		        };		        
		        
		        var lastTimeStamp = new Date().getTime();

		        this.myDataSource = new YAHOO.util.DataSource("/aim/addressBook.do?actionType=getContactsJSON&lastTimeStamp"+lastTimeStamp+"&");
		        this.myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
		        //this.myDataSource.connXhrMode = "queueRequests";
		        this.myDataSource.responseSchema = {
		            resultsList: "contacts",
		            fields: ["ID","title","name","email","organizations","function","phones","faxes"],
		            metaFields: {
		            	totalRecords: "totalRecords" // Access to value in the server response
		        	}    
		        };        
		        
		        var myColumnDefs = [
		            {key:"title", label:"<digi:trn>TITLE</digi:trn>", sortable:true},
		            {key:"name", label:"<digi:trn>NAME</digi:trn>", sortable:true},
		            {key:"email", label:"<digi:trn>EMAIL</digi:trn>", sortable:false},
		            {key:"organizations", label:"<digi:trn>ORGANIZATIONS</digi:trn>", sortable:false},
		            {key:"function", label:"<digi:trn>FUNCTION</digi:trn>", sortable:true},
		            {key:"phones", label:"<digi:trn>PHONE</digi:trn>", sortable:false},
		            {key:"faxes", label:"<digi:trn>FAX</digi:trn>", sortable:false},
		            {key:"actions", label:"<digi:trn>ACTIONS</digi:trn>", width: 65, formatter:this.formatActions,className:"ignore"}
		        ];
		  
		        var div = document.getElementById('errors');
		
		        var handleSuccess = function(o){
		        	if(o.responseText != undefined){
		        		o.argument.oArgs.liner_element.innerHTML=o.responseText;
		        	}
		        }
		
		        var handleFailure = function(o){
		        	if(o.responseText != undefined){
		        		div.innerHTML = "<li>Transaction id: " + o.tId + "</li>";
		        		div.innerHTML += "<li>HTTP status: " + o.status + "</li>";
		        		div.innerHTML += "<li>Status code message: " + o.statusText + "</li>";
		        	}
		        }
		        // Create the Paginator 
		        var myPaginator = new YAHOO.widget.Paginator({ 
		        	rowsPerPage:10,
		        	//totalRecords:document.getElementById("totalResults").value,
		        	containers : ["dt-pag-nav"], 
		        	template : "<digi:trn>Results:</digi:trn>{RowsPerPageDropdown}<br/>{FirstPageLink} {PreviousPageLink} {PageLinks} {NextPageLink} {LastPageLink}&nbsp;&nbsp;{CurrentPageReport}", 
		        	pageReportTemplate		: "<digi:trn>Showing items</digi:trn> {startIndex} - {endIndex} <digi:trn>of</digi:trn> {totalRecords}", 
		        	rowsPerPageOptions		: [10,25,50,100,{value:999999,text:'<digi:trn jsFriendly="true">All</digi:trn>'}],
		        	firstPageLinkLabel		: '<< <digi:trn jsFriendly="true">first</digi:trn>',
		        	previousPageLinkLabel	: '< <digi:trn jsFriendly="true">prev</digi:trn>',
		        	nextPageLinkLabel		: '<digi:trn jsFriendly="true">next</digi:trn> >',
		        	lastPageLinkLabel		: '<digi:trn jsFriendly="true">last</digi:trn> >>'
		        });
		         
		        var myConfigs = {
		            initialRequest: "sort=name&dir=asc&startIndex=0&results=10", // Initial request for first page of data
		            dynamicData: true, // Enables dynamic server-driven data
		            sortedBy : {key:"name", dir:YAHOO.widget.DataTable.CLASS_ASC}, // Sets UI initial sort arrow
		            //paginator: new YAHOO.widget.Paginator({ rowsPerPage:10 }) // Enables pagination
		            paginator:myPaginator,
		            MSG_ERROR:msgDataError,
		            MSG_LOADING:msgLoading,
		            MSG_EMPTY:noData
		        };
		    	 
		        this.myDataTable = new YAHOO.widget.DataTable("dynamicdata", myColumnDefs, this.myDataSource, myConfigs);
		        this.myDataTable.subscribe("rowMouseoverEvent", this.myDataTable.onEventHighlightRow); 
		        this.myDataTable.subscribe("rowMouseoutEvent", this.myDataTable.onEventUnhighlightRow);
		        this.myDataTable.subscribe("rowClickEvent", this.myDataTable.onEventSelectRow);
		        
		 		this.myDataTable.subscribe("rowClickEvent", function (ev) {
						var target = YAHOO.util.Event.getTarget(ev);
						var record = this.getRecord(target);
					});
		        
		        this.myDataTable.selectRow(this.myDataTable.getTrEl(0)); 
		        // Programmatically bring focus to the instance so arrow selection works immediately 
		        this.myDataTable.focus(); 
		
		        // Update totalRecords on the fly with value from server
		        this.myDataTable.handleDataReturnPayload = function(oRequest, oResponse, oPayload) {
		           oPayload.totalRecords = oResponse.meta.totalRecords;
		           return oPayload;
		       }

				//further lines are for generating tooltips
		        var showTimer,hideTimer;				
		        var tt = new YAHOO.widget.Tooltip("myTooltip");
				
		        this.myDataTable.on('cellMouseoverEvent', function (oArgs) {
					if (showTimer) {
						window.clearTimeout(showTimer);
						showTimer = 0;
					}

					var target = oArgs.target;
					var column = this.getColumn(target);
					if (column.key == 'name' || column.key == 'email' || column.key == 'organizations' || column.key == 'phones' || column.key == 'faxes') {
						var record = this.getRecord(target);
						var tooltipText = record.getData(column.key);

                        if(tooltipText!=null && tooltipText.length > 0){
                            var event=oArgs.event;
                            var x = 0;
                            var y = 0;

                            if (document.all) { //IE
                                x = event.clientX + document.body.scrollLeft;
                                y = event.clientY + document.body.scrollTop;
                            }
                            else {
                                x = event.pageX;
                                y = event.pageY;
                            }
							var xy = [x,y];

							showTimer = window.setTimeout(function() {
								tt.setBody(tooltipText);
                                tt.cfg.setProperty('xyoffset',[10,10]);
                                tt.cfg.setProperty('xy',xy);
								tt.show();
								hideTimer = window.setTimeout(function() {
									tt.hide();
								},5000);
							},500);
								
						}
						
					}
				});
				
		        this.myDataTable.on('cellMouseoutEvent', function (oArgs) {
					if (showTimer) {
						window.clearTimeout(showTimer);
						showTimer = 0;
					}
					if (hideTimer) {
						window.clearTimeout(hideTimer);
						hideTimer = 0;
					}
					tt.hide();
				});
				       
		       
		    };
	    
		}
		
</script>



<script type="text/javascript">
	function createContact(){
		addressbookForm.action="${contextPath}/aim/addressBook.do?actionType=addContact";
		addressbookForm.target = "_self";
		addressbookForm.submit();
	}

	function confirmDelete(){
		var msg='<digi:trn>Are you sure you want to delete contact ?</digi:trn>';
		return confirm(msg);
	}

	function resetSearch(){
		addressbookForm.action="${contextPath}/aim/addressBook.do?actionType=viewAddressBook&reset=true";
		addressbookForm.target = "_self";
		addressbookForm.submit();
		return true;
	}

	function clearKeyword (){
		document.getElementById('myInput').value='';
		addressbookForm.action="${contextPath}/aim/addressBook.do?actionType=viewAddressBook&currentPage=1";
		addressbookForm.target = "_self";
		addressbookForm.submit();
	}

	function clearCurrentAlpha() {
		document.getElementById('alphaDropdown').value='viewAll';
		addressbookForm.action="${contextPath}/aim/addressBook.do?actionType=viewAddressBook&currentPage=1";
		addressbookForm.target = "_self";
		addressbookForm.submit();
	}

	function myPrint() {
		window.open('/addressBook.do?actionType=viewPrintPreview', '_blank', '');
		//addressbookForm.action="${contextPath}/aim/addressBook.do?actionType=viewPrintPreview";
		//addressbookForm.target = "_self";
		//addressbookForm.submit();
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

	

	// don't remove or change this line!!!
	document.getElementsByTagName('body')[0].className='yui-skin-sam';
</script>


	<table bgColor="#ffffff" cellPadding="0" cellSpacing="0" width="772">
		<tr>
			<td width="14">&nbsp;</td>
			<td align="left"vAlign="top" width="750">
				<table cellPadding="0" cellSpacing="0" width="879">
					<tr>
						<!-- Start Navigation -->
						<td height="33" colspan="7" width="867">
							<span class="crumb">
				              	<c:set var="translation">
									<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
								</c:set>
								<digi:link href="/showDesktop.do" styleClass="comment" title="${translation}">
									<digi:trn>Portfolio</digi:trn>
								</digi:link>&nbsp;&gt;&nbsp;
								<digi:trn>Address Book</digi:trn>
			              </span>
						</td>
						<!-- End navigation -->
					</tr>
					<tr><td align="left">
                    	<jsp:include page="/repository/aim/view/exportTable.jsp" />
                	</td></tr>					
					<tr>
						<td height="16" vAlign="center" width="867" colspan="7">
							<span class=subtitle-blue><digi:trn>Address Book</digi:trn></span>
						</td>
					</tr>
					<tr>
						<td height="100%">
							<DIV id="tabs">
								<UL>
							      	<LI>
							           	<a name="node">
							               	<div>
												<digi:trn>Existing Contacts</digi:trn>							
							                </div>
							            </a>
							        </LI>
									<LI>
							           	<span>
											<a href="${contextPath}/aim/addressBook.do?actionType=addContact">
							               		<div title='<digi:trn>Add New Contact</digi:trn>'>
													<digi:trn>Add New Contact</digi:trn>
							                    </div>
											</a>							
							            </span>
							        </LI>
								</UL>					
							</DIV>
							
						</td>
					</tr>					
					<tr>
						<td noWrap width=100% vAlign="top" height="100%">
						<digi:form action="/addressBook.do?actionType=viewAddressBook" method="post">
							<div id="main" style="width:100%;">
								<table bgColor="#ffffff" cellPadding="1" cellSpacing="1" width="100%" valign="top">
											<tr bgColor="#ffffff">
												<td vAlign="top" width="100%">
													<table width="100%" cellspacing="1" cellpadding="1" valign="top" align="left">
														<tr><td>&nbsp;</td></tr>
														<tr><td class="box-title" >
															<!-- Table title -->
															<div style="width:100%;">
																<table width="100%">
																	<tr>
																		<td width="5%">
																			<digi:trn>keyword:</digi:trn>
																		</td>
																		<td align="left" width="40%">
																			<div id="myAutoComplete">
																				<html:text property="keyword" styleId="myInput" style="width:320px;font-size:100%"></html:text>																																								
																		    	<div id="myContainer" style="width:315px;"></div>																		    	
																		    	<html:hidden property="keyword" styleId="myHidden"/>																			   																			    	
																		   	</div>
																		</td>
																		<td align="left">
																			<c:set var="trn">
																				<digi:trn>find</digi:trn>
																			</c:set>
																			<input type="submit" value="${trn}"  class="dr-menu" style="font-family:verdana;font-size:11px;" onclick="clearCurrentAlpha()"/>
																			
																			<digi:trn>Go To : </digi:trn>
																			<html:select property="currentAlpha" style="font-family:verdana;font-size:11px; margin-right:100px;" onchange="clearKeyword()" styleId="alphaDropdown">
																				<c:if test="${not empty addressbookForm.currentAlpha && addressbookForm.currentAlpha!='viewAll'}">
																					<html:option value="${addressbookForm.currentAlpha}">${addressbookForm.currentAlpha}</html:option>
																				</c:if>
																				<html:option value="viewAll"><digi:trn>-All-</digi:trn></html:option>
																				<c:if test="${not empty addressbookForm.alphaPages}">
																					<logic:iterate name="addressbookForm" property="alphaPages" id="alphaPages" type="java.lang.String">
																						<c:if test="${alphaPages != null}">
																							<html:option value="<%=alphaPages %>"><%=alphaPages %></html:option>
																						</c:if>
																					</logic:iterate>
																				</c:if>
																			</html:select>
																		</td>
																	</tr>
																</table>
															</div>
															<!-- end table title -->
														</td></tr>
										
														<tr><td>&nbsp;</td></tr>
														<tr>
															<td>
                                                                <div class='yui-skin-sam'>
									                            	<div id="dynamicdata" class="report"></div>                            	
																	<div id="dt-pag-nav"></div>
																	<div id="errors"></div>
                                                                    <div id="tooltipsCtx"></div>
																</div>
															</td>
														</tr>														
													</table>
												</td>
											</tr>
										</table>
								</div>
							</digi:form>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

<script type="text/javascript" language="JavaScript" src="<digi:file src="script/yui/new/datasource-min.js"/>"></script>
<script type="text/javascript" language="JavaScript" src="<digi:file src="script/yui/new/autocomplete-min.js"/>"></script>


<script type="text/javascript">
	var myArray = [
		<c:forEach var="contName" items="${addressbookForm.contactNames}">
		 {name: "<c:out value='${contName}'/>"},
		</c:forEach>     
	];

	YAHOO.example.ItemSelectHandler = function() {
	    // Use a LocalDataSource
	    var oDS = new YAHOO.util.LocalDataSource(myArray);
	    oDS.responseSchema = {fields : ["name"]};

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
	        
	        // update hidden form field with the selected item's fullname	        
	        myHiddenField.value = oData.name;
	    };	   
	    //oAC.itemSelectEvent.subscribe(myHandler);	    

	    return {
	        oDS: oDS
	      
	    };
	}();    


	setHoveredTable("dataTable", false);
</script>

