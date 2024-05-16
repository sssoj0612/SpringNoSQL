package kopo.poly.service;

import kopo.poly.dto.RedisDTO;

import java.util.List;

public interface IMyRedisService {

    /* String 타입 저장 및 가져오기 */
    RedisDTO saveString(RedisDTO pDTO) throws Exception;

    /* String 타입에 JSON 형태로 저장 */
    RedisDTO saveStringJSON(RedisDTO pDTO) throws Exception;

    /* List 타입에 여러 문자열로 저장 */
    List<String> saveList(List<RedisDTO> pList) throws Exception;

    /* List 타입에 JSON 형태로 저장 */
    List<RedisDTO> saveListJSON(List<RedisDTO> pList) throws Exception;
}
