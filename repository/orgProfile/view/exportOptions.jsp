<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>


<digi:instance property="orgProfileExportOptionsForm"/>
<digi:form  action="/showExportOptions.do">
    <c:forEach var="helpers" items="${orgProfileExportOptionsForm.helpers}" varStatus="status">
        <c:set var="optionClass">
            <c:choose>
                <c:when test="${status.count%2==1}">
                    optionLeft
                </c:when>
                <c:otherwise>
                    optionRight
                </c:otherwise>
            </c:choose>
        </c:set>

        <div class="${optionClass}">

            <div class="settingTitle">
                <digi:trn>${helpers.widget.name}</digi:trn>
            </div>
            <div class="settingOptions" >
                <c:choose>
                    <c:when test="${helpers.widget.type==7}">
                        <html:radio indexed="true" name="helpers" property="selectedTypeOfExport" value="2"><digi:trn>Include Paris Indicator</digi:trn></html:radio><br/>
                        <html:radio indexed="true" name="helpers" property="selectedTypeOfExport" value="3"><digi:trn>Exclude Paris Indicator</digi:trn></html:radio>
                    </c:when>
                    <c:when test="${helpers.widget.type==1}">
                        <html:radio indexed="true" name="helpers" property="selectedTypeOfExport" value="2"><digi:trn>Include Summary</digi:trn></html:radio><br/>
                        <html:radio indexed="true" name="helpers" property="selectedTypeOfExport" value="3" title="${trn}"><digi:trn>Exclude Summary</digi:trn></html:radio>
                    </c:when>
                    <c:otherwise>
                        <c:set var="trn">
                            <digi:trn>this option will only export the selected charts</digi:trn>
                        </c:set>
                        <html:radio indexed="true" name="helpers" property="selectedTypeOfExport" value="0" title="${trn}"><digi:trn>Chart only</digi:trn></html:radio><br/>
                        <c:set var="trn">
                            <digi:trn>this option will only export the selected data sources</digi:trn>
                        </c:set>
                        <html:radio indexed="true" name="helpers" property="selectedTypeOfExport" value="1" title="${trn}"><digi:trn>Data Source</digi:trn></html:radio><br/>
                        <c:set var="trn">
                            <digi:trn>this option will export both the selected charts and data sources</digi:trn>
                        </c:set>
                        <html:radio indexed="true" name="helpers" property="selectedTypeOfExport" value="2" title="${trn}"><digi:trn>Chart and Data Source</digi:trn></html:radio><br/>
                        <c:set var="trn">
                            <digi:trn>this option will exclude both charts and data sources from export</digi:trn>
                        </c:set>
                        <html:radio indexed="true" name="helpers" property="selectedTypeOfExport" value="3" title="${trn}"><digi:trn>None</digi:trn></html:radio>
                    </c:otherwise>
                </c:choose>

            </div>
        </div>
    </c:forEach>
    <c:set var="settingWidgets"  value="${fn:length(orgProfileExportOptionsForm.helpers)}"/>
    <c:set var="optionClass">
        <c:choose>
            <c:when test="${settingWidgets%2==0}">
                optionLeft
            </c:when>
            <c:otherwise>
                optionRight
            </c:otherwise>
        </c:choose>
    </c:set>
    <div class="${optionClass}">
        <div class="settingTitle">
            <digi:trn>Export Format</digi:trn>
        </div>
        <div class="exportFormat">
            <c:set var="trn">
                <digi:trn>this option will export your selection in Word format</digi:trn>
            </c:set>
            <html:radio  property="selectedFormatOfExport" value="0" title="${trn}"><digi:trn>Export to Word</digi:trn><digi:img  hspace="0" vspace="0" height="15px" src="/TEMPLATE/ampTemplate/images/icons/doc.gif" border="0" alt='Export to Word'/></html:radio><br/>
            <c:set var="trn">
                <digi:trn>this option will export your selection in PDF format</digi:trn>
            </c:set>
            <html:radio  property="selectedFormatOfExport" value="1" title="${trn}"><digi:trn>Export to PDF</digi:trn><digi:img  hspace="0" vspace="0" height="15px" src="/TEMPLATE/ampTemplate/images/icons/pdf.gif" border="0" alt='Export to PDF'/></html:radio><br/>
            <html:checkbox property="monochromeOption" value="true" title="${trn}"><digi:trn>Monochrome Graphs</digi:trn></html:checkbox><br/>
        </div>
    </div>
    <div style="clear: both;text-align: center">
        <input type="button" class="button" value="<digi:trn key="orgProfile:filer:Apply">Apply</digi:trn>"   onclick="exportPage()">
        <input type="button" class="button" value="<digi:trn>Close</digi:trn>" onclick="closeSettingsPopin()">
    </div>

</digi:form>

