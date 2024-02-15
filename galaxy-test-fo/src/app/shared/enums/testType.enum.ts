export enum TestType {
WS= 'WS', UI= 'UI', SC= 'SC', BA= 'BA'
}

export const Test_type_2Label: Record<TestType, string> = {
  [TestType.WS]: 'TEST.LIST.TYPE.WS',
  [TestType.UI]: 'TEST.LIST.TYPE.UI',
  [TestType.SC]: 'TEST.LIST.TYPE.SC',
  [TestType.BA]: 'TEST.LIST.TYPE.BA',
};
