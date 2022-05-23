import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { PositionFormComponent } from './position-form/position-form.component';

const routes: Routes = [
  { path: 'posform', component: PositionFormComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }
