
package com.basic.code.fragment.components.spinner;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.basic.aop.annotation.SingleClick;
import com.basic.page.annotation.Page;
import com.basic.face.utils.ResUtils;
import com.basic.face.widget.actionbar.TitleBar;
import com.basic.face.widget.spinner.DropDownMenu;
import com.basic.code.R;
import com.basic.code.adapter.dropdownmenu.CityDropDownAdapter;
import com.basic.code.adapter.dropdownmenu.ConstellationAdapter;
import com.basic.code.adapter.dropdownmenu.ListDropDownAdapter;
import com.basic.code.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**

 * @since 2019-11-28 15:24
 */
@Page(name = "DropDownMenu\n下拉选择菜单")
public class DropDownMenuFragment extends BaseFragment {

    @BindView(R.id.ddm_content)
    DropDownMenu mDropDownMenu;

    private String[] mHeaders = {"城市", "年龄", "性别", "星座"};
    private List<View> mPopupViews = new ArrayList<>();

    private CityDropDownAdapter mCityAdapter;
    private ListDropDownAdapter mAgeAdapter;
    private ListDropDownAdapter mSexAdapter;
    private ConstellationAdapter mConstellationAdapter;

    private String[] mCitys;
    private String[] mAges;
    private String[] mSexs;
    private String[] mConstellations;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_drop_down_menu;
    }

    @Override
    protected TitleBar initTitle() {
        return super.initTitle().setLeftClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                handleBackPressed();
            }
        });
    }

    @Override
    protected void initArgs() {
        mCitys = ResUtils.getStringArray(R.array.city_entry);
        mAges = ResUtils.getStringArray(R.array.age_entry);
        mSexs = ResUtils.getStringArray(R.array.sex_entry);
        mConstellations = ResUtils.getStringArray(R.array.constellation_entry);
    }

    @Override
    protected void initViews() {
        final ListView cityView = new ListView(getContext());
        mCityAdapter = new CityDropDownAdapter(getContext(), mCitys);
        cityView.setDividerHeight(0);
        cityView.setAdapter(mCityAdapter);

        //init age menu
        final ListView ageView = new ListView(getContext());
        ageView.setDividerHeight(0);
        mAgeAdapter = new ListDropDownAdapter(getContext(), mAges);
        ageView.setAdapter(mAgeAdapter);

        //init sex menu
        final ListView sexView = new ListView(getContext());
        sexView.setDividerHeight(0);
        mSexAdapter = new ListDropDownAdapter(getContext(), mSexs);
        sexView.setAdapter(mSexAdapter);

        //init constellation
        final View constellationView = getLayoutInflater().inflate(R.layout.layout_drop_down_custom, null);
        GridView constellation = constellationView.findViewById(R.id.constellation);
        mConstellationAdapter = new ConstellationAdapter(getContext(), mConstellations);
        constellation.setAdapter(mConstellationAdapter);
        constellationView.findViewById(R.id.btn_ok).setOnClickListener(v -> {
            mDropDownMenu.setTabMenuText(mConstellationAdapter.getSelectPosition() <= 0 ? mHeaders[3] : mConstellationAdapter.getSelectItem());
            mDropDownMenu.closeMenu();
        });

        //init mPopupViews
        mPopupViews.add(cityView);
        mPopupViews.add(ageView);
        mPopupViews.add(sexView);
        mPopupViews.add(constellationView);

        //add item click event
        cityView.setOnItemClickListener((parent, view, position, id) -> {
            mCityAdapter.setSelectPosition(position);
            mDropDownMenu.setTabMenuText(position == 0 ? mHeaders[0] : mCitys[position]);
            mDropDownMenu.closeMenu();
        });

        ageView.setOnItemClickListener((parent, view, position, id) -> {
            mAgeAdapter.setSelectPosition(position);
            mDropDownMenu.setTabMenuText(position == 0 ? mHeaders[1] : mAges[position]);
            mDropDownMenu.closeMenu();
        });

        sexView.setOnItemClickListener((parent, view, position, id) -> {
            mSexAdapter.setSelectPosition(position);
            mDropDownMenu.setTabMenuText(position == 0 ? mHeaders[2] : mSexs[position]);
            mDropDownMenu.closeMenu();
        });

        constellation.setOnItemClickListener((parent, view, position, id) -> mConstellationAdapter.setSelectPosition(position));

        //init context view
        TextView contentView = new TextView(getContext());
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contentView.setText("内容显示区域");
        contentView.setGravity(Gravity.CENTER);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        //init dropdownview
        mDropDownMenu.setDropDownMenu(mHeaders, mPopupViews, contentView);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            handleBackPressed();
        }
        return true;
    }

    private void handleBackPressed() {
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
        } else {
            popToBack();
        }
    }

}
