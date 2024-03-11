<%-- renders the contacts part of the Pledge Form --%>
<%-- the HTML is ready to be included in the page per se, so no css/javascript includes here! --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://digijava.org/aim" prefix="aim" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org" prefix="digi" %>
<%@ taglib uri="http://digijava.org/fields" prefix="field" %>
<%@ taglib uri="http://digijava.org/features" prefix="feature" %>
<%@ taglib uri="http://digijava.org/modules" prefix="module" %>
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm"%>

<digi:instance property="pledgeForm" />
<div id='pledge_contacts_area'>
	<jsp:include page="pledge_render_contact.jsp">
		<jsp:param name="ct_nr" value="1" />
		<jsp:param name="sectiontitle" value="Point of Contact at Donors Conference on March 31st" />		
	</jsp:include>
	
	<jsp:include page="pledge_render_contact.jsp">
		<jsp:param name="ct_nr" value="2" />
		<jsp:param name="sectiontitle" value="Point of Contact for Follow Up" />
		<jsp:param name="checkboxtext" value="Same As Original Point Of Contact" />
	</jsp:include>
</div>
