package com.yeffcc.blog.service.Impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeffcc.blog.dto.UniqueViewDTO;
import com.yeffcc.blog.entity.UniqueView;
import com.yeffcc.blog.mapper.UniqueViewMapper;
import com.yeffcc.blog.service.RedisService;
import com.yeffcc.blog.service.UniqueViewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.yeffcc.blog.constant.RedisPrefixConst.UNIQUE_VISITOR;
import static com.yeffcc.blog.constant.RedisPrefixConst.VISITOR_AREA;
import static com.yeffcc.blog.enums.ZoneEnum.SHANGHAI;

/**
 * 用户量业务层接口实现类
 *
 * @author xoke
 * @date 2022/9/5
 */
@Slf4j
@Service
public class UniqueViewServiceImpl extends ServiceImpl<UniqueViewMapper, UniqueView> implements UniqueViewService {

    @Resource
    private RedisService redisService;
    @Resource
    private UniqueViewMapper uniqueViewMapper;

    /**
     * 查询7天用户量
     *
     * @return 7天用户量
     */
    @Override
    public List<UniqueViewDTO> listUniqueViews() {
        DateTime startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -7));
        DateTime endTime = DateUtil.endOfDay(new Date());
        return uniqueViewMapper.listUniqueViews(startTime, endTime);
    }

    /**
     * 保存访问量
     */
    @Scheduled(cron = " 0 0 0 * * ?", zone = "Asia/Shanghai")
    public void saveUniqueView() {
        // 获取每天用户量
        Long count = redisService.sSize(UNIQUE_VISITOR);
        // 获取昨天用户插入数据
        UniqueView uniqueView = UniqueView.builder()
                .createTime(LocalDateTimeUtil.offset(LocalDateTime.now(ZoneId.of(SHANGHAI.getZone())), -1, ChronoUnit.DAYS))
                .viewsCount(Optional.of(count.intValue()).orElse(0))
                .build();
        uniqueViewMapper.insert(uniqueView);
    }

    /**
     * 清空访问量
     */
    @Scheduled(cron = " 0 1 0 * * ?", zone = "Asia/Shanghai")
    public void clear() {
        // 清空redis访客记录
        redisService.del(UNIQUE_VISITOR);
        // 清空redis游客区域统计
        redisService.del(VISITOR_AREA);
    }
}
