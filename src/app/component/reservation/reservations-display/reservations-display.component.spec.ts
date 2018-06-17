import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationsDisplayComponent } from './reservations-display.component';

describe('ReservationsDisplayComponent', () => {
  let component: ReservationsDisplayComponent;
  let fixture: ComponentFixture<ReservationsDisplayComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReservationsDisplayComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReservationsDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
