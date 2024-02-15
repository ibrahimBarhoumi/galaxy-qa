export enum ScenarioStatus {
    BLK= 'BLK', ACT= 'ACT'
  }
  
  export const ScenarioStatus2Label: Record<ScenarioStatus, string> = {
    [ScenarioStatus.ACT]: 'SCENARIO.LIST.STATUS.ACTIVATED',
    [ScenarioStatus.BLK]: 'SCENARIO.LIST.STATUS.BLOCKED',
  };
  