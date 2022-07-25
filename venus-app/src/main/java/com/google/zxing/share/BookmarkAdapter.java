package com.google.zxing.share;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.emis.venus.R;

import java.util.List;

/**
 * 设计用于从游标获取书签的自定义适配器。在蜂巢之前我们使用
 * SimpleCursorAdapter，但是它假设存在一个_id列，并且书签模式是
 * 重写HC没有一个。这导致应用程序崩溃，因此有了这个新类
 * 向前和向后兼容。
 */
final class BookmarkAdapter extends BaseAdapter {

  private final Context context;
  private final List<String[]> titleURLs;

  BookmarkAdapter(Context context, List<String[]> titleURLs) {
    this.context = context;
    this.titleURLs = titleURLs;
  }

  @Override
  public int getCount() {
    return titleURLs.size();
  }

  @Override
  public Object getItem(int index) {
    return titleURLs.get(index);
  }

  @Override
  public long getItemId(int index) {
    return index;
  }

  @Override
  public View getView(int index, View view, ViewGroup viewGroup) {
    View layout;
    if (view instanceof LinearLayout) {
      layout = view;
    } else {
      LayoutInflater factory = LayoutInflater.from(context);
      layout = factory.inflate(R.layout.bookmark_picker_list_item, viewGroup, false);
    }
    String[] titleURL = titleURLs.get(index);
    ((TextView) layout.findViewById(R.id.bookmark_title)).setText(titleURL[0]);
    ((TextView) layout.findViewById(R.id.bookmark_url)).setText(titleURL[1]);
    return layout;
  }
}
