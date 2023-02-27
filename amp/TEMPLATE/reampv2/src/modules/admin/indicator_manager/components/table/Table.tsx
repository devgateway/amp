/* eslint-disable import/no-unresolved */
import React, { useEffect, useState } from 'react';
import {
  Col, Row, Button, Form
} from 'react-bootstrap';
import BootstrapTable, { PaginationOptions } from 'react-bootstrap-table-next';
import paginationFactory from 'react-bootstrap-table2-paginator';
import filterFactory, { FilterFactoryProps } from 'react-bootstrap-table2-filter';
import 'react-bootstrap-table2-filter/dist/react-bootstrap-table2-filter.min.css';
import ToolkitProvider, { Search, CSVExport, ToolkitContextType } from 'react-bootstrap-table2-toolkit';
import styles from './Table.module.css';
import AddNewIndicatorModal from '../modals/AddNewIndicatorModal';
import { DefaultComponentProps, SectorObjectType } from '../../types';

interface SkeletonTableProps extends DefaultComponentProps {
  columns: any;
  data: any;
  title: string;
  sectors?: SectorObjectType[];
  setSelectedSector: React.Dispatch<React.SetStateAction<number>>;
};

const SkeletonTable: React.FC<SkeletonTableProps> = (props) => {
  const { columns, data, title, sectors, setSelectedSector, translations } = props;

  const { SearchBar } = Search;
  const { ExportCSVButton } = CSVExport;

  const [showAddNewIndicatorModal, setShowAddNewIndicatorModal] = useState(false);

  const showAddNewIndicatorModalHandler = () => {
    setShowAddNewIndicatorModal(true);
  };

  useEffect(() => {
    setSelectedSector(0);
  }, [setSelectedSector]);

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
        text: translations['amp.indicatormanager:all'],
        value: data.length,
      }
    ],
    firstPageText: translations['amp.indicatormanager:first'],
    prePageText: translations['amp.indicatormanager:previous'],
    nextPageText: translations['amp.indicatormanager:next'],
    lastPageText: translations['amp.indicatormanager:last'],
    nextPageTitle: translations['amp.indicatormanager:next-page'],
    prePageTitle: translations['amp.indicatormanager:pre-page'],
    firstPageTitle: translations['amp.indicatormanager:first-page'],
    lastPageTitle: translations['amp.indicatormanager:last-page'],
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
      // console.log(result, column); 
    }
  };

  return (
    <>
      <AddNewIndicatorModal show={showAddNewIndicatorModal} setShow={setShowAddNewIndicatorModal} translations={translations} />
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
                        <Button type="primary" onClick={showAddNewIndicatorModalHandler}>
                          <i className="fa fa-plus" />
                          {' '}
                          <span>{translations['amp.dashboard:add-new']}</span>
                        </Button>
                        <ExportCSVButton
                          {...props.csvProps}
                          className={styles.export_button}
                        >
                          <i className="fa fa-download" />
                          {' '}
                          <span>{translations['amp.indicatormanager:export-csv']}</span>
                        </ExportCSVButton>
                      </div>

                    </Col>
                    {/* searchbar should be far right on the col */}
                    <Col sm={6}>
                      <div className={styles.table_header_bottom_right}>

                        <div className={styles.sector_filter_container}>
                          <Form.Label className={styles.filter_label}>{translations['amp.indicatormanager:sectors']}</Form.Label>
                          <Form.Control
                            onChange={(e) => setSelectedSector(e.target.value as unknown as number)}
                            as="select"
                            className={styles.filter_select}>
                            <option value="0">{translations['amp.indicatormanager:all-sectors']}</option>
                            {
                              sectors && sectors.length > 0 ?
                                sectors.map((sector) => (
                                  <option
                                    key={sector.id}
                                    value={sector.id}>
                                    {sector.name}
                                  </option>
                                )) : <option value="0">{translations['amp.indicatormanager:no-data']}</option>
                            }
                          </Form.Control>
                        </div>

                        <div className={styles.search_container}>
                          <SearchBar
                            {...props.searchProps}
                            placeholder={translations['amp.indicatormanager:search']}
                          />
                        </div>
                      </div>

                    </Col>
                  </Row>
                </Col>
                <hr />
                <BootstrapTable
                  {...props.baseProps}
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
                      <h5>{translations['amp.indicatormanager:no-data']}</h5>
                    </div>
                  )}
                />
              </div>
            )
          }
        </ToolkitProvider >

      </Col >
    </>

  );
};

export default SkeletonTable;
