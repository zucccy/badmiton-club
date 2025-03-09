package com.yun.springbootinit.model.dto.club;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description: TODO
 * @Author: chenyun
 * @Date: 2025/3/7 22:43
 * @version: 1.0
 */
@Data
public class ClubAddRequest {
    @NotBlank(message = "需要填写俱乐部名")
    @JsonProperty(value = "club_name")
    private String clubName;
}
