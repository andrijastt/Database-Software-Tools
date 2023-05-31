USE ProdavnicaArtikala
GO

CREATE FUNCTION F_SHORTEST_PATH
(	
	@start int,
	@end int
)
RETURNS TABLE 
AS
RETURN 
(
	WITH Tabela1 as(
		SELECT 
			CASE
			when IdCity1 = @start then IdCity2
			when IdCity2 = @start then IdCity1
			end IdCity
		, Distance
		from Line
		where IdCity1 = @start or IdCity2 = @start
		UNION ALL
		Select CASE
			when a.IdCity1 = t.IdCity then a.IdCity2
			when a.IdCity2 = t.IdCity then a.IdCity1
			end IdCity, t.Distance + a.Distance
		from Line a, Tabela1 t
		where a.IdCity2 = t.IdCity 
	),
	Tabela2 as(
		Select * 
		from Tabela1 
		where IdCity = @end
	)
	
	Select TOP 1 *
	from Tabela2
	Order by Distance ASC
)
GO
