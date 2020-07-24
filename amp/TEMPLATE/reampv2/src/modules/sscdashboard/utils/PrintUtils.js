import domtoimage from 'dom-to-image-font-patch';
import { PNG_FORMAT } from './constants';

export const printInnerCharts = (title, chartId, filtersObject, format, countriesForExport) => {
    const rows = document.getElementById(chartId).childNodes;
    const chartsIds = [];
    rows.forEach(r => {
        r.childNodes.forEach(c => {
            if (countriesForExport.includes(parseInt(c.id.substring("country-column".length, c.id.length)))) {
                chartsIds.push(c.id);
            }
        });
    });
    chartsIds.reduce((previousPromise, nextChartID) => {
        return previousPromise.then(() => {
            return printChart(title, nextChartID, [], format, false, 'print-simple-dummy-container');
        });
    }, Promise.resolve());
};
export const printChartPrinter = (title, chartId, printContainer, iframe) => {
    try {
        const doc = iframe ? iframe.contentDocument : document;

        const printElement = doc.getElementById(printContainer);
        const charts = document.getElementsByClassName("chart-column");

        let titleElement;
        if (title) {
            titleElement = doc.createElement('div');
            titleElement.className = "print-title";
            titleElement.appendChild(doc.createTextNode(title));
            printElement.appendChild(titleElement);
        }
        const totalRows = Math.ceil(charts.length / 2);
        let rowCount = 1;
        for (let i = 0; i < charts.length;) {
            const col1 = charts[i].cloneNode(true);
            col1.style["height"] = charts[i].clientHeight;
            col1.style["width"] = charts[i].clientWidth;
            i++;
            let col2;
            if (i < charts.length) {
                col2 = charts[i].cloneNode(true);
                col2.style["height"] = charts[i].clientHeight;
                col2.style["width"] = charts[i].clientWidth;
                i++;
            }
            const theRow = doc.createElement('div');
            theRow.className = `print-row row${rowCount < totalRows ? ' border-bottom ' : ' last-row'}`;

            rowCount++;
            theRow.appendChild(col1);
            if (col2) {
                theRow.appendChild(col2)
                col1.className = 'chart-column col-print-6 border-right';
                col2.className = 'chart-column col-print-6';
            } else {
                col1.className = 'chart-column col-print-12';
            }
            printElement.appendChild(theRow);
        }
    } catch (ex) {
        console.log('problem creating elements for print: ' + ex);
    }
}

/**
 * Original code from VIFAA
 * @param title
 * @param chartId
 * @param filtersObject
 * @param format
 * @param calculateChildren
 */
export const printChart = (title, chartId, filtersObject, format, calculateChildren, printContainer) => {
    try {
        const printElement = document.getElementById(printContainer);

        const chartElement = document.getElementById(chartId).cloneNode(true);
        let chartHeight = document.getElementById(chartId).clientHeight + "px";
        if (calculateChildren) {
            chartHeight = 0;
            document.getElementById(chartId).childNodes.forEach(c => {
                chartHeight += c.clientHeight;
            })
        }

        chartElement.style["height"] = chartHeight;
        chartElement.style["width"] = document.getElementById(chartId).clientWidth + "px";

        let titleElement;
        if (title) {
            titleElement = document.createElement('div');
            titleElement.className = "print-title";
            titleElement.appendChild(document.createTextNode(title));
            printElement.appendChild(titleElement);
        }

        printElement.appendChild(chartElement);
        const height = chartElement.clientHeight + 10 + (titleElement ? titleElement.clientHeight : 0);
        return doPrint(printElement, titleElement, chartElement, format,
            title, height, chartElement.clientWidth)
    } catch (ex) {
        console.log('problem creating elements for print: ' + ex);
    }
}
const doPrint = (printElement, titleElement, chartElement, format, title, height, width) => {
    const printMethod = format === PNG_FORMAT ? domtoimage.toPng : domtoimage.toSvg
    return new Promise((resolve, reject) => {
        printMethod(printElement, {
            height: height,
            width: width,
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
        })
    });
};
