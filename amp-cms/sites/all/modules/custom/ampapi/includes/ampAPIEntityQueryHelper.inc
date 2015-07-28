<?php
/**
 * @file
 * Contains the remote entity query handler.
 */

/**
 * Exception thrown by EntityFieldQuery() on unsupported query syntax.
 *
 * Raise an ampAPIEntityQueryHelperException when an unsupported
 * condition was specified.
 */
class ampAPIEntityQueryHelperException extends Exception {}

/**
 * Retrieves remote entities matching a given set of conditions.
 */
class ampAPIEntityQueryHelper {
  /**
   * Entity type for this query helper instance.
   */
  public $entityType;

  /**
   * Array of information about the entity.
   */
  public $entityInfo;

  /**
   * The API connection endpoint.
   */
  public $api_resource_path = NULL;

  /**
   * Whether or not SSC search has been selected.
   */
  public $current_search_ssc_status = FALSE;

  /**
   * Constructor: sets the connection.
   */
  function __construct($entity_type) {
    if (empty($entity_type)) {
      throw new ampAPIEntityQueryHelperException(t('An entity type must be specified.'));
    }

    // Get the entity information.
    $this->entityType = $entity_type;
    $this->entityInfo = entity_get_info($this->entityType);

    // Get the API endpoint.
    if (empty($this->entityInfo['api resource path'])) {
      throw new ampAPIEntityQueryHelperException(t('Entity %entity has no "api resource path".', array('%entity' => $this->entityType)));
    }
    $this->api_resource_path = $this->entityInfo['api resource path'];
  }

