package kopo.poly.service.impl;

import kopo.poly.dto.MelonDTO;
import kopo.poly.persistance.mongodb.IMelonMapper;
import kopo.poly.persistance.mongodb.redis.IMelonCacheMapper;
import kopo.poly.service.IMelonService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MelonService implements IMelonService {

    private final IMelonMapper melonMapper;
    private final IMelonCacheMapper melonCacheMapper;

    /* 멜론 차트 수집 함수 (웹크롤링) */
    private List<MelonDTO> doCollect() throws Exception {

        log.info(this.getClass().getName() + ".doCollect Start!");

        List<MelonDTO> pList = new LinkedList<>();

        // 멜론 탑100 중 50위까지 정보 가져오는 페이지
        String url = "https://www.melon.com/chart/index.htm";

        // JSOUP 라이브러리를 통해 사이트 접속되면, 그 사이트의 전체 HTML 소스 저장할 변수
        Document doc = Jsoup.connect(url).get();

        // <div class="service_list_song"> 이 태그 내에서 있는 HTML소스만 element에 저장됨
        Elements element = doc.select("div.service_list_song");

        // Iterator을 사용하여 멜론차트 정보 가져오기
        for (Element songInfo : element.select("div.wrap_song_info")) {

            // 크롤링을 통해 데이터 저장하기
            String song = CmmUtil.nvl(songInfo.select("div.ellipsis.rank01 a").text()); // 노래
            String singer = CmmUtil.nvl(songInfo.select("div.ellipsis.rank02 a").eq(0).text()); // 가수

            log.info("song : " + song);
            log.info("singer : " + singer);

            // 가수, 노래 정보가 모두 수집되었다면 저장함
            if ((!song.isEmpty()) && (!singer.isEmpty())) {

                MelonDTO pDTO = MelonDTO.builder().collectTime(DateUtil.getDateTime("yyyyMMddhhmmss"))
                        .song(song).singer(singer).build();

                // 한번에 여러개 데이터를 MongoDB에 저장할 List 형태의 데이터 저장하기
                pList.add(pDTO);

            }
        }

        log.info(this.getClass().getName() + ".doCollect End!");

        return pList;
    }

    @Override
    public int collectMelonSong() throws Exception {

        int res = 0;

        // 생성할 컬렉션명
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        // private 함수로 선언된 doCollect 함수 호출하여 결과 받기
        List<MelonDTO> rList = this.doCollect();

        // MongoDB에 데이터 저장하기
        res = melonMapper.insertSong(rList, colNm);

        if (!melonCacheMapper.getExistKey(colNm)) { // RedisDB에 저장된게 없다면
            res = melonCacheMapper.insertSong(rList, colNm); // RedisDB 저장하기
        }

        log.info(this.getClass().getName() + ".collectMelonSong End!");

        return res;
    }


    /* 수집된 멜론차트 조회하기 */
    @Override
    public List<MelonDTO> getSongList() throws Exception {

        log.info(this.getClass().getName() + ".getSongList Start!");

        // MongoDB에 저장된 컬렉션 이름
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        List<MelonDTO> rList;

        if (melonCacheMapper.getExistKey(colNm)) { // RedisDB에 데이터가 있다면
            rList = melonCacheMapper.getSongList(colNm); // RedisDB 데이터 가져오기
        } else {
            rList = melonMapper.getSongList(colNm); // MongoDB에서 데이터 가져오기
        }

        log.info(this.getClass().getName() + ".getSongList End!");

        return rList;
    }

    /* 멜론 가수별 노래 수 가져오기 */

    @Override
    public List<MelonDTO> getSingerSongCnt() throws Exception {

        log.info(this.getClass().getName() + ".getSingerSongCnt Start!");

        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        List<MelonDTO> rList = melonMapper.getSingerSongCnt(colNm);

        log.info(this.getClass().getName() + ".getSingerSongCnt End!");

        return rList;

    }


    @Override
    public List<MelonDTO> getSingerSong(MelonDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getSingerSong Start!");

        // MongoDB에 저장된 컬렉션 이름
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        // 결과값
        List<MelonDTO> rList = null;

        // Melon 노래 수집하기
        if (this.collectMelonSong() == 1) {

            // 가수 노래 조회
            rList = melonMapper.getSingerSong(colNm, pDTO);
        }

        log.info(this.getClass().getName() + ".getSingerSong End!");

        return rList;

    }

    /* 오늘 생성된 컬렉션 삭제 */

    @Override
    public int dropCollection() throws Exception {

        log.info(this.getClass().getName() + ".dropCollection Start!");

        int res = 0;

        // MongoDB에 저장된 컬렉션 이름
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        // 기존 수집된 컬렉션 삭제
        res = melonMapper.dropCollection(colNm);

        log.info(this.getClass().getName() + ".getSingerSong End!");

        return res;
    }

    @Override
    public List<MelonDTO> insertManyField() throws Exception {

        log.info(this.getClass().getName() + ".insertManyField Start!");

        List<MelonDTO> rList = null; // 변경된 데이터 조회 결과

        // 생성할 컬렉션명
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        // MongoDB에 저장하기
        if (melonMapper.insertManyField(colNm, this.doCollect()) == 1) { // insertMany Mapper 호출

            // 변경된 값을 확인하기 위해 MongoDB로부터 데이터 조회
            rList = melonMapper.getSongList(colNm);

        }

        log.info(this.getClass().getName() + ".insertManyField End!");

        return rList;

    }

    @Override
    public List<MelonDTO> updateField(MelonDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updateField Start!");

        List<MelonDTO> rList = null; // 변경된 데이터 조회 결과

        // 수정할 컬렉션
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        // 기존 수집된 멜론Top100 수집한 컬렉션 삭제
        melonMapper.dropCollection(colNm);

        // 멜론Top100 수집하기
        if (this.collectMelonSong() == 1) {

            // 예: singer 필드에 저장된 '방탄소년단' 값을 'BTS'로 변경
            if (melonMapper.updateField(colNm, pDTO) == 1) {

                // 변경된 값을 확인하기 위해 MongoDB로부터 데이터 조회
                rList = melonMapper.getUpdateSinger(colNm, pDTO); // 수정된 결과 조회
            }
        }

        log.info(this.getClass().getName() + ".updateField End!");

        return rList;

    }

    @Override
    public List<MelonDTO> updateAddField(MelonDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updateAddField Start!");

        List<MelonDTO> rList = null; // 변경된 데이터 조회 결과

        // 수정할 컬렉션
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        // 기존 수집된 멜론Top100 수집한 컬렉션 삭제
        melonMapper.dropCollection(colNm);

        // 멜론Top100 수집하기
        if (this.collectMelonSong() == 1) {

            // 예: singer 필드에 저장된 '방탄소년단' 값을 'BTS'로 변경
            if (melonMapper.updateAddField(colNm, pDTO) == 1) {

                // 변경된 값을 확인하기 위해 MongoDB로부터 데이터 조회
                rList = melonMapper.getSingerSongNickname(colNm, pDTO); // 수정된 결과 조회
            }
        }

        log.info(this.getClass().getName() + ".updateAddField End!");

        return rList;

    }

    @Override
    public List<MelonDTO> updateAddListField(MelonDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updateAddListField Start!");

        List<MelonDTO> rList = null; // 변경된 데이터 조회 결과

        // 수정할 컬렉션
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        // 기존 수집된 멜론Top100 수집한 컬렉션 삭제
        melonMapper.dropCollection(colNm);

        // 멜론Top100 수집하기
        if (this.collectMelonSong() == 1) {

            // 예: singer 필드에 저장된 '방탄소년단' 값을 'BTS'로 변경
            if (melonMapper.updateAddListField(colNm, pDTO) == 1) {

                // 변경된 값을 확인하기 위해 MongoDB로부터 데이터 조회
                rList = melonMapper.getSingerSongMember(colNm, pDTO); // 수정된 결과 조회
            }
        }

        log.info(this.getClass().getName() + ".updateAddListField End!");

        return rList;

    }


    @Override
    public List<MelonDTO> updateFieldAndAddField(MelonDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updateFieldAndAddField Start!");

        List<MelonDTO> rList = null; // 변경된 데이터 조회 결과

        // 수정할 컬렉션
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        // 기존 수집된 멜론Top100 수집한 컬렉션 삭제
        melonMapper.dropCollection(colNm);

        // 멜론Top100 수집하기
        if (this.collectMelonSong() == 1) {

            // 예: singer 필드에 저장된 '방탄소년단' 값을 'BTS'로 변경
            if (melonMapper.updateFieldAndAddField(colNm, pDTO) == 1) {

                // 변경된 값을 확인하기 위해 MongoDB로부터 데이터 조회
                rList = melonMapper.getSingerSongAddData(colNm, pDTO); // 수정된 결과 조회
            }
        }

        log.info(this.getClass().getName() + ".updateFieldAndAddField End!");

        return rList;

    }


    @Override
    public List<MelonDTO> deleteDocument(MelonDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".deleteDocument Start!");

        List<MelonDTO> rList = null; // 변경된 데이터 조회 결과

        // 삭제할 컬렉션
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        // 기존 수집된 멜론Top100 수집한 컬렉션 삭제
        melonMapper.dropCollection(colNm);

        // 멜론Top100 수집하기
        if (this.collectMelonSong() == 1) {

            // 예: singer 필드에 저장된 '방탄소년단' 값을 'BTS'로 변경
            if (melonMapper.deleteDocument(colNm, pDTO) == 1) {

                // 변경된 값을 확인하기 위해 MongoDB로부터 데이터 조회
                rList = melonMapper.getSongList(colNm); // 수정된 결과 조회
            }
        }

        log.info(this.getClass().getName() + ".deleteDocument End!");

        return rList;
    }





}
