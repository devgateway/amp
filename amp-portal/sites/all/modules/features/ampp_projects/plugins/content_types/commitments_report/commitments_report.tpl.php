<?php if(!empty($commitments_report)): ?>
<table class="commitments-report-table">
  <thead>
    <tr>
      <th class="column-one"><?php print t('Donor Group'); ?></th>
      <th class="column-two"><?php print t('Total Costs'); ?></th>
    </tr>
  </thead>

  <tbody>
    <?php for($i = 0; $i < sizeof($commitments_report); $i++): ?>
      <?php $class = ($i % 2 == 0) ? 'even' : 'odd'; ?>
      <tr class="row row-<?php print $class?>">
        <td class="column-one"><?php print "{$commitments_report[$i]['name']} ({$commitments_report[$i]['count']}) "; ?></td>
        <td class="column-two"><?php print "{$commitments_report[$i]['amount']}" . $currency_symbol; ?></td>
      </tr>
    <?php endfor; ?>
  </tbody>

  <tfoot>
    <tr>
      <th class="column-one"><?php print t('TOTAL (!total)', array('!total' => $total_entries_count)); ?></th>
      <th class="column-two"><?php print $total_amount . $currency_symbol; ?></th>
    </tr>
  </tfoot>
</table>
<?php endif;?>
