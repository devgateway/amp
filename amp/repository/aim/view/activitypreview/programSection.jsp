<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<digi:instance property="aimEditActivityForm" />
<%--@elvariable id="aimEditActivityForm" type="org.digijava.ampModule.aim.form.EditActivityForm"--%>

<ampModule:display name="/Activity Form/Program" parentModule="/Activity Form">
    <fieldset>
        <legend>
            <span class="legend_label" style="cursor: pointer;"><digi:trn>Program</digi:trn></span>
        </legend>
        <div class="toggleDiv">
            <ampModule:display name="/Activity Form/Program/National Plan Objective" parentModule="/Activity Form/Program">
                <c:set var="programs_list" value="${aimEditActivityForm.programs.nationalPlanObjectivePrograms}" scope="request" />
                <c:set var="programs_name" scope="request"><digi:trn>National Plan Objective</digi:trn></c:set>
                <jsp:include page="programs.jsp"/>
            </ampModule:display>
            <ampModule:display name="/Activity Form/Program/Primary Programs" parentModule="/Activity Form">
                <c:set var="programs_list" value="${aimEditActivityForm.programs.primaryPrograms}" scope="request" />
                <c:set var="programs_name" scope="request"><digi:trn>Primary Programs</digi:trn></c:set>
                <jsp:include page="programs.jsp"/>
            </ampModule:display>
            <ampModule:display name="/Activity Form/Program/Secondary Programs" parentModule="/Activity Form/Program">
                <c:set var="programs_list" value="${aimEditActivityForm.programs.secondaryPrograms}" scope="request" />
                <c:set var="programs_name" scope="request"><digi:trn>Secondary Programs</digi:trn></c:set>
                <jsp:include page="programs.jsp"/>
            </ampModule:display>
            <ampModule:display name="/Activity Form/Program/Tertiary Programs" parentModule="/Activity Form/Program">
                <c:set var="programs_list" value="${aimEditActivityForm.programs.tertiaryPrograms}" scope="request" />
                <c:set var="programs_name" scope="request"><digi:trn>Tertiary Programs</digi:trn></c:set>
                <jsp:include page="programs.jsp"/>
            </ampModule:display>
            <!-- program description -->
            <ampModule:display name="/Activity Form/Program/Program Description" parentModule="/Activity Form/Program">
                <c:set var="programDescription" value="${aimEditActivityForm.programs.programDescription}" />
                <c:if test="${not empty programDescription}">
                    <span class="word_break"><digi:trn>Program Description</digi:trn></span>
                    <table width="100%" cellSpacing="2" cellPadding="1" style="font-size: 11px;">
                        <tr>
                            <td width="85%">
                                <span class="word_break bold"><digi:edit key="${programDescription}" /></span>
                            </td>
                        </tr>
                    </table>
                    <hr>
                </c:if>
            </ampModule:display>
            <!-- end program description -->
        </div>
    </fieldset>
</ampModule:display>
