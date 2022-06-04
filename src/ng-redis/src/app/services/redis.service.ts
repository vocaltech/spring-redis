import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment'
import { Position } from '../models/position.interface'

@Injectable({
  providedIn: 'root'
})

export class RedisService {
  baseUrl = environment.redis_server_url + '/api/positions';

  constructor(
    private http: HttpClient
  ) {}

  getPositions = (): Observable<Position[]> => {
    return this.http.get<Position[]>(this.baseUrl)
  }

  postPosition = (position: Position): Observable<Position> => {
    return this.http.post<Position>(this.baseUrl, position)
  }

  deletePositions = (): Observable<any> => {
    return this.http.delete(this.baseUrl);
  }

  deletePositionById = (positionId: string) => {
    const url = this.baseUrl + '/' + positionId
    return this.http.delete<string>(url)
  }

  updatePositionById = (position: Position): Observable<Position> => {
    const url = this.baseUrl + '/' + position.id;
    return this.http.put<Position>(url, position)
  }
}
