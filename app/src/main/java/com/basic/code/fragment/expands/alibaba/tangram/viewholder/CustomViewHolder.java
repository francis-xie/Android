
package com.basic.code.fragment.expands.alibaba.tangram.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tmall.wireless.tangram.structure.viewcreator.ViewHolderCreator;

/**
 * 自定义ViewHolder
 *

 * @since 2020/4/10 12:37 AM
 */
public class CustomViewHolder extends ViewHolderCreator.ViewHolder {

    public TextView textView;

    public CustomViewHolder(Context context) {
        super(context);
    }

    @Override
    protected void onRootViewCreated(View root) {
        textView = (TextView) root;
    }
}
