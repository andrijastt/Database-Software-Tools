USE ProdavnicaArtikala
GO

CREATE TRIGGER TR_TIME_PASSED
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

	open @cursorTracking
	fetch next from @cursorTracking into @startDate, @idCity

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

			Delete from Tracking
			where IdOrder = @idOrder
		end
		else
		begin												
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
