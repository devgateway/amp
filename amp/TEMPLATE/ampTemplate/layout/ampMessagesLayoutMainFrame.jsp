<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>


	<digi:base/>
	<digi:context name="digiContext" property="context"/>
	<digi:instance property="messageForm"/>
		
	<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
	<script language="JavaScript" type="text/javascript" src="<digi:file src="module/message/scripts/messages.js"/>"></script>
	<br><br>
	
	<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
		<tr>
				<td>
					<div class="breadcrump_cont">
						<span class="sec_name">
							<digi:trn>Messages</digi:trn>
						</span>
						
						<span class="breadcrump_sep">|</span>
						<digi:link href="/viewMyDesktop.do" title="${translation}" styleClass="l_sm" module="aim">
							<digi:trn>Portfolio</digi:trn>
						</digi:link>
						<span class="breadcrump_sep"><b>�</b></span>
						<digi:trn>My Messages</digi:trn>
					</div>
				</td>
			</tr>
		<tr>
			<td width=768  valign="top">
				<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
					<ul style="height:29px;" class="desktop_tab_base ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
						<feature:display name="Message tab" module="Messages">
							<c:if test="${messageForm.tabIndex  == 1}">
								<li class="desktop_tab ui-state-default ui-corner-top ui-tabs-selected ui-state-active">
									<a href="#tabs-1" class="tab_link"><digi:trn>Messages</digi:trn></a>
								</li>
							</c:if>
							<c:if test="${messageForm.tabIndex  != 1}">
								<li class="desktop_tab ui-state-default ui-corner-top">
									<a href="#tabs-1" class="tab_link"><digi:trn>Messages</digi:trn></a>
								</li>
							</c:if>
						</feature:display>
						
						<feature:display name="Alert tab" module="Messages">
							<c:if test="${messageForm.tabIndex  == '2'}">
								<li class="desktop_tab ui-state-default ui-corner-top ui-tabs-selected ui-state-active"><a href="#tabs-2" class="tab_link">
									<digi:trn>Alerts</digi:trn></a>
								</li>
							</c:if>
							<c:if test="${messageForm.tabIndex  != '2'}">
								<li class="desktop_tab ui-state-default ui-corner-top"><a href="#tabs-2" class="tab_link">
									<digi:trn>Alerts</digi:trn></a>
								</li>
							</c:if>
						</feature:display>
						
						<feature:display name="Approval Tab" module="Messages">
							<c:if test="${messageForm.tabIndex  == '3'}">
								<li class="desktop_tab ui-state-default ui-corner-top ui-tabs-selected ui-state-active">
									<a href="#tabs-3" class="tab_link"><digi:trn>Approvals</digi:trn></a>
								</li>
							</c:if>
							<c:if test="${messageForm.tabIndex  != '3'}">
								<li class="desktop_tab ui-state-default ui-corner-top">
									<a href="#tabs-3" class="tab_link"><digi:trn>Approvals</digi:trn></a>
								</li>
							</c:if>
						</feature:display>
						
						<feature:display name="Event Tab" module="Messages">
							<c:if test="${messageForm.tabIndex  == '4'}">
								<li class="desktop_tab ui-state-default ui-corner-top ui-tabs-selected ui-state-active">
									<a href="#tabs-4" class="tab_link"><digi:trn>Calendar Events</digi:trn></a>
								</li>
							</c:if>
							<c:if test="${messageForm.tabIndex  != '4'}">
								<li class="desktop_tab ui-state-default ui-corner-top last_tab">
									<a href="#tabs-4" class="tab_link"><digi:trn>Calendar Events</digi:trn></a>
								</li>
							</c:if>
						</feature:display>
					</ul>
					
					<digi:insert attribute="body"/>
				</td>
				<td width="10" align="center" valign="top"></td>
				<td width="222" valign="top">
						<div class="right_menu">
						<div class="right_menu_header">
							<div class="right_menu_header_cont"><digi:trn>Information</digi:trn></div>
						</div>
						<div class="right_menu_box"><div class="right_menu_cont" style="font-size:11px;">
							<digi:trn key="message:totalNum">Total Number</digi:trn>: <b id="totalMsgCountContainer">${messageForm.allmsg}</b><br />
							<digi:trn key="message:numofhidden">Total Number Of Hidden</digi:trn>:	<b id="totalHiddenMsgCountContainer">${messageForm.hiddenMsgCount}</b>
							<hr />
							<b><digi:trn key="message:adminSetings">Admin Settings</digi:trn>:</b><br />	 
						<digi:trn key="message:refreshtime">Message Refresh Time(minutes)</digi:trn>: <b>${messageForm.msgRefreshTimeCurr}</b><br />
						<digi:trn key="message:storepermess">Message Storage Per Message Type</digi:trn>: <b>${messageForm.msgStoragePerMsgTypeCurr}</b><br />
						<digi:trn key="message:alertwarnings">Days of Advance Alert Warnings</digi:trn>: <b>${messageForm.daysForAdvanceAlertsWarningsCurr}</b><br />
						<digi:trn key="message:emailalerts">Email Alerts</digi:trn>: 
						<b>
							<c:if test="${empty messageForm.emailMsgsCurrent ||messageForm.emailMsgsCurrent==0}">
								<digi:trn key="message:No">No</digi:trn>
							</c:if>
							<c:if test="${messageForm.emailMsgsCurrent==1}">
								<digi:trn key="message:yes">Yes</digi:trn>
							</c:if>
						</b> 
					</div>
					</div>
					</div>

					
					<div class="right_menu">
					<div class="right_menu_header"><div class="right_menu_header_cont"><digi:trn>Icons Reference</digi:trn></div></div>
					<div class="right_menu_box"><div class="right_menu_cont" style="font-size:11px;">
					<img src="/TEMPLATE/ampTemplate/img_2/ico_unread.gif" style="margin-right:5px;"> - <digi:trn>Unread message</digi:trn><br/>
					<img src="/TEMPLATE/ampTemplate/img_2/ico_reply.gif" style="margin-right:5px;"> - <digi:trn>Reply to message</digi:trn><br/>
					<img src="/TEMPLATE/ampTemplate/img_2/ico_forward.gif" style="margin-right:5px;"> - <digi:trn>Forward message</digi:trn><br/>
					<img src="/TEMPLATE/ampTemplate/img_2/ico_trash.gif" style="margin-right:5px;"> - <digi:trn>Delete message</digi:trn><br/>
					
					</div>
					</div>
				</div>
		
			</td>
		</tr>
	</table>