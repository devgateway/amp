<%-- renders the contacts part of the Pledge Form --%>
<%-- the HTML is ready to be included in the page per se, so no css/javascript includes here! --%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/c.tld" prefix="c" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/aim.tld" prefix="aim" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/digijava.tld" prefix="digi" %>
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm"%>

<digi:instance property="pledgeForm" />
<div id='pledge_contacts_area'>
	<c:set var="sectiontitle" value="Point of Contact at Donors Conference on March 31st" />
	<c:set var="contact_var" value="${pledgeForm.contact1}" />
	<c:set var="ct_nr" value="1" />
	<%@include file="pledge_render_contact_view.jspf" %>
	
	<c:set var="sectiontitle" value="Point of Contact for Follow Up" />
	<c:set var="contact_var" value="${pledgeForm.contact2}" />
	<c:set var="ct_nr" value="2" />
	<%@include file="pledge_render_contact_view.jspf" %>	
</div>
