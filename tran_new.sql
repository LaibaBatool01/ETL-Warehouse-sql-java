USE TransactionMasterData;
select * from transactions;
UPDATE transactions
SET `Order Date` = STR_TO_DATE(`Order Date`, '%m/%d/%Y %H:%i')
WHERE STR_TO_DATE(`Order Date`, '%m/%d/%Y %H:%i') IS NOT NULL;

ALTER TABLE transactions
CHANGE COLUMN `Order Date` `Order Date` DATETIME;
