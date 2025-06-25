USE ELECTRONICA_DW;

#  1 
select
    sd.supplierName,
    YEAR(dd.DateID) as transactionYear,
    QUARTER(dd.DateID) as transactionQuarter,
    MONTH(dd.DateID) as transactionMonth,
    SUM(fs.TotalSale) as totalSales
from
    SupplierDimension sd
inner join
    ProductDimension pd on sd.SupplierID = pd.SupplierID
inner join
    FactSales fs on pd.ProductID = fs.ProductID
inner join
    DateDimension dd on fs.DateID = dd.DateID
group by
    sd.supplierName, transactionYear, transactionQuarter, transactionMonth
order by
    sd.supplierName, transactionYear, transactionQuarter, transactionMonth;



# 2


select
    P.productName as productName,
    DATE_FORMAT(D.DateID, '%Y-%m') as transactionMonth,
    SUM(F.TotalSale) as totalSales
from
    FactSales F
join
    ProductDimension P on F.ProductID = P.ProductID
join
    SupplierDimension S on P.supplierID = S.SupplierID
join
    DateDimension D on F.DateID = D.DateID
where
    S.supplierName = 'DJI' and YEAR(D.DateID) = 2019
group by
    P.productName, transactionMonth

union

select
    'Total' as productName,
    DATE_FORMAT(D.DateID, '%Y-%m') as transactionMonth,
    SUM(F.TotalSale) as totalSales
from
    FactSales F
join
    ProductDimension P on F.ProductID = P.ProductID
join
    SupplierDimension S on P.supplierID = S.SupplierID
join
    DateDimension D on F.DateID = D.DateID
where
    S.supplierName = 'DJI' and YEAR(D.DateID) = 2019
group by
    transactionMonth

order by
    productName, transactionMonth;


# 3


select
    p.productName,
    SUM(f.QuantityOrdered) as totalQuantitySold
from
    FactSales f
join
    ProductDimension p on f.ProductID = p.ProductID
join
    DateDimension d on f.DateID = d.DateID
where
    d.d_Year = 2019 and DAYOFWEEK(d.DateID) in (1, 7)
group by
    p.productName
order by
    totalQuantitySold DESC
limit 5;

# 4

select
    p.productName,
    SUM(CASE WHEN MONTH(d.DateID) between 1 and 3 THEN f.TotalSale ELSE 0 END) as Q1_Sales,
    SUM(CASE WHEN MONTH(d.DateID) between 4 and 6 THEN f.TotalSale ELSE 0 END) as Q2_Sales,
    SUM(CASE WHEN MONTH(d.DateID) between 7 and 9 THEN f.TotalSale ELSE 0 END) as Q3_Sales,
    SUM(CASE WHEN MONTH(d.DateID) between 10 and 12 THEN f.TotalSale ELSE 0 END) as Q4_Sales,
    SUM(f.TotalSale) as Total_Yearly_Sales
from
    FactSales f
join
    ProductDimension p on f.ProductID = p.ProductID
join
    DateDimension d on f.DateID = d.DateID
where
    d.d_Year = 2019
group by
    p.productName
order by
    p.productName;

# 5
select
    DateID,
    YEAR(DateID) as extractedYear,
    d_Year,
    d_Month,
    d_Day
from
    DateDimension
where
    YEAR(DateID) < 1900;

# 6
drop view if exists STOREANALYSIS_MV;
drop table if exists STOREANALYSIS_MV;
drop table if exists STOREANALYSIS_MV1;
drop event if exists update_STOREANALYSIS_MV1;

create view STOREANALYSIS_MV as
select
    f.StoreID as STORE_ID,
    f.ProductID as PROD_ID,
    SUM(f.TotalSale) as STORE_TOTAL
from
    FactSales f
group by
    f.StoreID, f.ProductID;

create table STOREANALYSIS_MV1 (
    STORE_ID INT,
    PROD_ID INT,
    STORE_TOTAL DECIMAL(10,2)
);

insert into STOREANALYSIS_MV1 (STORE_ID, PROD_ID, STORE_TOTAL)
select
    f.StoreID,
    f.ProductID,
    SUM(f.TotalSale)
from
    FactSales f
group by
    f.StoreID, f.ProductID;

delimiter //

