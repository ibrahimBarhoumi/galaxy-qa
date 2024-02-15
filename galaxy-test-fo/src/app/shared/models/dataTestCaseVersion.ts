import { ApplicationVersion } from "./application-version";
import { DataTestCase } from "./dataTestCase";
import { FileInfo } from "./FileInfo";
import { Test } from "./test";

export interface DataTestCaseVersion{
 applicationVersion?:ApplicationVersion;
 dataTestCase?:DataTestCase;
 fileInfo?:FileInfo;
}
