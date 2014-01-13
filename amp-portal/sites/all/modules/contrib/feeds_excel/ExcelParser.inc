<?php

require_once('includes/ExcelRange.inc');

define('EXCELSHEET_SEP', ',');
define('EXCELSHEET_RANGE', ':');

/**
 * Parses a given file as a Excel file.
 */
class FeedsExcelParser extends FeedsParser {

  /**
   * @var Spreadsheet_Excel_Reader
   *  the reader object for this parser.
   */
  protected $reader;

  /**
   * @var string
   *  file path of the feeds fetcher result.
   */
  protected $filepath;

  /**
   * @var array
   *   array of parsed sheet information.
   */
  protected $sheets;

  /**
   * Implementation of FeedsParser::parse().
   */
  public function parse(FeedsSource $source, FeedsFetcherResult $fetcher_result) {
    $source_config = $source->getConfigFor($this);
    // State object holds information on the progress of parsing the given source.
    $state = $source->state(FEEDS_PARSE);

    $this->fetcher_result = $fetcher_result;
    $this->filepath = $fetcher_result->getFilePath();
    _feeds_excel_include_libraries();

    // Read file to object
    $this->reader = new Spreadsheet_Excel_Reader();
    $this->reader->read($this->filepath);

    $limit = $source->importer->getLimit();

    // Populated state with default values.
    if (!isset($state->sheet_states)) {
      $state->sheet_states = array();
    }
    // Popuplate state with the sheet ids, as this will be the only time, so
    // we do process and count with the inital state.
    if (!isset($state->sheet_ids)) {
      $state->sheet_ids = $this->getSheetIDs();
    }

    $rows = $this->parseItems($state, $limit);

    // Get progress.
    $total = 0;
    $progress = 0;
    $max = 10;
    $no_count = 0;
    foreach ($state->sheet_ids as $id) {
      if (!empty($state->sheet_states[$id]['max'])) {
        $total += $state->sheet_states[$id]['max'];
        $progress += $state->sheet_states[$id]['offset'];
        $max = max($max, $state->sheet_states[$id]['max']);
      }
      else {
        $no_count++;
      }
    }
    $total += $no_count * $max;

    // Report progress.
    $state->total = $total;
    $state->progress($state->total, $progress);

    // Create a result object and return it.
    return new FeedsParserResult($rows, $source->feed_nid);
  }


  /**
   * Parse items from the given reader source respecting processing limits.
   * @param $state
   *   state object that comes from source and holds the parsed points.
   * @param integer $limit
   *   maximum number of items to process in one step
   * @return array
   */
  public function parseItems(&$state, $limit) {
    $count = 0;

    $return = array();
    // Run through each sheet until we got enough items.
    foreach ($state->sheet_ids as $sheet_id) {
      // Process only not yet completed sheets.
      if (empty($state->sheet_states[$sheet_id]['complete'])) {
        $offset = (!empty($state->sheet_states[$sheet_id]['offset'])) ? $state->sheet_states[$sheet_id]['offset'] : 0;
        $sheet = $this->getSheet($sheet_id);

        // For the moment we parse the whole sheet at once.
        // @TODO: parse single rows to avoid unnecessary processing.
        $items = $this->getItems($sheet);
        $state->sheet_states[$sheet_id]['max'] = count($items);

        $add = array_slice($items, $offset, $limit - $count, FALSE);
        $return = array_merge($return, $add);

        $count += count($add);
        // Set new offset.
        $state->sheet_states[$sheet_id]['offset'] = $offset + count($add);
        if ($state->sheet_states[$sheet_id]['offset'] >= $state->sheet_states[$sheet_id]['max']) {
          $state->sheet_states[$sheet_id]['complete'] = TRUE;
        }

        // If we got enough items, return them.
        if ($limit <= $count) {
          return $return;
        }
      }
    }
    return $return;
  }

  /**
   * Get the defined sheet ids of sheets that shall get parsed.
   * @return array
   */
  private function getSheetIDs() {
    $sheet_config = $this->config['sheets'];
    $available_sheets = array_keys($this->reader->sheets);

    // Determine all sheets we shall read in by definition.
    $sheet_ids = array();

    // Either all sheets, if no special one is specified.
    if (strlen($sheet_config) < 1) {
      $sheet_ids = $available_sheets;
    }
    // Or sheets, specified by string expression.
    else {
      $sheet_expressions = explode(EXCELSHEET_SEP, $sheet_config);
      foreach($sheet_expressions as $expr) {
        list($from, $to) = explode(EXCELSHEET_RANGE, $expr);
        // Numeric Range
        if (is_numeric($from) && (!$to || is_numeric($to))) {
          if (!$to) {
            $to = $from;
          }
          // Add matching sheets to sheet ids
          foreach($available_sheets as $available_sheet) {
            if ($from <= $available_sheet && $available_sheet <= $to) {
              $sheet_ids[$available_sheet] = $available_sheet;
            }
          }
        }
        // Search for sheet name
        else {
          foreach ($this->reader->boundsheets as $sheet_id => $boundsheet) {
            if ($boundsheet['name'] == $expr) {
              $sheet_ids[$sheet_id] = $sheet_id;
            }
          }
        }
      }
    }
    return $sheet_ids;
  }

