<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:errors/>
<digi:instance property="aimFiscalCalendarForm" />
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
			font-size:8pt;
			padding:2px;
		}
		 
		.Hovered {
			background-color:#a5bcf2;
		}
		
		.notHovered {
			background-color:#FFFFFF;
		}
		.tdtextsize{
		height:30px;
		font-weight:bold;
		
		}
		.bdpagenum{
		padding:3px;border:1px solid #999999;float:left;
		}
		
</style>
<script language="JavaScript">



  function setStripsTable(tableId, classOdd, classEven) {
		var tableElement = document.getElementById(tableId);
		rows = tableElement.getElementsByTagName('tr');
		for(var i = 0, n = rows.length; i < n; ++i) {
			if(i%2 == 0)
				rows[i].className = classEven;
			else
				rows[i].className = classOdd;
		}
		rows = null;
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

	
	function setHoveredRow(rowId) {

		var rowElement = document.getElementById(rowId);
		if(rowElement){
	    	var className = 'Hovered',
	        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
	        cells      = rowElement.getElementsByTagName('td');

			for(var i = 0, n = cells.length; i < n; ++i) {
				cells[i].onmouseover = function() {
					this.className += ' ' + className;
				};
				cells[i].onmouseout = function() {
					this.className = this.className.replace(pattern, ' ');

				};
			}
			cells = null;
		}
	}
	
	

</script>






<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 >
	<tr>
		<td  width=14>&nbsp;</td>
		<td align=left vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:fiscalCalendarManager"> Fiscal Calendar Manager
						</digi:trn>
                      </span>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
					
						<digi:trn key="aim:fiscalCalendarManager"><span class=subtitle-blue>Fiscal
                      Calendar Manager
						</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
					<table cellspacing=0 cellSpacing=0>
					<tr>
						<td noWrap vAlign="top">
							<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%">
								<tr >
									<td vAlign="top" width="100%">&nbsp;</td>
								</tr>
								<tr >
									<td valign="top">
										<table align=center  cellPadding=0 cellSpacing=0 width="90%" border=0>
											<tr>
												<td >
													<table border=1 cellPadding=0 cellSpacing=0  width="100%"   >
												
														<!-- Page Logic -->
														<logic:empty name="aimFiscalCalendarForm" property="fiscalCal">
														<tr>
															<td colspan="5">
                                                   				<b><digi:trn key="aim:nofiscalCalendar">No fiscal calendar present</digi:trn></b>
															</td>
														</tr>
														</logic:empty>
														<logic:notEmpty name="aimFiscalCalendarForm" 	property="fiscalCal">
														<tr>
															<td width="100%">
																<table width="533" border=0 cellspacing="0" id="dataTable"	 >
																	<tr style="background-color:#999999; color:#000;   " >
																	  <td width="144" class="tdtextsize" >
																			<digi:trn key="aim:nameFiscalCalendar">Name</digi:trn>
																	  </td>
                                                                        <td nowrap="nowrap" class="tdtextsize">
                                                                        	<digi:trn key="aim:BaseCalendarFiscalCalendar">Base Calendar</digi:trn>
																	  	</td>
																	  	<td width="70" nowrap="nowrap" align="center" class="tdtextsize">
																	  		<digi:trn key="aim:isFiscalCalendar">Is Fiscal</digi:trn>
																	  	</td>
																		<td width="117" class="tdtextsize">
																			<digi:trn key="aim:startMonthFiscalCalendar">Start Month</digi:trn>
																		</td>
																		<td width="105" class="tdtextsize">
																			<digi:trn key="aim:startDayFiscalCalendar">Start Day</digi:trn>
																		</td>
																		<td width="245" class="tdtextsize">
																			<digi:trn key="aim:offsetFromCurrentYear">Offset (From current year)</digi:trn>
																		</td>
																	</tr>
                                                                <c:set value="0" var="monthIndex"/>
																<logic:iterate name="aimFiscalCalendarForm" property="fiscalCal" id="fiscalCal">
																
																	<tr>
																	  <td height=20 width="144">
																	  	<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																		<c:set target="${urlParams}" property="fiscalCalId">
																			<bean:write name="fiscalCal" property="ampFiscalCalId" />
																		</c:set>
																		<c:set target="${urlParams}" property="action" value="edit" />
																		<c:set var="translation">
																			<digi:trn key="aim:clickToEditFiscalCalendar">Click here to Edit Fiscal Calendar</digi:trn>
																		</c:set>
																		<digi:link href="/editFiscalCalendar.do" name="urlParams" title="${translation}" >
																			<bean:write name="fiscalCal" property="name"/>
																		</digi:link>
																	  </td>
                                                                      <td nowrap="nowrap">
                                                                      	<bean:write name="fiscalCal" property="baseCal"/>
																	  </td>
																	  <td width="105" align="center">
																	  	<c:if test="${fiscalCal.isFiscal==true}"><digi:trn key="aim:fiscalCalendar:yes">Yes</digi:trn></c:if>
																	  	<c:if test="${fiscalCal.isFiscal==false}"><digi:trn key="aim:fiscalCalendar:no">No</digi:trn></c:if>
																	  </td>
																		<td width="117">
																			<c:set var="startMonth">
                                                                            	<digi:trn key="calendar:${aimFiscalCalendarForm.month[monthIndex]}">${aimFiscalCalendarForm.month[monthIndex]}</digi:trn>
                                                                          	</c:set>
                                                                          	<c:out value="${startMonth}" />
		                                                                  	<c:set value="${monthIndex + 1}" var="monthIndex"/>
                                                              			</td>
																		<td width="105">
																			<bean:write name="fiscalCal" property="startDayNum"/>
																		</td>
																		<td width="233">
                                                                 			<bean:write name="fiscalCal" property="yearOffset"/>
																		</td>
																	</tr>
																  </logic:iterate>
															  </table>
															</td>
														</tr>
														</logic:notEmpty>
														<!-- end page logic -->
													</table>
												</td>
											</tr>
											<!-- page logic for pagination -->
											<logic:notEmpty name="aimFiscalCalendarForm" property="pages">
											<tr>
												<td colspan="4" ><br>
													<!-- <digi:trn key="aim:fiscalCalendarPages">Pages :</digi:trn> -->
													<logic:iterate name="aimFiscalCalendarForm" 	property="pages" id="pages" type="java.lang.Integer">
													<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${urlParams1}" property="page"><%=pages%>
													</c:set>
													<c:set var="translation">
														<digi:trn key="aim:clickToViewNextPage">Click here to goto Next Page</digi:trn>
													</c:set>
													<div class = "bdpagenum">
													<digi:link href="/fiscalCalendarManager.do" name="urlParams1" title="${translation}" >
														<%=pages%>
													</digi:link> </div></logic:iterate>
												</td>
											</tr>
											</logic:notEmpty>
											<!-- end page logic for pagination -->
										</table>
									</td>
								</tr>
							
							</table>
						</td>
						<td noWrap  vAlign="top">
							
						<br>
							<table align=center cellPadding=0 cellSpacing=0 width="150px" border=0>
										<tr>
											<td bgColor=#c9c9c7 class=box-title height="20">
												<digi:trn key="aim:Links">
												Links
												</digi:trn>
											</td>
										</tr>
										<tr>
											<td bgColor=#ffffff class=box-border>
												<table cellPadding=5 cellSpacing=1 width="100%">

														<tr>
									<td>
										<table  >
											<tr>
												<td>
													<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
														<c:set var="translation">
															<digi:trn key="aim:clickToAddFiscalCalendar">Click here to Add Fiscal Calendar</digi:trn>
														</c:set>
														<digi:link href="/editFiscalCalendar.do?action=create"  title="${translation}" >
															<digi:trn key="aim:addNewFiscalCalendar">
																Add a Fiscal Calendar </digi:trn>
														</digi:link>
												</td>
											</tr>
											<tr>
												<td>
													<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
													<c:set var="translation">
														<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
													</c:set>
													<digi:link href="/admin.do" title="${translation}" >
													<digi:trn key="aim:AmpAdminHome">
													Admin Home
													</digi:trn>
													</digi:link>
												</td>
											</tr>
											
										</table>
								</td>
								
								</tr>
												
													<!-- end of other links -->
												</table>
											</td>
										</tr>
									</table>

						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</td>
	</tr>
</table>
<script language="javascript">
	setStripsTable("dataTable", "tableEven", "tableOdd");
	setHoveredTable("dataTable", false);
	setHoveredRow("rowHighlight");
</script>

