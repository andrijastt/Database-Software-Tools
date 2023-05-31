USE ProdavnicaArtikala
GO

CREATE TRIGGER TR_TIME_PASSED
   ON  SimulationTime
   AFTER INSERT, UPDATE
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

	while @@FETCH_STATUS = 0
	begin

		if(DATEDIFF(day, @sentTime, @date) >= @travelTime)
		begin

			declare @idCity int

			Select @idCity = IdCity
			from Buyer
			where IdBuyer = @idBuyer

			Update [Order]
			set Status = 'arrived', CurrentCity = @idCity, ReceivedTime = DATEADD(day, @travelTime, @sentTime)
			where IdOrder = @idOrder
		end

		fetch next from @cursor into @sentTime, @travelTime, @idBuyer, @idOrder
	end

	close @cursor
	deallocate @cursor

END
GO
