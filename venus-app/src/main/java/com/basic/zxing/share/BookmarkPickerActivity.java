package com.basic.zxing.share;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import com.basic.zxing.util.Intents;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个类是唯一需要的，因为我不能成功地发送一个ACTION_PICK意图
 * com.android.browser.BrowserBookmarksPage. 如果这在未来开始起作用，它可以消失。
 */
public final class BookmarkPickerActivity extends ListActivity {

  private static final String TAG = BookmarkPickerActivity.class.getSimpleName();

  private static final String[] BOOKMARK_PROJECTION = {
    "title", // Browser.BookmarkColumns.TITLE
    "url", // Browser.BookmarkColumns.URL
  };
  // Copied from android.provider.Browser.BOOKMARKS_URI:
  private static final Uri BOOKMARKS_URI = Uri.parse("content://browser/bookmarks");

  private static final String BOOKMARK_SELECTION = "bookmark = 1 AND url IS NOT NULL";

  private final List<String[]> titleURLs = new ArrayList<>();

  @Override
  protected void onResume() {
    super.onResume();
    titleURLs.clear();
    try (Cursor cursor = getContentResolver().query(BOOKMARKS_URI, BOOKMARK_PROJECTION,
      BOOKMARK_SELECTION, null, null)) {
      if (cursor == null) {
        Log.w(TAG, "No cursor returned for bookmark query");
        finish();
        return;
      }
      while (cursor.moveToNext()) {
        titleURLs.add(new String[]{cursor.getString(0), cursor.getString(1)});
      }
    }
    setListAdapter(new BookmarkAdapter(this, titleURLs));
  }


  @Override
  protected void onListItemClick(ListView l, View view, int position, long id) {
    String[] titleURL = titleURLs.get(position);
    Intent intent = new Intent();
    intent.addFlags(Intents.FLAG_NEW_DOC);
    intent.putExtra("title", titleURL[0]); // Browser.BookmarkColumns.TITLE
    intent.putExtra("url", titleURL[1]); // Browser.BookmarkColumns.URL
    setResult(RESULT_OK, intent);
    finish();
  }
}
