package com.yun.springbootinit.constant;

import com.yun.springbootinit.model.enums.ClubLevelEnum;
import com.yun.springbootinit.model.enums.MemberAthleteLevelEnum;
import com.yun.springbootinit.model.enums.MemberRefereeLevelEnum;
import com.yun.springbootinit.model.enums.MemberResidenceEnum;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * 通用常量
 *
 * @author chenyun
 * 
 */
public interface CommonConstant {

    /**
     * 升序
     */
    String SORT_ORDER_ASC = "ascend";

    /**
     * 降序
     */
    String SORT_ORDER_DESC = "descend";

    String UNKNOWN = "unknown";

    String END = "end";

    String START = "start";

    String MALE = "男";

    String FEMALE = "女";

    String TRUE = "是";

    String FALSE = "否";

    String EXCEL_FILE_SUFFIX = ".xlsx";

    interface templateConstant {

        String TEMPLATE_NAME = "陈阿三";

        String TEMPLATE_GENDER = "男";

        String TEMPLATE_BRITH_DATE = "2000-06-21";

        String TEMPLATE_PHONE = "17306415325";

        String TEMPLATE_NATION = "汉族";

        String TEMPLATE_ORIGIN_ADDRESS = "浙江苍南";

        String TEMPLATE_HOME_ADDRESS = "某镇";

        String TEMPLATE_WORK_UNIT = "某公司";

        String TEMPLATE_OCCUPATION = "后端开发工程师";

        String TEMPLATE_POLITICAL_PARTY = "中共党员";

        String TEMPLATE_CLUB_DUTY = "成员";

        String TEMPLATE_IS_CIVIL_SERVANT = TRUE;

        String TEMPLATE_IS_CADRE = TRUE;

        String TEMPLATE_IS_VETERAN = TRUE;

        String TEMPLATE_ATHLETE_LEVEL = MemberAthleteLevelEnum.COUNTY.getText();

        String TEMPLATE_REFEREE_LEVEL = MemberRefereeLevelEnum.SECOND.getText();

        String TEMPLATE_HONOUR_INFO = "2021年xx杯羽毛球赛冠军";

        Double TEMPLATE_HEIGHT = 175.0;

        Double TEMPLATE_WEIGHT = 75.0;

        String TEMPLATE_UNIFORM_SIZE = "XL";

        String TEMPLATE_RESIDENCE_AREA = MemberResidenceEnum.CANGNAN.getText();

        String TEMPLATE_CURRENT_CLUB_NAME = "苍南羽毛球俱乐部";

        String TEMPLATE_CURRENT_LEVEL = ClubLevelEnum.B_LEVEL.getText();

        String TEMPLATE_ID_NUMBER = "330327XXXXXX";

        String TEMPLATE_BANK_ACCOUNT = "XXXXXX";

        String TEMPLATE_BANK_NAME = "中国银行";

        String TEMPLATE_BANK_BRANCH = "苍南分行";
    }

}
