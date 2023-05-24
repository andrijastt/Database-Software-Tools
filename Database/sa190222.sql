CREATE DATABASE ProdavnicaArtikala
GO

USE ProdavnicaArtikala
GO
 
CREATE TABLE [Article]
( 
	[IdArticle]          integer  IDENTITY  NOT NULL ,
	[Name]               varchar(100)  NOT NULL ,
	[Price]              decimal(10,3)  NOT NULL ,
	[Count]              integer  NOT NULL ,
	[IdShop]             integer  NULL 
)
go

CREATE TABLE [Buyer]
( 
	[IdBuyer]            integer  IDENTITY  NOT NULL ,
	[Name]               varchar(100)  NOT NULL ,
	[Wallet]             decimal(10,3)  NOT NULL ,
	[IdCity]             integer  NULL 
)
go

CREATE TABLE [City]
( 
	[IdCity]             integer  IDENTITY  NOT NULL ,
	[Name]               varchar(100)  NOT NULL 
)
go

CREATE TABLE [Item]
( 
	[IdArticle]          integer  NOT NULL ,
	[Count]              integer  NOT NULL ,
	[IdOrder]            integer  NULL ,
	[IdItem]             integer  IDENTITY  NOT NULL 
)
go

CREATE TABLE [Line]
( 
	[IdCity2]            integer  NOT NULL ,
	[IdCity1]            integer  NOT NULL ,
	[Distance]           integer  NOT NULL 
	CONSTRAINT [Default_Value_339_742414176]
		 DEFAULT  1,
	[IdLine]             integer  IDENTITY  NOT NULL 
)
go

CREATE TABLE [Order]
( 
	[IdOrder]            integer  IDENTITY  NOT NULL ,
	[IdBuyer]            integer  NULL ,
	[Status]             varchar(100)  NOT NULL ,
	[CurrentCity]        integer  NULL ,
	[SentTime]           datetime  NULL ,
	[ReceivedTime]       datetime  NULL 
)
go

CREATE TABLE [Shop]
( 
	[IdShop]             integer  IDENTITY  NOT NULL ,
	[Name]               varchar(100)  NOT NULL ,
	[Discount]           integer  NOT NULL 
	CONSTRAINT [ZERO]
		 DEFAULT  0
	CONSTRAINT [Discount_0_100]
		CHECK  ( Discount BETWEEN 0 AND 100 ),
	[IdCity]             integer  NOT NULL ,
	[Balance]            decimal(10,3)  NOT NULL 
	CONSTRAINT [ZERO_743014288]
		 DEFAULT  0
)
go

CREATE TABLE [Transaction]
( 
	[AmountPaid]         decimal(10,3)  NOT NULL 
	CONSTRAINT [ZERO_1565478598]
		 DEFAULT  0,
	[IdOrder]            integer  NOT NULL ,
	[IdShop]             integer  NOT NULL ,
	[IdBuyer]            integer  NOT NULL ,
	[ExecutionTime]      datetime  NULL ,
	[SystemCut]          integer  NOT NULL 
	CONSTRAINT [FIVE_1581478741]
		 DEFAULT  5
	CONSTRAINT [Three_or_Five_141057686]
		CHECK  ( SystemCut BETWEEN 3 AND 5 )
)
go

ALTER TABLE [Article]
	ADD CONSTRAINT [XPKArticle] PRIMARY KEY  CLUSTERED ([IdArticle] ASC)
go

ALTER TABLE [Buyer]
	ADD CONSTRAINT [XPKBuyer] PRIMARY KEY  CLUSTERED ([IdBuyer] ASC)
go

ALTER TABLE [City]
	ADD CONSTRAINT [XPKCity] PRIMARY KEY  CLUSTERED ([IdCity] ASC)
go

ALTER TABLE [Item]
	ADD CONSTRAINT [XPKItem] PRIMARY KEY  CLUSTERED ([IdItem] ASC)
go

ALTER TABLE [Line]
	ADD CONSTRAINT [XPKLine] PRIMARY KEY  CLUSTERED ([IdLine] ASC)
go

ALTER TABLE [Order]
	ADD CONSTRAINT [XPKOrder] PRIMARY KEY  CLUSTERED ([IdOrder] ASC)
go

ALTER TABLE [Shop]
	ADD CONSTRAINT [XPKShop] PRIMARY KEY  CLUSTERED ([IdShop] ASC)
go

ALTER TABLE [Transaction]
	ADD CONSTRAINT [XPKTransaction] PRIMARY KEY  CLUSTERED ([IdOrder] ASC,[IdShop] ASC,[IdBuyer] ASC)
go


ALTER TABLE [Article]
	ADD CONSTRAINT [R_1] FOREIGN KEY ([IdShop]) REFERENCES [Shop]([IdShop])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Buyer]
	ADD CONSTRAINT [R_2] FOREIGN KEY ([IdCity]) REFERENCES [City]([IdCity])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Item]
	ADD CONSTRAINT [R_14] FOREIGN KEY ([IdArticle]) REFERENCES [Article]([IdArticle])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Item]
	ADD CONSTRAINT [R_15] FOREIGN KEY ([IdOrder]) REFERENCES [Order]([IdOrder])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Line]
	ADD CONSTRAINT [R_9] FOREIGN KEY ([IdCity2]) REFERENCES [City]([IdCity])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Line]
	ADD CONSTRAINT [R_10] FOREIGN KEY ([IdCity1]) REFERENCES [City]([IdCity])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Order]
	ADD CONSTRAINT [R_3] FOREIGN KEY ([IdBuyer]) REFERENCES [Buyer]([IdBuyer])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Shop]
	ADD CONSTRAINT [R_12] FOREIGN KEY ([IdCity]) REFERENCES [City]([IdCity])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Transaction]
	ADD CONSTRAINT [R_11] FOREIGN KEY ([IdOrder]) REFERENCES [Order]([IdOrder])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Transaction]
	ADD CONSTRAINT [R_16] FOREIGN KEY ([IdShop]) REFERENCES [Shop]([IdShop])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Transaction]
	ADD CONSTRAINT [R_17] FOREIGN KEY ([IdBuyer]) REFERENCES [Buyer]([IdBuyer])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


