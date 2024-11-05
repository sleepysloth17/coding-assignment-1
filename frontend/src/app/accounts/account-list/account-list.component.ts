import { CommonModule } from '@angular/common';
import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Account } from '../account';
import { AccountDetailComponent } from './account-detail/account-detail.component';

@Component({
  selector: 'app-account-list',
  standalone: true,
  imports: [CommonModule, AccountDetailComponent],
  templateUrl: './account-list.component.html',
  styleUrl: './account-list.component.scss',
})
export class AccountListComponent implements OnChanges {
  @Input() public accounts: Account[] = [];

  private _expandedAccount: Account | null = null;

  public ngOnChanges(changes: SimpleChanges) {
    if (changes['accounts']) {
      this._expandedAccount = null;
    }
  }

  public expand(account: Account): void {
    if (!this._expandedAccount || this._expandedAccount.id !== account.id) {
      this._expandedAccount = account;
    }
  }

  public isExpanded(account: Account): boolean {
    return !!this._expandedAccount && this._expandedAccount.id === account.id;
  }
}
