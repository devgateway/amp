function sameAsfunction(val)
{
		if(val == 1)
		{
			if(document.aimEditActivityForm.sameAs1.checked == true)
				document.aimEditActivityForm.revisedAppDate.value = document.aimEditActivityForm.originalAppDate.value;
			else
				document.aimEditActivityForm.revisedAppDate.value = "";
		}
		else if(val == 2)
		{
			if(document.aimEditActivityForm.sameAs2.checked == true)
				document.aimEditActivityForm.revisedStartDate.value = document.aimEditActivityForm.originalStartDate.value;
			else
				document.aimEditActivityForm.revisedStartDate.value = "";
		}
}

function quitRnot()
{
	var temp = confirm('WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.');
	return(temp);
}
function quitRnot1(message)
{
	var msg= message;
	var temp = confirm(msg);
	return(temp);
}
