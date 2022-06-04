import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Position } from '../models/position.interface';

@Component({
  selector: 'app-position-update-form',
  templateUrl: './position-update-form.component.html',
  styleUrls: ['./position-update-form.component.css']
})

export class PositionUpdateFormComponent implements OnInit {
  position!: Position;

  constructor(private router: Router) {
    this.position = this.router.getCurrentNavigation()?.extras.state?.position
  }

  ngOnInit(): void {}
}
