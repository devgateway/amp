<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>



<table cellpadding="3" cellspacing="1" rules="all" frame="border" style="margin-left: 5px;border-color:#999999">
<tr>
<th>Idx</th>
<th>Exception</th>
<th>Date</th>
<th>Is Error</th>
<th>Elapsed (ms)</th>
</tr>
<logic:notEmpty name="patchLogs">
<logic:iterate id="log" name="patchLogs" scope="request" indexId="idx">
<tr>
<td><b><bean:write name="idx"/></b></td>
<td>
<logic:notEmpty name="log" property="log">
<a style='text-decoration: underline' onclick="loadLogBody(${log.id})">${log.logLabel}</a>;
</logic:notEmpty>
<logic:empty name="log" property="log">
[none]
</logic:empty>
</td>
<td><bean:write name="log" property="date"/></td>
<td><bean:write name="log" property="error"/></td>
<td><bean:write name="log" property="elapsed"/></td>
</tr>
</logic:iterate>
</logic:notEmpty>
</table>