package com.yun.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yun.springbootinit.model.dto.DeleteDTO;
import com.yun.springbootinit.model.dto.club.ClubAddRequest;
import com.yun.springbootinit.model.dto.club.ClubDTO;
import com.yun.springbootinit.model.dto.club.FrontendObject;
import com.yun.springbootinit.model.entity.Club;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yun.springbootinit.model.entity.Member;
import com.yun.springbootinit.model.vo.ClubVO;
import com.yun.springbootinit.model.vo.MemberVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 俱乐部信息 服务类
 * </p>
 *
 * @author chenyun
 * @since 2025-02-21
 */
public interface IClubService extends IService<Club> {

    List<FrontendObject> getAllClubName();

    QueryWrapper<Club> getQueryWrapper(ClubDTO clubDTO);

    List<ClubVO> listClubVO(List<Club> clubList);

    ClubVO getClubVO(Club club);

    Long addClub(ClubAddRequest clubAddRequest);

    Integer deleteClub(DeleteDTO deleteDTO);
}