CREATE TRIGGER tD_Article ON Article FOR DELETE AS
/* erwin Builtin Trigger */
/* DELETE trigger on Article */
BEGIN
  DECLARE  @errno   int,
           @severity int,
           @state    int,
           @errmsg  varchar(255)
    /* erwin Builtin Trigger */
    /* Article  Item on parent delete no action */
    /* ERWIN_RELATION:CHECKSUM="000207cc", PARENT_OWNER="", PARENT_TABLE="Article"
    CHILD_OWNER="", CHILD_TABLE="Item"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_14", FK_COLUMNS="IdArticle" */
    IF EXISTS (
      SELECT * FROM deleted,Item
      WHERE
        /*  %JoinFKPK(Item,deleted," = "," AND") */
        Item.IdArticle = deleted.IdArticle
    )
    BEGIN
      SELECT @errno  = 30001,
             @errmsg = 'Cannot delete Article because Item exists.'
      GOTO error
    END

    /* erwin Builtin Trigger */
    /* Shop  Article on child delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Shop"
    CHILD_OWNER="", CHILD_TABLE="Article"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_1", FK_COLUMNS="IdShop" */
    IF EXISTS (SELECT * FROM deleted,Shop
      WHERE
        /* %JoinFKPK(deleted,Shop," = "," AND") */
        deleted.IdShop = Shop.IdShop AND
        NOT EXISTS (
          SELECT * FROM Article
          WHERE
            /* %JoinFKPK(Article,Shop," = "," AND") */
            Article.IdShop = Shop.IdShop
        )
    )
    BEGIN
      SELECT @errno  = 30010,
             @errmsg = 'Cannot delete last Article because Shop exists.'
      GOTO error
    END


    /* erwin Builtin Trigger */
    RETURN
error:
   RAISERROR (@errmsg, -- Message text.
              @severity, -- Severity (0~25).
              @state) -- State (0~255).
    rollback transaction
END

go


CREATE TRIGGER tU_Article ON Article FOR UPDATE AS
/* erwin Builtin Trigger */
/* UPDATE trigger on Article */
BEGIN
  DECLARE  @numrows int,
           @nullcnt int,
           @validcnt int,
           @insIdArticle integer,
           @errno   int,
           @severity int,
           @state    int,
           @errmsg  varchar(255)

  SELECT @numrows = @@rowcount
  /* erwin Builtin Trigger */
  /* Article  Item on parent update no action */
  /* ERWIN_RELATION:CHECKSUM="00026ade", PARENT_OWNER="", PARENT_TABLE="Article"
    CHILD_OWNER="", CHILD_TABLE="Item"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_14", FK_COLUMNS="IdArticle" */
  IF
    /* %ParentPK(" OR",UPDATE) */
    UPDATE(IdArticle)
  BEGIN
    IF EXISTS (
      SELECT * FROM deleted,Item
      WHERE
        /*  %JoinFKPK(Item,deleted," = "," AND") */
        Item.IdArticle = deleted.IdArticle
    )
    BEGIN
      SELECT @errno  = 30005,
             @errmsg = 'Cannot update Article because Item exists.'
      GOTO error
    END
  END

  /* erwin Builtin Trigger */
  /* Shop  Article on child update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Shop"
    CHILD_OWNER="", CHILD_TABLE="Article"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_1", FK_COLUMNS="IdShop" */
  IF
    /* %ChildFK(" OR",UPDATE) */
    UPDATE(IdShop)
  BEGIN
    SELECT @nullcnt = 0
    SELECT @validcnt = count(*)
      FROM inserted,Shop
        WHERE
          /* %JoinFKPK(inserted,Shop) */
          inserted.IdShop = Shop.IdShop
    /* %NotnullFK(inserted," IS NULL","select @nullcnt = count(*) from inserted where"," AND") */
    select @nullcnt = count(*) from inserted where
      inserted.IdShop IS NULL
    IF @validcnt + @nullcnt != @numrows
    BEGIN
      SELECT @errno  = 30007,
             @errmsg = 'Cannot update Article because Shop does not exist.'
      GOTO error
    END
  END


  /* erwin Builtin Trigger */
  RETURN
error:
   RAISERROR (@errmsg, -- Message text.
              @severity, -- Severity (0~25).
              @state) -- State (0~255).
    rollback transaction
END

go




CREATE TRIGGER tD_Buyer ON Buyer FOR DELETE AS
/* erwin Builtin Trigger */
/* DELETE trigger on Buyer */
BEGIN
  DECLARE  @errno   int,
           @severity int,
           @state    int,
           @errmsg  varchar(255)
    /* erwin Builtin Trigger */
    /* Buyer  Transaction on parent delete no action */
    /* ERWIN_RELATION:CHECKSUM="0002e66f", PARENT_OWNER="", PARENT_TABLE="Buyer"
    CHILD_OWNER="", CHILD_TABLE="Transaction"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_17", FK_COLUMNS="IdBuyer" */
    IF EXISTS (
      SELECT * FROM deleted,Transaction
      WHERE
        /*  %JoinFKPK(Transaction,deleted," = "," AND") */
        Transaction.IdBuyer = deleted.IdBuyer
    )
    BEGIN
      SELECT @errno  = 30001,
             @errmsg = 'Cannot delete Buyer because Transaction exists.'
      GOTO error
    END

    /* erwin Builtin Trigger */
    /* Buyer  Order on parent delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Buyer"
    CHILD_OWNER="", CHILD_TABLE="Order"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_3", FK_COLUMNS="IdBuyer" */
    IF EXISTS (
      SELECT * FROM deleted,Order
      WHERE
        /*  %JoinFKPK(Order,deleted," = "," AND") */
        Order.IdBuyer = deleted.IdBuyer
    )
    BEGIN
      SELECT @errno  = 30001,
             @errmsg = 'Cannot delete Buyer because Order exists.'
      GOTO error
    END

    /* erwin Builtin Trigger */
    /* City  Buyer on child delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="City"
    CHILD_OWNER="", CHILD_TABLE="Buyer"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_2", FK_COLUMNS="IdCity" */
    IF EXISTS (SELECT * FROM deleted,City
      WHERE
        /* %JoinFKPK(deleted,City," = "," AND") */
        deleted.IdCity = City.IdCity AND
        NOT EXISTS (
          SELECT * FROM Buyer
          WHERE
            /* %JoinFKPK(Buyer,City," = "," AND") */
            Buyer.IdCity = City.IdCity
        )
    )
    BEGIN
      SELECT @errno  = 30010,
             @errmsg = 'Cannot delete last Buyer because City exists.'
      GOTO error
    END


    /* erwin Builtin Trigger */
    RETURN
