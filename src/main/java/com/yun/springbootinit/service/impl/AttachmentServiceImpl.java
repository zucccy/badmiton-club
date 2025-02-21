package com.yun.springbootinit.service.impl;

import com.yun.springbootinit.model.entity.Attachment;
import com.yun.springbootinit.mapper.AttachmentMapper;
import com.yun.springbootinit.service.IAttachmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文件附件表 服务实现类
 * </p>
 *
 * @author chenyun
 * @since 2025-02-21
 */
@Service
public class AttachmentServiceImpl extends ServiceImpl<AttachmentMapper, Attachment> implements IAttachmentService {

}
