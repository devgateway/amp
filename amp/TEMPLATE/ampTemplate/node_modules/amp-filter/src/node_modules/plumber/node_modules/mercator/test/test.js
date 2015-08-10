var chai = require('chai');
var should = chai.should();

var fs = require('fs');

var SourceMapConsumer = require('source-map').SourceMapConsumer;


var mercator = require('..');


function readFile(path) {
    return fs.readFileSync(path).toString();
}

function readJsonFile(path) {
    return JSON.parse(readFile(path));
}


describe('SourceMap', function() {
    var SourceMap = mercator.SourceMap;

    it('should be an object', function() {
        SourceMap.should.be.an('object');
    });


    describe('#forSource', function() {
        var path = 'test/fixtures/source.js';
        var source = readFile(path);

        it('should be a function', function() {
            SourceMap.forSource.should.be.a('function');
        });

        it('should throw an error if called without data', function() {
            (function() {
                SourceMap.forSource();
            }).should.throw('Missing data in SourceMap.forSource');
        });

        it('should throw an error if called without source path', function() {
            (function() {
                SourceMap.forSource(source);
            }).should.throw('Missing sourcePath in SourceMap.forSource');
        });

        it('should return a source map object of version 3', function() {
            var map = SourceMap.forSource(source, path);

            // Can't do instanceof as the prototype is not exposed
            map.should.be.an('object');
            map.version.should.equal(3);
        });

        it('should return a source map referencing the original filename as file', function() {
            var map = SourceMap.forSource(source, path);
            map.file.should.equal('source.js');
        });

        it('should return a source map referencing the path in sources', function() {
            var map = SourceMap.forSource(source, path);
            map.sources.should.deep.equal([path]);
        });

        it('should return a source map that includes the source contents', function() {
            var map = SourceMap.forSource(source, path);
            map.sourcesContent.should.deep.equal([source]);
        });

        it('should return a source map mapping the file onto itself', function() {
            var map = SourceMap.forSource(source, path);
            var consumer = new SourceMapConsumer(map);

            var numLines = numLines = source.split('\n').length;
            for (var line = 1; line <= numLines; line++) {
                consumer.originalPositionFor({line: line, column: 0}).should.deep.equal({
                    source: path,
                    line: line,
                    column: 0,
                    name: null
                });
            }
        });
    });


    describe('#fromMapObject', function() {

        it('should be a function', function() {
            SourceMap.fromMapObject.should.be.a('function');
        });

        it('should return a source map object identical to the input', function() {
            var mapObject = {
                version: 3,
                file: 'source.js',
                mappings: 'AAAA;AACA;AACA;AACA;AACA;AACA;AACA;AACA;AACA;AACA;AACA;AACA',
                sources: [ 'test/fixtures/source.js' ],
                sourcesContent: [ 'var x = 3;\nvar y = x;\n\n/* a comment */\n\nfunction inc(x) {\n    return x + 1;\n}\n\nvar z = inc(x);\n\n' ],
                names: ['x']
            };
            var map = SourceMap.fromMapObject(mapObject);

            map.should.be.an('object');
            ['version', 'file', 'mappings', 'sources',
             'sourcesContent', 'names'].forEach(function(key) {
                 map[key].should.deep.equal(mapObject[key]);
             });
        });

    });


    describe('#fromMapData', function() {

        it('should be a function', function() {
            SourceMap.fromMapData.should.be.a('function');
        });

        it('should return a source map object identical to the input', function() {
            var mapObject = {
                version: 3,
                file: 'source.js',
                mappings: 'AAAA;AACA;AACA;AACA;AACA;AACA;AACA;AACA;AACA;AACA;AACA;AACA',
                sources: [ 'test/fixtures/source.js' ],
                sourcesContent: [ 'var x = 3;\nvar y = x;\n\n/* a comment */\n\nfunction inc(x) {\n    return x + 1;\n}\n\nvar z = inc(x);\n\n' ],
                names: ['x']
            };
            var map = SourceMap.fromMapData(JSON.stringify(mapObject));

            map.should.be.an('object');
            ['version', 'file', 'mappings', 'sources',
             'sourcesContent', 'names'].forEach(function(key) {
                 map[key].should.deep.equal(mapObject[key]);
             });
        });

    });


    describe('prototype', function() {
        var path = 'test/fixtures/source.js.map';
        var sourceMapObj = readJsonFile(path);
        var sourceMap;

        beforeEach(function() {
            sourceMap = SourceMap.fromMapObject(sourceMapObj);
        });

        describe('#toString', function() {
            it('should return the original data', function() {
                var flattenedOriginalMap = JSON.stringify(sourceMapObj);
                sourceMap.toString().should.equal(flattenedOriginalMap);
            });
        });

        describe('#mapSourcePaths', function() {
            var mappedMap;

            beforeEach(function() {
                mappedMap = sourceMap.mapSourcePaths(function(path) {
                    return path + '-test';
                });
            });

            it('should map the source paths', function() {
                mappedMap.sources.should.deep.equal(['test/fixtures/source.js-test']);
            });

            it('should retain all other fields', function() {
                ['version', 'file', 'mappings',
                 'sourcesContent', 'names'].forEach(function(key) {
                     mappedMap[key].should.deep.equal(sourceMap[key]);
                 });
            });
        });


        describe('#rebaseSourcePaths', function() {
            var rebasedMap;

            beforeEach(function() {
                rebasedMap = sourceMap.rebaseSourcePaths('test');
            });

            it('should map the source paths', function() {
                rebasedMap.sources.should.deep.equal(['fixtures/source.js']);
            });

            it('should retain all other fields', function() {
                ['version', 'file', 'mappings',
                 'sourcesContent', 'names'].forEach(function(key) {
                     rebasedMap[key].should.deep.equal(sourceMap[key]);
                 });
            });
        });


        describe('#withFile', function() {
            var updatedMap;

            beforeEach(function() {
                updatedMap = sourceMap.withFile('other-name.js');
            });

            it('should map the source paths', function() {
                updatedMap.file.should.equal('other-name.js');
            });

            it('should retain all other fields', function() {
                ['version', 'mappings', 'sources',
                 'sourcesContent', 'names'].forEach(function(key) {
                     updatedMap[key].should.deep.equal(sourceMap[key]);
                 });
            });
        });


        describe('#withSourceContent', function() {

            describe('when source content not already present', function() {
                var updatedMap;

                beforeEach(function() {
                    var sourceMapNoContent = sourceMap.copy({sourcesContent: null});

                    updatedMap = sourceMap.withSourceContent("test/fixtures/source.js", 'var a = 2;');
                });

                it('should return a new source map with updated source content', function() {
                    updatedMap.sourcesContent.should.deep.equal(
                        ['var a = 2;']
                    );
                });

                it('should keep all other fields', function() {
                    ['file', 'version', 'mappings',
                     'sources', 'names'].forEach(function(key) {
                         updatedMap[key].should.deep.equal(sourceMap[key]);
                     });
                });
            });

            describe('when source content already present', function() {
                var updatedMap;

                beforeEach(function() {
                    updatedMap = sourceMap.withSourceContent("test/fixtures/source.js", 'var a = 2;');
                });

                it('should update the source content', function() {
                    updatedMap.sourcesContent.should.deep.equal(
                        ['var a = 2;']
                    );
                });

                it('should keep all other fields', function() {
                    ['file', 'version', 'mappings',
                     'sources', 'names'].forEach(function(key) {
                         updatedMap[key].should.deep.equal(sourceMap[key]);
                     });
                });

            });

            describe('when content for source not in the list', function() {
                it('should throw an error', function() {
                    (function() {
                        sourceMap.withSourceContent("path/to/no-such-file.js", 'var a = 2;');
                    }).should.throw("path/to/no-such-file.js not found in sources of source map");
                });

            });

        });


        describe('#withoutSourceContent', function() {
            var updatedMap;

            beforeEach(function() {
                updatedMap = sourceMap.withoutSourcesContent();
            });

            it('should return a new source map with no source content', function() {
                should.not.exist(updatedMap.sourcesContent);
            });

            it('should keep all other fields', function() {
                ['file', 'version', 'mappings',
                 'sources', 'names'].forEach(function(key) {
                     updatedMap[key].should.deep.equal(sourceMap[key]);
                 });
            });
        });


        describe('#apply', function() {
            var concatPath = 'test/fixtures/concatenated.js.map';
            var concatSourceMapObj = readJsonFile(concatPath);
            var concatSourceMap;
            var minPath = 'test/fixtures/concatenated.min.js.map';
            var minSourceMapObj = readJsonFile(minPath);
            var minSourceMap;

            beforeEach(function() {
                concatSourceMap = SourceMap.fromMapObject(concatSourceMapObj);
                minSourceMap = SourceMap.fromMapObject(minSourceMapObj);
            });

            it('should return the composition of both mappings', function() {
                var appliedMap = concatSourceMap.apply(minSourceMap);
                var consumer = new SourceMapConsumer(appliedMap);

                /*
                 function inc(n){return n+1}var x=3,y=x,z=inc(x);[...]
                 ^
                 */
                consumer.originalPositionFor({line: 1, column: 0}).should.deep.equal({
                    source: 'test/fixtures/source.js',
                    line: 6,
                    column: 0,
                    name: null
                });
                /*
                 function inc(n){return n+1}var x=3,y=x,z=inc(x);[...]
                 ^
                 */
                consumer.originalPositionFor({line: 1, column: 16}).should.deep.equal({
                    source: 'test/fixtures/source.js',
                    line: 7,
                    column: 0,
                    name: null
                });
                /*
                 function inc(n){return n+1}var x=3,y=x,z=inc(x);[...]
                 ^
                 */
                consumer.originalPositionFor({line: 1, column: 27}).should.deep.equal({
                    source: 'test/fixtures/source.js',
                    line: 1,
                    column: 0,
                    name: null
                });
                /*
                 function inc(n){return n+1}var x=3,y=x,z=inc(x);[...]
                 ^
                 */
                consumer.originalPositionFor({line: 1, column: 31}).should.deep.equal({
                    source: 'test/fixtures/source.js',
                    line: 1,
                    column: 0,
                    name: 'x'
                });
                /*
                 function inc(n){return n+1}var x=3,y=x,z=inc(x);[...]
                 ^
                 */
                consumer.originalPositionFor({line: 1, column: 35}).should.deep.equal({
                    source: 'test/fixtures/source.js',
                    line: 2,
                    column: 0,
                    name: 'y'
                });
                /*
                 function inc(n){return n+1}var x=3,y=x,z=inc(x);[...]
                 ^
                 */
                consumer.originalPositionFor({line: 1, column: 39}).should.deep.equal({
                    source: 'test/fixtures/source.js',
                    line: 10,
                    column: 0,
                    name: 'z'
                });
                /*
                 [...]var x=3,y=x,z=inc(x);define([],function(){function n(n){[...]
                 ^
                 */
                consumer.originalPositionFor({line: 1, column: 48}).should.deep.equal({
                    source: 'test/fixtures/library.js',
                    line: 2,
                    column: 0,
                    name: 'define'
                });
                /*
                 [...]var x=3,y=x,z=inc(x);define([],function(){function n(n){[...]
                 ^
                 */
                consumer.originalPositionFor({line: 1, column: 58}).should.deep.equal({
                    source: 'test/fixtures/library.js',
                    line: 2,
                    column: 0, // Note: only line info...
                    name: null
                });
                /*
                 [...]define([],function(){function n(n){return n+r}var r=1;return n});
                 ^
                 */
                consumer.originalPositionFor({line: 1, column: 69}).should.deep.equal({
                    source: 'test/fixtures/library.js',
                    line: 5,
                    column: 0,
                    name: null
                });
                /*
                 [...]define([],function(){function n(n){return n+r}var r=1;return n});
                 ^
                 */
                consumer.originalPositionFor({line: 1, column: 83}).should.deep.equal({
                    source: 'test/fixtures/library.js',
                    line: 6,
                    column: 0,
                    name: null
                });
                /*
                 [...]define([],function(){function n(n){return n+r}var r=1;return n});
                 ^
                 */
                consumer.originalPositionFor({line: 1, column: 94}).should.deep.equal({
                    source: 'test/fixtures/library.js',
                    line: 3,
                    column: 0,
                    name: null
                });
                /*
                 [...]define([],function(){function n(n){return n+r}var r=1;return n});
                 ^
                 */
                consumer.originalPositionFor({line: 1, column: 98}).should.deep.equal({
                    source: 'test/fixtures/library.js',
                    line: 3,
                    column: 0,
                    name: 'number'
                });
                /*
                 [...]define([],function(){function n(n){return n+r}var r=1;return n});
                 ^
                 */
                consumer.originalPositionFor({line: 1, column: 102}).should.deep.equal({
                    source: 'test/fixtures/library.js',
                    line: 9,
                    column: 0,
                    name: null
                });
            });

            it('should contain the sources and sourcesContent from the first map', function() {
                var appliedMap = concatSourceMap.apply(minSourceMap);
                appliedMap.sources.should.deep.equal(concatSourceMap.sources);
                appliedMap.sourcesContent.should.deep.equal(concatSourceMap.sourcesContent);
            });
        });



        describe('#append', function() {
            var sourceJsPath = 'test/fixtures/source.js';
            var sourceJs = readFile(sourceJsPath);
            var sourceJsNumLines = sourceJs.split('\n').length;

            var sourcePath = 'test/fixtures/source.js.map';
            var sourceSourceMapObj = readJsonFile(sourcePath);
            var sourceSourceMap;
            var libraryPath = 'test/fixtures/library.js.map';
            var librarySourceMapObj = readJsonFile(libraryPath);
            var librarySourceMap;

            var appendedMap;

            beforeEach(function() {
                sourceSourceMap = SourceMap.fromMapObject(sourceSourceMapObj);
                librarySourceMap = SourceMap.fromMapObject(librarySourceMapObj);
                appendedMap = sourceSourceMap.append(librarySourceMap, sourceJsNumLines, 'appended.js');
            });

            it('should return a source map containing all original sources', function() {
                appendedMap.sources.should.deep.equal([
                    'test/fixtures/source.js',
                    'library.js'
                ]);
            });

            it('should return a source map containing all original source contents', function() {
                appendedMap.sourcesContent.should.deep.equal([
                    sourceSourceMap.sourcesContent[0],
                    librarySourceMap.sourcesContent[0]
                ]);
            });

            it('should return the same mappings for the first map', function() {
                var consumer = new SourceMapConsumer(appendedMap);
                consumer.originalPositionFor({line: 1, column: 0}).should.deep.equal({
                    source: 'test/fixtures/source.js',
                    line: 1,
                    column: 0,
                    name: null
                });
                consumer.originalPositionFor({line: sourceJsNumLines, column: 0}).should.deep.equal({
                    source: 'test/fixtures/source.js',
                    line: sourceJsNumLines,
                    column: 0,
                    name: null
                });
            });

            it('should return offsetted mappings for the second map', function() {
                var consumer = new SourceMapConsumer(appendedMap);
                consumer.originalPositionFor({line: sourceJsNumLines + 1, column: 0}).should.deep.equal({
                    source: 'library.js',
                    line: 1,
                    column: 0,
                    name: null
                });
                consumer.originalPositionFor({line: sourceJsNumLines + 2, column: 0}).should.deep.equal({
                    source: 'library.js',
                    line: 2,
                    column: 0,
                    name: null
                });
            });

        });

        // TODO: properties
    });
});


