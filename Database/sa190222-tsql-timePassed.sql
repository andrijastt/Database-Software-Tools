USE ProdavnicaArtikala
GO

CREATE TRIGGER TR_TRANSFER_MONEY_TO_SHOPS
   ON  SimulationTime
   AFTER UPDATE
AS 
BEGIN
	
	declare @date Date
	select @date = Time
	from SimulationTime

	declare @cursor cursor

	set @cursor = cursor for
	select SentTime, TravelTime, IdBuyer, IdOrder
	from [Order]
	where Status = 'sent'

	open @cursor

	declare @sentTime date
	declare @travelTime int
	declare @idBuyer int
	declare @idOrder int	

	fetch next from @cursor into @sentTime, @travelTime, @idBuyer, @idOrder

	declare @idCity int
	declare @startDate date

	declare @cursorTracking cursor

	set @cursorTracking = cursor for
	Select StartDate, IdCity
	from Tracking
	where IdOrder = @idOrder
	order by StartDate ASC

	open @cursorTracking
	
	while @@FETCH_STATUS = 0
	begin
		if(DATEDIFF(day, @sentTime, @date) >= @travelTime)
		begin			
			Select @idCity = IdCity
			from Buyer
			where IdBuyer = @idBuyer

			Update [Order]
			set Status = 'arrived', CurrentCity = @idCity, ReceivedTime = DATEADD(day, @travelTime, @sentTime)
			where IdOrder = @idOrder

			-- update shop balance
			declare @cursorShop cursor
			declare @idShop int
			declare @balance decimal(10,3)

			set @cursorShop = cursor for
			Select S.IdShop
			from [Order] O join Item I on O.IdOrder = I.IdOrder join Article A on A.IdArticle = I.IdArticle join Shop S on S.IdShop = A.IdShop
			where O.IdOrder = @idOrder

			open @cursorShop
			fetch next from @cursorShop into @idShop

			while @@FETCH_STATUS = 0
			begin

				Select @balance = Sum(I.[Count] * A.Price * (100 - S.Discount) / 100)
				from [Order] O join Item I on O.IdOrder = I.IdOrder join Article A on A.IdArticle = I.IdArticle join Shop S on S.IdShop = A.IdShop
				where O.IdOrder = @idOrder and S.IdShop = @idShop

				Insert into [Transaction](IdShop, IdOrder, AmountPaid, ExecutionTime, SystemCut)	-- insert Transaction for order, buyer
				values(@idShop, @idOrder, @balance, DATEADD(day, @travelTime, @sentTime), 5)

				set @balance = @balance * 0.95

				Update Shop																			-- update shop balance
				set Balance = Balance + @balance
				where IdShop = @idShop				

				fetch next from @cursorShop into @idShop
			end

			close @cursorShop
			deallocate @cursorShop


			Delete from Tracking
			where IdOrder = @idOrder
		end
		else
		begin							
			fetch next from @cursorTracking into @startDate, @idCity
			while @@FETCH_STATUS = 0
			begin			
				if(DATEDIFF(day, @startDate, @date) >= 0)
				begin

				Update [Order]
				set CurrentCity = @idCity
				where IdOrder = @idOrder				

				end
				fetch next from @cursorTracking into @startDate, @idCity
			end						
		end	
		fetch next from @cursor into @sentTime, @travelTime, @idBuyer, @idOrder
	end

	close @cursor
	deallocate @cursor
	close @cursorTracking
	deallocate @cursorTracking

END
GO
