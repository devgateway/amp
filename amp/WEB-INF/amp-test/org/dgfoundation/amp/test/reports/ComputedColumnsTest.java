package org.dgfoundation.amp.test.reports;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.GroupColumn;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.ComputedAmountCell;
import org.dgfoundation.amp.ar.cell.ComputedCountingAmountCell;
import org.dgfoundation.amp.ar.cell.TextCell;
import org.dgfoundation.amp.ar.workers.ComputedAmountColWorker;
import org.dgfoundation.amp.ar.workers.ComputedCountingAmountColWorker;
import org.dgfoundation.amp.ar.workers.ComputedDateColWorker;
import org.dgfoundation.amp.exprlogic.MathExpressionRepository;
import org.dgfoundation.amp.test.util.Configuration;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.hibernate.Session;

import com.sun.rowset.CachedRowSetImpl;

public class ComputedColumnsTest extends TestCase {

	class TestDateWorker extends ComputedDateColWorker {

		public TestDateWorker(String condition, String viewName, String columnName, ReportGenerator generator) {
			super(condition, viewName, columnName, generator);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected Column extractCellColumn() {
			try {
				Session sess = null;
				Connection conn = null;
				CellColumn cc = null;
				sess = PersistenceManager.getSession();
				conn = sess.connection();

				java.sql.PreparedStatement ps;

				ps = conn
						.prepareStatement("select 1 AS amp_activity_id, date ('2009-02-01') AS activity_close_date,  date( CURRENT_DATE - 1) AS actual_start_date, date ('2009-02-01') AS actual_approval_date, date ('2009-02-01') AS activity_approval_date, date ('2009-02-01') AS proposed_start_date, date ('2009-02-01') AS actual_completion_date, date ('2009-02-01') AS proposed_completion_date from dual");
				java.sql.ResultSet rs = ps.executeQuery();
				rsmd = rs.getMetaData();
				int colsCount = rsmd.getColumnCount() + 1;

				columnsMetaData = new HashSet<String>();

				for (int i = 1; i < colsCount; i++) {
					columnsMetaData.add(rsmd.getColumnName(i).toLowerCase());
				}
				rs.last();
				int rsSize = rs.getRow();
				cc = newColumnInstance(rsSize + 1);

				rs.beforeFirst();

				CachedRowSetImpl crs = new CachedRowSetImpl();

				crs.populate(rs);

				rs.close();

				while (crs.next()) {
					Cell c = getCellFromRow(crs);
					if (c != null)
						cc.addCell(c);
				}

				crs.close();
				return cc;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	// Current Date - Actual Start Date 
	//needs to be reviewed 
	public void testComputedDateCell() {
		Configuration.initConfig();
		ComputedDateColWorker worker = new TestDateWorker(null, "v_computed_dates", "", null);
		AmpColumns a = new AmpColumns();
		a.setTokenExpression(MathExpressionRepository.AGE_OF_PROJECT_KEY);
		worker.setRelatedColumn(a);
		Column c = worker.populateCellColumn();

		
		Collection<TextCell> list = c.getItems();
		for (TextCell textCell : list) {
			assertEquals("1", textCell.getValue());
		}
		//TODO: Check why in the server the result is 9 AMP-5585
	}

	class TestComputedWorker extends ComputedAmountColWorker {

		public TestComputedWorker(String columnName, GroupColumn rawColumns, ReportGenerator generator) {
			super(columnName, rawColumns, generator);
			metaInfoCache = new HashMap();// TODO Auto-generated constructor
			// stub
		}

		@Override
		protected Column generateCellColumn() {

			Collection<CategAmountCell> list = getFundingDataSet();
			CellColumn dest = newColumnInstance(list.size());
			Iterator i = list.iterator();
			while (i.hasNext()) {
				Cell element = (Cell) i.next();
				Cell destCell = getCellFromCell(element);
				if (destCell != null)
					dest.addCell(destCell);
			}
			return dest;
		}
	}

	// 5000 -10000 /10000
	// ((Actual Disbursements - planned Disbursements)/planned disbursements) X
	// 100
	public void testComputedAmountCell() {

		Configuration.initConfig();
		TestComputedWorker worker = new TestComputedWorker("", null, null);
		AmpColumns a = new AmpColumns();
		a.setTokenExpression(MathExpressionRepository.PREDICTABILITY_OF_FUNDING_KEY);
		worker.setExtractor(false);
		worker.setRelatedColumn(a);
		Column c = worker.populateCellColumn();

		Collection<ComputedAmountCell> list = c.getItems();
		for (ComputedAmountCell computedAmountCell : list) {
			if (computedAmountCell.getAmount().intValue()!=0){
				assertEquals(-50, computedAmountCell.getAmount().intValue());
			} 
		}

	}

	class TestComputedCountingWorker extends ComputedCountingAmountColWorker {
		public TestComputedCountingWorker(String columnName, GroupColumn rawColumns, ReportGenerator generator) {
			super(columnName, rawColumns, generator);
			metaInfoCache = new HashMap();// TODO Auto-generated constructor

		}

		@Override
		protected Column generateCellColumn() {

			Collection<CategAmountCell> list = getFundingDataSet();
			CellColumn dest = newColumnInstance(2);
			Iterator i = list.iterator();
			while (i.hasNext()) {
				Cell element = (Cell) i.next();
				Cell destCell = getCellFromCell(element);
				if (destCell != null)
					dest.addCell(destCell);
			}
			return dest;
		}
	}

	public void testComputedCountingAmountCell() {

		Configuration.initConfig();
		TestComputedCountingWorker worker = new TestComputedCountingWorker("", null, null);
		AmpColumns a = new AmpColumns();
		a.setTokenExpression(MathExpressionRepository.AVERAGE_SIZE_OF_PROJECT_KEY);
		worker.setExtractor(false);
		worker.setRelatedColumn(a);
		Column c = worker.populateCellColumn();

		Collection<ComputedCountingAmountCell> list = c.getItems();
		for (ComputedCountingAmountCell computedCountingAmountCell : list) {
			assertEquals(0, computedCountingAmountCell.getAmount().intValue());
		}
		Collection<ComputedCountingAmountCell> list2 = c.getTrailCells();
		for (ComputedCountingAmountCell computedCountingAmountCell : list2) {
			assertEquals(85000, computedCountingAmountCell.getAmount().intValue());
		}

	}

	Collection<CategAmountCell> list = null;

	private Collection<CategAmountCell> getFundingDataSet() {
		if (list != null)
			return list;
		list = new HashSet<CategAmountCell>();
		CategAmountCell cell = new ComputedCountingAmountCell();
		cell.setId(1l);
		cell.setAmount(new BigDecimal(100000));
		cell.setCurrencyCode("USD");
		cell.setCurrencyDate(new Date());
		cell.setOwnerId(1l);
		MetaInfo trMs = getCachedMetaInfo(ArConstants.TRANSACTION_TYPE, ArConstants.COMMITMENT);
		MetaInfo adjMs = getCachedMetaInfo(ArConstants.ADJUSTMENT_TYPE, ArConstants.ACTUAL);
		cell.getMetaData().add(trMs);
		cell.getMetaData().add(adjMs);

		list.add(cell);

		cell = new ComputedCountingAmountCell();
		cell.setId(1l);
		cell.setAmount(new BigDecimal(5000));
		cell.setCurrencyCode("USD");
		cell.setCurrencyDate(new Date());
		cell.setOwnerId(1l);
		trMs = getCachedMetaInfo(ArConstants.TRANSACTION_TYPE, ArConstants.DISBURSEMENT);
		adjMs = getCachedMetaInfo(ArConstants.ADJUSTMENT_TYPE, ArConstants.ACTUAL);
		cell.getMetaData().add(trMs);
		cell.getMetaData().add(adjMs);
		list.add(cell);

		cell = new ComputedCountingAmountCell();
		cell.setId(1l);
		cell.setAmount(new BigDecimal(10000));
		cell.setCurrencyCode("USD");
		cell.setCurrencyDate(new Date());
		cell.setOwnerId(1l);
		trMs = getCachedMetaInfo(ArConstants.TRANSACTION_TYPE, ArConstants.DISBURSEMENT);
		adjMs = getCachedMetaInfo(ArConstants.ADJUSTMENT_TYPE, ArConstants.PLANNED);
		cell.getMetaData().add(trMs);
		cell.getMetaData().add(adjMs);
		list.add(cell);
		
		
		cell = new ComputedCountingAmountCell();
		cell.setId(1l);
		cell.setAmount(new BigDecimal(9999));
		cell.setCurrencyCode("USD");
		cell.setCurrencyDate(new Date());
		cell.setOwnerId(1l);
		trMs = getCachedMetaInfo(ArConstants.TRANSACTION_TYPE, ArConstants.EXPENDITURE);
		adjMs = getCachedMetaInfo(ArConstants.ADJUSTMENT_TYPE, ArConstants.PLANNED);
		cell.getMetaData().add(trMs);
		cell.getMetaData().add(adjMs);
		list.add(cell);
		
		cell = new ComputedCountingAmountCell();
		cell.setId(1l);
		cell.setAmount(new BigDecimal(9999));
		cell.setCurrencyCode("USD");
		cell.setCurrencyDate(new Date());
		cell.setOwnerId(1l);
		trMs = getCachedMetaInfo(ArConstants.TRANSACTION_TYPE, ArConstants.EXPENDITURE);
		adjMs = getCachedMetaInfo(ArConstants.ADJUSTMENT_TYPE, ArConstants.ACTUAL);
		cell.getMetaData().add(trMs);
		cell.getMetaData().add(adjMs);
		list.add(cell);

		// one more actual commmitmets
		cell = new ComputedCountingAmountCell();
		cell.setId(2l);
		cell.setAmount(new BigDecimal(70000));
		cell.setCurrencyCode("USD");
		cell.setCurrencyDate(new Date());
		cell.setOwnerId(2l);
		trMs = getCachedMetaInfo(ArConstants.TRANSACTION_TYPE, ArConstants.COMMITMENT);
		adjMs = getCachedMetaInfo(ArConstants.ADJUSTMENT_TYPE, ArConstants.ACTUAL);
		cell.getMetaData().add(trMs);
		cell.getMetaData().add(adjMs);
		list.add(cell);

		return list;

	}

	private Map metaInfoCache;

	private MetaInfo getCachedMetaInfo(String category, Comparable value) {
		Map valuesMap = (Map) metaInfoCache.get(category);
		if (valuesMap == null) {
			valuesMap = new HashMap();
			metaInfoCache.put(category, valuesMap);
		}
		MetaInfo mi = (MetaInfo) valuesMap.get(value);
		if (mi != null)
			return mi;
		mi = new MetaInfo(category, value);
		valuesMap.put(value, mi);
		return mi;
	}
}
