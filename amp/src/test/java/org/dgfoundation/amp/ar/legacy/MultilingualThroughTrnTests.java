package org.dgfoundation.amp.ar.legacy;

import org.dgfoundation.amp.testutils.ReportsTestCase;

import org.dgfoundation.amp.nireports.testcases.ColumnReportDataModel;
import org.dgfoundation.amp.nireports.testcases.GroupColumnModel;
import org.dgfoundation.amp.nireports.testcases.GroupReportModel;
import org.dgfoundation.amp.nireports.testcases.SimpleColumnModel;
import org.junit.Ignore;
import org.junit.Test;

import static org.dgfoundation.amp.testutils.ReportTestingUtils.MUST_BE_EMPTY;

public class MultilingualThroughTrnTests extends ReportsTestCase {

    @Test
    @Ignore
    public void testHierByImplLevel()
    {
        GroupReportModel fddr_correct = 
                GroupReportModel.withGroupReports("AMP-16666-by-impl-level",
                        GroupReportModel.withColumnReports("AMP-16666-by-impl-level",
                            ColumnReportDataModel.withColumns("Implementation Level: National",
                                SimpleColumnModel.withContents("Project Title", "date-filters-activity", "date-filters-activity"), 
                                SimpleColumnModel.withContents("Modalities", MUST_BE_EMPTY), 
                                SimpleColumnModel.withContents("Status", "date-filters-activity", "default status"), 
                                SimpleColumnModel.withContents("Financing Instrument", "date-filters-activity", "default financing instrument"), 
                                SimpleColumnModel.withContents("Mode of Payment", MUST_BE_EMPTY), 
                                SimpleColumnModel.withContents("Type Of Assistance", "date-filters-activity", "default type of assistance"), 
                                GroupColumnModel.withSubColumns("Funding",
                                    GroupColumnModel.withSubColumns("2009",
                                        GroupColumnModel.withSubColumns("Actual Commitments",
                                            SimpleColumnModel.withContents("Mode of Payment Unallocated", "date-filters-activity", "100 000"))), 
                                    GroupColumnModel.withSubColumns("2010",
                                        GroupColumnModel.withSubColumns("Actual Disbursements",
                                            SimpleColumnModel.withContents("Cash", MUST_BE_EMPTY), 
                                            SimpleColumnModel.withContents("Mode of Payment Unallocated", "date-filters-activity", "60 000"))), 
                                    GroupColumnModel.withSubColumns("2011",
                                        GroupColumnModel.withSubColumns("Actual Commitments",
                                            SimpleColumnModel.withContents("Mode of Payment Unallocated", MUST_BE_EMPTY))), 
                                    GroupColumnModel.withSubColumns("2012",
                                        GroupColumnModel.withSubColumns("Actual Commitments",
                                            SimpleColumnModel.withContents("Mode of Payment Unallocated", "date-filters-activity", "25 000")), 
                                        GroupColumnModel.withSubColumns("Actual Disbursements",
                                            SimpleColumnModel.withContents("Mode of Payment Unallocated", "date-filters-activity", "12 000"))), 
                                    GroupColumnModel.withSubColumns("2013",
                                        GroupColumnModel.withSubColumns("Actual Commitments",
                                            SimpleColumnModel.withContents("Cash", MUST_BE_EMPTY), 
                                            SimpleColumnModel.withContents("Direct payment", MUST_BE_EMPTY), 
                                            SimpleColumnModel.withContents("No Information", MUST_BE_EMPTY), 
                                            SimpleColumnModel.withContents("Reimbursable", MUST_BE_EMPTY)), 
                                        GroupColumnModel.withSubColumns("Actual Disbursements",
                                            SimpleColumnModel.withContents("Direct payment", MUST_BE_EMPTY), 
                                            SimpleColumnModel.withContents("Mode of Payment Unallocated", MUST_BE_EMPTY)))), 
                                GroupColumnModel.withSubColumns("Total Costs",
                                    SimpleColumnModel.withContents("Actual Commitments", "date-filters-activity", "125 000"), 
                                    SimpleColumnModel.withContents("Actual Disbursements", "date-filters-activity", "72 000")))
                            .withTrailCells(null, null, null, null, null, null, "100 000", "0", "60 000", "0", "25 000", "12 000", "0", "0", "0", "0", "0", "0", "125 000", "72 000"),
                            ColumnReportDataModel.withColumns("Implementation Level: Provincial",
                                SimpleColumnModel.withContents("Project Title", "crazy funding 1", "crazy funding 1", "Test MTEF directed", "Test MTEF directed", "Eth Water", "Eth Water", "TAC_activity_1", "TAC_activity_1", "ptc activity 1", "ptc activity 1", "TAC_activity_2", "TAC_activity_2", "ptc activity 2", "ptc activity 2", "SSC Project 1", "SSC Project 1", "SSC Project 2", "SSC Project 2"), 
                                SimpleColumnModel.withContents("Modalities", "Eth Water", "Diplomats and courses", "SSC Project 1", "Diplomats and courses", "SSC Project 2", "Virtual Platforms and blogs to consult, learn, and exchange ideas"), 
                                SimpleColumnModel.withContents("Status", "crazy funding 1", "default status", "Test MTEF directed", "default status", "Eth Water", "second status", "TAC_activity_1", "default status", "ptc activity 1", "default status", "TAC_activity_2", "default status", "ptc activity 2", "default status", "SSC Project 1", "default status", "SSC Project 2", "default status"), 
                                SimpleColumnModel.withContents("Financing Instrument", "crazy funding 1", "[default financing instrument, second financing instrument]", "Test MTEF directed", "default financing instrument", "Eth Water", "[default financing instrument, second financing instrument]", "ptc activity 1", "default financing instrument", "TAC_activity_1", "default financing instrument", "ptc activity 2", "default financing instrument", "TAC_activity_2", "default financing instrument", "SSC Project 1", "default financing instrument", "SSC Project 2", "default financing instrument"), 
                                SimpleColumnModel.withContents("Mode of Payment", "crazy funding 1", "[Cash, Direct payment]", "Test MTEF directed", "Cash", "Eth Water", "Direct payment", "ptc activity 1", "Reimbursable", "ptc activity 2", "No Information", "SSC Project 1", "Direct payment", "SSC Project 2", "Direct payment"), 
                                SimpleColumnModel.withContents("Type Of Assistance", "crazy funding 1", "[default type of assistance, second type of assistance]", "Test MTEF directed", "default type of assistance", "Eth Water", "[default type of assistance, second type of assistance]", "TAC_activity_1", "default type of assistance", "ptc activity 1", "default type of assistance", "ptc activity 2", "default type of assistance", "TAC_activity_2", "default type of assistance", "SSC Project 1", "default type of assistance", "SSC Project 2", "default type of assistance"), 
                                GroupColumnModel.withSubColumns("Funding",
                                    GroupColumnModel.withSubColumns("2009",
                                        GroupColumnModel.withSubColumns("Actual Commitments",
                                            SimpleColumnModel.withContents("Mode of Payment Unallocated", MUST_BE_EMPTY))), 
                                    GroupColumnModel.withSubColumns("2010",
                                        GroupColumnModel.withSubColumns("Actual Disbursements",
                                            SimpleColumnModel.withContents("Cash", "Test MTEF directed", "143 777"), 
                                            SimpleColumnModel.withContents("Mode of Payment Unallocated", "TAC_activity_1", "123 321", "TAC_activity_2", "453 213"))), 
                                    GroupColumnModel.withSubColumns("2011",
                                        GroupColumnModel.withSubColumns("Actual Commitments",
                                            SimpleColumnModel.withContents("Mode of Payment Unallocated", "TAC_activity_1", "213 231", "TAC_activity_2", "999 888"))), 
                                    GroupColumnModel.withSubColumns("2012",
                                        GroupColumnModel.withSubColumns("Actual Commitments",
                                            SimpleColumnModel.withContents("Mode of Payment Unallocated", MUST_BE_EMPTY)), 
                                        GroupColumnModel.withSubColumns("Actual Disbursements",
                                            SimpleColumnModel.withContents("Mode of Payment Unallocated", MUST_BE_EMPTY))), 
                                    GroupColumnModel.withSubColumns("2013",
                                        GroupColumnModel.withSubColumns("Actual Commitments",
                                            SimpleColumnModel.withContents("Cash", "crazy funding 1", "111 111"), 
                                            SimpleColumnModel.withContents("Direct payment", "crazy funding 1", "222 222", "SSC Project 1", "111 333", "SSC Project 2", "567 421"), 
                                            SimpleColumnModel.withContents("No Information", "ptc activity 2", "333 222"), 
                                            SimpleColumnModel.withContents("Reimbursable", "ptc activity 1", "666 777")), 
                                        GroupColumnModel.withSubColumns("Actual Disbursements",
                                            SimpleColumnModel.withContents("Direct payment", "SSC Project 1", "555 111", "SSC Project 2", "131 845"), 
                                            SimpleColumnModel.withContents("Mode of Payment Unallocated", "Eth Water", "545 000")))), 
                                GroupColumnModel.withSubColumns("Total Costs",
                                    SimpleColumnModel.withContents("Actual Commitments", "crazy funding 1", "333 333", "TAC_activity_1", "213 231", "ptc activity 1", "666 777", "TAC_activity_2", "999 888", "ptc activity 2", "333 222", "SSC Project 1", "111 333", "SSC Project 2", "567 421"), 
                                    SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "143 777", "Eth Water", "545 000", "TAC_activity_1", "123 321", "TAC_activity_2", "453 213", "SSC Project 1", "555 111", "SSC Project 2", "131 845")))
                            .withTrailCells(null, null, null, null, null, null, "0", "143 777", "576 534", "1 213 119", "0", "0", "111 111", "900 976", "333 222", "666 777", "686 956", "545 000", "3 225 205", "1 952 267"))
                        .withTrailCells(null, null, null, null, null, null, "100 000", "143 777", "636 534", "1 213 119", "25 000", "12 000", "111 111", "900 976", "333 222", "666 777", "686 956", "545 000", "3 350 205", "2 024 267"))
                    .withTrailCells(null, null, null, null, null, null, "100 000", "143 777", "636 534", "1 213 119", "25 000", "12 000", "111 111", "900 976", "333 222", "666 777", "686 956", "545 000", "3 350 205", "2 024 267")
                    .withPositionDigest(true,
                        "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1), RHLC Modalities: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 1, colSpan: 1), RHLC Status: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 2, colSpan: 1), RHLC Financing Instrument: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 3, colSpan: 1), RHLC Mode of Payment: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 4, colSpan: 1), RHLC Type Of Assistance: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 5, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 6, colSpan: 12), RHLC Total Costs: (startRow: 0, rowSpan: 3, totalRowSpan: 4, colStart: 18, colSpan: 2))",
                        "(line 1:RHLC 2009: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 6, colSpan: 1), RHLC 2010: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 7, colSpan: 2), RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 9, colSpan: 1), RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 10, colSpan: 2), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 12, colSpan: 6))",
                        "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 4), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 16, colSpan: 2))",
                        "(line 3:RHLC Mode of Payment Unallocated: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Cash: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Mode of Payment Unallocated: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Mode of Payment Unallocated: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Mode of Payment Unallocated: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Mode of Payment Unallocated: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Cash: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1), RHLC Direct payment: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1), RHLC No Information: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1), RHLC Reimbursable: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1), RHLC Direct payment: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1), RHLC Mode of Payment Unallocated: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1), RHLC Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1), RHLC Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1))");
    
