package kopo.poly.controller;

import kopo.poly.controller.response.CommonResponse;
import kopo.poly.dto.RedisDTO;
import kopo.poly.service.IMyRedisService;
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
@RequestMapping(value = "/redis/v1")
@RequiredArgsConstructor
@RestController
public class RedisController {

    private final IMyRedisService myRedisService;

    /* Redis 문자열 저장 실습 */
    @PostMapping(value = "saveString")
    public ResponseEntity saveString(@RequestBody RedisDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".saveString Start!");

        log.info("pDTO(html에서 전달 받은 값) : " + pDTO); // 전달 받은 값

        RedisDTO rDTO = Optional.ofNullable(myRedisService.saveString(pDTO))
                .orElseGet(()->RedisDTO.builder().build());

        log.info(this.getClass().getName() + ".saveString End!");

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rDTO)
        );
    }

    @PostMapping(value = "saveStringJSON")
    public ResponseEntity saveStringJSON(@RequestBody RedisDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".saveStringJSON Start!");

        log.info("pDTO(html에서 전달 받은 값) : " + pDTO); // 전달 받은 값

        RedisDTO rDTO = Optional.ofNullable(myRedisService.saveStringJSON(pDTO))
                .orElseGet(()->RedisDTO.builder().build());

        log.info(this.getClass().getName() + ".saveStringJSON End!");

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rDTO)
        );
    }


    @PostMapping(value = "saveList")
    public ResponseEntity saveList(@RequestBody List<RedisDTO> pList) throws Exception {

        log.info(this.getClass().getName() + ".saveList Start!");

        log.info("pList(html에서 전달 받은 값) : " + pList); // 전달 받은 값

        List<String> rList = Optional.ofNullable(myRedisService.saveList(pList))
                .orElseGet(ArrayList::new);

        log.info(this.getClass().getName() + ".saveList End!");

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rList)
        );
    }

    @PostMapping(value = "saveListJSON")
    public ResponseEntity saveListJSON(@RequestBody List<RedisDTO> pList) throws Exception {

        log.info(this.getClass().getName() + ".saveListJSON Start!");

        log.info("pList(html에서 전달 받은 값) : " + pList); // 전달 받은 값

        List<RedisDTO> rList = Optional.ofNullable(myRedisService.saveListJSON(pList))
                .orElseGet(ArrayList::new);

        log.info(this.getClass().getName() + ".saveListJSON End!");

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rList)
        );
    }

    @PostMapping(value = "saveHash")
    public ResponseEntity saveHash(@RequestBody RedisDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".saveHash Start!");

        log.info("pDTO(html에서 전달 받은 값) : " + pDTO); // 전달 받은 값

        RedisDTO rDTO = Optional.ofNullable(myRedisService.saveHash(pDTO))
                .orElseGet(() -> RedisDTO.builder().build());

        log.info(this.getClass().getName() + ".saveHash End!");

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rDTO)
        );
    }

}
