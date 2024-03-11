<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/category" prefix="category"%>

<digi:instance property="aimEditActivityForm" />
<%--@elvariable id="aimEditActivityForm" type="org.digijava.module.aim.form.EditActivityForm"--%>

<module:display name="/Activity Form/Identification" parentModule="/Activity Form">
    <fieldset>
        <legend>
			<span class=legend_label id="identificationlink" style="cursor: pointer;">
				<digi:trn>Identification</digi:trn>
			</span>
        </legend>
        <div id="identificationdiv" class="toggleDiv">
            <module:display name="/Activity Form/Identification/Project Title" parentModule="/Activity Form/Identification">
            <digi:trn key="aim:projectTitle">Project title</digi:trn>:&nbsp;<br />
            <span class="word_break bold">
            <c:out value="${aimEditActivityForm.identification.title}"/></span>
            <hr />
            </module:display>

            <digi:trn key="aim:ampId">AMP ID</digi:trn>:&nbsp;<br /><b><c:out value="${aimEditActivityForm.identification.ampId}"/></b> <br>
            <hr />

            <module:display name="/Activity Form/Identification/Activity Status" parentModule="/Activity Form/Identification">
            <digi:trn key="aim:status">Status</digi:trn>:&nbsp;
            <b><category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.statusId}"/></b>
            <hr />
            </module:display>
            <module:display name="/Activity Form/Identification/Status Other Info"
                            parentModule="/Activity Form/Identification">
            <c:if test="${not empty aimEditActivityForm.identification.statusOtherInfo}">
            <digi:trn>Status Other Info</digi:trn>:&nbsp;<br />
            <span class="word_break bold"><c:out value="${aimEditActivityForm.identification.statusOtherInfo}"/></span>
            <hr />
            </c:if>
            </module:display>

            <module:display name="/Activity Form/Identification/Status Reason" parentModule="/Activity Form/Identification">
            <digi:trn key="aim:statusReason">Status Reason</digi:trn>:&nbsp;
            <c:if test="${not empty aimEditActivityForm.identification.statusReason}">
                <c:set var="projstatusReason" value="${aimEditActivityForm.identification.statusReason}"/>
            <span class="word_break bold"><digi:edit key="${projstatusReason}"/></span>
            </c:if>
            <hr />
            </module:display>

            <module:display name="/Activity Form/Funding/Overview Section/Type of Cooperation"
                            parentModule="/Activity Form/Funding/Overview Section">
            <c:if test="${not empty aimEditActivityForm.identification.ssc_typeOfCooperation}">
            <digi:trn>Type of Cooperation</digi:trn>:&nbsp;<br />
            <b><c:out value="${aimEditActivityForm.identification.ssc_typeOfCooperation}"/></b>
            <hr />
            </c:if>
            </module:display>

            <module:display name="/Activity Form/Funding/Overview Section/Type of Implementation"
                            parentModule="/Activity Form/Funding/Overview Section">
            <digi:trn>Type of Implementation</digi:trn>:&nbsp;<br />
            <b><c:out value="${aimEditActivityForm.identification.ssc_typeOfImplementation}"/></b>
            <hr />
            </module:display>

                <c:set var="modalitiesPath" value="/Activity Form/Funding/Overview Section/Modalities"/>
            <c:if test="${aimEditActivityForm.identification.team !=null && aimEditActivityForm.identification.team.isSSCWorkspace()}">
                <c:set var="modalitiesPath" value="/Activity Form/Funding/Overview Section/SSC Modalities"/>
            </c:if>

            <module:display name="${modalitiesPath}"
                            parentModule="/Activity Form/Funding/Overview Section">
            <digi:trn>Modalities</digi:trn>:&nbsp;<br />
            <c:if test="${not empty aimEditActivityForm.identification.ssc_modalities}">
            <b>
                <c:forEach var="modality" items="${aimEditActivityForm.identification.ssc_modalities}">
                    <span class="word_break">${modality}</span><br/>
                </c:forEach>
            </b>
            </c:if>
            <hr />
            </module:display>


                <c:set var="modalitiesPath" value="/Activity Form/Funding/Overview Section/Modalities Other Info"/>
            <c:if test="${aimEditActivityForm.identification.team !=null && aimEditActivityForm.identification.team.isSSCWorkspace()}">
                <c:set var="modalitiesPath" value="/Activity Form/Funding/Overview Section/SSC Modalities Other Info"/>
            </c:if>

            <module:display name="${modalitiesPath}"
                            parentModule="/Activity Form/Funding/Overview Section">
            <c:if test="${not empty aimEditActivityForm.identification.modalitiesOtherInfo}">
            <digi:trn>Modalities Other Info</digi:trn>:&nbsp;<br />
            <span class="word_break bold"><c:out value="${aimEditActivityForm.identification.modalitiesOtherInfo}"/></span>
            <hr />
            </c:if>
            </module:display>


            <module:display name="/Activity Form/Identification/Objective" parentModule="/Activity Form/Identification">
            <digi:trn key="aim:objectives">Objectives</digi:trn>:&nbsp;<br />
            <c:if test="${aimEditActivityForm.identification.objectives!=null}">
                <c:set var="objKey" value="${aimEditActivityForm.identification.objectives}"/>
            <span class="word_break bold"><digi:edit key="${objKey}"/></span>
            </c:if>
            <hr />
            </module:display>

            <module:display name="/Activity Form/Identification/Objective Comments" parentModule="/Activity Form/Identification">
            <logic:present name="currentMember" scope="session">
            <digi:trn key="aim:objectiveComments">Objective Comments</digi:trn>:&nbsp;
            <ul>
                <logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
                    <module:display name="/Activity Form/Identification/Objective Comments/Objective Assumption" parentModule="/Activity Form/Identification/Objective Comments">
                        <logic:equal name="comments" property="key" value="Objective Assumption">
                            <logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
                                <li>
                                    <digi:trn>Objective Assumption</digi:trn>:&nbsp;<br />
                                    <span class="word_break bold"><bean:write name="comment" property="comment" /></span>
                                    <br />
                                </li>
                            </logic:iterate>
                        </logic:equal>
                    </module:display>
                    <module:display name="/Activity Form/Identification/Objective Comments/Objective Verification" parentModule="/Activity Form/Identification/Objective Comments">
                        <logic:equal name="comments" property="key" value="Objective Verification">
                            <logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
                                <li>
                                    <digi:trn>Objective Verification</digi:trn>:&nbsp;	<br />
                                    <span class="word_break bold"><bean:write name="comment" property="comment" /></span>
                                    <br/>
                                </li>
                            </logic:iterate>
                        </logic:equal>
                    </module:display>
                    <module:display name="/Activity Form/Identification/Objective Comments/Objective Objectively Verifiable Indicators"
                                    parentModule="/Activity Form/Identification/Objective Comments">
                        <logic:equal name="comments" property="key" value="Objective Objectively Verifiable Indicators">
                            <logic:iterate name="comments" id="comment" property="value"
                                           type="org.digijava.module.aim.dbentity.AmpComments">
                                <li>
                                    <digi:trn>Objective Objectively Verifiable Indicators</digi:trn>:&nbsp;	<br />
                                    <span class="word_break bold"><bean:write name="comment" property="comment" /></span>
                                    <br/>
                                </li>
                            </logic:iterate>
                        </logic:equal>
                    </module:display>
                </logic:iterate>
            </ul>
            <hr/>
            </logic:present>
            </module:display>

            <module:display name="/Activity Form/Identification/Description" parentModule="/Activity Form/Identification">
            <digi:trn key="aim:description">Description</digi:trn>:&nbsp;<br />
            <c:if test="${aimEditActivityForm.identification.description!=null}">
                <c:set var="descKey" value="${aimEditActivityForm.identification.description}"/>
            <span class="word_break bold"><digi:edit key="${descKey}"/></span>
            </c:if>
            <hr />
            </module:display>

            <module:display name="/Activity Form/Identification/Project Comments" parentModule="/Activity Form/Identification">
            <digi:trn>Project Comments</digi:trn>:&nbsp;<br />
            <c:if test="${aimEditActivityForm.identification.projectComments!=null}">
                <c:set var="projcomKey" value="${aimEditActivityForm.identification.projectComments}"/>
            <span class="word_break bold"><digi:edit key="${projcomKey}"/></span>
            </c:if>
            <hr />
            </module:display>


            <module:display name="/Activity Form/Identification/Lessons Learned" parentModule="/Activity Form/Identification">
            <digi:trn>Lessons Learned</digi:trn>:&nbsp;<br />
            <c:if test="${not empty aimEditActivityForm.identification.lessonsLearned}">
            <bean:define id="lessonsLearnedKey">
                <c:out value="${aimEditActivityForm.identification.lessonsLearned}"/>
            </bean:define>
            <span class="word_break bold"><digi:edit key="${lessonsLearnedKey}"/></span>
            </c:if>
            <hr />
            </module:display>

                <bean:define id="largeTextFeature" value="Identification" toScope="request"/>
            <logic:present name="aimEditActivityForm" property="identification.projectImpact">
                <bean:define id="moduleName" value="/Activity Form/Identification/Project Impact" toScope="request"/>
                <bean:define id="parentModule" value="/Activity Form/Identification" toScope="request"/>
                <bean:define id="largeTextLabel" value="Project Impact" toScope="request"/>
            <bean:define id="largeTextKey" toScope="request">
                <c:out value="${aimEditActivityForm.identification.projectImpact}"/>
            </bean:define>
            <span class="word_break">
					<jsp:include page="../largeTextPropertyView.jsp" />
				</span>
            </logic:present>

            <logic:present name="aimEditActivityForm" property="identification.activitySummary">
                <bean:define id="moduleName" value="/Activity Form/Identification/Activity Summary" toScope="request"/>
                <bean:define id="parentModule" value="/Activity Form/Identification" toScope="request"/>
                <bean:define id="largeTextLabel" value="Activity Summary" toScope="request"/>
            <bean:define id="largeTextKey" toScope="request">
                <c:out value="${aimEditActivityForm.identification.activitySummary}"/>
            </bean:define>
            <span class="word_break">
					<jsp:include page="../largeTextPropertyView.jsp" />
				</span>
            </logic:present>
            <logic:present name="aimEditActivityForm" property="identification.conditionality">
                <bean:define id="moduleName" value="/Activity Form/Identification/Conditionalities" toScope="request"/>
                <bean:define id="parentModule" value="/Activity Form/Identification" toScope="request"/>
                <bean:define id="largeTextLabel" value="Conditionalities" toScope="request"/>
            <bean:define id="largeTextKey" toScope="request">
                <c:out value="${aimEditActivityForm.identification.conditionality}"/>
            </bean:define>
            <span class="word_break">
					<jsp:include page="../largeTextPropertyView.jsp" />
				</span>
            </logic:present>
            <logic:present name="aimEditActivityForm" property="identification.projectManagement">
                <bean:define id="moduleName" value="/Activity Form/Identification/Project Management" toScope="request"/>
                <bean:define id="parentModule" value="/Activity Form/Identification" toScope="request"/>
                <bean:define id="largeTextLabel" value="Project Management" toScope="request"/>
            <bean:define id="largeTextKey" toScope="request">
                <c:out value="${aimEditActivityForm.identification.projectManagement}"/>
            </bean:define>
            <span class="word_break">
					<jsp:include page="../largeTextPropertyView.jsp" />
				</span>
            </logic:present>
            <module:display name="/Activity Form/Identification/Purpose" parentModule="/Activity Form/Identification">
            <digi:trn >Purpose</digi:trn>:<br />
            <c:if test="${aimEditActivityForm.identification.purpose!=null}">
                <c:set var="objKey" value="${aimEditActivityForm.identification.purpose}"/>
            <span class="word_break bold"><digi:edit key="${objKey}"/></span>
            </c:if>
            <hr/>
            </module:display>
            <module:display name="/Activity Form/Identification/Purpose Comments" parentModule="/Activity Form/Identification">
            <logic:present name="aimEditActivityForm" property="comments.allComments">
            <digi:trn>Purpose Comments</digi:trn>&nbsp;
            <ul>
                <logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
                    <module:display name="/Activity Form/Identification/Purpose Comments/Purpose Assumption" parentModule="/Activity Form/Identification/Purpose Comments">
                        <logic:equal name="comments" property="key" value="Purpose Assumption">
                            <logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
                                <li>
                                    <digi:trn key="aim:purposeAssumption">Purpose Assumption</digi:trn>:<br />
                                    <span class="word_break bold"><bean:write name="comment" property="comment" /></span>
                                    <br />
                                </li>
                            </logic:iterate>
                        </logic:equal>
                    </module:display>
                    <module:display name="/Activity Form/Identification/Purpose Comments/Purpose Verification" parentModule="/Activity Form/Identification/Purpose Comments">
                        <logic:equal name="comments" property="key" value="Purpose Verification">
                            <logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
                                <li>
                                    <digi:trn key="aim:purposeVerification">Purpose Verification</digi:trn>:<br />
                                    <span class="word_break bold"><bean:write name="comment" property="comment" /></span>
                                    <br/>
                                </li>
                            </logic:iterate>
                        </logic:equal>
                    </module:display>
                    <module:display name="/Activity Form/Identification/Purpose Comments/Purpose Objectively Verifiable Indicators" parentModule="/Activity Form/Identification/Purpose Comments">
                        <logic:equal name="comments" property="key" value="Purpose Objectively Verifiable Indicators">
                            <logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
                                <li>
                                    <digi:trn key="aim:purposeObjectivelyVerifiableIndicators">Purpose Objectively Verifiable Indicators</digi:trn>:<br />
                                    <span class="word_break bold"><bean:write name="comment" property="comment" /></span>
                                    <br/>
                                </li>
                            </logic:iterate>
                        </logic:equal>
                    </module:display>
                </logic:iterate>
            </ul>
            <hr/>
            </logic:present>
            </module:display>

            <module:display name="/Activity Form/Identification/Results" parentModule="/Activity Form/Identification">
            <digi:trn key="aim:results">Results</digi:trn>:&nbsp;<br />
            <c:if test="${aimEditActivityForm.identification.results!=null}">
                <c:set var="objKey" value="${aimEditActivityForm.identification.results}"/>
            <span class="word_break bold"><digi:edit key="${objKey}"/></span>
            <hr>
            </c:if>
            </module:display>
            <logic:present name="aimEditActivityForm" property="comments.allComments">
            <module:display name="/Activity Form/Identification/Results Comments" parentModule="/Activity Form/Identification">
            <digi:trn>Results Comments</digi:trn>
            <ul>
                <logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
                    <module:display name="/Activity Form/Identification/Results Comments/Results Assumption" parentModule="/Activity Form/Identification/Results Comments">
                        <logic:equal name="comments" property="key" value="Results Assumption">
                            <logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
                                <li>
                                    <digi:trn key="aim:resultsAssumption">Results Assumption</digi:trn>:<br />
                                    <span class="word_break bold"><bean:write name="comment" property="comment" /></span>
                                    <br/>
                                </li>
                            </logic:iterate>
                        </logic:equal>
                    </module:display>
                    <module:display name="/Activity Form/Identification/Results Comments/Results Verification" parentModule="/Activity Form/Identification/Results Comments">
                        <logic:equal name="comments" property="key" value="Results Verification">
                            <logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
                                <li>
                                    <digi:trn key="aim:resultsVerification">Results Verification</digi:trn>:<br />
                                    <span class="word_break bold"><bean:write name="comment" property="comment" /></span>
                                    <br/>
                                </li>
                            </logic:iterate>
                        </logic:equal>
                    </module:display>
                    <module:display name="/Activity Form/Identification/Results Comments/Results Objectively Verifiable Indicators" parentModule="/Activity Form/Identification/Results Comments">
                        <logic:equal name="comments" property="key" value="Results Objectively Verifiable Indicators">
                            <logic:iterate name="comments" id="comment" property="value" type="org.digijava.module.aim.dbentity.AmpComments">
                                <li>
                                    <digi:trn key="aim:resultsObjectivelyVerifiableIndicators">Results Objectively Verifiable Indicators</digi:trn>:<br />
                                    <span class="word_break bold"><bean:write name="comment" property="comment" /></span>
                                    <br/>
                                </li>
                            </logic:iterate>
                        </logic:equal>
                    </module:display>
                </logic:iterate>
            </ul>
            </module:display>
            <hr/>
            </logic:present>



            <module:display name="/Activity Form/Identification/Accession Instrument" parentModule="/Activity Form/Identification">
            <c:if test="${aimEditActivityForm.identification.accessionInstrument > 0}">
            <digi:trn key="aim:AccessionInstrument">Accession Instrument</digi:trn>:&nbsp;<br />
            <span class="word_break bold">
						<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.accessionInstrument}"/>
					</span>
            <hr />
            </c:if>
            </module:display>

            <!-- MISSING FIELD IN THE NEW FM STRUCTURE -->
            <module:display name="/Activity Form/Identification/Project Implementing Unit" parentModule="/Activity Form/Identification">
            <c:if test="${aimEditActivityForm.identification.projectImplUnitId > 0}">
            <digi:trn>Project Implementing Unit</digi:trn><br />
            <span class="word_break bold">
						<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.projectImplUnitId}"/>
					</span>
            </c:if>
            <hr/>
            </module:display>

            <module:display name="/Activity Form/Identification/A.C. Chapter" parentModule="/Activity Form/Identification">
            <c:if test="${aimEditActivityForm.identification.acChapter > 0}">
            <digi:trn>A.C. Chapter</digi:trn><br />
            <span class="word_break bold">
						<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.acChapter}"/>
					</span>
            <hr />
            </c:if>
            </module:display>

            <module:display name="/Activity Form/Identification/Cris Number" parentModule="/Activity Form/Identification">
            <digi:trn>Cris Number</digi:trn>:&nbsp;<br />
            <span class="word_break bold">
						<c:out value="${aimEditActivityForm.identification.crisNumber}"/>
					</span>
            <hr />
            </module:display>

            <module:display name="/Activity Form/Identification/IATI Identifier" parentModule="/Activity Form/Identification">
            <digi:trn>IATI Identifier</digi:trn>:&nbsp;<br />
            <span class="word_break bold">
						<c:out value="${aimEditActivityForm.identification.iatiIdentifier}"/>
					</span>
            <hr />
            </module:display>

            <module:display name="/Activity Form/Identification/Procurement System" parentModule="/Activity Form/Identification">
            <c:if test="${aimEditActivityForm.identification.procurementSystem > 0}">
            <digi:trn>Procurement System</digi:trn><br />
            <span class="word_break bold">
						<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.procurementSystem}"/>
					</span>
            </c:if>
            <hr />
            </module:display>

            <module:display name="/Activity Form/Identification/Reporting System" parentModule="/Activity Form/Identification">
            <c:if test="${aimEditActivityForm.identification.reportingSystem > 0}">
            <digi:trn>Reporting System</digi:trn>:&nbsp;<br />
            <span class="word_break bold">
						<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.reportingSystem}"/>
					</span>
            <hr />
            </c:if>
            </module:display>

            <module:display name="/Activity Form/Identification/Audit System" parentModule="/Activity Form/Identification">
            <c:if test="${aimEditActivityForm.identification.auditSystem > 0}">
            <digi:trn>Audit System</digi:trn>:&nbsp;<br />
            <span class="word_break bold">
						<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.auditSystem}"/>
					</span>
            </c:if>
            <hr />
            </module:display>

            <module:display name="/Activity Form/Identification/Institutions" parentModule="/Activity Form/Identification">
            <c:if test="${aimEditActivityForm.identification.institutions > 0}">
            <digi:trn>Institutions</digi:trn>:&nbsp;<br />
            <span class="word_break bold">
						<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.institutions}"/>
					</span>
            <hr />
            </c:if>
            </module:display>
            <!-- MISSING FIELD IN THE NEW FM STRUCTURE -->
            <module:display name="/Activity Form/Identification/Project Category" parentModule="/Activity Form/Identification">
            <c:if test="${aimEditActivityForm.identification.projectCategory > 0}">
            <digi:trn>Project Category</digi:trn><br />
            <span class="word_break bold">
						<category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.projectCategory}"/>
					</span>
            <hr />
            </c:if>
            </module:display>
            <module:display name="/Activity Form/Identification/Project Category Other Info"
                            parentModule="/Activity Form/Identification">
            <c:if test="${not empty aimEditActivityForm.identification.projectCategoryOtherInfo}">
            <digi:trn>Project Category Other Info</digi:trn>:&nbsp;<br />
            <span class="word_break bold"><c:out value="${aimEditActivityForm.identification.projectCategoryOtherInfo}"/></span>
            <hr />
            </c:if>
            </module:display>

            <!-- MISSING FIELD IN THE NEW FM STRUCTURE -->
            <module:display name="/Activity Form/Identification/Government Agreement Number" parentModule="/Activity Form/Identification">
            <c:if test="${not empty aimEditActivityForm.identification.govAgreementNumber}">
            <digi:trn>Government Agreement Number</digi:trn>:&nbsp;<br />
            <span class="word_break bold"> <c:out value="${aimEditActivityForm.identification.govAgreementNumber}"/>
				</span><hr />
            </c:if>
            </module:display>
