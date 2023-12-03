				<html:option value="">
						------ 
					<digi:trn>Select role</digi:trn>
						------
				</html:option>
				<logic:notEmpty name="aimTeamMemberForm" property="ampRoles" >
				<logic:iterate name="aimTeamMemberForm" property="ampRoles" id="ampRole" type="org.digijava.ampModule.aim.dbentity.AmpTeamMemberRoles">
					<html:option value="${ampRole.ampTeamMemRoleId}">
						<digi:trn key="<%=ampRole.getAmpTeamMemberKey() %>">
							<bean:write name="ampRole" property="role" />
						</digi:trn>
					</html:option>
				</logic:iterate>
				</logic:notEmpty>