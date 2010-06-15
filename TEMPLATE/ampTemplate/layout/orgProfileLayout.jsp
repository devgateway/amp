<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<HTML>
    <digi:base />
    <digi:context name="digiContext" property="context"/>

    <HEAD>
        <TITLE><digi:trn>Organization Profile</digi:trn></TITLE>

        <script type="text/javascript">
            function exportPDF() {
            <digi:context name="url" property="context/module/moduleinstance/pdfExport.do" />
                    document.orgProfOrgProfileFilterForm.action="${url}";
                    document.orgProfOrgProfileFilterForm.target="_blank";
                    document.orgProfOrgProfileFilterForm.submit();

                }
                function exportWord() {
            <digi:context name="url" property="context/module/moduleinstance/wordExport.do" />
                    document.orgProfOrgProfileFilterForm.action="${url}";
                    document.orgProfOrgProfileFilterForm.target="_blank";
                    document.orgProfOrgProfileFilterForm.submit();
            
                }
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
          
                function showOrgProfileToolbar(){
                    var toolbar=document.getElementById("orgProfToolbarId");
                    toolbar.style.display="block";
                }
        </script>
        <script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
        <link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/container.css'/>"/>
        <script type="text/javascript" src='<digi:file src="module/aim/scripts/panel/yahoo-dom-event.js"/>' ></script>
        <script type="text/javascript" src='<digi:file src="module/aim/scripts/panel/container-min.js"/>' ></script>
        <script type="text/javascript" src='<digi:file src="module/aim/scripts/panel/connection-min.js"/>' ></script>
        <script type="text/javascript" src='<digi:file src="module/aim/scripts/panel/dragdrop-min.js"/>' ></script>
        <link rel="stylesheet" type="text/css" href="<digi:file src="module/aim/css/amptabs.css"/>"/>
        <style type="text/css">
            .tableHeaderCls {
                font-size: 12px;
                color: White;
                background-color:
                    rgb(55, 96, 145);
                font-family: Arial;
                font-weight: bold;
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

            .tableOdd{
                background-color:#FFFFFF;
                font-family: Arial;
                font-size:10px !important;
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
            .chartPlaceCss{
                padding: 20px 20px 20px 20px !important;
                background-color:#EDF5FF;
                font-size:10px;
                font-family:Arial,Helvetica,sans-serif;
            }
            .contentbox_border{
                clear:both;
                border: 1px solid black;
                border-width: 1px 1px 1px 1px;
            }
            body {
                margin:0px; padding:0px;
            }

            .settingOptions {
                height: 85px
            }
            .settingTitle{
                font-size: 14px;
                font-weight: bold;
                text-align: center;
                color: White;
                background-color:
                    rgb(55, 96, 145);

            }
            .optionRight{
                margin-left: 250px
            }
            .optionLeft{
                float:left;
                width:200px

            }
        </style>
    </HEAD>
    <BODY>
        <TABLE cellSpacing=0 cellPadding=0 width="100%"  border=0 valign="top" align="left">
            <TBODY>
                <TR>
                    <TD width="100%" bgColor=#323232 vAlign="center" align="left" height="10">
                        <digi:insert attribute="headerTop" />
                    </TD>
                </TR>
                <TR>
                    <TD width="100%" align="center" vAlign="top" bgcolor="#376091">
                        <TABLE cellSpacing=0 cellPadding=0 width="98%" border=0 vAlign="center" bgcolor="#376091">
                            <TBODY>
                                <TR bgColor=#376091 height="15">
                                    <TD align="left" vAlign="center" height="15">
                                        <digi:insert attribute="headerMiddle" />
                                    </TD>
                                    <TD align="right" vAlign="top" height="15">
                                        <digi:insert attribute="loginWidget" />
                                    </TD>
                                </TR>
                            </TBODY>
                        </TABLE>
                    </TD>
                </TR>
                <TR>
                    <TD width="100%" vAlign="top" align="left" bgColor=#ffffff>
                        <TABLE width="100%"  vAlign="top" align="left" border="0">
                            <TBODY>
                                <TR>
                                    <TD vAlign="top" align="left">
                                        <digi:insert attribute="filters"/>
                                    </TD>
                                </TR>

                                <TR>
                                    <TD>&nbsp;</TD>
                                </TR>
                                <TR>
                                    <TD>
                                        <TABLE cellpadding="20" cellspacing="20" width="100%" >
                                            <TBODY>
                                                <TR>
                                                    <TD VALIGN="TOP" style="width: 50%">
                                                <c:set var="stylePlace1">
                                                    display: none;
                                                </c:set>
                                                <feature:display name="orgprof_chart_place1" module="Org Profile">
                                                    <c:set var="stylePlace1">
                                                        z-index: 1000;
                                                    </c:set>
                                                </feature:display>
                                                <div style="${stylePlace1}">
                                                    <digi:insert attribute="chart1">
                                                        <digi:put name="widget-teaser-param" >orgprof_chart_place1</digi:put>
                                                    </digi:insert>
                                                </div>
                                                </TD>
                                                <TD  VALIGN="TOP"    >
                                                <c:set var="stylePlace2">
                                                    display: none; z-index: 1000
                                                </c:set>
                                                <feature:display name="orgprof_chart_place2" module="Org Profile">
                                                    <c:set var="stylePlace2">
                                                        z-index: 1000
                                                    </c:set>
                                                </feature:display>
                                                <div style="${stylePlace2}">
                                                    <digi:insert attribute="chart2">
                                                        <digi:put name="widget-teaser-param">orgprof_chart_place2</digi:put>
                                                    </digi:insert>
                                                </div>
                                            </TD>
                                        </TR>
                                        <TR>
                                            <TD   VALIGN="TOP" style="width: 50%">
                                                <c:set var="stylePlace3">
                                                    display: none; z-index: 1000
                                                </c:set>
                                                <feature:display name="orgprof_chart_place3" module="Org Profile">
                                                    <c:set var="stylePlace3">
                                                        z-index: 1000
                                                    </c:set>
                                                </feature:display>
                                                <div style="${stylePlace3}">
                                                    <digi:insert attribute="chart3">
                                                        <digi:put name="widget-teaser-param">orgprof_chart_place3</digi:put>
                                                    </digi:insert>
                                                </div>
                                            </TD>
                                            <TD  VALIGN="TOP">
                                                <c:set var="stylePlace4">
                                                    display: none; z-index: 1000
                                                </c:set>
                                                <feature:display name="orgprof_chart_place4" module="Org Profile">
                                                    <c:set var="stylePlace4">
                                                        z-index: 1000
                                                    </c:set>
                                                </feature:display>
                                                <div style="${stylePlace4}">
                                                    <digi:insert attribute="chart4">
                                                        <digi:put name="widget-teaser-param">orgprof_chart_place4</digi:put>
                                                    </digi:insert>
                                                </div>
                                            </TD>
                                        </TR>
                                        <TR>
                                            <TD  VALIGN="TOP"  style="width: 50%" >
                                                <c:set var="stylePlace5">
                                                    display: none; z-index: 1000
                                                </c:set>
                                                <feature:display name="orgprof_chart_place5" module="Org Profile">
                                                    <c:set var="stylePlace5">
                                                        z-index: 1000
                                                    </c:set>
                                                </feature:display>
                                                <div style="${stylePlace5}"
                                                     <digi:insert attribute="chart5">
                                                         <digi:put name="widget-teaser-param">orgprof_chart_place5</digi:put>
                                                     </digi:insert>
                                            </div>
                                        </TD>
                                        <TD  VALIGN="TOP"   >
                                            <c:set var="stylePlace6">
                                                display: none; z-index: 1000
                                            </c:set>
                                            <feature:display name="orgprof_chart_place6" module="Org Profile">
                                                <c:set var="stylePlace6">
                                                    z-index: 1000
                                                </c:set>
                                            </feature:display>
                                            <div style="${stylePlace6}">
                                                <digi:insert attribute="chart6">
                                                    <digi:put name="widget-teaser-param">orgprof_chart_place6</digi:put>
                                                </digi:insert>
                                            </div>
                                        </TD>
                                    </TR>
                                    <TR>
                                        <TD VALIGN="TOP" colspan="2"   >
                                            <c:set var="stylePlace7">
                                                display: none; z-index: 1000
                                            </c:set>
                                            <feature:display name="orgprof_chart_place7" module="Org Profile">
                                                <c:set var="stylePlace7">
                                                    z-index: 1000
                                                </c:set>
                                            </feature:display>
                                            <div style="${stylePlace7}">
                                                <digi:insert attribute="chart7">
                                                    <digi:put name="widget-teaser-param">orgprof_chart_place7</digi:put>
                                                </digi:insert>
                                            </div>
                                </TD>
                            </TR>
                        </TBODY>
                    </TABLE>
                </TD>
            </TR>
                </TBODY>
            </TABLE>
        </TD>
    </TR>
            <TR>
                <TD width="100%" >
                    <digi:insert attribute="footer" />
                </TD>
            </TR>
    </TABLE>
    <script language="javascript" type="text/javascript">
        setStripsTable("tableEven", "tableOdd");
        setHoveredTable();
        showOrgProfileToolbar();
    </script>
</BODY>
</HTML>



