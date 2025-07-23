import { Race } from "./race";

export interface Application {
  id: string;
  raceId: string;
  firstName: string;
  lastName: string;
  club: string;
  race?: Race; // optional za joined data
}