  /**
   * Retrieve all relevant sheets from dataset.
   */
  private function getSheet($sheet_id) {
    if (!isset($this->sheets[$sheet_id])) {
      $excel_sheet = $this->reader->sheets[$sheet_id];
      $excel_sheet['boundsheet_offset'] =  $this->reader->boundsheets[$sheet_id]['offset'];
      $excel_sheet['name'] =  $this->reader->boundsheets[$sheet_id]['name'];
      $excel_sheet['id'] = $sheet_id;
      //$sheets[$sheet_id] = token_get_values('excel_sheet', $sheet);
      $excel_sheet['fixed_cells'] = $this->getFixed($this->reader->sheets[$sheet_id]);
      $this->sheets[$sheet_id] = $excel_sheet;
    }
    return $this->sheets[$sheet_id];
  }

  /**
   * Sort out the given fixed cells.
   */
  private function getFixed($sheet) {
    // Store fixed cells in sheet
    $fixed = array();
    if ($this->config['fixed']) {
      $max = array(
        'rows' => $sheet['numRows'],
        'cols' => $sheet['numCols'],
      );
      $fixed_range = new ExcelRange($this->config['fixed'], $max);

      foreach ($sheet['cells'] as $row => $cols) {
        if ($fixed_range->isRowInAnyRange($row)) {
          foreach ($cols as $col => $val) {
            if ($fixed_range->isCellInAnyRange($row, $col)) {
              $cell = array(
                'value' => $val,
                'type' => $sheet['cellsInfo'][$row][$col]['type'],
                'raw' => (isset($sheet['cellsInfo'][$row][$col]['raw'])) ? $sheet['cellsInfo'][$row][$col]['raw'] : $val,
                'column' => $col,
                'row' => $row,
              );
              $fixed["$row-$col"] = $cell;
            }
          }
        }
      }
    }
    return $fixed;
  }

  /**
   * Retrieve Items from Spredsheet.
   * @param $sheet
   */
  private function getItems($sheet) {
    $mode = $this->config['mapping_mode'];

    $items = array();

    // Only run non-empty sheets
    if (!empty($sheet['cells'])) {
      $max = array(
        'rows' => $sheet['numRows'],
        'cols' => $sheet['numCols'],
      );
      $iterative_range = new ExcelRange($this->config['iterative'], $max);
      $sheet_id = $sheet['id'];

      // Retrieve all items out of defined iterative Range.
      while ($range = $iterative_range->getNextRange()) {
        $range_id = $iterative_range->getCurrentRangeID();
        $cells = $sheet['cells'];
        foreach ($cells as $row => $cols) {
          if ($iterative_range->isRowInCurrentRange($row)) {
            $row_offset = $iterative_range->getCurrentRangeRowOffset();
            $max_rows = $iterative_range->getCurrentRangeMaxRow();
            foreach($cols as $col => $value) {

              if ($iterative_range->isColInCurrentRange($col)) {
                $col_offset = $iterative_range->getCurrentRangeColOffset();
                $max_cols = $iterative_range->getCurrentRangeMaxRow();
                // Encode value and raw to utf8 in any case
                $value = utf8_encode($value);
                if (!isset($sheet['cellsInfo'][$row][$col]['raw'])) {
                  $raw = $value;
                }
                else {
                  $raw = utf8_encode($sheet['cellsInfo'][$row][$col]['raw']);
                }

                // Build cell
                $cell = array(
                  'value' => $value,
                  'type' => $sheet['cellsInfo'][$row][$col]['type'],
                  'raw' => $raw,
                  'column' => $col,
                  'row' => $row,
                  'x' => $col - $col_offset,
                  'y' => $row - $row_offset,
                );

                // Set items dependent on
                switch ($mode) {
                  case 'rows':
                    $key = "$sheet_id:$range_id:$row";
                    $items[$key]['cells'][$col - $col_offset] = $cell;
                    $items[$key]['row'] = $row;
                    break;
                  case 'columns':
                    $key = "$sheet_id:$range_id:$col";
                    $items[$key]['cells'][$row - $row_offset] = $cell;
                    $items[$key]['column'] = $col;
                    break;
                }
                // Meta data for the given item
                if ($key && !isset($items[$key]['meta'])) {
                  $items[$key]['meta'] = array(
                    'range_row_offset' => $row_offset,
                    'range_col_offset' => $col_offset,
                    'range_id' => $range_id,
                    'sheet_id' => $sheet_id,
                    'range_max_cols' => $max_cols,
                    'range_max_rows' => $max_rows,
                    'key' => $key,
                  );
                }

              }
            }
          }
        }
      }
    }
    return $items;
  }

