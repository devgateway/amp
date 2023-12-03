<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet"/>

<c:set var="translationDelete">
    <digi:trn jsFriendly="true">Do you really want to remove the result?</digi:trn>
</c:set>

<c:set var="translationDeleteSelected">
    <digi:trn jsFriendly="true">Do you really want to remove the selected results?</digi:trn>
</c:set>

<jsp:include page="../../aim/view/scripts/newCalendar.jsp" flush="true" />

<script language="JavaScript" type="text/javascript">
    <jsp:include page="../../aim/view/scripts/calendar.js.jsp"  />
</script>

<script language="JavaScript">

    function deleteResult() {
        return confirm('${translationDelete}');
    }

    function deleteSelected() {
        var chk = document.getElementsByTagName('input');
        var ids = [];
        for(var i=0;i<chk.length;i++){
            if(chk[i].type == 'checkbox' && chk[i].checked){
                if (chk[i].value) {
                    ids.push(chk[i].value);
                }
            }
        }
        if(ids.length > 0) {
            if(confirm('${translationDeleteSelected}')) {
                document.getElementById("selectedIds").value = ids;
                document.interchangeResultForm.action = "/budgetIntegration/interchangeResult.do?deleteAll=true";
                document.interchangeResultForm.submit();
            }
        }
    }

    function exportXSL() {
        document.interchangeResultForm.action = "/interchangeResult.do?export=true";
        document.interchangeResultForm.target = "_blank";
        document.interchangeResultForm.submit();
    }
    function show() {
        document.interchangeResultForm.submit();
    }

    function resetAll() {
        var date = document.getElementById('filterDate');
        date.value = '';
        document.getElementById("pageSizeCombo").selectedIndex = 0;
    }

    function selectAll(){
        var chkAll = document.getElementById('select-all-ckb');
        var chk = document.getElementsByTagName('input');
        for(var i=0;i<chk.length;i++){
            if(chk[i].type == 'checkbox'){
                if (chkAll.checked) {
                    chk[i].checked = true;
                }else{
                    chk[i].checked = false;
                }
            }
        }
    }
</script>


<digi:instance property="interchangeResultForm"/>
<digi:context name="digiContext" property="context"/>
<digi:form action="/interchangeResult.do" method="post">
<h1 class="admintitle"><digi:trn>Data Import Manager Results</digi:trn></h1>

