<?php
/**
 * @file
 * Template file for update script log entries.
 */
?>
<h4><?php print $back_link; ?></h4>

<h3><?php print t('General infomation'); ?></h3>
<?php print $general_information; ?>

<?php if (!is_null($queries)): ?>
  <h3><?php print t('SQL queries executed'); ?></h3>
  <?php print $queries; ?>
<?php endif; ?>

<?php if (!is_null($errors)): ?>
  <h3><?php print t('Errors raised'); ?></h3>
  <?php print $errors; ?>
<?php endif; ?>

