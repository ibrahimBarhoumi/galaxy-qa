export enum ApplicationStatus {
  ENDED= 'ENDED', OPERATING= 'OPERATING'
}
export const ApplicationStatus2Label: Record<ApplicationStatus, string> = {
  [ApplicationStatus.OPERATING]: 'APPLICATION.LIST.STATUS.OPERATING',
  [ApplicationStatus.ENDED]: 'APPLICATION.LIST.STATUS.ENDED',
};
