function FormatDateHelper(oData) {
	this.oData	= oData;
	if (FormatDateHelper.prototype.formatString != null) {
		this.fString	= FormatDateHelper.prototype.formatString.toLowerCase(); 
	}
	else
		this.fString	= "mm/dd/yyyy";
}
FormatDateHelper.prototype.formatString	= null;
FormatDateHelper.prototype.months		= ["Jan", "Feb", "Mar", "Apr", "May","Jun", "Jul","Aug","Sep","Oct","Nov","Dec"];

FormatDateHelper.prototype.formatDate	= function () {
	var returnString	= "";
	var elements		= this.fString.split("/");
	if ( elements != null && elements.length == 3 ) {
		for (var i=0; i<elements.length; i++) {
			if (elements[i] == "dd") {
				returnString += this.oData.getDate() + "/";
			}
			else if (elements[i] == "mm") {
				returnString += (this.oData.getMonth()+1) + "/";
			}
			else if (elements[i] == "mmm") {
				var monthName	= FormatDateHelper.prototype.months[this.oData.getMonth()];
				returnString 	+= monthName + "/";
			}
			else if (elements[i] == "yyyy") {
				returnString += this.oData.getFullYear() + "/";
			}
		}
		returnString		= returnString.substr(0,returnString.length-1);
		return returnString;
	}
	return "";
}