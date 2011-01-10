<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>




<digi:instance property="addressbookForm" />
<digi:context name="digiContext" property="context" />

<link type="text/css" rel="stylesheet" href="/TEMPLATE/ampTemplate/js_2/yui/datatable/assets/skins/sam/datatable.css">
<link type="text/css" rel="stylesheet" href="/TEMPLATE/ampTemplate/css_2/desktop_yui_tabs.css">
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/tabview-core.css"> 



<style>
		.yui-skin-sam .yui-dt th, .yui-skin-sam .yui-dt th a {
		color:#000000;
		font-weight:bold;
		font-size: 11px;
		text-decoration:none;
		vertical-align:bottom;
		}
	
		.yui-skin-sam th.yui-dt-asc .yui-dt-liner {
		background:transparent url(/repository/aim/images/up.gif) no-repeat scroll right center;
		}
		.yui-skin-sam th.yui-dt-desc .yui-dt-liner {
		background:transparent url(/repository/aim/images/down.gif) no-repeat scroll right center;
		}
		.yui-skin-sam .yui-dt td {
		color:#000000;
		font-size: 11px;
		text-decoration:none;
		vertical-align:bottom;
		}


	</style>

<!-- for auto complete -->
<style type="text/css">


.autocompleteClass .yui-ac{position:relative;font-family:arial;font-size:100%;}
.autocompleteClass .yui-ac-input{position:absolute;width:100%;}
.autocompleteClass .yui-ac-container{position:absolute;top:1.6em;width:100%;}
.autocompleteClass .yui-ac-content{position:absolute;width:100%;border:1px solid #808080;background:#fff;overflow:hidden;z-index:9050;}
.autocompleteClass .yui-ac-shadow{position:absolute;margin:.3em;width:100%;background:#000;-moz-opacity:.10;opacity:.10;filter:alpha(opacity=10);z-index:9049;}
.autocompleteClass .yui-ac iframe{opacity:0;filter:alpha(opacity=0);padding-right:.3em;padding-bottom:.3em;}
.autocompleteClass .yui-ac-content ul{margin:0;padding:0;width:100%;}
.autocompleteClass .yui-ac-content li{margin:0;padding:2px 5px;cursor:default;white-space:nowrap;list-style:none;zoom:1;}
.autocompleteClass .yui-ac-content li.yui-ac-prehighlight{background:#B3D4FF;}
.autocompleteClass .yui-ac-content li.yui-ac-highlight{background:#426FD9;color:#FFF;}


#myContainer .yui-ac-content { 
    max-height:16em;overflow:auto;overflow-x:hidden; /* set scrolling */ 
    _height:16em; /* ie6 */ 
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


</style>





<!-- Individual YUI JS files --> 

<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/datasource/datasource-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo/yahoo-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/event/event-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/json-min.js"></script>
 

 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/datasource/datasource-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/autocomplete/autocomplete-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/paginator/paginator-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/datatable/datatable-min.js"></script>








 

 

	
  


	
<script language="JavaScript">
	var msgDataError = '<digi:trn>Data error</digi:trn>';
	var msgLoading	 = '<digi:trn>Loading...</digi:trn>';
	
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
		        	template : "<digi:trn>Results:</digi:trn>{RowsPerPageDropdown}<br/>[{FirstPageLink} {PreviousPageLink}] {PageLinks} {NextPageLink} {LastPageLink}&nbsp;&nbsp;{CurrentPageReport}", 
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
		            MSG_LOADING:msgLoading
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



	

	// don't remove or change this line!!!
	document.getElementsByTagName('body')[0].className='autocompleteClass';
</script>
<table width="1000" align="center" border="0" cellpadding="0" cellspacing="0">	
<tbody>
<tr>
<td valign="top" align="left">
<div style="padding-left: 10px; width: 98%; min-width: 680px;"   class="yui-skin-sam" id="content"> 
<div id="demo"  class="yui-navset yui-navset-top ">
<ul  class="yui-nav"> 
	<li class="selected">
	<a>
	<div>
	<digi:trn>Existing Contacts</digi:trn>
	</div>
	</a>
	</li>
	<li>
	<a href="${contextPath}/aim/addressBook.do?actionType=addContact">
	<div title='<digi:trn>Add New Contact</digi:trn>'>
		<digi:trn>Add New Contact</digi:trn>
	</div>
	</a>		
	</li>
</ul>
<div class="yui-content resource_popin" style="border-color: rgb(208, 208, 208);">
<jsp:include page="/repository/aim/view/exportTable.jsp" />
<table>
<tbody>
<tr>
						<td noWrap vAlign="top">
						<digi:form action="/addressBook.do?actionType=viewAddressBook" method="post">

								<table bgColor="#ffffff" align="center" cellPadding="1" cellSpacing="1" width="95%" valign="top">
											<tr bgColor="#ffffff">
												<td vAlign="top" width="100%">
													
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
							</digi:form>
						</td>
					</tr>
</tbody>
</table>
</div>
</div>
</div>
</td>
</tr>
					
					</tbody>
</table>
	





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

</script>