</module:display>

<!-- BUDGET SECTION -->
<!-- MISSING FIELD IN THE NEW ACTIVITY FORM -->

<module:display name="/Activity Form/Identification/Activity Budget" parentModule="/Activity Form/Identification">
    <b><digi:trn>Budget</digi:trn></b><br/>
    <c:choose>
        <c:when test="${aimEditActivityForm.identification.budgetCV==aimEditActivityForm.identification.budgetCVOn}">
            <digi:trn>Activity is On Budget</digi:trn><br />
        </c:when>
        <c:when test="${aimEditActivityForm.identification.budgetCV==aimEditActivityForm.identification.budgetCVOff}">
            <digi:trn>Activity is Off Budget</digi:trn><br />
        </c:when>
        <c:when test="${aimEditActivityForm.identification.budgetCV==0}">
            <digi:trn>Budget Unallocated</digi:trn><br />
        </c:when>
        <c:otherwise>
            <digi:trn>Activity is On</digi:trn>
            <b><category:getoptionvalue categoryValueId="${aimEditActivityForm.identification.budgetCV}"/></b>
        </c:otherwise>
    </c:choose>
    <hr/>

    <c:if test="${!empty aimEditActivityForm.identification.chapterForPreview}" >
        <digi:trn>Code Chapitre</digi:trn>:<br />
        <span class="word_break bold"><bean:write name="aimEditActivityForm" property="identification.chapterForPreview.code" /> -
			<bean:write name="aimEditActivityForm" property="identification.chapterForPreview.description"/></span>]
        <digi:trn>Imputations</digi:trn>:<br />
        <logic:iterate id="imputation" name="aimEditActivityForm" property="identification.chapterForPreview.imputations">
				<span class="word_break bold"><bean:write name="aimEditActivityForm" property="identification.chapterForPreview.year"/> -
				<bean:write name="imputation" property="code" /> -
				<bean:write name="imputation" property="description" /></span>
            <hr>
        </logic:iterate>
    </c:if>
