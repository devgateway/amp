var SaikuQueryTemplate = {
  "queryModel": {}, 
  "queryType": "",
  "type": ""
};

var SaikuQueryHelper = function(query) {
	this.query = query;
};


SaikuQueryHelper.prototype.model = function() {
	return this.query.model;
};