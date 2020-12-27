package com.sjl.dropdownmenu;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.baiiu.filter.adapter.MenuAdapter;
import com.baiiu.filter.adapter.SimpleTextAdapter;
import com.baiiu.filter.interfaces.OnFilterDoneListener;
import com.baiiu.filter.interfaces.OnFilterItemClickListener;
import com.baiiu.filter.typeview.DoubleListView;
import com.baiiu.filter.typeview.SingleListView;
import com.baiiu.filter.util.UIUtil;
import com.baiiu.filter.view.FilterCheckedTextView;
import com.sjl.dropdownmenu.entity.FilterTagItem;
import com.sjl.dropdownmenu.entity.FilterType;
import com.sjl.dropdownmenu.entity.FilterData;
import com.sjl.dropdownmenu.view.FlowTagFilterView;

import java.util.List;


public class DropMenuAdapter implements MenuAdapter {
    private final Context mContext;
    private OnFilterDoneListener onFilterDoneListener;
    private String[] titles;

    public DropMenuAdapter(Context context, String[] titles, OnFilterDoneListener onFilterDoneListener) {
        this.mContext = context;
        this.titles = titles;
        this.onFilterDoneListener = onFilterDoneListener;
    }

    @Override
    public int getMenuCount() {
        return titles.length;
    }

    @Override
    public String getMenuTitle(int position) {
        return titles[position];
    }

    @Override
    public int getBottomMargin(int position) {
//        if (position == 3) {
//            return 0;
//        }
        return UIUtil.dp(mContext, 140);
    }

    @Override
    public View getView(int position, FrameLayout parentContainer) {
        View view = parentContainer.getChildAt(position);

        switch (position) {
            case 0:
                view = createRecommendView(position);
                break;
            case 1:
                view = createAddressView(position);
                break;
            case 2:
                view = createCompanyView(position);
                break;
            case 3:
                view = createRequestView(position);
                break;
        }

        return view;
    }


    /**
     * 推荐
     *
     * @param position
     * @return
     */
    private View createRecommendView(int position) {
        return createSingleListView(position);
    }

    /**
     * 地点
     *
     * @param position
     * @return
     */
    private View createAddressView(int position) {
        return createDoubleListView(position);
    }

    /**
     * 公司
     *
     * @param position
     * @return
     */
    private View createCompanyView(int position) {
        List<FilterTagItem> requestData = FilterData.getCompanyData();

        return new FlowTagFilterView(mContext)
                .setGroupData(requestData)
                .setOnFilterDoneListener(onFilterDoneListener, position, titles[position])
                .build();
    }

    /**
     * 要求
     *
     * @param position
     * @return
     */
    private View createRequestView(int position) {
        List<FilterTagItem> requestData = FilterData.getRequestData();

        return new FlowTagFilterView(mContext)
                .setGroupData(requestData)
                .setOnFilterDoneListener(onFilterDoneListener, position, titles[position])
                .build();
    }

    /**
     * 单列ListView
     *
     * @param position
     * @return
     */
    private View createSingleListView(final int position) {
        SingleListView<String> singleListView = new SingleListView<String>(mContext)
                .adapter(new SimpleTextAdapter<String>(null, mContext) {
                    @Override
                    public String provideText(String string) {
                        return string;
                    }

                    @Override
                    protected void initCheckedTextView(FilterCheckedTextView checkedTextView) {
                        int dp = UIUtil.dp(mContext, 15);
                        checkedTextView.setPadding(dp, dp, 0, dp);
                    }
                })
                .onItemClick(new OnFilterItemClickListener<String>() {
                    @Override
                    public void onItemClick(String item) {

                        if (onFilterDoneListener != null) {
                            onFilterDoneListener.onFilterDone(position, titles[position], item);
                        }
                    }
                });
        List<String> list = FilterData.getRecommendData();

        //初始化数据
        singleListView.setList(list, -1);//默认不选中
        return singleListView;
    }


    /**
     * 双列ListView
     *
     * @param position
     * @return
     */
    private View createDoubleListView(final int position) {
        DoubleListView<FilterType, String> comTypeDoubleListView = new DoubleListView<FilterType, String>(mContext)
                .leftAdapter(new SimpleTextAdapter<FilterType>(null, mContext) {
                    @Override
                    public String provideText(FilterType filterType) {
                        return filterType.desc;
                    }

                    @Override
                    protected void initCheckedTextView(FilterCheckedTextView checkedTextView) {
                        checkedTextView.setPadding(UIUtil.dp(mContext, 44), UIUtil.dp(mContext, 15), 0, UIUtil.dp(mContext, 15));
                    }
                })
                .rightAdapter(new SimpleTextAdapter<String>(null, mContext) {
                    @Override
                    public String provideText(String s) {
                        return s;
                    }

                    @Override
                    protected void initCheckedTextView(FilterCheckedTextView checkedTextView) {
                        checkedTextView.setPadding(UIUtil.dp(mContext, 30), UIUtil.dp(mContext, 15), 0, UIUtil.dp(mContext, 15));
                        checkedTextView.setBackgroundResource(android.R.color.white);
                    }
                })
                .onLeftItemClickListener(new DoubleListView.OnLeftItemClickListener<FilterType, String>() {
                    @Override
                    public List<String> provideRightList(FilterType item, int position) {
                        List<String> child = item.child;
                        Log.i("SIMPLE_LOGGER", "左边：" + item.toString());
                        return child;
                    }
                })
                .onRightItemClickListener(new DoubleListView.OnRightItemClickListener<FilterType, String>() {
                    @Override
                    public void onRightItemClick(FilterType item, String string) {
                        Log.i("SIMPLE_LOGGER", "右边：" + string);
                        if (onFilterDoneListener != null) {
                            onFilterDoneListener.onFilterDone(position, titles[position], item.desc + ":" + string);
                        }
                    }
                });

        List<FilterType> list = FilterData.getAddressData();

        //初始化选中.
        comTypeDoubleListView.setLeftList(list, 1);
        comTypeDoubleListView.setRightList(list.get(1).child, -1);
        comTypeDoubleListView.getLeftListView().setBackgroundColor(mContext.getResources().getColor(R.color.b_c_fafafa));

        return comTypeDoubleListView;
    }


}