</module:display>
<c:if test="${aimEditActivityForm.identification.budgetCV==aimEditActivityForm.identification.budgetCVOn}">
    <module:display name="/Activity Form/Identification/Budget Extras/FY" parentModule="/Activity Form/Identification/Budget Extras">
        <digi:trn>FY</digi:trn>:&nbsp;
        <b><bean:write name="aimEditActivityForm" property="identification.FY"/></b>
        <br />
    </module:display>
    <module:display name="/Activity Form/Identification/Budget Extras/Ministry Code"  parentModule="/Activity Form/Identification/Budget Extras">
        <digi:trn>Ministry Code</digi:trn>:&nbsp;
        <b><bean:write name="aimEditActivityForm" property="identification.ministryCode"/></b>
        <br />
    </module:display>

    <module:display name="/Activity Form/Identification/Budget Extras/Project Code" parentModule="/Activity Form/Identification/Budget Extras">
        <digi:trn>Project Code</digi:trn>:&nbsp;
        <b><bean:write name="aimEditActivityForm" property="identification.projectCode"/></b>
        <br />
    </module:display>

    <module:display name="/Activity Form/Identification/Budget Extras/Vote"  parentModule="/Activity Form/Identification/Budget Extras">
        <digi:trn>Vote</digi:trn>:&nbsp;
        <span class="word_break bold"><bean:write name="aimEditActivityForm" property="identification.vote"/></span>
        <br />
    </module:display>
    <module:display name="/Activity Form/Identification/Budget Extras/Sub-Vote"  parentModule="/Activity Form/Identification/Budget Extras">
        <digi:trn>Sub-Vote </digi:trn>:&nbsp;
        <span class="word_break bold"><bean:write name="aimEditActivityForm" property="identification.subVote"/></span>
        <br />
    </module:display>
    <module:display name="/Activity Form/Identification/Budget Extras/Sub-Program" parentModule="/Activity Form/Identification/Budget Extras">
        <digi:trn>Sub-Program</digi:trn>:&nbsp;
        <span class="word_break bold"><bean:write name="aimEditActivityForm" property="identification.subProgram"/></span>
        <br />
    </module:display>

