package com.sjl.dropdownmenu.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename FilterData.java
 * @time 2019/2/14 11:26
 * @copyright(C) 2019 song
 */
public class FilterData {
    /**
     * 公司数据
     *
     * @return
     */
    public static List<FilterTagItem> getCompanyData() {
        List<FilterTagItem> filterTagItemList = new ArrayList<>();
        FilterTagItem tagStage = new FilterTagItem();
        tagStage.groupIndex = 0;
        tagStage.groupTitle = "融资阶段";
        List<FilterTagItem.FilterTag> stage = new ArrayList<>();
        stage.add(new FilterTagItem.FilterTag(0, "全部"));
        stage.add(new FilterTagItem.FilterTag(1, "未融资"));
        stage.add(new FilterTagItem.FilterTag(2, "天使轮"));
        stage.add(new FilterTagItem.FilterTag(3, "A轮"));
        stage.add(new FilterTagItem.FilterTag(4, "B轮"));
        stage.add(new FilterTagItem.FilterTag(5, "C轮"));
        stage.add(new FilterTagItem.FilterTag(6, "D轮及以上"));
        stage.add(new FilterTagItem.FilterTag(7, "已上市"));
        stage.add(new FilterTagItem.FilterTag(8, "不需要融资"));
        tagStage.filterTags = stage;

        FilterTagItem tagPerson = new FilterTagItem();
        tagPerson.groupIndex = 1;
        tagPerson.groupTitle = "人员规模";
        List<FilterTagItem.FilterTag> person = new ArrayList<>();
        person.add(new FilterTagItem.FilterTag(0, "全部"));
        person.add(new FilterTagItem.FilterTag(1, "0-20人"));
        person.add(new FilterTagItem.FilterTag(2, "20-99人"));
        person.add(new FilterTagItem.FilterTag(3, "100-499人"));
        person.add(new FilterTagItem.FilterTag(4, "500-999人"));
        person.add(new FilterTagItem.FilterTag(5, "1000-9999人"));
        person.add(new FilterTagItem.FilterTag(6, "10000人以上"));
        tagPerson.filterTags = person;


        FilterTagItem tagTmt = new FilterTagItem();
        tagTmt.groupIndex = 2;
        tagTmt.groupTitle = "行业";
        List<FilterTagItem.FilterTag> tmt = new ArrayList<>();
        tmt.add(new FilterTagItem.FilterTag(0, "全部"));
        tmt.add(new FilterTagItem.FilterTag(1, "电子商务"));
        tmt.add(new FilterTagItem.FilterTag(2, "游戏"));
        tmt.add(new FilterTagItem.FilterTag(3, "媒体"));
        tmt.add(new FilterTagItem.FilterTag(4, "广告营销"));
        tmt.add(new FilterTagItem.FilterTag(5, "数据服务"));
        tmt.add(new FilterTagItem.FilterTag(6, "医疗健康"));
        tmt.add(new FilterTagItem.FilterTag(7, "生活服务"));
        tmt.add(new FilterTagItem.FilterTag(8, "O2O"));
        tmt.add(new FilterTagItem.FilterTag(9, "旅游"));
        tmt.add(new FilterTagItem.FilterTag(10, "分类信息"));
        tmt.add(new FilterTagItem.FilterTag(11, "音乐/视频/阅读"));
        tmt.add(new FilterTagItem.FilterTag(12, "在线教育"));
        tmt.add(new FilterTagItem.FilterTag(13, "社交网络"));
        tmt.add(new FilterTagItem.FilterTag(14, "人力资源服务"));
        tmt.add(new FilterTagItem.FilterTag(15, "企业服务"));
        tmt.add(new FilterTagItem.FilterTag(16, "信息安全"));
        tmt.add(new FilterTagItem.FilterTag(17, "智能硬件"));
        tmt.add(new FilterTagItem.FilterTag(18, "移动互联网"));
        tmt.add(new FilterTagItem.FilterTag(19, "互联网"));
        tmt.add(new FilterTagItem.FilterTag(20, "计算机软件"));
        tmt.add(new FilterTagItem.FilterTag(21, "通信/网络设备"));
        tmt.add(new FilterTagItem.FilterTag(22, "广告/公关/会展"));
        tmt.add(new FilterTagItem.FilterTag(23, "互联网金融"));
        tmt.add(new FilterTagItem.FilterTag(24, "物料/仓储"));
        tmt.add(new FilterTagItem.FilterTag(25, "贸易/进出口"));
        tmt.add(new FilterTagItem.FilterTag(26, "咨询"));
        tmt.add(new FilterTagItem.FilterTag(27, "工程施工"));
        tmt.add(new FilterTagItem.FilterTag(28, "汽车生产"));
        tmt.add(new FilterTagItem.FilterTag(29, "其他行业"));
        tagTmt.filterTags = tmt;
        filterTagItemList.add(tagStage);
        filterTagItemList.add(tagPerson);
        filterTagItemList.add(tagTmt);

        return filterTagItemList;
    }