error:
   RAISERROR (@errmsg, -- Message text.
              @severity, -- Severity (0~25).
              @state) -- State (0~255).
    rollback transaction
END

go


CREATE TRIGGER tU_Buyer ON Buyer FOR UPDATE AS
/* erwin Builtin Trigger */
/* UPDATE trigger on Buyer */
BEGIN
  DECLARE  @numrows int,
           @nullcnt int,
           @validcnt int,
           @insIdBuyer integer,
           @errno   int,
           @severity int,
           @state    int,
           @errmsg  varchar(255)

  SELECT @numrows = @@rowcount
  /* erwin Builtin Trigger */
  /* Buyer  Transaction on parent update no action */
  /* ERWIN_RELATION:CHECKSUM="00037356", PARENT_OWNER="", PARENT_TABLE="Buyer"
    CHILD_OWNER="", CHILD_TABLE="Transaction"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_17", FK_COLUMNS="IdBuyer" */
  IF
    /* %ParentPK(" OR",UPDATE) */
    UPDATE(IdBuyer)
  BEGIN
    IF EXISTS (
      SELECT * FROM deleted,Transaction
      WHERE
        /*  %JoinFKPK(Transaction,deleted," = "," AND") */
        Transaction.IdBuyer = deleted.IdBuyer
    )
    BEGIN
      SELECT @errno  = 30005,
             @errmsg = 'Cannot update Buyer because Transaction exists.'
      GOTO error
    END
  END

  /* erwin Builtin Trigger */
  /* Buyer  Order on parent update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Buyer"
    CHILD_OWNER="", CHILD_TABLE="Order"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_3", FK_COLUMNS="IdBuyer" */
  IF
    /* %ParentPK(" OR",UPDATE) */
    UPDATE(IdBuyer)
  BEGIN
    IF EXISTS (
      SELECT * FROM deleted,Order
      WHERE
        /*  %JoinFKPK(Order,deleted," = "," AND") */
        Order.IdBuyer = deleted.IdBuyer
    )
    BEGIN
      SELECT @errno  = 30005,
             @errmsg = 'Cannot update Buyer because Order exists.'
      GOTO error
    END
  END

  /* erwin Builtin Trigger */
  /* City  Buyer on child update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="City"
    CHILD_OWNER="", CHILD_TABLE="Buyer"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_2", FK_COLUMNS="IdCity" */
  IF
    /* %ChildFK(" OR",UPDATE) */
    UPDATE(IdCity)
  BEGIN
    SELECT @nullcnt = 0
    SELECT @validcnt = count(*)
      FROM inserted,City
        WHERE
          /* %JoinFKPK(inserted,City) */
          inserted.IdCity = City.IdCity
    /* %NotnullFK(inserted," IS NULL","select @nullcnt = count(*) from inserted where"," AND") */
    select @nullcnt = count(*) from inserted where
      inserted.IdCity IS NULL
    IF @validcnt + @nullcnt != @numrows
    BEGIN
      SELECT @errno  = 30007,
             @errmsg = 'Cannot update Buyer because City does not exist.'
      GOTO error
    END
  END


  /* erwin Builtin Trigger */
  RETURN
error:
   RAISERROR (@errmsg, -- Message text.
              @severity, -- Severity (0~25).
              @state) -- State (0~255).
    rollback transaction
END

go




CREATE TRIGGER tD_City ON City FOR DELETE AS
/* erwin Builtin Trigger */
/* DELETE trigger on City */
BEGIN
  DECLARE  @errno   int,
           @severity int,
           @state    int,
           @errmsg  varchar(255)
    /* erwin Builtin Trigger */
    /* City  Shop on parent delete no action */
    /* ERWIN_RELATION:CHECKSUM="000370e3", PARENT_OWNER="", PARENT_TABLE="City"
    CHILD_OWNER="", CHILD_TABLE="Shop"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_12", FK_COLUMNS="IdCity" */
    IF EXISTS (
      SELECT * FROM deleted,Shop
      WHERE
        /*  %JoinFKPK(Shop,deleted," = "," AND") */
        Shop.IdCity = deleted.IdCity
    )
    BEGIN
      SELECT @errno  = 30001,
             @errmsg = 'Cannot delete City because Shop exists.'
      GOTO error
    END

    /* erwin Builtin Trigger */
    /* City  Line on parent delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="City"
    CHILD_OWNER="", CHILD_TABLE="Line"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_10", FK_COLUMNS="IdCity1" */
    IF EXISTS (
      SELECT * FROM deleted,Line
      WHERE
        /*  %JoinFKPK(Line,deleted," = "," AND") */
        Line.IdCity1 = deleted.IdCity
    )
    BEGIN
      SELECT @errno  = 30001,
             @errmsg = 'Cannot delete City because Line exists.'
      GOTO error
    END

    /* erwin Builtin Trigger */
    /* City  Line on parent delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="City"
    CHILD_OWNER="", CHILD_TABLE="Line"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_9", FK_COLUMNS="IdCity2" */
    IF EXISTS (
      SELECT * FROM deleted,Line
      WHERE
        /*  %JoinFKPK(Line,deleted," = "," AND") */
        Line.IdCity2 = deleted.IdCity
    )
    BEGIN
      SELECT @errno  = 30001,
             @errmsg = 'Cannot delete City because Line exists.'
      GOTO error
    END

    /* erwin Builtin Trigger */
    /* City  Buyer on parent delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="City"
    CHILD_OWNER="", CHILD_TABLE="Buyer"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_2", FK_COLUMNS="IdCity" */
    IF EXISTS (
      SELECT * FROM deleted,Buyer
      WHERE
        /*  %JoinFKPK(Buyer,deleted," = "," AND") */
        Buyer.IdCity = deleted.IdCity
    )
    BEGIN
      SELECT @errno  = 30001,
             @errmsg = 'Cannot delete City because Buyer exists.'
      GOTO error
    END


    /* erwin Builtin Trigger */
    RETURN
