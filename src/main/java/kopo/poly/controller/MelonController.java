package kopo.poly.controller;

import kopo.poly.controller.response.CommonResponse;
import kopo.poly.dto.MelonDTO;
import kopo.poly.dto.MsgDTO;
import kopo.poly.service.IMelonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "/melon/v1")
@RequiredArgsConstructor
@RestController
public class MelonController {

    private final IMelonService melonService;

    /* 멜론 노래 리스트 저장하기 */
    @PostMapping(value = "collectMelonSong")
    public ResponseEntity collectMelonSong() throws Exception {

        log.info(this.getClass().getName() + ".collectMelonSong Start!");

        // 수집 결과 출력
        String msg = "";

        int res = melonService.collectMelonSong();

        if (res == 1) {
            msg = "멜론차트 수집 성공";
        } else {
            msg = "멜론차트 수집 실패";
        }

        MsgDTO dto = MsgDTO.builder().result(res).msg(msg).build();

        log.info(this.getClass().getName() + ".collectMelonSong End!");

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), dto));

    }

    /* 오늘 수집된 멜론 노래리스트 가져오기 */
    @PostMapping(value = "getSongList")
    public ResponseEntity getSongList() throws Exception {

        log.info(this.getClass().getName() + ".getSongList Start!");

        List<MelonDTO> rList = Optional.ofNullable(melonService.getSongList())
                .orElseGet(ArrayList::new);

        log.info(this.getClass().getName() + ".getSongList End!");

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rList)
        );
    }

    /* 가수별 수집된 노래의 수 가져오기 */
    @PostMapping(value = "getSingerSongCnt")
    public ResponseEntity getSingerSongCnt() throws Exception {

        log.info(this.getClass().getName() + ".getSingerSongCnt Start!");

        List<MelonDTO> rList = Optional.ofNullable(melonService.getSingerSongCnt())
                .orElseGet(ArrayList::new);

        log.info(this.getClass().getName() + ".getSingerSongCnt End!");

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rList)
        );
    }

    /* 가수 이름으로 조회하기 */
    @PostMapping(value = "getSingerSong")
    public ResponseEntity getSingerSong(@RequestBody MelonDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getSingerSong Start!");

        log.info("pDTO : " + pDTO); // JSON 구조로 받은 값이 잘 받았는지 확인

        List<MelonDTO> rList = Optional.ofNullable(melonService.getSingerSong(pDTO))
                .orElseGet(ArrayList::new);

        log.info(this.getClass().getName() + ".getSingerSong End!");

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rList)
        );
    }

    /* 수집된 멜론 차트 저장된 MongoDB 컬렉션 삭제 */
    @PostMapping(value = "dropCollection")
    public ResponseEntity dropCollection() throws Exception {

        log.info(this.getClass().getName() + ".dropCollection Start!");

        String msg = ""; // 삭제 결과 출력

        int res = melonService.dropCollection();

        if (res == 1) {
            msg = "멜론차트 삭제 성공!";
        } else {
            msg = "멜론차트 삭제 실패!";
        }

        MsgDTO dto = MsgDTO.builder().result(res).msg(msg).build();

        log.info(this.getClass().getName() + ".dropCollection End!");

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), dto)
        );

    }

    /* 멜론 노래 리스트 한번에 저장 */
    @PostMapping(value = "insertManyField")
    public ResponseEntity insertManyField() throws Exception {

        log.info(this.getClass().getName() + ".insertManyField Start!");

        List<MelonDTO> rList = Optional.ofNullable(melonService.insertManyField())
                        .orElseGet(ArrayList::new);

        log.info(this.getClass().getName() + ".insertManyField End!");

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rList)
        );

    }


}