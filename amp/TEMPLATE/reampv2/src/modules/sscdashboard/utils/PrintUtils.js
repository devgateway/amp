import domtoimage from 'dom-to-image-font-patch';
import { COUNTRY_COLUMN, PNG_FORMAT } from './constants';

export const printInnerCharts = (title, chartId, filtersObject, format, countriesForExport) => {
  const rows = document.getElementById(chartId).childNodes;
  const chartsIds = [];
  rows.forEach(r => {
    r.childNodes.forEach(c => {
      if (countriesForExport.includes(parseInt(c.id.substring('country-column'.length, c.id.length), 10))) {
        chartsIds.push(c.id);
      }
    });
  });
  chartsIds.reduce((previousPromise, nextChartID) => previousPromise
    .then(() => printChart(title, nextChartID, [], format, false, 'print-simple-dummy-container')), Promise.resolve());
};

export const printChartPrinter = (title, chartId, printContainer, iframe, countriesForExport) => {
  try {
    let oneByOne = false;
    if (countriesForExport && countriesForExport.length > 0) {
      oneByOne = true;
    }
    const doc = iframe ? iframe.contentDocument : document;
    const printElement = doc.getElementById(printContainer);
    if (oneByOne) {
      printElement.className = `${printElement.className} one-by-one`;
    }
    const charts = document.getElementsByClassName('chart-column');

    let titleElement;
    if (title) {
      titleElement = doc.createElement('div');
      titleElement.className = 'print-title';
      titleElement.appendChild(doc.createTextNode(title));
      if (!oneByOne) {
        printElement.appendChild(titleElement);
      }
    }
    let totalRows;
    if (!oneByOne) {
      totalRows = Math.ceil(charts.length / 2);
    } else {
      totalRows = charts.length;
    }
    let rowCount = 1;
    for (let i = 0; i < charts.length;) {
      const col1 = charts[i].cloneNode(true);
      if (!oneByOne || countriesForExport.includes(parseInt(col1.id.substring(COUNTRY_COLUMN.length), 10))) {
        col1.style.height = charts[i].clientHeight;
        col1.style.width = charts[i].clientWidth;
        i += 1;
        let col2;
        if (i < charts.length && !oneByOne) {
          col2 = charts[i].cloneNode(true);
          col2.style.height = charts[i].clientHeight;
          col2.style.width = charts[i].clientWidth;
          i += 1;
        }
        const theRow = doc.createElement('div');
        // eslint-disable-next-line no-nested-ternary
        theRow.className = `print-row row${rowCount < totalRows ? ' border-bottom ' : (!oneByOne ? ' last-row' : '')}`;
        rowCount += 1;
        if (titleElement && oneByOne) {
          const newTitleElement = titleElement.cloneNode(true);
          printElement.appendChild(newTitleElement);
        }

        theRow.appendChild(col1);
        if (col2) {
          theRow.appendChild(col2);
          col1.className = 'chart-column col-print-6 border-right';
          col2.className = 'chart-column col-print-6';
        } else {
          col1.className = 'chart-column col-print-12';
        }
        printElement.appendChild(theRow);
        if (oneByOne) {
          const pageBreak = doc.createElement('p');
          pageBreak.className = 'new-page';
          printElement.appendChild(pageBreak);
        }
      } else {
        i += 1;
        rowCount += 1;
      }
    }
  } catch (ex) {
    console.log(`problem creating elements for print: ${ex}`);
  }
};

/**
 * Original code from VIFAA
 * @param title
 * @param chartId
 * @param filtersObject
 * @param format
 * @param calculateChildren
 * @param printContainer
 */
export const printChart = (title, chartId, filtersObject, format, calculateChildren, printContainer, useTitle) => {
  try {
    let printElement = document.getElementById(printContainer).firstChild;
    if (!printElement) {
      printElement = document.getElementById(printContainer);
    }
    const chartElement = document.getElementById(chartId).cloneNode(true);
    let chartHeight = `${document.getElementById(chartId).clientHeight}px`;
    if (calculateChildren) {
      chartHeight = 0;
      document.getElementById(chartId).childNodes.forEach(c => {
        chartHeight += c.clientHeight;
      });
    }

    chartElement.style.height = chartHeight;
    chartElement.style.width = `${document.getElementById(chartId).clientWidth}px`;

    let titleElement;
    if (title && useTitle) {
      titleElement = document.createElement('div');
      titleElement.className = 'print-title';
      titleElement.appendChild(document.createTextNode(title));
      printElement.appendChild(titleElement);
    }

    printElement.appendChild(chartElement);
    const height = chartElement.clientHeight + 10 + (titleElement ? titleElement.clientHeight : 0);
    return doPrint(printElement, titleElement, chartElement, format,
      title, height, chartElement.clientWidth);
  } catch (ex) {
    debugger;
    console.log(`problem creating elements for print: ${ex}`);
  }
};
const doPrint = (printElement, titleElement, chartElement, format, title, height, width) => {
  const printMethod = format === PNG_FORMAT ? domtoimage.toPng : domtoimage.toSvg;
  return new Promise((resolve, reject) => {
    printMethod(printElement, {
      height,
      width,
      bgcolor: '#FFF'
    })
      .then((dataUrl) => {
        const link = document.createElement('a');
        link.download = `${title}.${format}`;
        link.href = dataUrl;
        document.body.appendChild(link);
        link.click();

        if (titleElement) {
          printElement.removeChild(titleElement);
        }
        printElement.removeChild(chartElement);
        return resolve();
      }).catch((e) => {
        printElement.removeChild(chartElement);
        if (titleElement) {
          printElement.removeChild(titleElement);
        }
        printElement.removeChild(chartElement);
        reject(new Error(e));
      });
  });
};
