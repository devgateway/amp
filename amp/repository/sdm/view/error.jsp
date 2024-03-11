
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>
	<digi:errors/>
	<digi:instance property="sdmForm"/>
	<table width="60%" border="0">
		<tr>
			<td>	Please resubmit your entry:
			</td>
		</tr>
		<tr>
			<td>	You must specify something for <b>Content Title</b>
			</td>
		</tr>
		<tr>
			<td>	Please click on the back button on your browser and resubmit your entry.
			</td>
		</tr>
	</table>