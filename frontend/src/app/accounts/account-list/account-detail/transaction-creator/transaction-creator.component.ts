import { CommonModule } from '@angular/common';
import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { take } from 'rxjs';
import { Account } from '../../../account';
import { Transaction } from '../transaction';
import { TransactionService } from '../transaction.service';

@Component({
  selector: 'app-transaction-creator',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './transaction-creator.component.html',
  styleUrl: './transaction-creator.component.scss',
})
export class TransactionCreatorComponent implements OnChanges {
  private static readonly ERROR_MESSAGE: Readonly<Record<string, string>> = {
    ['required']: 'Amount is required',
    ['balanceTooSmall']: 'Total withdrawn must be less than account balance',
  };

  @Input() account: Account | null = null;

  public formControl: FormControl<number | null>;

  constructor(
    private _transactionService: TransactionService,
    private _formBuilder: FormBuilder,
  ) {
    this.formControl = this._formBuilder.control(0, [
      Validators.required,
      this.createMinTransactionValidator(this.account),
    ]);
  }

  public ngOnChanges(changes: SimpleChanges): void {
    if (changes['account']) {
      this.formControl.setValidators([
        Validators.required,
        this.createMinTransactionValidator(this.account),
      ]);
    }
  }

  public createMinTransactionValidator(account: Account | null): ValidatorFn {
    return (control: AbstractControl<number>): ValidationErrors | null => {
      const value: number = control.value;

      if (!account || !value || value >= 0) {
        return null;
      }

      return Math.abs(value) > account?.balance
        ? { balanceTooSmall: true }
        : null;
    };
  }

  public hasError(): boolean {
    return this.formControl.touched && this.formControl.invalid;
  }

  public getErrorMessage(): string {
    if (!this.formControl.errors) {
      return '';
    }

    return Object.keys(this.formControl.errors)
      .map((err: string) => TransactionCreatorComponent.ERROR_MESSAGE[err])
      .join(', ');
  }

  public submit(): void {
    if (
      this.account &&
      this.formControl.valid &&
      this.formControl.value !== null
    ) {
      this._transactionService
        .createTransaction(this.account.id, this.formControl.value)
        .pipe(take(1))
        .subscribe((createdTransaction: Transaction | null) => {
          if (this.account && createdTransaction) {
            this.account.transactions.push(createdTransaction);
            this.account.balance += createdTransaction.amount;
            this.formControl.reset(0);
          }
        });
    }
  }
}
