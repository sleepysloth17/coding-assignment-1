import { Injectable } from '@angular/core';
import { Customer } from './customer';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  // constructor() {}

  public createCustomer(name: string, surname: string): Promise<Customer> {
    return Promise.resolve({ id: '', name, surname }); // TODO
  }
}
