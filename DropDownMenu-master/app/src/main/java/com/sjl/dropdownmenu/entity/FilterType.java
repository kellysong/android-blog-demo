package com.sjl.dropdownmenu.entity;

import java.util.List;

/**
 * author: baiiu
 * date: on 16/2/19 18:09
 * description:
 */
public class FilterType {
    public String desc;
    public List<String> child;

    @Override
    public String toString() {
        return "FilterType{" +
                "desc='" + desc + '\'' +
                ", child=" + child +
                '}';
    }
}
