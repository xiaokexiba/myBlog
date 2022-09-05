package com.yeffcc.blog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffcc.blog.entity.Comment;
import com.yeffcc.blog.mapper.CommentMapper;
import com.yeffcc.blog.service.CommentService;
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




