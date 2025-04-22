<?xml version="1.0"?>
<Document xmlns="urn:iso:std:iso:20022:tech:xsd:pain.001.001.09"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="urn:iso:std:iso:20022:tech:xsd:pain.001.001.09 pain.001.001.09.xsd">
    <CstmrCdtTrfInitn>
        <GrpHdr>
            <MsgId>${id}</MsgId>
            <CreDtTm>${date}</CreDtTm>
            <NbOfTxs>${nb_of_txs}</NbOfTxs>
            <InitgPty>
                <Nm>${initiator_name}</Nm>
            </InitgPty>
        </GrpHdr>
        <PmtInf>
            <PmtInfId>${payment_id}</PmtInfId>
            <PmtMtd>${payment_method}</PmtMtd>
            <ReqdExctnDt>
                <Dt>${requested_execution_date}</Dt>
            </ReqdExctnDt>
            <Dbtr>
                <Nm>${debtor_name}</Nm>
            </Dbtr>
            <DbtrAcct>
                <Id>
                    <IBAN>${debtor_account_IBAN}</IBAN>
                </Id>
            </DbtrAcct>
            <DbtrAgt>
                <FinInstnId>
                    <BICFI>${debtor_agent_BIC}</BICFI>
                </FinInstnId>
            </DbtrAgt>
            <ChrgBr>${charge_bearer}</ChrgBr>

            <#list transactions as tx>
                <CdtTrfTxInf>
                    <PmtId>
                        <EndToEndId>${tx.payment_id}</EndToEndId>
                    </PmtId>
                    <Amt>
                        <InstdAmt Ccy="${tx.payment_currency}">${tx.payment_amount}</InstdAmt>
                    </Amt>
                    <CdtrAgt>
                        <FinInstnId>
                            <BICFI>${tx.creditor_agent_BIC}</BICFI>
                        </FinInstnId>
                    </CdtrAgt>
                    <Cdtr>
                        <Nm>${tx.creditor_name}</Nm>
                    </Cdtr>
                    <CdtrAcct>
                        <Id>
                            <IBAN>${tx.creditor_account_IBAN}</IBAN>
                        </Id>
                    </CdtrAcct>

                    <#if tx.remittance_information??>
                        <RmtInf>
                            <Ustrd>${tx.remittance_information}</Ustrd>
                        </RmtInf>
                    </#if>

                    <SplmtryData>
                        <Envlp>
                            <WC/>
                        </Envlp>
                    </SplmtryData>
                </CdtTrfTxInf>
            </#list>

        </PmtInf>
    </CstmrCdtTrfInitn>
</Document>
