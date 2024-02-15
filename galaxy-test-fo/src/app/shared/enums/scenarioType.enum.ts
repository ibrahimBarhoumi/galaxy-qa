export enum ScenarioType {
    TNR= 'TNR', TEST= 'TEST'
  }
  
  export const ScenarioType2Label: Record<ScenarioType, string> = {
    [ScenarioType.TNR]: 'CAMPAIGN.LIST.TYPE.TNR',
    [ScenarioType.TEST]: 'SCENARIO.LIST.TYPE.TEST'
  };
  