package org.teamfour.registry.data;

import org.teamfour.dao.annotations.Column;
import org.teamfour.dao.annotations.PrimaryKey;
import org.teamfour.dao.annotations.Table;

@Table(name = "registeredVoter")
public class RegisteredVoter {
    @PrimaryKey(columnIdentifier = "id")
    @Column(name = "id")
    private Integer id;

    @Column(name = "demographicHash")
    private String demographicHash;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "voteStatus")
    private String voteStatus;

    public RegisteredVoter(Builder builder) {
        this.demographicHash = builder.demographicHash;
        this.name = builder.name;
        this.address = builder.address;
        this.voteStatus = builder.voteStatus;
    }

    public RegisteredVoter() {}

    public String getDemographicHash() {
        return demographicHash;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getVoteStatus() {
        return voteStatus;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "RegisteredVoter{" +
                "id=" + id +
                ", demographicHash='" + demographicHash + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", voteStatus='" + voteStatus + '\'' +
                '}';
    }

    public static class Builder {
        private String demographicHash;
        private String name;
        private String address;
        private String voteStatus;

        public Builder setDemographicHash(String demographicHash) {
            this.demographicHash = demographicHash;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setVoteStatus(String voteStatus) {
            this.voteStatus = voteStatus;
            return this;
        }

        public RegisteredVoter createRegisteredVoter() {
            return new RegisteredVoter(this);
        }
    }
}
