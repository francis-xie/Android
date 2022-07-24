
package com.basic.code.fragment.components.refresh.sample;

import com.basic.page.annotation.Page;
import com.basic.code.base.ComponentContainerFragment;
import com.basic.code.fragment.components.refresh.sample.diffutil.DiffUtilRefreshFragment;
import com.basic.code.fragment.components.refresh.sample.edit.NewsListEditFragment;
import com.basic.code.fragment.components.refresh.sample.selection.ListSelectionFragment;
import com.basic.code.fragment.components.refresh.sample.sortedlist.SortedListRefreshFragment;

/**

 * @since 2020/9/2 8:46 PM
 */
@Page(name = "列表使用案例集合")
public class SampleListFragment extends ComponentContainerFragment {
    @Override
    protected Class[] getPagesClasses() {
        return new Class[]{
                DiffUtilRefreshFragment.class,
                SortedListRefreshFragment.class,
                ListSelectionFragment.class,
                NewsListEditFragment.class
        };
    }
}
