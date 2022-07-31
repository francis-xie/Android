package com.basic.code.fragment.utils;

import com.basic.code.R;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.log.CrashFragment;
import com.basic.code.fragment.log.LogFragment;
import com.basic.page.annotation.Page;

@Page(name = "LogUtils", extra = R.drawable.ic_util_view)
public class LogUtilsFragment extends ComponentContainerFragment {

  /**
   * 获取页面的类集合[使用@Page注解进行注册的页面]
   *
   * @return
   */
  @Override
  protected Class[] getPagesClasses() {
    return new Class[]{
      LogFragment.class,
      CrashFragment.class
    };
  }
/*
  @Override
  protected TitleBar initTitleBar() {
    return super.initTitleBar().setLeftClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ClickUtils.exitBy2Click();
      }
    });
  }


  *//**
   * 菜单、返回键响应
   *//*
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      ClickUtils.exitBy2Click();
    }
    return true;
  }*/

}
