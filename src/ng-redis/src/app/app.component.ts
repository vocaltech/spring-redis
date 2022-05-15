import { Component } from '@angular/core';

import { RedisService } from './services/redis.service'
import { Position } from './models/position.interface';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  title = 'ng-redis';

  constructor(private redis: RedisService) {}

  positions: Observable<Position[]> = this.redis.getPositions()
}
