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

  constructor(private redis: RedisService) {}

  ngOnInit(): void {
    console.log('[ngOnInit()]...')
    this.redis.getPositions().subscribe(
      response => {
        console.log(response)
      },
      error => {
        console.log(error)
      },
      () => {
        console.log('completed')
      }
    )
  }

  //positions: Observable<Position[]> = this.redis.getPositions()

}
