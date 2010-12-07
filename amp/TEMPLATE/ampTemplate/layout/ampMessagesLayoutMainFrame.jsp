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
		
	<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
	<script language="JavaScript" type="text/javascript" src="<digi:file src="module/message/scripts/messages.js"/>"></script>
		
		
		
		
	<table width="1000" border="0" cellspacing="0" cellpadding="0" align=center>
		<tr>
			<td width=768  valign=top>
				<div id="tabs">
					<ul class="desktop_tab_base">
						<li class="desktop_tab"><a href="#tabs-1" class="tab_link"><digi:trn>Messages</digi:trn></a></li>
						
						<%--
						<li class="desktop_tab"><a href="/message/messageActions.do?actionType=gotoMessagesPage&tabIndex=2&listOnly=true" class="tab_link"><digi:trn>Alerts</digi:trn></a></li>
						--%>
						
						<li class="desktop_tab"><a href="#tabs-2" class="tab_link"><digi:trn>Alerts</digi:trn></a></li>
						
						<li class="desktop_tab"><a href="#tabs-3" class="tab_link"><digi:trn>Approvals</digi:trn></a></li>
						<li class="desktop_tab"><a href="#tabs-4" class="tab_link"><digi:trn>Calendar Events</digi:trn></a></li>
					</ul>
					
					<digi:insert attribute="body"/>
				</td>
				<td width=10 align=center valign=top></td>
				<td width="222" valign=top>
						<div class="right_menu">
						<div class="right_menu_header">
							<div class="right_menu_header_cont">Information</div>
						</div>
						<div class="right_menu_box"><div class="right_menu_cont" style="font-size:11px;">
							Total Number: <b>2</b><br />
							Total Number Of Hidden:	<b>0</b>
							<hr />
							<b>Admin Settings:</b><br />	 
						Message Refresh Time (minutes): <b>2</b><br />
						Message Storage Per Message Type: <b>100</b><br />
						Days of Advance Alert Warnings: <b>2</b><br />
						Email Alerts: <b>No</b> 
					</div>
					</div>
					</div>

					
					<div class="right_menu">
					<div class="right_menu_header"><div class="right_menu_header_cont">Icons Reference</div></div>
					<div class="right_menu_box"><div class="right_menu_cont" style="font-size:11px;">
					<img src="/TEMPLATE/ampTemplate/img_2/ico_unread.gif" style="margin-right:5px;"> - Unread message<br />
					<img src="/TEMPLATE/ampTemplate/img_2/ico_unread.gif" style="margin-right:5px;"> - Unread message<br />
					<img src="/TEMPLATE/ampTemplate/img_2/ico_reply.gif" style="margin-right:5px;"> - Reply to message<br />
					<img src="/TEMPLATE/ampTemplate/img_2/ico_forward.gif" style="margin-right:5px;"> - Forward message<br />
					<img src="/TEMPLATE/ampTemplate/img_2/ico_trash.gif" style="margin-right:5px;"> - Delete message<br />
					
					</div>
					</div>
				</div>
		
			</td>
		</tr>
	</table>