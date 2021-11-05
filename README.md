# CS313 Concurrency Team 4 2021

## Part A

### Task 1
#### Basic functionality
- [ ] At least three different bank account types.
- [ ] Functionality that allows account holders and bank employees to: deposit/withdraw/transfer money, check/print the current balance and account details, create/delete/edit bank accounts.

#### Scenarios
- [ ] Two account holders are trying to check the balance simultaneously.
- [ ] One account holder is trying to check the balance while the other is depositing/withdrawing
money.
- [ ] The two account holders are trying simultaneously to deposit/withdraw money & check the
balance.
- [ ] Same as 3, but at the same time a bank employee is in the process of completing a money transfer in/out the account.
- [ ] There are insufficient funds to complete a withdraw. This is an open-ended scenario and it is up to you to decide what the expected behaviour will be. Ideally (i.e. in order to achieve full marks), your system should implement a mechanism that waits for the balance to grow.
- [ ] Two bank employees are trying simultaneously to modify the details of a bank account
