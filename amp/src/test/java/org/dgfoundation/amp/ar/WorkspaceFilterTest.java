package org.dgfoundation.amp.ar;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.List;

import org.dgfoundation.amp.StandaloneAMPInitializer;
import org.dgfoundation.amp.test.categories.DatabaseTests;
import org.dgfoundation.amp.testutils.InTransactionRule;
import org.digijava.kernel.job.cachedtables.PublicViewColumnsUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Activity name format: ~wsf~ {$workspace_nr} {description}
 *
 * <p>Workspace structure:</p>
 *
 * <b>Management workspace 1</b>
 * <ul>
 * <li>Normal workspace 1
 * <li>Normal workspace 2
 * </ul>
 *
 * <b>Management workspace 2</b>
 * <ul>
 * <li>Normal workspace 3
 * </ul>
 *
 * <b>Management workspace 3</b>
 * <ul>
 * <li>Management workspace 4
 * <li>Management workspace 5
 * </ul>
 *
 * <b>Management workspace 4</b>
 * <ul>
 * <li>Computed ws 5 with DN Norway
 * </ul>
 *
 * <b>Management workspace 5</b>
 * <ul>
 * <li>Computed ws 7 with org Norway
 * </ul>
 *
 * <b>Normal workspace 1</b>
 * <ul>
 * <li>~wsf~ 1 Approved with DN Norway
 * <li>~wsf~ 1 Draft with DN Norway
 * <li>~wsf~ 1 Approved with BN Norway
 * <li>~wsf~ 1 Draft with BN norway
 * </ul>
 *
 * <b>Normal workspace 2</b>
 * <ul>
 * <li>~wsf~ 2 Approved
 * <li>~wsf~ 2 Draft
 * </ul>
 *
 * <b>Normal workspace 3</b>
 * <ul>
 * <li>~wsf~ 3 Approved
 * <li>~wsf~ 3 Edited
 * <li>~wsf~ 3 Started approved
 * <li>~wsf~ 3 Started
 * <li>~wsf~ 3 Rejected
 * </ul>
 *
 * <b>Private workspace 4</b>
 * <ul>
 * <li>~wsf~ 4 Submitted with DN Norway
 * <li>~wsf~ 4 Draft with DN Norway
 * <li>~wsf~ 4 Submitted with BN norway
 * <li>~wsf~ 4 Draft with BN norway
 * </ul>
 *
 * <b>Computed ws 5 with DN Norway</b>
 * <ul>
 * <li>~wsf~ 5 Draft
 * <li>~wsf~ 5 Approved
 * </ul>
 *
 * <b>Computed ws 6 with DN Norway, no draft</b>
 * <ul>
 * <li>~wsf~ 6 Draft
 * </ul>
 *
 * <b>Computed ws 7 with org Norway</b>
 * <ul>
 * <li>~wsf~ 7 Draft
 * <li>~wsf~ 7 Approved
 * </ul>
 *
 * <b>Computed ws 8 with org Norway, no draft</b>
 * <ul>
 * <li>~wsf~ 8 Draft
 * </ul>
 *
 * <b>Activities without a workspace</b>
 * <ul>
 * <li>~wsf~ Approved without workspace
 * </ul>
 *
 * @author Octavian Ciubotaru
 */
@Category(DatabaseTests.class)
public class WorkspaceFilterTest {

    @Rule
    public InTransactionRule inTransactionRule = new InTransactionRule();

    /**
     * Redoing public view caches to be able to properly test the anonymous case. See {@link #testAnonymous()}.
     */
    @BeforeClass
    public static void setUp() {
        StandaloneAMPInitializer.initialize();
        PublicViewColumnsUtil.redoCaches();
    }

    @Test
    public void testNormalWorkspace1() {
        TeamMember tm = teamMemberFor("Normal workspace 1");
        List<String> acts = findActivities(tm);
        assertThat(acts, containsInAnyOrder(
                "~wsf~ 1 Approved with DN Norway",
                "~wsf~ 1 Draft with DN Norway",
                "~wsf~ 1 Approved with BN Norway",
                "~wsf~ 1 Draft with BN norway"));
    }

    @Test
    public void testNormalWorkspace2() {
        TeamMember tm = teamMemberFor("Normal workspace 2");
        List<String> acts = findActivities(tm);
        assertThat(acts, containsInAnyOrder(
                "~wsf~ 2 Approved",
                "~wsf~ 2 Draft"));
    }

    @Test
    public void testNormalWorkspace3() {
        TeamMember tm = teamMemberFor("Normal workspace 3");
        List<String> acts = findActivities(tm);
        assertThat(acts, containsInAnyOrder(
                "~wsf~ 3 Approved",
                "~wsf~ 3 Edited",
                "~wsf~ 3 Started approved",
                "~wsf~ 3 Started",
                "~wsf~ 3 Rejected"));
    }

    @Test
    public void testPrivateWorkspace() {
        TeamMember tm = teamMemberFor("Private workspace 4");
        List<String> acts = findActivities(tm);
        assertThat(acts, containsInAnyOrder(
                "~wsf~ 4 Submitted with DN Norway",
                "~wsf~ 4 Draft with DN Norway",
                "~wsf~ 4 Submitted with BN norway",
                "~wsf~ 4 Draft with BN norway"));
    }

