package com.yun.springbootinit.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description: TODO
 * @Author: chenyun
 * @Date: 2025/2/27 22:18
 * @version: 1.0
 */
@Data
public class MemberExportVO {

    public static final String SHEET_NAME = "会员导出信息表";

    @ExcelProperty(value = "姓名")
    private String name;

    @ExcelProperty(value = "性别（男/女）")
    private String gender;

    @ExcelProperty(value = "年龄")
    private Integer age;

    @ExcelProperty(value = "手机号")
    private String phone;

    @ExcelProperty(value = "民族")
    private String nation;

    @ExcelProperty(value = "籍贯")
    private String originAddress;

    @ExcelProperty(value = "家庭住址")
    private String homeAddress;

    @ExcelProperty(value = "工作单位")
    private String workUnit;

    @ExcelProperty(value = "职业")
    private String occupation;

    @ExcelProperty(value = "党派")
    private String politicalParty;

    @ExcelProperty(value = "俱乐部职务")
    private String clubDuty;

    @ExcelProperty(value = "是否公务员（是/否）")
    @NotNull(message = "需要填写是否公务员")
    private String isCivilServant;

    @ExcelProperty(value = "是否科局级及以上（是/否）")
    @NotNull(message = "需要填写是否科局级及以上")
    private String isCadre;

    @ExcelProperty(value = "是否退役军人（是/否）")
    @NotNull(message = "需要填写是否退役军人")
    private String isVeteran;

    @ExcelProperty(value = "专业运动员等级（国家级/省级/市级/县级）")
    private String athleteLevel;

    @ExcelProperty(value = "裁判员等级（国家级/一级/二级/三级）")
    private String refereeLevel;

    @ExcelProperty(value = "荣誉信息")
    private String honourInfo;

    @ExcelProperty(value = "身高(cm)")
    private Double height;

    @ExcelProperty(value = "体重(kg)")
    private Double weight;

    @ExcelProperty(value = "服装尺寸")
    private String uniformSize;

    @ExcelProperty(value = "人员归属地（龙港、苍南、平阳、温州市内、浙江省内、浙江省外）")
    private String residenceArea;

    @ExcelProperty(value = "当前所属俱乐部名")
    private String currentClubName;

    @ExcelProperty(value = "组别（甲组/乙组/丙组）")
    private String currentLevel;
}
