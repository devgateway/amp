<%@ page contentType="text/html; charset=UTF-8" %> 
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<HTML>
<digi:base />
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<digi:context name="digiContext" property="context"/>

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
        <TD class="r-dotted-lg" width="10">&nbsp;</TD>
        <TD align=center vAlign=top>
        <TABLE width="100%" cellPadding=0 cellSpacing=0 vAlign="top" align="left" border="0">
            <TR>
                <TD vAlign="top" align="left" class="r-dotted-lg">
                    <digi:insert attribute="filters"/>
                </TD>										
            </TR>
            <TR>
            <TD>                                        		
                <TABLE cellpadding="5" border="0" width="100%">
                <TR>
                    <TD WIDTH="50%">
                                    <digi:insert attribute="chart1">
                                        <digi:put name="widget-teaser-param" >orgprof_chart_place1</digi:put>
                                    </digi:insert>
                        
                    </TD>
                    <TD WIDTH="50%">
                                    <digi:insert attribute="chart2">
                                        <digi:put name="widget-teaser-param">orgprof_chart_place2</digi:put>
                                    </digi:insert>
                                            
                    </TD>
                </TR>
                <TR>
                    <TD WIDTH="50%">
                                    <digi:insert attribute="chart3">
                                        <digi:put name="widget-teaser-param">orgprof_chart_place3</digi:put>
                                    </digi:insert>
                                            
                    </TD>
                    
                    <TD WIDTH="50%">
                    <digi:insert attribute="chart4">
                        <digi:put name="widget-teaser-param">orgprof_chart_place4</digi:put>
                    </digi:insert>
                                            
                </TD>
                
                
            </TR>
            <TR>
                <TD WIDTH="50%">
                                <digi:insert attribute="chart5">
                                    <digi:put name="widget-teaser-param">orgprof_chart_place5</digi:put>
                                </digi:insert>
                                            
                </TD>
                <TD WIDTH="50%">
                                <digi:insert attribute="chart6">
                                    <digi:put name="widget-teaser-param">orgprof_chart_place6</digi:put>
                                </digi:insert>
                </TD>
            </TR>
            <TR>
                <TD WIDTH="50%">
                                <digi:insert attribute="chart7">
                                    <digi:put name="widget-teaser-param">orgprof_chart_place7</digi:put>
                                </digi:insert>
                </TD>
                <TD WIDTH="50%">
                                <digi:insert attribute="chart8">
                                    <digi:put name="widget-teaser-param">orgprof_chart_place8</digi:put>
                                </digi:insert>
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

    
                      
                  