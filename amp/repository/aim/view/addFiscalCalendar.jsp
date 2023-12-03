<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<DIV id="TipLayer" style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="JavaScript">

	function msg() {
		if (confirm('<digi:trn jsFriendly="true" key="aim:fiscalCalendar:deleteQuestion">Are you sure about deleting this calendar?</digi:trn>')) {
			document.aimAddFiscalCalForm.action.value = "delete";
			document.aimAddFiscalCalForm.submit();
		}
		else
			return false;
	}

	function move() {
		<digi:context name="selectLoc" property="context/ampModule/moduleinstance/fiscalCalendarManager.do" />
		url = "<%= selectLoc %>";
		document.location.href = url;
	}

	function check() {
        var msg='';

        var sm=document.aimAddFiscalCalForm.startMonthNum.value;
        var sd=document.aimAddFiscalCalForm.startDayNum.value;
        var yr=document.aimAddFiscalCalForm.yearOffset.value;

        var str = document.aimAddFiscalCalForm.fiscalCalName.value;
        str = trim(str);
		if (str.length == 0 || str == null) {
                   msg='<digi:trn jsFriendly="true" key="aim:enterNameFiscalCalendar">Please enter name for this Fiscal Calenedar</digi:trn>'
                   alert(msg);

			document.aimAddFiscalCalForm.fiscalCalName.focus();
            return false;
        }

        if(sm.match("[^0-9]") || sm.length == 0){
          msg='<digi:trn jsFriendly="true" key="aim:invalidStartMonth">Invalid Start Month value</digi:trn>'
          alert(msg);
          document.aimAddFiscalCalForm.startMonthNum.focus();
          return false;
        }else if(sm>12 || sm<1){
           msg='<digi:trn jsFriendly="true" key="aim:invalidStartMonth">Invalid Start Month value</digi:trn>'
          alert(msg);
          document.aimAddFiscalCalForm.startMonthNum.focus();
          return false;
        }

        if(sd.match("[^0-9]") || sd.length == 0){
          msg='<digi:trn jsFriendly="true" key="aim:invalidStartDay">Invalid Start Day value</digi:trn>'
          alert(msg);
          document.aimAddFiscalCalForm.startDayNum.focus();
          return false;
        }else if(sd>31){
          msg='<digi:trn jsFriendly="true" key="aim:invalidStartDay">Invalid Start Day value</digi:trn>'
          alert(msg);
          document.aimAddFiscalCalForm.startDayNum.focus();
          return false;
        }

        if(yr.match("[^0-9,-]") || yr.length == 0){
          msg='<digi:trn jsFriendly="true" key="aim:invalidYearOffset">Invalid Year Offset value</digi:trn>'
          alert(msg);
          document.aimAddFiscalCalForm.yearOffset.focus();
          return false;
        }
        /*
        else if(yr>0){
          msg='<digi:trn jsFriendly="true" key="aim:invalidYearOffset">Invalid Year Offset value</digi:trn>'
          alert(msg);
          document.aimAddFiscalCalForm.yearOffset.focus();
          return false;
        }
        */


        document.aimAddFiscalCalForm.fiscalCalName.value = str;
        document.aimAddFiscalCalForm.submit();
	}

	function trim ( inputStringTrim ) {
		fixedTrim = "";
		lastCh = " ";
		for (x=0; x < inputStringTrim.length; x++) {
			ch = inputStringTrim.charAt(x);
			if ((ch != " ") || (lastCh != " ")) { fixedTrim += ch; }
				lastCh = ch;
		}
		if (fixedTrim.charAt(fixedTrim.length - 1) == " ") {
			fixedTrim = fixedTrim.substring(0, fixedTrim.length - 1); }
		return fixedTrim
	}

	var enterBinder	= new EnterHitBinder('addFiscalCalBtn');
</script>

<digi:instance property="aimAddFiscalCalForm" />
<digi:context name="digiContext" property="context"/>

