<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/c.tld" prefix="c" %>
<jsp:include page="../../aim/view/scripts/newCalendar.jsp"  />

<digi:instance property="calendarViewForm"/>
<script type="text/javascript" src="<digi:file src="module/calendar/js/main.js"/>"></script>
<script type="text/javascript">
function selectCalendarType(view, type) {
    var form = document.getElementById('filterForm');
    if (form != null) {
        if (view == 'custom') {
            form.customViewStartDate.value = null;
            form.customViewEndDate.value = null;
        }
        form.view.value = view;
        form.submit();
    }
}
</script>

<!--<table style="width: 220px">-->
<!--    <tr>-->
<!--        <td nowrap="nowrap">-->
<!--            <table border="0" cellpadding="0" cellspacing="0" width="100%">-->
<!--                <tr>-->
<!--                	<td nowrap="nowrap">-->
<!--	                	<div class="right_menu" style="margin-bottom: 0px;">-->
<!--							<div class="right_menu_header_big">-->
<!--                                                            <div class="right_menu_header_cont"><digi:trn>Calendar Type</digi:trn></div>-->
<!--							</div>-->
<!--							<div class="right_menu_box_big">-->
<!--								<div class="right_menu_cont">-->
<!--									<digi:trn>Type</digi:trn>&nbsp;-->
<!--									<html:select styleClass="selector_type" name="calendarViewForm" property="selectedCalendarType" onchange="selectCalendarType('${calendarViewForm.view}', '${calendarViewForm.selectedCalendarType}')">-->
<!--                            			<bean:define id="types" name="calendarViewForm" property="calendarTypes" type="java.util.List"/>-->
<!--                            			<html:options collection="types" property="value" labelProperty="label"/>-->
<!--                        			</html:select>-->
<!--								</div>-->
<!--							</div>-->
<!--						</div>-->
<!--                    </td>-->
<!--                </tr>-->
<!--            </table>-->
<!--        </td>-->
<!--    </tr>-->
<!--</table>-->