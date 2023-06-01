USE ProdavnicaArtikala
GO

Insert into SimulationTime(Id, Time) values (1, getDate())

Insert into City(name) values('A')
Insert into City(name) values('B')
Insert into City(name) values('C1')
Insert into City(name) values('C2')
Insert into City(name) values('C3')
Insert into City(name) values('C4')
Insert into City(name) values('C5') 

INSERT into Line(IdCity1, IdCity2, Distance) values(1, 7, 15)
INSERT into Line(IdCity1, IdCity2, Distance) values(1, 3, 10)
INSERT into Line(IdCity1, IdCity2, Distance) values(2, 7, 2)
INSERT into Line(IdCity1, IdCity2, Distance) values(2, 3, 8)
INSERT into Line(IdCity1, IdCity2, Distance) values(1, 4, 3)
INSERT into Line(IdCity1, IdCity2, Distance) values(1, 6, 3)
INSERT into Line(IdCity1, IdCity2, Distance) values(6, 5, 1)
INSERT into Line(IdCity1, IdCity2, Distance) values(5, 4, 2)

Insert into Buyer(Name, Wallet, IdCity) Values ('Andrija', 10000, 2)

Insert into Shop(Name, IdCity) values ('Prodavnica1', 1)
Insert into Shop(Name, IdCity) values ('Prodavnica2', 4)
Insert into Shop(Name, IdCity) values ('Prodavnica3', 5)


Insert into Article(Name, Price, [Count], IdShop) values ('Article1', 100, 10, 1)
Insert into Article(Name, Price, [Count], IdShop) values ('Article2', 200, 20, 2)
Insert into Article(Name, Price, [Count], IdShop) values ('Article3', 300, 30, 3)

Insert into [Order](IdBuyer, Status) values (1, 'created')

Insert into Item(IdArticle, [Count], IdOrder) values (1, 5, 1)
Insert into Item(IdArticle, [Count], IdOrder) values (2, 10, 1)
Insert into Item(IdArticle, [Count], IdOrder) values (3, 20, 1)

Update SHOP
set Discount = 15
where IdShop = 2


/* Update [Order]
set Status = 'created', CurrentCity = null, TravelTime = null
where IdOrder = 1 */

select * from City
select * from Line
select * from Buyer
select * from Shop
select * from Article
select * from [Order]
select * from Item
select * from [Transaction]
--select * from [TransactionShop]
select * from SimulationTime
select * from Tracking
