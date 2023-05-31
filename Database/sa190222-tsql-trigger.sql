USE ProdavnicaArtikala
GO

CREATE TRIGGER TR_TRANSFER_MONEY
   ON  [Order]
   AFTER UPDATE
AS 
if (update(status))
BEGIN
		
	declare @idOrder int
	declare @idBuyer int
	declare @status varchar(100)

	-- select changed data
	select @idOrder = IdOrder, @idBuyer = IdBuyer, @status = status
	from inserted

	-- if status has changed to arrived, we dont do anything
	if(@status = 'arrived' or @status = 'created') return

	-- where the package has to go
	declare @idCityBuyer int
	select @idCityBuyer = IdCity
	from Buyer
	where IdBuyer = @idBuyer

	-- current date
	declare @date date
	select @date = [Time]
	from SimulationTime
	
	declare @count int		-- item amount
	declare @idShop int		-- from what shop
	declare @discount int	-- if the shop has discount
	declare @price decimal	-- price of article
	declare @idCity int		-- idCity from shop
	declare @idArticle int	-- idArticle we need to change count
	declare @amountToPay decimal	-- price for articles

	declare @systemCut int		-- systemCut, 3 or 5 percent
	if((Select sum(AmountPaid) from [Transaction] where IdBuyer = @idBuyer and @date >= DateAdd(day, -30, @date)) > 10000) 
	begin
		set @systemCut = 3
	end
	else
		set @systemCut = 5	

	declare @cursor cursor				-- we use cursor tu select previously declared variables

	set @cursor = cursor for
	select [I].[Count], S.IdShop, S.Discount, A.Price, S.IdCity, A.IdArticle
	from Article A join Item I on A.IdArticle = I.IdArticle join Shop S on S.IdShop = A.IdShop
	where I.IdOrder = @idOrder

	open @cursor
	fetch next from @cursor into @count, @idShop, @discount, @price, @idCity, @idArticle

	Select @amountToPay = sum(I.[Count] * A.Price * (100 - S.Discount) / 100)			-- here we calculate how much is the transaction for buyer
	from Article A join Item I on A.IdArticle = I.IdArticle join Shop S on S.IdShop = A.IdShop
	where I.IdOrder = @idOrder -- and S.IdShop = @idShop
	
	Insert into [Transaction](IdOrder, IdBuyer, AmountPaid, ExecutionTime, SystemCut)	-- insert Transaction for order, buyer
	values(@idOrder, @idBuyer, @amountToPay, @date, @systemCut)

	while @@FETCH_STATUS = 0
		begin				

		Select @amountToPay = sum(I.[Count] * A.Price * (100 - S.Discount) / 100)			-- here we calculate how much is the transaction for shop
		from Article A join Item I on A.IdArticle = I.IdArticle join Shop S on S.IdShop = A.IdShop
		where I.IdOrder = @idOrder and S.IdShop = @idShop

		declare @idTransaction int
		select @idTransaction = IdTransaction
		from [Transaction]
		where IdOrder = @idOrder

		Insert into [TransactionShop](IdShop, IdTransaction, IdOrder, AmountPaid)
		values(@idShop, @idTransaction, @idOrder, (@amountToPay * (100 - @systemCut) / 100))								-- @amountToPay promena

		Update Shop																			-- update shop balance
		set Balance = Balance + (@amountToPay * (100 - @systemCut) / 100)
		where IdShop = @idShop

		Update Article
		set [Count] = [Count] - @count
		where IdArticle = @idArticle		

		fetch next from @cursor into @count, @idShop, @discount, @price, @idCity, @idArticle
	end
		
	Delete from Item
	where IdOrder = @idOrder

	close @cursor
	deallocate @cursor
	
END
GO
