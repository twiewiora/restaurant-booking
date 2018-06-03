import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OpenHoursComponent } from './open-hours.component';

describe('OpenHoursComponent', () => {
  let component: OpenHoursComponent;
  let fixture: ComponentFixture<OpenHoursComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OpenHoursComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OpenHoursComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
