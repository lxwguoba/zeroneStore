package com.zerone_catering.avtivity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.zerone_catering.Base64AndMD5.CreateToken;
import com.zerone_catering.Contants.IpConfig;
import com.zerone_catering.R;
import com.zerone_catering.avtivity.BaseSet.BaseActvity;
import com.zerone_catering.avtivity.cashierpage.Have_Order_Check_TableActivity;
import com.zerone_catering.avtivity.manageorderpage.orderlist.TheOrderListActivity;
import com.zerone_catering.avtivity.manageorderpage.printlist.Print_Order_Check_TableActivity;
import com.zerone_catering.avtivity.openorderpage.CheckTableActivity;
import com.zerone_catering.avtivity.system.SystemSettingsActivity;
import com.zerone_catering.control.InitConfig;
import com.zerone_catering.control.MySyntherizer;
import com.zerone_catering.control.NonBlockSyntherizer;
import com.zerone_catering.domain.UserInfo;
import com.zerone_catering.listener.UiMessageListener;
import com.zerone_catering.service.PushNotificationService;
import com.zerone_catering.util.AutoCheck;
import com.zerone_catering.util.OfflineResource;
import com.zerone_catering.utils.AppSharePreferenceMgr;
import com.zerone_catering.utils.GetUserInfo;
import com.zerone_catering.utils.NetUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActvity {

    //声明一个操作常量字符串
    public static final String ACTION_UPDATEUI = "action.updateUI";
    // ================选择TtsMode.ONLINE  不需要设置以下参数; 选择TtsMode.MIX 需要设置下面2个离线资源文件的路径
    private static final String TEMP_DIR = "/sdcard/baiduTTS"; // 重要！请手动将assets目录下的3个dat 文件复制到该目录
    // 请确保该PATH下有这个文件
    private static final String TEXT_FILENAME = TEMP_DIR + "/" + "bd_etts_text.dat";
    // 请确保该PATH下有这个文件 ，m15是离线男声
    private static final String MODEL_FILENAME =
            TEMP_DIR + "/" + "bd_etts_common_speech_m15_mand_eng_high_am-mix_v3.0.0_20170505.dat";
    //声明一个广播实例
    public UpdateUIBroadcastReceiver broadcastReceiver;
    //==============================语音============================
    // 主控制类，所有合成控制方法从这个类开始
    protected MySyntherizer synthesizer;
    protected String offlineVoice = OfflineResource.VOICE_MALE;
    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
//    protected TtsMode ttsMode = TtsMode.MIX;
    protected String appId = "11527074";
    protected String appKey = "1pSVKz6AiMrGaToeYD8dOSLR";
    protected String secretKey = "NmbR0VPxyhgiELeak4vPK4QUMCZ6KSkb";
    protected SpeechSynthesizer mSpeechSynthesizer;
    @Bind(R.id.btn_main_open_order)
    LinearLayout btnMainOpenOrder;
    @Bind(R.id.btn_main_collect_money)
    LinearLayout btnMainCollectMoney;
    @Bind(R.id.btn_main_set_system)
    LinearLayout btnMainSetSystem;
    @Bind(R.id.btn_main_order_manage)
    LinearLayout btnMainOrderManage;
    @Bind(R.id.btn_main_out_app)
    LinearLayout btnMainOutApp;
    @Bind(R.id.main_order_count)
    TextView mainOrderCount;
    Handler mainHandler = new Handler() {
        /*
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };
    private Dialog cashier_dialog;
    private UserInfo userInfo;
    private int ocount = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        String countJson = (String) msg.obj;
                        Log.i("URL", "countJosn=" + countJson);
                        JSONObject jsonObject = new JSONObject(countJson);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            int anInt = jsonObject.getJSONObject("data").getInt("num");
                            if (anInt > 0) {
                                mainOrderCount.setVisibility(View.VISIBLE);
                                mainOrderCount.setText("" + anInt);
                            } else {
                                mainOrderCount.setVisibility(View.GONE);
                                mainOrderCount.setText("");
                            }
                            boolean lean = (boolean) AppSharePreferenceMgr.get(MainActivity.this, "speak", true);
                            //语音播报
                            if (anInt == 0) {
                            } else if (anInt == ocount) {
                                //没有新的订单
                            } else {
                                ocount = anInt;
                                if (lean) {
                                    speak(ocount + "");
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
    private TtsMode ttsMode = TtsMode.ONLINE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        userInfo = GetUserInfo.initGetUserInfo(this);
        startService();
//        initialTts();
        initTTs();
    }

    private void startService() {
        //启动服务
        Intent i = new Intent(this, PushNotificationService.class);
        //下面写自己的路径
        i.setAction("com.zerone_catering.service.PushNotificationService");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startService(i);
        // 动态注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_UPDATEUI);
        broadcastReceiver = new UpdateUIBroadcastReceiver();
        registerReceiver(broadcastReceiver, filter);
    }

    /**
     * 自定义对话框
     * 这个是点单管理页面
     */
    private void orderDialog() {
        cashier_dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_cashier_view, null);
        Button pbtn = view.findViewById(R.id.btn_pop_order_print_list);
        Button pbtnlist = view.findViewById(R.id.btn_pop_order_list);
        Button pqx = view.findViewById(R.id.quxiao);
        cashier_dialog.setContentView(view);
        pbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Print_Order_Check_TableActivity.class);
                startActivity(intent);
                cashier_dialog.dismiss();
            }
        });
        pbtnlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TheOrderListActivity.class);
                startActivity(intent);
                cashier_dialog.dismiss();
            }
        });
        pqx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashier_dialog.dismiss();
            }
        });


        cashier_dialog.show();
    }

    /**
     * 自定义对话框
     */
    private void out_Activity_Dialog() {
        final Dialog dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.activity_dialog_out_view, null);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);
        dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(true);
        //设置对话框的大小
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.removeALLActivity();
                System.exit(0);
            }
        });
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.btn_main_open_order, R.id.btn_main_collect_money, R.id.btn_main_set_system, R.id.btn_main_order_manage, R.id.btn_main_out_app})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_open_order:
                //开单
                Intent intent = new Intent(MainActivity.this, CheckTableActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_main_collect_money:
                //收银
                Intent intent01 = new Intent(MainActivity.this, Have_Order_Check_TableActivity.class);
                startActivity(intent01);
                break;
            case R.id.btn_main_set_system:
                //系统设置
                Intent intent02 = new Intent(MainActivity.this, SystemSettingsActivity.class);
                startActivity(intent02);
                break;
            case R.id.btn_main_order_manage:
                //修改为订单管理
                orderDialog();
                break;
            case R.id.btn_main_out_app:
                //注销app
                out_Activity_Dialog();
                break;
        }
    }

    /**
     * 点击返回键后跳出退出对话框
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //如果点击的是后退键  首先判断webView是否能够后退
            //如果点击的是后退键  首先判断webView是否能够后退   返回值是boolean类型的
            out_Activity_Dialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    /**
     * 获取新的订单数
     */
    private void goto_Get_Order_Number() {
        try {
            String timestamp = System.currentTimeMillis() + "";
            String token = CreateToken.createToken(userInfo.getUuid(), timestamp, userInfo.getAccount());
            Map<String, String> kdMap = new HashMap<String, String>();
            kdMap.put("account_id", userInfo.getAccount_id());
            kdMap.put("organization_id", userInfo.getOrganization_id());
            kdMap.put("token", token);
            kdMap.put("timestamp", timestamp);
            NetUtils.netWorkByMethodPost(MainActivity.this, kdMap, IpConfig.URL_ORDER_NEWS, handler, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void initialTts() {
        LoggerProxy.printable(true); // 日志打印在logcat中
        // 设置初始化参数
        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
        SpeechSynthesizerListener listener = new UiMessageListener(mainHandler);
        Map<String, String> params = getParams();
        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);

        // 如果您集成中出错，请将下面一段代码放在和demo中相同的位置，并复制InitConfig 和 AutoCheck到您的项目中
        // 上线时请删除AutoCheck的调用
        AutoCheck.getInstance(getApplicationContext()).check(initConfig, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainDebugMessage();
//                        toPrint(message); // 可以用下面一行替代，在logcat中查看代码
                        // Log.w("AutoCheckMessage", message);
                    }
                }
            }

        });
        synthesizer = new NonBlockSyntherizer(this, initConfig, mainHandler); // 此处可以改为MySyntherizer 了解调用过程
    }


