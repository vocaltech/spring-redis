import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { PositionAddFormComponent } from './position-add-form/position-add-form.component';
import { PositionUpdateFormComponent } from './position-update-form/position-update-form.component';
import { PositionsListComponent } from './positions-list/positions-list.component';

const routes: Routes = [
  { path: '', component: PositionsListComponent },
  { path: 'addform', component: PositionAddFormComponent },
  { path: 'updateform', component: PositionUpdateFormComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { onSameUrlNavigation: 'reload' })],
  exports: [RouterModule]
})

export class AppRoutingModule { }
