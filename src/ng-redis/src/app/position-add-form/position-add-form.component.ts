import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';

import { RedisService } from '../services/redis.service';

@Component({
  selector: 'app-position-add-form',
  templateUrl: './position-add-form.component.html',
  styleUrls: ['./position-add-form.component.css']
})

export class PositionAddFormComponent implements OnInit {
  addForm = this.formBuilder.group({
    latitude: '',
    longitude: ''
  })

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private redisService: RedisService
  ) { }

  ngOnInit(): void {}

  onCancel = () => {
    this.router.navigateByUrl('')
  }

  onSubmit = () => {
    const newPos = {
      ...this.addForm.value,
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

        // redirection
        this.router.navigateByUrl('')
      }
    )    
  }
}
