<%@ page pageEncoding="UTF-8" %>
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
		font-size:8pt;!important
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
<style>
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


#subtabs ul {
	display: inline;
	list-style-type: none;
	margin: 0;
	padding: 0;
}

#subtabs li {
	float: left;
	padding: 0px 4px 0px 4px;
}

#subtabs a, #subtabs span { 
	font-size: 8pt; 
}

#subtabs a {
}

#subtabs ul li span {
	text-decoration: none;
}

#subtabs ul li div span {
	text-decoration: none;
}

#subtabs {
	text-align: center;
	font-family:Arial,Helvetica,sans-serif;
	font-size: 8pt;
	padding: 2px 4px 2px 4px;
	background-color:#CCDBFF;
}

#main {
	clear:both;
	text-align: left;
	border-top: 2px solid #222E5D;
	border-left: 1px solid #666;
	border-right: 1px solid #666;
	padding: 2px 4px 2px 4px;
}
html>body #main {
	width:867px;
}

#mainEmpty {
	border-top: 2px solid #222E5D;
	width: 750px;
	clear:both;
}
html>body #mainEmpty {
	clear:both;
	width:752px;
}

</style>

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
		document.getElementById('myInput').value=null;
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


<digi:form action="/addressBook.do?actionType=viewAddressBook" method="post">	
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
							<div id="main">
								<table bgColor="#ffffff" cellPadding="1" cellSpacing="1" width="100%" valign="top">
											<tr bgColor="#ffffff">
												<td vAlign="top" width="100%">
													<table width="100%" cellspacing="1" cellpadding="1" valign="top" align="left">
														<tr><td>&nbsp;</td></tr>
														<tr><td class="box-title" >
															<!-- Table title -->
															<div style="width:867px;">
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
																				<html:option value="viewAll">-All-</html:option>
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
																<div style="border:1px solid #999999;width: 865px" >
																	<div style= "background-color:#999999; color:#000; font-weight:bold; padding-top:5px; height:15px; width:865px;">
																		<center>Contact List</center>
																	</div>										
																	<div style="overflow:auto;width:865px;height:250px;max-height:220px; " >																		
																		<table width="100%" id="dataTable" cellspacing="0" cellpadding="4" valign="top"  align="left">
																			<c:if test="${empty addressbookForm.contactsForPage}">
													                        	<tr>
																					<td colspan="5">
									                                                	<b><digi:trn>No Contacts present</digi:trn></b>
																					</td>
																				</tr>
												                            </c:if>
												                            <c:if test="${not empty addressbookForm.contactsForPage}">
												                            	<tr height="100%">																						
																					<td width="152">
																						<c:if test="${not empty addressbookForm.sortBy && addressbookForm.sortBy!='nameAscending'}">
																							<digi:link href="/addressBook.do?actionType=searchContacts&sortBy=nameAscending&reset=false">
																								<b><digi:trn>Name</digi:trn></b>
																							</digi:link>																					
																						</c:if>
																						<c:if test="${empty addressbookForm.sortBy || addressbookForm.sortBy=='nameAscending'}">
																							<digi:link href="/addressBook.do?actionType=searchContacts&sortBy=nameDescending&reset=false">
																								<b><digi:trn>Name</digi:trn></b>
																							</digi:link>																					
																						</c:if>
																						<c:if test="${empty addressbookForm.sortBy || addressbookForm.sortBy=='nameAscending'}"><img  src="/repository/aim/images/up.gif"/></c:if>
																						<c:if test="${not empty addressbookForm.sortBy && addressbookForm.sortBy=='nameDescending'}"><img src="/repository/aim/images/down.gif"/></c:if>
																					</td>
																					<td width="100">																		
																						<c:if test="${empty addressbookForm.sortBy || addressbookForm.sortBy!='emailAscending'}">
																							<digi:link href="/addressBook.do?actionType=searchContacts&sortBy=emailAscending&reset=false">
																								<b><digi:trn>Email</digi:trn></b>
																							</digi:link>																					
																						</c:if>
																						<c:if test="${not empty addressbookForm.sortBy && addressbookForm.sortBy=='emailAscending'}">
																							<digi:link href="/addressBook.do?actionType=searchContacts&sortBy=emailDescending&reset=false">
																								<b><digi:trn>Email</digi:trn></b>
																							</digi:link>
																						</c:if>
																						<c:if test="${not empty addressbookForm.sortBy && addressbookForm.sortBy=='emailAscending'}"><img  src="/repository/aim/images/up.gif"/></c:if>
																						<c:if test="${not empty addressbookForm.sortBy && addressbookForm.sortBy=='emailDescending'}"><img src="/repository/aim/images/down.gif"/></c:if>
																					</td>
																					<td width="5px">&nbsp;</td>
																					<td width="150">
																						<c:if test="${empty addressbookForm.sortBy || addressbookForm.sortBy!='orgNameAscending'}">
																							<digi:link href="/addressBook.do?actionType=searchContacts&sortBy=orgNameAscending&reset=false">
																								<b><digi:trn >Organisation Name</digi:trn></b>
																							</digi:link>																					
																						</c:if>
																						<c:if test="${not empty addressbookForm.sortBy && addressbookForm.sortBy=='orgNameAscending'}">
																							<digi:link href="/addressBook.do?actionType=searchContacts&sortBy=orgNameDescending&reset=false">
																								<b><digi:trn >Organisation Name</digi:trn></b>
																							</digi:link>																					
																						</c:if>
																						<c:if test="${not empty addressbookForm.sortBy && addressbookForm.sortBy=='orgNameAscending'}"><img  src="/repository/aim/images/up.gif"/></c:if>
																						<c:if test="${not empty addressbookForm.sortBy && addressbookForm.sortBy=='orgNameDescending'}"><img src="/repository/aim/images/down.gif"/></c:if>																																			
																					</td>
																					<td width="100">
																						<c:if test="${empty addressbookForm.sortBy || addressbookForm.sortBy!='titleAscending'}">
																							<digi:link href="/addressBook.do?actionType=searchContacts&sortBy=titleAscending&reset=false">
																								<b><digi:trn>Title</digi:trn></b>
																							</digi:link>																					
																						</c:if>
																						<c:if test="${not empty addressbookForm.sortBy && addressbookForm.sortBy=='titleAscending'}">
																							<digi:link href="/addressBook.do?actionType=searchContacts&sortBy=titleDescending&reset=false">
																								<b><digi:trn>Title</digi:trn></b>
																							</digi:link>																					
																						</c:if>
																						<c:if test="${not empty addressbookForm.sortBy && addressbookForm.sortBy=='titleAscending'}"><img  src="/repository/aim/images/up.gif"/></c:if>
																						<c:if test="${not empty addressbookForm.sortBy && addressbookForm.sortBy=='titleDescending'}"><img src="/repository/aim/images/down.gif"/></c:if>
																					</td>
																					<td height="30" width="100">
																						<b><digi:trn>Phone</digi:trn></b>													
																					</td>
																					<td height="30" width="100">
																						<b><digi:trn>Fax</digi:trn></b>															
																					</td>
																					<td height="30" colspan="2" width="100"><b>
																						<digi:trn>Actions</digi:trn></b>
																					</td>
																				</tr>
												                            	<c:forEach var="cont" items="${addressbookForm.contactsForPage}" varStatus="stat">
					                                                           		<c:set var="background">
																						<c:if test="${stat.index%2==0}">#dbe5f1</c:if>
																						<c:if test="${stat.index%2==1}">#ffffff</c:if>
																					</c:set>
																					<tr bgcolor="${background}">
						                                                           		<td height="30" width="152">
																						  ${cont.name}&nbsp;${cont.lastname}
																						</td>
																						<td height="30" width="100">
																						  	${cont.email}
																						</td>
																						<td width="5px">&nbsp;</td>																	
																						<td height="30" width="100">
																							${cont.organisationName}
																						</td>
																						<td height="30" width="100">
																							${cont.title}
																						</td>
																						<td height="30" width="100">
																							${cont.phone}
																						</td>
																						<td height="30" width="100">
																							${cont.fax}
																						</td>
																						<td height="30" width="100">
																							<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																							<c:set target="${urlParams}" property="contactId">
																								<bean:write name="cont" property="id"/>
																							</c:set>
																							<digi:link href="/addressBook.do?actionType=editContact" name="urlParams"><img src="/repository/message/view/images/edit.gif" border="0" /></digi:link>
																							<digi:link href="/addressBook.do?actionType=deleteContact" name="urlParams" onclick="return confirmDelete()"><img src="/repository/message/view/images/trash_12.gif" border="0" /></digi:link>
																						</td>																			
					                                                            	</tr>
																				</c:forEach>
												                            </c:if>
																			<!-- end page logic -->
																		</table>
																	</div>
																</div>
															</td>
														</tr>
														<tr>
															<td>	
																<div style= " float:left; width:865px;" >
																		<!-- page logic for pagination -->
																		<jsp:useBean id="urlParams3" type="java.util.Map" class="java.util.HashMap"/>
                                                            			<c:set target="${urlParams3}" property="page">1</c:set>
                                                            			<c:set target="${urlParams3}" property="numPerPage">${addressbookForm.resultsPerPage}</c:set>
                                                             			<c:set var="translation">
                                                                			<digi:trn>First Page</digi:trn>
                                                            			</c:set>
																		&nbsp;                                                            			
                                                                		<c:if test="${addressbookForm.pagesSize>0}">                                                                    		
		                                                                	<c:if test="${addressbookForm.currentPage != 1}">
		                                                                		<c:set target="${urlParams3}" property="page">1</c:set>
		                                                                       	<div class="pagination">
		                                                                           <digi:link href="/addressBook.do?actionType=searchContacts" name="urlParams3" title="${translation}" >
		                                                                                &lt;&lt;
		                                                                            </digi:link>
		                                                                        </div>
		                                                                    </c:if>
                                                                   	 		<div style="float:left;">&nbsp;</div>
                                                                		</c:if>
                                                                		<c:if test="${addressbookForm.currentPage>10}">
                                                                			<div class="pagination">
                                                                				<c:set target="${urlParams3}" property="page">${addressbookForm.currentPage - 10}</c:set>
	                                                                			<c:set var="translation">
							                                                    	<digi:trn>previous 10 pages</digi:trn>
							                                                    </c:set>
							                                                    <digi:link href="/addressBook.do?actionType=searchContacts" name="urlParams3"title="${translation}" >  &lt;Previous 10</digi:link>
                                                                			</div>                                                                			
                                                                		</c:if>
																		<logic:notEmpty name="addressbookForm" property="pages">
																			<c:set var="start" value="${addressbookForm.offset}"/>
																			<logic:iterate name="addressbookForm" property="pages" id="pages" type="java.lang.Integer" offset="${start}" length="10">
																				<div style="float:left; width:10px;  padding:3px;border:1px solid #999999; ">
																					<c:set target="${urlParams3}" property="page"><%=pages%></c:set>
																					<c:set var="translation">
																						<digi:trn>Click here to view All pages</digi:trn>
																					</c:set>
						                                                            <c:if test="${addressbookForm.currentPage == pages}">
						                                                                <font color="#FF0000"><%=pages%></font>
						                                                            </c:if>
						                                                            <c:if test="${addressbookForm.currentPage != pages}">
						                                                                <c:set var="translation">
						                                                                    <digi:trn>Click here to go to Next Page</digi:trn>
						                                                                </c:set>
						                                                                	<digi:link href="/addressBook.do?actionType=searchContacts" name="urlParams3"title="${translation}" ><%=pages%></digi:link>
						                                                            </c:if>
																				</div>
																				<div style="float:left;">&nbsp;</div>
																			</logic:iterate>
																		</logic:notEmpty>
																		<c:if test="${addressbookForm.currentPage+10 <= addressbookForm.pagesSize}">
																			<div class="pagination">
																				<c:set target="${urlParams3}" property="page">${addressbookForm.currentPage + 10}</c:set>
	                                                                			<c:set var="translation">
							                                                    	<digi:trn>next 10 pages</digi:trn>
							                                                    </c:set>
							                                                    <digi:link href="/addressBook.do?actionType=searchContacts" name="urlParams3"title="${translation}" >Next 10</digi:link>
																			</div>                                                                			
                                                                		</c:if>
		                                                                <c:set var="translation">
		                                                                    <digi:trn>Last Page</digi:trn>
		                                                                </c:set>
		                                                                <c:if test="${pages>0}">
		                                                                	<c:set target="${urlParams3}" property="page">${addressbookForm.pagesSize}</c:set>
		                                                                    <div class="pagination">
		                                                                        <c:if test="${addressbookForm.currentPage != pages}">
		                                                                            <digi:link href="/addressBook.do?actionType=searchContacts" name="urlParams3" title="${translation}" >
		                                                                                &gt;&gt;
		                                                                            </digi:link>
		                                                                        </c:if>
		                                                                        <c:if test="${addressbookForm.currentPage == pages}">
		                                                                            &gt;&gt;
		                                                                        </c:if>
		                                                                    </div>
		                                                                    <div style="float:left;">&nbsp;</div>
		                                                                    <div class="pagination">
		                                                                        <c:out value="${addressbookForm.currentPage}"></c:out>&nbsp;<digi:trn key="aim:of">of</digi:trn>&nbsp;<c:out value="${addressbookForm.pagesSize}"></c:out>
		                                                                    </div>
		                                                                </c:if>
                                                                		<!-- end page logic for pagination -->
		                                                                <div style= " float:right;" >
		                                                                    <digi:trn key="aim:results">Results</digi:trn>:&nbsp;
		                                                                    <html:select property="resultsPerPage" styleClass="inp-text" onchange="submit()" >
		                                                                        <html:option value="10">10</html:option>
		                                                                        <html:option value="20">20</html:option>
		                                                                        <html:option value="50">50</html:option>
		                                                                        <html:option value="-1"><digi:trn>All</digi:trn></html:option>
		                                                                    </html:select>
		                                                                </div>
                                                        			</div>
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
								</div>
							
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	
<script type="text/javascript" language="JavaScript" src="<digi:file src="module/message/script/autocomplete.js"/>"></script>
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
	    oAC.itemSelectEvent.subscribe(myHandler);	    

	    return {
	        oDS: oDS,
	        oAC: oAC
	    };
	}();    


	setHoveredTable("dataTable", false);
</script>
</digi:form>

