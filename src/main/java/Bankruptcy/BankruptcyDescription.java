package Bankruptcy;

import KLECBR.KLECaseComponent;
import jcolibri.cbrcore.Attribute;

import java.util.HashMap;

public class BankruptcyDescription implements KLECaseComponent {

    int caseId;
    int NP_Over_TA;
    int CA_Over_STL;
    int EBIT_Over_TA;
    int Sum_Of_Profit_EI_FE_OVER_TA;
    int GP_Over_STL;
    int Sum_Of_PI_Over_TA;
    int Sum_Of_PD_Over_TL;
    int GP_Over_TA;
    int GP_3Y_Over_TA;
    int Sum_Of_NPD_Over_TL;
    int CA_Over_TL;

    public BankruptcyDescription() {}

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public int getNP_Over_TA() {
        return NP_Over_TA;
    }

    public void setNP_Over_TA(int NP_Over_TA) {
        this.NP_Over_TA = NP_Over_TA;
    }

    public int getCA_Over_STL() {
        return CA_Over_STL;
    }

    public void setCA_Over_STL(int CA_Over_STL) {
        this.CA_Over_STL = CA_Over_STL;
    }

    public int getEBIT_Over_TA() {
        return EBIT_Over_TA;
    }

    public void setEBIT_Over_TA(int EBIT_Over_TA) {
        this.EBIT_Over_TA = EBIT_Over_TA;
    }

    public int getSum_Of_Profit_EI_FE_OVER_TA() {
        return Sum_Of_Profit_EI_FE_OVER_TA;
    }

    public void setSum_Of_Profit_EI_FE_OVER_TA(int sum_Of_Profit_EI_FE_OVER_TA) {
        Sum_Of_Profit_EI_FE_OVER_TA = sum_Of_Profit_EI_FE_OVER_TA;
    }

    public int getGP_Over_STL() {
        return GP_Over_STL;
    }

    public void setGP_Over_STL(int GP_Over_STL) {
        this.GP_Over_STL = GP_Over_STL;
    }

    public int getSum_Of_PI_Over_TA() {
        return Sum_Of_PI_Over_TA;
    }

    public void setSum_Of_PI_Over_TA(int sum_Of_PI_Over_TA) {
        Sum_Of_PI_Over_TA = sum_Of_PI_Over_TA;
    }

    public int getSum_Of_PD_Over_TL() {
        return Sum_Of_PD_Over_TL;
    }

    public void setSum_Of_PD_Over_TL(int sum_Of_PD_Over_TL) {
        Sum_Of_PD_Over_TL = sum_Of_PD_Over_TL;
    }

    public int getGP_Over_TA() {
        return GP_Over_TA;
    }

    public void setGP_Over_TA(int GP_Over_TA) {
        this.GP_Over_TA = GP_Over_TA;
    }

    public int getGP_3Y_Over_TA() {
        return GP_3Y_Over_TA;
    }

    public void setGP_3Y_Over_TA(int GP_3Y_Over_TA) {
        this.GP_3Y_Over_TA = GP_3Y_Over_TA;
    }

    public int getSum_Of_NPD_Over_TL() {
        return Sum_Of_NPD_Over_TL;
    }

    public void setSum_Of_NPD_Over_TL(int sum_Of_NPD_Over_TL) {
        Sum_Of_NPD_Over_TL = sum_Of_NPD_Over_TL;
    }

    public int getCA_Over_TL() {
        return CA_Over_TL;
    }

    public void setCA_Over_TL(int CA_Over_TL) {
        this.CA_Over_TL = CA_Over_TL;
    }

    @Override
    public Attribute getIdAttribute() {
        return new Attribute("caseId", this.getClass());
    }

    @Override
    public HashMap<String, String> getHashMap() {

        HashMap<String, String> hm = new HashMap<>();

        hm.put("net profit / total assets", Integer.toString(NP_Over_TA));
        hm.put("current assets / short-term liabilities", Integer.toString(CA_Over_STL));
        hm.put("EBIT / total assets", Integer.toString(EBIT_Over_TA));
        hm.put("(gross profit + extraordinary items + financial expenses) / total assets", Integer.toString(Sum_Of_Profit_EI_FE_OVER_TA));
        hm.put("gross profit / short-term liabilities", Integer.toString(GP_Over_STL));
        hm.put("(gross profit + interest) / total assets", Integer.toString(Sum_Of_Profit_EI_FE_OVER_TA));
        hm.put("(gross profit + depreciation) / total liabilities", Integer.toString(Sum_Of_PD_Over_TL));
        hm.put("gross profit / total assets", Integer.toString(GP_Over_TA));
        hm.put("gross profit (in 3 years) / total assets", Integer.toString(GP_3Y_Over_TA));
        hm.put("(net profit + depreciation) / total liabilities", Integer.toString(Sum_Of_NPD_Over_TL));
        hm.put("current assets / total liabilities", Integer.toString(CA_Over_STL));

        return hm;
    }
}
