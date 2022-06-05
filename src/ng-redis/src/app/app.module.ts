import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http'

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ReactiveFormsModule } from '@angular/forms';

import { PositionsListComponent } from './positions-list/positions-list.component';
import { PositionUpdateFormComponent } from './position-update-form/position-update-form.component';
import { PositionAddFormComponent } from './position-add-form/position-add-form.component';

@NgModule({
  declarations: [
    AppComponent,
    PositionsListComponent,
    PositionUpdateFormComponent,
    PositionAddFormComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
