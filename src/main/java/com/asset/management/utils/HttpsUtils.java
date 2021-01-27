package com.asset.management.utils;

import org.apache.maven.surefire.shade.org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class HttpsUtils {

    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String METHOD_POST = "POST";
    public static final String APPLICATION_JSON = "application/json";
    public static final String X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final int DEFAULT_CONNECT_TIMEOUT  = 5000;
    private static final int DEFAULT_READ_TIMEOUT  = 15000;
    private static String contentType = APPLICATION_JSON;

    private HttpsUtils() {
    }

    public static String doPost(String url, String json) throws IOException{
        return doPost(url,json,APPLICATION_JSON,DEFAULT_CONNECT_TIMEOUT,DEFAULT_READ_TIMEOUT);
    }

    public static String doPostByFormUrlencoded(String url,String str) throws IOException{
        return doPost(url,str,X_WWW_FORM_URLENCODED,DEFAULT_CONNECT_TIMEOUT,DEFAULT_READ_TIMEOUT);
    }

    private static String doPost(String url, String json,String contentType,int connectTimeout, int readTimeout) throws IOException {
        HttpsUtils.contentType = contentType;
        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp = null;
        try {
            byte[] content = json.getBytes(DEFAULT_CHARSET);
            conn = getConnection(new URL(url), METHOD_POST);
            conn = setHeader(conn);
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);
            out = conn.getOutputStream();
            out.write(content);
            rsp = getResponseAsString(conn);
        } finally {
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rsp;
    }

    private static HttpURLConnection setHeader(HttpURLConnection conn){
        conn.setRequestProperty("Content-Type",contentType);
        return conn;
    }

    private static HttpURLConnection getConnection(URL url, String method) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        return conn;
    }

    protected static String getResponseAsString(HttpURLConnection conn) throws IOException {
        String charset = getResponseCharset(conn.getContentType());
        InputStream es = conn.getErrorStream();
        if (es == null) {
            return getStreamAsString(conn.getInputStream(), charset);
        } else {
            String msg = getStreamAsString(es, charset);
            if (StringUtils.isEmpty(msg)) {
                throw new IOException(conn.getResponseCode() + ":" + conn.getResponseMessage());
            } else {
                throw new IOException(msg);
            }
        }
    }

    private static String getStreamAsString(InputStream stream, String charset) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
            StringWriter writer = new StringWriter();
            char[] chars = new char[256];
            boolean var5 = false;

            int count;
            while ((count = reader.read(chars)) > 0) {
                writer.write(chars, 0, count);
            }

            String var6 = writer.toString();
            return var6;
        } finally {
            if (stream != null) {
                stream.close();
            }

        }
    }

    private static String getResponseCharset(String charsetType) {
        String charset = DEFAULT_CHARSET;
        if (!StringUtils.isEmpty(charsetType)) {
            String[] params = charsetType.split(";");
            String[] var3 = params;
            int var4 = params.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String param = var3[var5];
                param = param.trim();
                if (param.startsWith("charset")) {
                    String[] pair = param.split("=", 2);
                    if (pair.length == 2 && !StringUtils.isEmpty(pair[1])) {
                        charset = pair[1].trim();
                    }
                    break;
                }
            }
        }
        return charset;
    }

}

