import { Component, OnInit } from '@angular/core';
import  { FormBuilder } from '@angular/forms'
import { RedisService } from '../services/redis.service';

@Component({
  selector: 'app-position-form',
  templateUrl: './position-form.component.html',
  styleUrls: ['./position-form.component.css']
})

export class PositionFormComponent implements OnInit {
  posForm = this.formBuilder.group({
    latitude: '',
    longitude: ''
  })

  constructor(
    private formBuilder: FormBuilder,
    private redisService: RedisService
  ) {}

  ngOnInit(): void {}

  onSubmit = () => {
    const newPos = {
      ...this.posForm.value,
      time: Math.round(Date.now() / 1000),
      trackId: 'track_1',
      userId: 'user_1'
    }

    // post newPos
    this.redisService.postPosition(newPos).subscribe(
      (response) => {
        console.log(response)
      },
      (error) => {
        console.log(error)
      },
      () => {
        console.log('completed')
      }
    )

    // clear form
    this.posForm.reset()
    
  }
}
