package org.dgfoundation.amp.ecs.webservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.StringTokenizer;

import org.apache.axis.Constants;
import org.apache.axis.MessageContext;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ecs.common.ErrorScene;
import org.dgfoundation.amp.ecs.common.ErrorUser;

public class ECSImpl {
	private static Logger logger = Logger.getLogger(ECSImpl.class);

	private Connection getDB(){
		try{
			Class.forName("org.postgresql.Driver");
		}catch (ClassNotFoundException cnfe) {
			logger.info("Can't find postgres Driver", cnfe);
		    return null;
		}
		  
		Connection c = null;
		
		try{
			c = DriverManager.getConnection("jdbc:postgresql://localhost/amp_ecs",
		                                    "alexandru", "");
		}catch (SQLException s) {
			logger.info("Can't connect to database:", s);
			RuntimeException re = new RuntimeException("Can't connect to database:", s);
			throw re;
		}
		return c;
	}
	
	/**
	 * Gets server's associated id
	 * @param c
	 * @param serverName
	 * @return id; if serverName is not found it inserts it with default values;
	 */
	private int getServerId(Connection c, String serverName){
		int r = -1;
		Exception ex = null;
		try {
			Statement query = c.createStatement();
			ResultSet states = query.executeQuery("SELECT id FROM servers WHERE name LIKE '" + serverName + "'");
			while (states.next()) {
            	r = Integer.parseInt(states.getString("id"));
            	break;
            }
            // Close all of our JDBC resources.
            states.close();
            query.close();
		} catch (SQLException e) {
			logger.error(e);
			ex = e;
		}
		
		if (r == -1){
			try {
				Statement query = c.createStatement();
				query.executeUpdate("INSERT INTO servers(name) VALUES ('" + serverName + "')", Statement.RETURN_GENERATED_KEYS);
				ResultSet states = query.getGeneratedKeys();

				if (states.next()) {
			        r = states.getInt(1);
			    } else {
			    	logger.error("Error while getting generated keys!");
			    }
	            // Close all of our JDBC resources.
	            states.close();
	            query.close();
			} catch (SQLException e) {
				logger.error(e);
				ex = e;
			}
		}
		
		if (r == -1){ //didn't insert
			if (ex == null)
				ex = new Exception("didn't throw any exception");
			RuntimeException re = new RuntimeException("Didn't manage to insert in the db:", ex);
			throw re;
		}
		return r;
	}
	
	/**
	 * Get user's associated ID; inserts if not available
	 * @param c
	 * @param serverId
	 * @param login
	 * @param fullName
	 * @param password
	 * @return
	 */
	private int getUserId(Connection c, int serverId, String login, String fullName, String password){
		int r = -1;
		Exception ex = null;
		try {
			Statement query = c.createStatement();
			ResultSet states = query.executeQuery("SELECT id FROM users WHERE serverId="+ serverId +" AND login LIKE '" + login + "'");
			while (states.next()) {
            	r = Integer.parseInt(states.getString("id"));
            	break;
            }
            // Close all of our JDBC resources.
            states.close();
            query.close();
		} catch (SQLException e) {
			logger.error(e);
			ex = e;
		}
		
		if (r == -1){ //insert 
			try {
				Statement query = c.createStatement();
				query.executeUpdate("INSERT INTO users(serverId, login, fullName, password) VALUES ("+ serverId +", '"+ login +"', '"+ fullName +"','"+ password +"')", Statement.RETURN_GENERATED_KEYS);
				ResultSet states = query.getGeneratedKeys();

				if (states.next()) {
			        r = states.getInt(1);
			    } else {
			    	logger.error("Error while getting generated keys!");
			    }
	            // Close all of our JDBC resources.
	            states.close();
	            query.close();
			} catch (SQLException e) {
				logger.error(e);
				ex = e;
			}
		}
		
		if (r == -1){ //didn't insert
			if (ex == null)
				ex = new Exception("didn't throw any exception");
			RuntimeException re = new RuntimeException("Didn't manage to insert in the db:", ex);
			throw re;
		}
		return r;
	}

	/**
	 * Get error Id or insert it
	 * @param c
	 * @param stackTrace
	 * @return
	 */
	private int getErrorId(Connection c, String stackTrace){
		int r = -1;
		Exception ex = null;
		try {
			Statement query = c.createStatement();
			ResultSet states = query.executeQuery("SELECT id FROM errors WHERE stackTrace LIKE '"+ stackTrace +"'");
			while (states.next()) {
            	r = Integer.parseInt(states.getString("id"));
            	break;
            }
            // Close all of our JDBC resources.
            states.close();
            query.close();
		} catch (SQLException e) {
			logger.error(e);
			ex = e;
		}
		
		if (r == -1){ //insert 
			try {
				Statement query = c.createStatement();
				query.executeUpdate("INSERT INTO errors(stackTrace) VALUES ('"+ stackTrace +"')", Statement.RETURN_GENERATED_KEYS);
				ResultSet states = query.getGeneratedKeys();

				if (states.next()) {
			        r = states.getInt(1);
			    } else {
			    	logger.error("Error while getting generated keys!");
			    }
	            // Close all of our JDBC resources.
	            states.close();
	            query.close();
			} catch (SQLException e) {
				logger.error(e);
				ex = e;
			}
		}
		
		if (r == -1){ //didn't insert
			if (ex == null)
				ex = new Exception("didn't throw any exception");
			RuntimeException re = new RuntimeException("Didn't manage to insert in the db:", ex);
			throw re;
		}
		return r;
	}
	
