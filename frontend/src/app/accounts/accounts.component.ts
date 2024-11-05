import {
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges,
} from '@angular/core';
import { Account } from './account';
import { AccountCreatorComponent } from './account-creator/account-creator.component';
import { AccountListComponent } from './account-list/account-list.component';
import { AccountService } from './account.service';

@Component({
  selector: 'app-accounts',
  standalone: true,
  imports: [AccountCreatorComponent, AccountListComponent],
  templateUrl: './accounts.component.html',
  styleUrl: './accounts.component.scss',
})
export class AccountsComponent implements OnInit, OnChanges {
  @Input() customerId: string | null = null;

  public accounts: Account[] = [];

  constructor(private _accountService: AccountService) {}

  public ngOnInit(): void {
    this._updateAccounts();
  }

  public ngOnChanges(changes: SimpleChanges) {
    console.log('hello');
    if (changes['customerId'].previousValue !== this.customerId) {
      this._updateAccounts();
    }
  }

  private _updateAccounts(): void {
    if (this.customerId) {
      this._accountService
        .getAccounts(this.customerId)
        .then((accounts: Account[]) => (this.accounts = accounts));
    }
  }
}
