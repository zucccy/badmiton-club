package com.yun.springbootinit.model.dto.member;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.yun.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description: TODO
 * @Author: chenyun
 * @Date: 2025/2/25 0:27
 * @version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberQueryRequest extends PageRequest implements Serializable {

    // 会员id
    private Long id;

    private String name;

    private String gender;

    private Integer startAge;

    private Integer endAge;

    private Boolean isCivilServant;

    private Boolean isCadre;

    private Boolean isVeteran;

    private String athleteLevel;

    private String refereeLevel;

    // 人员归属地（分龙港、苍南、平阳、温州市内、浙江省内、省外6个选项）
    private String residenceArea;

    // 当前所属俱乐部id
    private Long currentClubId;

    // 当前组别，0表示丙，1表示乙，2表示甲
    private Integer currentLevel;
}
