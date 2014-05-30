package org.dgfoundation.amp.ar.moldovamigration;

import java.util.*;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;

import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedPropertyDescription;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.SiteCache;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpStructureType;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;

public class MoldovaTranslationsSplit
{
	public final static String SQL_UTILS_NULL = "###NULL###";
	public static boolean DO_UPDATES = true;
	public ArrayList<TranslationWarningMessage> warningMessages = new ArrayList<TranslationWarningMessage>();
	
	public void doMoldovaTranslations()
	{
//		doWorkspaceNamesTranslations();
//		doProjectNamesTranslations();
//		doPurposesTranslations();
//		doProjectImpactTranslations();
//		doProjectDescriptionTranslations();
//		doProjectObjectivesTranslations();
//		doProjectResultsTranslations();
//		doProjectActivitySummaryTranslations();
//		
//		removeRomanianProjectTitleAsAColumn();
//		renameProjectTitleColumns();
//		disableContractingArrangements();
//		enableResultsColumnInReports();
//		
//		translateStructureTypes();
//		hardcodeSomeTranslations();
//		
//		System.out.format("conversion done with %d warning messages\n", warningMessages.size());
//		for(int i = 0; i < warningMessages.size(); i++)
//			//System.out.println(warningMessages.get(i).toString());
		doOrganisationNamesTranslations();
		
		try
		{
			PersistenceManager.getRequestDBSession().getTransaction().commit();
			PersistenceManager.getRequestDBSession().close();
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		throw new RuntimeException("gata pe azi");
	}
		
	protected void doOrganisationNamesTranslations()
	{
		TranslationsPack pack;
		pack = new TranslationsPack(21192L, "ACADEMY OF SCIENCES","Academia de Științe");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21325L, "Argidius Foundation","Fundația Argidius");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20748L, "Republic of Austria","Republica Austria");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21421L, "Austrian Development Agency","Agenția de Dezvoltare a Austriei");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21029L, "Bio Carbon Fund","Fondul BioCarbon");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21178L, "BMZ - Federal Ministry For Cooperation","BMZ - Ministerul Federal pentru Cooperare");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20677L, "Canada","Canada");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20563L, "People's Republic of China","Republica Populară Chineză");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21358L, "Council of Europe","Consiliul Europei");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20703L, "Council of Europe Development Bank","Banca de Dezvoltare a Consiliului Europei");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21729L, "Czech Development Agency","Agenția de Dezvoltare a Cehiei");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20537L, "Czech Republic","Republica Cehă");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20598L, "Delegation Of The European Union to Moldova","Delegația Uniunii Europene în Moldova");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21522L, "Kingdom of Denmark","Regatul Danemarcei");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20538L, "Department For International Development","Departamentul pentru Dezvoltare Internațională");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21337L, "Education, Audiovisual And Culture Executive Agency","Agenția Executivă pentru Educație, Audiovizual și Cultură");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20576L, "Embassy Of Japan In Moldova","Amabasada Japoniei în Moldova");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21604L, "ENPI CBC","ENPI POC");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21614L, "ENPI CBC BLACK SEA","ENPI POC Marea Neagră");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21600L, "ENPI CBC RO-UA-MD","ENPI POC RO-UA-MD");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21862L, "Estonia","Republica Estonia");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20656L, "European Bank For Reconstruction And Development","Banca Europeană pentru Reconstrucție și Dezvoltare");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21338L, "European Commission","Comisia Europeană");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20633L, "European Investment Bank","Banca Europeană de Investiții");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20999L, "European Union","Uniunea Europeană");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21863L, "Republic of Finland","Republica Finlanda");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21501L, "Food And Agriculture Organization","Organizația pentru Alimentație și Agricultură");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21864L, "French Republic","Republica Franceză");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20720L, "Federal Republic of Germany","Republica Federală Germană");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20870L, "GIZ - German Development Cooperation","Agentia de Cooperare Internationala a Germaniei ");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21764L, "Global Alliance for Vaccines and Immunisation","Alianța Globală pentru Vaccinuri și Imunizare");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21379L, "Global Environment Facility","Fondul Global de Mediu");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20675L, "Government of P.R. of China","Guvernul Republicii Populare Chineze");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21865L, "Hungary","Ungaria");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21486L, "Institute For Transuranium Elements","Institutul pentru Elemente Transuraniene");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21566L, "International Atomic Energy Agency","Agenția Internațională pentru Energie Atomică");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20539L, "International Development Agency","Agenția Internațională pentru Dezvoltare");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21362L, "International Foundation 'Liechtenstein Development Service'","Fundația Internațională \"Liechtenstein Development Service\"");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21063L, "International Fund For Agricultural Development","Fondul Internațional pentru dezvoltare agricole");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21059L, "International Labour Organization","Organizația Internațională a Muncii");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20824L, "International Monetary Fund","Fondul Monetar Internațional");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21505L, "International Organization For Migration","Organizația Internațională pentru Migrație");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20884L, "International Telecommunication Union","Uniunea Internațională a Telecomunicațiilor");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21056L, "Italian Republic","Republica Italiană");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21136L, "Japan","Japonia");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21563L, "Japanese International Cooperation Agency","Agenția Japoneză pentru Cooperare Internațională");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20556L, "Japan Social Investment Fund","Fondul de Investiții Sociale Japonez");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21552L, "Joint United Nations Programme on HIV/AIDS","Programul Comun al Națiunilor Unite privind HIV / SIDA");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20805L, "Reconstruction Credit Institute KFW","Institutul de Credit pentru Reconstrucție KFW");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20921L, "Konrad Adenauer Foundation","Fundația Konrad Adenauer");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21019L, "State of Kuwait","Statul Kuwait");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21739L, "Kuwait Fund for Economic Development","Fondul Kuwait pentru Dezvoltare Economică");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20615L, "Republic of Latvia ","Republica Letonia");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21149L, "Latvian State Chancellery","Cancelaria de Stat a Letoniei");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21382L, "Principality of Liechtenstein","Principatul Liechtenstein");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21254L, "Republic of Lithuania","Republica Lituania");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21214L, "Millenium Challenge Corporation","Corporația Provocările Mileniului");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20631L, "Ministry of Environment","Ministerul Mediului");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21309L, "Ministry of Finance","Ministerul Finanțelor");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21488L, "Ministry of Internal Affairs","Ministerul Afacerilor Interne");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21513L, "Ministry Of Labour, Social Protection And Family","Ministerul Muncii, Protecției Sociale și Familiei");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21537L, "Neighbourhood Investment Facility","Fondul de Investiţii în Vecinătate ");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20922L, "Kingdom of the Netherlands","Regatul Olandei");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20523L, "Netherlands Ministry of Foreign Affairs","Ministerul Afacerilor Externe al Olandei");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21577L, "Kingdom of Norway","Regatul Norvegiei");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20698L, "Norwegian Agency For Development","Agenția Pentru Dezvoltare a Norvegiei");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20847L, "Republic of Poland","Republica Polonia");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21230L, "Romania","România");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21523L, "Slovac Agency For International Cooperation In Development","Agenția slovacă pentru cooperare internațională în dezvoltare");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21866L, "Slovak Republic","Republica Slovacă");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21451L, "Kingdom of Sweden","Regatul Suediei");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20904L, "Swedish Agency for Economic and Regional Growth","Agenția suedeză pentru dezvoltare economică și dezvoltare regională");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20715L, "Swedish International Development Authority","Autoritatea Suedeză pentru Dezvoltare Internațională");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20535L, "Swedish Radiation Safety Authority","Autoritatea Suedeză pentru Siguranța Radiațiilor");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21404L, "Swiss Agency For Development And Cooperation","Agenția Elvețiană pentru Dezvoltare și Cooperare");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21006L, "Swiss Confederation","Confederația Elvețiană");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21616L, "TACIS","TACIS");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21084L, "TEMPUS","TEMPUS");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21517L, "The Global Fund to Fight AIDS, Tuberculosis and Malaria","Fondul Global pentru Combaterea SIDA, Tuberculozei și Malariei");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21250L, "The United Nations High Commissioner for Refugees","Înaltul Comisariat al Națiunilor Unite pentru Refugiați");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21439L, "The United Nations Population Fund","Fondul Națiunilor Unite pentru Populație");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21461L, "Turkish International Cooperation Agency","Agenția Turcă de Cooperare Internațională");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20551L, "Turkey","Turcia");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20898L, "United Kingdom","Regatul Unit");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21091L, "United Nations","Organizația Națiunilor Unite");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21082L, "United Nations Children's Fund","Fondul Națiunilor Unite pentru Copii");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20752L, "United Nations Development Programme","Programul Națiunilor Unite pentru Dezvoltare");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20660L, "United Nations Environment Programme","Programul de Mediu al Națiunilor Unite");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20780L, "United Nations Institute For Training And Research","Institutul Națiunilor Unite pentru Formare și Cercetare");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21118L, "United Nations Office on Drugs and Crime","Biroul Națiunilor Unite pentru Droguri și Criminalitate");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20593L, "United States Agency For International Development","Agenția Statelor Unite pentru Dezvoltare Internațională");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20796L, "United States Department Of State","Departamentul de Stat al Statelor Unite");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20691L, "United States of America","Statele Unite ale Americii ");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20548L, "US Department Of Defence","Departamentul Apărării al Statelor Unite");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20873L, "US Department Of Energy","Departamentul Energetic al Statelor Unite");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(20627L, "US Nuclear Regulatory Commission","Comisia de Reglementare Nucleară a Statelor Unite");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21344L, "World Bank","Banca Mondială");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
		pack = new TranslationsPack(21482L, "World Health Organization","Organizația Mondială a Sănătății");savePlain(pack, InternationalizedModelDescription.getForProperty(AmpOrganisation.class,"name"));
	}
	
	protected void translateAs(String englishTranslation, String translation, String locale)
	{
		try
		{
//			Message msg = new Message();
//			msg.setSite(SiteCache.lookupById(3L));
//			msg.setLocale(locale);
//			msg.setMessage(translation);
//			msg.setKeyWords(null);
//			msg.setKey(TranslatorWorker.generateTrnKey(englishTranslation));
			String msgKey = TranslatorWorker.generateTrnKey(englishTranslation);
			executeQuery("DELETE FROM dg_message WHERE site_id = '3' AND lang_iso = '" + locale + "' AND message_key='" + msgKey + "'");
			executeQuery("INSERT INTO dg_message(site_id, lang_iso, message_key, message_utf8) VALUES ('3', '" + locale + "', '" + msgKey + "', '" + translation + "')");
			//TranslatorWorker.getInstance("").save(msg);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	protected void hardcodeSomeTranslations()
	{
		translateAs("and", "şi", "ro");
		translateAs("Poverty", "Sărăcie", "ro");
		translateAs("Population", "Populaţie", "ro");
		translateAs("Showing commitments for region", "Angajamentele pentru raion / municipiu", "ro");
		translateAs("Showing disbursements for region", "Debursările pentru raion / municipiu", "ro");
		translateAs("Click here to see national projects", "Apăsaţi aici pentru a vedea proiectele de rang naţional", "ro");
		translateAs("What data should the map show?", "Ce fel de date doriţi să fie afişate pe hartă?", "ro");
		translateAs("Regions With Zones", "Raioane cu localităţi", "ro");
		translateAs("Project Status", "Stadiu proiect", "ro");
		translateAs("Currency Type", "Valută calcule", "ro");
	}
	
	protected void translateStructureTypes()
	{
		List<TranslationsPack> translations = new ArrayList<TranslationsPack>();
		translations.add(new TranslationsPack(1L,  "Agriculture, fishing, and forestry", "Agricultură, pescuit și silvicultură"));
		translations.add(new TranslationsPack(2L,  "Energy and mining", "Energie și minerit"));
		translations.add(new TranslationsPack(3L,  "Health and other social services", "Sănătate și alte servicii sociale"));
		translations.add(new TranslationsPack(4L,  "Public administration, law and Justice", "Administrație publică, lege și justiție"));
		translations.add(new TranslationsPack(5L,  "Water, sanitation and flood protection", "Apă, sistem sanitar și protecție la inundații"));
		translations.add(new TranslationsPack(7L,  "Airport", "Aeroport"));
		translations.add(new TranslationsPack(8L,  "Climate change", "Schimbări climatice"));
		translations.add(new TranslationsPack(9L,  "Communications", "Comunicații"));
		translations.add(new TranslationsPack(10L,  "Education", "Educaţie"));
		translations.add(new TranslationsPack(11L,  "Employment", "Ocuparea forței de muncă"));
		translations.add(new TranslationsPack(12L,  "Forestry", "Silvicultură"));
		translations.add(new TranslationsPack(13L,  "Governance", "Administraţie"));
		translations.add(new TranslationsPack(14L,  "Hospital", "Spital"));
		translations.add(new TranslationsPack(15L,  "Housing", "Locuinţă"));
		translations.add(new TranslationsPack(16L,  "Infant and Maternal Health", "Sănătatea mamei şi a copilului"));
		translations.add(new TranslationsPack(17L,  "Infrastructure", "Infrastructură"));
		translations.add(new TranslationsPack(18L,  "Local development", "Dezvoltare regională"));
		translations.add(new TranslationsPack(19L,  "Police", "Poliţie"));
		translations.add(new TranslationsPack(20L,  "Road", "Drum"));
		translations.add(new TranslationsPack(21L,  "School", "Şcoală"));
		
		for(TranslationsPack pack:translations)
		{
			savePlain(pack, InternationalizedModelDescription.getForProperty(AmpStructureType.class, "name"));
		}
	}
	
	protected void renameProjectTitleColumns()
	{
		executeQuery("UPDATE dg_message SET message_utf8='Aranjamente Contractuale' WHERE message_utf8 = '" + "Titlul proiectului ( română )" + "'");
		executeQuery("UPDATE dg_message SET message_utf8='Titlul proiectului'WHERE message_utf8 = '" + "Titlul proiectului ( engleză )" + "'");
		
		executeQuery("UPDATE dg_message SET message_utf8='Contracting Arrangements' WHERE message_utf8 = '" + "Romanian Project Title" + "'");
		executeQuery("UPDATE dg_message SET message_utf8='Project Title' WHERE message_utf8 = '" + "Project Title (English)" + "'");
	}
	
	protected void disableContractingArrangements()
	{
		int CONTRACTING_ARRANGEMENTS_MODULE_VISIBILITY_ID = 208;
		int CONTRACTING_ARRANGEMENTS_FIELDS_VISIBILITY_ID = 718;
		
		executeQuery("DELETE FROM amp_modules_templates WHERE module = " + CONTRACTING_ARRANGEMENTS_MODULE_VISIBILITY_ID);
		executeQuery("DELETE FROM amp_fields_templates WHERE field = " + CONTRACTING_ARRANGEMENTS_FIELDS_VISIBILITY_ID);		
		//"/Activity Form/Identification/Contracting Arrangements"
	}
	
	protected void enableResultsColumnInReports()
	{
		int RESULTS_FIELDS_VISIBILITY_ID = 370;
		executeQuery("DELETE FROM amp_fields_templates WHERE field = " + RESULTS_FIELDS_VISIBILITY_ID); // this is expected to do nothing
		executeQuery("INSERT INTO amp_fields_templates (template, field) VALUES (1, " + RESULTS_FIELDS_VISIBILITY_ID + ")"); // this is expected to do nothing
	}
	
	protected void removeRomanianProjectTitleAsAColumn()
	{
		//System.out.println("replacing all references to column ROMANIAN_PROJECT_TITLE to references to PROJECT_TITLE");
		
		// ROMANIAN PROJECT TITLE: amp_column_id = 276
		long ROMANIAN_PROJ_TITLE_COLUMN_ID = 276;
		long PROJECT_TITLE_COLUMN_ID = 4;
		
		List<Object[]> cols = getResults("SELECT amp_report_id, columnid, order_id FROM amp_report_column");
		Map<Long, SortedMap<Long, Long>> colsByReport = new HashMap<Long, SortedMap<Long, Long>>(); //Map<amp_report_id, Map<amp_column_id, order>>
		for(Object[] col:cols)
		{
			long amp_report_id = getLong(col[0]);
			long amp_column_id = getLong(col[1]);
			long column_order = getLong(col[2]);
			if (!colsByReport.containsKey(amp_report_id))
				colsByReport.put(amp_report_id, new TreeMap<Long, Long>());
			colsByReport.get(amp_report_id).put(amp_column_id, column_order);
		}
		for(Long amp_report_id:colsByReport.keySet())
		{
			SortedMap<Long, Long> reportCols = colsByReport.get(amp_report_id);
			if (!reportCols.containsKey(ROMANIAN_PROJ_TITLE_COLUMN_ID))
				continue;
			//System.out.println("redoing report with amp_report_id = " + amp_report_id + ", which has the column RO-PROJ-TITLE with order_id = " + reportCols.get(ROMANIAN_PROJ_TITLE_COLUMN_ID));
			if (!reportCols.containsKey(PROJECT_TITLE_COLUMN_ID))
			{
				//System.out.println("\tproject does not contain the PROJECT TITLE column, just replacing RO-PROJECT-TITLE with PROJECT-TITLE");
				executeQuery("UPDATE amp_report_column set columnid = " + PROJECT_TITLE_COLUMN_ID + " WHERE amp_report_id = " + amp_report_id + " AND columnid = " + ROMANIAN_PROJ_TITLE_COLUMN_ID);
				continue;
			}
			// remove ROMANIAN PROJECT TITLE and renumber the following columns
			//System.out.println("\tproject contains the PROJECT TITLE column, left-shifting all columns which appear after RO_PROJ_TITLE in the report");
			executeQuery("DELETE FROM amp_report_column WHERE amp_report_id = " + amp_report_id + " AND columnid = " + ROMANIAN_PROJ_TITLE_COLUMN_ID);
			executeQuery("UPDATE amp_report_column SET order_id = order_id - 1 WHERE amp_report_id = " + amp_report_id + " AND order_id > " + reportCols.get(ROMANIAN_PROJ_TITLE_COLUMN_ID));
		}
	}
	
	/**
	 * v_purposes
	 */
	protected void doPurposesTranslations()
	{
		List<TranslationsPack> translations = collectTranslations("purpose", false, '/');
		//System.out.println("=== PROJECT PURPOSES ===");
		for(TranslationsPack pack: translations)
		{
			//System.out.println(pack);
			saveDgEditorTranslation(pack);
		}
	}

	/**
	 * v_description
	 */
	protected void doProjectDescriptionTranslations()
	{
		List<TranslationsPack> translations = collectTranslations("description", true, '/');
		//System.out.println("=== PROJECT DESCRIPTIONS ===");
		for(TranslationsPack pack: translations)
		{
			//System.out.println(pack);
			saveDgEditorTranslation(pack);
		}
	}
	
	/**
	 * v_objectives
	 */
	protected void doProjectObjectivesTranslations()
	{
		List<TranslationsPack> translations = collectTranslations("objectives", true, '/');
		//System.out.println("=== PROJECT OBJECTIVES ===");
		for(TranslationsPack pack: translations)
		{
			//System.out.println(pack);
			saveDgEditorTranslation(pack);
		}
	}	

	/**
	 * v_results
	 */
	protected void doProjectResultsTranslations()
	{
		List<TranslationsPack> translations = collectTranslations("results", true, '/');
		//System.out.println("=== PROJECT RESULTS ===");
		for(TranslationsPack pack: translations)
		{
			//System.out.println(pack);
			saveDgEditorTranslation(pack);
		}
	}	

	/**
	 * v_proj_impact  AKA Action Components
	 */
	protected void doProjectImpactTranslations()
	{
		List<TranslationsPack> translations = collectTranslations("project_impact", true, '/');
		//System.out.println("=== PROJECT IMPACT SUMMARIES ===");
		for(TranslationsPack pack: translations)
		{
			//System.out.println(pack);
			saveDgEditorTranslation(pack);
		}
	}	
	
	/**
	 * NO VIEW FOR THIS
	 */
	protected void doProjectActivitySummaryTranslations()
	{
		List<TranslationsPack> translations = collectTranslations("activity_summary", true, '/');
		//System.out.println("=== PROJECT ACTIVITY SUMMARIES ===");
		for(TranslationsPack pack: translations)
		{
			//System.out.println(pack);
			saveDgEditorTranslation(pack);
		}
	}	

	protected void saveDgEditorTranslation(TranslationsPack pack)
	{
		if (!DO_UPDATES)
		{
			//System.out.println("NOT UPDATING dg_editor, disabled");
			return;
		}
		if (pack.englishTranslation != null)
			saveOrUpdateDgEditor(pack.englishTranslation, pack.editorId, "en");
		
		if (pack.romanianTranslation != null)
			saveOrUpdateDgEditor(pack.romanianTranslation, pack.editorId, "ro");
	}
	
	protected void saveOrUpdateDgEditor(String text, String editorId, String locale)
	{
		if (editorId == null)
			throw new RuntimeException("null editorId not allowed in here");
		
		String updateQuery = String.format("UPDATE dg_editor SET body = %s WHERE site_id='amp' AND editor_key='%s' AND language='%s'", stringifyObject(text), editorId, locale);
		int updatedRows = executeQuery(updateQuery);
		if (updatedRows < 1)
		{
			// do the insert
			String insertQuery = String.format("INSERT INTO dg_editor(site_id, editor_key, language, last_mod_date, url, body, order_index) VALUES " + 
			"('amp', '%s', '%s', '2013-12-05', 'http://split-by-languages.org', %s, 0)", editorId, locale, stringifyObject(text));
			executeQuery(insertQuery);
		}
	}
	
	protected ArrayList<TranslationsPack> collectTranslations(String activityField, boolean removeNewLines, char separator)
	{
		String query = String.format("SELECT a.amp_activity_id, e.body AS ebody, a.%s AS editor_key, e.language AS locale " + 
		   "FROM amp_activity a, dg_editor e " + 
		   "WHERE a.%s = e.editor_key::text AND btrim(e.body) <> ''::text " + 
		  "ORDER BY a.amp_activity_id", activityField, activityField);
		
		List<Object[]> rawData = getResults(query);
		Map<Long, String> variants = new TreeMap<Long, String>(); // Map<ampActivityId, text>
		Map<Long, String> editorKeys = new TreeMap<Long, String>(); // Map<ampActivityId, editorKey>
		
		for(Object[] bla:rawData)
		{
			variants.put(getLong(bla[0]), "");
			editorKeys.put(getLong(bla[0]), "");
		}
		
		for(Object[] bla:rawData)
		{
			Long activityId = getLong(bla[0]);
			String text = removeHtml((String) bla[1], removeNewLines);
			String editorKey = (String) bla[2];
			if (variants.get(activityId).length() < text.length())
			{
				variants.put(activityId, text);
				editorKeys.put(activityId, editorKey);
			}
		}
		
		ArrayList<TranslationsPack> packs = new ArrayList<TranslationsPack>();
		
		for(Long activityKey:variants.keySet())
		{
			TranslationsPack pack = splitTranslations(activityKey, variants.get(activityKey), separator);
			pack = pack.setEditorKey(editorKeys.get(activityKey));
			packs.add(pack);			
		}
		return packs;
	}
	
	protected void doWorkspaceNamesTranslations()
	{
		String titlesQuery = "select amp_team_id, name FROM amp_team";
		List<Object[]> allTitles = getResults(titlesQuery);
		List<TranslationsPack> translations = new ArrayList<TranslationsPack>();
		
		for(int i = 0; i < allTitles.size(); i++)
		{
			Object[] line = allTitles.get(i);
			long id = getLong(line[0]);
			String mixedName = (String) line[1];
			
			TranslationsPack pack = splitTranslations(id, mixedName, '/');						
			translations.add(pack);
		}
		
		for(TranslationsPack pack: translations)
		{
			System.out.format("for workspace %d, the names are:\n\tEnglish: <%s>\n\tRomanian: <%s>\n", pack.id, pack.englishTranslation, pack.romanianTranslation);
		}
		for(TranslationsPack pack:translations)
		{
			savePlain(pack, InternationalizedModelDescription.getForProperty(AmpTeam.class, "name"));
		}
	}
	
	protected void doProjectNamesTranslations()
	{
		String titlesQuery = "select tit.amp_activity_id AS en_id, tit.name as en_name, romtit.amp_activity_id as ro_id, romtit.body AS ro_name FROM v_titles tit FULL OUTER JOIN v_contracting_arrangements romtit ON tit.amp_activity_id = romtit.amp_activity_id";
		List<Object[]> allTitles = getResults(titlesQuery);
		List<TranslationsPack> translations = new ArrayList<TranslationsPack>();
		
		for(int i = 0; i < allTitles.size(); i++)
		{
			Object[] line = allTitles.get(i);
			Long englishId = getLong(line[0]);
			String englishName = (String) line[1];
			Long romanianId = getLong(line[2]);
			String romanianName = (String) line[3];
			
			translations.add(new TranslationsPack(englishId, englishName, romanianId, removeHtml(romanianName, true)));
		}
		
		for(TranslationsPack pack: translations)
		{
			System.out.format("for activity %d, the names are:\n\tEnglish: <%s>\n\tRomanian: <%s>\n", pack.id, pack.englishTranslation, pack.romanianTranslation);
		}
		for(TranslationsPack pack:translations)
		{
			savePlain(pack, InternationalizedModelDescription.getForProperty(AmpActivityVersion.class, "name"));
			executeQuery("UPDATE amp_activity_version SET contracting_arrangements = null WHERE amp_activity_id = " + pack.id);
		}
	}
	
	protected TranslationsPack splitTranslations(long id, String text, char separator)
	{
		String[] translations = splitInTwo(text, separator);
		
		if (translations[0] == null && translations[1] == null)
			return new TranslationsPack(id, null, null);
		
		if (translations[0] == null)
			translations[0] = translations[1];
		
		if (translations[1] == null)
			translations[1] = translations[0];
		
		return new TranslationsPack(id, translations[1], translations[0]); // Romanian first
	}
	
	protected String[] splitInTwo(String text, char separator)
	{
		String[] hardcodedRes = HardCodedStuff.checkHardcodedCases(text, separator);
		if (hardcodedRes != null)
			return hardcodedRes;
		
		if (text == null)
			return new String[] {null, null};
		
		text = text.trim();
		if (text.isEmpty())
			return new String[] {null, null};
		
		int pos = findBestPosition(text, separator);
		if (pos == -1)
		{
			return new String[] {text, null};
		}
		
		if (pos == 0) // text starts with separator - first language missing
			return new String[] {null, text.substring(1).trim()}; 
		
		if (pos == text.length() - 1) // text ends with separator - second language missing
			return new String[]{text.substring(0, pos).trim(), null};
		
		String first = text.substring(0, pos).trim();
		String second = text.substring(pos + 1).trim();
		
		return new String[] {first, second};
	}
	
	/**
	 * finds the position of a charater in a string closest to the middle of the said string
	 * @param text
	 * @param separator
	 */
	protected int findBestPosition(String text, char separator)
	{
		int rs = -1;
		
		int middle = text.length() / 2;
		for(int i = 0; i < text.length() / 2 + 2; i++)
		{
			int left = middle - i;
			int right = middle + i;
			
			if (left >= 0 && text.charAt(left) == separator)
			{
				rs = left;
				break;
			}
			if (right < text.length() && text.charAt(right) == separator)
			{
				rs = right;
				break;
			}
		}
		if (rs == -1)
			return -1;

		if (rs < text.length() / 5 || rs > text.length() * 4 / 5)
			return -1;
		
//		if (rs >= 1 && rs < 10text.length() > 15 && (separator == '/' || separator == '-' || separator == ':'))
//			return -1; // do not split a date
		
		return rs;
	}
	
	/**
	 * <b>saves to the database</b> a plain field's translations
	 * @param pack
	 * @param property
	 */
	public final void savePlain(TranslationsPack pack, InternationalizedPropertyDescription property)
	{
		if (!DO_UPDATES)
		{
			//System.out.println("NOT updating, as it is disabled");
			return;
		}
		// write English translation
		String updateBaseTranslationQuery = String.format("UPDATE %s SET %s = %s WHERE %s = %d", property.modelTableName, property.modelColumnName, stringifyObject(pack.englishTranslation), property.modelTableId, pack.id);
		executeQuery(updateBaseTranslationQuery);
		
		String usedEnglishTranslation = pack.englishTranslation;
		String usedRomanianTranslation = pack.romanianTranslation;
		
		if (usedEnglishTranslation.length() > 253)
		{
			warningMessages.add(new TranslationWarningMessage(property, "en", "name longer than 253 chars: " + usedEnglishTranslation));			
			usedEnglishTranslation = usedEnglishTranslation.substring(0, 250) + "...";
		}
		
		if (usedRomanianTranslation.length() > 253)
		{
			warningMessages.add(new TranslationWarningMessage(property, "ro", "name longer than 253 chars: " + usedRomanianTranslation));
			usedRomanianTranslation = usedRomanianTranslation.substring(0, 250) + "...";
		}
		
		String updateEnglishTranslationQuery = String.format("UPDATE amp_content_translation SET translation = %s WHERE object_class = '%s' AND object_id = %d AND field_name = '%s' and locale='en'", 
				stringifyObject(usedEnglishTranslation), property.className, pack.id, property.propertyName);
		executeQuery(updateEnglishTranslationQuery);
		
		if (usedRomanianTranslation.equals(usedEnglishTranslation))
		{
			// Romanian and English translations match -> delete any existing Romanian translation
			String deleteRomanianTranslationQuery = String.format("DELETE FROM amp_content_translation WHERE object_class = '%s' AND object_id = %d AND field_name = '%s' and locale='ro'", 
					property.className, pack.id, property.propertyName);
			executeQuery(deleteRomanianTranslationQuery);
		}
		else
		{
			// Romanian and English translations do not match -> update / add Romanian translation
			String updateRomanianTranslationQuery = String.format("UPDATE amp_content_translation SET translation = %s WHERE object_class = '%s' AND object_id = %d AND field_name = '%s' and locale='ro'", 
				stringifyObject(usedRomanianTranslation), property.className, pack.id, property.propertyName);
			int numberAffected = executeQuery(updateRomanianTranslationQuery);
			if (numberAffected < 1)
			{
				// update did nothing, e.g. there is no Romanian translation existing in the database
				String createRomanianTranslationQuery = String.format("INSERT INTO amp_content_translation(id, object_class, object_id, field_name, locale, translation) VALUES(nextval('amp_content_translation_seq'), '%s', %d, '%s', 'ro', %s)", 
					 property.className, pack.id, property.propertyName, stringifyObject(usedRomanianTranslation));
				executeQuery(createRomanianTranslationQuery);
			}
		}
		
	}
	
	/**
	 * returns an escaped String good for passing to postgres in a value list
	 * @param obj
	 * @return
	 */
	public static String stringifyObject(Object obj)
	{
		if (obj instanceof Number)
			return obj.toString();
		else if (obj instanceof String)
		{
			if (obj.toString().equals(SQL_UTILS_NULL))
				return "NULL";
			//$t$blablabla$t$ - dollar-quoting
			//return "'" + obj.toString() + "'";
			String dollarQuote = "$fsdfdsfdsfsdjifusdifdskfjdskfjks$";
			return dollarQuote + obj.toString() + dollarQuote;
		}
		else
		{
			return "'" + obj.toString() + "'";
		}
	}
	
	/**
	 * extracts a Long from an object
	 * @param obj
	 * @return
	 */
	public final static Long getLong(Object obj)
	{
		if (obj == null)
			return null;
		if (obj instanceof Long)
			return (Long) obj;
		if (obj instanceof Number)
			return ((Number) obj).longValue();
		throw new RuntimeException("cannot convert object of class " + obj.getClass().getName() + " to long");
	}
	
	/**
	 * removes HTML formatting from a string
	 * @param src
	 * @param removeNewLines
	 * @return
	 */
	public final static String removeHtml(String src, boolean removeNewLines)
	{
		if (src == null)
			return null;
		src = src.trim();
		if (src.isEmpty())
			return null;
		Source htmlSource = new Source(src);
		Segment htmlSeg = new Segment(htmlSource, 0, htmlSource.length());
		Renderer htmlRend = new Renderer(htmlSeg);
		String result = htmlRend.toString();
		if (result == null)
			return null;
		result = result.trim();
		if (result.isEmpty())
			return null;
		
//		if (result.startsWith("Cooperarea parcurilor "))
//			//System.out.println("interrupted");
		if (removeNewLines)
			result = result.replace('\n', ' ').replace('\r', ' ').replaceAll("  ", " ");
		
		return result.replaceAll("  ", " ");
	}
	
	public static List<Object[]> getResults(final String query)
	{
		try
		{
			Session session = PersistenceManager.getRequestDBSession();
			final List<Object[]> result = new ArrayList<Object[]>();
			Work work = new Work()
			{
				@Override
				public void execute(Connection conn) throws SQLException
				{
					ResultSet res = conn.createStatement().executeQuery(query);
					int n = res.getMetaData().getColumnCount();
					while(res.next())
					{
						Object[] lineRes = new Object[n];
						for(int i = 0; i < n; i++)
							lineRes[i] = res.getObject(i + 1);
						result.add(lineRes);
					}
				}
			};
			session.doWork(work);
			return result;
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static int executeQuery(final String query)
	{
		//System.out.println("executing query " + query);
		try
		{
			Session session = PersistenceManager.getRequestDBSession();
			MyHibernateWork work = new MyHibernateWork(query);
			session.doWork(work);
			return work.getResult();
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}		
	}
}
