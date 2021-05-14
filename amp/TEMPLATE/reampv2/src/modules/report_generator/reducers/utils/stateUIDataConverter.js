import { revertTotalGrouping } from '../../utils/Utils';

/**
 * IMPORTANT: KEEP THIS FUNCTIONS PURE BECAUSE THEY WILL BE USED INSIDE stateUIReducer.js!!!
 * https://reactjs.org/docs/components-and-props.html
 * @param data
 */

export function convertReportDetails(reportDetails, data) {
  const _reportDetails = { ...reportDetails };
  _reportDetails.description = data.description;
  _reportDetails.name = data.name;
  _reportDetails.selectedShowOriginalCurrencies = data.showOriginalCurrency;
  _reportDetails.selectedTotalGrouping = revertTotalGrouping(data.groupingOption);
  _reportDetails.selectedSummaryReport = data.summary;
  _reportDetails.selectedSplitByFunding = data.splitByFunding;
  _reportDetails.selectedAllowEmptyFundingColumns = data.allowEmptyFundingColumns;
  _reportDetails.selectedAlsoShowPledges = data.alsoShowPledges;
  return _reportDetails;
}

export function convertColumns(columns, data) {
  const _columns = { ...columns };
  _columns.selected = data.columns.sort((i, j) => i.orderId > j.orderId).map(i => i.id);
  return _columns;
}

export function convertHierarchies(hierarchies, data, columns) {
  const _hierarchies = { ...hierarchies };
  _hierarchies.available = columns.available.filter(i => data.hierarchies.find(j => j.id === i.id));
  _hierarchies.selected = data.hierarchies.sort((i, j) => i.levelId > j.levelId).map(i => i.id);
  // Note: Dont do _hierarchies.order = _hierarchies.selected because it will break the hierarchies selection.
  _hierarchies.order = data.hierarchies.sort((i, j) => i.levelId > j.levelId).map(i => i.id);
  return _hierarchies;
}

export function convertMeasures(measures, data) {
  const _measures = { ...measures };
  _measures.selected = data.measures.sort((i, j) => i.orderId > j.orderId).map(i => i.id);
  _measures.order = _measures.selected;
  return _measures;
}
