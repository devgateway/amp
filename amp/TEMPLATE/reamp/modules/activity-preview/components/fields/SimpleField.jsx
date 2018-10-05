import React, { Component } from 'react';
require('../../styles/ActivityView.css');

/**
 *    
 */
export default class SimpleField extends Component {


  /**
   * Gets an instance of Simple Field
   * @param title the label to translate and use as a title. This label is also used as the component key.
   * @param value the field value
   * @param inline show title and value in the same row.
   * @param separator add or not an <hr> tag.
   * @return {SimpleField}
   */
  static instance(title, value, useInnerHTML=false, inline = false, separator = false, nameClass, valueClass) {
    return (<SimpleField
      title={title} value={value} useInnerHTML={useInnerHTML} inline={inline} separator={separator}
      fieldNameClass={nameClass} fieldValueClass={valueClass} />);
  }

  constructor(props) {
    super(props);
    this.useSeparator = this.props.separator !== false;
    this.displayClass = this.props.fieldClass || (this.props.inline === true ? 'inline' : 'block');
  }

  _getValue() {
    const classNames = `${this.props.fieldValueClass} ${this.displayClass}`;
    const value = this.props.value ? this.props.value : 'No Data';
    let displayValue;
    if (Array.isArray(value) && value.length > 1 && typeof value[0] === 'string') {
      // Improve the display of simple array of strings.
      displayValue = value.map((i) => (` ${i}`)).toString();
    } else {
      displayValue = (this.props.inline && this.props.value instanceof String) ? `${value} ` : value;
    }
    
    if (this.props.useInnerHTML) {
      return <div className={classNames} dangerouslySetInnerHTML={{ __html: displayValue }} />;
    } else {
      return <div className={classNames}>{displayValue}</div>;
    }
    
  }

  render() {
    const classNames = `${this.props.fieldNameClass} ${this.displayClass}`;
    return (<div className={this.displayClass}>
      <div className={classNames}>{this.props.title}</div> {this._getValue()}
      {this.useSeparator ? <hr /> : ''}
    </div>);
  }
}
