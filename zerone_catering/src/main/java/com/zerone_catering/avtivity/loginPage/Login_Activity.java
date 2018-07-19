package com.zerone_catering.avtivity.loginPage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.zerone_catering.DB.impl.AccountInfoDao;
import com.zerone_catering.DB.impl.UserInfoImpl;
import com.zerone_catering.R;
import com.zerone_catering.avtivity.BaseSet.BaseActvity;
import com.zerone_catering.avtivity.MainActivity;
import com.zerone_catering.domain.UserInfo;
import com.zerone_catering.domain.login.Account;
import com.zerone_catering.retrofitIp.CateringIp;
import com.zerone_catering.retrofitIp.ResponseUtils;
import com.zerone_catering.utils.AppSharePreferenceMgr;
import com.zerone_catering.utils.IsIntentExite;
import com.zerone_catering.utils.LoadingUtils;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by on 2018/6/21 0021 09 37.
 * Author  LiuXingWen
 */

public class Login_Activity extends BaseActvity {
    @Bind(R.id.closeactivity)
    ImageView closeactivity;
    @Bind(R.id.username)
    EditText username;
    @Bind(R.id.clearusername)
    ImageView clearusername;
    @Bind(R.id.noneinfo)
    ImageView noneinfo;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.showpassword)
    ImageView showpassword;
    @Bind(R.id.loginbtn)
    Button loginbtn;
    @Bind(R.id.login_rember_account)
    CheckBox loginRemberAccount;
    @Bind(R.id.tongyi)
    TextView tongyi;
    @Bind(R.id.agreement)
    TextView agreement;
    @Bind(R.id.xiyi)
    RelativeLayout xiyi;
    @Bind(R.id.companyname)
    TextView companyname;
    private ZLoadingDialog loading_dailog;
    private AccountInfoDao accountInfoDao;
    private boolean remberChecked = true;
    private boolean showBoolean = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String loginJson = (String) msg.obj;
                    Log.i("URL", loginJson);
                    if (loading_dailog != null) {
                        loading_dailog.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(loginJson);
                        int status = jsonObject.getInt("status");
                        if (status == 0) {
                            //失败
                            String errormsg = jsonObject.getString("msg");
                            Toast.makeText(Login_Activity.this, errormsg, Toast.LENGTH_SHORT).show();
                        } else if (status == 1) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            UserInfo userInfo = new UserInfo();
                            userInfo.setUuid(data.getString("uuid"));
                            userInfo.setOrganization_id(data.getString("organization_id"));
                            userInfo.setAccount_id(data.getString("account_id"));
                            userInfo.setAccount(data.getString("account"));
                            userInfo.setRealName(data.getString("realname"));
                            userInfo.setOrganization_name(data.getString("organization_name"));
                            userInfo.setFansmanage_id(data.getString("fansmanage_id"));
                            saveUserInfo(userInfo);
                            //记住用账号
                            Account account = new Account();
                            account.setAccount_name(username.getText().toString());
                            account.setAccount_pwd(password.getText().toString());
                            if (remberChecked) {
                                saveAccountIntoTable(account);
                            } else {
                                clearAccount();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    if (showBoolean) {
                        password.setInputType(InputType.TYPE_CLASS_TEXT);
                    } else {
                        password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    }
                    break;
                case 511:
                    VolleyError error = (VolleyError) msg.obj;
                    if (error != null) {
                        if (error instanceof TimeoutError) {
                            Toast.makeText(Login_Activity.this, "网络请求超时，请重试！", Toast.LENGTH_SHORT).show();
                            loading_dailog.dismiss();
                            return;
                        }
                        if (error instanceof ServerError) {
                            Toast.makeText(Login_Activity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                            loading_dailog.dismiss();
                            return;
                        }
                        if (error instanceof NetworkError) {
                            Toast.makeText(Login_Activity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                            loading_dailog.dismiss();
                            return;
                        }
                        if (error instanceof ParseError) {
                            Toast.makeText(Login_Activity.this, "数据格式错误", Toast.LENGTH_SHORT).show();
                            loading_dailog.dismiss();
                            return;
                        }
                        Toast.makeText(Login_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        loading_dailog.dismiss();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //获取POS机的信息
        initPosInfo();
        initView();
    }

    private void initView() {
        remberChecked = (boolean) AppSharePreferenceMgr.get(Login_Activity.this, "remberChecked", false);
        remberChecked = loginRemberAccount.isChecked();
        accountInfoDao = new AccountInfoDao(Login_Activity.this);
        try {
            if (accountInfoDao != null) {
                Account account = accountInfoDao.getAccount("10");
                username.setText(account.getAccount_name());
                password.setText(account.getAccount_pwd());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.closeactivity, R.id.clearusername, R.id.loginbtn, R.id.login_rember_account, R.id.xiyi, R.id.showpassword})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.closeactivity:
                Login_Activity.this.finish();
                break;
            case R.id.clearusername:
                break;
            case R.id.loginbtn:
                gotoLogin();
                break;
            case R.id.login_rember_account:
                AppSharePreferenceMgr.put(Login_Activity.this, "remberChecked", loginRemberAccount.isChecked());
                break;
            case R.id.xiyi:
                break;
            case R.id.showpassword:
                showBoolean = !showBoolean;
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
                break;
        }
    }
    /**
     * 登陆操作
     */
    private void gotoLogin() {
        String user = username.getText().toString();
        String pwd = password.getText().toString();
        String terminalId = (String) AppSharePreferenceMgr.get(Login_Activity.this, "terminalId", "");
        loading_dailog = LoadingUtils.getDailog(Login_Activity.this, Color.RED, "登录中...");
        loading_dailog.show();
        CateringIp cateringIp = ResponseUtils.getCateringIp();
        Call<ResponseBody> userInfo = cateringIp.login(user, pwd, terminalId);
        userInfo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Message message = new Message();
                    Log.i("URL", response.toString());
                    message.obj = response.body().string();
                    message.what = 0;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("URL", call.toString());
            }
        });

    }

    /**
     * 保存账号信息到数据库 用来做返显
     *
     * @param account
     */
    private void saveAccountIntoTable(Account account) {
        try {
            Account ac = accountInfoDao.getAccount("10");
            if (ac == null) {
                accountInfoDao.saveAccount(account);
            } else {
                accountInfoDao.deltable();
                accountInfoDao.saveAccount(account);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清楚数据库中的表
     */
    private void clearAccount() {
        try {
            accountInfoDao.deltable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存用户信息
     */
    private void saveUserInfo(UserInfo userInfo) {
        UserInfoImpl userimpl = new UserInfoImpl(Login_Activity.this);
        try {
            UserInfo user = userimpl.getUserInfo("10");
            if (user == null) {
                Log.i("URL", "第一次向数据库里填写东西");
                userimpl.saveUserInfo(userInfo);
            } else {
                userimpl.deltable();
                userimpl.saveUserInfo(userInfo);
            }
            Intent intent = new Intent(Login_Activity.this, MainActivity.class);
            startActivity(intent);
            Login_Activity.this.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initPosInfo() {
        Intent intent = new Intent("sunmi.payment.L3");
        String transId = System.currentTimeMillis() + "";
        intent.putExtra("transId", transId);
        intent.putExtra("transType", 13);
        intent.putExtra("appId", getPackageName());
        //判断intent是否存在
        if (IsIntentExite.isIntentExisting(intent, Login_Activity.this)) {
            startActivity(intent);
            AppSharePreferenceMgr.put(Login_Activity.this, "transType", "13");
        } else {
            Toast.makeText(this, "此机器上没有安装L3应用", Toast.LENGTH_SHORT).show();
        }
    }
}