//    private void speak() {
//
//        String text ="零壹新科技温馨提示您，您有一份新的订单，请注意查收";
//        // 需要合成的文本text的长度不能超过1024个GBK字节。
//        if (TextUtils.isEmpty(text)) {
//        }
//        // 合成前可以修改参数：
//        // Map<String, String> params = getParams();
//        // synthesizer.setParams(params);
//        int result = synthesizer.speak(text);
////        checkResult(result, "speak");
//    }


    //=======================================================

    /**
     * 合成的参数，可以初始化时填写，也可以在合成前设置。
     *
     * @return
     */
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        // 以下参数均为选填
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        params.put(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, "9");
        // 设置合成的语速，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, "5");

        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

        // 离线资源文件， 从assets目录中复制到临时目录，需要在initTTs方法前完成
        OfflineResource offlineResource = createOfflineResource(offlineVoice);
        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
        params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
        params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
                offlineResource.getModelFilename());
        return params;
    }

    protected OfflineResource createOfflineResource(String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(this, voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
//            toPrint("【error】:copy files from assets failed." + e.getMessage());
        }
        return offlineResource;
    }

    /**
     * 切换离线发音。注意需要添加额外的判断：引擎在合成时该方法不能调用
     */
    private void loadModel(String mode) {
        offlineVoice = mode;
        OfflineResource offlineResource = createOfflineResource(offlineVoice);
//        toPrint("切换离线语音：" + offlineResource.getModelFilename());
        int result = synthesizer.loadModel(offlineResource.getModelFilename(), offlineResource.getTextFilename());
        checkResult(result, "loadModel");
    }

    private void checkResult(int result, String method) {
        if (result != 0) {
            Log.i("URL", "error code :" + result + " method:" + method + ", 错误码文档:http://yuyin.baidu.com/docs/tts/122 ");
        }
    }

    // ===============初始化参数设置完毕，更多合成参数请至getParams()方法中设置 =================

    private void initTTs() {
        LoggerProxy.printable(true); // 日志打印在logcat中
        boolean isMix = ttsMode.equals(TtsMode.MIX);
        boolean isSuccess;
        if (isMix) {
            // 检查2个离线资源是否可读
            isSuccess = checkOfflineResources();
            if (!isSuccess) {
                return;
            } else {
//                print("离线资源存在并且可读, 目录：" + TEMP_DIR);
            }
        }
        // 日志更新在UI中，可以换成MessageListener，在logcat中查看日志
        SpeechSynthesizerListener listener = new UiMessageListener(mainHandler);

        // 1. 获取实例
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        mSpeechSynthesizer.setContext(this);

        // 2. 设置listener
        mSpeechSynthesizer.setSpeechSynthesizerListener(listener);

        // 3. 设置appId，appKey.secretKey
        int result = mSpeechSynthesizer.setAppId(appId);
        checkResult(result, "setAppId");
        result = mSpeechSynthesizer.setApiKey(appKey, secretKey);
        checkResult(result, "setApiKey");

        // 4. 支持离线的话，需要设置离线模型
        if (isMix) {
            // 检查离线授权文件是否下载成功，离线授权文件联网时SDK自动下载管理，有效期3年，3年后的最后一个月自动更新。
            isSuccess = checkAuth();
            if (!isSuccess) {
                return;
            }
            // 文本模型文件路径 (离线引擎使用)， 注意TEXT_FILENAME必须存在并且可读
            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, TEXT_FILENAME);
            // 声学模型文件路径 (离线引擎使用)， 注意TEXT_FILENAME必须存在并且可读
            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, MODEL_FILENAME);
        }

        // 5. 以下setParam 参数选填。不填写则默认值生效
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9");
        // 设置合成的语速，0-9 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-9 ，默认 5
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");

        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

        mSpeechSynthesizer.setAudioStreamType(AudioManager.MODE_IN_CALL);
        // 不使用压缩传输
        // mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_ENCODE, SpeechSynthesizer.AUDIO_ENCODE_PCM);
        // mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_RATE, SpeechSynthesizer.AUDIO_BITRATE_PCM);

        // x. 额外 ： 自动so文件是否复制正确及上面设置的参数
        Map<String, String> params = new HashMap<>();
        // 复制下上面的 mSpeechSynthesizer.setParam参数
        // 上线时请删除AutoCheck的调用
        if (isMix) {
            params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, TEXT_FILENAME);
            params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, MODEL_FILENAME);
        }
        InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);
        AutoCheck.getInstance(getApplicationContext()).check(initConfig, new Handler() {
            @Override
            /**
             * 开新线程检查，成功后回调
             */
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainDebugMessage();
//                        print(message); // 可以用下面一行替代，在logcat中查看代码
                        // Log.w("AutoCheckMessage", message);
                    }
                }
            }

        });

        // 6. 初始化
        result = mSpeechSynthesizer.initTts(ttsMode);
        checkResult(result, "initTts");

    }

    /**
     * 检查appId ak sk 是否填写正确，另外检查官网应用内设置的包名是否与运行时的包名一致。本demo的包名定义在build.gradle文件中
     *
     * @return
     */
    private boolean checkAuth() {
        AuthInfo authInfo = mSpeechSynthesizer.auth(ttsMode);
        if (!authInfo.isSuccess()) {
            // 离线授权需要网站上的应用填写包名。本demo的包名是com.baidu.tts.sample，定义在build.gradle中
            String errorMsg = authInfo.getTtsError().getDetailMessage();
//            print("【error】鉴权失败 errorMsg=" + errorMsg);
            return false;
        } else {
//            print("验证通过，离线正式授权文件存在。");
            return true;
        }
    }

    /**
     * 检查 TEXT_FILENAME, MODEL_FILENAME 这2个文件是否存在，不存在请自行从assets目录里手动复制
     *
     * @return
     */
    private boolean checkOfflineResources() {
        String[] filenames = {TEXT_FILENAME, MODEL_FILENAME};
        for (String path : filenames) {
            File f = new File(path);
            if (!f.canRead()) {
//                print("[ERROR] 文件不存在或者不可读取，请从assets目录复制同名文件到：" + path);
//                print("[ERROR] 初始化失败！！！");
                return false;
            }
        }
        return true;
    }

    private void speak(String num) {
        /* 以下参数每次合成时都可以修改
         *  mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
         *  设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
         *  mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "5"); 设置合成的音量，0-9 ，默认 5
         *  mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5"); 设置合成的语速，0-9 ，默认 5
         *  mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5"); 设置合成的语调，0-9 ，默认 5
         *
         *  mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
         *  MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
         *  MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
         *  MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
         *  MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
         */
        if (mSpeechSynthesizer == null) {
            return;
        }
        String text = "温馨提示，您有" + num + "份订单没有处理，请及时处理";
        int result = mSpeechSynthesizer.speak(text);
        checkResult(result, "speak");
    }

    private void stop() {
        int result = mSpeechSynthesizer.stop();
        checkResult(result, "stop");
    }

    /**
     * 这个是用来接收服务器跑来的数据更新UI
     */
    private class UpdateUIBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int code = intent.getIntExtra("code", 0);
            if (code == 1) {
                goto_Get_Order_Number();
            } else if (code == 0) {

            }
        }
    }

}
