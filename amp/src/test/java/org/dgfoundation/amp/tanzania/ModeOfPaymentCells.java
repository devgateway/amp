package org.dgfoundation.amp.tanzania;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.testmodels.nicolumns.HardcodedCells;

import org.dgfoundation.amp.nireports.TextCell;


public class ModeOfPaymentCells extends HardcodedCells<TextCell> {

	public ModeOfPaymentCells(Map<String, Long> activityNames, Map<String, Long> entityNames, NiDimension dim, String key) {
		super(activityNames, entityNames, degenerate(dim, key));
	}
	public ModeOfPaymentCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
		super(activityNames, entityNames, lc);
	}

	@Override
	protected List<TextCell> populateCells() {
		return 	Arrays.asList(
		cell("(JICA) (TA-P10) Iringa-Shinyanga Backbone Transmission Investment Project", "Ininga MC", 271),
		cell("54020090 - EcoEnergy Tanzania 2014 - 2015", "Bagamoyo DC", 252),
		cell("Adapting to climate change in coastal Dar es Salaam", "Dar es Salaam CC", 259),
		cell("Beekeeper Economic Empowerment (BEE) Project, Tabora, Tanzania", "Tabora MC", 370),
		cell("Capacity Development in Specialized Tax Administration", "Dar es Salaam CC", 259),
		cell("Chalinze Water supply", "Kibaha DC", 253),
		cell("Documenting of Tanzanian Art Collection", "Dar es Salaam CC", 259),
		cell("East African Legislative Assembly (EALA) /AWEPA", "Dar es Salaam CC", 259),
		cell("EDUCATION -[EQUAL]", "Bagamoyo DC", 252),
		cell("EDUCATION -[EQUAL]", "Hai DC", 291),
		cell("EDUCATION -[EQUAL]", "Iringa DC", 270),
		cell("EDUCATION -[EQUAL]", "Kasulu DC", 287),
		cell("EDUCATION -[EQUAL]", "Magu DC", 338),
		cell("EDUCATION -[EQUAL]", "Mbarali DC", 319),
		cell("EDUCATION -[EQUAL]", "Mbeya DC", 321),
		cell("EDUCATION -[EQUAL]", "Mtwara DC", 331),
		cell("EDUCATION -[EQUAL]", "Mufindi DC", 276),
		cell("EDUCATION -[EQUAL]", "Njombe DC", 277),
		cell("EDUCATION -[EQUAL]", "Siha DC", 297),
		cell("Empowering Vulnerable Rural Communities to Adapt and Mitigate the Impacts of Climate Change in Central Tanzania", "Dodoma MC", 266),
		cell("Enhancing Climate Change adaptation and mitigation capacities of vulnerable communities in eco-villages of different ecosystems of the Uluguru Mountains", "Morogoro DC", 326),
		cell("Eradicating the worst forms of labour in the 8 mining wards of Geita district", "Geita DC", 336),
		cell("Facilitation of National REDD Strategy PHASE 2", "Dar es Salaam CC", 259),
		cell("Fighting Child Labour in Zanzibar", "Urban West- Unguja", 385),
		cell("Foundation for Civil Society (FCS)-Currently Edited by Mwakisu", "Dar es Salaam CC", 259),
		cell("Framework Contract International Law and Policy Institute", "Dar es Salaam CC", 259),
		cell("IGF : biodiversity and Land use", "Babati DC", 304),
		cell("Improving incomes, market access, and disaster preparedness: A rapid response to food insecurity in Shinyanga, Tanzania", "Shinyanga MC", 362),
		cell("Institutional support to the MoHA for the cordination of the Old Settlements", "Dar es Salaam CC", 259),
		cell("LESS is more: Labour Empowerment and Social Services for vulnerable people in Dar es Salaam", "Dar es Salaam CC", 259),
		cell("PAAP - Policy Advocacy and Analysis Programme - EDITED BY MOLOLO", "Bagamoyo DC", 252),
		cell("Planning Coordination Programme (Field Services)", "Iringa DC", 270),
		cell("Planning Coordination Programme (Field Services)", "Iringa RAS", 272),
		cell("Planning Coordination Programme (Field Services)", "Makete DC", 275),
		cell("Planning Coordination Programme (Field Services)", "Mbarali DC", 319),
		cell("Planning Coordination Programme (Field Services)", "Mbeya DC", 321),
		cell("Planning Coordination Programme (Field Services)", "Mufindi DC", 276),
		cell("Planning Coordination Programme (Field Services)", "Njombe DC", 277),
		cell("Planning Coordination Programme (Field Services)", "Njombe RAS", 343),
		cell("Planning Coordination Programme (Field Services)", "Temeke MC", 262),
		cell("Promoting Heritage Resources in kilwa to Strengthen Social and Economic Development", "Kilwa DC", 298),
		cell("REDD PILOT PROJECT BY WWF", "Dar es Salaam CC", 259),
		cell("Refugees-Police Package in the refugee camps in North-West Tanzania-MoHA", "Kasulu DC", 287),
		cell("Resilient Landscapes for Resilient Communities in Pemba", "North Pemba", 381),
		cell("Resource Policy Oriented Development Research", "Dar es Salaam CC", 259),
		cell("Resource Revenue Management Capacity", "Dar es Salaam CC", 259),
		cell("Same And 'Mwanga' Districts Water Supply Project", "Same DC", 296),
		cell("Small Hydro- Kinhansi", "Dar es Salaam CC", 259),
		cell("SOUTHERN AGRICULTURAL GROWTH CORRIDOR – (SAGCOT Centre Ltd)", "Kilombero DC", 324),
		cell("Strengthening Local Capacities for development coordination and micro projects in North-western Tanzania", "Ngara DC", 286),
		cell("Support to Primary Education in Drought Prone & Pastoral Areas-Edited by Matembele", "Babati DC", 304),
		cell("Support to Primary Education in Drought Prone & Pastoral Areas-Edited by Matembele", "Chamwino DC", 264),
		cell("Support to Primary Education in Drought Prone & Pastoral Areas-Edited by Matembele", "Iramba DC", 363),
		cell("Support to Primary Education in Drought Prone & Pastoral Areas-Edited by Matembele", "Karatu DC", 247),
		cell("Support to Primary Education in Drought Prone & Pastoral Areas-Edited by Matembele", "Kishapu DC", 358),
		cell("Support to Primary Education in Drought Prone & Pastoral Areas-Edited by Matembele", "Kitelo DC", 307),
		cell("Support to Primary Education in Drought Prone & Pastoral Areas-Edited by Matembele", "Monduli DC", 250),
		cell("Support to Primary Education in Drought Prone & Pastoral Areas-Edited by Matembele", "Ngorongoro DC", 251),
		cell("Support to Primary Education in Drought Prone & Pastoral Areas-Edited by Matembele", "Simanjiro DC", 309),
		cell("Support to Primary Education in Drought Prone & Pastoral Areas-Edited by Matembele", "Singida DC", 365),
		cell("SUPPORT TO STRATEGIC ACTION PLAN FOR VOCATIONAL EDUCATION & TRAINING", "Direct Payment", 140),
		cell("Tanzania Capital Flight Study", "Dar es Salaam CC", 259),
		cell("Unsung Heroines", "Dar es Salaam CC", 259),
		cell("Urban Water Supply Songea Phase. II", "Songea MC", 353),
		cell("Vocational and Community Level Training on PFM, REDD and Climate Change", "Dar es Salaam CC", 259),
		cell("Water supply and sewerage for Singida town", "Singida MC", 366),
		cell("WCS-REDD Readness in Southwest Tanzania", "Sumbawanga DC", 347),
		cell("Young Scientist", "Dar es Salaam CC", 259));
	}

}
