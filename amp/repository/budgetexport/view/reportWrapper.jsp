<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi"%>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="org.dgfoundation.amp.ar.AmpARFilter"%>
<%@ taglib uri="/src/main/resources/tld/featureVisibility.tld" prefix="feature"%>
<%@ taglib uri="/src/main/resources/tld/moduleVisibility.tld" prefix="module"%>

<jsp:include page="/aim/viewNewAdvancedReport.do" >
	<jsp:param value="${param.ampReportId}" name="ampReportId"/>
	<jsp:param value="reset" name="view"/>
	<jsp:param value="false" name="widget"/>
</jsp:include>
