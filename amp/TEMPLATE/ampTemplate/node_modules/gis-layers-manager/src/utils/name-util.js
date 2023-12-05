var _ = require('underscore');
var nameUtil = {};

nameUtil.isNameAvailable = function(name, oldName){
	var deferred = $.Deferred();
	var deffereds = [];
	var results = {};
	var langs = _.keys(name);
	_.each(langs,function(lang){		
		if(!_.isUndefined(name[lang]) && !_.isNull(name[lang])){
			var checkName = true;
			//in edit mode check only if name has changed
			if(!_.isUndefined(oldName[lang]) && !_.isNull(oldName[lang])){
				checkName = oldName[lang] !== name[lang]
			}
			if(checkName){
				deffereds.push(nameUtil._checkName(name[lang]).then(function(result) {
					results[lang] = result;
				}));
			}			
		}
	});	
	
	$.when.apply(null, deffereds).done(function() {
		deferred.resolve(results);
    });
	
	return deferred;
};

nameUtil._checkName = function(name){
	 var deferred = $.Deferred();
	 $.ajax({
        url : '/rest/indicator/indicator-layer/check-name?name=' + name,
        type : 'GET', 
        headers : {
			'Accept' : 'application/json',
			'Content-Type' : 'application/json'
		},
		success: function(data){
			deferred.resolve(data);
		},
		error:function(data){
			deferred.resolve(data.responseJSON);
		}
   }); 
   return deferred;
};

module.exports = nameUtil;