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
<div style="width:100%;text-align:center;">
    <feature:display name="Logframe" module="Previews">
        <field:display name="Logframe Preview Button" feature="Logframe" >
            <a onclick="previewLogFrameClicked()" title="${previewLogframeTitle}" style="cursor:pointer;">
                <img src="/TEMPLATE/ampTemplate/imagesSource/common/preview_logframe.png" alt="<digi:trn>Preview Logframe</digi:trn>">
            </a>
        </field:display>
    </feature:display>
    <a onclick="previewClicked()" title="${previewTitle}" style="cursor:pointer;">
        <img src="/TEMPLATE/ampTemplate/imagesSource/common/preview.png" alt="<digi:trn>Preview</digi:trn>">
    </a>
    <a onclick="saveClicked()" title="${saveAndSubmitTitle}" style="cursor:pointer;">
        <img src="/TEMPLATE/ampTemplate/imagesSource/common/save_22.png" alt="<digi:trn>Save and Submit</digi:trn>">
    </a>
    <field:display name="Draft" feature="Identification">
        <a onclick="saveAsDraftClicked()" title="${saveAsDraftTitle}" style="cursor:pointer;">
            <img src="/TEMPLATE/ampTemplate/imagesSource/common/savedraft_22.png" alt="<digi:trn>Save as Draft</digi:trn>">
        </a>
    </field:display>
    <a onclick="closeClicked()" title="${closeTitle}" style="cursor:pointer;">
        <img src="/TEMPLATE/ampTemplate/imagesSource/common/close.gif" alt="<digi:trn>Close</digi:trn>">
    </a>
</div>
