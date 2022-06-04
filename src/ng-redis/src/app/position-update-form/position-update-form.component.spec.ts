import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PositionUpdateFormComponent } from './position-update-form.component';

describe('PositionUpdateFormComponent', () => {
  let component: PositionUpdateFormComponent;
  let fixture: ComponentFixture<PositionUpdateFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PositionUpdateFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PositionUpdateFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
