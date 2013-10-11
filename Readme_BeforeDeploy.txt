Steps before deploying the application to production
==========================================================
- remove Test servlet

- setup bitcoind - set username, password, allowed listening addresses if in cluster
  - generate pool of bitcoin addresses (ca 10000) and put them into the configuration - you can do it with BitcoinTest.generateAccounts()

- when deploying to a cluster
	- always use multi-node cluster implementation for LockProvider
	- reimplement BalanceService - the substracting of the balance must be replicated through the cluster
	 
- create account on BitStamp
	 - add some money to it
	 - get verified
	 - disable sending confirming e-mails
- setup SMS and e-mail for logging
- configure correctly Unicredit connector
- when deploying to a cluster reimplement Timer in trader and unicredit-connector
- turn off recreating database schema. Turn off echoing SQL to log.
- setup PostgreSQL 9.1 - do not forget to setup its memory, security and maybe another important things (easy documentation: https://help.ubuntu.com/community/PostgreSQL, create password for postgres linux user and start pgadmin3 as this user)

- add better CountryDirectory implementation - the JavaCountryDirectory does not contain all countries!