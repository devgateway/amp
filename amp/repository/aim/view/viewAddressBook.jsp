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
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/tabview.css">

<style>

.yui-skin-sam .yui-dt thead th,.yui-skin-sam .yui-dt .yui-dt-data td {
	border-color: #CCC;
	border-style: none solid solid none;
	border-width: medium 1px 1px medium;
}


.yui-skin-sam .yui-dt  thead th {
	border-color: #FFF;
	background: #C7D4DB;
	color: #000;
	height: 30px;
	text-align: center;
}
.yui-dt-liner{
	font-size: 11px;
}

.yui-skin-sam .yui-dt th .yui-dt-liner {
	font-size: 12px;
	text-align: center;
	font-weight: bold;
	font-family: Arial, Verdana, Helvetica, sans-serif;
	border-color: #CCC;
}

.yui-skin-sam .yui-dt td .yui-dt-liner {
	font-size: 12px;
	font-family: Arial, Verdana, Helvetica, sans-serif;
}

.yui-skin-sam tr.yui-dt-odd,.yui-skin-sam tr.yui-dt-odd td.yui-dt-asc,.yui-skin-sam tr.yui-dt-odd td.yui-dt-desc
	{
	background-color: #dbe5f1;
}
.yui-skin-sam tr.yui-dt-even td.yui-dt-asc,.yui-skin-sam tr.yui-dt-even td.yui-dt-desc
	{
	background-color: #FFF;
}

.yui-skin-sam tr.yui-dt-highlighted td,.yui-skin-sam tr.yui-dt-highlighted td.yui-dt-asc,.yui-skin-sam tr.yui-dt-highlighted td.yui-dt-desc
	{
	background-color: #a5bcf2;
}

.yui-skin-sam .yui-dt th a.yui-dt-sortable{
	text-decoration: underline;
	color: #376091;
	font-weight: bold;
}

.yui-skin-sam a.yui-pg-page {
	padding-right: 10px;
	font-size: 11px;
	border-right: 1px solid rgb(208, 208, 208);
}

.yui-skin-sam .yui-pg-pages {
	border: 0px;
	padding-left: 0px;
}

.yui-pg-current-page {
	background-color: #FFFFFF;
	color: rgb(208, 208, 208);
	padding: 0px;
}

.current-page {
	background-color: #FF6000;
	color: #FFFFFF;
	margin-right: 5px;
	font-weight: bold;
}

.yui-pg-last {
	border: 0px
}

.yui-skin-sam span.yui-pg-first,.yui-skin-sam span.yui-pg-previous,.yui-skin-sam span.yui-pg-next,.yui-skin-sam span.yui-pg-last
	{
	display: none;
}

.yui-skin-sam a.yui-pg-first {
	margin-left: 2px;
	padding-right: 7px;
	border-right: 1px solid rgb(208, 208, 208);
}

.yui-tt {
	visibility: hidden;
	position: absolute;
	color: #000;
	background-color: #FFF;
	font-size: 11px;
	padding: 2px;
	border: 1px solid #CCC;
	width: auto;
}
</style>



<!-- for auto complete -->
<style type="text/css">


