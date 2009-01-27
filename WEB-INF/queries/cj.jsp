<%@ page session="true" contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<jsp:include page="PrepareQuery.jsp"/>

<jp:mondrianQuery 
	id="query01" 
	dataSource="ampDS"  
	catalogUri="/WEB-INF/queries/AMP.xml"
	dynResolver="org.digijava.module.mondrian.query.SchemaManager"
>
SELECT NON EMPTY CROSSJOIN({[Donor Dates]},{[Measures].[raw Actual Commitments]}) ON COLUMNS,
NON EMPTY CROSSJOIN({[Financing Instrument]},CROSSJOIN({[Terms of Assistance]},CROSSJOIN ({[Donor]},CROSSJOIN({[Primary Sector]},{[Activity]})))) ON ROWS 
FROM [Donor Funding Weighted] 
</jp:mondrianQuery>





