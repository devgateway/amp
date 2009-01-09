<%@ page contentType="text/html; charset=UTF-8" %> 
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<HTML>
    <digi:base />
    <digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
    <digi:context name="digiContext" property="context"/>
    <digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
    <digi:ref href="css/tabview.css" type="text/css" rel="stylesheet" />

    <HEAD>
        <TITLE>Organization Profile</TITLE>
        <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
        <META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
        <META HTTP-EQUIV="EXPIRES" CONTENT="0">
    </HEAD>
    <BODY leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">
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
                <TD width="100%" vAlign="top" align="left">
                    <TABLE bgColor=#ffffff cellPadding=0 cellSpacing=0 width="99%" vAlign="top" align="left" border=0>
                        <TR>
                            <TD align=center vAlign=top>
                                <TABLE width="100%" cellPadding=0 cellSpacing=0 vAlign="top" align="left" border="0">
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
                                            <TABLE cellpadding="5" border="0" width="80%">
                                                <TR>
                                                    <TD WIDTH="50%">
                                                        <feature:display name="orgprof_chart_place1" module="Org Profile">
                                                            <div id="content" class="yui-skin-sam" style="width:100%;">
                                                                <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                                                                    <div class="tableHeaderCls"  style="height:20px;font-size:11px;font-weight:bold;"  align="center">
                                                                        <digi:trn key="orgProfile:orgprof_chart_place1:title">orgprof_chart_place1 title</digi:trn>       
                                                                    </div>
                                                                </feature:display>
                                                                <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
                                                                    <digi:insert attribute="chart1">
                                                                        <digi:put name="widget-teaser-param" >orgprof_chart_place1</digi:put>
                                                                    </digi:insert>
                                                                    <feature:display name="orgprof_chart_place1" module="Org Profile">
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </feature:display>
                                                    </TD>
                                                    <TD WIDTH="50%">
                                                        <feature:display name="orgprof_chart_place2" module="Org Profile">
                                                            <div id="content" class="yui-skin-sam" style="width:100%;">
                                                                <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                                                                     <div class="tableHeaderCls"  style="height:20px;font-size:11px;font-weight:bold;" align="center">
                                                                                        <digi:trn key="orgProfile:orgprof_chart_place2:title">orgprof_chart_place2 title</digi:trn>
                                                                                 
                                                                      </div>
                                                                </feature:display>
                                                                <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
                                                                    <digi:insert attribute="chart2">
                                                                        <digi:put name="widget-teaser-param">orgprof_chart_place2</digi:put>
                                                                    </digi:insert>
                                                                    <feature:display name="orgprof_chart_place2" module="Org Profile">
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </feature:display>
                                                    </TD>
                                                </TR>
                                                <TR>
                                                    <TD WIDTH="50%">
                                                        <feature:display name="orgprof_chart_place3" module="Org Profile">
                                                            <div id="content" class="yui-skin-sam" style="width:100%;">
                                                                <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                                                                   <div class="tableHeaderCls"  style="height:20px;font-size:11px;font-weight:bold;" align="center">
                                                                                        <digi:trn key="orgProfile:orgprof_chart_place3:title">orgprof_chart_place3 title</digi:trn>
                                                                                   
                                                                     </div>
                                                                    
                                                                </feature:display>
                                                                <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
                                                                    <digi:insert attribute="chart3">
                                                                        <digi:put name="widget-teaser-param">orgprof_chart_place3</digi:put>
                                                                    </digi:insert>
                                                                    <feature:display name="orgprof_chart_place3" module="Org Profile">
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </feature:display>
                                                    </TD>

                                                    <TD WIDTH="50%">
                                                        <feature:display name="orgprof_chart_place4" module="Org Profile">
                                                            <div id="content" class="yui-skin-sam" style="width:100%;">
                                                                <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">

                                                                <div class="tableHeaderCls"  style="height:20px;font-size:11px;font-weight:bold;" align="center">
                                                                    <digi:trn key="orgProfile:orgprof_chart_place4:title">orgprof_chart_place4 title</digi:trn>
                                                                </div>
                                                                              
                                                                </feature:display>
                                                                <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
                                                                    <digi:insert attribute="chart4">
                                                                        <digi:put name="widget-teaser-param">orgprof_chart_place4</digi:put>
                                                                    </digi:insert>
                                                                    <feature:display name="orgprof_chart_place4" module="Org Profile">
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </feature:display>
                                                    </TD>


                                                </TR>
                                                <TR>
                                                    <TD WIDTH="50%">
                                                        <feature:display name="orgprof_chart_place5" module="Org Profile">
                                                            <div id="content" class="yui-skin-sam" style="width:100%;">
                                                                <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">

                                                                   <div class="tableHeaderCls"  style="height:20px;font-size:11px;font-weight:bold;" align="center">
                                                                                        <digi:trn key="orgProfile:orgprof_chart_place5:title">orgprof_chart_place5 title</digi:trn>
                                                                   </div>
                                                                          
                                                                </feature:display>
                                                                <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
                                                                    <digi:insert attribute="chart5">
                                                                        <digi:put name="widget-teaser-param">orgprof_chart_place5</digi:put>
                                                                    </digi:insert>
                                                                    <feature:display name="orgprof_chart_place5" module="Org Profile">
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </feature:display>
                                                    </TD>
                                                    <TD WIDTH="50%">
                                                        <feature:display name="orgprof_chart_place6" module="Org Profile">
                                                            <div id="content" class="yui-skin-sam" style="width:100%;">
                                                                <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                                                                  <div class="tableHeaderCls"  style="height:20px;font-size:11px;font-weight:bold;" align="center">
                                                                                        <digi:trn key="orgProfile:orgprof_chart_place6:title">orgprof_chart_place6 title</digi:trn>
                                                                  </div>
                                                                </feature:display>
                                                                <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
                                                                    <digi:insert attribute="chart6">
                                                                        <digi:put name="widget-teaser-param">orgprof_chart_place6</digi:put>
                                                                    </digi:insert>
                                                                    <feature:display name="orgprof_chart_place6" module="Org Profile">
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </feature:display>
                                                    </TD>
                                                </TR>
                                                <TR>
                                                    <TD WIDTH="50%">
                                                        <feature:display name="orgprof_chart_place7" module="Org Profile">
                                                        <div id="content" class="yui-skin-sam" style="width:100%;">
                                                            <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                                                                    <div class="tableHeaderCls"  style="height:20px;font-size:11px;font-weight:bold;" align="center">
                                                                                        <digi:trn key="orgProfile:orgprof_chart_place7:title">orgprof_chart_place7 title</digi:trn>
                                                                     </div>
                                                                             
                                                                </feature:display>
                                                                <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
                                                                    <digi:insert attribute="chart7">
                                                                        <digi:put name="widget-teaser-param">orgprof_chart_place7</digi:put>
                                                                    </digi:insert>
                                                                    <feature:display name="orgprof_chart_place6" module="Org Profile">
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </feature:display>
                                                    </TD>
                                                    <TD WIDTH="50%">
                                                        <feature:display name="orgprof_chart_place8" module="Org Profile">
                                                            <div id="content" class="yui-skin-sam" style="width:100%;">
                                                                <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                                                                   <div class="tableHeaderCls"  style="height:20px;font-size:11px;font-weight:bold;" align="center">
                                                                                        <digi:trn key="orgProfile:orgprof_chart_place8:title">orgprof_chart_place8 title</digi:trn>
                                                                    </div>
                                                                        
                                                                </feature:display>
                                                                <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
                                                                    <digi:insert attribute="chart8">
                                                                        <digi:put name="widget-teaser-param">orgprof_chart_place8</digi:put>
                                                                    </digi:insert>
                                                                    <feature:display name="orgprof_chart_place8" module="Org Profile">
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </feature:display>
                                                    </TD>
                                                </TR>

                                            </TABLE>
                                        </TD>
                                    </TR>
                                    <TR>
                                        <TD width="100%" >
                                            <digi:insert attribute="footer" />
                                        </TD>
                                    </TR>
                                </TABLE>
                            </TD>
                        </TR>

                    </TABLE>
                </TD>
            </TR>
        </TABLE>
    </BODY>
</HTML>



