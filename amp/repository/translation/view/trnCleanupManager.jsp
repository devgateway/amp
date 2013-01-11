<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/displaytag" prefix="display"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<script type="text/javascript">
	function confirmMsgDelete() {
		var msg = '<digi:trn jsFriendly="true">Are you sure?</digi:trn>';
		return confirm(msg);
	}
</script>
<digi:instance property="translationCleanupForm" />
<digi:context name="digiContext" property="context" />
<digi:form action="/msgCleanupManager.do" method="post">
	<table  cellpadding="2" cellspacing="2" border="0" width="300px">
	<tr>
	<td colspan="2">
	  <span style="font-size:12px; color:#000000;">
          <b><digi:trn>Translation Cleanup Manager</digi:trn></b><hr />
      </span>
	</td>
	</tr>
		<tr>
			<td>
				<table cellpadding="2" cellspacing="2" border="0" width="300px">
					<tr>
						<td align="right"><digi:trn>Delete Messages Older then</digi:trn>:&nbsp;&nbsp;</td>
						<td align="left" height="30px"><html:select
								property="deleteBeforeDate" styleClass="inp-text">
								<html:option value="1">1 <digi:trn>Day</digi:trn>
								</html:option>
								<html:option value="30">30 <digi:trn>Days</digi:trn>
								</html:option>
								<html:option value="60">60 <digi:trn>Days</digi:trn>
								</html:option>
								<html:option value="90">90 <digi:trn>Days</digi:trn>
								</html:option>
								<html:option value="180">180 <digi:trn>Days</digi:trn>
								</html:option>
								<html:option value="360">360 <digi:trn>Days</digi:trn>
								</html:option>
							</html:select></td>
					</tr>
					<tr>
						<td align="center" colspan="2"><input class="buttonx"
							type="submit" onclick="return confirmMsgDelete();"
							value="<digi:trn>delete</digi:trn>"> &nbsp;</td>
					</tr>
				</table>
			</td>
			<td>
				<table align="center" cellpadding="0" cellspacing="0" width="90%"
					border="0">
					<tr>
						<td>
							<!-- Other Links -->
							<table cellpadding="0" cellspacing="0" width="100">
								<tr>
									<td bgColor=#c9c9c7>
									<b style="font-size:12px; padding-left:5px;">
													<digi:trn key="aim:otherLinks">
													Other links
													</digi:trn>
												</b></td>
									<td background="module/aim/images/corner-r.gif" height="17"
										width=17></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td bgColor=#ffffff>
							<table cellPadding=5 cellspacing="1" width="100%" class="inside">
								<tr>
									<td class="inside"><digi:link href="/importexport.do">
											<digi:trn>Translation Manager</digi:trn>
										</digi:link></td>
								</tr>
								<tr>
									<td class="inside"><digi:link href="/admin.do" module="aim">
											<digi:trn key="aim:AmpAdminHome">
													Admin Home
													</digi:trn>
										</digi:link></td>
								</tr>
								<!-- end of other links -->
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</digi:form>