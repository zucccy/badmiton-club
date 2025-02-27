package com.yun.springbootinit.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Description: TODO
 * @Author: chenyun
 * @Date: 2025/2/25 0:28
 * @version: 1.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberVO {

    private Long id;

    private String name;

    // 性别
    private String gender;

    // 年龄
    private Integer age;

    // 手机号码
    private String phone;

    // 民族
    private String nation;

    // 籍贯
    private String originAddress;

    // 家庭住址
    private String homeAddress;

    // 工作单位
    private String workUnit;

    // 职业
    private String occupation;

    // 党派
    private String politicalParty;

    // 俱乐部职务
    private String clubDuty;

    // 是否公务员，0为否
    private Boolean isCivilServant;

    // 是否科局级及以上，0为否
    private Boolean isCadre;

    // 是否退役军人，0为否
    private Boolean isVeteran;

    // 专业运动员等级
    private String athleteLevel;

    // 裁判员等级
    private String refereeLevel;

    // 荣誉信息
    private String honourInfo;

    // 身高(cm)
    private Double height;

    // 体重(kg)
    private Double weight;

    // 服装尺寸
    private String uniformSize;

    // 人员归属地（分龙港、苍南、平阳、温州市内、浙江省内、省外6个选项）
    private String residenceArea;

    // 当前所属俱乐部名
    private String currentClubName;

    // 当前组别，0表示丙，1表示乙，2表示甲
    private String currentLevel;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
