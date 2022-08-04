
package com.basic.face.widget.layout.linkage.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.basic.face.widget.layout.linkage.ChildLinkageEvent;
import com.basic.face.widget.layout.linkage.ILinkageScroll;
import com.basic.face.widget.layout.linkage.LinkageScrollHandler;
import com.basic.face.widget.layout.linkage.LinkageScrollHandlerAdapter;

/**
 * 置于联动容器的TextView
 */
public class LinkageTextView extends AppCompatTextView implements ILinkageScroll {

    public LinkageTextView(Context context) {
        this(context, null);
    }

    public LinkageTextView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LinkageTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setChildLinkageEvent(ChildLinkageEvent event) {

    }

    @Override
    public LinkageScrollHandler provideScrollHandler() {
        return new LinkageScrollHandlerAdapter() {
            @Override
            public boolean isScrollable() {
                return false;
            }

            @Override
            public int getVerticalScrollExtent() {
                return getHeight();
            }

            @Override
            public int getVerticalScrollOffset() {
                return 0;
            }

            @Override
            public int getVerticalScrollRange() {
                return getHeight();
            }
        };
    }
}
