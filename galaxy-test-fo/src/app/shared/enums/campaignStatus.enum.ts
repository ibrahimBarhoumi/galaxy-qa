export enum CampaignStatus {
  BLOCKED= 'BLOCKED', ACTIVATED= 'ACTIVATED'
}

export const CampaignStatus2Label: Record<CampaignStatus, string> = {
  [CampaignStatus.ACTIVATED]: 'CAMPAIGN.LIST.STATUS.ACTIVATED',
  [CampaignStatus.BLOCKED]: 'CAMPAIGN.LIST.STATUS.BLOCKED',
};