        runReportTest("the most important trn-translated columns, by ToA, ENGLISH", "AMP-16666-by-impl-level", 
                new String[] {"Eth Water", "mtef activity 1", "mtef activity 2", "ptc activity 1", "ptc activity 2", "SSC Project 1", 
                "SSC Project 2", "TAC_activity_1", "TAC_activity_2", "date-filters-activity", 
                "Proposed Project Cost 1 - USD", "Proposed Project Cost 2 - EUR", "Pure MTEF Project", "Test MTEF directed", 
                "AMP-15967-activity-2", "AMP-15967-activity-1", "crazy funding 1", "activity with components", "Project with documents"}, 
                fddr_correct, null, "en");  

        fddr_correct = GroupReportModel.withGroupReports("AMP-16666-by-impl-level",
                GroupReportModel.withColumnReports("AMP-16666-by-impl-level",
                        ColumnReportDataModel.withColumns("Implementation Level: Национальный",
                            SimpleColumnModel.withContents("Project Title", "проект-фильтры-по-датам", "проект-фильтры-по-датам").setIsPledge(false), 
                            SimpleColumnModel.withContents("Modalities", MUST_BE_EMPTY).setIsPledge(false), 
                            SimpleColumnModel.withContents("Status", "проект-фильтры-по-датам", "первый статус").setIsPledge(false), 
                            SimpleColumnModel.withContents("Financing Instrument", "проект-фильтры-по-датам", "первичный инструмент финансирования").setIsPledge(false), 
                            SimpleColumnModel.withContents("Mode of Payment", MUST_BE_EMPTY).setIsPledge(false), 
                            SimpleColumnModel.withContents("Type Of Assistance", "проект-фильтры-по-датам", "первичный тип помощи").setIsPledge(false), 
                            GroupColumnModel.withSubColumns("Funding",
                                GroupColumnModel.withSubColumns("2009",
                                    GroupColumnModel.withSubColumns("Actual Commitments",
                                        SimpleColumnModel.withContents("Mode of Payment Unallocated", "проект-фильтры-по-датам", "100 000").setIsPledge(false))), 
                                GroupColumnModel.withSubColumns("2010",
                                    GroupColumnModel.withSubColumns("Actual Disbursements",
                                        SimpleColumnModel.withContents("Mode of Payment Unallocated", "проект-фильтры-по-датам", "60 000").setIsPledge(false), 
                                        SimpleColumnModel.withContents("Наличные", MUST_BE_EMPTY).setIsPledge(false))), 
                                GroupColumnModel.withSubColumns("2011",
                                    GroupColumnModel.withSubColumns("Actual Commitments",
                                        SimpleColumnModel.withContents("Mode of Payment Unallocated", MUST_BE_EMPTY).setIsPledge(false))), 
                                GroupColumnModel.withSubColumns("2012",
                                    GroupColumnModel.withSubColumns("Actual Commitments",
                                        SimpleColumnModel.withContents("Mode of Payment Unallocated", "проект-фильтры-по-датам", "25 000").setIsPledge(false)), 
                                    GroupColumnModel.withSubColumns("Actual Disbursements",
                                        SimpleColumnModel.withContents("Mode of Payment Unallocated", "проект-фильтры-по-датам", "12 000").setIsPledge(false))), 
                                GroupColumnModel.withSubColumns("2013",
                                    GroupColumnModel.withSubColumns("Actual Commitments",
                                        SimpleColumnModel.withContents("Компенсируемые", MUST_BE_EMPTY).setIsPledge(false), 
                                        SimpleColumnModel.withContents("Наличные", MUST_BE_EMPTY).setIsPledge(false), 
                                        SimpleColumnModel.withContents("Нет данных", MUST_BE_EMPTY).setIsPledge(false), 
                                        SimpleColumnModel.withContents("Прямая оплата", MUST_BE_EMPTY).setIsPledge(false)), 
                                    GroupColumnModel.withSubColumns("Actual Disbursements",
                                        SimpleColumnModel.withContents("Mode of Payment Unallocated", MUST_BE_EMPTY).setIsPledge(false), 
                                        SimpleColumnModel.withContents("Прямая оплата", MUST_BE_EMPTY).setIsPledge(false)))), 
                            GroupColumnModel.withSubColumns("Total Costs",
                                SimpleColumnModel.withContents("Actual Commitments", "проект-фильтры-по-датам", "125 000").setIsPledge(false), 
                                SimpleColumnModel.withContents("Actual Disbursements", "проект-фильтры-по-датам", "72 000").setIsPledge(false)))
                        .withTrailCells(null, null, null, null, null, null, "100 000", "60 000", "0", "0", "25 000", "12 000", "0", "0", "0", "0", "0", "0", "125 000", "72 000"),
                        ColumnReportDataModel.withColumns("Implementation Level: Провинциальный",
                            SimpleColumnModel.withContents("Project Title", "crazy funding 1", "crazy funding 1", "Тест направленных МТЕФ", "Тест направленных МТЕФ", "Вода Eth", "Вода Eth", "Проект_TAC_1", "Проект_TAC_1", "Проект PTC 1", "Проект PTC 1", "Проект_TAC_2", "Проект_TAC_2", "Проект PTC 2", "Проект PTC 2", "Проект КЮЮ 1", "Проект КЮЮ 1", "Проект КЮЮ 2", "Проект КЮЮ 2").setIsPledge(false), 
                            SimpleColumnModel.withContents("Modalities", "Вода Eth", "Дипломаты и курсы", "Проект КЮЮ 1", "Дипломаты и курсы", "Проект КЮЮ 2", "Виртуальные Платформы и блоги проконсультироваться, узнать, и обмениваться идеями").setIsPledge(false), 
                            SimpleColumnModel.withContents("Status", "crazy funding 1", "первый статус", "Тест направленных МТЕФ", "первый статус", "Вода Eth", "второй статус", "Проект_TAC_1", "первый статус", "Проект PTC 1", "первый статус", "Проект_TAC_2", "первый статус", "Проект PTC 2", "первый статус", "Проект КЮЮ 1", "первый статус", "Проект КЮЮ 2", "первый статус").setIsPledge(false), 
                            SimpleColumnModel.withContents("Financing Instrument", "crazy funding 1", "[вторичный инструмент финансирования, первичный инструмент финансирования]", "Тест направленных МТЕФ", "первичный инструмент финансирования", "Вода Eth", "[вторичный инструмент финансирования, первичный инструмент финансирования]", "Проект PTC 1", "первичный инструмент финансирования", "Проект_TAC_1", "первичный инструмент финансирования", "Проект PTC 2", "первичный инструмент финансирования", "Проект_TAC_2", "первичный инструмент финансирования", "Проект КЮЮ 1", "первичный инструмент финансирования", "Проект КЮЮ 2", "первичный инструмент финансирования").setIsPledge(false), 
                            SimpleColumnModel.withContents("Mode of Payment", "crazy funding 1", "[Наличные, Прямая оплата]", "Тест направленных МТЕФ", "Наличные", "Вода Eth", "Прямая оплата", "Проект PTC 1", "Компенсируемые", "Проект PTC 2", "Нет данных", "Проект КЮЮ 1", "Прямая оплата", "Проект КЮЮ 2", "Прямая оплата").setIsPledge(false), 
                            SimpleColumnModel.withContents("Type Of Assistance", "crazy funding 1", "[вторичный тип помощи, первичный тип помощи]", "Тест направленных МТЕФ", "первичный тип помощи", "Вода Eth", "[вторичный тип помощи, первичный тип помощи]", "Проект_TAC_1", "первичный тип помощи", "Проект PTC 1", "первичный тип помощи", "Проект PTC 2", "первичный тип помощи", "Проект_TAC_2", "первичный тип помощи", "Проект КЮЮ 1", "первичный тип помощи", "Проект КЮЮ 2", "первичный тип помощи").setIsPledge(false), 
                            GroupColumnModel.withSubColumns("Funding",
                                GroupColumnModel.withSubColumns("2009",
                                    GroupColumnModel.withSubColumns("Actual Commitments",
                                        SimpleColumnModel.withContents("Mode of Payment Unallocated", MUST_BE_EMPTY).setIsPledge(false))), 
                                GroupColumnModel.withSubColumns("2010",
                                    GroupColumnModel.withSubColumns("Actual Disbursements",
                                        SimpleColumnModel.withContents("Mode of Payment Unallocated", "Проект_TAC_1", "123 321", "Проект_TAC_2", "453 213").setIsPledge(false), 
                                        SimpleColumnModel.withContents("Наличные", "Тест направленных МТЕФ", "143 777").setIsPledge(false))), 
                                GroupColumnModel.withSubColumns("2011",
                                    GroupColumnModel.withSubColumns("Actual Commitments",
                                        SimpleColumnModel.withContents("Mode of Payment Unallocated", "Проект_TAC_1", "213 231", "Проект_TAC_2", "999 888").setIsPledge(false))), 
                                GroupColumnModel.withSubColumns("2012",
                                    GroupColumnModel.withSubColumns("Actual Commitments",
                                        SimpleColumnModel.withContents("Mode of Payment Unallocated", MUST_BE_EMPTY).setIsPledge(false)), 
                                    GroupColumnModel.withSubColumns("Actual Disbursements",
                                        SimpleColumnModel.withContents("Mode of Payment Unallocated", MUST_BE_EMPTY).setIsPledge(false))), 
                                GroupColumnModel.withSubColumns("2013",
                                    GroupColumnModel.withSubColumns("Actual Commitments",
                                        SimpleColumnModel.withContents("Компенсируемые", "Проект PTC 1", "666 777").setIsPledge(false), 
                                        SimpleColumnModel.withContents("Наличные", "crazy funding 1", "111 111").setIsPledge(false), 
                                        SimpleColumnModel.withContents("Нет данных", "Проект PTC 2", "333 222").setIsPledge(false), 
                                        SimpleColumnModel.withContents("Прямая оплата", "crazy funding 1", "222 222", "Проект КЮЮ 1", "111 333", "Проект КЮЮ 2", "567 421").setIsPledge(false)), 
                                    GroupColumnModel.withSubColumns("Actual Disbursements",
                                        SimpleColumnModel.withContents("Mode of Payment Unallocated", "Вода Eth", "545 000").setIsPledge(false), 
                                        SimpleColumnModel.withContents("Прямая оплата", "Проект КЮЮ 1", "555 111", "Проект КЮЮ 2", "131 845").setIsPledge(false)))), 
                            GroupColumnModel.withSubColumns("Total Costs",
                                SimpleColumnModel.withContents("Actual Commitments", "crazy funding 1", "333 333", "Проект_TAC_1", "213 231", "Проект PTC 1", "666 777", "Проект_TAC_2", "999 888", "Проект PTC 2", "333 222", "Проект КЮЮ 1", "111 333", "Проект КЮЮ 2", "567 421").setIsPledge(false), 
                                SimpleColumnModel.withContents("Actual Disbursements", "Тест направленных МТЕФ", "143 777", "Вода Eth", "545 000", "Проект_TAC_1", "123 321", "Проект_TAC_2", "453 213", "Проект КЮЮ 1", "555 111", "Проект КЮЮ 2", "131 845").setIsPledge(false)))
                        .withTrailCells(null, null, null, null, null, null, "0", "576 534", "143 777", "1 213 119", "0", "0", "666 777", "111 111", "333 222", "900 976", "545 000", "686 956", "3 225 205", "1 952 267"))
                    .withTrailCells(null, null, null, null, null, null, "100 000", "636 534", "143 777", "1 213 119", "25 000", "12 000", "666 777", "111 111", "333 222", "900 976", "545 000", "686 956", "3 350 205", "2 024 267"))
                .withTrailCells(null, null, null, null, null, null, "100 000", "636 534", "143 777", "1 213 119", "25 000", "12 000", "666 777", "111 111", "333 222", "900 976", "545 000", "686 956", "3 350 205", "2 024 267")
                .withPositionDigest(true,
                    "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1), RHLC Modalities: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 1, colSpan: 1), RHLC Status: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 2, colSpan: 1), RHLC Financing Instrument: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 3, colSpan: 1), RHLC Mode of Payment: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 4, colSpan: 1), RHLC Type Of Assistance: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 5, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 6, colSpan: 12), RHLC Total Costs: (startRow: 0, rowSpan: 3, totalRowSpan: 4, colStart: 18, colSpan: 2))",
                    "(line 1:RHLC 2009: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 6, colSpan: 1), RHLC 2010: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 7, colSpan: 2), RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 9, colSpan: 1), RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 10, colSpan: 2), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 12, colSpan: 6))",
                    "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 4), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 16, colSpan: 2))",
                    "(line 3:RHLC Mode of Payment Unallocated: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Mode of Payment Unallocated: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Наличные: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Mode of Payment Unallocated: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Mode of Payment Unallocated: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Mode of Payment Unallocated: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Компенсируемые: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1), RHLC Наличные: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1), RHLC Нет данных: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1), RHLC Прямая оплата: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1), RHLC Mode of Payment Unallocated: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1), RHLC Прямая оплата: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1), RHLC Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1), RHLC Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1))");