.autocompleteClass .yui-ac{position:relative;font-size:12px;}
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
	var msgDataError = '<digi:trn jsFriendly="true">Data error</digi:trn>';
	var msgLoading	 = '<digi:trn jsFriendly="true">Loading...</digi:trn>';
	var noData 		 = '<digi:trn jsFriendly="true">No Records found</digi:trn>';
	
	YAHOO.util.Event.addListener(window, "load", initDynamicTable1);
		function initDynamicTable1() {	
				
		    YAHOO.example.XHR_JSON = new function() {

		    	this.formatActions = function(elCell, oRecord, oColumn, sData) {
		        	elCell.innerHTML =
		        		"<a href=/aim/addressBook.do?actionType=editContact&contactId=" +oRecord.getData( 'ID' )+" title='<digi:trn jsFriendly="true">Click here to Edit Contact</digi:trn>'>" + "<img vspace='2' border='0' src='/TEMPLATE/ampTemplate/imagesSource/common/application_edit.png'/>" + "</a>&nbsp;&nbsp;&nbsp;&nbsp;"+
		            	"<a onclick='return confirmDelete()' href=/aim/addressBook.do?actionType=deleteContact&contactId=" +oRecord.getData( 'ID' )+" title='<digi:trn jsFriendly="true">Click here to Delete Contact</digi:trn>'>" + "<img vspace='2' border='0' src='/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif'/>" + "</a>" 		        	
			        	
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
		            {key:"title", label:"<digi:trn>Title</digi:trn>", sortable:true,className:"inside"},
		            {key:"name", label:"<digi:trn>Name</digi:trn>", sortable:true,className:"inside"},
		            {key:"email", label:"<digi:trn>Email</digi:trn>", sortable:false,className:"inside"},
		            {key:"organizations", label:"<digi:trn>Organisations</digi:trn>", sortable:false,className:"inside"},
		            {key:"function", label:"<digi:trn>Function</digi:trn>", sortable:true,className:"inside"},
		            {key:"phones", label:"<digi:trn>Phone</digi:trn>", sortable:false,className:"inside"},
		            {key:"faxes", label:"<digi:trn>Fax</digi:trn>", sortable:false,className:"inside"},
		            {key:"actions", label:"<digi:trn>Actions</digi:trn>", width: 65, formatter:this.formatActions,className:"ignore"}
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
		        	containers : ["dt-pag-nav","dt-pag-nav2"], 
		        	template : "{CurrentPageReport}&nbsp;<span class='l_sm'><digi:trn>Results:</digi:trn></span>&nbsp;{RowsPerPageDropdown}&nbsp;{FirstPageLink}{PageLinks}{LastPageLink}", 
		        	pageReportTemplate		: "<span class='l_sm'><digi:trn>Showing items</digi:trn></span> <span class='txt_sm_b'>{startRecord} - {endRecord} <digi:trn>of</digi:trn> {totalRecords}</span>", 
		        	rowsPerPageOptions		: [10,25,50,100,{value:999999,text:'<digi:trn jsFriendly="true">All</digi:trn>'}],
		        	firstPageLinkLabel : 	"<digi:trn>first page</digi:trn>", 
		        	previousPageLinkLabel : "<digi:trn>prev</digi:trn>", 
		        	firstPageLinkClass : "yui-pg-first l_sm",
		        	lastPageLinkClass: "yui-pg-last l_sm",
		        	nextPageLinkClass: "yui-pg-next l_sm",
		        	previousPageLinkClass: "yui-pg-previous l_sm",
		        	rowsPerPageDropdownClass:"l_sm",
		        	nextPageLinkLabel		: '<digi:trn jsFriendly="true">next</digi:trn>',
		        	lastPageLinkLabel		: '<digi:trn jsFriendly="true">last page</digi:trn>',
		        	 // use custom page link labels
		            pageLabelBuilder: function (page,paginator) {
		                var curr = paginator.getCurrentPage();
		                if(curr==page){
		                	return "<span class='current-page'>&nbsp;&nbsp;"+page+"&nbsp;&nbsp;</span>|";
		                }
		                else{
		                	return page;
		                }
		                
		            }

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
		var msg='<digi:trn jsFriendly="true">Are you sure you want to delete contact ?</digi:trn>';
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



	

	// don't remove or change this line!!!
	document.getElementsByTagName('body')[0].className='autocompleteClass';
</script>
<!-- BREADCRUMP START -->
<div class="breadcrump">
<div class="centering">
<div class="breadcrump_cont">
<span class="sec_name"><digi:trn>Address Book</digi:trn></span><span class="breadcrump_sep">|</span><a class="l_sm"><digi:trn>Tools</digi:trn></a><span class="breadcrump_sep"><b>»</b></span><span class="bread_sel"><digi:trn>Address Book</digi:trn></span></div>
</div>
</div>
<!-- BREADCRUMP END --> 
<table width="1000" align="center" border="0" cellpadding="0" cellspacing="0">	
<tbody>
<tr>
<td valign="top" align="left">
<div id="content" class="yui-skin-sam"> 
<div id="demo" class="yui-navset">
<ul class="yui-nav">

	<li class="selected">
	<a>
	<div>
	<digi:trn>Existing Contacts</digi:trn>
	</div>
	</a>
	</li>
	<li>
	<a href="${contextPath}/aim/addressBook.do?actionType=addContact">
	<div title='<digi:trn jsFriendly="true">Add New Contact</digi:trn>'>
		<digi:trn>Add New Contact</digi:trn>
	</div>
	</a>		
	</li>
</ul>
<div class="yui-content" style="border: 1px solid rgb(208, 208, 208);">
<jsp:include page="/repository/aim/view/exportTable.jsp" />
<table>
<tbody>
<tr>
						<td noWrap vAlign="top">
						<digi:form action="/addressBook.do?actionType=viewAddressBook" method="post">

								<table bgColor="#ffffff" align="center" cellPadding="1" cellSpacing="1" width="100%" valign="top">
											<tr bgColor="#ffffff">
												<td vAlign="top" width="100%">
													
															<!-- Table title -->
															<div style="width:100%;">
																<table width="100%">
																	<tr>
																		<td class="t_mid" style="padding-right:10px;" width=95 valign="top">
																			<digi:trn>Search Contacts</digi:trn>:
																		</td>
																		<td align="left" width=300 style="padding-right:10px;">
																			<div id="myAutoComplete">
																				<html:text property="keyword" styleId="myInput" style="width:320px;font-size:100%; font-size:12px; border:1px solid #D0D0D0; background-color:#FFFFFF;"></html:text>																																								
																		    	<div id="myContainer" style="width:315px;"></div>																		    	
																		    	<html:hidden property="keyword" styleId="myHidden"/>																			   																			    	
																		   	</div>
																		</td>
																		<td align="left" valign="top" width="30px">
																			<c:set var="trn">
																				<digi:trn>Find</digi:trn>
																			</c:set>
																			<input type="submit" value="${trn}"  class="buttonx_sm" onclick="clearCurrentAlpha()"/>																			
																		</td>
																		<td valign="top" align="left">
																			<c:set var="trnReset">
																				<digi:trn>Reset</digi:trn>
																			</c:set>
																			<input type="button" value="${trnReset}"  class="buttonx_sm" onclick="resetSearch()"/>
																		</td>
																		<td class="t_mid" valign="top">						
																			<digi:trn>Go To : </digi:trn>
																			<html:select property="currentAlpha" style="font-family:verdana;font-size:11px;" onchange="clearKeyword()" styleId="alphaDropdown">
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
														<tr>
															<td>
                                                                <div class='yui-skin-sam'>
                                                                <div id="dt-pag-nav2"></div>
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

