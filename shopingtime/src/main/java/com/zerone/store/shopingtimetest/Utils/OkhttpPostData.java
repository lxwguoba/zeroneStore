//package com.zerone.store.shopingtimetest.Utils;
//
//import android.os.Build;
//import android.util.Log;
//
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.net.URLEncoder;
//import java.util.Map;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
///**
// * Created by on 2018/6/1 0001 15 12.
// * Author  LiuXingWen
// */
//
//public class OkhttpPostData {
//    /**
//     * 2.2 okHttp post异步请求
//     *
//     * @param actionUrl 接口地址
//     * @param paramsMap 请求参数
//     * @param callBack  请求返回数据回调
//     * @return
//     */
//    public void postByAsyn(String actionUrl, Map<String, Object> paramsMap, final OkHttpCallback callBack) {
//        try {
//            StringBuilder tempParams = new StringBuilder();
//            int pos = 0;
//            for (String key : paramsMap.keySet()) {
//                if (pos > 0) {
//                    tempParams.append("&");
//                }
//                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key).toString(), "utf-8")));
//                pos++;
//            }
//            String params = tempParams.toString();
//            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, params);
//            String requestUrl = String.format("%s%s", Config.SERVER_ADDRESS, actionUrl);
//            final Request request = addHeaders().url(requestUrl).post(body).build();
//            final Call call = mOkHttpClient.newCall(request);
//            call.enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    onFailedCallBack(e, callBack);
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    if (response.isSuccessful()) {
//                        try {
//                            String responseStr = response.body().string();
//                            Log.i(TAG, "onResponse responseStr=" + responseStr);
//                            JSONObject oriData = new JSONObject(responseStr.trim());
//                            ServerResponse serverResponse = ServerResponse.parseFromResponse(oriData);
//
//                            onSuccessCallBack(oriData, serverResponse, callBack);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            });
//        } catch (Exception e) {
//            Log.e(TAG, e.toString());
//        }
//    }
//
//    private Request.Builder addHeaders() {
//        Request.Builder builder = new Request.Builder()
//                //addHeader，可添加多个请求头  header，唯一，会覆盖
//                .addHeader("Connection", "keep-alive")
//                .addHeader("platform", "2")
//                .addHeader("phoneModel", Build.MODEL)
//                .addHeader("systemVersion", Build.VERSION.RELEASE)
//                .addHeader("appVersion", "3.2.0")
//                .header("sid", "eyJhZGRDaGFubmVsIjoiYXBwIiwiYWRkUHJvZHVjdCI6InFia3BsdXMiLCJhZGRUaW1lIjoxNTAzOTk1NDQxOTEzLCJyb2xlIjoiUk9MRV9VU0VSIiwidXBkYXRlVGltZSI6MTUwMzk5NTQ0MTkxMywidXNlcklkIjoxNjQxMTQ3fQ==.b0e5fd6266ab475919ee810a82028c0ddce3f5a0e1faf5b5e423fb2aaf05ffbf");
//        return builder;
//    }
//}
