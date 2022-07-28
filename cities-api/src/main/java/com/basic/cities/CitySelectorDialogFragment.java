package com.basic.cities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.basic.cities.adapter.CityListAdapter;
import com.basic.cities.adapter.InnerListener;
import com.basic.cities.adapter.OnLocationListener;
import com.basic.cities.adapter.OnPickListener;
import com.basic.cities.adapter.decoration.DividerItemDecoration;
import com.basic.cities.adapter.decoration.SectionItemDecoration;
import com.basic.cities.data.ICityCenter;
import com.basic.cities.model.City;
import com.basic.cities.model.HotCity;
import com.basic.cities.model.LocateState;
import com.basic.cities.model.LocatedCity;
import com.basic.cities.util.ScreenUtils;
import com.basic.cities.view.SideIndexBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市选择的弹窗页面
 */
public class CitySelectorDialogFragment extends DialogFragment implements TextWatcher,
        View.OnClickListener, SideIndexBar.OnIndexTouchedChangedListener, InnerListener, OnLocationListener {
    private static final String KEY_ENABLE_ANIM = "cp_enable_anim";

    private View mContentView;
    private RecyclerView mRecyclerView;
    private View mEmptyView;
    private TextView mOverlayTextView;
    private SideIndexBar mIndexBar;
    private EditText mSearchBox;
    private TextView mCancelBtn;
    private ImageView mClearAllBtn;

    private LinearLayoutManager mLayoutManager;
    private CityListAdapter mAdapter;
    private List<City> mAllCities;
    private List<HotCity> mHotCities;
    private List<City> mResults;

    private ICityCenter mCityCenter;

    private int height;
    private int width;

    private boolean enableAnim = false;
    private int mAnimStyle = R.style.CitySelectorAnimation;
    private LocatedCity mLocatedCity;
    private int locateState;
    private OnPickListener mOnPickListener;

    /**
     * 获取实例
     *
     * @param enable 是否启用动画效果
     * @return
     */
    public static CitySelectorDialogFragment newInstance(boolean enable) {
        final CitySelectorDialogFragment fragment = new CitySelectorDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(KEY_ENABLE_ANIM, enable);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.CitySelectorStyle);
    }

    public void setLocatedCity(LocatedCity location) {
        mLocatedCity = location;
    }

    public void setHotCities(List<HotCity> data) {
        if (data != null && !data.isEmpty()) {
            this.mHotCities = data;
        }
    }

    @SuppressLint("ResourceType")
    public void setAnimationStyle(@StyleRes int resId) {
        this.mAnimStyle = resId <= 0 ? mAnimStyle : resId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.cp_dialog_city_picker, container, false);
        return mContentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initViews();
    }

    private void initViews() {
        mRecyclerView = mContentView.findViewById(R.id.cp_city_recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SectionItemDecoration(getActivity(), mAllCities), 0);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()), 1);
        mAdapter = new CityListAdapter(getActivity(), mAllCities, mHotCities, locateState);
        mAdapter.autoLocate(true);
        mAdapter.setInnerListener(this);
        mAdapter.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //确保定位城市能正常刷新
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mAdapter.refreshLocationItem();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });

        mEmptyView = mContentView.findViewById(R.id.cp_empty_view);
        mOverlayTextView = mContentView.findViewById(R.id.cp_overlay);

        mIndexBar = mContentView.findViewById(R.id.cp_side_index_bar);
        mIndexBar.setNavigationBarHeight(ScreenUtils.getNavigationBarHeight(getActivity()));
        mIndexBar.setOverlayTextView(mOverlayTextView)
                .setOnIndexChangedListener(this);

        mSearchBox = mContentView.findViewById(R.id.cp_search_box);
        mSearchBox.addTextChangedListener(this);

        mCancelBtn = mContentView.findViewById(R.id.cp_cancel);
        mClearAllBtn = mContentView.findViewById(R.id.cp_clear_all);
        mCancelBtn.setOnClickListener(this);
        mClearAllBtn.setOnClickListener(this);
    }

    private void initData() {
        Bundle args = getArguments();
        if (args != null) {
            enableAnim = args.getBoolean(KEY_ENABLE_ANIM);
        }
        //初始化热门城市
        if (mHotCities == null || mHotCities.isEmpty()) {
            mHotCities = new ArrayList<>();
            mHotCities.add(new HotCity("北京", "北京", "101010100"));
            mHotCities.add(new HotCity("上海", "上海", "101020100"));
            mHotCities.add(new HotCity("广州", "广东", "101280101"));
            mHotCities.add(new HotCity("深圳", "广东", "101280601"));
            mHotCities.add(new HotCity("天津", "天津", "101030100"));
            mHotCities.add(new HotCity("杭州", "浙江", "101210101"));
            mHotCities.add(new HotCity("南京", "江苏", "101190101"));
            mHotCities.add(new HotCity("成都", "四川", "101270101"));
            mHotCities.add(new HotCity("武汉", "湖北", "101200101"));
        }
        //初始化定位城市，默认为空时会自动回调定位
        if (mLocatedCity == null) {
            mLocatedCity = new LocatedCity(getString(R.string.cp_locating), "未知", "0");
            locateState = LocateState.LOCATING;
        } else {
            locateState = LocateState.SUCCESS;
        }

        mCityCenter = CitySelector.getICityCenter(getContext());
        mAllCities = mCityCenter.getAllCities();
        mAllCities.add(0, mLocatedCity);
        mAllCities.add(1, new HotCity("热门城市", "未知", "0"));
        mResults = mAllCities;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mOnPickListener != null) {
                        mOnPickListener.onCancel();
                    }
                }
                return false;
            }
        });

        measure();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            window.setGravity(Gravity.BOTTOM);
            window.setLayout(width, height - ScreenUtils.getStatusBarHeight(getActivity()));
            if (enableAnim) {
                window.setWindowAnimations(mAnimStyle);
            }
        }
    }

    private void measure() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(dm);
            height = dm.heightPixels;
            width = dm.widthPixels;
        } else {
            DisplayMetrics dm = getResources().getDisplayMetrics();
            height = dm.heightPixels;
            width = dm.widthPixels;
        }
    }

    /**
     * 搜索框监听
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String keyword = s.toString();
        if (TextUtils.isEmpty(keyword)) {
            mClearAllBtn.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.GONE);
            mResults = mAllCities;
            ((SectionItemDecoration) (mRecyclerView.getItemDecorationAt(0))).setData(mResults);
            mAdapter.updateData(mResults);
        } else {
            mClearAllBtn.setVisibility(View.VISIBLE);
            //开始数据库查找
            mResults = mCityCenter.searchCity(keyword);
            ((SectionItemDecoration) (mRecyclerView.getItemDecorationAt(0))).setData(mResults);
            if (mResults == null || mResults.isEmpty()) {
                mEmptyView.setVisibility(View.VISIBLE);
            } else {
                mEmptyView.setVisibility(View.GONE);
                mAdapter.updateData(mResults);
            }
        }
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cp_cancel) {
            dismiss();
            if (mOnPickListener != null) {
                mOnPickListener.onCancel();
            }
        } else if (id == R.id.cp_clear_all) {
            mSearchBox.setText("");
        }
    }

    @Override
    public void onIndexChanged(String index, int position) {
        //滚动RecyclerView到索引位置
        mAdapter.scrollToSection(index);
    }

    @Override
    public void onLocationChanged(LocatedCity location, int state) {
        mAdapter.updateLocateState(location, state);
    }

    @Override
    public void pick(int position, City city) {
        if (mEmptyView.getVisibility() == View.VISIBLE) {
            return;
        }

        dismiss();
        if (mOnPickListener != null) {
            mOnPickListener.onPick(position, city);
        }
    }

    @Override
    public void locate() {
        if (mOnPickListener != null) {
            mOnPickListener.onLocate(this);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        ScreenUtils.hideSoftInput(mSearchBox);
    }

    public void setOnPickListener(OnPickListener listener) {
        mOnPickListener = listener;
    }

}
