Steps before deploying the application to production
==========================================================
- remove Test servlet

- setup bitcoind - set username, password, allowed listening addresses if in cluster
  - generate pool of bitcoin addresses (ca 10000) and put them into database - you can do it with BitcoinTest.generateAccounts()

- when deploying to a cluster
	- always use multi-node cluster implementation for LockProvider
	- change Quartz configuration to use clustered DB job store instead of RamJobStore
	- reimplement Timer in trader and unicredit-connector
	- store the variable BitstampAutoTraderPrefferingUSD.lastUsdWithdrawal to DB to be available and actual to all nodes
	 
- create account on BitStamp
	 - add some money to it
	 - get verified
	 - disable sending confirming e-mails
- setup SMS and e-mail for logging
- configure correctly Unicredit connector
- turn off recreating database schema. Turn off echoing SQL to log.
- setup PostgreSQL 9.1 - do not forget to setup its memory, security and maybe another important things (easy documentation: https://help.ubuntu.com/community/PostgreSQL, create password for postgres linux user and start pgadmin3 as this user)

- add better CountryDirectory implementation - the JavaCountryDirectory does not contain all countries!

- set actual balance into table BALANCE - into row with ID=1
