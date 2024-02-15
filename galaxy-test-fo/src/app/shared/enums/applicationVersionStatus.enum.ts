export enum ApplicationVersionStatus {
  ENDED= 'ENDED', OPERATING= 'OPERATING'
}
export const ApplicationVersionStatus2Label: Record<ApplicationVersionStatus, string> = {
  [ApplicationVersionStatus.OPERATING]: 'APPLICATION.LIST.STATUS.OPERATING',
  [ApplicationVersionStatus.ENDED]: 'APPLICATION.LIST.STATUS.ENDED',
};
