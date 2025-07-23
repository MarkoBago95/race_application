// src/app/shared/models/race.model.ts
export interface Race {
  id: string;
  name: string;
  distance: 'FiveK' | 'TenK' | 'HalfMarathon' | 'Marathon';
}
