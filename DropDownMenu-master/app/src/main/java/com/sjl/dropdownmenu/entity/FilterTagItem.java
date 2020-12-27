package com.sjl.dropdownmenu.entity;

import java.util.List;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename FilterTagItem.java
 * @time 2019/2/14 11:08
 * @copyright(C) 2019 song
 */
public class FilterTagItem {
    public int groupIndex;//分组索引
    public String groupTitle;//分组标题
    public List<FilterTag> filterTags;//每组的标签数据



    public static class FilterTag {
        public int value;
        public String text;

        public FilterTag(int value, String text) {
            this.value = value;
            this.text = text;
        }
    }
}
