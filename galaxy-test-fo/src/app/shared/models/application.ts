import { ApplicationStatus } from "../enums/applicationStatus.enum";
import { ConfigurationML } from "./configurationML";
import { CreatedBy } from "./createdBy";

export interface Application {
    id: string;
    code: string;
    applicationStatus: ApplicationStatus
    applicationPhase: string ;
    configurationMLS: ConfigurationML[];
    creationDate: Date ;
    createdBy: CreatedBy;
}
