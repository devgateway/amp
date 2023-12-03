#!/usr/bin/python
"""Custom processor for FM Exports. Detects enabled AF fields occurrences. Result is presorted by Occurrences - desc, FM Path - asc. Usage:
fmprocessor.py fmexport1.xml fmexport2.xml ...
"""

import sys
import getopt
import os

import StringIO
from collections import OrderedDict

import lxml.etree as ET

xsl = '''<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
         <xsl:output omit-xml-declaration="yes" />
        <xsl:template match="/" >
          <xsl:for-each select="visibilityTemplates/template//ampModule[starts-with(@name,'/Activity Form/') and (@visible='true') and (count(ancestor::ampModule)= count(ancestor::ampModule[@visible='true']))]">
            <xsl:sort select="@name"/>
            <xsl:value-of select="@name"/>
            <xsl:text>&#xa;</xsl:text>
          </xsl:for-each>
        </xsl:template>
        </xsl:stylesheet>'''
result = "result.csv"

def main():
  # get file names or show help
  args = read_args()

  # <fm entry, count> map
  fmentries = {}
  # <fm_entry, [file1, file2, ...]> map
  fm_per_country = {}
  for file_name in args:
    # read only enabled fields (all ancestores must be enabled as well to be considered enabled in AF)
    fin = convert(file_name)
    # process each line: count occurrences, remember countries that use it and adjust for CSV
    for line in fin:
      line = "\"" + line.strip() + "\""
      if line.__len__() > 2:
        count = fmentries.setdefault(line, 0)
        count += 1
        fmentries[line] = count
        countries = fm_per_country.setdefault(line, [])
        countries.append(file_name)
  fmentries = OrderedDict(sorted(fmentries.items(), key=lambda(k,v): (-v,k)))
  write_result(fmentries, fm_per_country, args)

def write_result( fmentries, fm_per_country, args ):
  fout = open_file(result, "w")
  header = "FM Path,Occurrences"
  for file_name in args:
    header += "," + file_name
  fout.writelines(header + os.linesep)
  for fmentry, count in fmentries.iteritems():
    entry_desc = fmentry + "," + `count`
    countries = fm_per_country[fmentry]
    for file_name in args:
      if countries.__contains__(file_name):
        entry_desc += ",true"
      else:
        entry_desc += ",false"
    fout.writelines(entry_desc + os.linesep)
  fout.close()

def read_args():
  try:
    opts, args = getopt.getopt(sys.argv[1:], "h", ["help"])
  except getopt.error, msg:
    print msg
    print "for help use --help"
    sys.exit(2)
  for o, a in opts:
    if o in ("-h", "--help"):
      print __doc__
      sys.exit(0)
  return args

def convert( file_name ):
  fin = open_file(file_name, "r")
  input_str = fin.read(-1)
  fin.close()
  input_str = input_str.replace("xmlns=\"http://dgfoundation.org/amp/visibility/feed/fm/schema.xml\"", "")
  # workaround AF root FM bug: <ampModule name="/Activity Form" visible="false">
  input_str = input_str.replace("<ampModule name=\"/Activity Form\" visible=\"false\">", "<ampModule name=\"/Activity Form\" visible=\"true\">")
  dom = ET.fromstring(input_str)
  xslt = ET.fromstring(xsl)
  transform = ET.XSLT(xslt)
  newdom = transform(dom)
  result = str(newdom).splitlines()
  return result

def open_file( file_name, mode ):
  try:
     file = open(file_name, mode)
  except IOError, msg:
    print msg
    print "There was an error writing to ", file_name
    sys.exit()
  return file

if __name__ == "__main__":
    main()
