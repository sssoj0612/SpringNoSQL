package kopo.poly.persistance.mongodb.redis.impl;

import kopo.poly.dto.RedisDTO;
import kopo.poly.persistance.mongodb.redis.IMyRedisMapper;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.List;
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

    @Override   // 문자열 저장
    public int saveString(String redisKey, RedisDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".saveString Start!");

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

        log.info(this.getClass().getName() + ".saveString End!");

        return res;
    }


    @Override   // 문자열 조회
    public RedisDTO getString(String redisKey) throws Exception {

        log.info(this.getClass().getName() + ".getString Start!");

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

        log.info(this.getClass().getName() + ".getString End!");

        return rDTO;

    }


    @Override // 문자열 JSON 저장
    public int saveStringJSON(String redisKey, RedisDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".saveStringJSON Start!");

        int res;

        /* redis 저장 및 읽기에 대한 데이터 타입 지정(String 타입으로 지정함) */
        redisDB.setKeySerializer(new StringRedisSerializer()); // String타입

        /* RedisDTO에 저장된 데이터를 자동으로 JSON으로 변경하기 */
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisDTO.class)); // DTO값을 JSON으로 변환

        this.deleteRedisKey(redisKey); // RedisDB 저장된 키 삭제

        // 데이터 저장하기
        redisDB.opsForValue().set(redisKey, pDTO);

        // RedisDB에 저장되는 데이터의 유효시간 설정 (TTL 설정 필수!)
        // 2일이 지나면 자동으로 데이터가 삭제되도록 설정
        redisDB.expire(redisKey, 2, TimeUnit.DAYS);

        res = 1;

        log.info(this.getClass().getName() + ".saveStringJSON End!");

        return res;
    }


    @Override // 문자열 JSON으로 저장된거 조회
    public RedisDTO getStringJSON(String redisKey) throws Exception {

        log.info(this.getClass().getName() + ".getStringJSON Start!");

        log.info("String redisKey : " + redisKey);

        RedisDTO rDTO = null;

        /* redis 저장 및 읽기에 대한 데이터 타입 지정(String 타입으로 지정함) */
        redisDB.setKeySerializer(new StringRedisSerializer()); // String타입

        /* RedisDTO에 저장된 데이터를 자동으로 JSON으로 변경하기 */
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisDTO.class)); // DTO값을 JSON으로 변환

        if (redisDB.hasKey(redisKey)) { // 데이터가 존재한다면 조회하기
            rDTO = (RedisDTO) redisDB.opsForValue().get(redisKey);
        }

        log.info(this.getClass().getName() + ".getStringJSON End!");

        return rDTO;
    }


    @Override // List 구조로 저장
    public int saveList(String redisKey, List<RedisDTO> pList) throws Exception {

        log.info(this.getClass().getName() + ".saveList Start!");

        int res;

        /* redis 저장 및 읽기에 대한 데이터 타입 지정(String 타입으로 지정함) */
        redisDB.setKeySerializer(new StringRedisSerializer()); // String타입
        redisDB.setValueSerializer(new StringRedisSerializer()); // String타입

        this.deleteRedisKey(redisKey); // RedisDB 저장된 키 삭제

        pList.forEach(dto -> {

            // 오름차순으로 저장
            // redisDB.opsForList().rightPush(redisKey, CmmUtil.nvl(text));

            // 내림차순으로 저장
            redisDB.opsForList().leftPush(redisKey, CmmUtil.nvl(dto.text()));
        });

        // RedisDB에 저장되는 데이터의 유효시간 설정 (TTL 설정 필수!)
        // 5시간이 지나면 자동으로 데이터가 삭제되도록 설정
        redisDB.expire(redisKey, 5, TimeUnit.HOURS);

        res = 1;

        log.info(this.getClass().getName() + ".saveList End!");

        return res;

    }


    @Override // List 구조로 저장된 데이터 조회
    public List<String> getList(String redisKey) throws Exception {

        log.info(this.getClass().getName() + ".getList Start!");

        List<String> rList = null;

        /* Redis 저장 및 읽기에 대한 데이터 타입 지정(반드시 저장할때 사용한 타입과 동일하게 맞추기) */
        redisDB.setKeySerializer(new StringRedisSerializer()); // String 타입
        redisDB.setValueSerializer(new StringRedisSerializer()); // String타입

        if (redisDB.hasKey(redisKey)) { // 데이터가 존재한다면 조회하기

            // 0부터 -1은 전체 데이터를 가져오는 것을 의미함
            rList = (List) redisDB.opsForList().range(redisKey,0,-1);

        }

        log.info(this.getClass().getName() + ".getList End!");

        return rList;

    }


    @Override
    public int saveListJSON(String redisKey, List<RedisDTO> pList) throws Exception {

        log.info(this.getClass().getName() + ".saveListJSON Start!");

        int res;

        /* redis 저장 및 읽기에 대한 데이터 타입 지정(String 타입으로 지정함) */
        redisDB.setKeySerializer(new StringRedisSerializer()); // String타입

        /* RedisDTO에 저장된 데이터를 자동으로 JSON으로 변경하기 */
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisDTO.class)); // DTO값을 JSON으로 변환

        this.deleteRedisKey(redisKey); // RedisDB 저장된 키 삭제

        pList.forEach(dto -> {

             //오름차순으로 저장
             redisDB.opsForList().rightPush(redisKey, dto);

        });

        // RedisDB에 저장되는 데이터의 유효시간 설정 (TTL 설정 필수!)
        // 5시간이 지나면 자동으로 데이터가 삭제되도록 설정
        redisDB.expire(redisKey, 5, TimeUnit.HOURS);

        res = 1;

        log.info(this.getClass().getName() + ".saveListJSON End!");

        return res;

    }


    @Override
    public List<RedisDTO> getListJSON(String redisKey) throws Exception {

        log.info(this.getClass().getName() + ".getListJSON Start!");

        List<RedisDTO> rList = null;

        /* Redis 저장 및 읽기에 대한 데이터 타입 지정(반드시 저장할때 사용한 타입과 동일하게 맞추기) */
        redisDB.setKeySerializer(new StringRedisSerializer()); // String 타입

        /* RedisDTO에 저장된 데이터를 자동으로 JSON으로 변경하기 */
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisDTO.class)); // DTO값을 JSON으로 변환

        if (redisDB.hasKey(redisKey)) { // 데이터가 존재한다면 조회하기

            // 0부터 -1은 전체 데이터를 가져오는 것을 의미함
            rList = (List) redisDB.opsForList().range(redisKey,0,-1);

        }

        log.info(this.getClass().getName() + ".getListJSON End!");

        return rList;

    }


    @Override
    public int saveHash(String redisKey, RedisDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".saveHash Start!");

        int res;

        /* redis 저장 및 읽기에 대한 데이터 타입 지정(String 타입으로 지정함) */
        redisDB.setKeySerializer(new StringRedisSerializer()); // String타입
        redisDB.setHashKeySerializer(new StringRedisSerializer()); // Hash타입 구조의 키 타입, String타입
        redisDB.setHashValueSerializer(new StringRedisSerializer()); // Hash타입 구조의 값 타입, String타입

        this.deleteRedisKey(redisKey); // RedisDB 저장된 키 삭제

        // 데이터 저장하기
        redisDB.opsForHash().put(redisKey, "name", CmmUtil.nvl(pDTO.name()));
        redisDB.opsForHash().put(redisKey, "email", CmmUtil.nvl(pDTO.email()));
        redisDB.opsForHash().put(redisKey, "addr", CmmUtil.nvl(pDTO.addr()));

        // RedisDB에 저장되는 데이터의 유효시간 설정 (TTL 설정 필수!)
        // 100분이 지나면 자동으로 데이터가 삭제되도록 설정
        redisDB.expire(redisKey, 100, TimeUnit.MINUTES);

        res = 1;

        log.info(this.getClass().getName() + ".saveHash End!");

        return res;

    }


    @Override
    public RedisDTO getHash(String redisKey) throws Exception {

        log.info(this.getClass().getName() + ".getHash Start!");

        RedisDTO rDTO = null;

        /* Redis 저장 및 읽기에 대한 데이터 타입 지정(반드시 저장할때 사용한 타입과 동일하게 맞추기) */
        redisDB.setKeySerializer(new StringRedisSerializer()); // String 타입
        redisDB.setHashKeySerializer(new StringRedisSerializer()); // String타입
        redisDB.setHashValueSerializer(new StringRedisSerializer()); // String타입

        if (redisDB.hasKey(redisKey)) { // 데이터가 존재한다면 조회하기

            String name = CmmUtil.nvl((String) redisDB.opsForHash().get(redisKey, "name"));
            String email = CmmUtil.nvl((String) redisDB.opsForHash().get(redisKey, "email"));
            String addr = CmmUtil.nvl((String) redisDB.opsForHash().get(redisKey, "addr"));

            log.info("name : " + name);
            log.info("email : " + email);
            log.info("addr : " + addr);

            rDTO = RedisDTO.builder().name(name).email(email).addr(addr).build();
        }

        log.info(this.getClass().getName() + ".getHash End!");

        return rDTO;

    }
}
