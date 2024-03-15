package com.telerikacademy.web.virtualwallet.models;

import jakarta.persistence.*;

@Entity
@Table(name = "verification_codes", schema = "virtual_wallet", catalog = "")
public class VerificationCodes {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "verification_code_id")
    private int verificationCodeId;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Basic
    @Column(name = "verification_code")
    private String verificationCode;

    public int getVerificationCodeId() {
        return verificationCodeId;
    }

    public void setVerificationCodeId(int verificationCodeId) {
        this.verificationCodeId = verificationCodeId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User userId) {
        this.user = userId;
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

        VerificationCodes that = (VerificationCodes) o;

        if (verificationCodeId != that.verificationCodeId) return false;
        if (user != that.user) return false;
        if (verificationCode != null ? !verificationCode.equals(that.verificationCode) : that.verificationCode != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = verificationCodeId;
        result = 31 * result + (verificationCode != null ? verificationCode.hashCode() : 0);
        return result;
    }
}
