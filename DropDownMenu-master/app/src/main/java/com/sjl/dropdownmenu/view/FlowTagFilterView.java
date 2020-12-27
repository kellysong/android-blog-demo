package com.sjl.dropdownmenu.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baiiu.filter.interfaces.OnFilterDoneListener;
import com.baiiu.filter.util.UIUtil;
import com.sjl.dropdownmenu.R;
import com.sjl.dropdownmenu.entity.FilterTagItem;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zhy.view.flowlayout.TagView;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 流式标签过滤view
 *
 * @author Kelly
 * @version 1.0.0
 * @filename FilterTagItem.java
 * @time 2019/2/13 15:12
 * @copyright(C) 2019 song
 */
public class FlowTagFilterView extends LinearLayout {

    @BindView(R.id.ll_container)
    LinearLayout mContainer;
    private LayoutInflater mInflater;
    private List<FilterTagItem> filterTagItemList;
    private OnFilterDoneListener mOnFilterDoneListener;
    private int position;
    private String positionTitle;

    public FlowTagFilterView(Context context) {
        this(context, null);
    }

    public FlowTagFilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FlowTagFilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlowTagFilterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setBackgroundColor(Color.WHITE);
        inflate(context, R.layout.filter4_request, this);
        ButterKnife.bind(this, this);
        mInflater = LayoutInflater.from(context);

    }

    public FlowTagFilterView setGroupData(List<FilterTagItem> filterTagItemList) {
        this.filterTagItemList = filterTagItemList;
        return this;
    }


    public FlowTagFilterView setOnFilterDoneListener(OnFilterDoneListener listener, int position, String positionTitle) {
        this.mOnFilterDoneListener = listener;
        this.position = position;
        this.positionTitle = positionTitle;
        return this;
    }


    public FlowTagFilterView build() {
        if (filterTagItemList != null && filterTagItemList.size() > 0) {
            for (int i = 0; i < filterTagItemList.size(); i++) {
                FilterTagItem tagItem = filterTagItemList.get(i);
                TextView textView = new TextView(getContext());
                textView.setText(tagItem.groupTitle);
                textView.setTag(tagItem.groupIndex);
                List<FilterTagItem.FilterTag> filterTags = tagItem.filterTags;
                final TagFlowLayout tagFlowLayout = new TagFlowLayout(getContext());
                int padding = UIUtil.dp(getContext(), 10);
                tagFlowLayout.setPadding(padding, padding, padding, padding);
                ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                tagFlowLayout.setAdapter(new TagAdapter<FilterTagItem.FilterTag>(filterTags) {
                    @Override
                    public View getView(FlowLayout parent, int position, FilterTagItem.FilterTag filterTag) {
                        TextView tv = (TextView) mInflater.inflate(R.layout.flow_tag_tv, tagFlowLayout, false);
                        tv.setText(filterTag.text);
                        tv.setTag(filterTag.value);
                        return tv;
                    }

                });
                tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        if (position == 0) {//点击全部标签，其它标签都不选中
                            tagFlowLayout.setSelect(position);

                        } else {//点击其它标签 ，全部标签不选中
                            tagFlowLayout.setUnSelect((TagView) tagFlowLayout.getChildAt(0), 0);
                        }
                        return true;
                    }
                });
                //默认选项
                tagFlowLayout.setSelect(0);
                mContainer.addView(textView);
                mContainer.addView(tagFlowLayout, params);
            }
        }
        return this;
    }


    @OnClick(R.id.bt_confirm)
    public void clickDone() {
        onFilterDone();
    }


    @OnClick(R.id.bt_reset)
    public void clickReset() {
        int childCount = mContainer.getChildCount() / 2;
        for (int i = 0; i < childCount; i++) {
            View childAt = mContainer.getChildAt(2 * i + 1);
            if (childAt instanceof TagFlowLayout) {
                TagFlowLayout tagFlowLayout = (TagFlowLayout) childAt;
                tagFlowLayout.setSelect(0);
            }
        }
        onFilterDone();
    }

    /**
     * 筛选完成回调
     */
    private void onFilterDone() {
        if (mOnFilterDoneListener != null) {
            int childCount = mContainer.getChildCount() / 2;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < childCount; i++) {
                View childAt = mContainer.getChildAt(2 * i + 1);
                if (childAt instanceof TagFlowLayout) { //获得所有选中的pos集合
                    TagFlowLayout tagFlowLayout = (TagFlowLayout) childAt;
                    Set<Integer> selectedList = tagFlowLayout.getSelectedList();
                    FilterTagItem filterTagItem = filterTagItemList.get(i);
                    sb.append(filterTagItem.groupTitle + "：" + selectedList.toString());
                }
            }
            mOnFilterDoneListener.onFilterDone(position, positionTitle, sb.toString());
        }
    }


}
