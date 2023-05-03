package org.teamfour.system.data;

import org.teamfour.system.enums.Status;

public class Metadata {
      private final String version;
      private Integer electionId;
      private Status status;
      private String voterAccessCode;
      private boolean autoTestComplete;
      private boolean manualTestComplete;
      private boolean tabulationComplete;

    public Metadata(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public Integer getElectionId() {
        return electionId;
    }

    public Status getStatus() {
        return status;
    }

    public String getVoterAccessCode() {
        return voterAccessCode;
    }

    public boolean isAutoTestComplete() {
        return autoTestComplete;
    }

    public boolean isManualTestComplete() {
        return manualTestComplete;
    }

    public boolean isTabulationComplete() {
        return tabulationComplete;
    }

    public void setElectionId(Integer electionId) {
        this.electionId = electionId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setVoterAccessCode(String voterAccessCode) {
        this.voterAccessCode = voterAccessCode;
    }

    public void setAutoTestComplete(boolean autoTestComplete) {
        this.autoTestComplete = autoTestComplete;
    }

    public void setManualTestComplete(boolean manualTestComplete) {
        this.manualTestComplete = manualTestComplete;
    }

    public void setTabulationComplete(boolean tabulationComplete) {
        this.tabulationComplete = tabulationComplete;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "version='" + version + '\'' +
                ", electionId='" + electionId + '\'' +
                ", status='" + status + '\'' +
                ", voterAccessCode='" + voterAccessCode + '\'' +
                ", autoTestComplete=" + autoTestComplete +
                ", manualTestComplete=" + manualTestComplete +
                ", tabulationComplete=" + tabulationComplete +
                '}';
    }
}
