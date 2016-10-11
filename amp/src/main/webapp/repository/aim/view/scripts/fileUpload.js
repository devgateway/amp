function initFileUploads(value) {
	var fakeFileUpload = $('<div class="fakefile"></div>');
	var fileNameInput = $('<input type="text" name="fileNameInput"/>');
	fakeFileUpload.append (fileNameInput);
	var browseContainer =  $('<div class="fakefile2"/>');
	var button = $('<input type ="button" name="fakeInputFile" value='+value+'> </input>');
	browseContainer.append(button);

	fakeFileUpload.append(browseContainer);
	
	var fileHidden = $('.fileinputs input:file');
	fileHidden.addClass ('file hidden');
	var clone = fakeFileUpload.clone(true);
	fileHidden.parent().append($(clone));
	
	$('[name="fakeInputFile"]').on('click',function (event){ 
		
		//we need to access through the parents because there are pages with more than one file input
		$(this).parents('.fileinputs').find('input:file').click();
		}
	);
	fileHidden.on('change', function(event) {
		var value  = $(this).parents('.fileinputs').find('input:file').val();
		$(this).parent().find ('[name="fileNameInput"]').val(value);
	});

}