        runReportTest("the most important trn-translated columns, by ToA, RUSSIAN", "AMP-16666-by-impl-level", 
                new String[] {"Проект_TAC_1", "Проект_TAC_2", "Предполагаемая стоймость проекта 1 - USD", "Предполагаемая стоймость проекта 2 - EUR", "Тест направленных МТЕФ", "Чисто-МТЕФ-Проект", 
                "проект с подпроектами", "Проект с документами", "Вода Eth", "Проект МТЕФ 1", 
                "проект-фильтры-по-датам", "Проект МТЕФ 2", "Проект PTC 1", "Проект PTC 2", 
                "Проект КЮЮ 1", "Проект КЮЮ 2", "crazy funding 1"}, 
                fddr_correct, null, "ru");


    }

    @Test
    public void testHierByTypeOfAssistance()
    {
        GroupReportModel fddr_correct = 
                GroupReportModel.withGroupReports("AMP-16666-by-type-of-assistance",
                        GroupReportModel.withColumnReports("AMP-16666-by-type-of-assistance",
                            ColumnReportDataModel.withColumns("Type Of Assistance: default type of assistance",
                                SimpleColumnModel.withContents("Project Title", "crazy funding 1", "crazy funding 1", "Test MTEF directed", "Test MTEF directed", "Eth Water", "Eth Water", "date-filters-activity", "date-filters-activity", "ptc activity 2", "ptc activity 2", "TAC_activity_1", "TAC_activity_1", "TAC_activity_2", "TAC_activity_2", "ptc activity 1", "ptc activity 1", "SSC Project 2", "SSC Project 2", "SSC Project 1", "SSC Project 1"), 
                                GroupColumnModel.withSubColumns("Funding",
                                    GroupColumnModel.withSubColumns("2009",
                                        SimpleColumnModel.withContents("Actual Commitments", "date-filters-activity", "100 000"), 
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)), 
                                    GroupColumnModel.withSubColumns("2010",
                                        SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY), 
                                        SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "143 777", "date-filters-activity", "60 000", "TAC_activity_1", "123 321", "TAC_activity_2", "453 213")), 
                                    GroupColumnModel.withSubColumns("2011",
                                        SimpleColumnModel.withContents("Actual Commitments", "TAC_activity_1", "213 231", "TAC_activity_2", "999 888"), 
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)), 
                                    GroupColumnModel.withSubColumns("2012",
                                        SimpleColumnModel.withContents("Actual Commitments", "date-filters-activity", "25 000"), 
                                        SimpleColumnModel.withContents("Actual Disbursements", "date-filters-activity", "12 000")), 
                                    GroupColumnModel.withSubColumns("2013",
                                        SimpleColumnModel.withContents("Actual Commitments", "crazy funding 1", "111 111", "ptc activity 2", "333 222", "ptc activity 1", "666 777", "SSC Project 2", "567 421", "SSC Project 1", "111 333"), 
                                        SimpleColumnModel.withContents("Actual Disbursements", "Eth Water", "545 000", "SSC Project 2", "131 845", "SSC Project 1", "555 111"))), 
                                GroupColumnModel.withSubColumns("Total Costs",
                                    SimpleColumnModel.withContents("Actual Commitments", "crazy funding 1", "111 111", "date-filters-activity", "125 000", "ptc activity 2", "333 222", "TAC_activity_1", "213 231", "TAC_activity_2", "999 888", "ptc activity 1", "666 777", "SSC Project 2", "567 421", "SSC Project 1", "111 333"), 
                                    SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "143 777", "Eth Water", "545 000", "date-filters-activity", "72 000", "TAC_activity_1", "123 321", "TAC_activity_2", "453 213", "SSC Project 2", "131 845", "SSC Project 1", "555 111")))
                            .withTrailCells(null, "100 000", "0", "0", "780 311", "1 213 119", "0", "25 000", "12 000", "1 789 864", "1 231 956", "3 127 983", "2 024 267"),
                            ColumnReportDataModel.withColumns("Type Of Assistance: second type of assistance",
                                SimpleColumnModel.withContents("Project Title", "crazy funding 1", "crazy funding 1"), 
                                GroupColumnModel.withSubColumns("Funding",
                                    GroupColumnModel.withSubColumns("2009",
                                        SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY), 
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)), 
                                    GroupColumnModel.withSubColumns("2010",
                                        SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY), 
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)), 
                                    GroupColumnModel.withSubColumns("2011",
                                        SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY), 
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)), 
                                    GroupColumnModel.withSubColumns("2012",
                                        SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY), 
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)), 
                                    GroupColumnModel.withSubColumns("2013",
                                        SimpleColumnModel.withContents("Actual Commitments", "crazy funding 1", "222 222"), 
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY))), 
                                GroupColumnModel.withSubColumns("Total Costs",
                                    SimpleColumnModel.withContents("Actual Commitments", "crazy funding 1", "222 222"), 
                                    SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)))
                            .withTrailCells(null, "0", "0", "0", "0", "0", "0", "0", "0", "222 222", "0", "222 222", "0"))
                        .withTrailCells(null, "100 000", "0", "0", "780 311", "1 213 119", "0", "25 000", "12 000", "2 012 086", "1 231 956", "3 350 205", "2 024 267"))
                    .withTrailCells(null, "100 000", "0", "0", "780 311", "1 213 119", "0", "25 000", "12 000", "2 012 086", "1 231 956", "3 350 205", "2 024 267")
                    .withPositionDigest(true,
                        "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 10), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 11, colSpan: 2))",
                        "(line 1:RHLC 2009: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2), RHLC 2010: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2), RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2), RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2))",
                        "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))");

        runReportTest("the most important trn-translated columns, by ToA, ENGLISH", "AMP-16666-by-type-of-assistance", 
                new String[] {"Eth Water", "mtef activity 1", "mtef activity 2", "ptc activity 1", "ptc activity 2", "SSC Project 1", 
                "SSC Project 2", "TAC_activity_1", "TAC_activity_2", "date-filters-activity", 
                "Proposed Project Cost 1 - USD", "Proposed Project Cost 2 - EUR", "Pure MTEF Project", "Test MTEF directed", 
                "AMP-15967-activity-2", "AMP-15967-activity-1", "crazy funding 1", "activity with components", "Project with documents"}, 
                fddr_correct, null, "en");  

        fddr_correct = 
                GroupReportModel.withGroupReports("AMP-16666-by-type-of-assistance",
                        GroupReportModel.withColumnReports("AMP-16666-by-type-of-assistance",
                            ColumnReportDataModel.withColumns("Type Of Assistance: вторичный тип помощи",
                                SimpleColumnModel.withContents("Project Title", "crazy funding 1", "crazy funding 1"), 
                                GroupColumnModel.withSubColumns("Funding",
                                    GroupColumnModel.withSubColumns("2009",
                                        SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY), 
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)), 
                                    GroupColumnModel.withSubColumns("2010",
                                        SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY), 
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)), 
                                    GroupColumnModel.withSubColumns("2011",
                                        SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY), 
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)), 
                                    GroupColumnModel.withSubColumns("2012",
                                        SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY), 
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)), 
                                    GroupColumnModel.withSubColumns("2013",
                                        SimpleColumnModel.withContents("Actual Commitments", "crazy funding 1", "222 222"), 
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY))), 
                                GroupColumnModel.withSubColumns("Total Costs",
                                    SimpleColumnModel.withContents("Actual Commitments", "crazy funding 1", "222 222"), 
                                    SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)))
                            .withTrailCells(null, "0", "0", "0", "0", "0", "0", "0", "0", "222 222", "0", "222 222", "0"),
                            ColumnReportDataModel.withColumns("Type Of Assistance: первичный тип помощи",
                                SimpleColumnModel.withContents("Project Title", "crazy funding 1", "crazy funding 1", "Тест направленных МТЕФ", "Тест направленных МТЕФ", "Вода Eth", "Вода Eth", "проект-фильтры-по-датам", "проект-фильтры-по-датам", "Проект PTC 2", "Проект PTC 2", "Проект_TAC_1", "Проект_TAC_1", "Проект_TAC_2", "Проект_TAC_2", "Проект PTC 1", "Проект PTC 1", "Проект КЮЮ 2", "Проект КЮЮ 2", "Проект КЮЮ 1", "Проект КЮЮ 1"), 
                                GroupColumnModel.withSubColumns("Funding",
                                    GroupColumnModel.withSubColumns("2009",
                                        SimpleColumnModel.withContents("Actual Commitments", "проект-фильтры-по-датам", "100 000"), 
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)), 
                                    GroupColumnModel.withSubColumns("2010",
                                        SimpleColumnModel.withContents("Actual Commitments", MUST_BE_EMPTY), 
                                        SimpleColumnModel.withContents("Actual Disbursements", "Тест направленных МТЕФ", "143 777", "проект-фильтры-по-датам", "60 000", "Проект_TAC_1", "123 321", "Проект_TAC_2", "453 213")), 
                                    GroupColumnModel.withSubColumns("2011",
                                        SimpleColumnModel.withContents("Actual Commitments", "Проект_TAC_1", "213 231", "Проект_TAC_2", "999 888"), 
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)), 
                                    GroupColumnModel.withSubColumns("2012",
                                        SimpleColumnModel.withContents("Actual Commitments", "проект-фильтры-по-датам", "25 000"), 
                                        SimpleColumnModel.withContents("Actual Disbursements", "проект-фильтры-по-датам", "12 000")), 
                                    GroupColumnModel.withSubColumns("2013",
                                        SimpleColumnModel.withContents("Actual Commitments", "crazy funding 1", "111 111", "Проект PTC 2", "333 222", "Проект PTC 1", "666 777", "Проект КЮЮ 2", "567 421", "Проект КЮЮ 1", "111 333"), 
                                        SimpleColumnModel.withContents("Actual Disbursements", "Вода Eth", "545 000", "Проект КЮЮ 2", "131 845", "Проект КЮЮ 1", "555 111"))), 
                                GroupColumnModel.withSubColumns("Total Costs",
                                    SimpleColumnModel.withContents("Actual Commitments", "crazy funding 1", "111 111", "проект-фильтры-по-датам", "125 000", "Проект PTC 2", "333 222", "Проект_TAC_1", "213 231", "Проект_TAC_2", "999 888", "Проект PTC 1", "666 777", "Проект КЮЮ 2", "567 421", "Проект КЮЮ 1", "111 333"), 
                                    SimpleColumnModel.withContents("Actual Disbursements", "Тест направленных МТЕФ", "143 777", "Вода Eth", "545 000", "проект-фильтры-по-датам", "72 000", "Проект_TAC_1", "123 321", "Проект_TAC_2", "453 213", "Проект КЮЮ 2", "131 845", "Проект КЮЮ 1", "555 111")))
                            .withTrailCells(null, "100 000", "0", "0", "780 311", "1 213 119", "0", "25 000", "12 000", "1 789 864", "1 231 956", "3 127 983", "2 024 267"))
                        .withTrailCells(null, "100 000", "0", "0", "780 311", "1 213 119", "0", "25 000", "12 000", "2 012 086", "1 231 956", "3 350 205", "2 024 267"))
                    .withTrailCells(null, "100 000", "0", "0", "780 311", "1 213 119", "0", "25 000", "12 000", "2 012 086", "1 231 956", "3 350 205", "2 024 267")
                    .withPositionDigest(true,
                        "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 10), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 11, colSpan: 2))",
                        "(line 1:RHLC 2009: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2), RHLC 2010: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2), RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2), RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2))",
                        "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))");

        runReportTest("the most important trn-translated columns, by ToA, RUSSIAN", "AMP-16666-by-type-of-assistance", 
                new String[] {"Проект_TAC_1", "Проект_TAC_2", "Предполагаемая стоймость проекта 1 - USD", "Предполагаемая стоймость проекта 2 - EUR", "Тест направленных МТЕФ", "Чисто-МТЕФ-Проект", 
                "проект с подпроектами", "Проект с документами", "Вода Eth", "Проект МТЕФ 1", 
                "проект-фильтры-по-датам", "Проект МТЕФ 2", "Проект PTC 1", "Проект PTC 2", 
                "Проект КЮЮ 1", "Проект КЮЮ 2", "crazy funding 1"}, 
                fddr_correct, null, "ru");
    }

    @Test
    @Ignore
    public void testFlat()
    {
        GroupReportModel fddr_correct = 
            GroupReportModel.withColumnReports("AMP-16666-flat",
                ColumnReportDataModel.withColumns("AMP-16666-flat",
                    SimpleColumnModel.withContents("Project Title", "crazy funding 1", "crazy funding 1", "TAC_activity_1", "TAC_activity_1", "TAC_activity_2", "TAC_activity_2", "Proposed Project Cost 1 - USD", "Proposed Project Cost 1 - USD", "Proposed Project Cost 2 - EUR", "Proposed Project Cost 2 - EUR", "Test MTEF directed", "Test MTEF directed", "Pure MTEF Project", "Pure MTEF Project", "activity with components", "activity with components", "Project with documents", "Project with documents", "Eth Water", "Eth Water", "mtef activity 1", "mtef activity 1", "date-filters-activity", "date-filters-activity", "mtef activity 2", "mtef activity 2", "ptc activity 1", "ptc activity 1", "ptc activity 2", "ptc activity 2", "SSC Project 1", "SSC Project 1", "SSC Project 2", "SSC Project 2"), 
                    SimpleColumnModel.withContents("Modalities", "Eth Water", "Diplomats and courses", "SSC Project 1", "Diplomats and courses", "SSC Project 2", "Virtual Platforms and blogs to consult, learn, and exchange ideas"), 
                    SimpleColumnModel.withContents("Status", "crazy funding 1", "default status", "TAC_activity_1", "default status", "TAC_activity_2", "default status", "Proposed Project Cost 1 - USD", "default status", "Proposed Project Cost 2 - EUR", "default status", "Test MTEF directed", "default status", "Pure MTEF Project", "default status", "activity with components", "default status", "Project with documents", "default status", "Eth Water", "second status", "mtef activity 1", "default status", "date-filters-activity", "default status", "mtef activity 2", "default status", "ptc activity 1", "default status", "ptc activity 2", "default status", "SSC Project 1", "default status", "SSC Project 2", "default status"), 
                    SimpleColumnModel.withContents("Financing Instrument", "crazy funding 1", "[default financing instrument, second financing instrument]", "TAC_activity_1", "default financing instrument", "TAC_activity_2", "default financing instrument", "Test MTEF directed", "default financing instrument", "Pure MTEF Project", "default financing instrument", "Eth Water", "[default financing instrument, second financing instrument]", "mtef activity 1", "default financing instrument", "date-filters-activity", "default financing instrument", "mtef activity 2", "default financing instrument", "ptc activity 1", "default financing instrument", "ptc activity 2", "default financing instrument", "SSC Project 1", "default financing instrument", "SSC Project 2", "default financing instrument"), 
                    SimpleColumnModel.withContents("Mode of Payment", "crazy funding 1", "[Cash, Direct payment]", "Test MTEF directed", "Cash", "Eth Water", "Direct payment", "mtef activity 2", "Direct payment", "ptc activity 1", "Reimbursable", "ptc activity 2", "No Information", "SSC Project 1", "Direct payment", "SSC Project 2", "Direct payment"), 
                    SimpleColumnModel.withContents("Type Of Assistance", "crazy funding 1", "[default type of assistance, second type of assistance]", "TAC_activity_1", "default type of assistance", "TAC_activity_2", "default type of assistance", "Test MTEF directed", "default type of assistance", "Pure MTEF Project", "default type of assistance", "Eth Water", "[default type of assistance, second type of assistance]", "mtef activity 1", "default type of assistance", "date-filters-activity", "default type of assistance", "mtef activity 2", "default type of assistance", "ptc activity 1", "default type of assistance", "ptc activity 2", "default type of assistance", "SSC Project 1", "default type of assistance", "SSC Project 2", "default type of assistance"), 
                    SimpleColumnModel.withContents("Implementation Level", "crazy funding 1", "Provincial", "TAC_activity_1", "Provincial", "TAC_activity_2", "Provincial", "Proposed Project Cost 1 - USD", "Provincial", "Proposed Project Cost 2 - EUR", "Provincial", "Test MTEF directed", "Provincial", "Pure MTEF Project", "Provincial", "activity with components", "Provincial", "Project with documents", "Provincial", "Eth Water", "Provincial", "mtef activity 1", "National", "date-filters-activity", "National", "mtef activity 2", "Provincial", "ptc activity 1", "Provincial", "ptc activity 2", "Provincial", "SSC Project 1", "Provincial", "SSC Project 2", "Provincial"), 
                    GroupColumnModel.withSubColumns("Funding",
                        GroupColumnModel.withSubColumns("2009",
                            GroupColumnModel.withSubColumns("Actual Commitments",
                                SimpleColumnModel.withContents("Mode of Payment Unallocated", "date-filters-activity", "100 000"))), 
                        GroupColumnModel.withSubColumns("2010",
                            GroupColumnModel.withSubColumns("Actual Disbursements",
                                SimpleColumnModel.withContents("Cash", "Test MTEF directed", "143 777"), 
                                SimpleColumnModel.withContents("Mode of Payment Unallocated", "date-filters-activity", "60 000", "TAC_activity_1", "123 321", "TAC_activity_2", "453 213"))), 
                        GroupColumnModel.withSubColumns("2011",
                            GroupColumnModel.withSubColumns("Actual Commitments",
                                SimpleColumnModel.withContents("Mode of Payment Unallocated", "TAC_activity_1", "213 231", "TAC_activity_2", "999 888"))), 
                        GroupColumnModel.withSubColumns("2012",
                            GroupColumnModel.withSubColumns("Actual Commitments",
                                SimpleColumnModel.withContents("Mode of Payment Unallocated", "date-filters-activity", "25 000")),
                            GroupColumnModel.withSubColumns("Actual Disbursements",
                                SimpleColumnModel.withContents("Mode of Payment Unallocated", "date-filters-activity", "12 000"))), 
                        GroupColumnModel.withSubColumns("2013",
                            GroupColumnModel.withSubColumns("Actual Commitments",
                                SimpleColumnModel.withContents("Cash", "crazy funding 1", "111 111"), 
                                SimpleColumnModel.withContents("Direct payment", "crazy funding 1", "222 222", "SSC Project 1", "111 333", "SSC Project 2", "567 421"), 
                                SimpleColumnModel.withContents("No Information", "ptc activity 2", "333 222"), 
                                SimpleColumnModel.withContents("Reimbursable", "ptc activity 1", "666 777")), 
                            GroupColumnModel.withSubColumns("Actual Disbursements",
                                SimpleColumnModel.withContents("Direct payment", "SSC Project 1", "555 111", "SSC Project 2", "131 845"), 
                                SimpleColumnModel.withContents("Mode of Payment Unallocated", "Eth Water", "545 000")))), 
                    GroupColumnModel.withSubColumns("Total Costs",
                        SimpleColumnModel.withContents("Actual Commitments", "crazy funding 1", "333 333", "date-filters-activity", "125 000", "TAC_activity_1", "213 231", "ptc activity 1", "666 777", "TAC_activity_2", "999 888", "ptc activity 2", "333 222", "SSC Project 1", "111 333", "SSC Project 2", "567 421"), 
                        SimpleColumnModel.withContents("Actual Disbursements", "Test MTEF directed", "143 777", "Eth Water", "545 000", "date-filters-activity", "72 000", "TAC_activity_1", "123 321", "TAC_activity_2", "453 213", "SSC Project 1", "555 111", "SSC Project 2", "131 845")))
                .withTrailCells(null, null, null, null, null, null, null, "100 000", "143 777", "636 534", "1 213 119", "25 000", "12 000", "111 111", "900 976", "333 222", "666 777", "686 956", "545 000", "3 350 205", "2 024 267"))
            .withTrailCells(null, null, null, null, null, null, null, "100 000", "143 777", "636 534", "1 213 119", "25 000", "12 000", "111 111", "900 976", "333 222", "666 777", "686 956", "545 000", "3 350 205", "2 024 267")
            .withPositionDigest(true,
                "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1), RHLC Modalities: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 1, colSpan: 1), RHLC Status: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 2, colSpan: 1), RHLC Financing Instrument: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 3, colSpan: 1), RHLC Mode of Payment: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 4, colSpan: 1), RHLC Type Of Assistance: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 5, colSpan: 1), RHLC Implementation Level: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 6, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 7, colSpan: 12), RHLC Total Costs: (startRow: 0, rowSpan: 3, totalRowSpan: 4, colStart: 19, colSpan: 2))",
                "(line 1:RHLC 2009: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 7, colSpan: 1), RHLC 2010: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 8, colSpan: 2), RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 10, colSpan: 1), RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 11, colSpan: 2), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 13, colSpan: 6))",
                "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 13, colSpan: 4), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 17, colSpan: 2))",
                "(line 3:RHLC Mode of Payment Unallocated: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Cash: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Mode of Payment Unallocated: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Mode of Payment Unallocated: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Mode of Payment Unallocated: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Mode of Payment Unallocated: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1), RHLC Cash: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1), RHLC Direct payment: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1), RHLC No Information: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1), RHLC Reimbursable: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1), RHLC Direct payment: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1), RHLC Mode of Payment Unallocated: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1), RHLC Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1), RHLC Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1))");

        runReportTest("the most important trn-translated columns, flat, ENGLISH", "AMP-16666-flat", 
                new String[] {"Eth Water", "mtef activity 1", "mtef activity 2", "ptc activity 1", "ptc activity 2", "SSC Project 1", 
                "SSC Project 2", "TAC_activity_1", "TAC_activity_2", "date-filters-activity", 
                "Proposed Project Cost 1 - USD", "Proposed Project Cost 2 - EUR", "Pure MTEF Project", "Test MTEF directed", 
                "AMP-15967-activity-2", "AMP-15967-activity-1", "crazy funding 1", "activity with components", "Project with documents"}, 
                fddr_correct, null, "en");  

        fddr_correct = GroupReportModel.withColumnReports("AMP-16666-flat",
                ColumnReportDataModel.withColumns("AMP-16666-flat",
                        SimpleColumnModel.withContents("Project Title", "crazy funding 1", "crazy funding 1", "Проект_TAC_1", "Проект_TAC_1", "Проект_TAC_2", "Проект_TAC_2", "Предполагаемая стоймость проекта 1 - USD", "Предполагаемая стоймость проекта 1 - USD", "Предполагаемая стоймость проекта 2 - EUR", "Предполагаемая стоймость проекта 2 - EUR", "Тест направленных МТЕФ", "Тест направленных МТЕФ", "Чисто-МТЕФ-Проект", "Чисто-МТЕФ-Проект", "проект с подпроектами", "проект с подпроектами", "Проект с документами", "Проект с документами", "Вода Eth", "Вода Eth", "Проект МТЕФ 1", "Проект МТЕФ 1", "проект-фильтры-по-датам", "проект-фильтры-по-датам", "Проект МТЕФ 2", "Проект МТЕФ 2", "Проект PTC 1", "Проект PTC 1", "Проект PTC 2", "Проект PTC 2", "Проект КЮЮ 1", "Проект КЮЮ 1", "Проект КЮЮ 2", "Проект КЮЮ 2"), 
                        SimpleColumnModel.withContents("Modalities", "Вода Eth", "Дипломаты и курсы", "Проект КЮЮ 1", "Дипломаты и курсы", "Проект КЮЮ 2", "Виртуальные Платформы и блоги проконсультироваться, узнать, и обмениваться идеями"), 
                        SimpleColumnModel.withContents("Status", "crazy funding 1", "первый статус", "Проект_TAC_1", "первый статус", "Проект_TAC_2", "первый статус", "Предполагаемая стоймость проекта 1 - USD", "первый статус", "Предполагаемая стоймость проекта 2 - EUR", "первый статус", "Тест направленных МТЕФ", "первый статус", "Чисто-МТЕФ-Проект", "первый статус", "проект с подпроектами", "первый статус", "Проект с документами", "первый статус", "Вода Eth", "второй статус", "Проект МТЕФ 1", "первый статус", "проект-фильтры-по-датам", "первый статус", "Проект МТЕФ 2", "первый статус", "Проект PTC 1", "первый статус", "Проект PTC 2", "первый статус", "Проект КЮЮ 1", "первый статус", "Проект КЮЮ 2", "первый статус"), 
                        SimpleColumnModel.withContents("Financing Instrument", "crazy funding 1", "[вторичный инструмент финансирования, первичный инструмент финансирования]", "Проект_TAC_1", "первичный инструмент финансирования", "Проект_TAC_2", "первичный инструмент финансирования", "Тест направленных МТЕФ", "первичный инструмент финансирования", "Чисто-МТЕФ-Проект", "первичный инструмент финансирования", "Вода Eth", "[вторичный инструмент финансирования, первичный инструмент финансирования]", "Проект МТЕФ 1", "первичный инструмент финансирования", "проект-фильтры-по-датам", "первичный инструмент финансирования", "Проект МТЕФ 2", "первичный инструмент финансирования", "Проект PTC 1", "первичный инструмент финансирования", "Проект PTC 2", "первичный инструмент финансирования", "Проект КЮЮ 1", "первичный инструмент финансирования", "Проект КЮЮ 2", "первичный инструмент финансирования"), 
                        SimpleColumnModel.withContents("Mode of Payment", "crazy funding 1", "[Наличные, Прямая оплата]", "Тест направленных МТЕФ", "Наличные", "Вода Eth", "Прямая оплата", "Проект МТЕФ 2", "Прямая оплата", "Проект PTC 1", "Компенсируемые", "Проект PTC 2", "Нет данных", "Проект КЮЮ 1", "Прямая оплата", "Проект КЮЮ 2", "Прямая оплата"), 
                        SimpleColumnModel.withContents("Type Of Assistance", "crazy funding 1", "[вторичный тип помощи, первичный тип помощи]", "Проект_TAC_1", "первичный тип помощи", "Проект_TAC_2", "первичный тип помощи", "Тест направленных МТЕФ", "первичный тип помощи", "Чисто-МТЕФ-Проект", "первичный тип помощи", "Вода Eth", "[вторичный тип помощи, первичный тип помощи]", "Проект МТЕФ 1", "первичный тип помощи", "проект-фильтры-по-датам", "первичный тип помощи", "Проект МТЕФ 2", "первичный тип помощи", "Проект PTC 1", "первичный тип помощи", "Проект PTC 2", "первичный тип помощи", "Проект КЮЮ 1", "первичный тип помощи", "Проект КЮЮ 2", "первичный тип помощи"), 
                        SimpleColumnModel.withContents("Implementation Level", "crazy funding 1", "Провинциальный", "Проект_TAC_1", "Провинциальный", "Проект_TAC_2", "Провинциальный", "Предполагаемая стоймость проекта 1 - USD", "Провинциальный", "Предполагаемая стоймость проекта 2 - EUR", "Провинциальный", "Тест направленных МТЕФ", "Провинциальный", "Чисто-МТЕФ-Проект", "Провинциальный", "проект с подпроектами", "Провинциальный", "Проект с документами", "Провинциальный", "Вода Eth", "Провинциальный", "Проект МТЕФ 1", "Национальный", "проект-фильтры-по-датам", "Национальный", "Проект МТЕФ 2", "Провинциальный", "Проект PTC 1", "Провинциальный", "Проект PTC 2", "Провинциальный", "Проект КЮЮ 1", "Провинциальный", "Проект КЮЮ 2", "Провинциальный"), 
                        GroupColumnModel.withSubColumns("Funding",
                            GroupColumnModel.withSubColumns("2009",
                                GroupColumnModel.withSubColumns("Actual Commitments",
                                    SimpleColumnModel.withContents("Mode of Payment Unallocated", "проект-фильтры-по-датам", "100 000"))), 
                            GroupColumnModel.withSubColumns("2010",
                                GroupColumnModel.withSubColumns("Actual Disbursements",
                                    SimpleColumnModel.withContents("Mode of Payment Unallocated", "проект-фильтры-по-датам", "60 000", "Проект_TAC_1", "123 321", "Проект_TAC_2", "453 213"), 
                                    SimpleColumnModel.withContents("Наличные", "Тест направленных МТЕФ", "143 777"))), 
                            GroupColumnModel.withSubColumns("2011",
                                GroupColumnModel.withSubColumns("Actual Commitments",
                                    SimpleColumnModel.withContents("Mode of Payment Unallocated", "Проект_TAC_1", "213 231", "Проект_TAC_2", "999 888"))), 
                            GroupColumnModel.withSubColumns("2012",
                                GroupColumnModel.withSubColumns("Actual Commitments",
                                    SimpleColumnModel.withContents("Mode of Payment Unallocated", "проект-фильтры-по-датам", "25 000")), 
                                GroupColumnModel.withSubColumns("Actual Disbursements",
                                    SimpleColumnModel.withContents("Mode of Payment Unallocated", "проект-фильтры-по-датам", "12 000"))), 
                            GroupColumnModel.withSubColumns("2013",
                                GroupColumnModel.withSubColumns("Actual Commitments",
                                    SimpleColumnModel.withContents("Компенсируемые", "Проект PTC 1", "666 777"), 
                                    SimpleColumnModel.withContents("Наличные", "crazy funding 1", "111 111"), 
                                    SimpleColumnModel.withContents("Нет данных", "Проект PTC 2", "333 222"), 
                                    SimpleColumnModel.withContents("Прямая оплата", "crazy funding 1", "222 222", "Проект КЮЮ 1", "111 333", "Проект КЮЮ 2", "567 421").setIsPledge(false)), 
                                GroupColumnModel.withSubColumns("Actual Disbursements",
                                    SimpleColumnModel.withContents("Mode of Payment Unallocated", "Вода Eth", "545 000"), 
                                    SimpleColumnModel.withContents("Прямая оплата", "Проект КЮЮ 1", "555 111", "Проект КЮЮ 2", "131 845")))), 
                        GroupColumnModel.withSubColumns("Total Costs",
                            SimpleColumnModel.withContents("Actual Commitments", "crazy funding 1", "333 333", "проект-фильтры-по-датам", "125 000", "Проект_TAC_1", "213 231", "Проект PTC 1", "666 777", "Проект_TAC_2", "999 888", "Проект PTC 2", "333 222", "Проект КЮЮ 1", "111 333", "Проект КЮЮ 2", "567 421"), 
                            SimpleColumnModel.withContents("Actual Disbursements", "Тест направленных МТЕФ", "143 777", "Вода Eth", "545 000", "проект-фильтры-по-датам", "72 000", "Проект_TAC_1", "123 321", "Проект_TAC_2", "453 213", "Проект КЮЮ 1", "555 111", "Проект КЮЮ 2", "131 845")))
                    .withTrailCells(null, null, null, null, null, null, null, "100 000", "636 534", "143 777", "1 213 119", "25 000", "12 000", "666 777", "111 111", "333 222", "900 976", "545 000", "686 956", "3 350 205", "2 024 267"))
                .withTrailCells(null, null, null, null, null, null, null, "100 000", "636 534", "143 777", "1 213 119", "25 000", "12 000", "666 777", "111 111", "333 222", "900 976", "545 000", "686 956", "3 350 205", "2 024 267")
                .withPositionDigest(true,
                    "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1), RHLC Modalities: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 1, colSpan: 1), RHLC Status: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 2, colSpan: 1), RHLC Financing Instrument: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 3, colSpan: 1), RHLC Mode of Payment: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 4, colSpan: 1), RHLC Type Of Assistance: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 5, colSpan: 1), RHLC Implementation Level: (startRow: 0, rowSpan: 4, totalRowSpan: 4, colStart: 6, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 7, colSpan: 12), RHLC Total Costs: (startRow: 0, rowSpan: 3, totalRowSpan: 4, colStart: 19, colSpan: 2))",
                    "(line 1:RHLC 2009: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 7, colSpan: 1), RHLC 2010: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 8, colSpan: 2), RHLC 2011: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 10, colSpan: 1), RHLC 2012: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 11, colSpan: 2), RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 13, colSpan: 6))",
                    "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 13, colSpan: 4), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 17, colSpan: 2))",
                    "(line 3:RHLC Mode of Payment Unallocated: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1), RHLC Mode of Payment Unallocated: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1), RHLC Наличные: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1), RHLC Mode of Payment Unallocated: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1), RHLC Mode of Payment Unallocated: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1), RHLC Mode of Payment Unallocated: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1), RHLC Компенсируемые: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1), RHLC Наличные: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1), RHLC Нет данных: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1), RHLC Прямая оплата: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1), RHLC Mode of Payment Unallocated: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1), RHLC Прямая оплата: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1), RHLC Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1), RHLC Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1))");
        
        runReportTest("the most important trn-translated columns, flat, RUSSIAN", "AMP-16666-flat", 
                new String[] {"Проект_TAC_1", "Проект_TAC_2", "Предполагаемая стоймость проекта 1 - USD", "Предполагаемая стоймость проекта 2 - EUR", "Тест направленных МТЕФ", "Чисто-МТЕФ-Проект", 
                "проект с подпроектами", "Проект с документами", "Вода Eth", "Проект МТЕФ 1", 
                "проект-фильтры-по-датам", "Проект МТЕФ 2", "Проект PTC 1", "Проект PTC 2", 
                "Проект КЮЮ 1", "Проект КЮЮ 2", "crazy funding 1"}, 
                fddr_correct, null, "ru");  


    }
    
}