</c:if>

<hr>
<module:display name="/Activity Form/Identification/Budget Classification" parentModule="/Activity Form/Identification">
    <c:if test="${!empty aimEditActivityForm.identification.selectedbudgedsector}">
        <digi:trn>Budget Classification</digi:trn>:<br />
        <c:forEach var="selectedsector" items="${aimEditActivityForm.identification.budgetsectors}">
            <c:if test="${aimEditActivityForm.identification.selectedbudgedsector==selectedsector.idsector}">
                <li style="margin-left: 10px">
							<span class="word_break bold">
								<c:out value="${selectedsector.code}" /> - <c:out value="${selectedsector.sectorname}" />
							</span>
                </li>
            </c:if>
        </c:forEach>
        <br>
    </c:if>

    <c:if test="${!empty aimEditActivityForm.identification.selectedorg}">
        <c:forEach var="selectedorgs" items="${aimEditActivityForm.identification.budgetorgs}">
            <c:if test="${aimEditActivityForm.identification.selectedorg==selectedorgs.ampOrgId}">
                <li style="margin-left: 10px"><c:out value="${selectedorgs.budgetOrgCode}"/> -
                    <span class="word_break bold"><c:out value="${selectedorgs.name}" /></span>
                </li>
            </c:if>
        </c:forEach>
        <br>
    </c:if>

    <c:if test="${!empty aimEditActivityForm.identification.selecteddepartment}">
        <c:forEach var="selecteddep" items="${aimEditActivityForm.identification.budgetdepartments}">
            <c:if test="${aimEditActivityForm.identification.selecteddepartment==selecteddep.id}">
                <li style="margin-left: 10px">
							<span class="word_break bold"><c:out value="${selecteddep.code}"/> -
							<c:out value="${selecteddep.name}"/></span>
                </li>
            </c:if>
        </c:forEach>
        <br>
    </c:if>

    <c:if test="${!empty aimEditActivityForm.identification.selectedprogram}" >
        <c:forEach var="selectedprog" items="${aimEditActivityForm.identification.budgetprograms}">
            <c:if test="${aimEditActivityForm.identification.selectedprogram==selectedprog.ampThemeId}">
                <li style="margin-left: 10px">
							<span class="word_break bold">
								<c:out value="${selectedprog.themeCode}"/> - <c:out value="${selectedprog.name}" />
							</span>
                </li>
            </c:if>
        </c:forEach>
    </c:if>
    <hr/>
