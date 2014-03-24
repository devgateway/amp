<%--historical place --%>
<c:set var="act">${param.extraAction}</c:set>
<%--<c:set var="rll_ajax">render_locations_list</c:set>  --%>
<%
	if ("add_locations_refresh_data".equals(request.getParameter("extraAction"))) // ajax?
	{
		%><jsp:include page="pledgelocationslist.jsp"></jsp:include><%  
	} else if ("add_locations_refresh_add".equals(request.getParameter("extraAction")))
	{
		%><jsp:include page="pledgelocationsAddPledge.jsp"></jsp:include><%
    } else 
    { // not ajax: render the full bootstrap iframe 
%>
<%--
	<c:set var="numeric_value_only_msg"><digi:trn jsFriendly='true' key="aim:addSecorNumericValueErrorMessage">Please enter numeric value only</digi:trn></c:set>
	<c:set var="sum_cannot_exceed_100_msg"><digi:trn jsFriendly='true'>Sum of percentages can not exceed 100</digi:trn></c:set>
 --%>
<jsp:include page="/repository/aim/view/pledgeform/pledgeFormMain.jsp"></jsp:include>
<%
    } // the big "otherwise"
%>
