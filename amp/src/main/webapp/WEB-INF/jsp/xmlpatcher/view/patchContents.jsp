<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>


<logic:present name="patch">
<table cellpadding="3" cellspacing="1" rules="rows" frame="box" style="margin-left: 5px;border-color:#999999;">
<tr><td bgcolor="#0066cc" style="color: #ffffff; font-size: larger;">Patch ID</td><td style="font-size: larger;"><bean:write name="patch" property="patchId"/></td></tr>
<tr><td bgcolor="#0066cc" style="color: #ffffff; font-size: larger;">Location</td><td style="font-size: larger;"><bean:write name="patch" property="location"/></td></tr>
</table>

<pre class="sh_xml" style="white-space: -moz-pre-wrap;white-space: -pre-wrap;white-space: -o-pre-wrap;white-space: pre-wrap;word-wrap: break-word;width:90%">
<bean:write name="patchContents"/>
</pre>
</logic:present>