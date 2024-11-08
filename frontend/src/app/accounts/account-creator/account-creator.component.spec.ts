import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TestingModule } from '../../../testing.module.spec';
import { AccountCreatorComponent } from './account-creator.component';

describe('AccountCreatorComponent', () => {
  let component: AccountCreatorComponent;
  let fixture: ComponentFixture<AccountCreatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountCreatorComponent, TestingModule],
    }).compileComponents();

    fixture = TestBed.createComponent(AccountCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
