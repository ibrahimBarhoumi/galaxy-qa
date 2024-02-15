import { User } from "./user";

export interface CreatedBy {
    user: User;
    actionDate: Date;
}