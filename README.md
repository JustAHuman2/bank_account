# CodingChallenge

Thank you for considering the Code Factory as your potential next employer. The coding challenge gives you an impression 
of our tech stack and the tasks you would work on when joining the Code Factory.

## Prerequisites

There are bank customers with the following account types:
- Checking account (functions as a reference account for loan and saving accounts)
- Savings account (to separate money from daily expenses, has a slightly higher interest rate)
- Loan account

The accounts have the following constraints:
- Checking account: transferring money from and to any account is possible
- Savings account: receiving money from any account is possible, but transferring is only allowed to the reference 
- account (checking account)
- Loan account: receiving money from any account is possible, but transferring is prohibited

## The task

Your team is responsible for a service which allows customers to see their account data and the corresponding 
transactions, and to initiate payments/ money transfers to other accounts.

You have taken over the work of a colleague who already started implementing the read-only part, meaning providing 
endpoints to retrieve account and transaction data. What’s missing is an endpoint which allows customers to initiate 
payments to other accounts.

Your task is to add the missing endpoint and, additionally, get the service in shape, so you feel comfortable with it.

## Notes

If you don’t manage to implement everything you'd like to, please write it down with a reason why you think it’s 
important/ necessary.

The service is a SpringBoot application written in Kotlin and built with Gradle.

## Deliverables

Your solution should be either a ZIP archive containing the GIT commit history, or a link to a Github repository.

## Hints

Build the project: `./gradlew build`

Start the project: `./gradlew bootRun`

Further information you can find in the `HELP.md`.

Have fun with our coding challenge. If you have any questions don’t hesitate to ask. 