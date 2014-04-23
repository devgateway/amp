<?php

/**
 * Simple excel generating from PHP5
 *
 * @package Utilities
 * @license http://www.opensource.org/licenses/mit-license.php
 * @author Oliver Schwarz <oliver.schwarz@gmail.com>
 * @version 1.0
 */

/**
 * Generating excel documents on-the-fly from PHP5
 * 
 * Uses the excel XML-specification to generate a native
 * XML document, readable/processable by excel.
 * 
 * @package Utilities
 * @subpackage Excel
 * @author Oliver Schwarz <oliver.schwarz@vaicon.de>
 * @version 1.1
 * 
 * @todo Issue #4: Internet Explorer 7 does not work well with the given header
 * @todo Add option to give out first line as header (bold text)
 * @todo Add option to give out last line as footer (bold text)
 * @todo Add option to write to file
 */
class Excel_XML
{

	/**
	 * Header (of document)
	 * @var string
	 */
        private $header = "<?xml version=\"1.0\" encoding=\"%s\"?\>\n<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\" xmlns:c=\"urn:schemas-microsoft-com:office:component:spreadsheet\" xmlns:html=\"http://www.w3.org/TR/REC-html40\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\" xmlns:x2=\"http://schemas.microsoft.com/office/excel/2003/xml\" xmlns:x=\"urn:schemas-microsoft-com:office:excel\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n <OfficeDocumentSettings xmlns=\"urn:schemas-microsoft-com:office:office\"> <Colors> <Color> <Index>3</Index> <RGB>#c0c0c0</RGB> </Color> <Color> <Index>4</Index> <RGB>#ff0000</RGB> </Color> </Colors> </OfficeDocumentSettings> <ExcelWorkbook xmlns=\"urn:schemas-microsoft-com:office:excel\"> <WindowHeight>9000</WindowHeight> <WindowWidth>13860</WindowWidth> <WindowTopX>240</WindowTopX> <WindowTopY>75</WindowTopY> <ProtectStructure>False</ProtectStructure> <ProtectWindows>False</ProtectWindows> </ExcelWorkbook> <Styles> <Style ss:ID=\"Default\" ss:Name=\"Default\"/> <Style ss:ID=\"Result\" ss:Name=\"Result\"> <Font ss:Bold=\"1\" ss:Italic=\"1\" ss:Underline=\"Single\"/> </Style> <Style ss:ID=\"Result2\" ss:Name=\"Result2\"> <Font ss:Bold=\"1\" ss:Italic=\"1\" ss:Underline=\"Single\"/> <NumberFormat ss:Format=\"Currency\"/> </Style> <Style ss:ID=\"Heading\" ss:Name=\"Heading\"> <Font ss:Bold=\"1\" ss:Italic=\"1\" ss:Size=\"16\"/> </Style> <Style ss:ID=\"Heading1\" ss:Name=\"Heading1\"> <Font ss:Bold=\"1\" ss:Italic=\"1\" ss:Size=\"16\"/> </Style><Style ss:ID=\"co1\"/> <Style ss:ID=\"ta1\"/> </Styles>";

        /**
         * Footer (of document)
         * @var string
         */
        private $footer = "</Workbook>";

        /**
         * Lines to output in the excel document
         * @var array
         */
        private $lines = array();

        /**
         * Used encoding
         * @var string
         */
        private $sEncoding;
        
        /**
         * Convert variable types
         * @var boolean
         */
        private $bConvertTypes;
        
        /**
         * Worksheet title
         * @var string
         */
        private $sWorksheetTitle;

        /**
         * Constructor
         * 
         * The constructor allows the setting of some additional
         * parameters so that the library may be configured to
         * one's needs.
         * 
         * On converting types:
         * When set to true, the library tries to identify the type of
         * the variable value and set the field specification for Excel
         * accordingly. Be careful with article numbers or postcodes
         * starting with a '0' (zero)!
         * 
         * @param string $sEncoding Encoding to be used (defaults to UTF-8)
         * @param boolean $bConvertTypes Convert variables to field specification
         * @param string $sWorksheetTitle Title for the worksheet
         */
        public function __construct($sEncoding = 'UTF-8', $bConvertTypes = false, $sWorksheetTitle = 'Table1')
        {
                $this->bConvertTypes = $bConvertTypes;
        	$this->setEncoding($sEncoding);
        	$this->setWorksheetTitle($sWorksheetTitle);
        }
        
        /**
         * Set encoding
         * @param string Encoding type to set
         */
        public function setEncoding($sEncoding)
        {
        	$this->sEncoding = $sEncoding;
        }

