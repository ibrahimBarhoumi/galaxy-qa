import {ScenarioStatus} from '../enums/scenarioStatus.enum';
import {ScenarioType} from '../enums/scenarioType.enum';
import {Application} from './application';
import {ConfigurationML} from './configurationML';
import {ScenarioTest} from './scenarioTest';
import {ScenarioVersion} from './scenarioVersion';

export interface Scenario {
  id?: string;
  code?: string;
  scenarioType?: ScenarioType;
  scenarioStatus?: ScenarioStatus;
  configurationMLS?: ConfigurationML[];
  scenarioTests?: ScenarioTest[];
  application: Application;
  scenarioVersions: ScenarioVersion[];
}
