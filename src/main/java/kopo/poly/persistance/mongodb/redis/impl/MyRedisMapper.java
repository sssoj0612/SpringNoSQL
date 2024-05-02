package kopo.poly.persistance.mongodb.redis.impl;

import kopo.poly.dto.RedisDTO;
import kopo.poly.persistance.mongodb.redis.IMyRedisMapper;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class MyRedisMapper implements IMyRedisMapper {

    private final RedisTemplate<String, Object> redisDB;

    private void deleteRedisKey(String redisKey) {

        if (redisDB.hasKey(redisKey)) { // 데이터가 존재하면, 기존 데이터 삭제하기
            redisDB.delete(redisKey); // 삭제하기
            log.info("삭제 성공!");
        }

    }

    @Override   // 키 저장
    public int saveString(String redisKey, RedisDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + "saveString Start!");

        int res;

        String saveData = CmmUtil.nvl(pDTO.text()); // 저장할 값

        /* redis 저장 및 읽기에 대한 데이터 타입 지정(String 타입으로 지정함) */
        redisDB.setKeySerializer(new StringRedisSerializer()); // String타입
        redisDB.setValueSerializer(new StringRedisSerializer()); // String타입

        this.deleteRedisKey(redisKey); // RedisDB 저장된 키 삭제

        // 데이터 저장하기
        redisDB.opsForValue().set(redisKey, saveData);

        // RedisDB에 저장되는 데이터의 유효시간 설정 (TTL 설정 필수!)
        // 2일이 지나면 자동으로 데이터가 삭제되도록 설정
        redisDB.expire(redisKey, 2, TimeUnit.DAYS);

        res = 1;

        log.info(this.getClass().getName() + "saveString End!");

        return res;
    }


    @Override   // 키 조회
    public RedisDTO getString(String redisKey) throws Exception {

        log.info(this.getClass().getName() + "getString Start!");

        log.info("String redisKey : " + redisKey);

        /* Redis 저장 및 읽기에 대한 데이터 타입 지정(반드시 저장할때 사용한 타입과 동일하게 맞추기) */
        redisDB.setKeySerializer(new StringRedisSerializer()); // String 타입
        redisDB.setValueSerializer(new StringRedisSerializer()); // String타입

        RedisDTO rDTO = null;

        if (redisDB.hasKey(redisKey)) { // 데이터가 존재한다면 조회하기
            String res = (String) redisDB.opsForValue().get(redisKey); // redisKey를 통해 조회
            log.info("res : " + res); // 조회 결과
            rDTO = RedisDTO.builder().text(res).build(); // RedisDB에 저장된 데이터를 DTO에 저장
        }

        log.info(this.getClass().getName() + "getString End!");

        return rDTO;

    }
}
