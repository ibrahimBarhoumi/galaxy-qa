import { ApplicationVersion } from './application-version';
import { DataTestCaseVersion } from './dataTestCaseVersion';
import { Test } from './test';
import { TestParam } from './TestParam';

export interface DataTestCase{
  code?:string;
  id?:string;
  test?:Test;
  order?:number;
  isDefault?:boolean;
  applicationVersionList?:DataTestCaseVersion[];
  values?:TestParam[];
}
