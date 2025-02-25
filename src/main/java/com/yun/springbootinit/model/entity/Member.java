package com.yun.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 会员主表
 * </p>
 *
 * @author chenyun
 * @since 2025-02-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("member")
@ApiModel(value="Member对象", description="会员主表")
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "关联用户ID")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "姓名")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "性别，0为男，1为女")
    @TableField("gender")
    private Integer gender;

    @ApiModelProperty(value = "出生日期")
    @TableField("birth_date")
    private LocalDate birthDate;

    @ApiModelProperty(value = "手机号码")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "民族")
    @TableField("nation")
    private String nation;

    @ApiModelProperty(value = "籍贯")
    @TableField("origin_address")
    private String originAddress;

    @ApiModelProperty(value = "家庭住址")
    @TableField("home_address")
    private String homeAddress;

    @ApiModelProperty(value = "工作单位")
    @TableField("work_unit")
    private String workUnit;

    @ApiModelProperty(value = "职业")
    @TableField("occupation")
    private String occupation;

    @ApiModelProperty(value = "党派")
    @TableField("political_party")
    private String politicalParty;

    @ApiModelProperty(value = "俱乐部职务")
    @TableField("club_duty")
    private String clubDuty;

    @ApiModelProperty(value = "是否公务员，0为否")
    @TableField("is_civil_servant")
    private Boolean isCivilServant;

    @ApiModelProperty(value = "是否科局级及以上，0为否")
    @TableField("is_cadre")
    private Boolean isCadre;

    @ApiModelProperty(value = "是否退役军人，0为否")
    @TableField("is_veteran")
    private Boolean isVeteran;

    @ApiModelProperty(value = "专业运动员等级")
    @TableField("athlete_level")
    private String athleteLevel;

    @ApiModelProperty(value = "裁判员等级")
    @TableField("referee_level")
    private String refereeLevel;

    @ApiModelProperty(value = "荣誉信息")
    @TableField("honour_info")
    private String honourInfo;

    @ApiModelProperty(value = "身高(cm)")
    @TableField("height")
    private Double height;

    @ApiModelProperty(value = "体重(kg)")
    @TableField("weight")
    private Double weight;

    @ApiModelProperty(value = "服装尺寸")
    @TableField("uniform_size")
    private String uniformSize;

    @ApiModelProperty(value = "人员归属地（分龙港、苍南、平阳、温州市内、浙江省内、省外6个选项）")
    @TableField("residence_area")
    private String residenceArea;

    @ApiModelProperty(value = "当前所属俱乐部id")
    @TableField("current_club_id")
    private Long currentClubId;

    @ApiModelProperty(value = "当前组别，0表示丙，1表示乙，2表示甲")
    @TableField("current_level")
    private Integer currentLevel;

    @ApiModelProperty(value = "加密身份证号")
    @TableField("id_number")
    private String idNumber;

    @ApiModelProperty(value = "加密银行卡号")
    @TableField("bank_account")
    private String bankAccount;

    @TableField("bank_name")
    private String bankName;

    @TableField("bank_branch")
    private String bankBranch;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;


}
