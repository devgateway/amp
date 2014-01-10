
This module provides an Excel parser for feeds.module.

Requirements
============
- Feeds 1.x
  http://drupal.org/project/feeds
- Libraries 1.x
  http://drupal.org/project/libraries
- phpExcelReader
  https://github.com/derhasi/phpExcelReader/zipball/interim2
  (originally on http://sourceforge.net/projects/phpexcelreader/files/Spreadsheet_Excel_Reader/Interim%20update/phpExcelReader.zip/download
  but for really being able to use it, it has to be modified, so lives on github for that)

The token module is recommended, so token replacement patterns can be viewed for
help on the feeds mapping settings page.


Installation
============
- Install Feeds, Feeds Admin UI and its dependecnies
- Copy this module to your modules directory
- Place the phpExcelReader in sites/all/libaries, so reader.php is located at
  sites/all/libaries/phpExcelReader/Excel/reader.php
- Enable feeds_excel

Now it can be used as Parser in Feeds.

Problems with large files
=========================
There's a bottle neck in batch processing of feeds.module at the moment. This
can cause (large) excel files not getting completely imported. For more
information on this issue visit http://drupal.org/node/712304.

Parser Settings
========
Mapping mode:
  Currently there are two modes:
  * "rows" runs through iterative range row-by-row, so each row is a new dataset
  * "columns" runs through iterative range column-by-column, so each column is a new dataset

Fixed Range:
  a region that holds absolute variables, that can be fetch by [sheet-cell-R-C],
  where R represents the row number and C the column number

Iterative Range:
  This is the region where datasets are processed from, either by row or by
  column.

Header Range:
  This option is currently disabled.

Keys for mapping sources
========================
Keys are processed through token.module, so see the "Legend sources" fieldset
on the mapping settings page.

phpExcelReader
==============
The module needs phpExcelReader placed into libraries, so reader.php is located
at sites/all/modules/custom/feeds_excel/libraries/phpExcelReader/Excel/reader.php

The library was originally placed at http://sourceforge.net/projects/phpexcelreader/
but needed some modification to work. So I forked it to github:
https://github.com/derhasi/phpExcelReader

There you can download the archive at https://github.com/derhasi/phpExcelReader/zipball/interim2
