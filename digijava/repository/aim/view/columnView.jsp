<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<% String rowspan = request.getParameter("rowspan"); %>

<!-- start of columnView.jsp -->
				<logic:notEmpty name="records" property="title">
				<td align="center" height="21" rowspan="<%=rowspan%>">
				<bean:write name="records" property="title" />
				</td>
				</logic:notEmpty>
				
				<logic:notEmpty name="records" property="actualStartDate">
				<td align="center" height="21" rowspan="<%=rowspan%>">
				<bean:write name="records" property="actualStartDate" /></td></logic:notEmpty>
				
				<logic:notEmpty name="records" property="actualCompletionDate">
				<td align="center" height="21" rowspan="<%=rowspan%>">
				<bean:write name="records" property="actualCompletionDate" /></td></logic:notEmpty>
				
				<logic:notEmpty name="records" property="status">
				<td align="center" height="21" rowspan="<%=rowspan%>">
				<bean:write name="records" property="status" /></td></logic:notEmpty>
				
				<logic:notEmpty name="records" property="level">
				<td align="center" height="21" rowspan="<%=rowspan%>">
				<bean:write name="records" property="level" /></td></logic:notEmpty>
				
				<logic:notEmpty name="records" property="objective">
				<td align="center" height="21" rowspan="<%=rowspan%>">
				<bean:write name="records" property="objective" /></td></logic:notEmpty>
				
				<logic:notEmpty name="records" property="description">
				<td align="center" height="21" rowspan="<%=rowspan%>" width="800">
				<bean:define id="descriptionKey">
				<bean:write name="records" property="description" />
				</bean:define>						
				<digi:edit key="<%=descriptionKey%>" />
				</td></logic:notEmpty>
								
				<logic:notEmpty name="records" property="donors">
				<td align="center" height="21" rowspan="<%=rowspan%>">
				<logic:iterate name="records" id="donors" property="donors"> <%=donors%>	
				<br>
				</logic:iterate></td></logic:notEmpty>
				
				<logic:notEmpty name="records" property="sectors">
				<td align="center" height="21" rowspan="<%=rowspan%>">
				<logic:iterate name="records" id="sectors" property="sectors"> <%=sectors%>	
				<br>
				</logic:iterate></td></logic:notEmpty>
				
				<logic:notEmpty name="records" property="regions">
				<td align="center" height="21" rowspan="<%=rowspan%>">
				<logic:iterate name="records" id="regions" property="regions"> <%=regions%>	
				<br>
				</logic:iterate></td></logic:notEmpty>
				
				<logic:notEmpty name="records" property="contacts">
				<td align="center" height="21" rowspan="<%=rowspan%>">
				<logic:iterate name="records" id="contacts" property="contacts"> <%=contacts%>	
				<br>
				</logic:iterate></td></logic:notEmpty>
				
				<logic:notEmpty name="records" property="modality">
				<td align="center" height="21" rowspan="<%=rowspan%>" >
				<logic:iterate name="records" id="modality" property="modality"> <%=modality%>	
				<br>
				</logic:iterate></logic:notEmpty>
				
				<logic:notEmpty name="records" property="year">
				<bean:write name="records" property="year" /></logic:notEmpty>
				
				<logic:notEmpty name="records" property="ampId">
				<td align="center" height="21" rowspan="<%=rowspan%>" >
				<bean:write name="records" property="ampId" /></td></logic:notEmpty>

				<logic:notEmpty name="records" property="totalCommitment">
				<td align="center" height="21" rowspan="<%=rowspan%>" >
				<logic:notEqual name="records" property="totalCommitment" value="0">
				<bean:write name="records" property="totalCommitment" /></logic:notEqual></td></logic:notEmpty>

				<logic:notEmpty name="records" property="totalDisbursement">
				<td align="center" height="21" rowspan="<%=rowspan%>" >
				<logic:notEqual name="records" property="totalDisbursement" value="0">
				<bean:write name="records" property="totalDisbursement" /></logic:notEqual></td></logic:notEmpty>
				
<!-- end of columnView.jsp -->				