USE ProdavnicaArtikala
GO

CREATE TRIGGER TR_TRANSFER_MONEY
   ON  [Order]
   AFTER UPDATE
AS 
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

	declare @shortestPathToBuyer int	-- nearest city from shop to buyer
	set @shortestPathToBuyer = -1		-- distance in days
	declare @idCityToBuyer int			-- idCity of the city

	declare @cursor cursor				-- we use cursor tu select previously declared variables

	set @cursor = cursor for
	select [I].[Count], S.IdShop, S.Discount, A.Price, S.IdCity, A.IdArticle
	from Article A join Item I on A.IdArticle = I.IdArticle join Shop S on S.IdShop = A.IdShop
	where I.IdOrder = @idOrder

	open @cursor
	fetch next from @cursor into @count, @idShop, @discount, @price, @idCity, @idArticle

	while @@FETCH_STATUS = 0
	begin	

	Select @amountToPay = sum(I.[Count] * A.Price * (100 - S.Discount) / 100)			-- here we calculate how much is the transaction for shops
	from Article A join Item I on A.IdArticle = I.IdArticle join Shop S on S.IdShop = A.IdShop
	where I.IdOrder = @idOrder and S.IdShop = @idShop
	
	Insert into [Transaction](IdShop, IdOrder, IdBuyer, AmountPaid, ExecutionTime, SystemCut)	-- insert Transaction for shop, order, buyer
	values(@idShop, @idOrder, @idBuyer, @amountToPay, @date, @systemCut)	

	Update Shop																			-- update shop balance
	set Balance = Balance + (@amountToPay * (100 - @systemCut) / 100)
	where IdShop = @idShop

	Update Article
	set [Count] = [Count] - @count
	where IdArticle = @idArticle

	declare @distanceToCity int															-- distance from shop to buyer

	if(@idCityBuyer = @idCity)															-- if shop is in the same city as buyer
	begin
		set @distanceToCity = 0
	end
	else																				-- else calculate distance
	begin
		select @distanceToCity = distance
		from dbo.F_SHORTEST_PATH(@idCityBuyer, @idCity)
	end		

	if(@shortestPathToBuyer = -1 or @distanceToCity < @shortestPathToBuyer)				-- check if distance is lower
	begin
		set @shortestPathToBuyer = @distanceToCity
		set @idCityToBuyer = @idCity
	end

	fetch next from @cursor into @count, @idShop, @discount, @price, @idCity, @idArticle
	end

	declare @cursorShop cursor

	set @cursorShop = cursor for		-- get idCity from shops, excludeing nearest city to buyer
	select S.IdCity
	from Article A join Item I on A.IdArticle = I.IdArticle join Shop S on S.IdShop = A.IdShop
	where I.IdOrder = @idOrder and S.IdCity != @idCityToBuyer

	open @cursorShop

	fetch next from @cursorShop into @idCity

	declare @longestPathToCity int
	set @longestPathToCity = 0	

	while @@FETCH_STATUS = 0
	begin

		select @distanceToCity = distance
		from dbo.F_SHORTEST_PATH(@idCityToBuyer, @idCity)

		if(@longestPathToCity < @distanceToCity)		-- if the longestPath is not longest then set longestPath
		begin
			set @longestPathToCity = @distanceToCity			
		end

		fetch next from @cursorShop into @idCity
	end

	UPDATE [Order]					-- update Order
	set Status = 'sent', TravelTime = @shortestPathToBuyer + @longestPathToCity, CurrentCity = @idCityToBuyer
	where IdOrder = @idOrder

	close @cursor
	deallocate @cursor
	close @cursorShop
	deallocate @cursorShop

END
GO
