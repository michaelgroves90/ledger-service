package com.michaelgroves.ledger_service.domain;

import com.michaelgroves.ledger_service.exceptions.UnbalancedEntryException;

public class NewEntry {

    public String balance(int debit, int fees, int credit) {
        int totalDebit = debit + fees;
        if  (totalDebit - credit == 0) {
            return "Balanced";
        } else {
            throw new UnbalancedEntryException("Not Balanced");
        }
    };

}