	/**
	 * Update last occurence of an error
	 * @param c
	 * @param errorId
	 * @param date
	 * @return
	 */
	public void updateLastOccurence(Connection c, int errorId, Calendar date){
		Exception ex = null;
		try {
			Statement query = c.createStatement();
			query.executeUpdate("UPDATE errors SET lastOccurrence='"+ date.get(Calendar.YEAR) +"-"+ date.get(Calendar.MONTH) +"-"+ date.get(Calendar.DATE) +" "+ date.get(Calendar.HOUR_OF_DAY) +":"+ date.get(Calendar.MINUTE) +"' WHERE id="+errorId);
            // Close all of our JDBC resources.
            query.close();
		} catch (SQLException e) {
			logger.error(e);
			ex = e;
			RuntimeException re = new RuntimeException("Didn't manage to insert in the db:", ex);
			throw re;
		}
	}
	
	private Calendar parseDate(String date){
		Calendar ret = Calendar.getInstance();
		StringTokenizer st = new StringTokenizer(date, " :-");

		int year, month, day, hour, minute, second;
		year=month=day=hour=minute=second=0; //init
		
		if (st.hasMoreElements())
			year = Integer.valueOf(st.nextToken());
		if (st.hasMoreElements())
			month = Integer.valueOf(st.nextToken());
		if (st.hasMoreElements())
			day = Integer.valueOf(st.nextToken());
		if (st.hasMoreElements())
			hour = Integer.valueOf(st.nextToken());
		if (st.hasMoreElements())
			minute = Integer.valueOf(st.nextToken());
		if (st.hasMoreElements())
			second = Integer.valueOf(st.nextToken());

		ret.set(year, month, day, hour, minute, second);
		return ret;
	}

	private Calendar getLastOccurence(Connection c, int errorId){
		Calendar r = null;
		Exception ex = null;
		try {
			Statement query = c.createStatement();
			ResultSet states = query.executeQuery("SELECT lastOccurrence FROM errors WHERE id="+ errorId);
			while (states.next()) {
				String d = states.getString("lastOccurrence");
				if (d != null)
					r = parseDate(d);
            	break;
            }
            // Close all of our JDBC resources.
            states.close();
            query.close();
		} catch (SQLException e) {
			logger.error(e);
			ex = e;
		}
		
		return r;
	}

	/**
	 * Insert new scene
	 * @param c
	 * @param date
	 * @param browser
	 * @return id
	 */
	public int addScene(Connection c, Calendar date, String browser){
		int r = -1;
		Exception ex = null;
		try {
			Statement query = c.createStatement();
			query.executeUpdate("INSERT INTO scenes(date, browser) VALUES ('"+ date.get(Calendar.YEAR) +"-"+ date.get(Calendar.MONTH) +"-"+ date.get(Calendar.DATE) +" "+ date.get(Calendar.HOUR_OF_DAY) +":"+ date.get(Calendar.MINUTE) +"', '"+ browser +"')", Statement.RETURN_GENERATED_KEYS);
			ResultSet states = query.getGeneratedKeys();

			if (states.next()) {
		        r = states.getInt(1);
		    } else {
		    	logger.error("Error while getting generated keys!");
		    }
            // Close all of our JDBC resources.
            states.close();
            query.close();
		} catch (SQLException e) {
			logger.error(e);
			ex = e;
		}
		if (r == -1){ //didn't insert
			if (ex == null)
				ex = new Exception("didn't throw any exception");
			RuntimeException re = new RuntimeException("Didn't manage to insert in the db:", ex);
			throw re;
		}
		return r;
	}

	/**
	 * Add the error occurrence
	 * @param c
	 * @param serverId
	 * @param errorId
	 * @param userId
	 * @param sceneId
	 * @return
	 */
	public int addOccurrence(Connection c, int serverId, int errorId, int userId, int sceneId){
		int r = -1;
		Exception ex = null;
		try {
			Statement query = c.createStatement();
			query.executeUpdate("INSERT INTO occurrences(serverId, errorId, userId, sceneId) VALUES ("+ serverId +", "+ errorId +", "+ userId +", "+ sceneId +")", Statement.RETURN_GENERATED_KEYS);
			ResultSet states = query.getGeneratedKeys();

			if (states.next()) {
		        r = states.getInt(1);
		    } else {
		    	logger.error("Error while getting generated keys!");
		    }
            // Close all of our JDBC resources.
            states.close();
            query.close();
		} catch (SQLException e) {
			logger.error(e);
			ex = e;
		}
		if (r == -1){ //didn't insert
			if (ex == null)
				ex = new Exception("didn't throw any exception");
			RuntimeException re = new RuntimeException("Didn't manage to insert in the db:", ex);
			throw re;
		}
		return r;
	}

