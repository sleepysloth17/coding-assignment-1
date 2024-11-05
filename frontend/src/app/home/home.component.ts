import { Component } from '@angular/core';
import { AccountsComponent } from '../accounts/accounts.component';
import { Customer } from './customer';
import { CustomerCreatorComponent } from './customer-creator/customer-creator.component';
import { CustomerListComponent } from './customer-list/customer-list.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CustomerListComponent, CustomerCreatorComponent, AccountsComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent {
  public customers: Customer[] = [
    { id: 'a', name: 'name-a', surname: 'surname-a' },
    { id: 'b', name: 'name-b', surname: 'surname-b' },
    {
      id: 'c',
      name: 'namecccccccccccccccccccccccc',
      surname: 'surnamecccccccccccccccccccccccccccccccccccccccccccc',
    },
    { id: 'd', name: 'name-d', surname: 'surname-d' },
  ];

  public onCustomerCreation(createdCustomer: Customer): void {
    this.customers.push(createdCustomer);
  }
}
