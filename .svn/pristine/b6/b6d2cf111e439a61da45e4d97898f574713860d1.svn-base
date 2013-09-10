package com.roiland.crm.sm.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

/**
 * @author Chunji Li
 *
 */
public class NetworkUtils {
    private static final String tag = "NetworkUtils";
    private final static String DEFAULT_ENCODE = HTTP.UTF_8;

    private NetworkUtils() {
    }

    public static boolean access(String domain, int port) {
        boolean result = false;
        Socket socket = null;
        try {
            socket = new Socket();
            SocketAddress remoteAddr = new InetSocketAddress(domain, port);
            socket.connect(remoteAddr, 10 * 1000);
            result = socket.isConnected();
            if (Log.DEBUG.get()) {
                Log.d(tag, domain + ":" + port + " >>> " + result);
            }
        } catch (Exception e) {
            Log.w(tag, e.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    Log.w(tag, e.getMessage(), e);
                }
            }
        }

        return result;
    }

    public static int getStatusCode(HttpResponse response) {
        return response.getStatusLine().getStatusCode();
    }

    public static String getSimpleString(HttpResponse response) {
        String result = null;
        BufferedReader reader = null;
        try {
            HttpEntity entity = response.getEntity();
            String encoding = DEFAULT_ENCODE;
            Header encodingHeader = entity.getContentEncoding();
            if (encodingHeader != null && !TextUtils.isEmpty(encodingHeader.getValue())) {
                encoding = encodingHeader.getValue();
            }

            InputStream input = entity.getContent();
            reader = new BufferedReader(new InputStreamReader(input, encoding));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
            result = builder.toString();
        } catch (IllegalStateException e) {
            Log.e(tag, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(tag, e.getMessage(), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(tag, e.getMessage(), e);
                }
            }
        }

        return result;
    }

    /* modified by BonSa 20130403 */
    public static boolean isNetworkConnected(Context context) {
		ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		boolean isWifi = (networkInfo != null) ? networkInfo.isConnected() : false;
		networkInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean isMobile = (networkInfo != null) ? networkInfo.isConnected() : false;
		networkInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
		boolean isEthernet = (networkInfo != null) ? networkInfo.isConnected() : false;
		return (isWifi || isMobile || isEthernet);
	}
    /* end modified */
}
