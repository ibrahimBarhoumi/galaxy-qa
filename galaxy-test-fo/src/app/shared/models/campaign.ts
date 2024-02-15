import { CampaignStatus } from '../enums/campaignStatus.enum';
import { CampaignType } from '../enums/campaignType.enum';
import { ConfigurationML } from './configurationML';
import {Scenario} from './scenario';

export interface Campaign {
  id?: number;
  code?: string;
  campaignType?: CampaignType;
  campaignStatus?: CampaignStatus;
  configurationMLS?: ConfigurationML[];
  scenarioList?: Scenario[];
}
