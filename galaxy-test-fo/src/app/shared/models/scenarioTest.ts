import { Scenario } from "./scenario";
import { Test } from "./test";

export interface ScenarioTest{
order: number
beforeTest: Test
test: Test
scenario: Scenario    
}