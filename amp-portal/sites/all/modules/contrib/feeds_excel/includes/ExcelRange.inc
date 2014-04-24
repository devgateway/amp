<?php


define('EXCELRANGE_SEP', ',');
define('EXCELRANGE_RANGE', ':');
define('EXCELRANGE_ROW', 'R');
define('EXCELRANGE_COL', 'C');

class ExcelRange {

  private $raw;
  private $raw_ranges;
  private $ranges = array();
  private $max_rows = 0;
  private $max_cols = 0;
  private $rows = array();
  private $columns = array();
  private $cells = array();
  private $currentRange = NULL;
  private $currentRangeID = NULL;


  /**
   * Init object.
   */
  function __construct($range_expression, $max = NULL) {
    if (isset($max)) {
      $this->max_rows = $max['rows'];
      $this->max_cols = $max['cols'];
    }
    $this->expression_to_ranges($range_expression);
  }

  /**
   * Function to retrieve if all range expressions could be transformed into range
   * arrays.
   */
  function converted_all_ranges() {
    return count($this->raw_ranges) == count($this->ranges);
  }

  /**
   * Convert a string expression to a ranges array.
   */
  function expression_to_ranges($expression) {
    $expression = strtoupper($expression);
    $raw_ranges = explode(EXCELRANGE_SEP, $expression);
    $this->raw_ranges = $raw_ranges;
    foreach($raw_ranges as $range_expression) {
      $range = $this->range_expression_to_range_array($range_expression);
      if ($range) {
        $this->ranges[] = $range;
      }
    }
  }

  /**
   * Set a specific range.
   */
  function setRange($id) {
    $this->currentRangeID = $id;
    if (isset($this->ranges[$id])) {
      $this->currentRange = $this->ranges[$id];
      return TRUE;
    }
    else {
      $this->currentRange = NULL;
      return FALSE;
    }
  }

  /**
   * Get the current Range description.
   */
  function getCurrentRange() {
    if (!isset($this->currentRangeID)) {
      $this->setRange(0);
    }
    return $this->currentRange;
  }

  /**
   * Set pointer to the next range.
   */
  function getNextRange() {
    if (!isset($this->currentRangeID)) {
      $this->setRange(0);
    }
    else {
      $this->setRange($this->currentRangeID +1);
    }
    return $this->currentRange;
  }
  /**
   * Set pointer to the previous range.
   */
  function getPreviousRange() {
    $this->setRange($this->currentRangeID - 1);
    return $this->currentRange;
  }

  /**
   * Convert a single range expression to a range array
   */
  function range_expression_to_range_array($range_expression) {

    // R1C1:R2C5
    $regexp1 = '&R([1-9][0-9]*)C([1-9][0-9]*)('. EXCELRANGE_RANGE .'R([1-9][0-9]*)C([1-9][0-9]*))?&is';
    // A1:E2
    $regexp2 = '&([A-Z]+)([1-9][0-9]*)('. EXCELRANGE_RANGE .'([A-Z]+)([1-9][0-9]*))?&is';
    // A:E
    $regexp3 = '&([A-Z]+)('. EXCELRANGE_RANGE .'([A-Z]+))?&is';
    // 1:2
    $regexp4 = '&([1-9][0-9]*)('. EXCELRANGE_RANGE .'([1-9][0-9]*))?&is';

    $matches = array();
    // Row-Colum-style
    if (preg_match($regexp1, $range_expression, $matches)) {
      // Found a "to expression"
      if (isset($matches[3])) {
        $range = array(
          'rows' => array( $matches[1],  $matches[4]),
          'cols' => array( $matches[2],  $matches[5]),
        );
      }
      else {
        $range = array(
          'rows' => array( $matches[1],  $matches[1]),
          'cols' => array( $matches[2],  $matches[2]),
        );
      }
    }
    // Alphanumeric style
    elseif (preg_match($regexp2, $range_expression, $matches)) {
      if (isset($matches[3])) {
        $range =  array(
          'rows' => array( $matches[2],  $matches[5]),
          'cols' => array( $this->alpha2num($matches[1]), $this->alpha2num($matches[4])),
        );
      }
      else {
        $alpha = $this->alpha2num($matches[1]);
        $range = array(
          'rows' => array( $matches[2], $matches[2]),
          'cols' => array( $alpha, $alpha),
        );
      }
    }
    // Alphanumeric columns only
    elseif (preg_match($regexp3, $range_expression, $matches)) {
      if (isset($matches[2])) {
        $range = array(
          'cols' => array( $this->alpha2num($matches[1]), $this->alpha2num($matches[3])),
        );
      }
      else {
        $alpha = $this->alpha2num($matches[1]);
        $range = array(
          'cols' => array( $alpha, $alpha),
        );
      }
    }
    // Numeric rows only
    elseif (preg_match($regexp4, $range_expression, $matches)) {
      if (isset($matches[2])) {
        $range = array(
          'rows' => array( $matches[1],  $matches[3]),
        );
      }
      else {
        $range = array(
          'rows' => array( $matches[1], $matches[1]),
        );
      }
    }

    // Check max rows and columns restictriction
    if (is_array($range)) {
      // max rows
      if ($this->max_rows) {
        // restric row to max rows count
        if (!isset($range['rows'])) {
          $range['rows'] = array(1, $this->max_rows);
        }
        // outside of max range
        elseif ($range['rows'][0] > $this->max_rows) {
          return NULL;
        }
        // last item is out of max range
        elseif ($range['rows'][1] > $this->max_rows) {
          $range['rows'][1] = $this->max_rows;
        }
      }

      // max columns
      if ($this->max_cols) {
        // restric row to max rows count
        if (!isset($range['cols'])) {
          $range['cols'] = array(1, $this->max_cols);
        }
        // outside of max range
        elseif ($range['cols'][0] > $this->max_cols) {
          return NULL;
        }
        // last item is out of max range
        elseif ($range['cols'][1] > $this->max_cols) {
          $range['cols'][1] = $this->max_cols;
        }
      }
    }

    return $range;
  }

