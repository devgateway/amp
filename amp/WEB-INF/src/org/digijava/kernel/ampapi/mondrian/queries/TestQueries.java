package org.digijava.kernel.ampapi.mondrian.queries;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.olap4j.Axis;
import org.olap4j.Cell;
import org.olap4j.CellSet;
import org.olap4j.CellSetAxis;
import org.olap4j.OlapConnection;
import org.olap4j.Position;
import org.olap4j.layout.RectangularCellSetFormatter;
import org.olap4j.metadata.Member;

public class TestQueries {

	public StringBuffer getQuery(String path) throws Exception {

		final String query = "SELECT NON EMPTY {[Measures].[Actual Commitments]}"
				+ "ON COLUMNS, NON EMPTY {Hierarchize({[Donor Types].[DonorType].Members})}"
				+ "ON ROWS FROM [Donor Funding]";

		// Load the driver
		Class.forName("mondrian.olap4j.MondrianOlap4jDriver");
		
		// Connect
		final Connection connection = DriverManager
				.getConnection(org.digijava.kernel.ampapi.mondrian.util.Connection.getConnection(path));

		// We are dealing with an olap connection. we must unwrap it.
		final OlapConnection olapConnection = connection
				.unwrap(OlapConnection.class);

		// Prepare a statement and execute query
		final CellSet cellSet = olapConnection.createStatement()
				.executeOlapQuery(query);

		List<CellSetAxis> cellSetAxes = cellSet.getAxes();

		// Print headings.
		System.out.print("\t");
		CellSetAxis columnsAxis = cellSetAxes.get(Axis.COLUMNS.axisOrdinal());
		for (Position position : columnsAxis.getPositions()) {
			Member measure = position.getMembers().get(0);
			System.out.print(measure.getName());
		}

		StringBuffer result = new StringBuffer("");
		// Print rows.
		CellSetAxis rowsAxis = cellSetAxes.get(Axis.ROWS.axisOrdinal());
		int cellOrdinal = 0;
		for (Position rowPosition : rowsAxis.getPositions()) {
			boolean first = true;
			for (Member member : rowPosition.getMembers()) {
				if (first) {
					first = false;
				} else {
					result.append('\t');
					// System.out.print('\t');
				}
				result.append(member.getName());
				// System.out.print(member.getName());
			}

			// Print the value of the cell in each column.
			for (Position columnPosition : columnsAxis.getPositions()) {
				// Access the cell via its ordinal. The ordinal is kept in step
				// because we increment the ordinal once for each row and
				// column.
				Cell cell = cellSet.getCell(cellOrdinal);

				// Just for kicks, convert the ordinal to a list of coordinates.
				// The list matches the row and column positions.
				List<Integer> coordList = cellSet
						.ordinalToCoordinates(cellOrdinal);
				assert coordList.get(0) == rowPosition.getOrdinal();
				assert coordList.get(1) == columnPosition.getOrdinal();

				++cellOrdinal;
				result.append('\t');
				// System.out.print('\t');
				result.append(cell.getFormattedValue());
				// System.out.print(cell.getFormattedValue());
			}
			// System.out.println();
		}

		// We use the utility formatter.
		RectangularCellSetFormatter formatter = new RectangularCellSetFormatter(false);
		
		// Print out.
		PrintWriter writer = new PrintWriter(System.out);
		formatter.format(cellSet, writer);
		writer.flush();
		return result;

	}

}
