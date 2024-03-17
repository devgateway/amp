var SaikuQueryTemplate = {
  "queryModel": {}, 
  "queryType": "",
  "type": ""
};

var SaikuQueryHelper = function(query) {
	Saiku.logger.log("SessionQueryHelper");
	this.query = query;
};


SaikuQueryHelper.prototype.model = function() {
	return this.query.model;
};