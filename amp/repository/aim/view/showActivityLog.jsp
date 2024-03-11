<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://digijava.org" prefix="digi"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<digi:instance property="aimAuditLoggerManagerForm" />

<table width="100%" cellspacing="0" cellpadding="0" vAlign="top" border="0">
	<tr>
		<td vAlign="top">

		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%"
			class=box-border-nopadding>
			<tr>
				<td align=left valign="top">
				<table bgcolor=#f4f4f2 cellpadding="0" cellspacing="0" width="100%"
					class=box-border-nopadding>
					<tr bgcolor="#006699">
						<td vAlign="center" width="100%" align="center" class="textalb"
							height="20">
							<digi:trn key="activitychanges">Activity Changes</digi:trn>
						</td>
					</tr>
					<tr>
						<td align="center" bgcolor=#ECF3FD>
						<table width="100%">
							<tr height="20">
								<td align="center"><strong><digi:trn key="showactivitylog_editorname">Editor Name</digi:trn></strong></td>
								<td align="center"><strong><digi:trn key="showactivitylog_changedate">Change Date</digi:trn></strong></td>
								<td align="center"><strong><digi:trn key="showactivitylog_action">Action</digi:trn></strong></td>
								<td align="center"><strong><digi:trn key="showactivitylog_detail">Detail</digi:trn></strong></td>
							</tr>
							<logic:iterate name="aimAuditLoggerManagerForm" property="logs" id="log" type="org.digijava.module.aim.dbentity.AmpAuditLogger">
								<tr height="20">
									<td align="center">&nbsp;<bean:write name="log" property="editorName"/>&nbsp;</td>
									<td align="center">&nbsp;<bean:write name="log" property="modifyDate"/>&nbsp;</td>
									<td align="center">&nbsp;<bean:write name="log" property="action"/>&nbsp;</td>
									<td align="center">&nbsp;<bean:write name="log" property="detail"/>&nbsp;</td>
								</tr>
							</logic:iterate>							
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