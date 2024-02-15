package fr.lovotech.galaxy.qa.backoffice.exception;

public interface DomainErrorCodes {

    String UITEST_NOT_FOUND = "UITest_NOT_FOUND";
    String SCENARIO_NOT_FOUND = "SCENARIO_NOT_FOUND";
    String FILE_NOT_FOUND = "FILE_NOT_FOUND";
    String APPLICATION_NOT_FOUND = "APPLICATION_NOT_FOUND";
    String ENVIRONMENT_NOT_FOUND = "ENVIRONMENT_NOT_FOUND";
    String TESTCASE_NOT_FOUND = "TESTCASE_NOT_FOUND";
    String TEST_NOT_FOUND = "TEST_NOT_FOUND";
    String CAMPAIGN_TEST_NOT_FOUND = "COMPAIGN_TEST_NOT_FOUND";
    String APPLICATION_VERSION_NOT_FOUND = "APPLICATION_VERSION_NOT_FOUND";
    String APPLICATION_CODE_EXISTS = "APPLICATION_CODE_EXISTS";
    String APPLICATION_CODE_DELETED = "APPLICATION_CODE_DELETED";
    String CAMPAIGN_TEST_CODE_EXISTS = "CAMPAIGN_TEST_CODE_EXISTS";
    String CAMPAIGN_TEST_CODE_DELETED = "CAMPAIGN_TEST_CODE_DELETED";
    String APPLICATIONVERSION_EXIST="APPLICATIONVERSION_EXIST";
    String APPLICATIONVERSION_EXIST_DELETED="APPLICATIONVERSION_EXIST_DELETED";
    String APPLICATION_HAS_VERSIONS="APPLICATION_HAS_VERSIONS";
    String TEST_CODE_EXISTS="TEST_CODE_EXISTS";
    String TEST_CODE_DELETED="TEST_CODE_DELETED";
    String SCENARIO_VERSION_NOT_FOUND = "SCENARIO_VERSION_NOT_FOUND";
    String VERSION_HAS_SCENARIOS="VERSION_HAS_SCENARIOS";
    String SCENARIO_HAS_VERSIONS="SCENARIO_HAS_VERSIONS";
    String SCENARIO_TEST_NOT_FOUND="SCENARIO_TEST_NOT_FOUND";
    String CAMPAIGN_HAS_SCENARIO="CAMPAIGN_HAS_SCENARIO";
    String VERSION_MUST_EXSIT="VERSION_MUST_EXSIT";
    String SCENARIO_EXIST_IN_CAMPAIGN="SCENARIO_EXIST_IN_CAMPAIGN";
    String SCENARIO_TEST_CODE_EXISTS = "SCENARIO_TEST_CODE_EXISTS";
    String SCENARIO_TEST_CODE_DELETED = "SCENARIO_TEST_CODE_DELETED";
    String TEST_OWNED_BY_SCENARIO = "TEST_OWNED_BY_SCENARIO";
    String DATA_TEST_CASE_EXISTS = "DATA_TEST_CASE_EXISTS";
    String DATA_TEST_CASE_DELETED = "DATA_TEST_CASE_DELETED";
}
