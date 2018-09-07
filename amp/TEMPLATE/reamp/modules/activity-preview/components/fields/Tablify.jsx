import React, { Component } from 'react';
require('../../styles/ActivityView.css');

export default class Tablify extends Component {

  /**
   * Takes an array and turns it into table rows and columns.
   * @param keyName key for tr and td inside  table
   * @param content content for table
   * @param cols number of columns
   */
  static addRows(keyName, content, cols) {
    if (content) {
      // Remove undefined cells.
      content = content.filter(c => c);
      // Decrease number of cols if we dont have enough elements.
      if (content.length < cols) {
        cols = content.length;
      }
      const rows = Math.ceil(content.length / cols);
      const tableContent = [];
      for (let i = 0; i < rows; i++) {
        let trKey = keyName + '-' + i;
        const rowContent = [];
        for (let j = 0; j < cols; j++) {
          let tdKey = trKey + '-' + j;
          let cellValue = content.pop();
          if (cellValue) {
            rowContent.push(<td key={tdKey}>{cellValue}</td>);
          }
        }
        tableContent.push(<tr key={trKey}>{rowContent}</tr>);
      }
      return tableContent;
    } else {
      return '';
    }
  }

  constructor(props) {
    super(props);
  }

  render() {
    if (this.props.content) {
      const columns = this.props.columns >= this.props.content.length ? this.props.content.length : this.props.columns;
      const cellWidth = `${(100 / columns)}%`;
      const cellwidthStyle = {
        width: cellWidth
      };
      const rows = Math.ceil(this.props.content.length / columns);
      const tableContent = [];
      for (let i = 0; i < rows; i++) {
        const rowContent = [];
        rowContent.push(<div style={cellwidthStyle} className={'tablify_outer_cell'}>
          {this.props.content.pop()}</div>);
        for (let j = 1; j < this.props.columns && this.props.content.length > 0; j++) {
          rowContent.push(<div style={cellwidthStyle} className={'tablify_inner_cell'}>
            {this.props.content.pop()}</div>);
        }
        tableContent.push(<div className={'flex'}>{rowContent}</div>);
      }
      return (<div>
        {tableContent}
      </div>);
    } else {
      return (<div></div>);
    }
  }
}
