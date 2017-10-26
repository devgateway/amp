<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<!-- <b>T/SITEDOMAIN LEFT</b>&nbsp; -->

<table border="0" width="100%" cellpadding="0" cellspacing="5">
<tr>
	<td width="100%" align="center" valign="top">
		<table border="0" cellpadding="3" cellspacing="0" width="100%">
			<tr>
				<td align="center" valign="top">
					<digi:insert attribute="highlightItem" />
				</td>
			</tr>
			<tr>
				<td align="center" valign="top">
				   <digi:insert attribute="cms" />	
				</td>
			</tr>
		</table>
	</td>
	<td valign="top" width="150">
		<digi:insert attribute="forum" />
	</td>
</tr>
</table>
