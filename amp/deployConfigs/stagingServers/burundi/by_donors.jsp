<%@ page session="true" contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<jp:mondrianQuery 
	id="query01" 
	dataSource="burundiStagingDS"  
	catalogUri="/WEB-INF/queries/AMP.xml"
>

    
SELECT {[Measures].[Actual Commitments], [Measures].[Actual Disbursements],[Measures].[Planned Commitments], [Measures].[Planned Disbursements]} ON COLUMNS,
{[Donor]} ON ROWS 
FROM [Donor Funding] 
</jp:mondrianQuery>




<c:set var="title01" scope="session">Test Query uses Mondrian OLAP</c:set>
