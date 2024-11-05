import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Account } from '../account';
import { AccountDetailComponent } from './account-detail/account-detail.component';

@Component({
  selector: 'app-account-list',
  standalone: true,
  imports: [CommonModule, AccountDetailComponent],
  templateUrl: './account-list.component.html',
  styleUrl: './account-list.component.scss',
})
export class AccountListComponent {
  @Input() public accounts: Account[] = [];

  public expand(account: Account): void {
    // TODO
    console.log('Expand', account);
  }

  public isExpanded(account: Account): boolean {
    // TODO
    return false;
  }
}
