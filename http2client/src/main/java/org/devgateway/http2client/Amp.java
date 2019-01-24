package org.devgateway.http2client;

import com.fasterxml.jackson.databind.node.ObjectNode;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * @author Octavian Ciubotaru
 */
public interface Amp {

    @GET("/rest/activity/project")
    Call<ObjectNode> getActivity(@Query("amp-id") String ampId, @Header("Cookie") String cookie);
}
