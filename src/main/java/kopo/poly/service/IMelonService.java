package kopo.poly.service;

import kopo.poly.dto.MelonDTO;

import java.util.List;

public interface IMelonService {

    /* 멜론 노래 리스트 저장하기 */
    int collectMelonSong() throws Exception;

    /* 오늘 수집된 멜론 노래리스트 가져오기 */
    List<MelonDTO> getSongList() throws Exception;

    /* 멜론 가수별 노래 수 가져오기 */
    List<MelonDTO> getSingerSongCnt() throws Exception;

    /* 가수의 노래 가져오기
    * @return 노래 리스트 */
    List<MelonDTO> getSingerSong(MelonDTO pDTO) throws Exception;

    /* 수집된 멜론 차트 저장된 MongoDB 컬렉션 삭제 */
    int dropCollection() throws Exception;

    /* 멜론 노래 리스트 한번에 저장 */
    List<MelonDTO> insertManyField() throws Exception;

    /* singer 필드 값인 방탄소년단을 BTS로 변경하기 */
    List<MelonDTO> updateField(MelonDTO pDTO) throws Exception;

    /* BTS 노래마다에 닉네임 필드 추가하고
    *  그 닉네임 필드에 BTS 저장 */
    List<MelonDTO> updateAddField(MelonDTO pDTO) throws Exception;

    /* BTS 노래마다에 멤버 필드 추가하고
     * 그 멤버 필드에 BTS 멤버 이름 List로 저장 */
    List<MelonDTO> updateAddListField(MelonDTO pDTO) throws Exception;

}
