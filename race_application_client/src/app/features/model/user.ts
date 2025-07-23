export interface User {
  id: string;
  username: string;
  role: 'ADMINISTRATOR' | 'APPLICANT';
}