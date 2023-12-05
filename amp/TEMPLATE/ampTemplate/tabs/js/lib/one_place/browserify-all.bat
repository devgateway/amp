@echo on
browserify ..\jqgrid-4.6.0\js2\jquery.jqGrid.src.js ..\jqgrid-4.6.0\js2\i18n\grid.locale-en.js | uglifyjs > jqgrid-all.js