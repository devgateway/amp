<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript" type="text/javascript">

</script>

<digi:instance property="aimEditActivityForm" />
							<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#f4f4f2">
                                <tr>
                                  <td bgColor=#f4f4f2 align="center" vAlign="top"><!-- contents -->
                                    <table width="95%" bgcolor="#f4f4f2">
                                    
                                      <tr>
                                        <td>
                                          <IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15 />
                                          <a title="<digi:trn key="aim:ProgramImp">Set of policies, projects and strategies grouped by area</digi:trn>">
                                          <b>
                                            <digi:trn key="aim:program">
                                              Program
                                            </digi:trn>
                                          </b>
										  </a>
                                        </td>
                                      </tr>
                                      <tr>
                                        <td>
                                          <digi:trn key="aim:selectProgram">
                                            Select the program from the list.
                                          </digi:trn>
                                        </td>
                                      </tr>
                                      <tr>
                                        <td>
                                          <table cellPadding=5 cellSpacing=1 border=0 width="100%" bgcolor="#d7eafd">
                                            <tr>
                                              <td align="left">
                                                <b>
                                                  <digi:trn key="aim:activitySelectedPrograms">
                                                  Programs
                                                  </digi:trn>
                                                </b>
                                              </td>
                                            </tr>
                                            <tr>
                                              <td bgcolor="#ffffff" width="100%">
                                                <table cellPadding=1 cellSpacing=1 border=0	bgcolor="#ffffff" width="100%">
                                                  <tr>
                                                    <td bgcolor="#ffffff">
                                                    </td>
                                                  </tr>

                                                  <tr>
                                                    <td>
                                                      <table cellSpacing=0 cellPadding=0 border=0 bgcolor="#ffffff" width="100%">
                                                        <c:if test="${!empty aimEditActivityForm.actPrograms}">
                                                          <c:forEach var="theProgram" items="${aimEditActivityForm.actPrograms}">
                                                            <tr>
                                                              <td>
                                                                <table width="100%" cellSpacing=1 cellPadding=1 vAlign="top" align="left">
                                                                  <tr>
                                                                    <td>
                                                                      <html:multibox property="selectedPrograms" value="${theProgram.ampThemeId}">
                                                                      ${theProgram.ampThemeId}
                                                                      </html:multibox>
                                                                      ${theProgram.programviewname}
                                                                    </td>
                                                                  </tr>
                                                                </table>
                                                              </td>
                                                            </tr>
                                                          </c:forEach>

                                                        </c:if>
                                                        <tr>
                                                          <td>
                                                          <field:display name="Add Program Button" feature="NPD Programs">
                                                            <input type="button" value='<digi:trn key="btn:addPrograms">Add Programs</digi:trn>' onclick="addProgram();" class="buton">
                                                            </field:display>
                                                            <c:if test="${!empty aimEditActivityForm.actPrograms}">
                                                            <field:display name="Remove Program Button" feature="NPD Programs">
                                                              <input type="button" value='<digi:trn key="btn:removeProgram">Remove program</digi:trn>' onclick="remProgram();" class="buton">
                                                              </field:display>
                                                            </c:if>
                                                          </td>
                                                        </tr>
                                                      </table>
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                          </table>
                                        </td>
                                      </tr>
                                      <field:display name="Description of Program" feature="NPD Programs">
                                      <tr>
                                        <td>
                                          <a title="<digi:trn key="aim:ProgramDesc">Description of program, objectives, or associated projects</digi:trn>">
                                          <digi:trn key="aim:description">Description</digi:trn>
											</a>                                    
										</td>
                                      </tr>
                                      <tr>
                                        <td>
                                          <a title="<digi:trn key="aim:ProgramDesc">Description of program, objectives, or associated projects</digi:trn>">
                                          <html:textarea property="programDescription" rows="3" cols="75" styleClass="inp-text"/>
											</a>
                                        </td>
                                      </tr>
									</field:display>
								</table>
							</td>
						</tr>
					</table>