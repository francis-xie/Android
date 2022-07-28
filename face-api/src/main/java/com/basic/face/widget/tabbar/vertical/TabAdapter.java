
package com.basic.face.widget.tabbar.vertical;


/**
 * Tab适配器
 */
public interface TabAdapter {
    int getCount();

    TabView.TabBadge getBadge(int position);

    TabView.TabIcon getIcon(int position);

    TabView.TabTitle getTitle(int position);

    int getBackground(int position);
}
