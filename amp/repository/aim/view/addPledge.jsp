<%@page import="org.digijava.kernel.translator.TranslatorWorker"%>
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm, org.digijava.module.categorymanager.dbentity.AmpCategoryValue, java.util.*, org.digijava.module.aim.dbentity.*, org.springframework.beans.BeanWrapperImpl" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>

<!--<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>-->
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<% int indexFund = 0; 
PledgeForm pledgeForm = (PledgeForm) session.getAttribute("pledgeForm");
%>
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script src="/repository/bootstrap/hacks.js"></script>

<iframe src="/aim/selectPledgeLocation.do?edit=false" width="100%" scrolling="no" seamless="seamless" frameborder="0" marginheight="0" marginwidth="0" name="pledges_locations_name"></iframe>


