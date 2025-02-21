package com.yun.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 文件附件表
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
@TableName("attachment")
@ApiModel(value="Attachment对象", description="文件附件表")
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("member_id")
    private Long memberId;

    @ApiModelProperty(value = "身份证、房产原件、社保")
    @TableField("file_type")
    private String fileType;

    @TableField("file_path")
    private String filePath;

    @TableField("status")
    private String status;

    @ApiModelProperty(value = "审核人")
    @TableField("reviewed_by")
    private Long reviewedBy;

    @ApiModelProperty(value = "审核时间")
    @TableField("reviewed_time")
    private LocalDateTime reviewedTime;

    @ApiModelProperty(value = "上传时间")
    @TableField("upload_time")
    private LocalDateTime uploadTime;


}
