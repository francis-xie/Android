
package com.basic.code.fragment.expands;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.basic.cities.CitySelector;
import com.basic.cities.adapter.OnLocationListener;
import com.basic.cities.adapter.OnPickListener;
import com.basic.cities.model.City;
import com.basic.cities.model.HotCity;
import com.basic.cities.model.LocateState;
import com.basic.aop.annotation.Permission;
import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.LocationService;
import com.basic.code.utils.Utils;
import com.basic.code.utils.XToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.basic.aop.consts.PermissionConsts.LOCATION;

@Page(name = "城市选择", extra = R.drawable.ic_expand_location)
public class CitySelectorFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.tv_current)
    TextView tvCurrent;
    @BindView(R.id.btn_style)
    Button btnStyle;
    @BindView(R.id.cb_hot)
    CheckBox cbHot;
    @BindView(R.id.cb_enable_anim)
    CheckBox cbEnableAnim;
    @BindView(R.id.cb_anim)
    CheckBox cbAnim;

    private List<HotCity> mHotCities;
    private int mAnim;
    private int mTheme;
    private boolean mEnableAnimation;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_city_picker;
    }

    @Override
    protected TitleBar initTitle() {
        TitleBar titleBar = super.initTitle();
        titleBar.addAction(new TitleBar.TextAction("Github") {
            @Override
            public void performAction(View view) {
                Utils.goWeb(getContext(), "https://github.com/zhiqiang/CitySelector");
            }
        });
        return titleBar;
    }

    @Override
    protected void initArgs() {
        super.initArgs();
        mTheme = R.style.DefaultCitySelectorTheme;
        btnStyle.setText("默认主题");
        getActivity().setTheme(mTheme);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initListeners() {
        cbHot.setOnCheckedChangeListener(this);
        cbEnableAnim.setOnCheckedChangeListener(this);
        cbAnim.setOnCheckedChangeListener(this);
    }

    @SingleClick
    @OnClick({R.id.btn_style, R.id.btn_pick})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_style:
                if (btnStyle.getText().toString().startsWith("自定义")) {
                    btnStyle.setText("默认主题");
                    mTheme = R.style.DefaultCitySelectorTheme;
                } else if (btnStyle.getText().toString().startsWith("默认")) {
                    btnStyle.setText("自定义主题");
                    mTheme = R.style.CustomCitySelectorTheme;
                }
                getActivity().setTheme(mTheme);
                break;
            case R.id.btn_pick:
                pickCity();
                break;
            default:
                break;
        }
    }

    @Permission(LOCATION)
    private void pickCity() {
        CitySelector.from(this)
                .enableAnimation(mEnableAnimation)
                .setAnimationStyle(mAnim)
                .setLocatedCity(null)
                .setHotCities(mHotCities)
                .setOnPickListener(new OnPickListener() {

                    OnBDLocationListener mListener = new OnBDLocationListener();

                    @Override
                    public void onPick(int position, City data) {
                        tvCurrent.setText(String.format("当前城市：%s，%s", data.getName(), data.getCode()));
                        XToastUtils.toast(String.format("点击的数据：%s，%s", data.getName(), data.getCode()));
                        LocationService.stop(mListener);
                    }

                    @Override
                    public void onCancel() {
                        XToastUtils.toast("取消选择");
                        LocationService.stop(mListener);
                    }

                    @Override
                    public void onLocate(final OnLocationListener locationListener) {
                        //开始定位
                        mListener.setOnLocationListener(locationListener);
                        LocationService.start(mListener);
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        //模拟定位成功
//                                        locationListener.onLocationChanged(new LocatedCity("深圳", "广东", "101280601"), LocateState.SUCCESS);
//                                    }
//                                }, 5000);
                    }

                })
                .show();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_hot:
                if (isChecked) {
                    mHotCities = new ArrayList<>();
                    mHotCities.add(new HotCity("北京", "北京", "101010100"));
                    mHotCities.add(new HotCity("上海", "上海", "101020100"));
                    mHotCities.add(new HotCity("广州", "广东", "101280101"));
                    mHotCities.add(new HotCity("深圳", "广东", "101280601"));
                    mHotCities.add(new HotCity("杭州", "浙江", "101210101"));
                } else {
                    mHotCities = null;
                }
                break;
            case R.id.cb_anim:
                mAnim = isChecked ? R.style.CustomAnim : R.style.CitySelectorAnimation;
                break;
            case R.id.cb_enable_anim:
                mEnableAnimation = isChecked;
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        resetTheme(); //主题还原
    }

    /**
     * 初始化主题
     */
    private void resetTheme() {
        Utils.initTheme(getActivity());
    }

    /**
     * 百度定位
     */
    public static class OnBDLocationListener extends BDAbstractLocationListener {

        private OnLocationListener mOnLocationListener;

        public OnBDLocationListener setOnLocationListener(OnLocationListener onLocationListener) {
            mOnLocationListener = onLocationListener;
            return this;
        }

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (mOnLocationListener != null) {
                mOnLocationListener.onLocationChanged(LocationService.onReceiveLocation(bdLocation), LocateState.SUCCESS);
                LocationService.get().unregisterListener(this);
            }
        }
    }

}
