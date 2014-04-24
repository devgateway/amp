<?php if(!empty($quality_records)): ?>
<div class="data-quality">
  <div class="data-quality-header">
    <div class="data-quality-column-1"><?php print t('Donor');?></div>
    <div class="data-quality-column-2"><?php print t('Last update');?></div>
  </div>
  <?php $index = 1;?>
  <?php foreach ($quality_records as $name => $date): ?>
  <?php $class = ($index % 2 == 0) ? 'even' : 'odd';?>
  <div class="data-quality-row-<?php print $class?>">
    <div class="data-quality-column-1"><?php print $name;?></div>
    <div class="data-quality-column-2"><?php print $date;?></div>
  </div>
  <?php $index++;?>
  <?php endforeach;?>
</table>

<?php endif;?>