error:
   RAISERROR (@errmsg, -- Message text.
              @severity, -- Severity (0~25).
              @state) -- State (0~255).
    rollback transaction
END

go


CREATE TRIGGER tU_City ON City FOR UPDATE AS
/* erwin Builtin Trigger */
/* UPDATE trigger on City */
BEGIN
  DECLARE  @numrows int,
           @nullcnt int,
           @validcnt int,
           @insIdCity integer,
           @errno   int,
           @severity int,
           @state    int,
           @errmsg  varchar(255)

  SELECT @numrows = @@rowcount
  /* erwin Builtin Trigger */
  /* City  Shop on parent update no action */
  /* ERWIN_RELATION:CHECKSUM="0003d04e", PARENT_OWNER="", PARENT_TABLE="City"
    CHILD_OWNER="", CHILD_TABLE="Shop"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_12", FK_COLUMNS="IdCity" */
  IF
    /* %ParentPK(" OR",UPDATE) */
    UPDATE(IdCity)
  BEGIN
    IF EXISTS (
      SELECT * FROM deleted,Shop
      WHERE
        /*  %JoinFKPK(Shop,deleted," = "," AND") */
        Shop.IdCity = deleted.IdCity
    )
    BEGIN
      SELECT @errno  = 30005,
             @errmsg = 'Cannot update City because Shop exists.'
      GOTO error
    END
  END

  /* erwin Builtin Trigger */
  /* City  Line on parent update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="City"
    CHILD_OWNER="", CHILD_TABLE="Line"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_10", FK_COLUMNS="IdCity1" */
  IF
    /* %ParentPK(" OR",UPDATE) */
    UPDATE(IdCity)
  BEGIN
    IF EXISTS (
      SELECT * FROM deleted,Line
      WHERE
        /*  %JoinFKPK(Line,deleted," = "," AND") */
        Line.IdCity1 = deleted.IdCity
    )
    BEGIN
      SELECT @errno  = 30005,
             @errmsg = 'Cannot update City because Line exists.'
      GOTO error
    END
  END

  /* erwin Builtin Trigger */
  /* City  Line on parent update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="City"
    CHILD_OWNER="", CHILD_TABLE="Line"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_9", FK_COLUMNS="IdCity2" */
  IF
    /* %ParentPK(" OR",UPDATE) */
    UPDATE(IdCity)
  BEGIN
    IF EXISTS (
      SELECT * FROM deleted,Line
      WHERE
        /*  %JoinFKPK(Line,deleted," = "," AND") */
        Line.IdCity2 = deleted.IdCity
    )
    BEGIN
      SELECT @errno  = 30005,
             @errmsg = 'Cannot update City because Line exists.'
      GOTO error
    END
  END

  /* erwin Builtin Trigger */
  /* City  Buyer on parent update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="City"
    CHILD_OWNER="", CHILD_TABLE="Buyer"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_2", FK_COLUMNS="IdCity" */
  IF
    /* %ParentPK(" OR",UPDATE) */
    UPDATE(IdCity)
  BEGIN
    IF EXISTS (
      SELECT * FROM deleted,Buyer
      WHERE
        /*  %JoinFKPK(Buyer,deleted," = "," AND") */
        Buyer.IdCity = deleted.IdCity
    )
    BEGIN
      SELECT @errno  = 30005,
             @errmsg = 'Cannot update City because Buyer exists.'
      GOTO error
    END
  END


  /* erwin Builtin Trigger */
  RETURN
error:
   RAISERROR (@errmsg, -- Message text.
              @severity, -- Severity (0~25).
              @state) -- State (0~255).
    rollback transaction
END

go




CREATE TRIGGER tD_Item ON Item FOR DELETE AS
/* erwin Builtin Trigger */
/* DELETE trigger on Item */
BEGIN
  DECLARE  @errno   int,
           @severity int,
           @state    int,
           @errmsg  varchar(255)
    /* erwin Builtin Trigger */
    /* Order  Item on child delete no action */
    /* ERWIN_RELATION:CHECKSUM="00024cc0", PARENT_OWNER="", PARENT_TABLE="Order"
    CHILD_OWNER="", CHILD_TABLE="Item"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_15", FK_COLUMNS="IdOrder" */
    IF EXISTS (SELECT * FROM deleted,Order
      WHERE
        /* %JoinFKPK(deleted,Order," = "," AND") */
        deleted.IdOrder = Order.IdOrder AND
        NOT EXISTS (
          SELECT * FROM Item
          WHERE
            /* %JoinFKPK(Item,Order," = "," AND") */
            Item.IdOrder = Order.IdOrder
        )
    )
    BEGIN
      SELECT @errno  = 30010,
             @errmsg = 'Cannot delete last Item because Order exists.'
      GOTO error
    END

    /* erwin Builtin Trigger */
    /* Article  Item on child delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Article"
    CHILD_OWNER="", CHILD_TABLE="Item"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_14", FK_COLUMNS="IdArticle" */
    IF EXISTS (SELECT * FROM deleted,Article
      WHERE
        /* %JoinFKPK(deleted,Article," = "," AND") */
        deleted.IdArticle = Article.IdArticle AND
        NOT EXISTS (
          SELECT * FROM Item
          WHERE
            /* %JoinFKPK(Item,Article," = "," AND") */
            Item.IdArticle = Article.IdArticle
        )
    )
    BEGIN
      SELECT @errno  = 30010,
             @errmsg = 'Cannot delete last Item because Article exists.'
      GOTO error
    END


    /* erwin Builtin Trigger */
    RETURN
error:
   RAISERROR (@errmsg, -- Message text.
              @severity, -- Severity (0~25).
              @state) -- State (0~255).
    rollback transaction
END

go


