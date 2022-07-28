
package com.basic.code.fragment.components.pickerview;

import android.graphics.Color;
import android.view.View;

import com.basic.aop.annotation.IOThread;
import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.widget.picker.widget.OptionsPickerView;
import com.basic.face.widget.picker.widget.builder.OptionsPickerBuilder;
import com.basic.code.DemoDataProvider;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;
import com.basic.code.utils.XToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

@Page(name = "OptionsPicker\n条件选择器--省市区三级联动")
public class AddressPickerFragment extends BaseFragment {

    private List<ProvinceInfo> options1Items = new ArrayList<>();
    private List<List<String>> options2Items = new ArrayList<>();
    private List<List<List<String>>> options3Items = new ArrayList<>();

    private boolean mHasLoaded;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_address_picker;
    }

    @Override
    protected void initArgs() {
        loadData(DemoDataProvider.getProvinceInfos());
    }

    @Override
    protected void initViews() {

    }

    @SingleClick
    @OnClick({R.id.btn_picker, R.id.btn_picker_dialog})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_picker:
                showPickerView(false);
                break;
            case R.id.btn_picker_dialog:
                showPickerView(true);
                break;
            default:
                break;
        }
    }

    @IOThread
    private void loadData(List<ProvinceInfo> provinceInfos) {//加载数据
        /**
         * 添加省份数据
         */
        options1Items = provinceInfos;

        //遍历省份（第一级）
        for (ProvinceInfo provinceInfo : provinceInfos) {
            //该省的城市列表（第二级）
            List<String> cityList = new ArrayList<>();
            //该省的所有地区列表（第三级）
            List<List<String>> areaList = new ArrayList<>();

            for (ProvinceInfo.City city : provinceInfo.getCityList()) {
                //添加城市
                String cityName = city.getName();
                cityList.add(cityName);
                //该城市的所有地区列表
                List<String> cityAreaList = new ArrayList<>();
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (city.getArea() == null || city.getArea().size() == 0) {
                    cityAreaList.add("");
                } else {
                    cityAreaList.addAll(city.getArea());
                }
                //添加该省所有地区数据
                areaList.add(cityAreaList);
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(areaList);
        }

        mHasLoaded = true;
    }


    private void showPickerView(boolean isDialog) {// 弹出选择器
        if (!mHasLoaded) {
            XToastUtils.toast("数据加载中...");
            return;
        }

        int[] defaultSelectOptions = getDefaultCity();

        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), (v, options1, options2, options3) -> {
            //返回的分别是三个级别的选中位置
            String tx = options1Items.get(options1).getPickerViewText() + "-" +
                    options2Items.get(options1).get(options2) + "-" +
                    options3Items.get(options1).get(options2).get(options3);

            XToastUtils.toast(tx);
            return false;
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                //切换选项时，还原到第一项
                .isRestoreItem(true)
                //设置选中项文字颜色
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .isDialog(isDialog)
                .setSelectOptions(defaultSelectOptions[0], defaultSelectOptions[1], defaultSelectOptions[2])
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    /**
     * @return 获取默认城市的索引
     */
    private int[] getDefaultCity() {
        int[] res = new int[3];
        ProvinceInfo provinceInfo;
        List<ProvinceInfo.City> cities;
        ProvinceInfo.City city;
        List<String> ares;
        for (int i = 0; i < options1Items.size(); i++) {
            provinceInfo = options1Items.get(i);
            if ("江苏省".equals(provinceInfo.getName())) {
                res[0] = i;
                cities = provinceInfo.getCityList();
                for (int j = 0; j < cities.size(); j++) {
                    city = cities.get(j);
                    if ("南京市".equals(city.getName())) {
                        res[1] = j;
                        ares = city.getArea();
                        for (int k = 0; k < ares.size(); k++) {
                            if ("雨花台区".equals(ares.get(k))) {
                                res[2] = k;
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }
        return res;
    }

    @Override
    public void onDestroyView() {
        options1Items.clear();
        options2Items.clear();
        options3Items.clear();
        mHasLoaded = false;
        super.onDestroyView();
    }
}
