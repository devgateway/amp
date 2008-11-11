<%@ page session="true" contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<jp:mondrianQuery 
	id="query01" 
	dataSource="drcStagingDS"  
	catalogUri="/WEB-INF/queries/AMP.xml"
>
SELECT NON EMPTY CROSSJOIN({[Time]},{[Measures].[Actual Commitments], [Measures].[Activity Count]}) ON COLUMNS,
NON EMPTY CROSSJOIN({[Financing Instrument]},CROSSJOIN({[Terms of Assistance]},CROSSJOIN ({[Donor]},CROSSJOIN({[Primary Sector]},{[Activity]})))) ON ROWS 
FROM [Donor Funding] 
</jp:mondrianQuery>

