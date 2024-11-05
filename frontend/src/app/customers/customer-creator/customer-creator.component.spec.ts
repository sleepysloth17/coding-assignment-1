import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerCreatorComponent } from './customer-creator.component';

describe('CustomerCreatorComponent', () => {
  let component: CustomerCreatorComponent;
  let fixture: ComponentFixture<CustomerCreatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CustomerCreatorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CustomerCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
