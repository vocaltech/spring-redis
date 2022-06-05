import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Position } from '../models/position.interface';
import { RedisService } from '../services/redis.service';

@Component({
  selector: 'app-position-update-form',
  templateUrl: './position-update-form.component.html',
  styleUrls: ['./position-update-form.component.css']
})

export class PositionUpdateFormComponent implements OnInit {
  position!: Position;

  updateForm = this.formBuilder.group({
    latitude: '',
    longitude: ''
  })

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private redisService: RedisService
  ) {
    // get position object from nav
    this.position = this.router.getCurrentNavigation()?.extras.state?.position

    // update form values
    this.updateForm.setValue({
      latitude: this.position.latitude,
      longitude: this.position.longitude
    })
  }

  ngOnInit(): void {}

  onSubmit = () => {
    const updatedPos: Position = {
      ...this.position, ...this.updateForm.value
    }

    this.redisService.updatePositionById(updatedPos)
    .subscribe(
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

  onCancel = () => {
    this.router.navigateByUrl('')
  }
}