CREATE TRIGGER tU_Item ON Item FOR UPDATE AS
/* erwin Builtin Trigger */
/* UPDATE trigger on Item */
BEGIN
  DECLARE  @numrows int,
           @nullcnt int,
           @validcnt int,
           @insIdItem integer,
           @errno   int,
           @severity int,
           @state    int,
           @errmsg  varchar(255)

  SELECT @numrows = @@rowcount
  /* erwin Builtin Trigger */
  /* Order  Item on child update no action */
  /* ERWIN_RELATION:CHECKSUM="0002cc69", PARENT_OWNER="", PARENT_TABLE="Order"
    CHILD_OWNER="", CHILD_TABLE="Item"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_15", FK_COLUMNS="IdOrder" */
  IF
    /* %ChildFK(" OR",UPDATE) */
    UPDATE(IdOrder)
  BEGIN
    SELECT @nullcnt = 0
    SELECT @validcnt = count(*)
      FROM inserted,Order
        WHERE
          /* %JoinFKPK(inserted,Order) */
          inserted.IdOrder = Order.IdOrder
    /* %NotnullFK(inserted," IS NULL","select @nullcnt = count(*) from inserted where"," AND") */
    select @nullcnt = count(*) from inserted where
      inserted.IdOrder IS NULL
    IF @validcnt + @nullcnt != @numrows
    BEGIN
      SELECT @errno  = 30007,
             @errmsg = 'Cannot update Item because Order does not exist.'
      GOTO error
    END
  END

  /* erwin Builtin Trigger */
  /* Article  Item on child update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Article"
    CHILD_OWNER="", CHILD_TABLE="Item"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_14", FK_COLUMNS="IdArticle" */
  IF
    /* %ChildFK(" OR",UPDATE) */
    UPDATE(IdArticle)
  BEGIN
    SELECT @nullcnt = 0
    SELECT @validcnt = count(*)
      FROM inserted,Article
        WHERE
          /* %JoinFKPK(inserted,Article) */
          inserted.IdArticle = Article.IdArticle
    /* %NotnullFK(inserted," IS NULL","select @nullcnt = count(*) from inserted where"," AND") */
    
    IF @validcnt + @nullcnt != @numrows
    BEGIN
      SELECT @errno  = 30007,
             @errmsg = 'Cannot update Item because Article does not exist.'
      GOTO error
    END
  END


  /* erwin Builtin Trigger */
  RETURN
error:
   RAISERROR (@errmsg, -- Message text.
              @severity, -- Severity (0~25).
              @state) -- State (0~255).
    rollback transaction
END

go




CREATE TRIGGER tD_Line ON Line FOR DELETE AS
/* erwin Builtin Trigger */
/* DELETE trigger on Line */
BEGIN
  DECLARE  @errno   int,
           @severity int,
           @state    int,
           @errmsg  varchar(255)
    /* erwin Builtin Trigger */
    /* City  Line on child delete no action */
    /* ERWIN_RELATION:CHECKSUM="00022a21", PARENT_OWNER="", PARENT_TABLE="City"
    CHILD_OWNER="", CHILD_TABLE="Line"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_10", FK_COLUMNS="IdCity1" */
    IF EXISTS (SELECT * FROM deleted,City
      WHERE
        /* %JoinFKPK(deleted,City," = "," AND") */
        deleted.IdCity1 = City.IdCity AND
        NOT EXISTS (
          SELECT * FROM Line
          WHERE
            /* %JoinFKPK(Line,City," = "," AND") */
            Line.IdCity1 = City.IdCity
        )
    )
    BEGIN
      SELECT @errno  = 30010,
             @errmsg = 'Cannot delete last Line because City exists.'
      GOTO error
    END

    /* erwin Builtin Trigger */
    /* City  Line on child delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="City"
    CHILD_OWNER="", CHILD_TABLE="Line"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_9", FK_COLUMNS="IdCity2" */
    IF EXISTS (SELECT * FROM deleted,City
      WHERE
        /* %JoinFKPK(deleted,City," = "," AND") */
        deleted.IdCity2 = City.IdCity AND
        NOT EXISTS (
          SELECT * FROM Line
          WHERE
            /* %JoinFKPK(Line,City," = "," AND") */
            Line.IdCity2 = City.IdCity
        )
    )
    BEGIN
      SELECT @errno  = 30010,
             @errmsg = 'Cannot delete last Line because City exists.'
      GOTO error
    END


    /* erwin Builtin Trigger */
    RETURN
error:
   RAISERROR (@errmsg, -- Message text.
              @severity, -- Severity (0~25).
              @state) -- State (0~255).
    rollback transaction
END

go


CREATE TRIGGER tU_Line ON Line FOR UPDATE AS
/* erwin Builtin Trigger */
/* UPDATE trigger on Line */
BEGIN
  DECLARE  @numrows int,
           @nullcnt int,
           @validcnt int,
           @insIdLine integer,
           @errno   int,
           @severity int,
           @state    int,
           @errmsg  varchar(255)

  SELECT @numrows = @@rowcount
  /* erwin Builtin Trigger */
  /* City  Line on child update no action */
  /* ERWIN_RELATION:CHECKSUM="000276af", PARENT_OWNER="", PARENT_TABLE="City"
    CHILD_OWNER="", CHILD_TABLE="Line"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_10", FK_COLUMNS="IdCity1" */
  IF
    /* %ChildFK(" OR",UPDATE) */
    UPDATE(IdCity1)
  BEGIN
    SELECT @nullcnt = 0
    SELECT @validcnt = count(*)
      FROM inserted,City
        WHERE
          /* %JoinFKPK(inserted,City) */
          inserted.IdCity1 = City.IdCity
    /* %NotnullFK(inserted," IS NULL","select @nullcnt = count(*) from inserted where"," AND") */
    
    IF @validcnt + @nullcnt != @numrows
    BEGIN
      SELECT @errno  = 30007,
             @errmsg = 'Cannot update Line because City does not exist.'
      GOTO error
    END
  END

  /* erwin Builtin Trigger */
  /* City  Line on child update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="City"
    CHILD_OWNER="", CHILD_TABLE="Line"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_9", FK_COLUMNS="IdCity2" */
  IF
    /* %ChildFK(" OR",UPDATE) */
    UPDATE(IdCity2)
  BEGIN
    SELECT @nullcnt = 0
    SELECT @validcnt = count(*)
      FROM inserted,City
        WHERE
          /* %JoinFKPK(inserted,City) */
          inserted.IdCity2 = City.IdCity
    /* %NotnullFK(inserted," IS NULL","select @nullcnt = count(*) from inserted where"," AND") */
    
    IF @validcnt + @nullcnt != @numrows
    BEGIN
      SELECT @errno  = 30007,
             @errmsg = 'Cannot update Line because City does not exist.'
      GOTO error
    END
  END


  /* erwin Builtin Trigger */
  RETURN
