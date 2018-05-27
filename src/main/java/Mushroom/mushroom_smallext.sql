CREATE TABLE Mushrooms(
   caseId                   INTEGER NOT NULL PRIMARY KEY
  ,isPoisonous              VARCHAR(1) NOT NULL
  ,capshape                 VARCHAR(1) NOT NULL
  ,capsurface               VARCHAR(1) NOT NULL
  ,capcolor                 VARCHAR(1) NOT NULL
  ,bruises                  VARCHAR(1) NOT NULL
  ,odor                     VARCHAR(1) NOT NULL
  ,gillattachment           VARCHAR(1) NOT NULL
  ,gillspacing              VARCHAR(1) NOT NULL
  ,gillsize                 VARCHAR(1) NOT NULL
  ,gillcolor                VARCHAR(1) NOT NULL
  ,stalkshape               VARCHAR(1) NOT NULL
  ,stalkroot                VARCHAR(1) NOT NULL
  ,stalksurfaceabovering    VARCHAR(1) NOT NULL
  ,stalksurfacebelowring    VARCHAR(1) NOT NULL
  ,stalkcolorabovering      VARCHAR(1) NOT NULL
  ,stalkcolorbelowring      VARCHAR(1) NOT NULL
  ,veiltype                 VARCHAR(1) NOT NULL
  ,veilcolor                VARCHAR(1) NOT NULL
  ,ringnumber               VARCHAR(1) NOT NULL
  ,ringtype                 VARCHAR(1) NOT NULL
  ,sporeprintcolor          VARCHAR(1) NOT NULL
  ,population               VARCHAR(1) NOT NULL
  ,habitat                  VARCHAR(1) NOT NULL
);
INSERT INTO Mushrooms(caseId,isPoisonous,capshape,capsurface,capcolor,bruises,odor,gillattachment,gillspacing,gillsize,gillcolor,stalkshape,stalkroot,stalksurfaceabovering,stalksurfacebelowring,stalkcolorabovering,stalkcolorbelowring,veiltype,veilcolor,ringnumber,ringtype,sporeprintcolor,population,habitat) VALUES (1,'p','x','s','n','t','p','f','c','n','k','e','e','s','s','w','w','p','w','o','p','k','s','u');
