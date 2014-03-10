<?php if(!empty($commitments_report)): ?>
<table class="commitments_report">
  <tr class="commitments_report-header">
    <td class="commitments_report-column-1"><?php print t('Donor Group');?></td>
    <td class="commitments_report-column-2"><?php print t('Total Costs');?></td>
  </tr>
  <?php $class = 'even'; ?>
  <?php for($i = 0; $i < sizeof($commitments_report); $i++):?>
  <?php
    $class = ($i % 2 == 0) ? 'even' : 'odd';
  ?>
  <tr class="commitments_report-row-<?php print $class?>">
    <td class="commitments_report-column-1"><?php print "{$commitments_report[$i]['name']} ({$commitments_report[$i]['count']}) ";?></td>
    <td class="commitments_report-column-2"><?php print "{$commitments_report[$i]['amount']}" . $curr_symbol;?></td>
  </tr>

  <?php endfor;?>
  <tr class="commitments_report-row-<?php print $class?>">
    <td class="commitments_report-column-1"><?php print t('TOTAL (!total)', array('!total' => $total_org_count))?></td>
    <td class="commitments_report-column-2"><?php print $total_amount . $curr_symbol;?></td>
  </tr>
</table>
<?php endif;?>