export enum CampaignType {
  TNR= 'TNR', TEST= 'TEST'
}

export const CampaignType2Label: Record<CampaignType, string> = {
  [CampaignType.TNR]: 'CAMPAIGN.LIST.TYPE.TNR',
  [CampaignType.TEST]: 'CAMPAIGN.LIST.TYPE.TEST'
};
