package io.hpp.redistest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

@Component
public class RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private Logger LOGGER = LoggerFactory.getLogger(RedisRepository.class);

    public RedisRepository(@Autowired RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private final String ACTIVE_QUEUE_KEY = "ACTIVE_QUEUE_KEY";
    // 값으로 조회하는거
    public String getToken(String token){
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        Double result = zSetOperations.score(ACTIVE_QUEUE_KEY,token);

        if(result != null)return token;
        else return null;
    }

    // 값 대입하는거
    public void addToken(String token){
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
//reverseRangeByScore(key, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0, 1);
//        String maxScore = Objects.
//                requireNonNull(zSetOperations.reverseRangeByScore(ACTIVE_QUEUE_KEY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,0, 1)).
//                stream().
//                findFirst().
//                orElse("0");


        //LOGGER.debug("score >>>>>>>>>>>>>>>>>>>> [{}]",maxScore);

        //int newScore = Integer.parseInt() + 1;
        long newScore = System.currentTimeMillis();
        LOGGER.debug("score >>>>>>>>>>>>>>>>>>>> [{}]",newScore);
        redisTemplate.opsForZSet().add(ACTIVE_QUEUE_KEY,token, newScore);
    }

    // 개수 구하는거
    public Long getCount(){
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        return zSetOperations.zCard(ACTIVE_QUEUE_KEY);
    }


    // 개수 구하는거
    public void displayAllMembers(){
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> result = zSetOperations.rangeWithScores(ACTIVE_QUEUE_KEY,0,-1);

        assert result != null;
        for (ZSetOperations.TypedTuple<String> s : result) {
            LOGGER.debug("모든 값!!!!!!!!!! [{}]          스코어 [{}]", s.getValue(), s.getScore());
        }

    }


    // 삭제하는거
    public void delToken(String token){
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.remove(ACTIVE_QUEUE_KEY,token);
    }

    public void delAllToken(){
        redisTemplate.delete(ACTIVE_QUEUE_KEY);
    }
}
