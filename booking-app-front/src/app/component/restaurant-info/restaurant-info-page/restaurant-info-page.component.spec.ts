import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RestaurantInfoPageComponent } from './restaurant-info-page.component';

describe('RestaurantInfoPageComponent', () => {
  let component: RestaurantInfoPageComponent;
  let fixture: ComponentFixture<RestaurantInfoPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RestaurantInfoPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RestaurantInfoPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
