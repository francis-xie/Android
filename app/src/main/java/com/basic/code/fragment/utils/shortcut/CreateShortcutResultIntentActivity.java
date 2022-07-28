
package com.basic.code.fragment.utils.shortcut;

import android.os.Bundle;

import com.basic.page.core.PageOption;
import com.basic.page.enums.CoreAnim;
import com.basic.code.base.BaseActivity;
import com.basic.code.fragment.utils.ShortcutUtilsFragment;
import com.basic.code.utils.Utils;

public class CreateShortcutResultIntentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PageOption.to(ShortcutUtilsFragment.class)
                .setAnim(CoreAnim.none)
                .open(this);
    }

    @Override
    public void onBackPressed() {
        if (this.getSupportFragmentManager().getBackStackEntryCount() == 1) {
            Utils.syncMainPageStatus();
        }
        super.onBackPressed();
    }
}
