CREATE TABLE BreastCancer(
   caseId                   INTEGER  NOT NULL PRIMARY KEY
  ,clumpThickness           INTEGER  NOT NULL
  ,uniformityOfCellSize     INTEGER  NOT NULL
  ,uniformityOfCellShape    INTEGER  NOT NULL
  ,marginalAdhesion         INTEGER  NOT NULL
  ,singleEpithelialCellSize INTEGER  NOT NULL
  ,bareNuclei               INTEGER  NOT NULL
  ,blandChromatin           INTEGER  NOT NULL
  ,normalNucleoli           INTEGER  NOT NULL
  ,mitoses                  INTEGER  NOT NULL
  ,classification           INTEGER  NOT NULL
);
INSERT INTO BreastCancer(caseId,clumpThickness,uniformityOfCellSize,uniformityOfCellShape,marginalAdhesion,singleEpithelialCellSize,bareNuclei,blandChromatin,normalNucleoli,mitoses,classification) VALUES (1000025,5,1,1,1,2,1,3,1,1,2);
