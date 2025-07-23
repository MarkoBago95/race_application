import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RaceListComponent } from './race-list.component';

describe('RaceListComponent', () => {
  let component: RaceListComponent;
  let fixture: ComponentFixture<RaceListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RaceListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RaceListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
