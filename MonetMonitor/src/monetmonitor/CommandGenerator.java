package monetmonitor;

public class CommandGenerator {

	/**
	 * Generates the command sequence for: stop, destroy, create, release
	 * @return
	 */
	public static String[] generateRecreateDatabaseCommands() {
		return new String[] {
				Constants.getCommandStop() + Constants.getDbName(),
				Constants.getCommandDestroy() + Constants.getDbName(),
				Constants.getCommandCreate() + Constants.getDbName(),
				Constants.getCommandRelease() + Constants.getDbName()};
	}
	public static String[] generateCreateDatabaseCommands() {
		return new String[] {
				Constants.getCommandCreate() + Constants.getDbName(),
				Constants.getCommandRelease() + Constants.getDbName()};
	}	
	public static String[] generateReleaseDatabaseCommands() {
		return new String[] { Constants.getCommandRelease() + Constants.getDbName()};
	}
}
