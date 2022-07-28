
package com.basic.code.fragment.components.tabbar;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.basic.code.R;

public class TestPageFragment extends Fragment {

    String content;

    public static TestPageFragment newInstance(String content) {
        return new TestPageFragment().setContent(content);
    }

    public TestPageFragment setContent(String content) {
        this.content = content;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(getContext());
        textView.setTextAppearance(getContext(), R.style.TextStyle_Content_Match);
        textView.setGravity(Gravity.CENTER);
        textView.setText("这个是" + content + "页面的内容");
        return textView;
    }
}
