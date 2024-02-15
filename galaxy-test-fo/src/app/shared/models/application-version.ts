import { ApplicationStatus } from '../enums/applicationStatus.enum';
import { Application } from './application';
import { ConfigurationML } from './configurationML';
import { CreatedBy } from './createdBy';
import { Scenario } from './scenario';

export interface ApplicationVersion {
  id?: string;
  code?: string;
  status?: ApplicationStatus;
  host?: string;
  url?: string;
  port?: number;
  username?: string;
  password?: string;
  dbHost?: string;
  dbPort?: number;
  dbUsername?: string;
  dbPassword?: string;
  application?: Application;
  configurationMLS?: ConfigurationML[];
  scenario?: Scenario[];
  createdBy: CreatedBy;
}