    /**
     * 要求数据
     *
     * @return
     */
    public static List<FilterTagItem> getRequestData() {
        List<FilterTagItem> filterTagItemList = new ArrayList<>();
        FilterTagItem tagEducation = new FilterTagItem();
        tagEducation.groupIndex = 0;
        tagEducation.groupTitle = "学历";
        List<FilterTagItem.FilterTag> education = new ArrayList<>();
        education.add(new FilterTagItem.FilterTag(0, "全部"));
        education.add(new FilterTagItem.FilterTag(1, "初中及以下"));
        education.add(new FilterTagItem.FilterTag(2, "中专/中技"));
        education.add(new FilterTagItem.FilterTag(3, "高中"));
        education.add(new FilterTagItem.FilterTag(4, "大专"));
        education.add(new FilterTagItem.FilterTag(5, "本科"));
        education.add(new FilterTagItem.FilterTag(6, "硕士"));
        education.add(new FilterTagItem.FilterTag(7, "博士"));
        tagEducation.filterTags = education;

        FilterTagItem tagYear = new FilterTagItem();
        tagYear.groupIndex = 1;
        tagYear.groupTitle = "经验";
        List<FilterTagItem.FilterTag> year = new ArrayList<>();
        year.add(new FilterTagItem.FilterTag(0, "全部"));
        year.add(new FilterTagItem.FilterTag(1, "应届生"));
        year.add(new FilterTagItem.FilterTag(2, "1年以内"));
        year.add(new FilterTagItem.FilterTag(3, "1-3年"));
        year.add(new FilterTagItem.FilterTag(4, "3-5年"));
        year.add(new FilterTagItem.FilterTag(5, "5-10年"));
        year.add(new FilterTagItem.FilterTag(6, "10年以上"));
        tagYear.filterTags = year;


        FilterTagItem tagMoney = new FilterTagItem();
        tagMoney.groupIndex = 2;
        tagMoney.groupTitle = "薪水(单选)";
        List<FilterTagItem.FilterTag> money = new ArrayList<>();
        money.add(new FilterTagItem.FilterTag(0, "全部"));
        money.add(new FilterTagItem.FilterTag(1, "3k以下"));
        money.add(new FilterTagItem.FilterTag(2, "3k-5k"));
        money.add(new FilterTagItem.FilterTag(3, "5-10k"));
        money.add(new FilterTagItem.FilterTag(4, "10k-20k"));
        money.add(new FilterTagItem.FilterTag(5, "20k-50k"));
        money.add(new FilterTagItem.FilterTag(6, "50k以上"));

        tagMoney.filterTags = money;


        filterTagItemList.add(tagEducation);
        filterTagItemList.add(tagYear);

        filterTagItemList.add(tagMoney);

        return filterTagItemList;
    }

    /**
     * 推荐数据
     */
    public static List<String> getRecommendData() {
        List<String> list = new ArrayList<>();
        list.add("推荐");
        list.add("最新");
        return list;
    }

    public static List<FilterType> getAddressData() {
        List<FilterType> list = new ArrayList<>();

        //第一项
        FilterType filterType = new FilterType();
        filterType.desc = "深圳";

        List<String> childListAll = new ArrayList<>();
        childListAll.add("全深圳");
        filterType.child = childListAll;
        list.add(filterType);

        //第二项
        filterType = new FilterType();
        filterType.desc = "南山区";
        List<String> childList = new ArrayList<>();
        childList.add("全南山区");
        childList.add("科技园");
        childList.add("西丽");
        childList.add("南头");
        childList.add("蛇口");
        childList.add("深圳湾");
        childList.add("华侨城");
        childList.add("南油");
        childList.add("前海");
        childList.add("后海");
        childList.add("新安");
        childList.add("白石洲");
        childList.add("大冲");
        childList.add("海上世界");
        childList.add("海王大厦");
        childList.add("桃源村");

        filterType.child = childList;
        list.add(filterType);

        //第三项
        filterType = new FilterType();
        filterType.desc = "福田区";
        childList = new ArrayList<>();
        childList.add("全福田区");
        childList.add("车公庙");
        childList.add("华强北");
        childList.add("梅林");
        childList.add("岗厦");
        childList.add("皇岗");
        childList.add("石厦");
        childList.add("购物公园");
        childList.add("八卦岭");
        childList.add("景田");
        childList.add("香蜜湖");
        childList.add("竹子林");
        childList.add("新洲");
        childList.add("上步");
        childList.add("下沙");
        childList.add("圆岭");
        childList.add("上沙");
        childList.add("华侨城");
        childList.add("益田村");
        childList.add("上梅林");
        childList.add("赤尾");
        childList.add("沙尾");
        childList.add("沙头");
        childList.add("笔架山");
        childList.add("莲花二村");
        childList.add("莲花村北");
        childList.add("振华路");
        childList.add("泥岗");
        childList.add("笋岗");
        childList.add("下梅林");
        childList.add("银湖");
        childList.add("莲花一村");
        childList.add("沙嘴");


        filterType.child = childList;
        list.add(filterType);
        return list;
    }
}
