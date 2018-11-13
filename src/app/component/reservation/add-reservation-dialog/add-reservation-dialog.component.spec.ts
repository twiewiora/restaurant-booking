import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddReservationDialogComponent } from './add-reservation-dialog.component';

describe('AddReservationDialogComponent', () => {
  let component: AddReservationDialogComponent;
  let fixture: ComponentFixture<AddReservationDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddReservationDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddReservationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
