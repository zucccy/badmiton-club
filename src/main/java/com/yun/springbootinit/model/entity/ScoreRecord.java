package com.yun.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 积分记录
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
@TableName("score_record")
@ApiModel(value="ScoreRecord对象", description="积分记录")
public class ScoreRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("member_id")
    private Long memberId;

    @TableField("game_id")
    private Long gameId;

    @ApiModelProperty(value = "本次赛事获得积分")
    @TableField("score")
    private Integer score;

    @ApiModelProperty(value = "赛事名次")
    @TableField("ranking")
    private Integer ranking;


}
