package kopo.poly.service.impl;

import kopo.poly.dto.RedisDTO;
import kopo.poly.persistance.mongodb.redis.IMyRedisMapper;
import kopo.poly.service.IMyRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyRedisService implements IMyRedisService {

    private final IMyRedisMapper myRedisMapper;

    @Override   // 저장 및 조회 구현
    public RedisDTO saveString(RedisDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".saveString Start!");

        // 저장할 RedisDB 키
        String redisKey = "myRedis_String";

        // 저장 결과
        RedisDTO rDTO = null;

        int res = myRedisMapper.saveString(redisKey, pDTO); // 저장

        if (res == 1) { // 저장 성공시 조회
            rDTO = myRedisMapper.getString(redisKey);
        } else {
            log.info("Redis 저장 실패!");
            new Exception("Redis 저장 실패!");
        }

        log.info(this.getClass().getName() + ".saveString End!");

        return rDTO;

    }


    @Override // JSON 형태로 저장 및 조회 구현
    public RedisDTO saveStringJSON(RedisDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".saveStringJSON Start!");

        // 저장할 RedisDB 키
        String redisKey = "myRedis_String_JSON";

        // 저장 결과
        RedisDTO rDTO;

        int res = myRedisMapper.saveStringJSON(redisKey, pDTO); // 저장

        if (res == 1) { // 저장 성공시 조회
            rDTO = myRedisMapper.getStringJSON(redisKey);
        } else {
            log.info("Redis 저장 실패!");
            throw new Exception("Redis 저장 실패!");
        }

        log.info(this.getClass().getName() + ".saveStringJSON End!");

        return rDTO;

    }
}