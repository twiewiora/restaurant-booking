import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ForTestsComponent } from './for-tests.component';

describe('ForTestsComponent', () => {
  let component: ForTestsComponent;
  let fixture: ComponentFixture<ForTestsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ForTestsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ForTestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
