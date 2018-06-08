package com.zerone.store.shopingtimetest.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zerone.store.shopingtimetest.R;

/**
 * Created by on 2018/5/26 0026 18 16.
 * Author  LiuXingWen
 */

public class LoadingButton extends FrameLayout {
    public boolean isLoading;
    View mRootView;
    /**
     * Top Button
     */
    Button mBtnFirst;
    /**
     * Bottom Button
     */
    Button mBtnSecond;
    View mLoadView;
    TextView mTvLoadInfo;
    ProgressBar mPbLoading;
    /**
     * Top Button Listener
     */
    OnFirstBtnClickListener mFirstListener;
    /**
     * Bottom Button Linstener
     */
    OnSecondBtnListener mSecondListener;
    private CharSequence mFirstText;
    private CharSequence mSecodText;
    //	private int mTBgResId = R.drawable.sele_btn_orange;
//	private int mBBgResId = R.drawable.sele_btn_green;
    private Drawable mFirstDrawable;
    private Drawable mSecondDrawable;
    private long mBarMillis = 300;
    private long mTextMillis = 700;
    private long mHideBarAnima = 300;

    public LoadingButton(Context context, AttributeSet attrs) {

        super(context, attrs);
//		mFirstDrawable = context.getResources().getDrawable(R.drawable.sele_btn_pink);
//		mSecondDrawable = context.getResources().getDrawable(R.drawable.sele_btn_green);

        initView();

        initEvent();

        initValue(attrs);
    }


    public LoadingButton(Context context) {
        super(context);
        initView();

        initEvent();
    }

    @SuppressLint("NewApi")
    private void initValue(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.FrameButton);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.FrameButton_firstText:
                    mFirstText = a.getString(attr);
                    break;
                case R.styleable.FrameButton_firstBackGround:
                    mFirstDrawable = a.getDrawable(attr);
                    break;
                case R.styleable.FrameButton_secondText:
                    mSecodText = a.getString(attr);
                    break;