create event update_STOREANALYSIS_MV1
on schedule every 1 day
do
begin
    truncate table STOREANALYSIS_MV1;
    insert into STOREANALYSIS_MV1 (STORE_ID, PROD_ID, STORE_TOTAL)
    select
        f.StoreID,
        f.ProductID,
        SUM(f.TotalSale)
    from
        FactSales f
    group by
        f.StoreID, f.ProductID;
end;

//

delimiter ;
select * from STOREANALYSIS_MV;

# 7
select
    pd.productName,
    dd.d_Year,
    dd.d_Month,
    SUM(fs.TotalSale) as totalSales
from
    FactSales fs
join
    StoreDimension sd on fs.StoreID = sd.StoreID
join
    ProductDimension pd on fs.ProductID = pd.ProductID
join
    DateDimension dd on fs.DateID = dd.DateID
where
    sd.storeName = 'Tech Haven'
group by
    pd.productName, dd.d_Year, dd.d_Month
order by
    pd.productName, dd.d_Year, dd.d_Month;

#8
drop view if exists SUPPLIER_PERFORMANCE_VIEW;
drop table if exists SUPPLIER_PERFORMANCE_MV;

create view SUPPLIER_PERFORMANCE_VIEW as
select
    sd.SupplierID,
    sd.supplierName,
    dd.d_Year,
    dd.d_Month,
    COUNT(DISTINCT fs.OrderID) as NumberOfOrders,
    SUM(fs.QuantityOrdered) as TotalQuantityOrdered,
    SUM(fs.TotalSale) as TotalSales
from
    FactSales fs
join
    ProductDimension pd on fs.ProductID = pd.ProductID
join
    SupplierDimension sd on pd.supplierID = sd.SupplierID
join
    DateDimension dd on fs.DateID = dd.DateID
group by
    sd.SupplierID, sd.supplierName, dd.d_Year, dd.d_Month;

create table SUPPLIER_PERFORMANCE_MV as
select * from SUPPLIER_PERFORMANCE_VIEW;

delimiter //
drop event if exists update_supplier_performance_mv;

create event if not exists update_supplier_performance_mv
on schedule every 1 day
do
begin
    truncate table SUPPLIER_PERFORMANCE_MV;
    insert into SUPPLIER_PERFORMANCE_MV select * from SUPPLIER_PERFORMANCE_VIEW;
end;

//

delimiter ;
set global event_scheduler = on;

select * from  SUPPLIER_PERFORMANCE_VIEW;
# 9 

select
    cd.CustomerID,
    cd.CustomerName,
    COUNT(DISTINCT fs.ProductID) as UniqueProductsPurchased,
    SUM(fs.TotalSale) as TotalSales
from
    FactSales fs
join
    CustomerDimension cd on fs.CustomerID = cd.CustomerID
join
    DateDimension dd on fs.DateID = dd.DateID
where
    dd.d_Year = 2019
group by
    cd.CustomerID, cd.CustomerName
order by
    TotalSales DESC
limit 5;

# 10  

drop view if exists CUSTOMER_STORE_SALES_VIEW;
drop table if exists CUSTOMER_STORE_SALES_MV;

create view CUSTOMER_STORE_SALES_VIEW as
select
    sd.StoreID,
    sd.storeName,
    cd.CustomerID,
    cd.CustomerName,
    dd.d_Year,
    dd.d_Month,
    SUM(fs.TotalSale) as TotalSales
from
    FactSales fs
join
    StoreDimension sd on fs.StoreID = sd.StoreID
join
    CustomerDimension cd on fs.CustomerID = cd.CustomerID
join
    DateDimension dd on fs.DateID = dd.DateID
group by
    sd.StoreID, cd.CustomerID, dd.d_Year, dd.d_Month;

create table CUSTOMER_STORE_SALES_MV as
select * from CUSTOMER_STORE_SALES_VIEW;

delimiter //

drop event if exists update_customer_store_sales_mv;

create event if not exists update_customer_store_sales_mv
on schedule every 1 day
do
begin
    truncate table CUSTOMER_STORE_SALES_MV;
    insert into CUSTOMER_STORE_SALES_MV select * from CUSTOMER_STORE_SALES_VIEW;
end;

//

delimiter ;

set global event_scheduler = on;

select * from CUSTOMER_STORE_SALES_VIEW;
