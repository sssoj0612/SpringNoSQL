package kopo.poly.persistance.mongodb.redis;

import kopo.poly.dto.RedisDTO;

import java.util.List;

public interface IMyRedisMapper {

    /* String 타입 저장하기
    * @param redisKey Redis 저장 키
    * @param pDTO     저장할 정보
    * @return         저장 성공 여부 */
    int saveString(String redisKey, RedisDTO pDTO) throws Exception;


    /* String 타입 가져오기
     * @param redisKey 가져올 RedisKey
     * @return         결과값 */
    RedisDTO getString(String redisKey) throws Exception;


    /* String 타입에 JSON 형태로 저장하기
    * @param redisKey Redis 저장 키
    * @param pDTO     저장할 정보
    * @return         결과값 */
    int saveStringJSON(String redisKey, RedisDTO pDTO) throws Exception;


    /* String 타입에 JSON 형태로 저장된 데이터 가져오기
     * @param redisKey 가져올 RedisKey
     * @return         결과값 */
    RedisDTO getStringJSON(String redisKey) throws Exception;


    /* List 타입에 여러 문자열로 저장하기
     * @param redisKey Redis 저장 키
     * @param pList    저장할 정보
     * @return         저장 성공 여부 */
    int saveList(String redisKey, List<RedisDTO> pList) throws Exception;


    /* List 타입에 여러 문자열로 저장된 데이터 가져오기
     * @param redisKey 가져올 RedisKey
     * @return         결과값 */
    List<String> getList(String redisKey) throws Exception;


    /* List 타입에 JSON 형태로 저장 (동기화)
     * @param redisKey Redis 저장 키
     * @param pList    저장할 정보
     * @return         저장 성공 여부 */
    int saveListJSON(String redisKey, List<RedisDTO> pList) throws Exception;


    /* List 타입에 JSON 형태로 저장된 데이터 가져오기
     * @param redisKey 가져올 RedisKey
     * @return         결과값 */
    List<RedisDTO> getListJSON(String redisKey) throws Exception;
}
