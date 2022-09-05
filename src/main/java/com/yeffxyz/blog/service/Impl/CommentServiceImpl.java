package com.yeffxyz.blog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffxyz.blog.entity.Comment;
import com.yeffxyz.blog.service.CommentService;
import com.yeffxyz.blog.mapper.CommentMapper;
import org.springframework.stereotype.Service;

/**
 * 评论业务层接口实现类
 *
 * @author xoke
 * @date 2022/9/6
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {

}




