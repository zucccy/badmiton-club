package com.yun.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 俱乐部会员联系表
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
@TableName("club_member_relation")
@ApiModel(value="ClubMemberRelation对象", description="俱乐部会员联系表")
public class ClubMemberRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("member_id")
    private Long memberId;

    @TableField("club_id")
    private Long clubId;

    @TableField("start_date")
    private LocalDate startDate;

    @ApiModelProperty(value = "为空表示当前所属")
    @TableField("end_date")
    private LocalDate endDate;


}
