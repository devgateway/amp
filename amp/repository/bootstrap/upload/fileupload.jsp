<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%--
	renders a ajax-file-upload-with-progress (progress only works according to about.txt)
	params:
		button_caption: (NOT translated) caption
		url: the URL to which to post the file. That URL should respond according to https://github.com/blueimp/jQuery-File-Upload/wiki/Setup
	@author: Dolghier Constantin
	
  --%>

<!-- The fileinput-button span is used to style the file input field as button -->
<div class="file-upload-container" data-post-url="/selectPledgeProgram.do?extraAction=file_upload">
    <span class="btn btn-success fileinput-button">
        <i class="glyphicon glyphicon-plus"></i>
        <span><digi:trn>${param.button_caption}</digi:trn></span>
        <!-- The file input field used as target for the file upload widget -->
        <%--<input class="fileupload" type="file" name="files[]">  --%>
		<input class="fileupload" type="file" name="files">        
    </span>
    <br />
    <br />
    <!-- The global progress bar -->
    <div id="progress" class="progress">
        <div class="progress-bar progress-bar-success"></div>
    </div>
    <!-- The container for the uploaded files -->
    <div class="files-list"></div>
</div>
