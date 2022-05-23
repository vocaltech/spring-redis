import { Component, OnInit } from '@angular/core';
import  { FormBuilder } from '@angular/forms'

@Component({
  selector: 'app-position-form',
  templateUrl: './position-form.component.html',
  styleUrls: ['./position-form.component.css']
})

export class PositionFormComponent implements OnInit {

  posForm = this.formBuilder.group({
    lat: '',
    lng: ''
  })

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit(): void {}

  onSubmit = () => {
    console.log(this.posForm.value)
  }

}
