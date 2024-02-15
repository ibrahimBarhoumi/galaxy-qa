import { DataTestCaseVersion } from "./dataTestCaseVersion";
import { Test } from "./test";

export interface LaunchTest {
    test : Test
    dataTestCaseVersion : DataTestCaseVersion
}