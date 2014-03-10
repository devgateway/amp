<?php if(!empty($date)) : ?>
  <div class="projects_as_of">
    <?php print t('Projects data is as of') . ': ' . $date; ?>
  </div>
<?php else: ?>
  <div class="projects_as_of">
    <?php print t('Projects data is as of') . ': ' . t('not available'); ?>
  </div>
<?php endif;?>