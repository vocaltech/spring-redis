import { Component, OnInit } from '@angular/core';

import { RedisService } from './services/redis.service'
import { Position } from './models/position.interface';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {
  title = 'ng-redis';
  positions$!: Observable<Position[]>;

  constructor(private redis: RedisService) {}

  ngOnInit(): void {
    this.positions$ = this.redis.getPositions();
  }
}
