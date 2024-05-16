package kopo.poly.service;

import kopo.poly.dto.RedisDTO;

public interface IMyRedisService {

    /* String 타입 저장 및 가져오기 */
    RedisDTO saveString(RedisDTO pDTO) throws Exception;

    /* String 타입에 JSON 형태로 저장 */
    RedisDTO saveStringJSON(RedisDTO pDTO) throws Exception;

}
