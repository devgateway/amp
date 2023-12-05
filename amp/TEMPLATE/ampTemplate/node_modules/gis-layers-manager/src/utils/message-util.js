var _ = require('underscore');
var messageUtil = {};
messageUtil.displayErrors = function(element, errors){	  
	  var messageBox = element.find('.message-box');
	  messageUtil.removeAlertClasses(element);	
	  messageBox.addClass('alert-danger');
	  messageBox.empty();	  
	_.each(errors, function(error){
		var span = $('<span></span>');		
		span.html(error);
		messageBox.append($('<span class="glyphicon glyphicon-exclamation-sign"></span>'));
		messageBox.append(span);
		messageBox.append($('<br>'));
	});  
	messageBox.show();
};

messageUtil.displaySuccessMessage = function(element, message){
	  var messageBox = element.find('.message-box');	  
	  messageUtil.removeAlertClasses(element);	
	  messageBox.addClass('alert-success');
	  messageBox.empty();
	  var span = $('<span></span>');		
	  span.html(message);
	  messageBox.append(span);
	  messageBox.append($('<br>'));	 	
	  messageBox.show();  
};

messageUtil.displayInfoMessage =  function(element, message){
	  var messageBox = element.find('.message-box');	  
	  messageUtil.removeAlertClasses(element);	  
	  messageBox.addClass('alert-info');
	  messageBox.empty();
	  var span = $('<span></span>');		
	  span.html(message);
	  messageBox.append(span);
	  messageBox.append($('<br>'));	 	
	  messageBox.show();  
};

messageUtil.removeAlertClasses = function(element){
	  var messageBox = element.find('.message-box');
	  messageBox.removeClass('alert-danger');
	  messageBox.removeClass('alert-success');	  
	  messageBox.removeClass('alert-info');
};
module.exports = messageUtil;
