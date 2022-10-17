package com.example.newbiechen.ireader.model.bean.packages;

import com.example.newbiechen.ireader.model.bean.BaseBean;
import com.example.newbiechen.ireader.model.bean.BillboardBean;

import java.util.List;

/**
 * Created by newbiechen on 17-4-23.
 */

public class BillboardPackage extends BaseBean {
    /**
     * male : [{"_id":"54d42d92321052167dfb75e3","title":"追书最热榜 Top100","cover":"/ranking-cover/142319144267827","collapse":false,"monthRank":"564d820bc319238a644fb408","totalRank":"564d8494fe996c25652644d2"},{"_id":"564547c694f1c6a144ec979b","title":"读者留存率 Top100","cover":"/ranking-cover/144738093413066","collapse":false,"monthRank":"564d898f59fd983667a5e3fa","totalRank":"564d8a004a15bb8369d9e28d"},{"_id":"564eb878efe5b8e745508fde","title":"追书完结榜 Top100","cover":"/ranking-cover/144799960841612","collapse":false,"monthRank":"564eb12c3edb8b45511139ff","totalRank":"564eea0b731ade4d6c509493"},{"_id":"57eb86f0ef9e5a8f20543d7d","title":"包月排行榜","cover":"/ranking-cover/14750532964058","collapse":false,"totalRank":"5848d4a4602c289505581f6f","monthRank":"57eb9283f031bfcc219389af"},{"_id":"54d42e72d9de23382e6877fb","title":"本周潜力榜","cover":"/ranking-cover/142319166399949","collapse":false,"monthRank":"564eee3ea82e3ada6f14b195","totalRank":"564eeeabed24953671f2a577"},{"_id":"582ed5fc93b7e855163e707d","title":"圣诞热搜榜","cover":"/ranking-cover/147946444450686","collapse":true},{"_id":"564ef4f985ed965d0280c9c2","title":"百度热搜榜","cover":"/ranking-cover/142319217152210","collapse":true},{"_id":"564d8003aca44f4f61850fcd","title":"掌阅热销榜","cover":"/ranking-cover/142319217152210","collapse":true},{"_id":"564d80457408cfcd63ae2dd0","title":"书旗热搜榜","cover":"/ranking-cover/142319217152210","collapse":true},{"_id":"54d430e9a8cb149d07282496","title":"17K 鲜花榜","cover":"/ranking-cover/142319217152210","collapse":true},{"_id":"54d4306c321052167dfb75e4","title":"起点月票榜","cover":"/ranking-cover/142319217152210","collapse":true},{"_id":"54d430962c12d3740e4a3ed2","title":"纵横月票榜","cover":"/ranking-cover/142319217152210","collapse":true},{"_id":"54d4312d5f3c22ae137255a1","title":"和阅读原创榜","cover":"/ranking-cover/142319217152210","collapse":true},{"_id":"5732aac02dbb268b5f037fc4","title":"逐浪点击榜","cover":"/ranking-cover/146293830220772","collapse":true}]
     * female : [{"_id":"54d43437d47d13ff21cad58b","title":"追书最热榜 Top100","cover":"/ranking-cover/142319314350435","collapse":false,"monthRank":"564d853484665f97662d0810","totalRank":"564d85b6dd2bd1ec660ea8e2"},{"_id":"5645482405b052fe70aeb1b5","title":"读者留存率 Top100","cover":"/ranking-cover/144738102858782","collapse":false,"monthRank":"564d8b6b36d10ccd6951195d","totalRank":"564d8c37752bcca16a976168"},{"_id":"564eb8a9cf77e9b25056162d","title":"追书完结榜 Top100","cover":"/ranking-cover/144799965747841","collapse":false,"monthRank":"564ee8ec146f8f1739777740","totalRank":"564eeae6c3345baa6bf06e38"},{"_id":"57eb959df60eb7e21fb3a8b7","title":"包月排行榜","cover":"/ranking-cover/147505705336267","collapse":false,"monthRank":"57eb98afcf7ddb81588947b4","totalRank":"57eb98fe1ae40b985292051e"},{"_id":"54d43709fd6ec9ae04184aa5","title":"本周潜力榜","cover":"/ranking-cover/142319383473238","collapse":false,"monthRank":"564eee77e3a44c9f0e5fd7ae","totalRank":"564eeeca5e6ba6ae074f10ec"},{"_id":"582fb5c412a401a20ea50275","title":"圣诞热搜榜","cover":"/ranking-cover/14795217326220","collapse":true},{"_id":"564d80d0e8c613016446c5aa","title":"掌阅热销榜","cover":"/ranking-cover/142319217152210","collapse":true},{"_id":"564d81151109835664770ad7","title":"书旗热搜榜","cover":"/ranking-cover/142319217152210","collapse":true},{"_id":"550b841715db45cd4b022107","title":"17K订阅榜","cover":"/ranking-cover/142319217152210","collapse":true},{"_id":"550b836229cd462830ff4d1d","title":"起点粉红票榜","cover":"/ranking-cover/142319217152210","collapse":true},{"_id":"550b8397de12381038ad8c0b","title":"潇湘月票榜","cover":"/ranking-cover/142319217152210","collapse":true}]
     * ok : true
     */
    private List<BillboardBean> male;
    private List<BillboardBean> female;

    public List<BillboardBean> getMale() {
        return male;
    }

    public void setMale(List<BillboardBean> male) {
        this.male = male;
    }

    public List<BillboardBean> getFemale() {
        return female;
    }

    public void setFemale(List<BillboardBean> female) {
        this.female = female;
    }
}
