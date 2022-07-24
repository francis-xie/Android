
package com.basic.code.fragment.components.refresh.sample.selection;

import com.basic.tools.common.StringUtils;

/**

 * @since 2020/9/2 9:34 PM
 */
public class SelectionItem {

    public String subjectTitle;
    public String selectionTitle1;
    public String selectionTitle2;


    public String subjectName;
    /**
     * 默认为-1，都没有选中
     */
    public int selection = -1;


    /**
     * 构建标题
     *
     * @param subjectTitle    项目标题
     * @param selectionTitle1 选项1
     * @param selectionTitle2 选项2
     */
    public SelectionItem(String subjectTitle, String selectionTitle1, String selectionTitle2) {
        this.subjectTitle = subjectTitle;
        this.selectionTitle1 = selectionTitle1;
        this.selectionTitle2 = selectionTitle2;
    }


    /**
     * 构建选择项目
     *
     * @param subjectName 项目名称
     * @param selection   选择
     */
    public SelectionItem(String subjectName, int selection) {
        this.subjectName = subjectName;
        this.selection = selection;
    }

    /**
     * 构建选择项目
     *
     * @param subjectName 项目名称
     */
    public SelectionItem(String subjectName) {
        this.subjectName = subjectName;
    }


    public boolean isSelection1() {
        return selection == 0;
    }

    public boolean isSelection2() {
        return selection == 1;
    }


    public SelectionItem setSelection(int selection) {
        this.selection = selection;
        return this;
    }

    public boolean isTitle() {
        return !StringUtils.isEmpty(subjectTitle);
    }

    @Override
    public String toString() {
        return "SelectionItem{" +
                ", subjectName='" + subjectName + '\'' +
                ", selection=" + selection +
                '}';
    }
}
