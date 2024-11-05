import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Transaction } from '../transaction';
import { TransactionService } from '../transaction.service';

@Component({
  selector: 'app-transaction-creator',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './transaction-creator.component.html',
  styleUrl: './transaction-creator.component.scss',
})
export class TransactionCreatorComponent {
  @Input() accountId: string | null = null;

  @Output() transactionCreated: EventEmitter<Transaction> =
    new EventEmitter<Transaction>();

  public formControl: FormControl<number | null>;

  constructor(
    private _transactionService: TransactionService,
    private _formBuilder: FormBuilder,
  ) {
    this.formControl = this._formBuilder.control(0, Validators.required);
  }

  public hasRequiredError(): boolean {
    return this.formControl.value === null && this.formControl.touched;
  }

  public submit(): void {
    if (
      this.accountId &&
      (this.formControl.value || this.formControl.value === 0)
    ) {
      this._transactionService
        .createTransaction(this.accountId, this.formControl.value)
        .then((createdTransaction: Transaction) => {
          this.transactionCreated.emit(createdTransaction);
          this.formControl.reset();
        });
    }
  }
}