  /**
   * Override parent::getMappingSources().
   */
  public function getMappingSources() {
    return FALSE;
  }

  /**
   * Return the token replaced value for the given element.
   */
  public function getSourceElement(FeedsSource $source, FeedsParserResult $result, $element_key){

    // excel file
    $types['excel-file'] = (object) array(
      'filepath' => $this->filepath,
    );
    // excel sheet
    $item = (object) $result->currentItem();
    $data['excel-sheet'] = (object) $this->getSheet($item->meta['sheet_id']);
    // excel row or column
    switch ($this->config['mapping_mode']) {
      case 'rows':
        $data['excel-row'] = $item;
        break;
      case 'columns':
        $data['excel-column'] = $item;
        break;
    }
    // global
    $data['site'] = '';

    // parent node
    if ($source->feed_nid) {
      $node = node_load($source->feed_nid);
      $data['node'] = $node;
    }

    $value = token_replace($element_key, $data, array('clear' => FALSE)); // For debug clear is set to FALSE;
    return $value;
  }

  /**
   * Define default configuration.
   */
  public function configDefaults() {
    return array(
      'mapping_mode' => 'rows',
      'sheets' => '',
      'header' => '',
      'fixed' => '',
      'iterative' => '1:10000',
    );
  }

  /**
   * Build configuration form.
   */
  public function configForm(&$form_state) {
    $form = array();
    $form['mapping_mode'] = array(
      '#type' => 'select',
      '#title' => t('Mapping mode'),
      '#description' => t('Whether to map rows or columns.'),
      '#options' => array(
        'rows' => t('Rows'),
        'columns' => t('Columns'),
      ),
      '#default_value' => $this->config['mapping_mode'],
    );

    $form['sheets'] = array(
      '#type' => 'textfield',
      '#title' => t('Sheets'),
      '#default_value' => $this->config['sheets'],
      '#description' => t('Enter the sheet names or IDs that shall be imported. Leaving empty means that all sheets are processed. Use <code>,</code> to separate different sheets. Use <code>:</code> to mark a range of IDs (e.g. <code>0:15</code>).'),
    );

    // Format string for range format.
    $format_single_1 = '<code>'. EXCELRANGE_ROW .'1'. EXCELRANGE_COL .'2</code>';
    $format_single_2 = '<code>B1</code>';
    $format_range_1 = '<code>'. EXCELRANGE_ROW .'1'. EXCELRANGE_COL .'2:'. EXCELRANGE_ROW .'5'. EXCELRANGE_COL .'4</code>';
    $format_range_2 = '<code>B1:D5</code>';
    $format_columns = '<code>B:D</code>';
    $format_rows = '<code>1:5</code>';
    $format = t('You can use different range formats: !formats <br/> Use !sep to separate multiple range entries.',
                array('!sep' => '<code>'. EXCELRANGE_SEP .'</code>', '!formats' => "$format_single_1, $format_single_2, $format_range_1, $format_range_2, $format_columns, $format_rows"));

    $form['fixed'] = array(
      '#type' => 'textfield',
      '#title' => t('Fixed Range'),
      '#description' => t('Range of the sheet, that can be referenced from any item.<br/>!format', array('!format' => $format)),
      '#default_value' => $this->config['fixed'],
    );
    $form['iterative'] = array(
      '#type' => 'textfield',
      '#title' => t('Iterative range'),
      '#description' => t('Enter a range that feeds should run through for iterative mapping to process items.<br/>!format', array('!format' => $format)),
      '#required' => TRUE,
      '#default_value' => $this->config['iterative'],
    );
    $form['header'] = array(
      '#type' => 'textfield',
      '#title' => t('Header range'),
      '#description' => t('Enter the range that can be used as row or column aliases for a given iterative range.<br/>!format', array('!format' => $format)),
      '#default_value' => $this->config['header'],
      '#disabled' => TRUE,
      '#prefix' => '<span class="error">Currently disabled option:</span>',
    );
    return $form;
  }

  /**
   * Validate rows and columns input in ConfigForm.
   */
  public function configFormValidate (&$values) {
    // Check valid range formats.
    $range_fields = array('iterative', 'fixed', 'header');
    foreach ($range_fields as $field) {
      if ($values[$field]) {
        $range = new ExcelRange($values[$field]);
        if (!$range->converted_all_ranges()) {
          form_set_error($field, t('The range expression could not be completely converted in a valid range format.'));
        }
      }
    }
  }
}
