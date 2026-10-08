Wicket.Event.add(window, "domready", function(event){
    //setupFileUpload('#${componentMarkupId}', '${url}', '${paramName}');
});

$.getScript("/TEMPLATE/ampTemplate/script/common/FileTypeValidator.js");

function setupFileUpload(componentId, componentUrl, componentParamName, deferUpload, importButtonMarkupId, importedRowsMarkupId, fileOnlyUpload){
    $(function () {
        var pendingUploadData = null;
        var waitForUpload = false;
        var uploadInProgress = false;
        var allowImportClick = false;
        var importButton = importButtonMarkupId ? document.getElementById(importButtonMarkupId) : null;
        var importedRows = importedRowsMarkupId ? document.getElementById(importedRowsMarkupId) : null;
        var fileInput = $(componentId).find('input[type=file]').get(0);

        function submitPendingUpload() {
            var uploadData = pendingUploadData;
            pendingUploadData = null;
            if (uploadData) {
                if (componentUrl.indexOf('/rest/resource/stage-upload') !== -1 && importedRows) {
                    try {
                        var previousUploads = JSON.parse(importedRows.value || '[]');
                        if (previousUploads.length && previousUploads[0].uploadId) {
                            var separator = componentUrl.indexOf('?') === -1 ? '?' : '&';
                            uploadData.url = componentUrl + separator + 'replaceUploadId='
                                + encodeURIComponent(previousUploads[0].uploadId);
                        }
                    } catch (error) {
                        // Leave the original URL; the server still enforces the session quota.
                    }
                }
                uploadInProgress = true;
                var fileSize = uploadData.files[0].size;
                $(componentId).find('[role=fileUploadedMsg]').show()
                    .html(" \"" + "${uploadStartedMsg}" + fileSize + "\" bytes");
                uploadData.submit();
            }
        }

        if (deferUpload && importButton && fileInput) {
            $(componentId).find('.fileupload-progress, .fileupload-loading').hide();
            if (importButton.ampDeferredUploadClickHandler) {
                importButton.removeEventListener('click', importButton.ampDeferredUploadClickHandler, true);
            }
            importButton.ampDeferredUploadClickHandler = function (event) {
                if (allowImportClick) {
                    allowImportClick = false;
                    return;
                }
                if (importedRows && importedRows.value && !pendingUploadData) {
                    return;
                }

                event.preventDefault();
                event.stopImmediatePropagation();
                if (uploadInProgress) {
                    return;
                }
                waitForUpload = true;
                if (pendingUploadData) {
                    submitPendingUpload();
                } else {
                    fileInput.click();
                }
            };
            importButton.addEventListener('click', importButton.ampDeferredUploadClickHandler, true);
        }

        var uploadOptions = {
            url: componentUrl,
            paramName: componentParamName,
            singleFileUploads: true,
            iframe:true,
            dataType:'json',
            minFileSize: 1,
            maxFileSize: 20000000,
            add: function (e, data) {
            	try {
	            	if (!FileTypeValidator.isValid(data.files[0].name)) {
	            		alert(FileTypeValidator.errorMessage);
	            		$('#uploadLabel').text(FileTypeValidator.errorMessage);
	            		$(this).find('[role=fileUploadedMsg]').html('');
	                    $(this).find('[role=fileUploadedMsg]').hide();
	            	} else if (data.files[0].size > parseFloat("${uploadMaxFileSize}")) {
	            		alert("${uploadFailedTooBigMsg}");
	            		$('#uploadLabel').text("${uploadNoFileLabel}");
	            		$(this).find('[role=fileUploadedMsg]').html('');
	                    $(this).find('[role=fileUploadedMsg]').hide();
	            	} else {
                        if (deferUpload) {
                            pendingUploadData = data;
                            if (waitForUpload) {
                                submitPendingUpload();
                            } else {
                                $(this).find('[role=fileUploadedMsg]').show()
                                    .text("${uploadPendingMsg}");
                            }
                        } else {
                            $(this).find('[role=fileUploadedMsg]').show()
                                .html(" \"" + "${uploadStartedMsg}" + data.files[0].size + "\" bytes");
                            data.submit();
                        }
	            	}
            	} catch(err) {
            		alert(FileTypeValidator.errorMessage);
            		$('#uploadLabel').text(FileTypeValidator.errorMessage);
            		$(this).find('[role=fileUploadedMsg]').html('');
                    $(this).find('[role=fileUploadedMsg]').hide();
            	}
            },
            done: function (e, data){
                if (deferUpload && waitForUpload && importButton && importedRows) {
                    var result = data.result;
                    try {
                        if (typeof result === 'string') {
                            result = JSON.parse(result);
                        }
                        if (!Array.isArray(result)) {
                            throw new Error('Unexpected structure import response');
                        }
                    } catch (error) {
                        alert("${uploadFailedMsg}");
                        waitForUpload = false;
                        uploadInProgress = false;
                        return;
                    }
                    importedRows.value = JSON.stringify(result);
                    waitForUpload = false;
                    uploadInProgress = false;
                    allowImportClick = true;
                    importButton.click();
                } else if (!deferUpload) {
                    var result = eval(data.result)[0];
                    $(this).find('[role=fileUploadedMsg]').html(result.uploadTxt);
                }
            },
            fail: function (e, data){
                //alert('upload failed! result[' + JSON.stringify(data.result) + '] status[' + data.textStatus + '] jqXHR[' + JSON.stringify(data.jqXHR) +']');
                alert("${uploadFailedMsg}");
                $('#uploadLabel').text("${uploadNoFileLabel}");
                $(this).find('[role=fileUploadedMsg]').html('');
                $(this).find('[role=fileUploadedMsg]').hide();
                waitForUpload = false;
                uploadInProgress = false;
                pendingUploadData = null;
            }
        };
        if (fileOnlyUpload) {
            uploadOptions.formData = [];
        }
        $(componentId).fileupload(uploadOptions);
    });
}

