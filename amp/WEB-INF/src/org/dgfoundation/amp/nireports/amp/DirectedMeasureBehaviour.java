package org.dgfoundation.amp.nireports.amp;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.apache.wicket.behavior.Behavior;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.ComparableValue;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.amp.dimensions.OrganisationsDimension;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.runtime.VSplitStrategy;
import org.dgfoundation.amp.nireports.schema.IdsAcceptor;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiReportedEntity;
import org.dgfoundation.amp.nireports.schema.TrivialMeasureBehaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

/**
 * the {@link Behavior} of a Funding Flow entity
 * @author Dolghier Constantin
 *
 */
public class DirectedMeasureBehaviour extends TrivialMeasureBehaviour {
		
	protected final String totalColumnName;
	
	public DirectedMeasureBehaviour() {
		this(null);
	}
	
	public DirectedMeasureBehaviour(String totalColumnName) {
		this.totalColumnName = totalColumnName;
	}
	
	@Override
	public List<VSplitStrategy> getSubMeasureHierarchies(NiReportsEngine context) {
		VSplitStrategy byFundingFlow = VSplitStrategy.build(cell -> new ComparableValue<String>(getFlowName(cell.getCell()), getFlowName(cell.getCell())), AmpReportsSchema.PSEUDOCOLUMN_FLOW);
		return Arrays.asList(byFundingFlow);
	}

	public static String getFlowName(Cell cell) {
		return cell.getMetaInfo().getMetaInfo(MetaCategory.DIRECTED_TRANSACTION_FLOW.category).v.toString();
	}
	
	@Override
	public ImmutablePair<String, ColumnContents> getTotalCells(NiReportsEngine context, NiReportedEntity<?> entity, ColumnContents fetchedContents) {
		if (totalColumnName == null)
			return super.getTotalCells(context, entity, fetchedContents);
		return new ImmutablePair<String, ColumnContents>(totalColumnName, fetchedContents);
	}
	
	@Override
	public Cell filterCell(Map<NiDimensionUsage, IdsAcceptor> acceptors, Cell oldCell, Cell splitCell) {
		if ((!splitCell.mainLevel.isPresent()) || getHierarchiesListener().test(splitCell.mainLevel.get().dimensionUsage))
			return super.filterCell(acceptors, oldCell, splitCell); // this is not a related-organisation hierarchy
		
		// gone till here -> we're doing a hierarchy by a related organisation
		String srcRoleCode = oldCell.getMetaInfo().getMetaInfo(MetaCategory.SOURCE_ROLE.category).v.toString();
		String recipientRoleCode = oldCell.getMetaInfo().getMetaInfo(MetaCategory.RECIPIENT_ROLE.category).v.toString();
		
		String dimUsgRole = splitCell.mainLevel.get().dimensionUsage.instanceName;
		Coordinate coordinateToCheckOn = null;
		if (srcRoleCode.equals(dimUsgRole))
			coordinateToCheckOn = new Coordinate(OrganisationsDimension.LEVEL_ORGANISATION, (Long) oldCell.getMetaInfo().getMetaInfo(MetaCategory.SOURCE_ORG.category).v);

		if (recipientRoleCode.equals(dimUsgRole))
			coordinateToCheckOn = new Coordinate(OrganisationsDimension.LEVEL_ORGANISATION, (Long) oldCell.getMetaInfo().getMetaInfo(MetaCategory.RECIPIENT_ORG.category).v);

		if (coordinateToCheckOn == null)
			return null; // related-org-role not implied in this kind of transaction
		
		NiDimensionUsage dimUsage = splitCell.mainLevel.get().dimensionUsage;
		IdsAcceptor acceptor = acceptors.get(dimUsage);
		if (acceptor.isAcceptable(coordinateToCheckOn))
			return oldCell;
		else
			return null;
	}
	
	/** 
	 * <strong>make the common case fast: if this function returns null, it is a shortcut for "measure listenes to all hierarchies"</strong>
	 * @return a predicate which governs the hierarchies whose percentages are listened to by an entity 
	 */
	@Override
	public Predicate<NiDimensionUsage> getHierarchiesListener() {
		return dimUsg -> !dimUsg.dimension.name.equals("orgs");
	}
	
	@Override
	public boolean shouldDeleteLeafIfEmpty(CellColumn column) {
		return true;
	}
}