<digi:form action="/editFiscalCalendar.do" method="post">
<html:hidden property="action" />
<html:hidden property="fiscalCalId" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->
<h1 class="admintitle" style="text-align:left;"><digi:trn key="aim:fiscalCalendarManager">Fiscal Calendar Manager</digi:trn></h1>
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=1000>
	<tr>
		<td align=left valign="top" width=1000>
			<table cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" >
							<digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<c:set var="translation">
							<digi:trn key="aim:clickToViewFiscalCalendarManager">Click here to view Fiscal Calendar Manager</digi:trn>
						</c:set>
						<digi:link href="/fiscalCalendarManager.do" styleClass="comment" title="${translation}" >
							<digi:trn key="aim:fiscalCalendarManager">Fiscal Calendar Manager</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<logic:equal name="aimAddFiscalCalForm" property="action" value="create" >
							<digi:trn key="aim:addFiscalCalendar">Add Fiscal Calendar</digi:trn>
						</logic:equal>
						<logic:equal name="aimAddFiscalCalForm" property="action" value="edit" >
							<digi:trn key="aim:editFiscalCalendar">Edit Fiscal Calendar</digi:trn>
						</logic:equal>
                      </span>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 valign="center" width=571>
						<br>
						<digi:errors/>
					</td>
				</tr>
				<tr>
					<td noWrap width="100%" vAlign="top">
					<table width="100%" cellspacing="1" cellspacing="1">
					<tr>
						<td noWrap width=600 vAlign="top" align=center>
							<table bgColor=#ffffff cellpadding="0" cellspacing="0" class=box-border-nopadding width="100%" align="center">
								<tr bgColor=#f4f4f2>
									<td vAlign="top" width="100%">&nbsp;</td>
								</tr>
								<tr bgColor=#f4f4f2>
									<td valign="top">
										<table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="90%" border="0">
											<tr>
												<td bgColor=#ffffff class=box-border>
													<table border="0" cellpadding="1" cellspacing="1" class=box-border width="100%">
														<tr bgColor=#dddddb>
															<td bgColor=#dddddb height="20" align="center" colspan="5">
																<logic:equal name="aimAddFiscalCalForm" property="action" value="create" >
																	<b><digi:trn key="aim:addFiscalCalendar">Add Fiscal Calendar</digi:trn></b>
																</logic:equal>
																<logic:equal name="aimAddFiscalCalForm" property="action" value="edit" >
																	<b><digi:trn key="aim:editFiscalCalendar">Edit Fiscal Calendar</digi:trn></b>
																</logic:equal>
															</td>
														</tr>
														<!-- Page Logic -->
														<tr>
															<td width="100%">
																<table width="100%" border="0"	 bgColor=#f4f4f2>
																	<tr>
																		<td width="30%" align="right">
                                                                        	<font color="red"><b>*</b></font>
																			<digi:trn key="aim:fiscalCalendarName">Name</digi:trn>	
																		</td>
																	    <td width="30%" >
																	          <html:text property="fiscalCalName" size="40" />																	    
																	    </td>
																	</tr>
																	<tr>
																		<td width="30%" align="right">
                                                                          <font color="red"><b>*</b></font>
																	        <digi:trn key="aim:fiscalCalendarStMnt">Start Month</digi:trn>																		</td>
																		<td width="30%">
																           <html:text property="startMonthNum" size="3" />
																        </td>
																	</tr>
																	<tr>
																		<td width="30%" align="right">
																			<font color="red"><b>*</b></font>
																	        <digi:trn key="aim:fiscalCalendarStDay">Start Day</digi:trn>																		</td>
																		<td width="30%">
																	          <html:text property="startDayNum" size="3" />
																	    </td>
																	</tr>
																	<tr>
																		<td width="30%" align="right">
																			<font color="red"><b>*</b></font>
																	        <digi:trn key="aim:fiscalCalendarYrOffset">Year Offset</digi:trn>																		</td>
																		<td width="30%">
																	          <html:text property="yearOffset" size="3" />
																	    </td>
																	</tr>
																	<tr>
																	  <td align="right"><digi:trn key="aim:fiscalCalendarType">Base Calendar</digi:trn></td>
																	  <td>
																	      <html:select property="baseCalendar" >
																	      	<c:forEach items="${aimAddFiscalCalForm.baseCalendarList}" var="baseCalendar">
																	      		<html:option value="${baseCalendar.value}">
																	      			<digi:trn key="${baseCalendar.trnName}">
																	      					<c:out value="${baseCalendar.name}"/>
																	      			</digi:trn>
																	      		</html:option>
																	      	</c:forEach>
																	      </html:select>
                                                                      </td>
																  </tr>
																  <tr>
																  	<td width="30%" align="right">
																  		<digi:trn key="aim:isFiscalCalendar">Is Fiscal</digi:trn>
																  	</td>
																  	<td width="30%" align="left">
																  		<html:select property="isFiscal" >
																  			<html:option value="1"><digi:trn key="aim:fiscalCalendar:yes">Yes</digi:trn></html:option>
																  			<html:option value="0"><digi:trn key="aim:fiscalCalendar:no">No</digi:trn></html:option>
																	    </html:select>
																  	</td>
																  </tr>
																	<tr>
																		<td width="30%" align="right">
																	        <digi:trn key="aim:fiscalCalendarDescription">Description</digi:trn>
																	    </td>
																	    <td width="30%">
																           <html:textarea property="description" cols="30" rows="3" />
																        </td>
																	</tr>
																	<tr>
																		<td colspan="2" width="60%"  align="center">
																			<table width="100%" cellspacing="5">
																				<tr>
																					<td width="45%" align="right">
																						<html:button  styleClass="dr-menu" property="submitButton"  onclick="check()" styleId="addFiscalCalBtn">
																							<digi:trn key="btn:save">Save</digi:trn>
																						</html:button>
																					</td>
																					<td width="8%" align="left">
																						<html:reset  styleClass="dr-menu" property="submitButton" >
																							<digi:trn key="btn:reset">Reset</digi:trn>
																						</html:reset>
																					</td>
																					<td width="45%" align="left">
																						<html:button  styleClass="dr-menu" property="submitButton"  onclick="move()">
																							<digi:trn key="btn:cancel">Cancel</digi:trn>
																						</html:button>
																					</td>
																				</tr>
																			</table>
																		</td>
																	</tr>
																	<logic:equal name="aimAddFiscalCalForm" property="flag" value="delete" >
																		<tr>
																			<td colspan="2" width="60%"  align="center">
																				<html:button  styleClass="dr-menu" property="submitButton"  onclick="msg()">
																					<digi:trn key="btn:deleteThisCalendar">Delete this Calendar</digi:trn>
																				</html:button>
																			</td>
																		</tr>
																	</logic:equal>
																	<logic:equal name="aimAddFiscalCalForm" property="flag" value="orgReferences" >
																		<tr>
																			<td colspan="2" width="60%"  align="center">
																				<b><digi:trn key="aim:cannotDeleteFisCalMsg1"><font color="#FF0000">
																						Can not delete this calendar since some organization references it !</font>
																					</digi:trn>
																				</b>
																			</td>
																		</tr>
																	</logic:equal>
																	<logic:equal name="aimAddFiscalCalForm" property="flag" value="orgConstantCurrencies" >
																		<tr>
																			<td colspan="2" width="60%"  align="center">
																				<html:button  styleClass="dr-menu" property="submitButton"  onclick="msg()">
																					<digi:trn key="btn:deleteThisCalendar">Delete this Calendar</digi:trn>
																				</html:button>
																				<BR /><b><digi:trn key="aim:warningDeleteConstCurrMsg"><font color="#FF0000">
																						This calendar is in use by constant currencies. If you delete the calendar, then its related constant currencies will be deleted too!</font>
																					</digi:trn>
																				</b>
																			</td>
																		</tr>
																	</logic:equal>
																</table>
														  </td>
														</tr>
													<!-- end page logic -->
													</table>
												</td>
											</tr>

										</table>
									</td>
								</tr>
								<tr>
									<td bgColor=#f4f4f2>&nbsp;
										
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
</digi:form>
