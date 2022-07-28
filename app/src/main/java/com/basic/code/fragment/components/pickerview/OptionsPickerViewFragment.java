package com.basic.code.fragment.components.pickerview;

import android.view.View;
import android.widget.Button;

import com.basic.page.annotation.Page;
import com.basic.face.utils.ResUtils;
import com.basic.face.widget.picker.widget.OptionsPickerView;
import com.basic.face.widget.picker.widget.builder.OptionsPickerBuilder;
import com.basic.code.R;
import com.basic.code.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

@Page(name = "OptionsPickerView\n条件选择器--自定义选择条件")
public class OptionsPickerViewFragment extends BaseFragment {
    @BindView(R.id.btn_sex_picker)
    Button btnSexPicker;
    @BindView(R.id.btn_class_picker)
    Button btnClassPicker;
    @BindView(R.id.btn_constellation_picker)
    Button btnConstellationPicker;
    @BindView(R.id.btn_nation_picker)
    Button btnNationPicker;

    private String[] mSexOption;
    private int sexSelectOption = 0;

    private String[] mGradeOption;
    private String[] mClassOption;
    private String[] mConstellationOption;
    private String[] mNationOption;
    private int gradeSelectOption = 0;
    private int classSelectOption = 0;
    private int constellationSelectOption = 0;
    private int nativeSelectOption = 0;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_options_pickerview;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        mSexOption = ResUtils.getStringArray(R.array.sex_option);
        mGradeOption = ResUtils.getStringArray(R.array.grade_option);
        mConstellationOption = ResUtils.getStringArray(R.array.constellation_entry);
        mNationOption = ResUtils.getStringArray(R.array.nation_value);
        mClassOption = new String[30];
        for (int i = 0; i < mClassOption.length; i++) {
            mClassOption[i] = (i + 1) + "班";
        }
    }

    @OnClick({R.id.btn_sex_picker, R.id.btn_class_picker, R.id.btn_constellation_picker, R.id.btn_nation_picker})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_sex_picker:
                showSexPickerView();
                break;
            case R.id.btn_class_picker:
                showClassPickerView();
                break;
            case R.id.btn_constellation_picker:
                showConstellationPickerView();
                break;
            case R.id.btn_nation_picker:
                showNativePickerView();
                break;
            default:
                break;
        }
    }


    /**
     * 性别选择
     */
    private void showSexPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), (v, options1, options2, options3) -> {
            btnSexPicker.setText(mSexOption[options1]);
            sexSelectOption = options1;
            return false;
        })
                .setTitleText(getString(R.string.title_sex_select))
                .setSelectOptions(sexSelectOption)
                .build();
        pvOptions.setPicker(mSexOption);
        pvOptions.show();
    }


    /**
     * 班级选择
     */
    private void showClassPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), (v, options1, options2, options3) -> {
            btnClassPicker.setText(String.format("%s%s", mGradeOption[options1], mClassOption[options2]));
            gradeSelectOption = options1;
            classSelectOption = options2;
            return false;
        })
                .setTitleText(getString(R.string.title_grade_class_select))
                .setSelectOptions(gradeSelectOption, classSelectOption)
                .build();
        pvOptions.setNPicker(mGradeOption, mClassOption);
        pvOptions.show();
    }

    /**
     * 星座选择
     */
    private void showConstellationPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), (v, options1, options2, options3) -> {
            btnConstellationPicker.setText(mConstellationOption[options1]);
            constellationSelectOption = options1;
            return false;
        })
                .setTitleText(getString(R.string.title_constellation_select))
                .setSelectOptions(constellationSelectOption)
                .build();
        pvOptions.setPicker(mConstellationOption);
        pvOptions.show();
    }

    /**
     * 民族选择
     */
    private void showNativePickerView() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), (v, options1, options2, options3) -> {
            btnNationPicker.setText(mNationOption[options1]);
            nativeSelectOption = options1;
            return false;
        })
                .setTitleText(getString(R.string.title_nation_select))
                .setSelectOptions(nativeSelectOption)
                .build();
        pvOptions.setPicker(mNationOption);
        pvOptions.show();
    }

}
