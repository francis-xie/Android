package com.basic.face.widget.spinner.editspinner;

/**
 * 监听输入并过滤
 */
public interface EditSpinnerFilter {
    /**
     * editText输入监听
     *
     * @param keyword 关键字
     * @return 是否找到匹配项
     */
    boolean onFilter(String keyword);
}
