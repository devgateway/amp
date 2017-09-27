<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>



<div>

   <digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
   <c:set var="trnDataExchangeExportTitle">
    <digi:trn key="dataexchange:trnDataExchangeExportTitle">Click here to view Data Export Manager</digi:trn>
   </c:set>
   <digi:link module="dataExchange"  href="/exportWizard.do?method=prepear" title="${trnDataExchangeExportTitle}" >
        <digi:trn key="dataexchange:DataExportManager">Data Export Manager</digi:trn>
   </digi:link>
</div>


<div>

   <digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>
   <c:set var="trnDataExchangeExportTitle">
    <digi:trn key="dataexchange:trnDataExchangeImportTitle">Click here to view Data Import Manager</digi:trn>
   </c:set>
   <digi:link module="dataExchange"  href="/import.do" title="${trnDataExchangeExportTitle}" >
        <digi:trn key="dataexchange:DataImportManager">Data Import Manager</digi:trn>
   </digi:link>
</div>

