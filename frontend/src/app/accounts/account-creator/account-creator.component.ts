import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { take } from 'rxjs';
import { Account } from '../account';
import { AccountService } from '../account.service';

@Component({
  selector: 'app-account-creator',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './account-creator.component.html',
  styleUrl: './account-creator.component.scss',
})
export class AccountCreatorComponent {
  private static readonly ERROR_MESSAGE: Readonly<Record<string, string>> = {
    ['required']: 'Initial value is required',
    ['min']: 'Initial value must have a minimum value of 0',
  };

  @Input() customerId: string | null = null;

  @Output() accountCreated: EventEmitter<Account> = new EventEmitter<Account>();

  public formControl: FormControl<number | null>;

  constructor(
    private _accountService: AccountService,
    private _formBuilder: FormBuilder,
  ) {
    this.formControl = this._formBuilder.control(0, [
      Validators.required,
      Validators.min(0),
    ]);
  }

  public hasError(): boolean {
    return this.formControl.touched && this.formControl.invalid;
  }

  public getErrorMessage(): string {
    if (!this.formControl.errors) {
      return '';
    }

    return Object.keys(this.formControl.errors)
      .map((err: string) => AccountCreatorComponent.ERROR_MESSAGE[err])
      .join(', ');
  }

  public submit(): void {
    if (
      this.customerId &&
      this.formControl.valid &&
      this.formControl.value !== null
    ) {
      this._accountService
        .createAccount(this.customerId, this.formControl.value)
        .pipe(take(1))
        .subscribe((createdAccount: Account | null) => {
          if (createdAccount) {
            this.accountCreated.emit(createdAccount);
          }
          this.formControl.reset(0);
        });
    }
  }
}
