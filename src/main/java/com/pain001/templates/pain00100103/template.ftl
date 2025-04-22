<?xml version="1.0" encoding="UTF-8"?>
<Document xmlns="urn:iso:std:iso:20022:tech:xsd:pain.001.001.03"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="urn:iso:std:iso:20022:tech:xsd:pain.001.001.03 pain.001.001.03.xsd">
    <CstmrCdtTrfInitn>
        <GrpHdr>
            <MsgId>${id}</MsgId>
            <CreDtTm>${date}</CreDtTm>
            <NbOfTxs>${nb_of_txs}</NbOfTxs>
            <CtrlSum>${ctrl_sum}</CtrlSum>
            <InitgPty>
                <Nm>${initiator_name}</Nm>
                <PstlAdr>
                    <#if initiator_street_name??><StrtNm>${initiator_street_name}</StrtNm></#if>
                    <#if initiator_building_number??><BldgNb>${initiator_building_number}</BldgNb></#if>
                    <#if initiator_postal_code??><PstCd>${initiator_postal_code}</PstCd></#if>
                    <#if initiator_town_name??><TwnNm>${initiator_town_name}</TwnNm></#if>
                    <#if initiator_country_code??><Ctry>${initiator_country_code}</Ctry></#if>
                </PstlAdr>
            </InitgPty>
        </GrpHdr>
        <PmtInf>
            <PmtInfId>${payment_information_id}</PmtInfId>
            <PmtMtd>${payment_method}</PmtMtd>
            <BtchBookg>${batch_booking}</BtchBookg>
            <ReqdExctnDt>${requested_execution_date}</ReqdExctnDt>
            <Dbtr>
                <Nm>${debtor_name}</Nm>
                <PstlAdr>
                    <#if debtor_street_name??><StrtNm>${debtor_street_name}</StrtNm></#if>
                    <#if debtor_building_number??><BldgNb>${debtor_building_number}</BldgNb></#if>
                    <#if debtor_postal_code??><PstCd>${debtor_postal_code}</PstCd></#if>
                    <#if debtor_town_name??><TwnNm>${debtor_town_name}</TwnNm></#if>
                    <#if debtor_country_code??><Ctry>${debtor_country_code}</Ctry></#if>
                </PstlAdr>
            </Dbtr>
            <DbtrAcct>
                <Id>
                    <IBAN>${debtor_account_IBAN}</IBAN>
                </Id>
            </DbtrAcct>
            <DbtrAgt>
                <FinInstnId>
                    <BIC>${debtor_agent_BIC}</BIC>
                </FinInstnId>
            </DbtrAgt>
            <ChrgBr>${charge_bearer}</ChrgBr>
            <#list transactions as tx>
                <CdtTrfTxInf>
                    <PmtId>
                        <InstrId>TX-${tx_index + 1}</InstrId>
                        <EndToEndId>${tx.payment_id}</EndToEndId>
                    </PmtId>
                    <Amt>
                        <InstdAmt Ccy="${tx.payment_currency}">${tx.payment_amount}</InstdAmt>
                    </Amt>
                    <ChrgBr>${tx.charge_bearer}</ChrgBr>
                    <CdtrAgt>
                        <FinInstnId>
                            <BIC>${tx.creditor_agent_BIC}</BIC>
                        </FinInstnId>
                    </CdtrAgt>
                    <Cdtr>
                        <Nm>${tx.creditor_name}</Nm>
                        <PstlAdr>
                            <#if tx.creditor_street_name??><StrtNm>${tx.creditor_street_name}</StrtNm></#if>
                            <#if tx.creditor_building_number??><BldgNb>${tx.creditor_building_number}</BldgNb></#if>
                            <#if tx.creditor_postal_code??><PstCd>${tx.creditor_postal_code}</PstCd></#if>
                            <#if tx.creditor_town_name??><TwnNm>${tx.creditor_town_name}</TwnNm></#if>
                            <#if tx.creditor_country_code??><Ctry>${tx.creditor_country_code}</Ctry></#if>
                        </PstlAdr>
                    </Cdtr>
                    <CdtrAcct>
                        <Id>
                            <IBAN>${tx.creditor_account_IBAN}</IBAN>
                        </Id>
                    </CdtrAcct>
                    <Purp>
                        <Cd>${tx.purpose_code}</Cd>
                    </Purp>
                    <RmtInf>
                        <Strd>
                            <RfrdDocInf>
                                <Nb>${tx.reference_number}</Nb>
                                <#if tx.reference_date??><RltdDt>${tx.reference_date}</RltdDt></#if>
                            </RfrdDocInf>
                        </Strd>
                    </RmtInf>
                </CdtTrfTxInf>
            </#list>
        </PmtInf>
    </CstmrCdtTrfInitn>
</Document>