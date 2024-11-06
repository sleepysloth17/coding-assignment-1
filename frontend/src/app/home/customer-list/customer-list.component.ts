import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
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

  @Output() customerSelected: EventEmitter<Customer> =
    new EventEmitter<Customer>();

  private selectedId = '';

  public isSelected(customer: Customer): boolean {
    return customer.id === this.selectedId;
  }

  public selectCustomer(selectedCustomer: Customer): void {
    this.selectedId = selectedCustomer.id;
    this.customerSelected.emit(selectedCustomer);
  }
}
