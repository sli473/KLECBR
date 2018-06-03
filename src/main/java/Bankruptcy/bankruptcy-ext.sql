CREATE TABLE Bankruptcy(
   caseId                      INTEGER  NOT NULL PRIMARY KEY
  ,NP_Over_TA                  NUMERIC(9,6) NOT NULL
  ,CA_Over_STL                 NUMERIC(10,6) NOT NULL
  ,EBIT_Over_TA                NUMERIC(9,6) NOT NULL
  ,Sum_Of_Profit_EI_FE_OVER_TA NUMERIC(9,6) NOT NULL
  ,GP_Over_STL                 NUMERIC(9,6) NOT NULL
  ,Sum_Of_PI_Over_TA           NUMERIC(9,6) NOT NULL
  ,Sum_Of_PD_Over_TL           NUMERIC(9,6) NOT NULL
  ,GP_Over_TA                  NUMERIC(9,6) NOT NULL
  ,GP_3Y_Over_TA               NUMERIC(9,6) NOT NULL
  ,Sum_Of_NPD_Over_TL          NUMERIC(9,6) NOT NULL
  ,CA_Over_TL                  NUMERIC(9,6) NOT NULL
  ,classification              BIT  NOT NULL
);
INSERT INTO Bankruptcy(caseId,NP_Over_TA,CA_Over_STL,EBIT_Over_TA,Sum_Of_Profit_EI_FE_OVER_TA,GP_Over_STL,Sum_Of_PI_Over_TA,Sum_Of_PD_Over_TL,GP_Over_TA,GP_3Y_Over_TA,Sum_Of_NPD_Over_TL,CA_Over_TL,classification) VALUES (1,0.081483,2.4928,0.092704,0.092704,0.30163,0.092704,0.39803,0.092704,0.17193,0.36152,2.4928,0);
