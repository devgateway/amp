<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<c:set var="previewTitle">
	<digi:trn>This button will take you to the Activity Preview page</digi:trn>
</c:set>
<c:set var="previewLogframeTitle">
	<digi:trn>This button will take you the Logframe overview page</digi:trn>
</c:set>
<c:set var="saveAsDraftTitle">
	<digi:trn>This button will save all changes that were made to the activity. The activity will be saved as a Draft</digi:trn>
</c:set>
<c:set var="saveAndSubmitTitle">
	<digi:trn>This button will close the activity, submit for validation and will take you to My Desktop</digi:trn>
</c:set>
<c:set var="closeTitle">
	<digi:trn>This button will take you back to My Desktop and will alert you if the changes to an activity have not been saved</digi:trn>
</c:set>

<table width="100%">
	<tr>	
		<td height="35" vAlign=center class="separator2">
			<feature:display name="Logframe" module="Previews">
				<field:display name="Logframe Preview Button" feature="Logframe" >
					<a id="linkEditActivityMenu" onclick="previewLogFrameClicked()" title="${previewLogframeTitle}">
						<digi:trn>Preview Logframe</digi:trn>&nbsp;|&nbsp;
					</a>
				</field:display>
			</feature:display>
			<feature:display name="Logframe" module="Previews">
				<field:display name="Logframe Preview Button" feature="Logframe" >			
					<a id="linkEditActivityMenu" onclick="previewClicked()">
						<digi:trn>Preview</digi:trn>&nbsp;|&nbsp;
					</a>
				</field:display>
			</feature:display>
			<a id="linkEditActivityMenu" onclick="saveClicked()" title="${saveAndSubmitTitle}">
				<digi:trn>Save and Submit</digi:trn>&nbsp;|&nbsp;
			</a>
			<field:display name="Draft" feature="Identification">
				<a id="linkEditActivityMenu" onclick="saveAsDraftClicked()" title="${saveAsDraftTitle}">
					<digi:trn>Save as Draft</digi:trn>&nbsp;|&nbsp;
				</a>
			</field:display>
			<a id="linkEditActivityMenu" onclick="closeClicked()" title="${closeTitle}"><digi:trn>Close</digi:trn></a>
		</td>
	</tr>
</table>