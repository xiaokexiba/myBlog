package com.yeffxyz.blog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffxyz.blog.entity.Message;
import com.yeffxyz.blog.service.MessageService;
import com.yeffxyz.blog.mapper.MessageMapper;
import org.springframework.stereotype.Service;

/**
 * 留言业务层接口实现类
 *
 * @author xoke
 * @date 2022/9/2
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
        implements MessageService {

}




