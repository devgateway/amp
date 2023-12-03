<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>

<digi:instance property="aimEditActivityForm" />
<%--@elvariable id="aimEditActivityForm" type="org.digijava.ampModule.aim.form.EditActivityForm"--%>

<ampModule:display name="/Activity Form/GPI" parentModule="/Activity Form">
    <fieldset>
        <legend>
			<span class=legend_label id="gpilink" style="cursor: pointer;">
				<digi:trn>GPI</digi:trn>
			</span>
        </legend>
        <div class="field_text_big">
            <div id="gpi" class="toggleDiv" style="display: block;">
                <table width="100%" cellSpacing="1" cellPadding="5" class="box-border-nopadding">
                    <bean:define id="gpiSurvey" name="gpiSurveys" scope="request" toScope="page"
                                 type="java.util.Collection"/>

                    <c:set var="currentIndicatorName" value=""/>
                    <logic:iterate name="gpiSurveys" id="gpiSurvey"
                                   type="java.util.Collection" indexId="gpiId">
                        <logic:iterate name="gpiSurvey" id="gpiresponse"
                                       type="org.digijava.ampModule.aim.dbentity.AmpGPISurveyResponse">

                            <c:if test="${!currentIndicatorName.equals(gpiresponse.ampQuestionId.ampIndicatorId.name)}">
                                <c:set var="currentIndicatorName"
                                       value="${gpiresponse.ampQuestionId.ampIndicatorId.name}"/>
                                <tr>
                                    <td bgcolor="#eeeeee" style="text-transform: uppercase;">
                                        <c:set var="indicatorName"
                                               value="${gpiresponse.ampQuestionId.ampIndicatorId.name}"/>
                                        <span class="word_break bold"><digi:trn>${indicatorName}</digi:trn></span>
                                    </td>
                                </tr>
                            </c:if>
                            <tr>
                                <td>
                                    <c:set var="questionText"
                                           value="${gpiresponse.ampQuestionId.questionText}"/>
                                    <c:set var="ampTypeName"
                                           value="${gpiresponse.ampQuestionId.ampTypeId.name}"/>
                                    <span class="word_break bold"><digi:trn>${questionText}</digi:trn></span>
                                    <c:set var="responseText" value="${gpiresponse.response}"/>
                                    <lu>
                                        <li>
                                            <c:if test='${"yes-no".equals(ampTypeName) &&
													!"".equals(responseText) && responseText != null}'>
                                                <span class="word_break bold"><digi:trn>${responseText}</digi:trn></span>
                                            </c:if>
                                            <c:if test='${!"yes-no".equals(ampTypeName)}'>
                                                <span class="word_break bold">${responseText}</span>
                                            </c:if>
                                        </li>
                                    </lu>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <hr/>
                                </td>
                            </tr>

                        </logic:iterate>

                    </logic:iterate>

                </table>
            </div>

        </div>
    </fieldset>
</ampModule:display>
