import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http'

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ReactiveFormsModule } from '@angular/forms';
import { PositionFormComponent } from './position-form/position-form.component';
import { PositionsListComponent } from './positions-list/positions-list.component';
import { PositionUpdateFormComponent } from './position-update-form/position-update-form.component';

@NgModule({
  declarations: [
    AppComponent,
    PositionFormComponent,
    PositionsListComponent,
    PositionUpdateFormComponent
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
