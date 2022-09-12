package com.yeffcc.blog.service.Impl;

import com.yeffcc.blog.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * redis操作业务层接口实现类
 *
 * @author xoke
 * @date 2022/9/12
 */
@Slf4j
@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 保存属性
     *
     * @param key   key值
     * @param value value值
     * @param time  时间戳
     */
    @Override
    public void set(String key, Object value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 保存属性
     *
     * @param key   key值
     * @param value value值
     */
    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取属性
     *
     * @param key key值
     * @return 返回对象
     */
    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除属性
     *
     * @param key key值
     * @return 返回成功
     */
    @Override
    public Boolean del(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 批量删除属性
     *
     * @param keys key值集合
     * @return 返回删除数量
     */
    @Override
    public Long del(List<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * 设置过期时间
     *
     * @param key  key值
     * @param time 时间戳
     * @return 返回成功
     */
    @Override
    public Boolean expire(String key, long time) {
        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 获取过期时间
     *
     * @param key key值
     * @return 返回时间戳
     */
    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key key值
     * @return 返回
     */
    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 按delta递增
     *
     * @param key   key值
     * @param delta delta值
     * @return 返回递增后结果
     */
    @Override
    public Long incr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 获取自增1后的 值
     *
     * @param key  key值
     * @param time 过期时间
     * @return 返回递增后结果
     */
    @Override
    public Long incrExpire(String key, long time) {
        Long count = redisTemplate.opsForValue().increment(key, 1);
        if (count != null && count == 1) {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
        return count;
    }

    /**
     * 按delta递减
     *
     * @param key   key值
     * @param delta delta值
     * @return 返回递减后结果
     */
    @Override
    public Long decr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * 获取Hash结构中的属性
     *
     * @param key     外部key值
     * @param hashKey 内部key值
     * @return 返回内部key的value
     */
    @Override
    public Object hGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 向Hash结构中放入一个属性
     *
     * @param key     外部key
     * @param hashKey 内部key
     * @param value   内部key的value
     * @param time    过期时间
     * @return 返回是否成功
     */
    @Override
    public Boolean hSet(String key, String hashKey, Object value, long time) {
        redisTemplate.opsForHash().put(key, hashKey, value);
        return expire(key, time);
    }

    /**
     * 向Hash结构中放入一个属性
     *
     * @param key     外部key
     * @param hashKey 内部key
     * @param value   内部key的value
     */
    @Override
    public void hSet(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 直接获取整个Hash结构
     *
     * @param key 外部key值
     * @return 返回hashMap
     */
    @Override
    public Map hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 直接设置整个Hash结构
     *
     * @param key  外部key
     * @param map  hashMap值
     * @param time 过期时间
     * @return 返回是否成功
     */
    @Override
    public Boolean hSetAll(String key, Map<String, Object> map, long time) {
        redisTemplate.opsForHash().putAll(key, map);
        return expire(key, time);
    }

    /**
     * 直接设置整个Hash结构
     *
     * @param key 外部key
     * @param map hashMap值
     */
    @Override
    public void hSetAll(String key, Map<String, ?> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 删除Hash结构中的属性
     *
     * @param key     外部key值
     * @param hashKey 内部key值
     */
    @Override
    public void hDel(String key, Object... hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    /**
     * 判断Hash结构中是否有该属性
     *
     * @param key     外部key
     * @param hashKey 内部key
     * @return 返回是否存在
     */
    @Override
    public Boolean hHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * Hash结构中属性递增
     *
     * @param key     外部key
     * @param hashKey 内部key
     * @param delta   递增条件
     * @return 返回递增后的数据
     */
    @Override
    public Long hIncr(String key, String hashKey, Long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    /**
     * Hash结构中属性递减
     *
     * @param key     外部key
     * @param hashKey 内部key
     * @param delta   递增条件
     * @return 返回递减后的数据
     */
    @Override
    public Long hDecr(String key, String hashKey, Long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, -delta);
    }

    /**
     * zset添加分数
     *
     * @param key   关键
     * @param value 价值
     * @param score 分数
     */
    @Override
    public Double zIncr(String key, Object value, Double score) {
        return redisTemplate.opsForZSet().incrementScore(key, value, score);
    }

    /**
     * zset减少分数
     *
     * @param key   关键
     * @param value 价值
     * @param score 分数
     */
    @Override
    public Double zDecr(String key, Object value, Double score) {
        return redisTemplate.opsForZSet().incrementScore(key, value, -score);
    }

    /**
     * zset根据分数排名获取指定元素信息
     *
     * @param key   关键
     * @param start 开始
     * @param end   结束
     */
    @Override
    public Map<Object, Double> zReverseRangeWithScore(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end)
                .stream()
                .collect(Collectors.toMap(ZSetOperations.TypedTuple::getValue, ZSetOperations.TypedTuple::getScore));
    }

    /**
     * 获取zset指定元素分数
     *
     * @param key   关键
     * @param value 价值
     */
    @Override
    public Double zScore(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 获取zset所有分数
     *
     * @param key 关键
     */
    @Override
    public Map<Object, Double> zAllScore(String key) {
        return Objects.requireNonNull(redisTemplate.opsForZSet().rangeWithScores(key, 0, -1))
                .stream()
                .collect(Collectors.toMap(ZSetOperations.TypedTuple::getValue, ZSetOperations.TypedTuple::getScore));
    }

    /**
     * 获取Set结构
     *
     * @param key key
     * @return 返回set集合
     */
    @Override
    public Set<Object> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 向Set结构中添加属性
     *
     * @param key    key
     * @param values value集
     * @return 返回增加数量
     */
    @Override
    public Long sAdd(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 向Set结构中添加属性
     *
     * @param key    key
     * @param time   过期时间
     * @param values 值集合
     * @return 返回添加的数量
     */
    @Override
    public Long sAddExpire(String key, long time, Object... values) {
        Long count = redisTemplate.opsForSet().add(key, values);
        expire(key, time);
        return count;
    }

    /**
     * 是否为Set中的属性
     *
     * @param key   key
     * @param value value
     * @return 返回是否存在
     */
    @Override
    public Boolean sIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 获取Set结构的长度
     *
     * @param key key
     * @return 返回长度
     */
    @Override
    public Long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 删除Set结构中的属性
     *
     * @param key    key
     * @param values value集合
     * @return 删除掉的数据量
     */
    @Override
    public Long sRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 获取List结构中的属性
     *
     * @param key   key
     * @param start 开始
     * @param end   结束
     * @return 返回查询的集合
     */
    @Override
    public List<Object> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 获取List结构的长度
     *
     * @param key key
     * @return 长度
     */
    @Override
    public Long lSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 根据索引获取List中的属性
     *
     * @param key   key
     * @param index 索引
     * @return 对象
     */
    @Override
    public Object lIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 向List结构中添加属性
     *
     * @param key   key
     * @param value value
     * @return 增加后的长度
     */
    @Override
    public Long lPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 向List结构中添加属性
     *
     * @param key   key
     * @param value value
     * @param time  过期时间
     * @return 增加后的长度
     */
    @Override
    public Long lPush(String key, Object value, long time) {
        Long index = redisTemplate.opsForList().rightPush(key, value);
        expire(key, time);
        return index;
    }

    /**
     * 向List结构中批量添加属性
     *
     * @param key    key
     * @param values value 集合
     * @return 增加后的长度
     */
    @Override
    public Long lPushAll(String key, Object... values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 向List结构中批量添加属性
     *
     * @param key    key
     * @param time   过期时间
     * @param values value集合
     * @return 增加后的长度
     */
    @Override
    public Long lPushAll(String key, Long time, Object... values) {
        Long count = redisTemplate.opsForList().rightPushAll(key, values);
        expire(key, time);
        return count;
    }

    /**
     * 从List结构中移除属性
     *
     * @param key   key
     * @param count 总量
     * @param value value
     * @return 返回删除后的长度
     */
    @Override
    public Long lRemove(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    /**
     * 向bitmap中新增值
     *
     * @param key    key
     * @param offset 偏移量
     * @param b      状态
     * @return 结果
     */
    @Override
    public Boolean bitAdd(String key, int offset, boolean b) {
        return redisTemplate.opsForValue().setBit(key, offset, b);
    }

    /**
     * 从bitmap中获取偏移量的值
     *
     * @param key    key
     * @param offset 偏移量
     * @return 结果
     */
    @Override
    public Boolean bitGet(String key, int offset) {
        return redisTemplate.opsForValue().getBit(key, offset);
    }

    /**
     * 获取bitmap的key值总和
     *
     * @param key key
     * @return 总和
     */
    @Override
    public Long bitCount(String key) {
        return redisTemplate.execute((RedisCallback<Long>) con -> con.bitCount(key.getBytes()));
    }

    /**
     * 获取bitmap范围值
     *
     * @param key    key
     * @param limit  范围
     * @param offset 开始偏移量
     * @return long类型集合
     */
    @Override
    public List<Long> bitField(String key, int limit, int offset) {
        return redisTemplate.execute((RedisCallback<List<Long>>) con ->
                con.bitField(key.getBytes(),
                        BitFieldSubCommands.create().get(BitFieldSubCommands.BitFieldType.unsigned(limit)).valueAt(offset)));
    }

    /**
     * 获取所有bitmap
     *
     * @param key key
     * @return 以二进制字节数组返回
     */
    @Override
    public byte[] bitGetAll(String key) {
        return redisTemplate.execute((RedisCallback<byte[]>) con -> con.get(key.getBytes()));
    }

    /**
     * 向hyperlog中添加数据
     *
     * @param key   key
     * @param value 值
     */
    @Override
    public Long hyperAdd(String key, Object... value) {
        return redisTemplate.opsForHyperLogLog().add(key, value);
    }

    /**
     * 获取hyperlog元素数量
     *
     * @param key key
     * @return 元素数量
     */
    @Override
    public Long hyperGet(String... key) {
        return redisTemplate.opsForHyperLogLog().size(key);
    }

    /**
     * 删除hyperlog数据
     *
     * @param key key
     */
    @Override
    public void hyperDel(String key) {
        redisTemplate.opsForHyperLogLog().delete(key);
    }

    /**
     * 增加坐标
     *
     * @param key  key
     * @param x    x
     * @param y    y
     * @param name 地点名称
     * @return 返回结果
     */
    @Override
    public Long geoAdd(String key, Double x, Double y, String name) {
        return redisTemplate.opsForGeo().add(key, new Point(x, y), name);
    }

    /**
     * 根据城市名称获取坐标集合
     *
     * @param key   key
     * @param place 地点
     * @return 坐标集合
     */
    @Override
    public List<Point> geoGetPointList(String key, Object... place) {
        return redisTemplate.opsForGeo().position(key, place);
    }

    /**
     * 计算两个城市之间的距离
     *
     * @param key      key
     * @param placeOne 地点1
     * @param placeTow 地点2
     * @return 返回距离
     */
    @Override
    public Distance geoCalculationDistance(String key, String placeOne, String placeTow) {
        return redisTemplate.opsForGeo()
                .distance(key, placeOne, placeTow, RedisGeoCommands.DistanceUnit.KILOMETERS);
    }

    /**
     * 获取附该地点附近的其他地点
     *
     * @param key      key
     * @param place    地点
     * @param distance 附近的范围
     * @param limit    查几条
     * @param sort     排序规则
     * @return 返回附近的地点集合
     */
    @Override
    public GeoResults<RedisGeoCommands.GeoLocation<Object>> geoNearByPlace(String key, String place, Distance distance, long limit, Sort.Direction sort) {
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates();
        // 判断排序方式
        if (Sort.Direction.ASC == sort) {
            args.sortAscending();
        } else {
            args.sortDescending();
        }
        args.limit(limit);
        return redisTemplate.opsForGeo()
                .radius(key, place, distance, args);
    }

    /**
     * 获取地点的hash
     *
     * @param key   key
     * @param place 地点
     * @return 返回集合
     */
    @Override
    public List<String> geoGetHash(String key, String... place) {
        return redisTemplate.opsForGeo()
                .hash(key, place);
    }
}
