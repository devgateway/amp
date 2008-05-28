<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>


<digi:instance property="messageForm"/>
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
<script langauage="JavaScript">
	
	function validate(record){
		if(record==null || record==""){
			alert('Please enter data to save');
			return false;
		}
		return true;
	}
	
	function saveRecord(settingType) {
		var record;
		if(settingType=='refreshTime'){
			 record=document.getElementsByName('msgRefreshTimeNew')[0];
		}else if(settingType=='storage'){
			record=document.getElementsByName('msgStoragePerMsgTypeNew')[0];
		}else if(settingType=='warning'){
			record=document.getElementsByName('daysForAdvanceAlertsWarningsNew')[0];
		}else if(settingType=='maxValidity'){
			record=document.getElementsByName('maxValidityOfMsgNew')[0];
		}else if(settingType=='emailAlerts'){
			record=document.getElementsByName('emailMsgsNew')[0];
		}
		
		if(validate(record.value)){			
			 <digi:context name="saveRecord" property="context/module/moduleinstance/msgSettings.do"/>
			 url = "<%= saveRecord %>?actionType=saveSettings&settingType="+settingType;
			 document.forms[0].action =url;
			 document.forms[0].submit();		 
			 return true;
		}
	}
	
	function saveAll(){
		<digi:context name="saveAll" property="context/module/moduleinstance/msgSettings.do"/>
		url = "<%= saveAll %>?actionType=saveSettings&settingType=saveAll";
		document.forms[0].action =url;
		document.forms[0].submit();		 
		return true;
	}	
</script>

<digi:form action="/msgSettings.do" >
	<TABLE align=center border=0 cellPadding=2 cellSpacing=3 width="100%" bgcolor="#f4f4f2">
		<TR>
			<TD class=r-dotted-lg-buttom vAlign=top>
				<TABLE border=0 cellPadding=0 cellSpacing=0 width="100%" >
						<TR><TD bgColor="#ffffff" class="box-border" align="left"> 
							<TABLE border="0" cellPadding="1" cellSpacing="1" width="100%">
								<TR>
									<TD>
										<TABLE border="1" cellPadding="3" cellSpacing="3" width="100%" bordercolor="#ccecff" rules="all">
											<tr>
												<td colspan="4" align="center" bgcolor="#ccecff"><digi:trn key="message:msgSettings">Message Settings</digi:trn> </td>								
											</tr>
											<tr>
												<td align="center"><digi:trn key="message:setting">Setting</digi:trn> </td>
												<td align="center"><digi:trn key="message:currentValue">Current Value</digi:trn></td>
												<td align="center"><digi:trn key="message:newValue">New Value</digi:trn></td>
												<td>&nbsp;</td>
											</tr>
											<tr>
												<td align="center"><digi:trn key="message:refreshTime">Message Refresh Time(minutes)</digi:trn></td>
												<td align="center">${messageForm.msgRefreshTimeCurr}</td>
												<td align="center"><html:text name="messageForm" property="msgRefreshTimeNew" /></td>
												<td align="center">
													<c:set var="saveBtn"><digi:trn key="message:btn:save">Save</digi:trn></c:set>
													<input type="button" value="${saveBtn}" onclick="saveRecord('refreshTime')"/>
												</td>
											</tr>
											<tr>
												<td align="center"><digi:trn key="message:msgStorage">Message Storage Per Message Type</digi:trn></td>
												<td align="center">${messageForm.msgStoragePerMsgTypeCurr} </td>
												<td align="center"><html:text name="messageForm" property="msgStoragePerMsgTypeNew" /> </td>
												<td align="center"><input type="button" value="${saveBtn}" onclick="saveRecord('storage')" /></td>
											</tr>
											<tr>
												<td align="center"><digi:trn key="message:advanceAlertWarnings">Days of Advance Alert Warnings</digi:trn></td>
												<td align="center">${messageForm.daysForAdvanceAlertsWarningsCurr}</td>
												<td align="center"><html:text name="messageForm" property="daysForAdvanceAlertsWarningsNew"/> </td>
												<td align="center"><input type="button" value="${saveBtn}" onclick="saveRecord('warning')" /></td>
											</tr>
											<tr>
												<td align="center"><digi:trn key="message:maxValidityOfMsg">Maximum validity of a message</digi:trn></td>
												<td align="center">${messageForm.maxValidityOfMsgCurr}</td>
												<td align="center"><html:text name="messageForm" property="maxValidityOfMsgNew" /></td>
												<td align="center"><input type="button" value="${saveBtn}" onclick="saveRecord('maxValidity')" /></td>
											</tr>
											<tr>
												<td align="center"><digi:trn key="message:emailAlerts">Email Alerts</digi:trn></td>
												<td align="center">
													<c:if test="${messageForm.emailMsgsCurrent==-1}">
														&nbsp;
													</c:if>			
													<c:if test="${messageForm.emailMsgsCurrent==0}">
														<digi:trn key="message:No">No</digi:trn>
													</c:if>	
													<c:if test="${messageForm.emailMsgsCurrent==1}">
														<digi:trn key="message:yes">Yes</digi:trn>
													</c:if>	
												</td>
												<td align="center">
													<html:select property="emailMsgsNew" name="messageForm">
														<html:option value="-1">please select</html:option>
														<html:option value="0">No</html:option>
														<html:option value="1">Yes</html:option>
													</html:select>
												</td>
												<td align="center"><input type="button" value="${saveBtn}" onclick="saveRecord('emailAlerts')" /></td>
											</tr>																			
											<tr>
												<td colspan="4" align="right">
													<c:set var="saveAllBtn"><digi:trn key="message:btn:saveAll">Save All</digi:trn></c:set>
													<input type="button" value="${saveAllBtn}" onclick="saveAll()" />
												</td>											
											</tr>
										</TABLE>
									</TD>
								</TR>													
							</TABLE>
						</TD></TR>												
				</TABLE>				
			</TD>
		</TR>
	</TABLE>
</digi:form>