<table width="1000" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <ht width=680>
            <table>
                <tr>
                    <td width=700 vAlign="top" colspan="7"
                        style="border-left: 1px solid #D0D0D0; border-right: 1px solid #D0D0D0; border-bottom: 1px solid #D0D0D0;">
                        <table width="100%" cellspacing="1" cellspacing="1">
                            <tr>
                                <td noWrap width=700 vAlign="top">
                                    <table cellPadding=5 cellspacing="0" width="100%" style="margin-bottom:15px;">
                                        <tr>
                                            <td bgcolor=#c7d4db align=center colspan=8
                                                style="background-color: #F2F2F2; border: 1px solid #D0D0D0; padding: 5px;">
                                                <digi:errors/>
                                                    <span style="font-size: 12px; font-weight: bold;">
								                        <digi:trn>List of Results</digi:trn>
                                                    </span>
                                            </td>
                                        </tr>
                                        <tr style="background-color: #F2F2F2; padding: 5px;">
                                            <td width="50px" class="usersSelectForm"
                                                style="border-top: 1px solid #D0D0D0; border-bottom: 1px solid #D0D0D0;">
                                                <digi:trn>Date:</digi:trn></td>
                                            <td width="50px"
                                                style="border-top: 1px solid #D0D0D0; border-bottom: 1px solid #D0D0D0;">
                                                <html:text styleId="filterDate" property="filterDate" readonly="true"/>
                                                <a id="dateLink" href='javascript:pickDateById("dateLink","filterDate")'>
                                                    <img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
                                                </a></td>
                                                <td width="40px" align="left" class="usersSelectForm"
                                                style="border-top: 1px solid #D0D0D0; border-bottom: 1px solid #D0D0D0;">
                                                <digi:trn key="aim:results">Results</digi:trn>&nbsp;</td>
                                            <td width="50px"
                                                style="border-top: 1px solid #D0D0D0; border-bottom: 1px solid #D0D0D0;">
                                                <html:select property="pageSize" styleClass="inp-text" styleId="pageSizeCombo">
                                                    <html:option value="10">10</html:option>
                                                    <html:option value="20">20</html:option>
                                                    <html:option value="50">50</html:option>
                                                    <html:option value="-1">ALL</html:option>
                                                </html:select></td>
                                            <td width="300px" style="border-top: 1px solid #D0D0D0; border-bottom: 1px solid #D0D0D0; border-right: 1px solid #D0D0D0;">
                                                <c:set var="translation">
                                                    <digi:trn>Show</digi:trn>
                                                </c:set>
                                                <input type="button" value="${translation}" class="dr-menu" style="font-family: verdana; font-size: 11px;" onclick="show()"/>
                                                <c:set var="translation">
                                                    <digi:trn>Reset</digi:trn>
                                                </c:set>
                                                <input type="button" value="${translation}" class="dr-menu" style="font-family: verdana; font-size: 11px;" onclick="resetAll()"/>
                                                <c:set var="translation">
                                                    <digi:trn>Delete Selected</digi:trn>
                                                </c:set>
                                                <input type="hidden" id="selectedIds" name="ids">
                                                <input type="button" value="${translation}" class="dr-menu" style="font-family: verdana; font-size: 11px;" onclick="deleteSelected()"/>
                                            </td>
                                        </tr>
                                    </table>
                                    <table bgColor=#ffffff cellpadding="0" cellspacing="0"
                                           width="100%">
                                        <tr bgColor=#f4f4f2>
                                            <td valign="top">
                                                <table align="center" bgColor=#f4f4f2 cellpadding="0"
                                                       cellspacing="0" width="90%" border="0">
                                                    <tr>
                                                        <td bgColor=#ffffff>
                                                            <table border="0" cellpadding="0" cellspacing="0"
                                                                   width="100%">
                                                                <tr bgColor=#dddddb>
                                                                    <!-- header -->
                                                                    <td bgColor=#f2f2f2 height="25" align="center"
                                                                        colspan="5" style="border: 1px solid #ebebeb;"><B>
                                                                        <digi:trn>Results</digi:trn> </b></td>
                                                                    <!-- end header -->
                                                                </tr>
                                                                <tr>
                                                                    <td width="100%" class="report">
                                                                        <table width="734" RULES=ALL FRAME=VOID
                                                                               id="viewAllUsers" border="0" cellspacing="1"
                                                                               cellpadding="1">

                                                                            <c:if test="${empty interchangeResultForm.results}">
                                                                                <tr>
                                                                                    <td colspan="5">
                                                                                        <b><digi:trn>No Result Present</digi:trn>
                                                                                    </b></td>
                                                                                </tr>
                                                                            </c:if>
                                                                            <c:if test="${not empty interchangeResultForm.results}">
                                                                                <thead background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif">
                                                                                <tr>
                                                                                    <td height="30" width="90">
                                                                                        <input type="checkbox" id="select-all-ckb" onclick="selectAll()">
                                                                                    </td>
                                                                                    <td height="30" width="220">
                                                                                        <c:if test="${not empty interchangeResultForm.sortBy && interchangeResultForm.sortBy!='dateAscending'}">
                                                                                            <digi:link ampModule="budgetIntegration" href="/interchangeResult.do?sortBy=dateAscending">
                                                                                                <b><digi:trn>Date</digi:trn></b>
                                                                                            </digi:link>
                                                                                        </c:if>
                                                                                        <c:if test="${empty interchangeResultForm.sortBy || interchangeResultForm.sortBy=='dateAscending'}">
                                                                                            <digi:link ampModule="budgetIntegration" href="/interchangeResult.do?sortBy=dateDescending&reset=false">
                                                                                                <b><digi:trn>Date</digi:trn></b>
                                                                                            </digi:link>
                                                                                        </c:if>
                                                                                        <c:if test="${not empty interchangeResultForm.sortBy && interchangeResultForm.sortBy=='dateAscending'}">
                                                                                            <img src="/repository/aim/images/up.gif"/>
                                                                                        </c:if>
                                                                                        <c:if test="${not empty interchangeResultForm.sortBy && interchangeResultForm.sortBy=='dateDescending'}">
                                                                                            <img src="/repository/aim/images/down.gif"/>
                                                                                        </c:if>
                                                                                    </td>
                                                                                    <td height="30" width="220"><b><digi:trn>Status</digi:trn></b></td>
                                                                                    <td height="30" width="220"><b><digi:trn>Project Id</digi:trn></b></td>
                                                                                    <td height="30" width="220"><b><digi:trn>Activity Id</digi:trn></b></td>
                                                                                    <td height="30" width="220"><b><digi:trn>Operation</digi:trn></b></td>
                                                                                    <td height="30" width="420"><b><digi:trn>Error Details</digi:trn></b></td>
                                                                                    <td height="30" width="150" class="ignore"><b><digi:trn>Actions</digi:trn></b></td>
                                                                                </tr>
                                                                                </thead>

                                                                                <tbody class="yui-dt-data">
                                                                                <c:forEach var="res" items="${interchangeResultForm.results}">
                                                                                    <tr>
                                                                                        <td height="30"><input type="checkbox" id="select-${res.id}" value="${res.id}"></td>
                                                                                        <td height="30"><c:out value="${res.date}"/></td>
                                                                                        <td height="30"><c:out value="${res.status}"/></td>
                                                                                        <td height="30"><c:out value="${res.projectId}"/></td>
                                                                                        <td height="30"><c:out value="${res.ampActivityId}"/></td>
                                                                                        <td height="30"><c:out value="${res.operation}"/></td>
                                                                                        <td height="30"><c:out value="${res.errorDetails}"/></td>
                                                                                        <td height="30" nowrap="nowrap" class="ignore">
                                                                                            <c:set var="translation">
                                                                                                <digi:trn>Delete</digi:trn>
                                                                                            </c:set>
                                                                                            <digi:link ampModule="budgetIntegration" href="/interchangeResult.do?delete=true&id=${res.id}" onclick="deleteResult()">${translation}</digi:link>
                                                                                        </td>
                                                                                    </tr>
                                                                                </c:forEach>
                                                                                </tbody>
                                                                            </c:if>
                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                                <!-- end page logic -->
                                                                <!-- page logic for pagination -->
                                                                <logic:notEmpty name="interchangeResultForm" property="results">
                                                                    <tr>
                                                                        <td colspan="4" nowrap="nowrap">
                                                                            <digi:trn>Pages:</digi:trn>
                                                                            <c:if test="${interchangeResultForm.currentPage > 1}">
                                                                                <jsp:useBean id="urlParamsFirst" type="java.util.Map" class="java.util.HashMap"/>
                                                                                <c:set target="${urlParamsFirst}" property="page" value="1"/>
                                                                                <c:set var="translation"><digi:trn>First Page</digi:trn></c:set>
                                                                                <digi:link ampModule="budgetIntegration" href="/interchangeResult.do" style="text-decoration=none" name="urlParamsFirst" title="${translation}">
                                                                                    &lt;&lt;
                                                                                </digi:link>

                                                                                <jsp:useBean id="urlParamsPrevious" type="java.util.Map" class="java.util.HashMap"/>
                                                                                <c:set target="${urlParamsPrevious}" property="page" value="${interchangeResultForm.currentPage - 1}"/>
                                                                                <c:set var="translation"><digi:trn>Previous Page</digi:trn></c:set>
                                                                                <digi:link ampModule="budgetIntegration" href="/interchangeResult.do" name="urlParamsPrevious" style="text-decoration=none" title="${translation}">
                                                                                    &lt;
                                                                                </digi:link>
                                                                            </c:if>
                                                                            <logic:iterate name="interchangeResultForm" property="pages" id="pageNumber" type="java.lang.Integer">
                                                                                <jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
                                                                                <c:set target="${urlParams1}" property="page"><%=pageNumber%></c:set>
                                                                                <c:if test="${interchangeResultForm.currentPage == pageNumber}">
                                                                                    <span color="#FF0000"><%=pageNumber%></span>
                                                                                </c:if>
                                                                                <c:if test="${interchangeResultForm.currentPage != pageNumber}">
                                                                                    <c:set var="translation">
                                                                                        <digi:trn key="aim:clickToViewNextPage">Click here to go to Next Page</digi:trn>
                                                                                    </c:set>
                                                                                    <digi:link ampModule="budgetIntegration" href="/interchangeResult.do" name="urlParams1" title="${translation}"><%=pageNumber%></digi:link>
                                                                                </c:if>
                                                                                |&nbsp;
                                                                            </logic:iterate>
                                                                            <c:if test="${interchangeResultForm.currentPage != interchangeResultForm.lastPage}">
                                                                                <jsp:useBean id="urlParamsNext" type="java.util.Map" class="java.util.HashMap"/>
                                                                                <c:set target="${urlParamsNext}" property="page" value="${interchangeResultForm.currentPage + 1}"/>
                                                                                <c:set var="translation">
                                                                                    <digi:trn key="aim:nextpage">Next Page</digi:trn>
                                                                                </c:set>
                                                                                <digi:link ampModule="budgetIntegration" href="/interchangeResult.do" style="text-decoration=none" name="urlParamsNext" title="${translation}">
                                                                                    &gt;
                                                                                </digi:link>
                                                                                <jsp:useBean id="urlParamsLast" type="java.util.Map" class="java.util.HashMap"/>
                                                                                <c:set target="${urlParamsLast}" property="page" value="${interchangeResultForm.lastPage}"/>
                                                                                <c:set var="translation"><digi:trn key="aim:lastpage">Last Page</digi:trn></c:set>
                                                                                <digi:link ampModule="budgetIntegration" href="/interchangeResult.do" style="text-decoration=none" name="urlParamsLast" title="${translation}">
                                                                                    &gt;&gt;
                                                                                </digi:link>
                                                                            </c:if>&nbsp;
                                                                            <c:out value="${interchangeResultForm.currentPage}"/>&nbsp;<digi:trn key="aim:of">of</digi:trn>&nbsp;
                                                                            <c:out value="${interchangeResultForm.lastPage}"/>
                                                                        </td>
                                                                    </tr>
                                                                </logic:notEmpty>
                                                                <!-- end page logic for pagination -->
                                                                </table>
                                                            </td>
                                                        </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td noWrap width="100%" vAlign="top">&nbsp;</td>
                            </tr>
                        </table>
                    </td>
                    <td width=20>&nbsp;</td>
                    <td width=300 valign=top>
                        <table align="center" cellpadding="0" cellspacing="0"
                               width="300" border="0">
                            <tr>
                                <td>
                                    <!-- Other Links -->
                                    <table cellpadding="0" cellspacing="0" width="100">
                                        <tr>
                                            <td bgColor=#c9c9c7 class=box-title><digi:trn
                                                    key="aim:otherLinks">
                                                <b style="font-weight: bold; font-size: 12px; padding-left:5px; color:#000000;"><digi:trn>Other links</digi:trn></b>
                                            </digi:trn></td>
                                            <td background="ampModule/aim/images/corner-r.gif"
                                                height="17" width=17>&nbsp;</td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td bgColor=#ffffff>
                                    <table cellPadding=0 cellspacing="0" width="100%" class="inside">
                                        <tr>
                                            <td class="inside"><digi:img
                                                    src="ampModule/aim/images/arrow-014E86.gif" width="15"
                                                    height="10"/> <digi:link ampModule="aim" href="/admin.do">
                                                <digi:trn key="aim:AmpAdminHome">
                                                    Admin Home
                                                </digi:trn>
                                            </digi:link></td>
                                        </tr>
                                        <!-- end of other links -->
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</digi:form>