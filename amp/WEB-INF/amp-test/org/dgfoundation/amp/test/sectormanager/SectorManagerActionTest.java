package org.dgfoundation.amp.test.sectormanager;

import java.util.Collection;

import javax.servlet.ServletContext;

import org.dgfoundation.amp.test.util.Configuration;
import org.dgfoundation.amp.test.util.TestUtil;
import org.digijava.module.aim.action.UpdateSectorSchemes;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.form.AddSectorForm;
import org.digijava.module.aim.util.SectorUtil;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.struts.BasicActionTestCaseAdapter;
import org.apache.log4j.Logger;


public class SectorManagerActionTest extends BasicActionTestCaseAdapter {
    private static Logger logger = Logger.getLogger(SectorManagerActionTest.class);
	private AddSectorForm form;
	private UpdateSectorSchemes action;
	private static final String DEFAULT_ID="-1";
	private static final int ORDERBY_SECTOR_CODE = 1;
	private static final int ORDERBY_DESCRIPTION = 2;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Configuration.initConfig();
		action = new UpdateSectorSchemes();
		ServletContext context = getActionMockObjectFactory().getMockServletContext();
		context.setAttribute(UpdateSectorSchemes.class.getName(), action);
		form = (AddSectorForm)createActionForm(AddSectorForm.class);
		setValidate(false);
	}
	/**
	 * Checks if the collection returned by database is the same returned from UpdateSectorScheme
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void testSectorList() throws Exception {
		MockHttpServletRequest request = getActionMockObjectFactory().getMockRequest();
		TestUtil.setLocaleEn(request);
		TestUtil.setSiteDomain(request);
		setValidate(false);
		setReset(false);
		String id = getValidSectorSchemeId();
		if(!id.equals(DEFAULT_ID)){
			addRequestParameter("ampSecSchemeId", id);
			setSessionAttribute("ampAdmin", "ampAdmin");
			addRequestParameter("event", "edit");
			addRequestParameter("dest", "admin");
			addRequestParameter("sortByColumn", "sectorCode");
			actionPerform(UpdateSectorSchemes.class, form);
			verifyNoActionErrors();
			Collection <AmpSector> sectors1 = SectorUtil.getSectorLevel1SortBySectorCode(new Integer(id));
			Collection <AmpSector> sectors2 = form.getFormFirstLevelSectors();
			assertTrue("There are errors in list of sectors returned by UpdateSectorSchemes", isEqualCollection(sectors1, sectors2));
		}
		else {
            logger.error("Unable to get a valid AmpSectorScheme");
            fail("Unable to check Collections in Sector Manager Action");
		}
		
	}
	
	/**
	 * Test if the collection returned from database is sorted by Sector Official Code or Name 
	 * depending on the case
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void testCollectionSorted() throws Exception {
		String id = getValidSectorSchemeId();
		if(!id.equals(DEFAULT_ID)){
			Collection <AmpSector> sectors1 = SectorUtil.getSectorLevel1SortBySectorCode(new Integer(id));
			assertTrue("Collection No sorted by Sector Code", isSorted(sectors1, ORDERBY_SECTOR_CODE));
			Collection <AmpSector> sectors2 = SectorUtil.getSectorLevel1(new Integer(id));
			assertTrue("Collection No sorted by Description", isSorted(sectors2, ORDERBY_DESCRIPTION));			
		}
		else {
            logger.error("Unable to get an valid AmpSectorScheme");
            fail("Unable to check if collection is sorted");
		}

	}
	
	/**
	 * Gets the first id from a AmpSectorScheme
	 * @return id
	 */
	private String getValidSectorSchemeId() {
		Collection<AmpSectorScheme> schemes = SectorUtil.getSectorSchemes();
		String id=DEFAULT_ID;
		if(schemes.size()>0){
			id = schemes.iterator().next().getAmpSecSchemeId().toString();
		}
		return id;
	}
	/**
	 * Compares two collections of AmpSector
	 * @param sectors1
	 * @param sectors2
	 * @return true if both list have the same elements in the same order
	 */
	private boolean isEqualCollection(Collection <AmpSector>sectors1, Collection <AmpSector>sectors2) {
		Object[] s1= sectors1.toArray();
		Object[] s2= sectors2.toArray();
		if(s1.length!=s2.length)
			return false;
		for(int i=0;i<s1.length; i++){
			AmpSector amps1 = (AmpSector)s1[i];
			AmpSector amps2 = (AmpSector)s2[i];
			if(!amps1.getSectorCodeOfficial().equals(amps2.getSectorCodeOfficial()))
				return false;
		}
		return true;
	}
	/**
	 * Check if the collection is sorted by sector code, or name
	 * @param sectors
	 * @param orderby
	 * @return true if the collection is sorted
	 */
	private boolean isSorted(Collection <AmpSector>sectors, int orderby) {
		String[] codes = new String[sectors.size()];
		int i=0;
		for (AmpSector se : sectors){
			if(orderby==ORDERBY_SECTOR_CODE){
				codes[i++]=se.getSectorCodeOfficial();
			}
			else if(orderby==ORDERBY_DESCRIPTION){
				codes[i++]=se.getName();
			
			}
			else{
				fail("Wrong option selected to sort");
			}
		}
		for(int j=0; j<codes.length; j++){
			for(int t=j; t<codes.length; t++){
			  if(codes[j].compareTo(codes[t])>0){
				  return false;
			  }
			}
		}
		return true;
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
