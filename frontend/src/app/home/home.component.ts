import { Component, OnInit } from '@angular/core';
import { take } from 'rxjs';
import { AccountsComponent } from '../accounts/accounts.component';
import { Customer } from './customer';
import { CustomerCreatorComponent } from './customer-creator/customer-creator.component';
import { CustomerListComponent } from './customer-list/customer-list.component';
import { CustomerService } from './customer.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CustomerListComponent, CustomerCreatorComponent, AccountsComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent implements OnInit {
  public customers: Customer[] = [];

  public selectedCustomer: Customer | null = null;

  constructor(private _customerService: CustomerService) {}

  public onCustomerCreation(createdCustomer: Customer): void {
    this.customers.push(createdCustomer);
  }

  public ngOnInit(): void {
    this._customerService
      .getCustomers()
      .pipe(take(1))
      .subscribe((customers: Customer[]) => (this.customers = customers));
  }

  public onCustomerSelection(customer: Customer): void {
    this.selectedCustomer = customer;
  }
}
