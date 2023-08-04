<%@page import="org.dgfoundation.amp.ar.AmpARFilter" %>
<%@page trimDirectiveWhitespaces="true" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<%@ page import="org.dgfoundation.amp.ar.ReportContextData" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!-- Dependencies -->

<%
    pageContext.setAttribute("reportCD", ReportContextData.getFromRequest());
    pageContext.setAttribute("currentFilter", ReportContextData.getFromRequest().getFilter());
%>

<bean:define id="generatedReport" name="reportCD" property="generatedReport"
             type="org.dgfoundation.amp.ar.GroupReportData" toScope="page"/>
<bean:define id="reportMeta" name="reportCD" property="reportMeta" type="org.digijava.module.aim.dbentity.AmpReports"
             toScope="page"/>


<!-- Individual YUI CSS files -->
<link rel="stylesheet" type="text/css" href="/repository/aim/view/css/filters/filters2.css">
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/container/assets/container.css">
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/skins/sam/tabview.css">
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/tabview.css"/>

<digi:ref href="css_2/report_html2_view.css" type="text/css" rel="stylesheet"/>
<style>
    .paging {
        font-size: 11px;
        color: #CCCCCC;
        margin-top: 10px;
        margin-bottom: 10px;
        font-family: Arial, Helvetica, sans-serif;
    }

    .paging_sel {
        color: #FFFFFF;
        background-color: #FF6000;
        padding: 2px 2px 2px 4px;
        text-align: center;
    }

    .l_sm {
        font-size: 11px;
        color: #376091;
    }

    .tab_opt {
        background-color: #F2F2F2;
    }

    .tab_opt_box {
        border: 1px solid #EBEBEB;
        max-height: 100px;
        overflow: auto;
    }

    .tab_opt_box_cont {
        padding: 5px;
        font-size: 11px;
        background-color: #FAFAFA;
    }

    .tab_opt_cont {
        padding: 5px;
        font-size: 11px;
        color: #CCCCCC;
    }

    .show_hide_setting {
        float: right;
        font-size: 11px;
        padding: 5px;
        width: 200px;
        font-family: Arial, Helvetica, sans-serif;
    }

    <%if ("print".equals(request.getParameter("viewFormat"))) {%>
    html {
        background: none !important;
    }

    body {
        background: none !important;
    }

    <%}%>

</style>
<!-- Individual YUI JS files -->


<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/dragdrop/dragdrop-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/animation/animation-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/connection/connection-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/container/container-min.js"></script>
<script type="text/javascript" src="/repository/aim/view/scripts/arFunctions.js"></script>
<script type="text/javascript" src="/repository/aim/view/multilingual/multilingual_scripts.js"></script>

<!-- Individual YUI JS files -->
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/dragdrop/dragdrop-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/animation/animation-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/connection/connection-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/container/container-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
<script type="text/javascript"
        src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-modalMessage.js"/>"></script>