        /**
         * Set worksheet title
         * 
         * Strips out not allowed characters and trims the
         * title to a maximum length of 31.
         * 
         * @param string $title Title for worksheet
         */
        public function setWorksheetTitle ($title)
        {
                $title = preg_replace ("/[\\\|:|\/|\?|\*|\[|\]]/", "", $title);
                $title = substr ($title, 0, 31);
                $this->sWorksheetTitle = $title;
        }

        /**
         * Add row
         * 
         * Adds a single row to the document. If set to true, self::bConvertTypes
         * checks the type of variable and returns the specific field settings
         * for the cell.
         * 
         * @param array $array One-dimensional array with row content
         */
        private function addRow ($array)
        {
        	$cells = "";
                foreach ($array as $k => $v):
                        $type = 'String';
                        if ($this->bConvertTypes === true && is_numeric($v)):
                                $type = 'Number';
                        endif;
                        $cells .= "<Cell><Data ss:Type=\"$type\">" . $v . "</Data></Cell>\n"; 
                endforeach;
                $this->lines[] = "<Row>\n" . $cells . "</Row>\n";
        }
        /**
         * Add 2d array as row
         * 
         * Adds a single row to the document. If set to true, self::bConvertTypes
         * checks the type of variable and returns the specific field settings
         * for the cell.
         * 
         * @param array $array One-dimensional array with row content
         * @param int $max_inner_elements max subrows in row
         */
        private function add2dRow ($array, $max_inner_elements)
        {
          $cells = "";
                $array = array_values($array);
                for ($i = 0; $i < $max_inner_elements; $i++):
                        $row =  "<Row>\n";
                        $cells = '';
                        for ($j = 0; $j < count($array); $j++):
                                if (is_array($array[$j])):
                                        $type = 'String';
                                        if (isset($array[$j][$i]) && $this->bConvertTypes === true && is_numeric($array[$j][$i])):
                                                $type = 'Number';
                                        endif;
                                        if (isset($array[$j][$i]) && !isset($array[$j+1][$i]) && $max_inner_elements - count($array[$j]) > 0):
                                                 $cells .= "<Cell ss:MergeDown=\"".($max_inner_elements - count($array[$j]))."\" ><Data ss:Type=\"$type\">" . $array[$j][$i] . "</Data></Cell>\n";        
                                        elseif (isset($array[$j][$i])):
                                                $cells .= "<Cell ". ($i > 0 ? 'ss:Index="'.($j+1).'" ' : '')." ><Data ss:Type=\"$type\">" . $array[$j][$i] . "</Data></Cell>\n";
                                        endif;
                                elseif ($i == 0):
                                    $type = 'String';
                                    if ($this->bConvertTypes === true && is_numeric($array[$j])):
                                            $type = 'Number';
                                    endif;
                                    $cells .= "<Cell ss:MergeDown=\"".($max_inner_elements - 1)."\"><Data ss:Type=\"$type\">" .  $array[$j]  . "</Data></Cell>\n";
                                //else:
                                    //$cells .= "<Cell><Data ss:Type=\"String\"> </Data></Cell>";

                                endif;
                        endfor;
                        $row .= $cells;
                        $row .= "</Row>\n";
                        $this->lines[] = $row;
                endfor;
        }
        /**
         * Add an array to the document
         * @param array 2-dimensional array
         */
        public function addArray ($array)
        {
                foreach ($array as $k => $v)
                        $this->addRow ($v);
        }

        /**
        * Add an array to the document
        * @param array 3-dimensional array
        */
        public function add3dArray ($array)
        {
                foreach ($array as $k => $v):
                        $max_inner_elements = 1; 
                        foreach($v as $val)
                                if(is_array($val) && count($val) > $max_inner_elements)
                                        $max_inner_elements = count($val);
                        if ($max_inner_elements == 1):
                                $this->addRow($v);
                        else:
                                $this->add2dRow ($v, $max_inner_elements);
                        endif;
                endforeach;
        }


        /**
         * Generate the excel file
         * @param string $filename Name of excel file to generate (...xls)
         */
        public function generateXML ($filename = 'excel-export')
        {
                // correct/validate filename
                $filename = preg_replace('/[^aA-zZ0-9\_\-]/', '', $filename);
    	
                // deliver header (as recommended in php manual)
                header("Content-Type: application/vnd.ms-excel; charset=" . $this->sEncoding);
                header("Content-Disposition: inline; filename=\"" . $filename . ".xls\"");

                // print out document to the browser
                // need to use stripslashes for the damn ">"
                echo stripslashes (sprintf($this->header, $this->sEncoding));
                echo "\n<ss:Worksheet ss:Name=\"" . $this->sWorksheetTitle . "\">\n<Table>\n<x:WorksheetOptions/>\n";
                foreach ($this->lines as $line)
                        echo $line;

                echo "</Table>\n</ss:Worksheet>\n";
                echo $this->footer;
        }

}
