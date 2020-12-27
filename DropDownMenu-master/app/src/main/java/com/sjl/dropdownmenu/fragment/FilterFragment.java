package com.sjl.dropdownmenu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.baiiu.filter.DropDownMenu;
import com.baiiu.filter.interfaces.OnFilterDoneListener;
import com.sjl.dropdownmenu.DropMenuAdapter;
import com.sjl.dropdownmenu.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename FilterFragment.java
 * @time 2019/2/18 15:39
 * @copyright(C) 2019 song
 */
public class FilterFragment extends Fragment implements OnFilterDoneListener {
    @BindView(R.id.dropDownMenu)
    DropDownMenu dropDownMenu;
    @BindView(R.id.mFilterContentView)
    FrameLayout mFilterContentView;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initFilterDropDownView();
    }

    private void initFilterDropDownView() {
        String[] titleList = new String[]{"推荐", "深圳", "公司", "要求"};
        dropDownMenu.setMenuAdapter(new DropMenuAdapter(getContext(), titleList, this));
    }

    @Override
    public void onFilterDone(int position, String positionTitle, String urlValue) {

        dropDownMenu.close();

    }

}
