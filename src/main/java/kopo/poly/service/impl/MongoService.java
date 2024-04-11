package kopo.poly.service.impl;

import kopo.poly.dto.MongoDTO;
import kopo.poly.persistance.mongodb.IMongoMapper;
import kopo.poly.service.IMongoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MongoService implements IMongoService {

    private final IMongoMapper mongoMapper; // MongoDB에 저장할 Mapper

    @Override
    public int mongoTest(MongoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".mongoTest Start!");

        // 생성할 컬렉션명
        String colNm = "MONGODB_TEST";

        // 몽고디비에 데이터 저장하기
        int res = mongoMapper.insertData(pDTO, colNm);

        log.info(this.getClass().getName() + ".mongoTest End!");

        return res;
    }
}
