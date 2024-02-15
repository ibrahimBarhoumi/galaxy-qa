package fr.lovotech.galaxy.qa.backoffice.core;

import fr.lovotech.galaxy.qa.backoffice.domain.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataTestCaseRequest {
    private static final long serialVersionUID = 1L;

    private ReferenceUID externalUid;
    private Date creationDate;
    private Date endDate;
    private String code;
    private Test test;
    private List<TestParam> values;
    private Integer order;
    private boolean isDefault;
    private List<DataTestCaseVersion> applicationVersionList;



    public DataTestCase buildDataTestCase() {
        return DataTestCase.builder()
                .creationDate(this.creationDate)
                .code(this.code)
                .test(this.test)
                .isDefault(this.isDefault)
                .endDate(this.endDate)
                .build();
    }
}