error:
   RAISERROR (@errmsg, -- Message text.
              @severity, -- Severity (0~25).
              @state) -- State (0~255).
    rollback transaction
END

go




CREATE TRIGGER tD_Order ON Order FOR DELETE AS
/* erwin Builtin Trigger */
/* DELETE trigger on Order */
BEGIN
  DECLARE  @errno   int,
           @severity int,
           @state    int,
           @errmsg  varchar(255)
    /* erwin Builtin Trigger */
    /* Order  Item on parent delete no action */
    /* ERWIN_RELATION:CHECKSUM="0002ffff", PARENT_OWNER="", PARENT_TABLE="Order"
    CHILD_OWNER="", CHILD_TABLE="Item"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_15", FK_COLUMNS="IdOrder" */
    IF EXISTS (
      SELECT * FROM deleted,Item
      WHERE
        /*  %JoinFKPK(Item,deleted," = "," AND") */
        Item.IdOrder = deleted.IdOrder
    )
    BEGIN
      SELECT @errno  = 30001,
             @errmsg = 'Cannot delete Order because Item exists.'
      GOTO error
    END

    /* erwin Builtin Trigger */
    /* Order  Transaction on parent delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Order"
    CHILD_OWNER="", CHILD_TABLE="Transaction"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_11", FK_COLUMNS="IdOrder" */
    IF EXISTS (
      SELECT * FROM deleted,Transaction
      WHERE
        /*  %JoinFKPK(Transaction,deleted," = "," AND") */
        Transaction.IdOrder = deleted.IdOrder
    )
    BEGIN
      SELECT @errno  = 30001,
             @errmsg = 'Cannot delete Order because Transaction exists.'
      GOTO error
    END

    /* erwin Builtin Trigger */
    /* Buyer  Order on child delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Buyer"
    CHILD_OWNER="", CHILD_TABLE="Order"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_3", FK_COLUMNS="IdBuyer" */
    IF EXISTS (SELECT * FROM deleted,Buyer
      WHERE
        /* %JoinFKPK(deleted,Buyer," = "," AND") */
        deleted.IdBuyer = Buyer.IdBuyer AND
        NOT EXISTS (
          SELECT * FROM Order
          WHERE
            /* %JoinFKPK(Order,Buyer," = "," AND") */
            Order.IdBuyer = Buyer.IdBuyer
        )
    )
    BEGIN
      SELECT @errno  = 30010,
             @errmsg = 'Cannot delete last Order because Buyer exists.'
      GOTO error
    END


    /* erwin Builtin Trigger */
    RETURN
error:
   RAISERROR (@errmsg, -- Message text.
              @severity, -- Severity (0~25).
              @state) -- State (0~255).
    rollback transaction
END

go


CREATE TRIGGER tU_Order ON Order FOR UPDATE AS
/* erwin Builtin Trigger */
/* UPDATE trigger on Order */
BEGIN
  DECLARE  @numrows int,
           @nullcnt int,
           @validcnt int,
           @insIdOrder integer,
           @errno   int,
           @severity int,
           @state    int,
           @errmsg  varchar(255)

  SELECT @numrows = @@rowcount
  /* erwin Builtin Trigger */
  /* Order  Item on parent update no action */
  /* ERWIN_RELATION:CHECKSUM="00036f82", PARENT_OWNER="", PARENT_TABLE="Order"
    CHILD_OWNER="", CHILD_TABLE="Item"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_15", FK_COLUMNS="IdOrder" */
  IF
    /* %ParentPK(" OR",UPDATE) */
    UPDATE(IdOrder)
  BEGIN
    IF EXISTS (
      SELECT * FROM deleted,Item
      WHERE
        /*  %JoinFKPK(Item,deleted," = "," AND") */
        Item.IdOrder = deleted.IdOrder
    )
    BEGIN
      SELECT @errno  = 30005,
             @errmsg = 'Cannot update Order because Item exists.'
      GOTO error
    END
  END

  /* erwin Builtin Trigger */
  /* Order  Transaction on parent update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Order"
    CHILD_OWNER="", CHILD_TABLE="Transaction"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_11", FK_COLUMNS="IdOrder" */
  IF
    /* %ParentPK(" OR",UPDATE) */
    UPDATE(IdOrder)
  BEGIN
    IF EXISTS (
      SELECT * FROM deleted,Transaction
      WHERE
        /*  %JoinFKPK(Transaction,deleted," = "," AND") */
        Transaction.IdOrder = deleted.IdOrder
    )
    BEGIN
      SELECT @errno  = 30005,
             @errmsg = 'Cannot update Order because Transaction exists.'
      GOTO error
    END
  END

  /* erwin Builtin Trigger */
  /* Buyer  Order on child update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Buyer"
    CHILD_OWNER="", CHILD_TABLE="Order"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_3", FK_COLUMNS="IdBuyer" */
  IF
    /* %ChildFK(" OR",UPDATE) */
    UPDATE(IdBuyer)
  BEGIN
    SELECT @nullcnt = 0
    SELECT @validcnt = count(*)
      FROM inserted,Buyer
        WHERE
          /* %JoinFKPK(inserted,Buyer) */
          inserted.IdBuyer = Buyer.IdBuyer
    /* %NotnullFK(inserted," IS NULL","select @nullcnt = count(*) from inserted where"," AND") */
    select @nullcnt = count(*) from inserted where
      inserted.IdBuyer IS NULL
    IF @validcnt + @nullcnt != @numrows
    BEGIN
      SELECT @errno  = 30007,
             @errmsg = 'Cannot update Order because Buyer does not exist.'
      GOTO error
    END
  END


  /* erwin Builtin Trigger */
  RETURN
error:
   RAISERROR (@errmsg, -- Message text.
              @severity, -- Severity (0~25).
              @state) -- State (0~255).
    rollback transaction