<script type="text/javascript">
    /*---snippet rafy---*/
    function css_browser_selector(u) {
        var ua = u.toLowerCase(), is = function (t) {
                return ua.indexOf(t) > -1
            }, g = 'gecko', w = 'webkit', s = 'safari', o = 'opera', m = 'mobile', h = document.documentElement,
            b = [(!(/opera|webtv/i.test(ua)) && /msie\s(\d)/.test(ua)) ? ('ie ie' + RegExp.$1) : is('firefox/2') ? g + ' ff2' : is('firefox/3.5') ? g + ' ff3 ff3_5' : is('firefox/3.6') ? g + ' ff3 ff3_6' : is('firefox/3') ? g + ' ff3' : is('gecko/') ? g : is('opera') ? o + (/version\/(\d+)/.test(ua) ? ' ' + o + RegExp.$1 : (/opera(\s|\/)(\d+)/.test(ua) ? ' ' + o + RegExp.$2 : '')) : is('konqueror') ? 'konqueror' : is('blackberry') ? m + ' blackberry' : is('android') ? m + ' android' : is('chrome') ? w + ' chrome' : is('iron') ? w + ' iron' : is('applewebkit/') ? w + ' ' + s + (/version\/(\d+)/.test(ua) ? ' ' + s + RegExp.$1 : '') : is('mozilla/') ? g : '', is('j2me') ? m + ' j2me' : is('iphone') ? m + ' iphone' : is('ipod') ? m + ' ipod' : is('ipad') ? m + ' ipad' : is('mac') ? 'mac' : is('darwin') ? 'mac' : is('webtv') ? 'webtv' : is('win') ? 'win' + (is('windows nt 6.0') ? ' vista' : '') : is('freebsd') ? 'freebsd' : (is('x11') || is('linux')) ? 'linux' : '', 'js'];
        c = b.join(' ');
        h.className += ' ' + c;
        return c;
    }

    css_browser_selector(navigator.userAgent);


    var msgwait0 = "<digi:trn> Please wait...</digi:trn>";
    var msgwait1 = "<digi:trn> Loading...</digi:trn>";
    var loadingreport = new YAHOO.widget.Panel("wait",
        {
            width: "240px",
            fixedcenter: true,
            close: false,
            draggable: false,
            zindex: 99,
            modal: true,
            visible: false,
            underlay: "shadow"
        }
    );

    if (location.toString().indexOf('queryEngine.do') === -1) {
        loadingreport.setHeader(msgwait0);
        loadingreport.setBody("<div align='center'>" + msgwait1 + "<br>" + '<img src="/TEMPLATE/ampTemplate/img_2/loading-icon.gif" />' + "</div>");
        loadingreport.render(document.body);
        loadingreport.show();
    }
</script>

<jsp:include page="/repository/aim/view/ar/reportsScripts.jsp"/>


<c:set var="showCurrSettings">
    <digi:trn key="rep:showCurrSettings">Show current settings</digi:trn>
</c:set>
<c:set var="hideCurrSettings">
    <digi:trn key="rep:hideCurrSettings">Hide current settings</digi:trn>
</c:set>
<script language="JavaScript">
    function toggleSettings() {
        var currentDisplaySettings = document.getElementById('currentDisplaySettings');
        var displaySettingsButton = document.getElementById('displaySettingsButton');
        if (currentDisplaySettings.style.display == "block") {
            currentDisplaySettings.style.display = "none";
            displaySettingsButton.innerHTML = "${showCurrSettings}";
        } else {
            currentDisplaySettings.style.display = "block";
            displaySettingsButton.innerHTML = "${hideCurrSettings}";
        }
    }

</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/util.js"/>"></script>

<div id="mySorter" class="dialog" style="padding:10px 5px;overflow: auto; display: none;">
    <jsp:include page="/repository/aim/view/ar/levelSorterPicker.jsp"/>
</div>
<%
    int counter = ReportContextData.getFromRequest().getProgressValue();
    counter++;
    ReportContextData.getFromRequest().setProgressValue(counter);
%>

<logic:notEqual name="viewFormat" scope="request" value="print">
    <div id="myFilterWrapper" style="display: none;">
        <div id="myFilter" style="display: none; height: 100%; overflow: hidden;">
            <jsp:include page="/aim/reportsFilterPicker.do">
                <jsp:param name="init" value=""/>
            </jsp:include>
        </div>
    </div>
</logic:notEqual>
<jsp:include page="/repository/aim/view/saveReports/dynamicSaveReportsAndFilters.jsp"/>
<%
    counter++;
    ReportContextData.getFromRequest().setProgressValue(counter);
%>

