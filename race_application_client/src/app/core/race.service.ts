import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Race } from '../features/model/race';


@Injectable({ providedIn: 'root' })
export class RaceService {
  private queryApiUrl = 'http://localhost:8081/api/races';    // GET
  private commandApiUrl = 'http://localhost:8080/api/races';  // POST, PATCH, DELETE

  constructor(private http: HttpClient) {}

  getAll(): Observable<Race[]> {
    return this.http.get<Race[]>(this.queryApiUrl);
  }

  getOne(id: string): Observable<Race> {
    return this.http.get<Race>(`${this.queryApiUrl}/${id}`);
  }

  create(race: Partial<Race>): Observable<Race> {
    return this.http.post<Race>(this.commandApiUrl, race);
  }

  update(id: string, race: Race): Observable<Race> {
    return this.http.patch<Race>(`${this.commandApiUrl}/${id}`, race);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.commandApiUrl}/${id}`);
  }
}
export { Race };

