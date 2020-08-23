package com.funcar.data.processor;

import com.funcar.persistence.entity.Vehicle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.funcar.enums.CSVHeader.CODE;
import static com.funcar.enums.CSVHeader.COLOR;
import static com.funcar.enums.CSVHeader.MAKE_MODEL;
import static com.funcar.enums.CSVHeader.POWER_IN_PS;
import static com.funcar.enums.CSVHeader.PRICE;
import static com.funcar.enums.CSVHeader.YEAR;

@Slf4j
public class CSVProcessor {

    private static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT.withHeader(CODE.getHeaderName(), MAKE_MODEL.getHeaderName(),
            POWER_IN_PS.getHeaderName(), YEAR.getHeaderName(), COLOR.getHeaderName(), PRICE.getHeaderName());

    public List<Vehicle> process(MultipartFile multipartFile, String dealerId) {

        try {
            Reader reader = new InputStreamReader(multipartFile.getInputStream());
            CSVParser csvRecords = CSV_FORMAT.parse(reader);
            return csvRecords.getRecords().stream().skip(1).map(this::mapToObj).filter(Objects::nonNull).collect(Collectors.toList());

        } catch (IOException ex) {
            throw new RuntimeException(String.format("Failed to parse csv file for dealerId=%s", dealerId), ex);
        }
    }

    private Vehicle mapToObj(CSVRecord csvRecord) {

        if (!csvRecord.isConsistent()) {
            log.info("Incorrect csv record found for code={}",csvRecord.get(CODE.getHeaderName()));
            return null;
        }

        String[] makeAndModel = csvRecord.isSet(MAKE_MODEL.getHeaderName()) ? csvRecord.get(MAKE_MODEL.getHeaderName()).split("/") : new String[]{null, null};
        String code = csvRecord.isSet(CODE.getHeaderName()) ? csvRecord.get(CODE.getHeaderName()) : null;
        BigDecimal price = csvRecord.isSet(PRICE.getHeaderName()) ? BigDecimal.valueOf(Double.valueOf(csvRecord.get(PRICE.getHeaderName()))) : null;
        Integer year = csvRecord.isSet(YEAR.getHeaderName()) ? Integer.valueOf(csvRecord.get(YEAR.getHeaderName())) : null;
        String color = csvRecord.isSet(COLOR.getHeaderName()) ? csvRecord.get(COLOR.getHeaderName()) : null;
        Double powerinPs = csvRecord.isSet(POWER_IN_PS.getHeaderName()) ? Double.valueOf(csvRecord.get(POWER_IN_PS.getHeaderName())) : null;

        return new Vehicle().setCode(code)
                .setPrice(price)
                .setMake(makeAndModel[0])
                .setModel(makeAndModel[1])
                .setYear(year)
                .setColor(color)
                .setKW(convertPstoKw(powerinPs));
    }

    private int convertPstoKw(Double psPower) {
        return (int) Math.floor(0.735499 * psPower);
    }
}
