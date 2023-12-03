<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<HTML>
    <digi:base />
    <digi:context name="digiContext" property="context"/>

    <HEAD>
        <TITLE><digi:trn>Aid Management Platform</digi:trn> - <digi:trn>Organization Profile</digi:trn></TITLE>

        <script type="text/javascript">
            function exportPDF() {
            <digi:context name="url" property="context/ampModule/moduleinstance/pdfExport.do" />
                    document.orgProfOrgProfileFilterForm.action="${url}";
                    document.orgProfOrgProfileFilterForm.target="_blank";
                    document.orgProfOrgProfileFilterForm.submit();

                }
                function exportWord() {
            <digi:context name="url" property="context/ampModule/moduleinstance/wordExport.do" />
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
                window.onload=showOrgProfileToolbar;
        </script>
        <script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/asynchronous.js"/>"></script>
	<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

		<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
        <script type="text/javascript" src='<digi:file src="ampModule/aim/scripts/panel/connection-min.js"/>' ></script>
        <link rel="stylesheet" type="text/css" href="<digi:file src="ampModule/aim/css/amptabs.css"/>"/>

        <style type="text/css">
            .tableHeaderCls {
                font-size: 12px;
                color: White;
                background-color:
                    rgb(55, 96, 145);
                font-family:Arial,Helvetica,sans-serif;
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
                font-family:Arial,Helvetica,sans-serif;
                padding:2px;
            }


            .tableOdd{
                background-color:#FFFFFF;
                font-family:Arial,Helvetica,sans-serif;
                font-size:10px !important;
                padding:2px;
            }

            .selectDropDown {
                font-family:Arial,Helvetica,sans-serif;
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
                padding: 10px 10px 10px 10px !important;
                background-color:#EDF5FF;
                font-size:10px;
                font-family:Arial,Helvetica,sans-serif;
            }
            .contentbox_border{
                clear:both;
                border: 1px solid black;       
            }
            .contentboxEmpty{
                clear:both;
                padding:4px 4px 4px 4px;
                background: #EDF5FF;
                border: 1px solid black;
                border-width: 1px 1px 0px 1px;
                font-size: 8pt !important;
            }
            .noTopBorder{
                border-width: 0px 1px 1px 1px;
            }
            .topBorder{
                border-width: 1px 1px 1px 1px;
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
        <TABLE cellspacing="0" cellpadding="0"  width="100%"  border="0" valign="top" align="left">
            <TBODY>
                <TR>
                    <TD width="100%" bgColor=#323232 vAlign="center" align="left" height="10">
                        <digi:insert attribute="headerTop" />
                    </TD>
                </TR>
                <TR>
                    <TD width="100%" align="center" vAlign="top" bgcolor="#376091">
                        <TABLE cellspacing="0" cellpadding="0" width="98%" border="0" vAlign="center" bgcolor="#376091">
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
                                    <TD>
                                        <TABLE cellpadding="2" cellspacing="2" width="1220" border="0">
                                            <TBODY>
                                                <TR>
                                                    <TD VALIGN="TOP" colspan="2">
                                                        <feature:display name="orgprof_chart_place1" ampModule="Org Profile">
                                                            <digi:insert attribute="chart1" flush="false">
                                                                <digi:put name="widget-teaser-param" >orgprof_chart_place1</digi:put>
                                                            </digi:insert>
                                                        </feature:display>
                                                    </TD>
                                                </TR>
                                                <TR>
                                                    <TD  VALIGN="TOP">
                                                        <feature:display name="orgprof_chart_place2" ampModule="Org Profile">
                                                            <digi:insert attribute="chart2" flush="false">
                                                                <digi:put name="widget-teaser-param">orgprof_chart_place2</digi:put>
                                                            </digi:insert>
                                                        </feature:display>
                                                    </TD>
                                                    <TD  VALIGN="TOP">
                                                        <feature:display name="orgprof_chart_place3" ampModule="Org Profile">
                                                            <digi:insert attribute="chart3" flush="false">
                                                                <digi:put name="widget-teaser-param">orgprof_chart_place3</digi:put>
                                                            </digi:insert>
                                                        </feature:display>
                                                    </TD>
                                                </TR>
                                                <TR>
                                                    <TD   VALIGN="TOP" style="width: 50%">
                                                        <feature:display name="orgprof_chart_place4" ampModule="Org Profile">
                                                            <digi:insert attribute="chart4" flush="false">
                                                                <digi:put name="widget-teaser-param">orgprof_chart_place4</digi:put>
                                                            </digi:insert>
                                                        </feature:display>
                                                    </TD>
                                                    <TD  VALIGN="TOP">
                                                        <feature:display name="orgprof_chart_place5" ampModule="Org Profile">
                                                            <digi:insert attribute="chart5" flush="false">
                                                                <digi:put name="widget-teaser-param">orgprof_chart_place5</digi:put>
                                                            </digi:insert>
                                                        </feature:display>
                                                    </TD>
                                                </TR>
                                                <TR>
                                                    <TD  VALIGN="TOP"  style="width: 50%" >
                                                        <feature:display name="orgprof_chart_place6" ampModule="Org Profile">
                                                            <digi:insert attribute="chart6" flush="false">
                                                                <digi:put name="widget-teaser-param">orgprof_chart_place6</digi:put>
                                                            </digi:insert>
                                                        </feature:display>
                                                    </TD>
                                                    <TD  VALIGN="TOP">
                                                        <feature:display name="orgprof_chart_place7" ampModule="Org Profile">
                                                            <digi:insert attribute="chart7" flush="false">
                                                                <digi:put name="widget-teaser-param">orgprof_chart_place7</digi:put>
                                                            </digi:insert>
                                                        </feature:display>
                                                    </TD>
                                                </TR>
                                                <TR>
                                                    <TD VALIGN="TOP" colspan="2"   >
                                                        <feature:display name="orgprof_chart_place8" ampModule="Org Profile">
                                                            <digi:insert attribute="chart8" flush="false">
                                                                <digi:put name="widget-teaser-param">orgprof_chart_place8</digi:put>
                                                            </digi:insert>
                                                        </feature:display>
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
        </script>
    </BODY>
</HTML>



