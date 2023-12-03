<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>

<digi:instance property="aimEditActivityForm" />
<%--@elvariable id="aimEditActivityForm" type="org.digijava.ampModule.aim.form.EditActivityForm"--%>

<ampModule:display name="/Activity Form/Planning" parentModule="/Activity Form">
    <fieldset>
        <legend>
		<span class=legend_label id="planninglink" style="cursor: pointer;">
			<digi:trn>Planning</digi:trn>
		</span>
        </legend>
        <div id="planningdiv" class="toggleDiv">
            <ampModule:display name="/Activity Form/Planning/Line Ministry Rank" parentModule="/Activity Form/Planning">
                <br>
                <div class="planning-line"><digi:trn>Line Ministry Rank</digi:trn>:&nbsp;</div>
                <c:if test="${aimEditActivityForm.planning.lineMinRank == -1}"></c:if>
                <c:if test="${aimEditActivityForm.planning.lineMinRank != -1}">
                    <div class="planning-line"><b>${aimEditActivityForm.planning.lineMinRank}</b></div>				</c:if>
            </ampModule:display>


            <ampModule:display name="/Activity Form/Planning/Proposed Approval Date" parentModule="/Activity Form/Planning">
                <hr>
                <div class="planning-line"><digi:trn>Proposed Approval Date</digi:trn>:&nbsp;</div>
                <div class="planning-line"><b>${aimEditActivityForm.planning.originalAppDate}</b></div>
            </ampModule:display>

            <!-- MISSING FIELD IN THE NEW FM STRUCTURE -->
            <ampModule:display name="/Activity Form/Planning/Actual Approval Date" parentModule="/Activity Form/Planning">
                <hr>
                <div class="planning-line"><digi:trn>Actual Approval Date</digi:trn>:&nbsp;</div>
                <div class="planning-line"><b>${aimEditActivityForm.planning.revisedAppDate}</b></div>
            </ampModule:display>

            <ampModule:display name="/Activity Form/Planning/Proposed Start Date" parentModule="/Activity Form/Planning">
                <hr>
                <div class="planning-line"><digi:trn>Proposed Start Date</digi:trn>:&nbsp;</div>
                <div class="planning-line"><b>${aimEditActivityForm.planning.originalStartDate}</b></div>
            </ampModule:display>

            <ampModule:display name="/Activity Form/Planning/Actual Start Date" parentModule="/Activity Form/Planning">
                <hr>
                <div class="planning-line"><digi:trn>Actual Start Date </digi:trn>:&nbsp;</div>
                <div class="planning-line"><b>${aimEditActivityForm.planning.revisedStartDate}</b></div>
            </ampModule:display>

            <ampModule:display name="/Activity Form/Planning/Original Completion Date" parentModule="/Activity Form/Planning">
                <hr>
                <div class="planning-line"><digi:trn>Original Completion Date</digi:trn>:&nbsp;</div>
                <div class="planning-line"><b>${aimEditActivityForm.planning.originalCompDate}</b></div>
            </ampModule:display>

            <ampModule:display name="/Activity Form/Planning/Proposed Completion Date" parentModule="/Activity Form/Planning">
                <hr>
                <div class="planning-line"><digi:trn>Proposed Completion Date</digi:trn>:&nbsp;</div>
                <div class="planning-line"><b>${aimEditActivityForm.planning.proposedCompDate}</b></div>
            </ampModule:display>
            <ampModule:display name="Project ID and Planning" parentModule="PROJECT MANAGEMENT">
                <feature:display name="Planning" ampModule="Project ID and Planning">
                    <field:display name="Final Date for Disbursements Comments" feature="Planning">
                        <hr>
                        <div class="planning-line"><digi:trn>Final Date for Disbursements comments</digi:trn>:&nbsp;</div>
                        <div class="planning-line">
                            <ul>
                                <logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
                                    <logic:equal name="comments" property="key" value="Final Date for Disbursements">
                                        <logic:iterate name="comments" id="comment" property="value" type="org.digijava.ampModule.aim.dbentity.AmpComments">
                                            <li>
											<span class="word_break ">
												<bean:write name="comment" property="comment" />
											</span>
                                                <br />
                                            </li>
                                        </logic:iterate>
                                    </logic:equal>
                                </logic:iterate>
                            </ul>
                        </div>
                    </field:display>
                </feature:display>
            </ampModule:display>

            <ampModule:display name="/Activity Form/Planning/Actual Completion Date" parentModule="/Activity Form/Planning">
                <hr>
                <div class="planning-line"><digi:trn>Actual Completion Date</digi:trn>:&nbsp;</div>
                <div class="planning-line"><b><c:out value="${aimEditActivityForm.planning.currentCompDate}"/></b></div>
            </ampModule:display>

            <ampModule:display name="Project ID and Planning" parentModule="PROJECT MANAGEMENT">
                <feature:display name="Planning" ampModule="Project ID and Planning">
                    <field:display name="Current Completion Date Comments" feature="Planning">
                        <hr>
                        <div class="planning-line"><digi:trn>Current Completion Date comments</digi:trn>:&nbsp;</div>
                        <div class="planning-line">
                            <ul>
                                <logic:iterate name="aimEditActivityForm" id="comments" property="comments.allComments">
                                    <logic:equal name="comments" property="key" value="current completion date">
                                        <logic:iterate name="comments" id="comment" property="value" type="org.digijava.ampModule.aim.dbentity.AmpComments">
                                            <li>
											<span class="word_break ">
												<bean:write name="comment" property="comment" />
											</span>
                                                <br />
                                            </li>
                                        </logic:iterate>
                                    </logic:equal>
                                </logic:iterate>
                            </ul>
                        </div>
                    </field:display>
                </feature:display>
            </ampModule:display>

            <ampModule:display name="/Activity Form/Planning/Project Implementation Delay" parentModule="/Activity Form/Planning">
                <hr>
                <div class="planning-line"><digi:trn>Project Implementation Delay</digi:trn>:&nbsp;</div>
                <div class="planning-line"><b>${aimEditActivityForm.planning.projectImplementationDelay}</b></div>
            </ampModule:display>

            <ampModule:display name="/Activity Form/Planning/Final Date for Contracting" parentModule="/Activity Form/Planning">
                <hr>
                <div class="planning-line"><digi:trn>Final Date for Contracting</digi:trn>:&nbsp;</div>
                <div class="planning-line"><b><c:out value="${aimEditActivityForm.planning.contractingDate}"/></b></div>
            </ampModule:display>

            <ampModule:display name="/Activity Form/Planning/Final Date for Disbursements" parentModule="/Activity Form/Planning">
                <hr>
                <div class="planning-line"><digi:trn>Final Date for Disbursements</digi:trn>:&nbsp;</div>
                <div class="planning-line"><b><c:out value="${aimEditActivityForm.planning.disbursementsDate}"/></b></div>
            </ampModule:display>

            <ampModule:display name="/Activity Form/Planning/Proposed Project Life" parentModule="/Activity Form/Planning">
                <hr>
                <div class="planning-line"><digi:trn>Proposed Project Life</digi:trn>:&nbsp;</div>
                <div class="planning-line"><b>${aimEditActivityForm.planning.proposedProjectLife}</b></div>
            </ampModule:display>

            <field:display name="Duration of Project" feature="Planning">
                <hr>
                <div class="planning-line"><digi:trn>Duration of project</digi:trn>:&nbsp;</div>
                <c:if test="${not empty aimEditActivityForm.planning.projectPeriod}">
                    <div class="planning-line">
                        <b>${aimEditActivityForm.planning.projectPeriod}</b>&nbsp;<digi:trn>Months</digi:trn>
                    </div>
                </c:if>
            </field:display>
        </div>
    </fieldset>
</ampModule:display>
