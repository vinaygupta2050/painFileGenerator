package com.pain001.db;

import java.util.*;
import java.util.logging.Logger;

public class DatabaseValidator {
    private static final Logger LOGGER = Logger.getLogger(DatabaseValidator.class.getName());

    private static final Set<String> REQUIRED_COLUMNS = new HashSet<String>(Arrays.asList(
            "id",
            "date",
            "nb_of_txs",
            "initiator_name",
            "initiator_street_name",
            "initiator_building_number",
            "initiator_postal_code",
            "initiator_town_name",
            "initiator_country_code",
            "payment_information_id",
            "payment_method",
            "batch_booking",
            "requested_execution_date",
            "debtor_name",
            "debtor_street_name",
            "debtor_building_number",
            "debtor_postal_code",
            "debtor_town_name",
            "debtor_country_code",
            "debtor_account_IBAN",
            "debtor_agent_BIC",
            "charge_bearer",
            "payment_id",
            "payment_amount",
            "currency",
            "payment_currency",
            "ctrl_sum",
            "creditor_agent_BIC",
            "creditor_name",
            "creditor_street_name",
            "creditor_building_number",
            "creditor_postal_code",
            "creditor_town_name",
            "creditor_country_code",
            "creditor_account_IBAN",
            "purpose_code",
            "reference_number",
            "reference_date",
            "service_level_code",
            "end_to_end_id",
            "payment_instruction_id",
            "instruction_id",
            "category_purpose",
            "remittance_info_unstructured",
            "remittance_info_structured",
            "addtl_end_to_end_id",
            "payment_info_structured",
            "forwarding_agent_BIC",
            "remittance_information"
    ));

    public static boolean validateDbData(List<Map<String, Object>> data) {
        for (Map<String, Object> row : data) {
            for (String column : REQUIRED_COLUMNS) {
                if (!row.containsKey(column) || row.get(column) == null) {
                    LOGGER.severe("Error: Missing value for column '" + column + "' in row: " + row);
                    return false;
                }
            }
        }
        return true;
    }
}
