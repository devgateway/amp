var util = require('./lib/util.js');

module.exports = {
  SourceMap: require('./lib/sourcemap.js'),
  readSourceMappingComment:        util.readSourceMappingComment,
  stripSourceMappingComment:       util.stripSourceMappingComment,
  generateJsSourceMappingComment:  util.generateJsSourceMappingComment,
  generateCssSourceMappingComment: util.generateCssSourceMappingComment
};