</module:display>

<!-- END BUDGET SECTION -->

<!-- INDETIFICATION SECTION 2 -->
<!-- MISSING FIELD IN THE NEW FM STRUCTURE -->
<field:display feature="Identification" name="Organizations and Project ID">
    <digi:trn>Organizations and Project IDs</digi:trn>:&nbsp;<br />
    <c:if test="${!empty aimEditActivityForm.identification.selectedOrganizations}">
        <table cellSpacing=2 cellPadding=2 border="0">
            <c:forEach var="selectedOrganizations" items="${aimEditActivityForm.identification.selectedOrganizations}">
                <c:if test="${not empty selectedOrganizations}">
                    <tr>
                        <td>
                            <c:if test="${!empty selectedOrganizations.organisation.ampOrgId}">
                                <bean:define id="selectedOrgForPopup"
                                             name="selectedOrganizations"
                                             type="org.digijava.module.aim.helper.OrgProjectId"
                                             toScope="request"/>
                                <jsp:include page="../previewOrganizationPopup.jsp" />
                            </c:if>
                        </td>
                    </tr>
                </c:if>
            </c:forEach>
        </table><hr />
    </c:if>
</field:display>

<module:display name="/Activity Form/Identification/Government Approval Procedures" parentModule="/Activity Form/Identification">
    <c:if test="${aimEditActivityForm.identification.governmentApprovalProcedures!=null}">
        <digi:trn>Government Approval Procedures</digi:trn>:&nbsp;<br />
        <c:if test="${aimEditActivityForm.identification.governmentApprovalProcedures==false}">
            <b><digi:trn key="aim:no">No</digi:trn></b>
        </c:if>
        <c:if test="${aimEditActivityForm.identification.governmentApprovalProcedures==true}">
            <b><digi:trn key="aim:yes">Yes</digi:trn></b>
        </c:if>
        <hr/>
    </c:if>
