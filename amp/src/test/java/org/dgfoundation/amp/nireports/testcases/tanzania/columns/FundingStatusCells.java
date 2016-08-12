package org.dgfoundation.amp.nireports.testcases.tanzania.columns;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.testcases.generic.HardcodedCells;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.TextCell;


public class FundingStatusCells extends HardcodedCells<TextCell>{

	public FundingStatusCells(Map<String, Long> activityNames, Map<String, Long> entityNames, NiDimension dim, String key) {
		super(activityNames, entityNames, degenerate(dim, key));
	}
	public FundingStatusCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
		super(activityNames, entityNames, lc);
	}

	@Override
	protected List<TextCell> populateCells() {
		return 	Arrays.asList(
		cell("(JICA) (TA-P10) Iringa-Shinyanga Backbone Transmission Investment Project", "Iringa", 224),
		cell("(JICA) Rural Water Supply Project in Tabora Region", "Tabora", 241),
		cell("51170055  Zanzibar Legal Services 2013 – 2018", "zanzibar", 243),
		cell("54020090 - EcoEnergy Tanzania 2014 - 2015", "Coast", 221),
		cell("Adapting to climate change in coastal Dar es Salaam", "Dar-es-Salaam", 222),
		cell("ALTERNATIVE LEARNING SKILLS DEVELOPMENT PROGRAMME (ALSDP)CURRENTLY EDITED BY SHARIF", "zanzibar", 243),
		cell("ALTERNATIVE LEARNING SKILLS DEVELOPMENT PROJECT II (ALSD II) - ZANZIBAR", "zanzibar", 243),
		cell("APPLIED TECHNICAL RESEARCH STANDARDIZATION AND DISSEMINATION PRGRAMME", "zanzibar", 243),
		cell("ASRH access to family planning (UNFPA URT7U309) - AGPAHI Direct Project Funds (DPF", "Shinyanga", 239),
		cell("ASRH access to family planning (UNFPA URT7U309) - KIWOHEDE Direct Project Funds (DPF)", "Shinyanga", 239),
		cell("Audit of Hedging Transactions", "Dar-es-Salaam", 222),
		cell("Beekeeper Economic Empowerment (BEE) Project, Tabora, Tanzania", "Tabora", 241),
		cell("Capacity Development in Specialized Tax Administration", "Dar-es-Salaam", 222),
		cell("Chalinze Water supply", "Coast", 221),
		cell("Construction of Three Roads in Pemba Island.", "zanzibar", 243),
		cell("Construction of two Secondary schools in Zanzibar", "zanzibar", 243),
		cell("Documenting of Tanzanian Art Collection", "Dar-es-Salaam", 222),
		cell("East African Legislative Assembly (EALA) /AWEPA", "Dar-es-Salaam", 222),
		cell("Eastern arc mountains conservation fund ( EAMCEF)", "Morogoro", 232),
		cell("EDUCATION -[EQUAL]", "Coast", 221),
		cell("EDUCATION -[EQUAL]", "Iringa", 224),
		cell("EDUCATION -[EQUAL]", "Kigoma", 226),
		cell("EDUCATION -[EQUAL]", "Kilimanjaro", 227),
		cell("EDUCATION -[EQUAL]", "Mbeya", 231),
		cell("EDUCATION -[EQUAL]", "mtwara", 233),
		cell("EDUCATION -[EQUAL]", "MWANZA", 234),
		cell("EDUCATION -[EQUAL]", "Njombe Region", 235),
		cell("Empowering Vulnerable Rural Communities to Adapt and Mitigate the Impacts of Climate Change in Central Tanzania", "Dodoma", 223),
		cell("Enhancing Climate Change adaptation and mitigation capacities of vulnerable communities in eco-villages of different ecosystems of the Uluguru Mountains", "Morogoro", 232),
		cell("Eradicating the worst forms of labour in the 8 mining wards of Geita district", "MWANZA", 234),
		cell("Expand access to and availability of family planning services (UNFPA URT7R202)- MST Direct Project Funds (DPF)", "zanzibar", 243),
		cell("Expand access to and availability of family planning services (UNFPA URT7R204)- PSI Direct Project Funds (DPF)", "Shinyanga", 239),
		cell("Facilitation of National REDD Strategy PHASE 2", "Dar-es-Salaam", 222),
		cell("Fighting Child Labour in Zanzibar", "zanzibar", 243),
		cell("Foundation for Civil Society (FCS)-Currently Edited by Mwakisu", "Dar-es-Salaam", 222),
		cell("Framework Contract International Law and Policy Institute", "Dar-es-Salaam", 222),
		cell("IGF : biodiversity and Land use", "Manyara", 229),
		cell("Improving incomes, market access, and disaster preparedness: A rapid response to food insecurity in Shinyanga, Tanzania", "Shinyanga", 239),
		cell("INSTITUTIONAL SUPPORT PROJECT FOR GOOD GOVERNANCE II (ISPG 2)", "zanzibar", 243),
		cell("Institutional support to the MoHA for the cordination of the Old Settlements", "Dar-es-Salaam", 222),
		cell("Lake victoria Water and Sanitation- Bukoba, Musoma, Mwanza", "MWANZA", 234),
		cell("LESS is more: Labour Empowerment and Social Services for vulnerable people in Dar es Salaam", "Dar-es-Salaam", 222),
		cell("Loliondo/Serengeti Development Programme/KfW", "Arusha", 220),
		cell("MAINTENANCE IN ZECO", "zanzibar", 243),
		cell("Maji kwa afya ya jamii (MKAJI)", "Dodoma", 223),
		cell("PAAP - Policy Advocacy and Analysis Programme - EDITED BY MOLOLO", "Pwani", 236),
		cell("Planning Coordination Programme (Field Services)", "Dar-es-Salaam", 222),
		cell("Planning Coordination Programme (Field Services)", "Iringa", 224),
		cell("Planning Coordination Programme (Field Services)", "Mbeya", 231),
		cell("Planning Coordination Programme (Field Services)", "Njombe Region", 235),
		cell("Prevention of HIV, other sexually transmitted infections and health related perceptions, reflections, experiences and practices among men having sex with men in Dar es Salaam", "Dar-es-Salaam", 222),
		cell("Promoting Heritage Resources in kilwa to Strengthen Social and Economic Development", "Lindi", 228),
		cell("REDD PILOT PROJECT BY WWF", "Dar-es-Salaam", 222),
		cell("Refugees-Police Package in the Old Settlements-MoHA", "Tabora", 241),
		cell("Refugees-Police Package in the refugee camps in North-West Tanzania-MoHA", "Kigoma", 226),
		cell("Resilient Landscapes for Resilient Communities in Pemba", "zanzibar", 243),
		cell("Resource Policy Oriented Development Research", "Dar-es-Salaam", 222),
		cell("Resource Revenue Management Capacity", "Dar-es-Salaam", 222),
		cell("Same And 'Mwanga' Districts Water Supply Project", "Kilimanjaro", 227),
		cell("Small Hydro- Kinhansi", "Dar-es-Salaam", 222),
		cell("SNV Tanzania", "Lindi", 228),
		cell("Songwe Airport Project in Mbeya", "Mbeya", 231),
		cell("SOUTHERN AGRICULTURAL GROWTH CORRIDOR – (SAGCOT Centre Ltd)", "Morogoro", 232),
		cell("State University of Zanzibar (SUZA.", "zanzibar", 243),
		cell("Strengthening Local Capacities for development coordination and micro projects in North-western Tanzania", "Kagera", 225),
		cell("SUPPORT TO MATERNAL MORTALITY REDUCTION PROJECT CURRENTLY EDITED BY SHARIFF", "zanzibar", 243),
		cell("Support to Primary Education in Drought Prone & Pastoral Areas-Edited by Matembele", "Arusha", 220),
		cell("Support to Primary Education in Drought Prone & Pastoral Areas-Edited by Matembele", "Dodoma", 223),
		cell("Support to Primary Education in Drought Prone & Pastoral Areas-Edited by Matembele", "Manyara", 229),
		cell("Support to Primary Education in Drought Prone & Pastoral Areas-Edited by Matembele", "Shinyanga", 239),
		cell("Support to Primary Education in Drought Prone & Pastoral Areas-Edited by Matembele", "Singida", 240),
		cell("Tanzania Capital Flight Study", "Dar-es-Salaam", 222),
		cell("Tanzania Extractive Industries Transparency Initiative II (TEITI II) A035513-001", "Dar-es-Salaam", 222),
		cell("TANZANIA ROAD SECTOR SUPPORT PROGRAMME II (RSSP II)", "zanzibar", 243),
		cell("The Project for Development of Mariculutre in Zanzibar", "zanzibar", 243),
		cell("UNDAP Economic Growth Programme Working Group (ILO)", "zanzibar", 243),
		cell("UNDAP EPR- Support to Emergency Preparedness and Response", "zanzibar", 243),
		cell("Unsung Heroines", "Dar-es-Salaam", 222),
		cell("Urban Water Supply Songea Phase. II", "Ruvama", 238),
		cell("Vocational and Community Level Training on PFM, REDD and Climate Change", "Dar-es-Salaam", 222),
		cell("Water supply and sewerage for Singida town", "Singida", 240),
		cell("WCS-REDD Readness in Southwest Tanzania", "Rukwa", 237),
		cell("Young Scientist", "Dar-es-Salaam", 222),
		cell("Zanzibar built heritage job creation", "zanzibar", 243),
		cell("ZANZIBAR MNAZI MMOJA HOSPITAL PEDIATRIC WING", "zanzibar", 243),
		cell("ZANZIBAR POLITICAL DEVELOPMENTS.", "zanzibar", 243),
		cell("ZANZIBAR URBAN WATER & SANITATION PROJECT", "zanzibar", 243),
		cell("ZANZIBAR WATER AND SANITATION PROJECT", "zanzibar", 243),
		cell("ZANZIBAR WOODS BIOMASS SURVEY", "zanzibar", 243),
		cell("ZLSC CORE SUPPORT ZANZIBAR LEGAL SERVICES CENTRE", "zanzibar", 243));
	}

}
