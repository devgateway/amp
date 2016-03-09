var SaikuQueryTemplate = {
  "queryModel": {}, 
  "queryType": "OLAP",
  "type": "QUERYMODEL"
};

var SaikuQueryHelper = function(query) {
	this.query = query;
};


SaikuQueryHelper.prototype.model = function() {
	return this.query.model;
};