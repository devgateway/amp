package org.dgfoundation.ecs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import org.dgfoundation.ecs.core.ECS;
import org.dgfoundation.ecs.keeper.ErrorKeeperItem;
import org.dgfoundation.ecs.keeper.ErrorScene;
import org.dgfoundation.ecs.keeper.ErrorUser;

public class MainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	try{
		// TODO Auto-generated method stub
		//Create socket
		String hostname = "localhost";
		int port = 80;

		ErrorKeeperItem eki = new ErrorKeeperItem();
		eki.getUsers().add(new ErrorUser("a1", "a2", "a3"));
		eki.getUsers().add(new ErrorUser("b1", "b2", "b3"));

		ErrorScene[][] es = new ErrorScene[2][];
		es[0] = new ErrorScene[2]; es[0][0] = new ErrorScene("sdfasf", Calendar.getInstance(),"session23"); es[0][1] = new ErrorScene("aaaaaf", Calendar.getInstance(), "session2");
		es[1] = new ErrorScene[3]; es[1][0] = new ErrorScene("sdfasf", Calendar.getInstance(),"session222133"); es[1][1] = new ErrorScene("sdfasf", Calendar.getInstance(), "session1"); es[1][2] = new ErrorScene("sdfasf", Calendar.getInstance(), "session23231");
		
		eki.getUserScenes().add(es[0]);
		eki.getUserScenes().add(es[1]);

		eki.setCount(2);
		eki.setStackTrace("dslkajdas dlkas dask dasldm asdkla dakld aslkd asd laskd aslkd asd aslkd asdlas daskld asd");
		
		String serverName = "server2";
		// Send data
		String data="{" +
				ECS.t("ServerName", serverName) +
				eki.toJson("eki") +
				"}";
		URL url = new URL("http://localhost:80/ecs.php");
        URLConnection conn = url.openConnection();
        conn.setUseCaches(false);
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();
    
        // Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
        	System.out.println(line);
        }
	} catch (Exception e) {
		e.printStackTrace();
	}
}

}
