<?php

/**
 * @file
 * Custom Entity Controller for questions.
 */

/**
 * Class ampAPIMetadataController
 */
class ampAPIMetadataController extends EntityDefaultMetadataController {
  protected $schemaInfo;

  public function __construct($type) {
    parent::__construct($type);
    $this->schemaInfo = ampapi_get_schema_info($type);
  }

  public function entityPropertyInfo() {
    $entity_label = $this->info['label'];

    // Provide defaults based on the schema.
    $info['properties'] = $this->convertSchema();
    foreach ($info['properties'] as $name => &$property) {
      if (isset($property['description'])) {
        continue;
      }
      // Add a description.
      $property['description'] = t('@entity "@property" property.', array('@entity' => drupal_ucfirst($entity_label), '@property' => $name));
    }

    // Care for the bundle.
    if (!empty($this->info['entity keys']['bundle']) && $key = $this->info['entity keys']['bundle']) {
      $info['properties'][$key]['type'] = 'token';
      $info['properties'][$key]['options list'] = array(get_class($this), 'bundleOptionsList');
    }

    return array($this->type => $info);
  }

  /**
   * Return a set of properties for an entity based on the schema.
   */
  protected function convertSchema() {
    $properties_info = array();

    foreach ($this->schemaInfo as $key => $info) {
      if (empty($info['property info']['type'])) {
        continue;
      }
      $field_property_info = $info['property info'] + array(
        'queryable' => TRUE,
        'schema field' => $key,
      );

      foreach (array('label', 'description', 'required') as $name) {
        if (isset($info[$name])) {
          $field_property_info[$name] = $info[$name];
        }
      }

      $bundle = FALSE;
      if (!empty($info['bundle'])) {
        $bundle = $info['bundle'];
      }

      if (empty($bundle)) {
        $properties_info[$key] = $field_property_info;
      }
      else {
        $properties_info['bundles'][$bundle]['properties'][$key] = $field_property_info;
      }
    }

    return $properties_info;
  }
}
