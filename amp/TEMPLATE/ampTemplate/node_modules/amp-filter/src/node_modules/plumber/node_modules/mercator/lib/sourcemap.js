var path = require('path');

var SourceMapGenerator = require('source-map').SourceMapGenerator;
var SourceMapConsumer  = require('source-map').SourceMapConsumer;


// List of properties in Source Map format
var properties = [
    'version', 'file', 'mappings',
    'sources', 'sourcesContent', 'names'
];

function SourceMap(params) {
    // FIXME: check compulsory ones?
    properties.forEach(function(prop) {
        this[prop] = params[prop];
    }.bind(this));
}

SourceMap.prototype.copy = function(override) {
    var params = {};
    properties.forEach(function(prop) {
        params[prop] = override.hasOwnProperty(prop) ? override[prop] : this[prop];
    }.bind(this));
    return new SourceMap(params);
};

/**
 * Apply a new source map onto the current one and generate a composed
 * mapping from the original file.
 *
 * @param {SourceMap|Object} nextSourceMap Source map for a new transformation applied after the current source map.
 * @return {SourceMap} A new source map composing both the current and the next.
 */
SourceMap.prototype.apply = function(nextSourceMap) {
    // We assume the nextSourceMap maps a single source, which
    // corresponds to the output of the current source map
    var nextSources = nextSourceMap.sources;
    if (nextSources.length !== 1) {
        throw new Error('Cannot apply a source map that maps multiple sources');
    }

    var currentMap = new SourceMapConsumer(this);
    var nextMap    = new SourceMapConsumer(nextSourceMap);

    var generator = SourceMapGenerator.fromSourceMap(nextMap);
    generator.applySourceMap(currentMap, nextSources[0]);
    return fromMapGenerator(generator);
};

SourceMap.prototype.append = function(nextSourceMap, offset, resultFilename) {
    var generator = new SourceMapGenerator({
        file: resultFilename
    });

    // Append self and next (with offset)
    var currentMap = new SourceMapConsumer(this);
    var nextMap    = new SourceMapConsumer(nextSourceMap);

    addSourcesContent(currentMap);
    addSourcesContent(nextMap);

    addMappingWithOffset(currentMap, 0);
    addMappingWithOffset(nextMap, offset);

    return fromMapGenerator(generator);


    // Register source contents from map in generator
    function addSourcesContent(map) {
        (map.sourcesContent || []).forEach(function(content, i) {
            generator.setSourceContent(map.sources[i], content);
        });
    }

    // Register all mappings from map in generator, adding the given
    // line offset
    function addMappingWithOffset(map, offset) {
        map.eachMapping(function(mapping) {
            generator.addMapping({
                generated: {
                    line: mapping.generatedLine + offset,
                    column: mapping.generatedColumn
                },
                original: {
                    line: mapping.originalLine,
                    column: mapping.originalColumn
                },
                source: mapping.source,
                name: mapping.name
            });
        });
    }
};

// SourceMap.prototype.concat = function(sourceMapsAndOffsets, resultFilename) {

//     var lineOffset = 0;
//     var generator = new SourceMapGenerator({
//         file: resultFilename
//     });

//     // Append each resource
//     sourceMapsAndOffsets.forEach(function(smao) {
//         var map = new SourceMapConsumer(smao.sourceMap);

//         // Rebase the mapping by the lineOffset
//         map.eachMapping(function(mapping) {
//             generator.addMapping({
//                 generated: {
//                     line: mapping.generatedLine + lineOffset,
//                     column: mapping.generatedColumn
//                 },
//                 original: {
//                     line: mapping.originalLine,
//                     column: mapping.originalColumn
//                 },
//                 source: mapping.source
//             });
//         });

//         lineOffset += smao.offset;
//     });

//     return fromMapGenerator(generator);
// };


SourceMap.prototype.mapSourcePaths = function(mapper) {
    return this.copy({
        sources: this.sources.map(function(sourcePath) {
            return mapper(sourcePath);
        })
    });
};

SourceMap.prototype.rebaseSourcePaths = function(targetDir) {
    return this.mapSourcePaths(function(sourcePath) {
        return path.relative(targetDir, sourcePath);
    });
};

SourceMap.prototype.withFile = function(file) {
    return this.copy({
        file: file
    });
};

/**
 * Record (or overwrite) the source content for the given source path.
 *
 * @param {String} sourcePath Path of the source to record content for.
 * @param {String} content Content of the file at sourcePath
 * @return {SourceMap} A new source map with the source content.
 */
SourceMap.prototype.withSourceContent = function(sourcePath, content) {
    // sourcePath must match an element in sources list
    var sourceIndex = this.sources.indexOf(sourcePath);
    if (sourceIndex === -1) {
        throw new Error(sourcePath + ' not found in sources of source map');
    }

    // Clone the current sourcesContent and inject the given content
    var sourcesContent = (this.sourcesContent || []).concat();
    sourcesContent[sourceIndex] = content;
    return this.copy({
        sourcesContent: sourcesContent
    });

/* Note: The following code is neater as it relies on the
   source-map library to set the sourcesContent, but the consumer
   and generator phases are extremely slow so we shortcut it with
   the code above.

    // sourcePath must match an element in sources list
    if (this.sources.indexOf(sourcePath) === -1) {
        throw new Error(sourcePath + ' not found in sources of source map');
    }

    var currentNames = this.names;
    var currentMap = new SourceMapConsumer(this);
    var generator = new SourceMapGenerator.fromSourceMap(currentMap);
    generator.setSourceContent(sourcePath, content);
    // Note: for some reason, this resets the names, but we likely
    // want to keep those, so we add them back.
    return fromMapGenerator(generator).copy({names: currentNames});
*/
};

/**
 * Return a new SourceMap without any of the sourcesContent.
 *
 * @return {SourceMap} A new source map without the source content.
 */
SourceMap.prototype.withoutSourcesContent = function() {
    return this.copy({
        sourcesContent: undefined
    });
};

SourceMap.prototype.toString = function() {
    return JSON.stringify(this);
};



function fromMapObject(sourceMapObject) {
    return new SourceMap(sourceMapObject);
}

function fromMapGenerator(sourceMapGenerator) {
    return fromMapObject(sourceMapGenerator.toJSON());
}

function fromMapData(sourceMapData) {
    return fromMapObject(JSON.parse(sourceMapData));
}

function forSource(data, sourcePath) {
    if (! data) {
        throw new Error('Missing data in SourceMap.forSource');
    }

    if (! sourcePath) {
        throw new Error('Missing sourcePath in SourceMap.forSource');
    }

    var generator = new SourceMapGenerator({
        // FIXME: or just filename?
        file: path.basename(sourcePath)
    });

    generator.setSourceContent(sourcePath, data);

    data.split('\n').forEach(function(l, i) {
        generator.addMapping({
            generated: { line: i + 1, column: null },
            original:  { line: i + 1, column: null },
            source: sourcePath
        });
    });

    return fromMapObject(generator.toJSON());
}


module.exports = {
    fromMapData: fromMapData,
    fromMapObject: fromMapObject,
    forSource: forSource
};
