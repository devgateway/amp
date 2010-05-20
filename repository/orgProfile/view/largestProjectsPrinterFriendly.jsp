<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<style type="text/css">
    .tableHeaderCls {
        font-size: 12px;
        color: White;
        background-color:
            rgb(55, 96, 145);
        font-family: Arial;
        text-align:center;
        border-style:solid;
        border-right:none;
        border-bottom:none;
        border-width:1px;
        border-color: rgb(255, 255, 255);
    }

    .tableEven {
        background-color:#dbe5f1;
        font-size:10px;
        font-family: Arial;
        padding:2px;
    }
    td {
        font-size:10px;
        font-family: Arial;
    }

    .tableOdd {
        background-color:#FFFFFF;
        font-family: Arial;
        font-size:10px;!important
        padding:2px;
    }

    .selectDropDown {
        font-family: Arial;
        font-size:10px;
        width:200px;
    }

    .Hovered {
        background-color:#a5bcf2;
    }


    .toolbar{
        width: 350px;
        background: #addadd;
        background-color: #addadd;
        padding: 3px 3px 3px 3px;
        position: relative;
        top: 10px;
        left: 10px;
        bottom: 100px;

    }

</style>

<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
<script type="text/javascript">
    function setStripsTable(classOdd, classEven) {
        var tableElements = $(".tableElement");
        for(var j = 0; j < tableElements.length; j++) {
            rows = tableElements[j].getElementsByTagName('tr');
            for(var i = 0, n = rows.length; i < n; ++i) {
                if(i%2 == 0)
                    rows[i].className = classEven;
                else
                    rows[i].className = classOdd;
            }
            rows = null;

        }

    }
    function setHoveredTable() {
        var tableElements = $(".tableElement");
        for(var j = 0; j < tableElements.length; j++) {
            if(tableElements){
                var className = 'Hovered',
                pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
                rows      = tableElements[j].getElementsByTagName('tr');

                for(var i = 0, n = rows.length; i < n; ++i) {
                    rows[i].onmouseover = function() {
                        this.className += ' ' + className;
                    };
                    rows[i].onmouseout = function() {
                        this.className = this.className.replace(pattern, ' ');

                    };
                }
                rows = null;
            }
        }
    }
</script>
<digi:instance property="orgProfLargestProjectsForm"/>

<digi:link styleId="printWin" href="#" onclick="window.print(); return false;">
    <img alt="print preview" src="/TEMPLATE/ampTemplate/images/print_icon.gif" border="0" >
</digi:link>
<table border="0"  bgcolor="#dddddd" width="100%"  class="tableElement" cellspacing="0" cellpadding="0">
    <tr>
        <th colspan="4" class="tableHeaderCls">
            <c:choose>
                <c:when test="${sessionScope.orgProfileFilter.largestProjectNumb==-1}">
                    <digi:trn>All</digi:trn>
                </c:when>
                <c:otherwise>
                    ${sessionScope.orgProfileFilter.largestProjectNumb}
                </c:otherwise>
            </c:choose><digi:trn>LARGEST PROJECTS</digi:trn>(${sessionScope.orgProfileFilter.year-1})</th>
    </tr>
    <tr>
        <td class="tableHeaderCls"><digi:trn>Project title</digi:trn></td>
        <td class="tableHeaderCls"><digi:trn>Commitment</digi:trn>(${sessionScope.orgProfileFilter.currName})</td>
        <td class="tableHeaderCls"><digi:trn>Disbursement</digi:trn>(${sessionScope.orgProfileFilter.currName})</td>
        <td class="tableHeaderCls"><digi:trn>Sector</digi:trn></td>
    </tr>

    <c:forEach items="${orgProfLargestProjectsForm.projects}" var="project"  varStatus="status">
        <tr>
            <td nowrap>
                <c:choose>
                    <c:when test="${empty project.fullTitle}">
                        ${project.title}
                    </c:when>
                    <c:otherwise>
                        ${project.fullTitle}
                    </c:otherwise>
                </c:choose>

            </td>
            <td align="center">${project.amount}</td>
            <td align="center">${project.disbAmount}</td>
            <td>
                ${project.sectorNames}
            </td>
        </tr>
    </c:forEach>

</table>
<script language="javascript" type="text/javascript">
    setStripsTable("tableEven", "tableOdd");
    setHoveredTable();
</script>
