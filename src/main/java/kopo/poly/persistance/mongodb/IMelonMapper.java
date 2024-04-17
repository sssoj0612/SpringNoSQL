package kopo.poly.persistance.mongodb;

import kopo.poly.dto.MelonDTO;

import java.util.List;

public interface IMelonMapper {

    /* 멜론 노래 리스트 저장하기
    * @param pList 저장될 정보
    * @param colNm 저장할 컬렉션 이름
    * @return 저장 결과 */
    int insertSong(List<MelonDTO> pList, String colNm) throws Exception;

    /* 오늘 수집 된 멜론 노래리스트 가져오기
    * @param colNm 조회할 컬렉션 이름
    * @return 노래 리스트 */
    List<MelonDTO> getSongList(String colNm) throws Exception;

    /* 가수별 수집된 노래의 수 가져오기
    * @param colNm 조회할 컬렉션 이름
    * @return 노래 리스트 */
    List<MelonDTO> getSingerSongCnt(String colNm) throws Exception;

    /* 가수 이름으로 조회하기
    * @param colNm 조회할 컬렉션 이름
    * @param pDTO 가수명
    * @return 노래 리스트 */
    List<MelonDTO> getSingerSong (String colNm, MelonDTO pDTO) throws Exception;

    /* 컬렉션 삭제
    *  @param colNm 삭제할 컬렉션 이름
    *  @return 저장 결과 */
    int dropCollection(String colNm) throws Exception;

    /* MongoDB insertMany 함수를 통해 멜론차트 저장
    * 한건 insert 대비 속도가 빠름
    * @param colNm 저장할 컬렉션 이름
    * @param pList 저장될 정보
    * @return 저장 결과 */
    int insertManyField(String colNm, List<MelonDTO> pList) throws Exception;

}
