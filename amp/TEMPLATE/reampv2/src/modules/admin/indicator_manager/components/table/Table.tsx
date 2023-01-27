import React from 'react';
import {
  Col, Row, Button, Form
} from 'react-bootstrap';
import BootstrapTable, { PaginationOptions } from 'react-bootstrap-table-next';
import paginationFactory from 'react-bootstrap-table2-paginator';
import filterFactory, { FilterFactoryProps } from 'react-bootstrap-table2-filter';
import 'react-bootstrap-table2-filter/dist/react-bootstrap-table2-filter.min.css';
import ToolkitProvider, { Search, CSVExport, ToolkitContextType } from 'react-bootstrap-table2-toolkit';
// eslint-disable-next-line import/no-unresolved
// @ts-ignore
import overlayFactory from 'react-bootstrap-table2-overlay';
import styles from './Table.module.css';

const SkeletonTable = ({ columns, data, title }: any) => {
  const { SearchBar } = Search;
  const { ExportCSVButton } = CSVExport;

  // create a pagination factory
  const paginationOptions: PaginationOptions = {
    paginationSize: 4,
    pageStartIndex: 1,
    alwaysShowAllBtns: true,
    sizePerPageList: [
      {
        text: '10',
        value: 10,
      },
      {
        text: '25',
        value: 25,
      },
      {
        text: '50',
        value: 50,
      },
      {
        text: '100',
        value: 100,
      },
      {
        text: 'All',
        value: data.length,
      }
    ],
    firstPageText: 'First',
    prePageText: 'Back',
    nextPageText: 'Next',
    lastPageText: 'Last',
    nextPageTitle: 'First page',
    prePageTitle: 'Pre page',
    firstPageTitle: 'Next page',
    lastPageTitle: 'Last page',
    showTotal: true,
    sizePerPage: 10,
    hidePageListOnlyOnePage: true,
    sizePerPageRenderer: ({
      options,
      currSizePerPage,
      onSizePerPageChange
    }) => (
      <Row lg={4}>
        <Form.Control
          as="select"
          value={currSizePerPage}
          style={{
            width: 180,
            marginLeft: 20
          }}
          onChange={(e) => onSizePerPageChange(Number(e.target.value))}
        >
          {
          options.map((option) => (
            <option key={option.text} value={option.page}>
              {option.text}
            </option>
          ))
        }
        </Form.Control>
      </Row>

    )
  };

  const filterOptions: FilterFactoryProps = {
    afterFilter: (result: any, column: any) => {
      console.log('result', result);
      console.log('column', column);
    }
  };

  return (
    <Col sm={12}>
      <ToolkitProvider
        keyField="id"
        data={data}
        columns={columns}
        search
        exportCSV
      >
        {
          (props: ToolkitContextType) => (
            <div>
              <Col
                sm={12}
                style={{
                  paddingBottom: 15
                }}>
                <Row className={styles.table_header}>
                  <Col sm={6}>
                    <h3>{title}</h3>
                  </Col>
                  <Col sm={6}>
                    <hr />
                  </Col>
                </Row>

                <Row sm={12} className={styles.table_header_bottom}>
                  <Col sm={6}>
                    <div className={styles.table_header_bottom_left}>
                      <Button type="primary">
                        <i className="fa fa-plus" />
                        {' '}
                        <span>Add New Indicator</span>
                      </Button>
                      <ExportCSVButton
                        {...props.csvProps}
                        className={styles.export_button}
                      >
                        <Button style={{
                          backgroundColor: '#198754',
                        }}>
                          <i className="fa fa-download" />
                          {' '}
                          Export CSV
                        </Button>
                      </ExportCSVButton>
                    </div>

                  </Col>
                  {/* searchbar should be far right on the col */}
                  <Col sm={6}>
                    <div className={styles.table_header_bottom_right}>

                      <div className={styles.sector_filter_container}>
                        <Form.Label className={styles.filter_label}>Sectors</Form.Label>
                        <Form.Control as="select" className={styles.filter_select}>
                          <option value="all">All Sectors</option>
                          <option value="agriculture">Agriculture</option>
                          <option value="education">Education</option>
                          <option value="health">Health</option>
                          <option value="infrastructure">Infrastructure</option>
                          <option value="social protection">Social Protection</option>
                        </Form.Control>
                      </div>

                      <div className={styles.search_container}>
                        <SearchBar
                          {...props.searchProps}
                          placeholder="Search"
                        />
                      </div>
                    </div>

                  </Col>
                </Row>
              </Col>
              <hr />
              <BootstrapTable
                {...props.baseProps}
                bordered
                bootstrap4
                headerClasses={styles.table_header_titles}
                bodyClasses={styles.table_body}
                pagination={paginationFactory(paginationOptions)}
                selectRow={{
                  mode: 'checkbox',
                  clickToSelect: false,
                }}
                filter={filterFactory(filterOptions)}
                noDataIndication={() => (
                  <div className={styles.no_data}>
                    <h3>No Data Available</h3>
                  </div>
                )}
                overlay={overlayFactory({
                  spinner: true,
                  styles: {
                    overlay: (base: any) => ({ ...base, background: 'rgba(255, 0, 0, 0.5)' })
                  }
                })}
              />
            </div>
          )
        }
      </ToolkitProvider>

    </Col>

  );
};

export default SkeletonTable;