</module:display>
<module:display name="/Activity Form/Identification/Joint Criteria" parentModule="/Activity Form/Identification">
    <c:if test="${aimEditActivityForm.identification.jointCriteria!=null}">
        <digi:trn>Joint Criteria</digi:trn>:&nbsp;<br />
        <c:if test="${aimEditActivityForm.identification.jointCriteria==false}">
            <b><digi:trn key="aim:no">No</digi:trn></b>
        </c:if>
        <c:if test="${aimEditActivityForm.identification.jointCriteria==true}">
            <b><digi:trn key="aim:yes">Yes</digi:trn></b>
        </c:if>
        <hr/>
    </c:if>
</module:display>
<module:display name="/Activity Form/Identification/Humanitarian Aid" parentModule="/Activity Form/Identification">
    <c:if test="${aimEditActivityForm.identification.humanitarianAid!=null}">
        <digi:trn>Humanitarian Aid</digi:trn>:&nbsp;<br />
        <c:if test="${aimEditActivityForm.identification.humanitarianAid==false}">
            <b><digi:trn key="aim:no">No</digi:trn></b>
        </c:if>
        <c:if test="${aimEditActivityForm.identification.humanitarianAid==true}">
            <b><digi:trn key="aim:yes">Yes</digi:trn></b>
        </c:if>
        <hr/>
    </c:if>
</module:display>
<!-- END INDETIFICATION SECTION 2 -->
</div>
</fieldset>