END

go




CREATE TRIGGER tD_Shop ON Shop FOR DELETE AS
/* erwin Builtin Trigger */
/* DELETE trigger on Shop */
BEGIN
  DECLARE  @errno   int,
           @severity int,
           @state    int,
           @errmsg  varchar(255)
    /* erwin Builtin Trigger */
    /* Shop  Transaction on parent delete no action */
    /* ERWIN_RELATION:CHECKSUM="0002e4b0", PARENT_OWNER="", PARENT_TABLE="Shop"
    CHILD_OWNER="", CHILD_TABLE="Transaction"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_16", FK_COLUMNS="IdShop" */
    IF EXISTS (
      SELECT * FROM deleted,Transaction
      WHERE
        /*  %JoinFKPK(Transaction,deleted," = "," AND") */
        Transaction.IdShop = deleted.IdShop
    )
    BEGIN
      SELECT @errno  = 30001,
             @errmsg = 'Cannot delete Shop because Transaction exists.'
      GOTO error
    END

    /* erwin Builtin Trigger */
    /* Shop  Article on parent delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Shop"
    CHILD_OWNER="", CHILD_TABLE="Article"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_1", FK_COLUMNS="IdShop" */
    IF EXISTS (
      SELECT * FROM deleted,Article
      WHERE
        /*  %JoinFKPK(Article,deleted," = "," AND") */
        Article.IdShop = deleted.IdShop
    )
    BEGIN
      SELECT @errno  = 30001,
             @errmsg = 'Cannot delete Shop because Article exists.'
      GOTO error
    END

    /* erwin Builtin Trigger */
    /* City  Shop on child delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="City"
    CHILD_OWNER="", CHILD_TABLE="Shop"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_12", FK_COLUMNS="IdCity" */
    IF EXISTS (SELECT * FROM deleted,City
      WHERE
        /* %JoinFKPK(deleted,City," = "," AND") */
        deleted.IdCity = City.IdCity AND
        NOT EXISTS (
          SELECT * FROM Shop
          WHERE
            /* %JoinFKPK(Shop,City," = "," AND") */
            Shop.IdCity = City.IdCity
        )
    )
    BEGIN
      SELECT @errno  = 30010,
             @errmsg = 'Cannot delete last Shop because City exists.'
      GOTO error
    END


    /* erwin Builtin Trigger */
    RETURN
error:
   RAISERROR (@errmsg, -- Message text.
              @severity, -- Severity (0~25).
              @state) -- State (0~255).
    rollback transaction
END

go


CREATE TRIGGER tU_Shop ON Shop FOR UPDATE AS
/* erwin Builtin Trigger */
/* UPDATE trigger on Shop */
BEGIN
  DECLARE  @numrows int,
           @nullcnt int,
           @validcnt int,
           @insIdShop integer,
           @errno   int,
           @severity int,
           @state    int,
           @errmsg  varchar(255)

  SELECT @numrows = @@rowcount
  /* erwin Builtin Trigger */
  /* Shop  Transaction on parent update no action */
  /* ERWIN_RELATION:CHECKSUM="000361e0", PARENT_OWNER="", PARENT_TABLE="Shop"
    CHILD_OWNER="", CHILD_TABLE="Transaction"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_16", FK_COLUMNS="IdShop" */
  IF
    /* %ParentPK(" OR",UPDATE) */
    UPDATE(IdShop)
  BEGIN
    IF EXISTS (
      SELECT * FROM deleted,Transaction
      WHERE
        /*  %JoinFKPK(Transaction,deleted," = "," AND") */
        Transaction.IdShop = deleted.IdShop
    )
    BEGIN
      SELECT @errno  = 30005,
             @errmsg = 'Cannot update Shop because Transaction exists.'
      GOTO error
    END
  END

  /* erwin Builtin Trigger */
  /* Shop  Article on parent update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Shop"
    CHILD_OWNER="", CHILD_TABLE="Article"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_1", FK_COLUMNS="IdShop" */
  IF
    /* %ParentPK(" OR",UPDATE) */
    UPDATE(IdShop)
  BEGIN
    IF EXISTS (
      SELECT * FROM deleted,Article
      WHERE
        /*  %JoinFKPK(Article,deleted," = "," AND") */
        Article.IdShop = deleted.IdShop
    )
    BEGIN
      SELECT @errno  = 30005,
             @errmsg = 'Cannot update Shop because Article exists.'
      GOTO error
    END
  END

  /* erwin Builtin Trigger */
  /* City  Shop on child update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="City"
    CHILD_OWNER="", CHILD_TABLE="Shop"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_12", FK_COLUMNS="IdCity" */
  IF
    /* %ChildFK(" OR",UPDATE) */
    UPDATE(IdCity)
  BEGIN
    SELECT @nullcnt = 0
    SELECT @validcnt = count(*)
      FROM inserted,City
        WHERE
          /* %JoinFKPK(inserted,City) */
          inserted.IdCity = City.IdCity
    /* %NotnullFK(inserted," IS NULL","select @nullcnt = count(*) from inserted where"," AND") */
    select @nullcnt = count(*) from inserted where
      inserted.IdCity IS NULL
    IF @validcnt + @nullcnt != @numrows
    BEGIN
      SELECT @errno  = 30007,
             @errmsg = 'Cannot update Shop because City does not exist.'
      GOTO error
    END
  END


  /* erwin Builtin Trigger */
  RETURN
error:
   RAISERROR (@errmsg, -- Message text.
              @severity, -- Severity (0~25).
              @state) -- State (0~255).
    rollback transaction
END

go




