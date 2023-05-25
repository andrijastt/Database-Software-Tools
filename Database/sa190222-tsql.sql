CREATE PROCEDURE SP_FINAL_PRICE
	@idOrder int
AS
BEGIN
	
Select Sum(I.[Count] * A.Price * (100 - S.Discount) / 100)
from [Order] O join Item I on O.IdOrder = I.IdOrder join Article A on A.IdArticle = I.IdArticle join Shop S on S.IdShop = A.IdShop
where O.IdOrder = @idOrder

END
GO
