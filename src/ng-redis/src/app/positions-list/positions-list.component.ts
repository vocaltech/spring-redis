import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';

import { Position } from '../models/position.interface';
import { RedisService } from '../services/redis.service'

@Component({
  selector: 'app-positions-list',
  templateUrl: './positions-list.component.html',
  styleUrls: ['./positions-list.component.css']
})

export class PositionsListComponent implements OnInit {
  positions$!: Observable<Position[]>;
  
  constructor(private redisService: RedisService) { }

  ngOnInit(): void {
    this.positions$ = this.redisService.getPositions()
  }
}
