function sameAsfunction(val)
{
		if(val == 1)
		{
			if(document.aimEditActivityForm.sameAs1.checked == true){
				document.aimEditActivityForm.revisedAppDate.value = document.aimEditActivityForm.originalAppDate.value;
				clearDisplay(document.getElementById('date2'),'clear2');				
			}			
				
			else
				document.aimEditActivityForm.revisedAppDate.value = "";
				clearDisplay(document.aimEditActivityForm.revisedAppDate,'clear2');
		}
		else if(val == 2)
		{
			if(document.aimEditActivityForm.sameAs2.checked == true) {
				document.aimEditActivityForm.revisedStartDate.value = document.aimEditActivityForm.originalStartDate.value;
				clearDisplay(document.getElementById('date4'),'clear4');	
			}
				
			else
				document.aimEditActivityForm.revisedStartDate.value = "";
				clearDisplay(document.aimEditActivityForm.revisedStartDate,'clear4');
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
	if (message == 'no')
		return true;
	
	var temp = confirm(msg);
	
	
	return(temp);
}

function ButtonWrapper(id) {
	this.id		= id;
}
ButtonWrapper.prototype.enable	= function () {
	var buttonEl			= document.getElementById (this.id);
	buttonEl.disabled		= false;
	buttonEl.style.color	= "";
}
ButtonWrapper.prototype.disable	= function () {
	var buttonEl			= document.getElementById (this.id);
	buttonEl.disabled		= true;
	buttonEl.style.color	= "lightgray";
}