CREATE TRIGGER tD_Transaction ON Transaction FOR DELETE AS
/* erwin Builtin Trigger */
/* DELETE trigger on Transaction */
BEGIN
  DECLARE  @errno   int,
           @severity int,
           @state    int,
           @errmsg  varchar(255)
    /* erwin Builtin Trigger */
    /* Buyer  Transaction on child delete no action */
    /* ERWIN_RELATION:CHECKSUM="00036b08", PARENT_OWNER="", PARENT_TABLE="Buyer"
    CHILD_OWNER="", CHILD_TABLE="Transaction"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_17", FK_COLUMNS="IdBuyer" */
    IF EXISTS (SELECT * FROM deleted,Buyer
      WHERE
        /* %JoinFKPK(deleted,Buyer," = "," AND") */
        deleted.IdBuyer = Buyer.IdBuyer AND
        NOT EXISTS (
          SELECT * FROM Transaction
          WHERE
            /* %JoinFKPK(Transaction,Buyer," = "," AND") */
            Transaction.IdBuyer = Buyer.IdBuyer
        )
    )
    BEGIN
      SELECT @errno  = 30010,
             @errmsg = 'Cannot delete last Transaction because Buyer exists.'
      GOTO error
    END

    /* erwin Builtin Trigger */
    /* Shop  Transaction on child delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Shop"
    CHILD_OWNER="", CHILD_TABLE="Transaction"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_16", FK_COLUMNS="IdShop" */
    IF EXISTS (SELECT * FROM deleted,Shop
      WHERE
        /* %JoinFKPK(deleted,Shop," = "," AND") */
        deleted.IdShop = Shop.IdShop AND
        NOT EXISTS (
          SELECT * FROM Transaction
          WHERE
            /* %JoinFKPK(Transaction,Shop," = "," AND") */
            Transaction.IdShop = Shop.IdShop
        )
    )
    BEGIN
      SELECT @errno  = 30010,
             @errmsg = 'Cannot delete last Transaction because Shop exists.'
      GOTO error
    END

    /* erwin Builtin Trigger */
    /* Order  Transaction on child delete no action */
    /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Order"
    CHILD_OWNER="", CHILD_TABLE="Transaction"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_11", FK_COLUMNS="IdOrder" */
    IF EXISTS (SELECT * FROM deleted,Order
      WHERE
        /* %JoinFKPK(deleted,Order," = "," AND") */
        deleted.IdOrder = Order.IdOrder AND
        NOT EXISTS (
          SELECT * FROM Transaction
          WHERE
            /* %JoinFKPK(Transaction,Order," = "," AND") */
            Transaction.IdOrder = Order.IdOrder
        )
    )
    BEGIN
      SELECT @errno  = 30010,
             @errmsg = 'Cannot delete last Transaction because Order exists.'
      GOTO error
    END


    /* erwin Builtin Trigger */
    RETURN
error:
   RAISERROR (@errmsg, -- Message text.
              @severity, -- Severity (0~25).
              @state) -- State (0~255).
    rollback transaction
END

go


CREATE TRIGGER tU_Transaction ON Transaction FOR UPDATE AS
/* erwin Builtin Trigger */
/* UPDATE trigger on Transaction */
BEGIN
  DECLARE  @numrows int,
           @nullcnt int,
           @validcnt int,
           @insIdOrder integer, 
           @insIdShop integer, 
           @insIdBuyer integer,
           @errno   int,
           @severity int,
           @state    int,
           @errmsg  varchar(255)

  SELECT @numrows = @@rowcount
  /* erwin Builtin Trigger */
  /* Buyer  Transaction on child update no action */
  /* ERWIN_RELATION:CHECKSUM="0003d024", PARENT_OWNER="", PARENT_TABLE="Buyer"
    CHILD_OWNER="", CHILD_TABLE="Transaction"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_17", FK_COLUMNS="IdBuyer" */
  IF
    /* %ChildFK(" OR",UPDATE) */
    UPDATE(IdBuyer)
  BEGIN
    SELECT @nullcnt = 0
    SELECT @validcnt = count(*)
      FROM inserted,Buyer
        WHERE
          /* %JoinFKPK(inserted,Buyer) */
          inserted.IdBuyer = Buyer.IdBuyer
    /* %NotnullFK(inserted," IS NULL","select @nullcnt = count(*) from inserted where"," AND") */
    
    IF @validcnt + @nullcnt != @numrows
    BEGIN
      SELECT @errno  = 30007,
             @errmsg = 'Cannot update Transaction because Buyer does not exist.'
      GOTO error
    END
  END

  /* erwin Builtin Trigger */
  /* Shop  Transaction on child update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Shop"
    CHILD_OWNER="", CHILD_TABLE="Transaction"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_16", FK_COLUMNS="IdShop" */
  IF
    /* %ChildFK(" OR",UPDATE) */
    UPDATE(IdShop)
  BEGIN
    SELECT @nullcnt = 0
    SELECT @validcnt = count(*)
      FROM inserted,Shop
        WHERE
          /* %JoinFKPK(inserted,Shop) */
          inserted.IdShop = Shop.IdShop
    /* %NotnullFK(inserted," IS NULL","select @nullcnt = count(*) from inserted where"," AND") */
    
    IF @validcnt + @nullcnt != @numrows
    BEGIN
      SELECT @errno  = 30007,
             @errmsg = 'Cannot update Transaction because Shop does not exist.'
      GOTO error
    END
  END

  /* erwin Builtin Trigger */
  /* Order  Transaction on child update no action */
  /* ERWIN_RELATION:CHECKSUM="00000000", PARENT_OWNER="", PARENT_TABLE="Order"
    CHILD_OWNER="", CHILD_TABLE="Transaction"
    P2C_VERB_PHRASE="", C2P_VERB_PHRASE="", 
    FK_CONSTRAINT="R_11", FK_COLUMNS="IdOrder" */
  IF
    /* %ChildFK(" OR",UPDATE) */
    UPDATE(IdOrder)
  BEGIN
    SELECT @nullcnt = 0
    SELECT @validcnt = count(*)
      FROM inserted,Order
        WHERE
          /* %JoinFKPK(inserted,Order) */
          inserted.IdOrder = Order.IdOrder
    /* %NotnullFK(inserted," IS NULL","select @nullcnt = count(*) from inserted where"," AND") */
    
    IF @validcnt + @nullcnt != @numrows
    BEGIN
      SELECT @errno  = 30007,
             @errmsg = 'Cannot update Transaction because Order does not exist.'
      GOTO error
    END
  END


  /* erwin Builtin Trigger */
  RETURN
error:
   RAISERROR (@errmsg, -- Message text.
              @severity, -- Severity (0~25).
              @state) -- State (0~255).
    rollback transaction
END

go


