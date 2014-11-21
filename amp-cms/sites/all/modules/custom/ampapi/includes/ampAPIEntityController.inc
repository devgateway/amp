<?php

/**
 * @file
 * Custom Entity Controller for entities stored in the AMP API.
 */

/**
 * Defines an interface for AMP API entities controller.
 */
interface ampAPISchemaInterface {
  /**
   * Renturs a list information about the API fields.
   */
  public static function schemaInfo();
}

/**
 * Class ampAPIEntityController
 */
class ampAPIEntityController extends EntityAPIController implements ampAPISchemaInterface {
  /**
   * Array of information about the entity properties.
   */
  public $propertyInfo;

  /**
   * The API Resource Path for this entity type.
   */
  protected $api_resource_path;

  /**
   * Helper module that maps AMP document properties to Drupal schema items.
   */
  public $schemaHelper;

  /**
   * {@inheritdoc}
   */
  public function __construct($entity_type) {
    parent::__construct($entity_type);

    // Get the API endpoint.
    if (empty($this->entityInfo['api resource path'])) {
      throw new ampAPIEntityQueryHelperException(t('Entity %entity has no "api resource path".', array('%entity' => $this->entityType)));
    }
    $this->api_resource_path = $this->entityInfo['api resource path'];

    $this->propertyInfo = entity_get_property_info($this->entityType);
  }

  /**
   * Overrides EntityAPIController::buildQuery().
   */
  protected function buildQuery($ids, $conditions = array(), $revision_id = FALSE) {
    // @see ampAPIEntityQueryHelper
    $query = new EntityFieldQuery();
    $query->entityCondition('entity_type', $this->entityType);

    // ???
    if (!empty($ids)) {
      if (count($ids) > 1) {
        $query->propertyCondition('entity_id', $ids);
      }
      else {
        $query->propertyCondition('entity_id', reset($ids));
      }
    }

    foreach ($conditions as $condition) {
      // Make sure we have the operator defined.
      $condition['operator'] = isset($condition['operator']) ? $condition['operator'] : NULL;
      $query->propertyCondition($condition['column'], $condition['value'], $condition['operator']);
    }

    return $query;
  }

  /**
   * Creates and saves a given entity.
   *
   * @see entity_create().
   * @see EntityAPIControllerInterface::create().
   */
  public function create(array $values = array()) {
    $entity = parent::create($values);
    unset($entity->is_new);

    return $entity;
  }

  /**
   * Loads one or more entities.
   *
   * @see entity_load().
   * @see DrupalEntityControllerInterface::load().
   */
  public function load($ids = array(), $conditions = array()) {
    $entities = array();

    // At he moment we don't support revisions.
    $revision_id = FALSE;

    // Create a new variable which is either a prepared version of the $ids
    // array for later comparison with the entity cache, or FALSE if no $ids
    // were passed. The $ids array is reduced as items are loaded from cache,
    // and we need to know if it's empty for this reason to avoid querying the
    // database when all requested entities are loaded from cache.
    $passed_ids = !empty($ids) ? array_flip($ids) : FALSE;

    // Try to load entities from the static cache.
    // if ($this->cache && !$revision_id) {
    //   $entities = $this->cacheGet($ids, $conditions);
    //   // If any entities were loaded, remove them from the ids still to load.
    //   if ($passed_ids) {
    //     $ids = array_keys(array_diff_key($passed_ids, $entities));
    //   }
    // }

    // Load any remaining entities from the database. This is the case if $ids
    // is set to FALSE (so we load all entities), if there are any ids left to
    // load, if loading a revision, or if $conditions was passed without $ids.
    if ($ids === FALSE || $ids || $revision_id || ($conditions && !$passed_ids)) {
      // Build the query.
      $query = $this->buildQuery($ids, $conditions, $revision_id);

      // Get the entities.
      $queried_entities = $query->execute();
    }

    // Pass all entities loaded from the database through $this->attachLoad(),
    // which attaches fields (if supported by the entity type) and calls the
    // entity type specific load callback, for example hook_node_load().
    if (!empty($queried_entities[$this->entityType])) {
      $entities += $queried_entities[$this->entityType];
    }

    // Statically cache the entities.
    $this->cacheSet($entities);

    return $entities;
  }

  /**
   * Delete permanently saved entities.
   *
   * @see entity_delete().
   * @see EntityAPIControllerInterface::delete().
   */
  public function delete($ids) {
    // We use a read-only API.
    return FALSE;
  }

  /**
   * Permanently saves the given entity.
   *
   * @see entity_save().
   * @see EntityAPIControllerInterface::save().
   */
  public function save($entity) {
    // We use a read-only API.
    return FALSE;
  }

  /**
   * Generate an array for rendering the given entities.
   *
   * @param $entities
   *   An array of entities to render.
   * @param $view_mode
   *   View mode, e.g. 'full', 'teaser'...
   * @param $langcode
   *   (optional) A language code to use for rendering. Defaults to the global
   *   content language of the current request.
   * @param $page
   *   (optional) If set will control if the entity is rendered: if TRUE
   *   the entity will be rendered without its title, so that it can be embeded
   *   in another context. If FALSE the entity will be displayed with its title
   *   in a mode suitable for lists.
   *   If unset, the page mode will be enabled if the current path is the URI
   *   of the entity, as returned by entity_uri().
   *   This parameter is only supported for entities which controller is a
   *   EntityAPIControllerInterface.
   * @return
   *   The renderable array, keyed by entity name or numeric id.
   */
  public function view($entities, $view_mode = 'full', $langcode = NULL, $page = NULL) {
    $build = parent::view($entities, $view_mode, $langcode, $page);
    return $build;
  }

  /**
   * Renturs a list information about AMP document fields.
   */
  public static function schemaInfo() {
    $info = array();

    $info['id'] = array(
      'mapper plugin' => 'activityId',
      'label' => 'Item ID',
      'description' => 'The unique remote ID of the Item.',
    );

    return $info;
  }

  /**
   * Renders a single entity property.
   */
  protected function renderEntityProperty($wrapper, $name, $property, $view_mode, $langcode, &$content) {
    // Render entity property if we don't use DS.
    $ds_layout = ds_get_layout($this->entityType, $wrapper->getBundle(), $view_mode);
    if (empty($ds_layout)) {
      parent::renderEntityProperty($wrapper, $name, $property, $view_mode, $langcode, $content);
    }
  }
}