<table width="100%">
    <tr>
        <td>
            <logic:notEqual name="widget" scope="request" value="true">
            <logic:notEqual name="viewFormat" scope="request" value="print">
                <bean:define id="viewable" name="generatedReport" type="org.dgfoundation.amp.ar.Viewable"
                             toScope="request"/>
                <jsp:include page="/repository/aim/view/ar/toolBar.jsp"/>

                <c:set var="rowIdx" value="<%=new Integer(0)%>" scope="request"/>

            <logic:notEqual name="widget" scope="request" value="true">
            <div class="reportname"
                 style="font-size:18px; padding-left:5px; font-family:Arial, Helvetica, sans-serif; margin-bottom:10px; font-weight:bold; color:#000000;">
                <bean:write name="reportMeta" property="name"/>
            </div>
            </logic:notEqual>

            <table width="100%" cellpadding="3" cellspacing="1" rules="rows" frame="box"
                   style="margin-left: 5px;border-color:#cccccc;">
                <logic:notEmpty property="reportDescription" name="reportMeta">
                    <tr>
                        <td style="padding-left: 5px;padding-left: 5px;">
                            <digi:trn key="rep:pop:Description">Description:</digi:trn><br>
                            <span style="font-weight: bold;font-size: 13px;margin-left: 5px;margin-top: 3px; font-family: Arial">
							<bean:write name="reportMeta" property="reportDescription"/>
						</span>
                        </td>
                    </tr>
                </logic:notEmpty>
                <tr>
                    <td align="left" height="20px" style="padding-left: 5px;padding-left: 5px;">
					<span style="color: red;font-family: Arial;font-size: 10px;">
						<%
                            AmpARFilter af = ReportContextData.getFromRequest().getFilter();
                            if (af.computeEffectiveAmountInThousand() == AmpARFilter.AMOUNT_OPTION_IN_THOUSANDS) {%>
	               			<digi:trn key="rep:pop:AllAmount">
                                Amounts are in thousands (000)
                            </digi:trn>
	           			<%}%>
						
	           			<%
                            if (af.computeEffectiveAmountInThousand() == AmpARFilter.AMOUNT_OPTION_IN_MILLIONS) {%>
	               			<digi:trn key="rep:pop:AllAmountMillions">
                                Amounts are in millions (000 000)
                            </digi:trn>
	           			<%}%>
						
						<bean:define id="selCurrency" name="reportCD" property="selectedCurrency"/>
						<digi:trn
                                key="<%=\"aim:currency:\" + ((String)selCurrency).toLowerCase().replaceAll(\" \", \"\") %>"><%=selCurrency %>
                        </digi:trn>
					</span>
                    </td>
                </tr>
            </table>
            </logic:notEqual>
            </logic:notEqual>
            <logic:equal name="viewFormat" scope="request" value="print">
            <!-- trigger printing automatically when viewing a report in "print" mode -->
            <script type="text/javascript">
                function load() {
                    window.print();
                }
            </script>
            </logic:equal>
            <logic:notEqual name="widget" scope="request" value="true">
            <logic:notEqual name="viewFormat" scope="request" value="print">
            <!-- not a tab, not a print, e.g. a normal report -->
    <tr>
        <td>
            <%@include file="/repository/aim/view/ar/opts/report_opts.jspf" %>
        </td>
    </tr>
    </logic:notEqual>
    </logic:notEqual>

    <logic:equal name="widget" scope="request" value="true">
        <!-- <table width="100%"> -->
        <tr>
            <td style="padding-left:-2px;">
                <%@include file="/repository/aim/view/ar/opts/tab_opts.jspf" %>
            </td>
        </tr>
        <tr>
            <td>
                <%@include file="/repository/aim/view/ar/opts/tab_opts_common.jspf" %>
            </td>
        </tr>
        <tr>
            <td>
                <jsp:include page="viewNewAdvancedReportPaginator.jsp"></jsp:include>
            </td>
        </tr>
        <!-- </table> -->
    </logic:equal>
    <!--<span>Level Sorters</span>-->
    <logic:notEmpty name="reportMeta" property="hierarchies">
        <logic:notEmpty name="generatedReport" property="levelSorters">
            <bean:define id="hierarchies" name="reportMeta" property="hierarchiesArray"/>
            <tr>
                <td align="left">
                    <logic:iterate name="generatedReport" property="levelSorters" id="sorter" indexId="levelId">
                        <div id="level-sorter">
                            <logic:present name="sorter">
                                <digi:colNameTrn>${hierarchies[levelId].column.columnName}</digi:colNameTrn>&nbsp;<digi:trn
                                    key="rep:pop:sortedBy">sorted by</digi:trn>&nbsp;<bean:write name="sorter"/>
                                <br/>
                            </logic:present>
                        </div>
                    </logic:iterate>
                </td>
            </tr>
        </logic:notEmpty>
    </logic:notEmpty>
    <!--<span>Total Unique Rows</span>	-->
    <logic:notEqual name="generatedReport" property="totalUniqueRows" value="0">
        <tr>
            <td class="table-cell-padding">
                <table style="width: 100%">
                    <tr>
                        <td>
                            <!-- begin big report table -->
                            <c:set var="pageNumber" value="<%=new Integer(0)%>" scope="request"/>
                            <c:set var="paginar" value="<%=new Boolean(true)%>" scope="request"/>
                            <c:if test="${not empty param.pageNumber }">
                                <c:set var="pageNumber"
                                       value="<%=Integer.valueOf(request.getParameter(\"pageNumber\"))%>"
                                       scope="request"/>
                            </c:if>
                            <logic:equal name="viewFormat" value="print">
                                <table id='reportTable' cellSpacing="0" width="900px" style="overflow:hidden">
                                    <bean:define id="viewable" name="generatedReport"
                                                 type="org.dgfoundation.amp.ar.Viewable" toScope="request"/>
                                    <jsp:include page="/repository/aim/view/ar/viewableItem.jsp"/>
                                </table>
                            </logic:equal>
                            <logic:notEqual name="viewFormat" value="print">
                                <div id="fixAutomaticDivWithHeight100">
                                    <table id='reportTable' class="html2ReportTable inside" width="100%" cellpadding="0"
                                           cellspacing="0">
                                        <bean:define id="viewable" name="generatedReport"
                                                     type="org.dgfoundation.amp.ar.Viewable" toScope="request"/>
                                        <jsp:include page="/repository/aim/view/ar/viewableItem.jsp"/>
                                    </table>
                                </div>
                            </logic:notEqual>
                        </td>
                    </tr>
                </table>
                <!-- end of big report table -->
            </td>
        </tr>
        <logic:equal name="viewFormat" value="print">
            <tr>
                <td>
                    <u><digi:trn key="rep:print:lastupdate">Last Update :</digi:trn></u>
                    &nbsp;
                    <c:if test="${reportMeta.updatedDate != null}">
                        <bean:write name="reportMeta" property="updatedDate"/>
                    </c:if>
                    &nbsp;
                    <u><digi:trn key="rep:print:user">User :</digi:trn></u>
                    <c:if test="${reportMeta.user != null}">
                        <bean:write name="reportMeta" property="user"/>
                    </c:if>
                    <BR>
                </td>
            </tr>
        </logic:equal>
        <tr>
            <td style="padding-left: 5px;padding-right: 5px">
                <jsp:include page="viewNewAdvancedReportPaginator.jsp"></jsp:include>
            </td>
        </tr>
    </logic:notEqual>
    <logic:equal name="generatedReport" property="totalUniqueRows" value="0">
        <tr>
            <td style="font-family: Arial;font-style: italic;font-size: 10x">
                <c:choose>
                    <c:when test="${param.queryEngine =='true'}">
                        <digi:trn
                                key="rep:pop:filteredSearch">The specified filters does not hold any data. Pick a different filter criteria.
                        </digi:trn>
                    </c:when>
                    <c:otherwise>
                        <digi:trn
                                key="rep:pop:filteredreport">The specified filtered report does not hold any data. Either
                            pick a different filter criteria or use another report.
                        </digi:trn>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </logic:equal>


</table>
</td>
</tr>
</table>
<script language="JavaScript">
    loadingreport.hide();
</script>
<%
    //session.setAttribute(" ", null); to be deleted later
    ReportContextData.getFromRequest().setProgressValue(-1);
%>
