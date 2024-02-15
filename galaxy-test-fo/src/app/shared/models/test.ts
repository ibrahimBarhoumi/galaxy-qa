import { TestStatus } from '../enums/testStatus.enum';
import { TestType } from '../enums/testType.enum';
import { Application } from './application';
import { ConfigurationML } from './configurationML';
import { TestVersion } from './testVersion';

export interface Test {
    id?: string;
    code?: string;
    type?: TestType;
    status?: TestStatus;
    application?: Application;
    testVersions?:TestVersion[];
    configurationMLS?: ConfigurationML[];
}
