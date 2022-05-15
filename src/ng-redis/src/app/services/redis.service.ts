import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from '../../environments/environment'
import { Position } from '../models/position.interface'

@Injectable({
  providedIn: 'root'
})

export class RedisService {
  constructor(
    private http: HttpClient
  ) {}

  getPositions = () => {
    const url = environment.redis_server_url + '/positions';

    return this.http.get<Position[]>(url)
  }
}
