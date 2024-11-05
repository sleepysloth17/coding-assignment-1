import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Customer } from '../customer';

@Component({
  selector: 'app-customer-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './customer-list.component.html',
  styleUrl: './customer-list.component.scss',
})
export class CustomerListComponent {
  @Input() customers: Customer[] = [];

  private selectedId = '';

  public isSelected(customer: Customer): boolean {
    return customer.id === this.selectedId;
  }

  public selectCustomer(selectedCustomer: Customer): void {
    this.selectedId = selectedCustomer.id;
    console.log('selected', selectedCustomer);
  }
}
