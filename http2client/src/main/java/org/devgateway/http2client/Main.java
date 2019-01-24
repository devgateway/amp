package org.devgateway.http2client;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.CertificatePinner;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @author Octavian Ciubotaru
 */
public class Main implements Callback<ObjectNode> {

    private String cookie;

    private CountDownLatch latch;
    private OkHttpClient client;
    private long startTime;

    public static void main(String[] args) {
        new Main().run();
    }

    static class NullHostNameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public void run() {

        String hostname = "localhost";
        CertificatePinner certificatePinner = new CertificatePinner.Builder()
                .add(hostname, "sha256/duw3XBbv+qsFdOFA/x2PFxSqvltwOiKEaVnjWJ//NmE=")
                .build();

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequestsPerHost(4);

        client = new OkHttpClient.Builder()
//                .protocols(Arrays.asList(HTTP_1_1)) // uncomment to limit oneself to http/1.1
                .dispatcher(dispatcher)
                .certificatePinner(certificatePinner)
                .hostnameVerifier(new NullHostNameVerifier())
                .build();

        startTime = System.currentTimeMillis();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://localhost:8443/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        Amp amp = retrofit.create(Amp.class);

        cookie = "JSESSIONID=02C8E8267D2A75527BDA3A13C8216514";

        List<String> ampIds = Activities.AMP_IDS;//.subList(0, 1);
        latch = new CountDownLatch(ampIds.size());

        ampIds.forEach(ampId -> amp.getActivity(ampId, cookie).enqueue(this));

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startTime = System.currentTimeMillis() - startTime;
        System.out.println("conn count: " + client.connectionPool().connectionCount());
        System.out.println("idle conn count: " + client.connectionPool().idleConnectionCount());
        System.out.println("acts: " + ampIds.size() + " time: " + startTime);
    }

    // http/1.1 plain    loopback 11824
    // http/1.1 with TLS loopback 12647 (with pool) 41933 (no pool)  3.3x
    // http/1.1 with TLS sulfur 231686 (with pool) 801672 (no pool)  3.4x
    // http/2   with TLS loopback 12661
    // http/2   with TLS sulfur 199655 (5 threads)

    public void onResponse(Call<ObjectNode> call, Response<ObjectNode> response) {
        if (response.isSuccessful()) {
            System.out.println("> " + response.body().get("project_title").asText());
        } else {
            System.err.println(response);
        }
        latch.countDown();

//        client.connectionPool().evictAll(); // uncomment to clear conn pool as if it didn't exist

        long duration = System.currentTimeMillis() - startTime;
        long rem = latch.getCount();
        if (rem < 2010) {
            System.out.println("Estimation: " + (duration * 2010.0) / (2010.0 - rem));
        }
    }

    public void onFailure(Call<ObjectNode> call, Throwable t) {
        t.printStackTrace();
        latch.countDown();
    }
}
