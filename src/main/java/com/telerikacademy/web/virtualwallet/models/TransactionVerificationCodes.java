package com.telerikacademy.web.virtualwallet.models;

import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import jakarta.persistence.*;

@Entity
@Table(name = "transaction_verification_codes", schema = "virtual_wallet", catalog = "")
public class TransactionVerificationCodes {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "transaction_verification_code_id")
    private int transactionVerificationCodeId;
    @JoinColumn(name = "transaction_id")
    @OneToOne
    private Transaction transaction;
    @Basic
    @Column(name = "verification_code")
    private String verificationCode;


    @OneToOne
    @JoinColumn(name = "sender_wallet")
    private Wallet senderWallet;

    @OneToOne
    @JoinColumn(name = "receiver_wallet")
    private Wallet receiverWallet;

    public Wallet getSenderWallet() {
        return senderWallet;
    }

    public void setSenderWallet(Wallet senderWallet) {
        this.senderWallet = senderWallet;
    }

    public Wallet getReceiverWallet() {
        return receiverWallet;
    }

    public void setReceiverWallet(Wallet receiverWallet) {
        this.receiverWallet = receiverWallet;
    }
    public int getTransactionVerificationCodeId() {
        return transactionVerificationCodeId;
    }

    public void setTransactionVerificationCodeId(int transactionVerificationCodeId) {
        this.transactionVerificationCodeId = transactionVerificationCodeId;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionVerificationCodes that = (TransactionVerificationCodes) o;

        if (transactionVerificationCodeId != that.transactionVerificationCodeId) return false;
        if (transaction != that.transaction) return false;
        if (verificationCode != null ? !verificationCode.equals(that.verificationCode) : that.verificationCode != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = transactionVerificationCodeId;
        result = 31 * result + (verificationCode != null ? verificationCode.hashCode() : 0);
        return result;
    }
}
