import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Application {
  id?: string;
  firstName: string;
  lastName: string;
  club?: string;
  raceId: string;
}

@Injectable({ providedIn: 'root' })
export class ApplicationService {
  private queryApiUrl = 'http://localhost:8081/api/applications';   // for GET
  private commandApiUrl = 'http://localhost:8080/api/applications'; // for POST/DELETE

  constructor(private http: HttpClient) {}

  create(app: Application): Observable<Application> {
    return this.http.post<Application>(this.commandApiUrl, app);
  }

  getAll(): Observable<Application[]> {
    return this.http.get<Application[]>(this.queryApiUrl);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.commandApiUrl}/${id}`);
  }
}