    @Test
    public void testComputedWs5NorwayAsDonor() {
        TeamMember tm = teamMemberFor("Computed ws 5 with DN Norway");
        List<String> acts = excludeUnknownActivities(findActivities(tm));
        assertThat(acts, containsInAnyOrder(
                "~wsf~ 5 Draft",
                "~wsf~ 5 Approved",
                "~wsf~ 1 Approved with DN Norway",
                "~wsf~ 1 Draft with DN Norway"));
    }

    @Test
    public void testComputedWs6NorwayAsDonorNoDraft() {
        TeamMember tm = teamMemberFor("Computed ws 6 with DN Norway, no draft");
        List<String> acts = excludeUnknownActivities(findActivities(tm));
        assertThat(acts, containsInAnyOrder(
                "~wsf~ 6 Draft",
                "~wsf~ 1 Approved with DN Norway"));
    }

    @Test
    public void testComputedWs7NorwayAsOrg() {
        TeamMember tm = teamMemberFor("Computed ws 7 with org Norway");
        List<String> acts = excludeUnknownActivities(findActivities(tm));
        assertThat(acts, containsInAnyOrder(
                "~wsf~ 7 Draft",
                "~wsf~ 7 Approved",
                "~wsf~ 1 Approved with DN Norway",
                "~wsf~ 1 Draft with DN Norway",
                "~wsf~ 1 Approved with BN Norway",
                "~wsf~ 1 Draft with BN norway"));
    }

    @Test
    public void testComputedWs8NorwayAsOrgNoDraft() {
        TeamMember tm = teamMemberFor("Computed ws 8 with org Norway, no draft");
        List<String> acts = excludeUnknownActivities(findActivities(tm));
        assertThat(acts, containsInAnyOrder(
                "~wsf~ 8 Draft",
                "~wsf~ 1 Approved with DN Norway",
                "~wsf~ 1 Approved with BN Norway"));
    }

    @Test
    public void testManagementWs1() {
        TeamMember tm = teamMemberFor("Management workspace 1");
        List<String> acts = findActivities(tm);
        assertThat(acts, containsInAnyOrder(
                "~wsf~ 1 Approved with DN Norway",
                "~wsf~ 1 Approved with BN Norway",
                "~wsf~ 2 Approved"));
    }

    @Test
    public void testManagementWs2() {
        TeamMember tm = teamMemberFor("Management workspace 2");
        List<String> acts = findActivities(tm);
        assertThat(acts, containsInAnyOrder(
                "~wsf~ 3 Approved",
                "~wsf~ 3 Started approved"));
    }

    @Test
    public void testManagementWs3() {
        TeamMember tm = teamMemberFor("Management workspace 3");
        List<String> acts = findActivities(tm);
        assertThat(acts, containsInAnyOrder(
                "~wsf~ 5 Approved",
                "~wsf~ 7 Approved"));
    }

    @Test
    public void testManagementWs4() {
        TeamMember tm = teamMemberFor("Management workspace 4");
        List<String> acts = findActivities(tm);
        assertThat(acts, containsInAnyOrder(
                "~wsf~ 5 Approved"));
    }

    @Test
    public void testManagementWs5() {
        TeamMember tm = teamMemberFor("Management workspace 5");
        List<String> acts = findActivities(tm);
        assertThat(acts, containsInAnyOrder(
                "~wsf~ 7 Approved"));
    }

    @Test
    public void testAnonymous() {
        // maintain
        List<String> acts = findActivities(null);
        assertThat(acts, containsInAnyOrder(
                "~wsf~ 1 Approved with DN Norway",
                "~wsf~ 1 Approved with BN Norway",
                "~wsf~ 2 Approved",
                "~wsf~ 3 Approved",
                "~wsf~ 3 Started approved",
                "~wsf~ 5 Approved",
                "~wsf~ 7 Approved"));
    }

    /**
     * Find activity names visible by specified team member. If team member is null then will return publicly
     * visible activities.
     */
    private List<String> findActivities(TeamMember teamMember) {
        return activityNamesFor(ActivityUtil.fetchLongs(WorkspaceFilter.generateWorkspaceFilterQuery(teamMember)));
    }

    /**
     * Returns activity names from activity ids.
     */
    @SuppressWarnings("unchecked")
    private List<String> activityNamesFor(Collection<Long> activityIds) {
        if (activityIds.isEmpty()) {
            return emptyList();
        }
        return PersistenceManager.getSession().createCriteria(AmpActivityVersion.class)
                .add(Restrictions.in("ampActivityId", activityIds))
                .setProjection(Projections.property("name"))
                .list();
    }

    /**
     * Returns team membership of <em>atl@amp.org</em> user in the requested workspace.
     */
    private TeamMember teamMemberFor(String workspaceName) {
        AmpTeamMember ampTeamMember = (AmpTeamMember) PersistenceManager.getSession()
                .createCriteria(AmpTeamMember.class)
                .createAlias("user", "u")
                .createAlias("ampTeam", "t")
                .add(Restrictions.eq("u.email", "atl@amp.org"))
                .add(Restrictions.eq("t.name", workspaceName))
                .uniqueResult();
        return ampTeamMember.toTeamMember();
    }

    /**
     * Computed workspaces include activities from all workspaces. Here we exclude activities from workspaces unknown
     * to this test class by using a convention that all activity names tested here must start with ~wsf~.
     */
    private List<String> excludeUnknownActivities(Collection<String> activityNames) {
        return activityNames.stream().filter(name -> name.startsWith("~wsf~")).collect(toList());
    }
}
