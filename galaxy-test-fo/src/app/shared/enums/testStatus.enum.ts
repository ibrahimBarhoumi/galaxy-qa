export enum TestStatus {
  UDEV= 'UDEV', REA= 'REA', CAN= 'CAN'
}

export const Test_status_2Label: Record<TestStatus, string> = {
  [TestStatus.UDEV]: 'TEST.LIST.STATUS.UDEV',
  [TestStatus.REA]: 'TEST.LIST.STATUS.REA',
  [TestStatus.CAN]: 'TEST.LIST.STATUS.CAN',
};
