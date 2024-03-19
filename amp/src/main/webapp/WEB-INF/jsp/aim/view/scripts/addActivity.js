// this file is still used in lots of AMP jsp's. Misnamed, but not worth the effort NOW to move the functions
function quitRnot()
{
	var temp = confirm('WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.');
	return(temp);
}

function quitRnot1(message)
{
	var msg= message;
	if (message == 'no' || 
			message =='')
		return true;
	
	var temp = confirm(msg);
	
	
	return(temp);
}
