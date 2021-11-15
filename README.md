# CS313 Concurrency Team 4 2021

## Part A

### Task 1
#### Basic functionality
- [x] At least three different bank account types.
- [x] Functionality that allows account holders and bank employees to: deposit/withdraw/transfer money, check/print the current balance and account details, create/delete/edit bank accounts.

#### Scenarios
- [ ] Two account holders are trying to check the balance simultaneously.
- [ ] One account holder is trying to check the balance while the other is depositing/withdrawing
money.
- [ ] The two account holders are trying simultaneously to deposit/withdraw money & check the
balance.
- [ ] Same as 3, but at the same time a bank employee is in the process of completing a money transfer in/out the account.
- [ ] There are insufficient funds to complete a withdraw. This is an open-ended scenario and it is up to you to decide what the expected behaviour will be. Ideally (i.e. in order to achieve full marks), your system should implement a mechanism that waits for the balance to grow.
- [ ] Two bank employees are trying simultaneously to modify the details of a bank account


### Task 2

1. #### Basic functionality
Your program must list each thread group and all threads within each group. When outputting each
thread, your program must also list the following information (including some descriptive text):

- [x] The thread name
- [x] The thread identifier
- [x] The state of the thread
- [x] The priority of the thread
- [x] Whether or not the thread is a daemon

2.  #### Bonus marks (optional)
- [x] It periodically refreshes the listing of threads (including their information) and thread groups by
implementing a refresh mechanism.

At least two of these:
- [ ] Search by thread name.
- [ ] Filter by thread group.
- [ ] Start new thread(s).
- [ ] Stop thread(s).
