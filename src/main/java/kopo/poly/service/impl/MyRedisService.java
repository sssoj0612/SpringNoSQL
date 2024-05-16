package kopo.poly.service.impl;

import kopo.poly.dto.RedisDTO;
import kopo.poly.persistance.mongodb.redis.IMyRedisMapper;
import kopo.poly.service.IMyRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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


    @Override // List 타입 저장 및 조회
    public List<String> saveList(List<RedisDTO> pList) throws Exception {

        log.info(this.getClass().getName() + ".saveList Start!");

        // 저장할 RedisDB 키
        String redisKey = "myRedis_String_JSON";

        // 저장 결과
        List<String> rList;

        int res = myRedisMapper.saveList(redisKey, pList); // 저장

        if (res == 1) { // 저장 성공시 조회
            rList = myRedisMapper.getList(redisKey);
        } else {
            log.info("Redis 저장 실패!");
            throw new Exception("Redis 저장 실패!");
        }

        log.info(this.getClass().getName() + ".saveList End!");

        return rList;

    }


    @Override // List 구조에 JSON 형태로 저장 및 조회
    public List<RedisDTO> saveListJSON(List<RedisDTO> pList) throws Exception {

        log.info(this.getClass().getName() + ".saveListJSON Start!");

        // 저장할 RedisDB 키
        String redisKey = "myRedis_String_JSON";

        // 저장 결과
        List<RedisDTO> rList;

        int res = myRedisMapper.saveListJSON(redisKey, pList); // 저장

        if (res == 1) { // 저장 성공시 조회
            rList = myRedisMapper.getListJSON(redisKey);
        } else {
            log.info("Redis 저장 실패!");
            throw new Exception("Redis 저장 실패!");
        }

        log.info(this.getClass().getName() + ".saveListJSON End!");

        return rList;

    }


    @Override // Hash 구조에 저장 및 조회
    public RedisDTO saveHash(RedisDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".saveHash Start!");

        // 저장할 RedisDB 키
        String redisKey = "myRedis_Hash";

        // 저장 결과
        RedisDTO rDTO;

        int res = myRedisMapper.saveHash(redisKey, pDTO); // 저장

        if (res == 1) { // 저장 성공시 조회
            rDTO = myRedisMapper.getHash(redisKey);
        } else {
            log.info("Redis 저장 실패!");
            throw new Exception("Redis 저장 실패!");
        }

        log.info(this.getClass().getName() + ".saveHash End!");

        return rDTO;

    }
}
