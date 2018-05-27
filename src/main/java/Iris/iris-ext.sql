iris-test.sqlCREATE TABLE Iris(
   caseId        INTEGER  NOT NULL PRIMARY KEY 
  ,SepalLengthCm NUMERIC(3,1) NOT NULL
  ,SepalWidthCm  NUMERIC(3,1) NOT NULL
  ,PetalLengthCm NUMERIC(3,1) NOT NULL
  ,PetalWidthCm  NUMERIC(3,1) NOT NULL
  ,Species       VARCHAR(15) NOT NULL
);
INSERT INTO Iris(caseId,SepalLengthCm,SepalWidthCm,PetalLengthCm,PetalWidthCm,Species) VALUES (1,4.9,3,1.4,0.2,'Iris-setosa');