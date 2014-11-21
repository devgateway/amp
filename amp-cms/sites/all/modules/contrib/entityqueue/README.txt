
CONTENTS OF THIS FILE
---------------------

 * About Entityqueue
 * Entityqueue API
 * Dependencies
 * Configuration and features

ABOUT ENTITYQUEUE
-----------------

The Entityqueue module allows users to create queues of any entity type. Each
queue is implemented as an Entityreference field, that can hold a single entity
type. For instance you can create a queue of:

 * Nodes
 * Users
 * Taxonomy Terms

Entityqueue provides Views integration, by adding an Entityqueue relationship to
your view, and adding a sort for Entityqueue position.

ENTITYQUEUE API
---------------

Entityqueue uses ctools plugin handlers for each queue. When creating a queue,
the user selects the handler to use for that queue. Entityqueue provides two
handlers by default, the "Simple queue" and "Multiple subqueues" handlers. Other
modules can provide their own handlers to alter the queue behavior.

Entityqueues are also exportable using ctools exportables.

DEPENDENCIES
------------

 * Drupal 7.12 or newer
 * Entity API
 * Entity reference
 * Chaos tool suite (ctools)
