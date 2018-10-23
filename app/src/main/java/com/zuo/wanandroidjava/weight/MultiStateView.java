package com.zuo.wanandroidjava.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.zuo.wanandroidjava.R;

//多状态布局
public class MultiStateView extends FrameLayout {
    private SparseArray<View> sparseViews;

    public static final int STATE_CONTENT = 1000;
    public static final int STATE_LOADING = 1001;
    public static final int STATE_EMPTY = 1002;
    public static final int STATE_FAIL = 1003;
    private int currentState = STATE_CONTENT;
    private LayoutInflater layoutInflater;
    private View contentView = null;

    public MultiStateView(@NonNull Context context) {
        this(context, null);
    }

    public MultiStateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public MultiStateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sparseViews = new SparseArray();
        layoutInflater = LayoutInflater.from(context);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiStateView);
        int resIdEmpty = a.getResourceId(R.styleable.MultiStateView_msv_emptyView, -1);
        int resIdLoading = a.getResourceId(R.styleable.MultiStateView_msv_loadingView, -1);
        int resIdFail = a.getResourceId(R.styleable.MultiStateView_msv_failView, -1);
        a.recycle();
        if (resIdEmpty != -1) {
            addViewForStatus(STATE_EMPTY, resIdEmpty);
        }


        if (resIdLoading != -1) {
            addViewForStatus(STATE_LOADING, resIdLoading);
        }


        if (resIdFail != -1) {
            addViewForStatus(STATE_FAIL, resIdFail);
        }


    }
    //改变状态
    public void setViewState(int state) {
        if (getCurrentView()==null) {
            return;
        }
        if (state != currentState) {
            View view = getView(state);
            getCurrentView().setVisibility(GONE);
            currentState = state;
            if (view!=null) {
                view.setVisibility(VISIBLE);
            }

        }


    }


    public View getCurrentView() {
        return getView(currentState);
    }


    private void addViewForStatus(int stateEmpty, int resIdEmpty) {
        View view = layoutInflater.inflate(resIdEmpty, this,false);
        sparseViews.put(stateEmpty, view);
        addView(view);
        view.setVisibility(GONE);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        validContentView(child);
        super.addView(child, index, params);
    }


    @Override
    public void addView(View child, int width, int height) {
        validContentView(child);
        super.addView(child, width, height);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        validContentView(child);
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index) {
        validContentView(child);
        super.addView(child, index);
    }

    @Override
    public void addView(View child) {
        validContentView(child);
        super.addView(child);
    }


    private void validContentView(View view) {
        if (isValidContentView(view)) {
            contentView = view;
            sparseViews.put(STATE_CONTENT, contentView);
        } else if (currentState != STATE_CONTENT) {
            contentView.setVisibility(GONE);
        }
    }

    public boolean isValidContentView(View view) {
        if (contentView == null) {
            for (int i = 0; i < sparseViews.size(); i++) {

                if (sparseViews.indexOfValue(view) != -1) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public View getView(int state) {
        return sparseViews.get(state);
    }

    public MultiStateView setEmptyResource(@LayoutRes int emptyResource) {
        addViewForStatus(STATE_EMPTY,emptyResource);
        return this;
    }
    public MultiStateView setLoadingResource(@LayoutRes int emptyResource) {
        addViewForStatus(STATE_LOADING,emptyResource);
        return this;
    }
    public MultiStateView setFailResource(@LayoutRes int emptyResource) {
        addViewForStatus(STATE_FAIL,emptyResource);
        return this;
    }

    public MultiStateView build() {
        showLoadingView();
        return this;
    }

    /**
     *  加载
     */

   public void showLoadingView() {
        setViewState(MultiStateView.STATE_LOADING);
    }

    /**
     *  异常
     */
    public void showErrorView() {
        if (getViewState() != MultiStateView.STATE_CONTENT) {
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setViewState(MultiStateView.STATE_FAIL);
                }
            }, 100);
        }

    }

    public void showEmptyView() {
        if (getViewState() != STATE_CONTENT) {
            setViewState(STATE_EMPTY);
        }
    }


    /**
     * 显示内容
     */
    public void showContent() {

        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                setViewState(MultiStateView.STATE_CONTENT);
            }
        }, 100);
    }

    public int getViewState() {
        return currentState;
    }
}
