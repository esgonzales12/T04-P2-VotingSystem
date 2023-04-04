package org.teamfour.system.data;

public class Metadata {
      private final String version;
      private String electionId;
      private String status;
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

    public String getElectionId() {
        return electionId;
    }

    public String getStatus() {
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

    public void setElectionId(String electionId) {
        this.electionId = electionId;
    }

    public void setStatus(String status) {
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