  /**
   * Retrieve if the given cell is in any range.
   */
  function isCellInAnyRange($row, $col) {
    foreach ($this->ranges as $range) {
      if ($this->isRowInRange($range, $row)) {
        if ($this->isColInRange($range, $col)) {
          return TRUE;
        }
      }
    }
    return FALSE;
  }

  /**
   * Retrieve if a cell of the row is present in any range.
   */
  function isRowInAnyRange($row) {
    foreach ($this->ranges as $range) {
      if ($this->isRowInRange($range, $row)) {
        return TRUE;
      }
    }
    return FALSE;
  }
  /**
   * Retrieve if a cell of the columns is present in any range.
   */
  function isColInAnyRange($col) {
    foreach ($this->ranges as $range) {
      if ($this->isColInRange($range, $col)) {
        return TRUE;
      }
    }
    return FALSE;
  }

  /**
   * Retrieve if the coordinates fit into the given range.
   */
  function isCellInRange($range, $row, $col) {
    if ($this->isRowInRange($range, $row)) {
      return $this->isColInRange($range, $col);
    }
    return FALSE;
  }
  /**
   * Retrieve if any cell of the row is in the given range.
   */
  function isRowInRange($range, $row) {
    return (!$range['rows']
            || $range['rows'][0] <= $row && $row <= $range['rows'][1]);
  }
  /**
   * Retrive if any cell is in the column of the given range.
   */
  function isColInRange($range, $col) {
    return (!$range['cols']
            || $range['cols'][0] <= $col && $col <= $range['cols'][1]);
  }


  /**
   * Retrieve if the coordinates fit to the current Range.
   */
  function isCellInCurrentRange($row, $col) {
    return $this->isCellInRange($this->currentRange, $row, $col);
  }
  /**
   * Retrieve if there are cells of the row in the current range.
   */
  function isRowInCurrentRange($row) {
    return $this->isRowInRange($this->currentRange, $row);
  }
  /**
   * Retrieve if there are cells of the column in the current range.
   */
  function isColInCurrentRange($col) {
    return $this->isColInRange($this->currentRange, $col);
  }

  /**
   * Retrieve the id of the current range.
   */
  function getCurrentRangeID() {
    return $this->currentRangeID;
  }

  /**
   * Retrieve the row offset of the current Range
   */
  function getCurrentRangeRowOffset(){
    if ($this->currentRange['rows']) {
      return ($this->currentRange['rows'][0] > 0) ? $this->currentRange['rows'][0] - 1 : 0;
    }
    return O;
  }
  /**
   * Retrieve the column offset of the current Range.
   */
  function getCurrentRangeColOffset(){
    if ($this->currentRange['cols']) {
      return ($this->currentRange['cols'][0] > 0) ? $this->currentRange['cols'][0] - 1 : 0;
    }
    return O;
  }

  /**
   * Get the maximum column of the defined range.
   */
  function getCurrentRangeMaxCol(){
    if ($this->currentRange['cols'] && isset($this->currentRange['cols'][1])) {
      return $this->currentRange['cols'][1];
    }
    return NULL;
  }
  /**
   * Get the maximum row of the defined range.
   */
  function getCurrentRangeMaxRow(){
    if ($this->currentRange['rows'] && isset($this->currentRange['rows'][1])) {
      return $this->currentRange['rows'][1];
    }
    return NULL;
  }

  /**
   * Converts an integer into the alphabet base (A-Z).
   *
   * @param int $n This is the number to convert.
   * @return string The converted number.
   * @author Theriault
   *
   * @see http://www.php.net/manual/de/function.base-convert.php#94874
   * Theriault counts from 0 to x, phpExcelReader does from 1 to x.
   *
   */
  function num2alpha($n) {
    $r = '';
    $n--;
    for ($i = 1; $n >= 0 && $i < 10; $i++) {
      $r = chr(0x41 + ($n % pow(26, $i) / pow(26, $i - 1))) . $r;
      $n -= pow(26, $i);
    }
    return $r;
   }

  /**
   * Converts an alphabetic string into an integer.
   *
   * @param int $n This is the number to convert.
   * @return string The converted number.
   * @author Theriault
   *
   * @see http://www.php.net/manual/de/function.base-convert.php#94874
   * Theriault counts from 0 to x, phpExcelReader does from 1 to x.
   */
  function alpha2num($alpha) {
    $a = str_split(strtoupper($alpha), 1);

    $r = 0;
    $l = count($a);
    for ($i = 0; $i < $l; $i++) {
      $r += pow(26, $i) * (ord($a[$l - $i - 1]) - 0x40);
    }
    return $r;
  }

}
