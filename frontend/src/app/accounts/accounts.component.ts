import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Customer } from '../home/customer';
import { AccountCreatorComponent } from './account-creator/account-creator.component';
import { AccountListComponent } from './account-list/account-list.component';

@Component({
  selector: 'app-accounts',
  standalone: true,
  imports: [CommonModule, AccountCreatorComponent, AccountListComponent],
  templateUrl: './accounts.component.html',
  styleUrl: './accounts.component.scss',
})
export class AccountsComponent {
  @Input() customer: Customer | null = null;
}
