import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
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
  
  constructor(
    private redisService: RedisService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.positions$ = this.redisService.getPositions()
  }

  onDeletePositionClick = (positionId: string) => {
    const result$: Observable<string> = this.redisService.deletePositionById(positionId)

    result$.subscribe(
      (data) => {
        // refresh list
        this.ngOnInit()
      },
      (error: any) => {
        console.log(error)
      },
      () => {
        console.log('deleteById completed !')
      }
    )
  }

  onRemoveAllClick = () => {
    let result$!: Observable<string>

    result$ = this.redisService.deletePositions()

    result$.subscribe(
      (data: string) => {
        // refresh list
        this.ngOnInit()
      },
      (error: any) => {
        console.log(error)
      },
      () => {
        console.log('deleteAll completed')
      }
    )
  }
}
