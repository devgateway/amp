import Excel from 'exceljs';

/**
 *
 * Code from VIFAA project.
 */
export const dataDownloadStyles = {
  totalLabels: {
    alignment: {
      vertical: 'middle',
      horizontal: 'middle'
    },
    font: {
      name: 'Calibri',
      family: 4,
      bold: true
    },
    fill: {
      type: 'pattern',
      pattern: 'solid',
      fgColor: {
        argb: '00d9ead3'
      }
    }
  },
  totalNumber: {
    alignment: {
      vertical: 'middle',
      horizontal: 'right'
    },
    font: {
      name: 'Calibri',
      family: 4,
      bold: true
    },
    fill: {
      type: 'pattern',
      pattern: 'solid',
      fgColor: {
        argb: '00d9ead3'
      }
    }
  }
};

export const exportToXLS = (exportData) => {
  const workbook = new Excel.Workbook();
  let title;
  if (Array.isArray(exportData)) {
    exportData.forEach((sheetData, index) => {
      addWorkSheet(workbook, sheetData.sheetName ? sheetData.sheetName : `Sheet ${index}`, sheetData);
    });
    title = exportData[0].title;
  } else {
    title = exportData.title;
    addWorkSheet(workbook, exportData.sheetName ? exportData.sheetName : 'Sheet 1', exportData);
  }

  // TODO its coming from VIFAA need to adujst
  workbook.xlsx.writeBuffer().then((data) => {
    const blob = new Blob([data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
    const url = window.URL.createObjectURL(blob);
    const anchor = document.createElement('a');
    anchor.href = url;
    anchor.download = `${title}.xlsx`;
    anchor.click();
    window.URL.revokeObjectURL(url);
  });
};

const addWorkSheet = (workbook, sheetName, exportData) => {
  const {
    title, filters, columns, rows, source
  } = exportData;
  workbook.addWorksheet(sheetName);
  const worksheet = workbook.getWorksheet(sheetName);
  worksheet.mergeCells(1, 1, 1, columns.length);
  worksheet.getCell('A1').value = title;
  worksheet.getCell('A1').font = {
    name: 'Calibri', family: 4, size: 18, bold: true, color: { argb: '00FFFFFF' }
  };
  worksheet.getCell('A1').alignment = { vertical: 'middle', horizontal: 'center' };
  worksheet.getCell('A1').fill = {
    type: 'pattern',
    pattern: 'solid',
    fgColor: { argb: '00273142' }
  };
  worksheet.getCell('A1').border = {
    top: { style: 'thin' },
    left: { style: 'thin' },
    bottom: { style: 'thin' },
    right: { style: 'thin' }
  };
  worksheet.addRow();
  filters.forEach(filter => {
    if (filter.values.length > 0) {
      worksheet.addRow([filter.name, filter.values]);
      const { lastRow } = worksheet;
      lastRow.getCell(1).font = {
        name: 'Calibri', family: 4, size: 11, bold: true
      };
    }
  });

  worksheet.addRow();
  worksheet.columns = columns;
  worksheet.addRow();
  const row = worksheet.lastRow;
  columns.forEach(col => {
    row.getCell(col.key).value = col.headerTitle;
    row.getCell(col.key).font = {
      name: 'Calibri', family: 4, size: 11, bold: true
    };
    row.getCell(col.key).alignment = { vertical: 'middle', horizontal: 'center' };
    row.getCell(col.key).fill = {
      type: 'pattern',
      pattern: 'solid',
      fgColor: { argb: '00cccccc' }
    };
    row.getCell(col.key).border = {
      top: { style: 'thin' },
      left: { style: 'thin' },
      bottom: { style: 'thin' },
      right: { style: 'thin' }
    };
  });

  rows.forEach(innerRow => {
    worksheet.addRow(innerRow);
    const { lastRow } = worksheet;
    if (innerRow.cellsStyles) {
      // eslint-disable-next-line no-restricted-syntax
      for (const key in innerRow.cellsStyles) {
        // eslint-disable-next-line no-prototype-builtins
        if (innerRow.cellsStyles.hasOwnProperty(key)) {
          const styleCell = innerRow.cellsStyles[key];
          const cellToStyle = lastRow.getCell(key);
          // eslint-disable-next-line no-restricted-syntax
          for (const styleKey in styleCell) {
            // eslint-disable-next-line no-prototype-builtins
            if (styleCell.hasOwnProperty(styleKey)) {
              cellToStyle[styleKey] = styleCell[styleKey];
            }
          }
        }
      }
    }

    if (innerRow.mergeCells) {
      const rowNumber = worksheet.rowCount;
      worksheet.mergeCells(rowNumber, innerRow.mergeCells.start, rowNumber, innerRow.mergeCells.end);
    }
  });
  // worksheet.addRows(rows);

  worksheet.eachRow((innerRow, rowNumber) => {
    // add borders to data cells
    if (rowNumber > filters.length + 2) {
      columns.forEach(col => {
        innerRow.getCell(col.key).border = {
          top: { style: 'thin' },
          left: { style: 'thin' },
          bottom: { style: 'thin' },
          right: { style: 'thin' }
        };
      });
    }
  });

  if (source) {
    worksheet.addRow();
    worksheet.addRow([`Source: ${source}`]);
    worksheet.lastRow.font = {
      name: 'Calibri', family: 4, size: 11, bold: true
    };
  }
};

export const exportToCSV = exportData => {
  let title;
  let columns;
  let
    rows;
  if (Array.isArray(exportData)) {
    title = exportData[0].title;
    columns = exportData[0].columns;
    rows = [];
    exportData.forEach(data => {
      rows = [...rows, ...data.rows];
    });
  } else {
    title = exportData.title;
    columns = exportData.columns;
    rows = exportData.rows;
  }

  let csv = '';
  const headers = columns.map(c => c.headerTitle);
  csv += headers.join(',');
  csv += '\n';

  const cols = columns.map(c => c.key);
  rows.forEach((row) => {
    const rowData = [];
    cols.forEach(column => {
      if (row[column]) {
        rowData.push(row[column].toString().split(',').join(''));
      } else {
        rowData.push('');
      }
    });
    csv += rowData.join(',');
    csv += '\n';
  });

  // console.log(csv);
  const anchor = document.createElement('a');
  anchor.href = `data:text/csv;charset=utf-8,${encodeURI(csv)}`;
  anchor.target = '_blank';
  anchor.download = `${title}.csv`;
  anchor.click();
};

export function populateFilters(translations, filters, selectedFilters) {
  const selectedFiltersForExport = [];

  const { countries } = filters.countries;
  const { sectors } = filters.sectors;
  const { modalities } = filters.modalities;
  const {
    selectedCountries, selectedYears, selectedSectors, selectedModalities
  } = selectedFilters;
  if (selectedCountries && selectedCountries.length > 0) {
    selectedFiltersForExport.push({
      name: translations['amp.ssc.dashboard:Country'],
      values: selectedCountries.map(sc => countries.find(c => c.id === sc).name).join(' | ')
    });
  }
  if (selectedYears && selectedYears.length > 0) {
    selectedFiltersForExport.push({
      name: translations['amp.ssc.dashboard:Year'],
      values: selectedYears.join(' | ')
    });
  }
  if (selectedSectors && selectedSectors.length > 0) {
    selectedFiltersForExport.push({
      name: translations['amp.ssc.dashboard:Sector'],
      values: selectedSectors.map(sc => sectors.find(c => c.id === sc).name).join(' | ')
    });
  }
  if (selectedModalities && selectedModalities.length > 0) {
    selectedFiltersForExport.push({
      name: translations['amp.ssc.dashboard:Modalities'],
      values: selectedModalities.map(sc => modalities.find(c => c.id === sc).name).join(' | ')
    });
  }

  return selectedFiltersForExport;
}
