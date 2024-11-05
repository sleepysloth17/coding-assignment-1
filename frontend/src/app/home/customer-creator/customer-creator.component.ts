import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Customer } from '../customer';
import { CustomerService } from '../customer.service';

interface CustomerCreationForm {
  name: FormControl<string>;
  surname: FormControl<string>;
}

@Component({
  selector: 'app-customer-creator',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './customer-creator.component.html',
  styleUrl: './customer-creator.component.scss',
})
export class CustomerCreatorComponent {
  @Output() customerCreated: EventEmitter<Customer> =
    new EventEmitter<Customer>();

  public form: FormGroup<CustomerCreationForm>;

  constructor(
    private _customerService: CustomerService,
    private _formBuilder: NonNullableFormBuilder,
  ) {
    this.form = this._formBuilder.group({
      name: ['', Validators.required],
      surname: ['', Validators.required],
    });
  }

  public hasRequiredError(field: keyof CustomerCreationForm): boolean {
    const control: AbstractControl<string, string> | null =
      this.form.get(field);
    return !!control && control.hasError('required') && control.touched;
  }

  public submit(): void {
    this._customerService
      .createCustomer(
        this.form.get('name')?.value || '',
        this.form.get('surname')?.value || '',
      )
      .then((createdCustomer: Customer) => {
        this.customerCreated.emit(createdCustomer);
        this.form.reset();
      });
  }
}
