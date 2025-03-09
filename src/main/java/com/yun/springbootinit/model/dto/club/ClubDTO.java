package com.yun.springbootinit.model.dto.club;

import com.yun.springbootinit.common.PageRequest;
import lombok.*;

import java.io.Serializable;

/**
 * @Description: TODO
 * @Author: chenyun
 * @Date: 2025/3/7 22:25
 * @version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ClubDTO extends PageRequest implements Serializable {

    private Long id;

    private String clubName;
}
