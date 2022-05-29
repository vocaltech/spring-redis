import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { PositionFormComponent } from './position-form/position-form.component';
import { PositionsListComponent } from './positions-list/positions-list.component';

const routes: Routes = [
  { path: '', component: PositionsListComponent },
  { path: 'posform', component: PositionFormComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { onSameUrlNavigation: 'reload' })],
  exports: [RouterModule]
})

export class AppRoutingModule { }