  /**
   * Queries entity collections stored in AMP.
   *
   * @see EntityFieldQuery::queryCallback().
   */
  function executeQuery(EntityFieldQuery $query) {
    global $language_content;

    static $api_status;
    if (!isset($api_status)) {
      $api_status = helpergeneric_check_api_status(FALSE);

      if (!$api_status['online']) {
        drupal_set_message($api_status['message'], 'warning');
      }
    }
    if (empty($api_status['online'])) {
      return ($query->count) ? 0 : array();
    }

    $api_parameters = array(
      'error_handling' => TRUE,
      'reset' => TRUE,
    );
    $request_body = array(
      'add_columns' => array(),
      'add_measures' => array(),
    );

    // Always load the Activity ID field.
    $plugin = ampapi_get_mapper_by_property_name('id');
    $request_body['add_columns'][] = $plugin['report field key'];

    // Build an array of parameters for the API.
    foreach ($query->entityConditions as $column => $condition) {
      // Exclude entity_type condition since we use a query handler per entity_type.
      if ($column == 'entity_type') {
        continue;
      }

      // Special filter for requesting SSC activities.
      if ($column == 'activities_search_type') {
        if (!empty($condition['value']) && ampapi_activity_ssc_status()) {
          $this->current_search_ssc_status = TRUE;
          // Some magic strings, see the API docs.
          $request_body['reportType'] = 'C';
          $request_body['projectType'] = array('S');
        }
        continue;
      }

      $plugin = ampapi_get_mapper_by_property_name($condition['column']);
      if ($class_name = ctools_plugin_get_class($plugin, 'handler')) {
        /** @var activityMapperInterface $handler */
        $handler = new $class_name();
        $handler->filter($plugin, $api_parameters, $request_body, $condition);
      }
    }

    // Build an array of parameters for the API.
    foreach ($query->propertyConditions as $condition) {
      $plugin = ampapi_get_mapper_by_property_name($condition['column']);
      if ($class_name = ctools_plugin_get_class($plugin, 'handler')) {
        /** @var activityMapperInterface $handler */
        $handler = new $class_name();
        $handler->filter($plugin, $api_parameters, $request_body, $condition);
      }
    }

    // Add a list of fields to load.
    // @TODO: Add a settings page.
    if (!empty($query->metaData['view']->field)) {
      $this->_api_add_fields($query, $request_body);
    }

    $api_parameters['query'] = array(
      'language' => $language_content->language,
    );

    $api_parameters['headers'] = array(
      'Accept' => 'application/json',
      'Content-Type' => 'application/json',
    );

    // The API is slow at the moment.
    $api_parameters['timeout'] = 120;

    if (!empty($query->range)) {
      $request_body['recordsPerPage'] = $query->range['length'];
      $request_body['page'] = $query->range['start'] / $query->range['length'] + 1; // It's not 0 based.

      // Only calculate totals when we request a single page.
      if (!empty($query->metaData['view'])) {
        $request_body['rowTotals'] = TRUE;
      }
    }
    else {
      // Request all entries if no pager settings are available.
      $request_body['recordsPerPage'] = -1;
    }

    // Always show Activities with empty funding.
    $request_body['show_empty_rows'] = TRUE;

    // @TODO: Deal with multiple sorts and maping of special properties.
    // @TODO: Request API support for count queries, for now we don't include order options.
    if ($query->order && !$query->count) {
      $order = end($query->order);
      $plugin = ampapi_get_mapper_by_property_name($order['specifier']);

      if ($class_name = ctools_plugin_get_class($plugin, 'handler')) {
        /** @var activityMapperInterface $handler */
        $handler = new $class_name();
        $handler->sort($plugin, $api_parameters, $request_body, $order);
      }
    }

    // Add a "report" name, required for API cache.
    $md5_data = $request_body;
    unset($md5_data['page']);
    unset($md5_data['regenerate']);
    $request_body['name'] = 'api:search:' . md5(json_encode($md5_data));

    // The api requires that we provide at least an empty object.
    $api_parameters['body'] = empty($request_body) ? '{}' : json_encode($request_body);
    $response = restclient_post($this->api_resource_path , $api_parameters);

    if (empty($response)) {
      drupal_set_message(t('The API returned an empty response, please try again later.'), 'error');
      return ($query->count) ? 0 : array();
    }

    // @TODO: Add support for more 200 class response codes.
    if (!empty($response) && restclient_response_code($response) != RESTCLIENT_RESPONSE_SUCCESS) {
      $t_args = array();
      if (!empty($response->error)) {
        $t_args['%message'] = $response->error;
      }
      else if (!empty($response->status_message)) {
        $t_args['%message'] = $response->status_message;
      }
      drupal_set_message(t('The API returned the following status message: %message', $t_args), 'error');
      return ($query->count) ? 0 : array();
    }

    $response_data = json_decode($response->data);
    if (!empty($response_data->error)) {
      foreach ($response_data->error as $error_message) {
        $t_args = array(
          '%message' => check_plain($error_message),
        );
        drupal_set_message(t('The API returned the following error message: %message', $t_args), 'error');
        return ($query->count) ? 0 : array();
      }
    }

    // Count the number of items.
    // @TODO: Request API support for count queries.
    if ($query->count) {
      if (empty($response_data->page->totalRecords)) {
        return 0;
      }
      return $response_data->page->totalRecords;
    }

    // We have a different format when there is only one result.
    // @TODO: Request a fix in the API.
    if ($response_data->page->totalRecords == 1 && empty($response_data->page->pageArea->children)) {
      $row = clone $response_data->page->pageArea;
      unset($response_data->page->pageArea->contents);
      $response_data->page->pageArea->children[] = $row;
    }

    // Get the number formatting pattern.
    $number_format = NULL;
    if (is_array($response_data->settings)) {
      foreach ($response_data->settings as $info) {
        if ($info->id != 'ammountPattern') {
          continue;
        }
        $number_format = $info->value;
      }
    }

    $entities = array();
    if (!empty($response_data->page->pageArea->children)) {
      $entities = $this->process_response_data($response_data, $query, $number_format);
    }

    // Tell efq_views that the entities are already loaded.
    if (!empty($query->metaData['view'])) {
      $this->_api_add_query_metadata($response_data, $query, $number_format);
    }

    return array($this->entityType => $entities);
  }

  /**
   * Adds fields enabled in the view to the API request.
   */
  function _api_add_fields($query, &$request_body) {

    // Get a list of enabled columns and measures.
    // GET /rest/data/report/columns
    $columns_data = ampapi_request('get', 'data/report/columns', TRUE);
    // GET /rest/data/report/measures
    $measures_data = ampapi_request('get', 'data/report/measures', TRUE);

    foreach ($query->metaData['view']->field as $field_name => $field_handler) {
      $plugin = ampapi_get_mapper_by_property_name($field_name);
      if (empty($plugin['report field key'])) {
        continue;
      }

      switch ($plugin['report field group']) {
        case 'columns':
          if (empty($columns_data->{$plugin['report field key']})) {
            continue;
          }
          if (!in_array($plugin['report field key'], $request_body['add_columns'])) {
            $request_body['add_columns'][] = $plugin['report field key'];
          }
          break;
        case 'measures':
          if (empty($measures_data->{$plugin['report field key']})) {
            continue;
          }
          if (in_array($plugin['report field key'], $request_body['add_measures'])) {
            continue;
          }

          $request_body['add_measures'][] = $plugin['report field key'];
          break;
      }
    }
  }

