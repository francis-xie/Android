
package com.basic.code.fragment.components.image.preview;

import com.basic.face.widget.imageview.nine.NineGridImageView;

import java.util.List;


/**
 * 九宫格信息
 */
public class NineGridInfo {

    private String mContent;

    private List<ImageViewInfo> mImgUrlList;

    private int mShowType;

    private int mSpanType;

    public NineGridInfo() {

    }

    public NineGridInfo(String content, List<ImageViewInfo> imgUrlList) {
       this(content, imgUrlList,  NineGridImageView.STYLE_GRID, NineGridImageView.NOSPAN);
    }

    public NineGridInfo(String content, List<ImageViewInfo> imgUrlList, int showType, int spanType) {
        mContent = content;
        mImgUrlList = imgUrlList;
        mShowType = showType;
        mSpanType = spanType;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public int getShowType() {
        return mShowType;
    }

    public NineGridInfo setShowType(int showType) {
        mShowType = showType;
        return this;
    }

    public List<ImageViewInfo> getImgUrlList() {
        return mImgUrlList;
    }

    public void setImgUrlList(List<ImageViewInfo> imgUrlList) {
        mImgUrlList = imgUrlList;
    }

    public NineGridInfo setSpanType(int spanType) {
        mSpanType = spanType;
        return this;
    }

    public int getSpanType() {
        return mSpanType;
    }
}