describe('stripSourceMappingComment', function() {
    var stripSourceMappingComment = mercator.stripSourceMappingComment;

    it('should strip source mapping comment from JS', function() {
        var source = 'var x = 3;\nvar yz = 2;\n';
        var sourceWithComment = source + '//# sourceMappingURL=some-file.js\n';
        stripSourceMappingComment(sourceWithComment).should.equal(source);
    });

    it('should strip source mapping comment from CSS', function() {
        var source = '.x {\n  border: 1px solid red;\n}\n';
        var sourceWithComment = source + '/*# sourceMappingURL=some-file.css */';
        stripSourceMappingComment(sourceWithComment).should.equal(source);
    });

    it('should do nothing if no source mapping comment is present', function() {
        var source = '.x {\n  border: 1px solid red;\n}';
        stripSourceMappingComment(source).should.equal(source);
    });
});



describe('readSourceMappingComment', function() {
    var readSourceMappingComment = mercator.readSourceMappingComment;

    it('should return undefined when no source mapping comment', function() {
        var source = 'var x = 3;\nvar yz = 2;\n';
        should.not.exist(readSourceMappingComment(source));
    });

    it('should return undefined when source mapping comment not at the end of the content', function() {
        var source = 'var x = 3;\n//# sourceMappingURL=some-file.js\nvar yz = 2;\n';
        should.not.exist(readSourceMappingComment(source));
    });

    it('should return undefined when source mapping comment at the end of the content but not on a new line', function() {
        var source = 'var x = 3;\nvar yz = 2;//# sourceMappingURL=some-file.js';
        should.not.exist(readSourceMappingComment(source));
    });

    it('should return the source map path when source mapping comment at the end of JS content', function() {
        var source = 'var x = 3;\n\nvar yz = 2;\n//# sourceMappingURL=some-file.js \n\n';
        readSourceMappingComment(source).should.equal('some-file.js');
    });

    it('should return the source map path when source mapping comment at the end of CSS content', function() {
        var source = '.rule { border: 0 }\n/*# sourceMappingURL=concatenated.less.map */\n';
        readSourceMappingComment(source).should.equal('concatenated.less.map');
    });

});


describe('generateJsSourceMappingComment', function() {
    var generateJsSourceMappingComment = mercator.generateJsSourceMappingComment;

    it('should generate the correct comment', function() {
        var comment = generateJsSourceMappingComment('some-file.js');
        comment.should.equal('//# sourceMappingURL=some-file.js');
    });

    it('should throw an exception if no filename is provided', function() {
        (function() {
            generateJsSourceMappingComment();
        }).should.throw('Missing source map filename when calling generateJsSourceMappingComment');
    });
});

describe('generateCssSourceMappingComment', function() {
    var generateCssSourceMappingComment = mercator.generateCssSourceMappingComment;

    it('should generate the correct comment', function() {
        var comment = generateCssSourceMappingComment('some-file.css');
        comment.should.equal('/*# sourceMappingURL=some-file.css */');
    });

    it('should throw an exception if no filename is provided', function() {
        (function() {
            generateCssSourceMappingComment();
        }).should.throw('Missing source map filename when calling generateCssSourceMappingComment');
    });
});