	/**
	 * Remote method invoked by clients to get custom parameters or to report some others
	 * @param serverName
	 * @param report
	 * @return
	 */
	public String[] getParameters(String serverName, String report) {
		//logger.info("getParameters("+serverName+", "+report+")\n");
		MessageContext curContext = MessageContext.getCurrentContext();

		String remoteIp = "Unknown";
		if(curContext != null) {
			Object ipProperty = curContext.getProperty(Constants.MC_REMOTE_ADDR);
			remoteIp = ipProperty.toString();
		}
		
		String[] params = new String[4];
		Connection c = getDB();
		try{
			int serverId = getServerId(c, serverName);
			
			int updateDelay = -1;
			try {
				Statement query = c.createStatement();
				ResultSet states = query.executeQuery("SELECT updatedelay FROM servers WHERE id=" + serverId);
				while (states.next()) {
					updateDelay = Integer.parseInt(states.getString("updateDelay"));
					break;
				}
				// Close all of our JDBC resources.
				states.close();
				query.close();
			} catch (SQLException e) {
				logger.error(e);
			}
			
			if (updateDelay == -1){
				updateDelay = 300000;
			}
			if (ECSConstants.SERVER_STARTING_UP.compareTo(report) == 0){
				logger.info("Server " + serverName + " from IP " + remoteIp + " is starting up!");
				
				params[0] = serverName;
				params[1] = String.valueOf(updateDelay);
			}
		} catch (RuntimeException e) {
			throw e;
		} finally {
			try {
				c.close();
			} catch (SQLException ignore) {}
		}
		
		return params;
	}

	public boolean sendError(String serverName, int count, String stackTrace, ErrorUser[] users, ErrorScene[][] userScenes) {
		logger.info("sendError("+serverName+", count:"+count+", "+ stackTrace.substring(0, 20) +"\n)\n");
		MessageContext curContext = MessageContext.getCurrentContext();

		String remoteIp = "Unknown";
		if(curContext != null) {
			Object ipProperty = curContext.getProperty(Constants.MC_REMOTE_ADDR);
			remoteIp = ipProperty.toString();
		}

		logger.info("Caller IP:" + remoteIp);
		Connection c = getDB();
		try{
			int serverId = getServerId(c, serverName);
			int errorId = getErrorId(c, stackTrace);
			logger.info("ServerId=" + serverId+" ErrorId=" + errorId);
			Calendar maxDate = null;
			
			for (int i = 0; i < users.length; i++){
				ErrorUser errorUser = users[i];
				
				int userId = getUserId(c, serverId, errorUser.getLogin(), errorUser.getFullName(), errorUser.getPassword());

				ErrorScene[] scenesList = (ErrorScene[]) userScenes[i];
				if (scenesList == null){
					continue;
				}
				
				for (int j = 0; j < scenesList.length; j++){
					ErrorScene errorScene = (ErrorScene) scenesList[j];
					int sceneId = addScene(c, errorScene.getDate(), errorScene.getBrowser());
					addOccurrence(c, serverId, errorId, userId, sceneId);
					if (maxDate == null){
						maxDate = errorScene.getDate();
					}
					else{
						if (maxDate.before(errorScene.getDate()))
							maxDate = errorScene.getDate();
					}
				}
			}
			logger.info("Updating last occurrence");
			Calendar currentLastOccurence = getLastOccurence(c, errorId);
			if (maxDate != null){
				if (currentLastOccurence == null || currentLastOccurence.before(maxDate))
					updateLastOccurence(c, errorId, maxDate);
			}
			logger.info("DONE !!!!");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Exception :", e);
			RuntimeException r = new RuntimeException(e);
			throw r;
		} finally {
			try {
				c.close();
			} catch (SQLException ignore) {}
		}
	
		return true;
	}

	public boolean sendErrorList(String serverName, Object[] errList) {
		logger.info("sendErrorList("+serverName+", list count:"+errList.length +")\n");
		return false;
	}
	
    public boolean test(String serverName, ErrorUser[] users, ErrorScene[][] scenes) throws java.rmi.RemoteException {
    	logger.info("From: "+serverName+" users:" + users.length + " scenes:" + scenes.length);
    	for (int i = 0; i< scenes.length; i++){
    		ErrorScene[] es = scenes[i];
    		logger.info("ss:" + es.length);
    	}
    	return true;
    }

	

}