  /**
   * Adds extra information from the API to be used in Views at a later point.
   */
  function _api_add_query_metadata($response_data, EntityFieldQuery $query, $number_format) {
    $search_settings = ampapi_activity_get_search_settings();
    $query->addMetaData('entities_preloaded', TRUE);

    // Add the currency to the query metadata.
    foreach ($response_data->settings as $setting) {
      // @HACK: The id for currency is "1"!!!
      // @see: Documentation for /rest/gis/settings
      if ($setting->id == '1' && isset($setting->value->defaultId)) {
        $query->addMetaData('report_currency', $setting->value->defaultId);
      }
    }

    // Add the totals to the query metadata.
    if (!empty($response_data->page->pageArea->contents)) {
      $response_totals = $response_data->page->pageArea->contents;
      $report_totals = array();

      // @see https://php.net/manual/en/numberformatter.create.php
      // @see http://www.icu-project.org/apiref/icu4c/classDecimalFormat.html#details
      $formatter_locale = NULL;
      $formatter = new NumberFormatter($formatter_locale, NumberFormatter::PATTERN_DECIMAL, $number_format);

      $headers_list = array();
      $mappers = ampapi_get_mappers();
      foreach ($mappers as $key => $plugin) {
        if (empty($plugin['property name'])) {
          continue;
        }

        if ($plugin['report field group'] != 'measures') {
          continue;
        }

        // Hide SSC specific fields when SSC functionality is disabled.
        if (!in_array($plugin['property name'], $search_settings['enabled_columns'])) {
          continue;
        }

        // Only display enabled measures in the totals table.
        $headers_list[$plugin['property name']] = '[' . $plugin['report field key'] . ']';
      }

      foreach ($response_data->headers as $h_info) {
        // @TODO: use mappers.
        if (!in_array($h_info->hierarchicalName, $headers_list)) {
          continue;
        }

        if (!isset($response_totals->{$h_info->hierarchicalName}->value)) {
          continue;
        }

        if ($formatter) {
          $amount = $formatter->format($response_totals->{$h_info->hierarchicalName}->value);
        }
        else {
          $amount = check_plain($response_totals->{$h_info->hierarchicalName}->displayedValue);
        }

        $report_totals[$h_info->originalColumnName] = array(
          'header' => filter_xss($h_info->columnName, array()),
          'amount' => $amount,
        );
      }
      $query->addMetaData('report_totals', $report_totals);
    }
  }

  /**
   * Creates entities out of data from the API.
   */
  function process_response_data($data, EntityFieldQuery $query, $number_format = NULL) {
    $entities = array();

    $headers = $data->headers;
    $rows = $data->page->pageArea->children;
    foreach ($rows as $delta => $row) {
      if (empty($row->contents)) {
        continue;
      }
      $values = array();

      // @TODO: Find a different list of fields that also includes the ID.
      // Removing the external URL will also remove the ID.
      foreach ($query->metaData['view']->field as $field_handler) {
        if ($field_handler->real_field == 'external_url') {
          $plugin = ampapi_get_mapper_by_property_name('id');
        }
        else {
          $plugin = ampapi_get_mapper_by_property_name($field_handler->real_field);
        }

        $formatter = NULL;
        if ($plugin['report field group'] == 'measures') {
          // @see https://php.net/manual/en/numberformatter.create.php
          // @see http://www.icu-project.org/apiref/icu4c/classDecimalFormat.html#details
          $formatter_locale = NULL;
          $formatter = new NumberFormatter($formatter_locale, NumberFormatter::PATTERN_DECIMAL, $number_format);
        }

        $hierarchical_name = NULL;
        foreach ($headers as $header) {
          if ($header->originalColumnName == $plugin['report field key']) {
            $hierarchical_name = $header->hierarchicalName;
          }
        }

        if ($plugin['property name'] == 'id') {
          // Get the entity id.
          if ($hierarchical_name && $class_name = ctools_plugin_get_class($plugin, 'handler')) {
            $values[$plugin['property name']] = call_user_func_array(array($class_name, 'getValue'), array($plugin, $row->contents->$hierarchical_name, $formatter));

            // Create the external url for this entity.
            $amp_baseurl = helpergeneric_detect_amp_baseurl();
            $url_prefix = '/aim/viewActivityPreview.do~public=true~pageId=2~activityId=';
            $language_suffix = helpergeneric_get_amp_language_parameter();
            $values['external_url'] = $amp_baseurl . $url_prefix . $values[$plugin['property name']] . $language_suffix;
          }
        }

        $request_body['add_measures'][] = $plugin['report field key'];
        if ($hierarchical_name && $class_name = ctools_plugin_get_class($plugin, 'handler')) {
          $values[$plugin['property name']] = call_user_func_array(array($class_name, 'getValue'), array($plugin, $row->contents->$hierarchical_name, $formatter));
        }
      }

      // Create an entity.
      $entities[$delta] = entity_create('activity', $values);
    }

    return $entities;
  }
}