                case R.styleable.FrameButton_secondBackGround:
                    mSecondDrawable = a.getDrawable(attr);
                    break;
                default:
                    break;
            }
        }

        a.recycle();

        mBtnFirst.setText(mFirstText);
        mBtnSecond.setText(mSecodText);

        if (mFirstDrawable != null)
            mBtnFirst.setBackground(mFirstDrawable);
        if (mSecondDrawable != null)
            mBtnSecond.setBackground(mSecondDrawable);

        isLoading = false;
    }

    private void initView() {
        mRootView = View.inflate(getContext(), R.layout.frame_button, this);
        mBtnSecond = findViewById(R.id.cus_btn_second);
        mBtnFirst = findViewById(R.id.cus_btn_first);
        mLoadView = findViewById(R.id.cus_loading);
        mTvLoadInfo = findViewById(R.id.cus_loading_tv_info);
        mPbLoading = findViewById(R.id.cus_loading_pb);
    }

    private void initEvent() {
        mBtnSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoadingView();
                clearText(mBtnSecond);

                if (null != mSecondListener)
                    mSecondListener.onSecondClick(view);
            }
        });

        mBtnFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoadingView();
                clearText(mBtnFirst);
                clearText(mBtnSecond);

                if (null != mFirstListener) {
                    mFirstListener.onFirstClick(view);
                }

            }
        });
    }

    public View getLoadView() {
        return mLoadView;
    }

    public ProgressBar getLoadProBar() {
        return mPbLoading;
    }

    public TextView getLoadTextView() {
        return mTvLoadInfo;
    }

    public void setLoadInfoVisible(int visility) {
        mTvLoadInfo.setVisibility(visility);
    }

    public void setLoadInfoText(CharSequence infoText) {
        mTvLoadInfo.setText(infoText);
    }

    public Button getFirstButton() {
        return mBtnFirst;
    }

    public Button getSecondButton() {
        return mBtnSecond;
    }

    // visibility
    public void hideLoadingView() {
        isLoading = false;
        AlphaAnimation alpha = new AlphaAnimation(1, 0);
        alpha.setDuration(mHideBarAnima);
        mLoadView.startAnimation(alpha);
        mLoadView.setVisibility(GONE);
    }

    public void showLoadingView() {
        isLoading = true;
        mLoadView.setVisibility(VISIBLE);
    }


    public void showFirstBotton() {
        if (!isLoading) {
            return;
        }

        if (mBtnSecond.getVisibility() != GONE) {
            mBtnSecond.setVisibility(GONE);
        }
        if (mBtnFirst.getVisibility() != VISIBLE) {
            mBtnFirst.setVisibility(VISIBLE);
        }

        postDelayed(new Runnable() {
            @Override
            public void run() {
                hideLoadingView();
            }
        }, mBarMillis);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                // reset button text
                resetText(mBtnFirst);
            }
        }, mTextMillis);
    }

    public void showSecondBotton() {
        if (!isLoading) {
            return;
        }

        if (mBtnFirst.getVisibility() != GONE) {
            mBtnFirst.setVisibility(GONE);
        }

        if (mBtnSecond.getVisibility() != VISIBLE) {
            mBtnSecond.setVisibility(VISIBLE);
        }

        postDelayed(new Runnable() {
            @Override
            public void run() {
                hideLoadingView();
            }
        }, mBarMillis);

        postDelayed(new Runnable() {
            @Override
            public void run() {// reset button text
                resetText(mBtnSecond);
            }
        }, mTextMillis);
    }


    public void clearText(Button button) {
        if (mBtnFirst == button) {
            if (!TextUtils.isEmpty(mBtnFirst.getText()))
                mFirstText = mBtnFirst.getText();
        } else if (mBtnSecond == button) {
            if (!TextUtils.isEmpty(mBtnSecond.getText()))
                mSecodText = mBtnSecond.getText();
        }
        button.setText("");
    }

    void resetText(Button button) {
        if (mBtnFirst == button) {
            if (!TextUtils.isEmpty(mFirstText)) {
                mBtnFirst.setText(mFirstText);
            }
        } else if (mBtnSecond == button) {
            if (!TextUtils.isEmpty(mSecodText)) {
                mBtnSecond.setText(mSecodText);
            }
        }
    }


    public void setHideLoadingDeyed(long millis) {
        if (millis >= 0) {
            this.mBarMillis = millis;
        }
    }

    public void setShowLoadInfoMillis(long millis) {
        if (millis >= 0) {
            this.mTextMillis = millis;
        }
    }

    public void setBarAnima(long millis) {
        if (millis >= 0) {
            this.mBarMillis = millis;
        }
    }

    // setting text
    public void setSecondText(CharSequence text) {
        mBtnSecond.setText(text);
    }

    public void setSecondText(int resId) {
        setSecondText(getContext().getString(resId));
    }

    public void setFirstText(CharSequence text) {
        mBtnFirst.setText(text);
    }

    public void setFirstText(int resid) {
        setFirstText(getContext().getString(resid));
    }

    // interface
    public void setOnFistBtnListener(OnFirstBtnClickListener listen) {
        this.mFirstListener = listen;
    }

    public void setOnSecondBtnListener(OnSecondBtnListener listen) {
        this.mSecondListener = listen;
    }

    public interface OnFirstBtnClickListener {

        /**
         * @param view 如果需要使用到另一个按钮，应当在方法执行完毕后调用
         *             {@see link FrameButton#showSecondBotton()},
         *             否则调用{@link LoadingButton#showFirstBotton()}}
         */
        void onFirstClick(View view);
    }

    public interface OnSecondBtnListener {
        //        /**如果需要使用到另一个按钮，应当在方法执行完毕后调用{@link LoadingButton#showFirstBotton(),否则调用{@link FrameButton#showSecondBotton()}}*/
        void onSecondClick(View view);
    }
}
