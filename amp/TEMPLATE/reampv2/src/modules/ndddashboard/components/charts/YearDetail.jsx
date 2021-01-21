import React, { Component } from 'react';
import { Button } from 'react-bootstrap';
import Modal from 'react-bootstrap-modal';
import PropTypes from 'prop-types';
import { TRN_PREFIX } from '../../utils/constants';
import { formatNumberWithSettings } from '../../utils/Utils';

const MAX_RECORDS = 50;

export default class YearDetail extends Component {
  constructor(props) {
    super(props);
    this.state = { recordsVisible: MAX_RECORDS };
  }

  createTable = () => {
    const {
      data, translations, fundingType, currencyCode, globalSettings
    } = this.props;
    const { recordsVisible } = this.state;
    const rows = [];
    data.forEach((i, j) => {
      rows.push(
        <tr className={j >= recordsVisible ? 'invisible-row' : ''}>
          <td>
            <a
              target="_blank"
              rel="noreferrer noopener"
              title={i.projectTitle}
              href={`/aim/viewActivityPreview.do~activityId=${i.id}`}>
              {i.projectTitle}
            </a>
          </td>
          <td className="amount-column">
            {formatNumberWithSettings(translations, globalSettings, i.amount, false)}
          </td>
        </tr>
      );
    });

    return (
      <div>
        <table className="table table-striped">
          <thead>
            <tr>
              <th className="header-row"><span>{translations[`${TRN_PREFIX}project-title`]}</span></th>
              <th className="header-row">
                <span>
                  {`${fundingType} (${translations[globalSettings.numberDividerDescriptionKey]} ${currencyCode})`}
                </span>
              </th>
            </tr>
          </thead>
          <tbody>{rows}</tbody>
        </table>
        {recordsVisible < rows.length
          ? (
            <Button variant="success" onClick={this.loadMore}>
              {`${`${translations[`${TRN_PREFIX}load-more`]} ${recordsVisible}`}/${rows.length}`}
            </Button>
          )
          : null}
      </div>
    );
  }

  loadMore = () => {
    const { recordsVisible } = this.state;
    this.setState({ recordsVisible: recordsVisible + MAX_RECORDS });
  }

  handleClose = () => {
    const { handleClose } = this.props;
    this.setState({ recordsVisible: MAX_RECORDS });
    handleClose();
  }

  render() {
    const {
      translations, show, data, loading, error, title
    } = this.props;
    return (
      <Modal show={show} onHide={this.handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>{title}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {loading
            ? <div className="loading" style={{ top: '5%', marginTop: '5px' }} />
            : null}
          {!loading && (!data || data.length === 0) && !error
            ? <span>{translations[`${TRN_PREFIX}no-data`]}</span>
            : null}
          {data && data.length > 0 ? <div className="detail-table-container">{this.createTable()}</div> : null}
          {error ? <span className="error">{JSON.stringify(error)}</span> : null}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={this.handleClose}>
            {translations[`${TRN_PREFIX}close`]}
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }
}

YearDetail.propTypes = {
  translations: PropTypes.object.isRequired,
  show: PropTypes.bool.isRequired,
  handleClose: PropTypes.func.isRequired,
  data: PropTypes.array.isRequired,
  loading: PropTypes.bool.isRequired,
  error: PropTypes.object.isRequired,
  fundingType: PropTypes.string.isRequired,
  currencyCode: PropTypes.string.isRequired,
  globalSettings: PropTypes.object.isRequired,
  title: PropTypes.string.isRequired
};
