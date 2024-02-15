import {ApplicationVersion} from './application-version';
import { Scenario } from './scenario';

export interface ScenarioVersion {
  id?:string;
  applicationVersion?: ApplicationVersion;
  scenario?:Scenario;
}
