<?php

/**
 * @file
 * Shared classes and interfaces for the API mapper system.
 */

/**
 * Defines the common interface for all API mapper classes.
 *
 * @TODO: Document!
 */
interface activityMapperInterface {
  public static function propertyInfo($plugin);
  public static function filter($plugin, &$api_parameters, &$request_body, $condition);
  public static function sort($plugin, &$api_parameters, &$request_body, $order);
  public static function getValue($plugin, $column);
}

class activityMapperException extends Exception {}

/**
 * Defines the base implementation for all API mappers.
 */
class activityMapperBase implements activityMapperInterface {
  /**
   * Property Info handler.
   */
  public static function propertyInfo($plugin) {
    $info = array(
      'label' => $plugin['title'],
      'description' => $plugin['description'],
    );
    $info['property info'] = $plugin['property info'];

    return array($plugin['property name'] => $info);
  }

  /**
   * Filter handler.
   */
  public static function filter($plugin, &$api_parameters, &$request_body, $condition) {
    if (empty($condition['value'])) {
      return;
    }

    if (empty($condition['operator'])) {
      $condition['operator'] = is_array($condition['value']) ? 'IN' : '=';
    }

    if (empty($plugin['report filter key'])) {
      $plugin_title = check_plain($plugin['title']);
      throw new ampAPIEntityQueryHelperException("The {$plugin_title} plugin can not be used to filter activities.");
    }

    $key = $plugin['report filter key'];

    switch ($condition['operator']) {
      case 'IN':
      case '=':
        // The AMP API likes to group filters into certain groups, or not.
        // @TODO: Requests for the API to change this behavior.
        if ($plugin['report field group'] == 'columns') {
          $request_body['filters']['columnFilters'][$key] = $condition['value'];
        }
        else if ($plugin['report field group'] == 'others') {
          $request_body['filters']['otherFilters'][$key] = $condition['value'];
        }
        break;

      default:
        $operator = check_plain($condition['operator']);
        throw new ampAPIEntityQueryHelperException("Operator \"$operator\" is not implemented for \"{$plugin['title']}\"");
    }
  }

  /**
   * Sort handler.
   */
  public static function sort($plugin, &$api_parameters, &$request_body, $order) {
    $key = $plugin['report field key'];
    $request_body['sorting'][] = array(
      'columns' => array($key),
      'asc' => $order['direction'] == 'asc' ? TRUE : FALSE,
    );
  }

  /**
   * Value handler.
   */
  public static function getValue($plugin, $column, $formatter = NULL) {
    if (isset($column->displayedValue)) {
      // We don't have to filter values in all the cases,
      // text types are passed through check_plain when are being viewed.
      $to_filter = TRUE;
      if (in_array($plugin['property info']['type'], array('text'))) {
        $to_filter = FALSE;
      }
      $filtered = ($to_filter) ? filter_xss($column->displayedValue, array()) : $column->displayedValue;
      return $filtered;
    }

    if ($formatter && $plugin['report field group'] == 'measures') {
      return $formatter->format($column->value);
    }

    if ($column->value == '#null') {
      return NULL;
    }
  }
}
