package org.teamfour.system;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.teamfour.model.bsl.Ballot;
import org.teamfour.registry.client.DemoFacadeImpl;
import org.teamfour.service.VotingService;
import org.teamfour.service.VotingServiceImpl;
import org.teamfour.system.data.*;
import org.teamfour.system.enums.Authority;
import org.teamfour.system.enums.Operation;
import org.teamfour.system.enums.Status;
import org.teamfour.system.enums.SystemResponseType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;

public class VotingSystemImpl extends SystemBase implements VotingSystem {

    private final Metadata systemMetadata;
    private final VotingService votingService;

    public VotingSystemImpl() throws IOException {
        this.systemMetadata = fetchSystemData();
        this.votingService = new VotingServiceImpl(new DemoFacadeImpl());
    }

    @Override
    public SystemResponse handleRequest(SystemRequest request) {
        switch (request.getType()) {
            case OPERATION -> {
                boolean completed = performOperation(request.getOperation());
                return SystemResponse
                        .builder()
                        .type(completed ? SystemResponseType.SUCCESS : SystemResponseType.FAILURE)
                        .build();
            }
            case VOTE_FINALIZE -> {
                log.info("PRINTING TO VVPAT");
                return SystemResponse.builder()
                        .type(SystemResponseType.SUCCESS)
                        .build();
            }
            case CAST_VOTE -> {
                boolean recorded = votingService.recordVotes(request.getVotes(), request.getVoterAccessCode());
                if (recorded) {
                    // TODO: CALL VVPAT
                    return SystemResponse.builder()
                            .type(SystemResponseType.SUCCESS)
                            .build();
                } else {
                    return SystemResponse.builder()
                            .type(SystemResponseType.FAILURE)
                            .build();
                }
            }
            case VOTER_LOGIN -> {
                if (votingService.voterLogin(request.getVoterAccessCode())) {
                    return SystemResponse.builder()
                            .type(SystemResponseType.SUCCESS)
                            .build();
                } else {
                    return SystemResponse.builder()
                            .type(SystemResponseType.FAILURE)
                            .build();
                }
            }
            case ADMIN_LOGIN -> {
                return SystemResponse.builder()
                        .type(SystemResponseType.SUCCESS)
                        .build();
            }
        }
        return null;
    }

    @Override
    public Metadata getSystemMetadata() {
        return systemMetadata;
    }

    @Override
    public org.teamfour.model.db.Ballot getBallot() {
        if (systemMetadata.getElectionId() != null) {
            return votingService.findBallot(systemMetadata.getElectionId());
        }
        return null;
    }

    private boolean performOperation(Operation operation) {
        switch (operation) {
            case BEGIN_VOTING_WINDOW -> {
                systemMetadata.setStatus(Status.IN_PROCESS);
                updateSystemData();
            }
            case CONFIGURATION -> {
                return handleConfiguration();
            }
            case VOTE_COUNT_EXPORT -> {
                return exportCount();
            }
            case SYSTEM_LOG_EXPORT -> {
                return exportSystemLogs();
            }
            case BEGIN_VOTE_COUNTING -> {
                systemMetadata.setStatus(Status.VOTE_COUNTING);
                updateSystemData();
            }
            case END_VOTE_PROCESS -> {
                systemMetadata.setStatus(Status.POST_ELECTION);
                systemMetadata.setAutoTestComplete(false);
                systemMetadata.setManualTestComplete(false);
                systemMetadata.setTabulationComplete(false);
                systemMetadata.setElectionId(null);
                updateSystemData();
            }
        }
        return true;
    }

    @Override
    public boolean validDeviceConnected() {
        try (FileReader reader = new FileReader(SystemFiles.DEVICE_PATH + DeviceFiles.AUTH)) {
            HashMap<String, String> tokenMap = new Gson().fromJson(reader, HashMap.class);
            if (!tokenMap.containsKey("token")) return false;
            DecodedJWT decodedJWT = verifyAndDecode(decrypt(tokenMap.get("token")));
            Authority authority = decodedJWT.getClaim(Claims.AUTHORITY).as(Authority.class);
            String username = decodedJWT.getClaim(Claims.USERNAME).asString();
            log.info("TOKEN RETRIEVED, CLAIMS: authority={}, username={}", authority, username);
            return authority == Authority.SYSTEM_ADMIN;
        } catch (IOException e) {
            log.error("ERROR VERIFYING DEVICE");
            log.error(e.getMessage());
        }
        return false;
    }

    private boolean exportCount() {
        String fileName = SystemFiles.DEVICE_PATH + "SysTabulationResults_" + Instant.now() + ".json";
        try {
            org.teamfour.model.db.Ballot ballot = votingService.findBallot(systemMetadata.getElectionId());
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String output = gson.toJson(ballot, Ballot.class);
            Files.writeString(Path.of(fileName), output, StandardCharsets.UTF_8);
            return new File(fileName).exists();
        } catch (IOException e) {
            log.error("ERROR WRITING COUNT FILE");
            log.error(e.getMessage());
        }
        return false;
    }

    private boolean exportSystemLogs() {
        String fileName = SystemFiles.DEVICE_PATH + "VOTING_SYSTEM_LOG_" + Instant.now() + ".log";
        try {
            File logFile = new File(SystemFiles.LOG);
            File exportedLog = new File(fileName);
            if (!logFile.exists()) {
                log.error("NO LOG FILE FOUND");
                return false;
            }
            FileUtils.copyFile(logFile, exportedLog);
        } catch (IOException e) {
            log.error("ERROR EXPORTING LOGS");
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    private boolean handleConfiguration() {
        try (BufferedReader br = new BufferedReader(new FileReader(SystemFiles.DEVICE_PATH + DeviceFiles.BALLOT))) {
            org.teamfour.model.bsl.Ballot ballot = new Gson().fromJson(br, Ballot.class);
            org.teamfour.model.db.Ballot dbBallot = votingService.saveBallot(ballot);
            if (dbBallot == null) {
                log.error("ERROR SAVING BALLOT");
                return false;
            }
            systemMetadata.setElectionId(dbBallot.getId());
            systemMetadata.setStatus(Status.PRE_ELECTION);
            updateSystemData();
            return true;
        }  catch (IOException ignored) {
            log.error("");
        }
        return false;
    }

    private Metadata fetchSystemData() {
        try (BufferedReader br = new BufferedReader(new FileReader(SystemFiles.META))) {
            return new Gson().fromJson(br, Metadata.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("UNABLE TO READ SYSTEM METADATA");
        }
        return null;
    }

    private void updateSystemData() {
        try (FileOutputStream outputStream = new FileOutputStream(SystemFiles.META, false)){
            outputStream.write(new Gson().toJson(systemMetadata).getBytes());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public DecodedJWT verifyAndDecode(String token) {
        DecodedJWT decodedJWT = null;
        try {
            decodedJWT = verifier().verify(token);
        } catch (Exception e) {
            log.info("ERROR DECODING TOKEN");
        }
        return decodedJWT;
    }

}