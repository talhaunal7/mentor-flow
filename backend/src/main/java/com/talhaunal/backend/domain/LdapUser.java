package com.talhaunal.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

@Entry(
        objectClasses = {"inetOrgPerson", "organizationalPerson", "person", "top"}
)
public final class LdapUser {

    @Id
    @JsonIgnore
    private Name dn;

    @Attribute(name = "cn")
    private String commonName;

    @Attribute(name = "sn")
    private String surname;

    @Attribute(name = "uid")
    private String uid;

    @Attribute(name = "mail")
    private String mail;


    public LdapUser() {
    }

    public LdapUser(Name dn, String commonName, String surname, String uid, String mail) {
        this.dn = dn;
        this.commonName = commonName;
        this.surname = surname;
        this.uid = uid;
        this.mail = mail;
    }

    public Name getDn() {
        return dn;
    }

    public void setDn(Name dn) {
        this.dn = dn;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}