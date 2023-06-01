USE ProdavnicaArtikala
GO

CREATE TRIGGER TR_TRANSFER_MONEY_FROM_BUYER
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

	-- current date
	declare @date date
	select @date = [Time]
	from SimulationTime
	
	declare @amountToPay decimal	-- price for articles

	declare @systemCut int		-- systemCut, 3 or 5 percent
	if((Select sum(AmountPaid) 
		from [Transaction]
		where IdBuyer = @idBuyer and @date >= DateAdd(day, -30, @date)) > 10000) 
	begin
		set @systemCut = 3
	end
	else
		set @systemCut = 5	

	Select @amountToPay = sum(I.[Count] * A.Price * (100 - S.Discount) / 100)			-- here we calculate how much is the transaction for shop
		from Article A join Item I on A.IdArticle = I.IdArticle join Shop S on S.IdShop = A.IdShop
		where I.IdOrder = @idOrder

	Insert into [Transaction](IdBuyer, IdOrder, AmountPaid, ExecutionTime, SystemCut)	-- insert Transaction for order, buyer
	values(@idBuyer, @idOrder, @amountToPay, @date, @systemCut)

	if(@systemCut = 3)
			set @amountToPay = @amountToPay * 0.98

	Update Buyer
	set Wallet = Wallet - @amountToPay
	where IdBuyer = @IdBuyer	

	declare @count int		-- item amount				
	declare @idArticle int	-- idArticle we need to change count

	declare @cursor cursor				-- we use cursor tu select previously declared variables

	set @cursor = cursor for
	select [I].[Count], A.IdArticle
	from Article A join Item I on A.IdArticle = I.IdArticle
	where I.IdOrder = @idOrder

	open @cursor
	fetch next from @cursor into @count, @idArticle	
		
	while @@FETCH_STATUS = 0
		begin				

		/*
		Select @amountToPay = sum(I.[Count] * A.Price * (100 - S.Discount) / 100)			-- here we calculate how much is the transaction for shop
		from Article A join Item I on A.IdArticle = I.IdArticle join Shop S on S.IdShop = A.IdShop
		where I.IdOrder = @idOrder		

		Insert into [Transaction](IdOrder, AmountPaid, ExecutionTime, SystemCut)	-- insert Transaction for order, buyer
		values(@idOrder, @amountToPay, @date, @systemCut)
		
		Update Shop																			-- update shop balance
		set Balance = Balance + (@amountToPay * (100 - @systemCut) / 100)
		where IdShop = @idShop
		*/

		Update Article
		set [Count] = [Count] - @count
		where IdArticle = @idArticle		

		fetch next from @cursor into @count, @idArticle
	end			

	close @cursor
	deallocate @cursor
	
END
GO
