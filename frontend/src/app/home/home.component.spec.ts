import { ComponentFixture, TestBed } from '@angular/core/testing';

import { of } from 'rxjs';
import { TestingModule } from '../../testing.module.spec';
import { CustomerService } from './customer.service';
import { HomeComponent } from './home.component';

describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;

  let customerService: jasmine.SpyObj<CustomerService>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HomeComponent, TestingModule],
    }).compileComponents();

    customerService = TestBed.inject(
      CustomerService,
    ) as jasmine.SpyObj<CustomerService>;
    customerService.getCustomers.and.returnValue(of());

    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
