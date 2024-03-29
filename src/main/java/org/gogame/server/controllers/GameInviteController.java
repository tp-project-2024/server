package org.gogame.server.controllers;

import lombok.RequiredArgsConstructor;
import org.gogame.server.domain.entities.dto.user.UserInviteDto;
import org.gogame.server.service.GameService;
import org.gogame.server.service.PermissionValidatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/game/invite")
@RequiredArgsConstructor
public class GameInviteController {

    private final GameService gameService;

    private final PermissionValidatorService validatorService;

    @PostMapping("/send")
    public ResponseEntity<UserInviteDto> sendGameInvite (
            @RequestBody UserInviteDto request,
            @RequestHeader("Authorization") String token
    ) {
        if (!validatorService.validateUserId(request.getUserSenderId(), token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserInviteDto response;
        try {
            response = gameService.sendGameInvite(request);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/accept")
    public ResponseEntity<UserInviteDto> acceptGameInvite (
            @RequestBody UserInviteDto request,
            @RequestHeader("Authorization") String token
    ) {
        if (!validatorService.validateUserId(request.getUserReceiverId(), token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserInviteDto response;
        try {
            response = gameService.acceptGameInvite(request);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/reject")
    public ResponseEntity<UserInviteDto> rejectGameInvite (
            @RequestBody UserInviteDto request,
            @RequestHeader("Authorization") String token
    ) {
        if (!validatorService.validateUserId(request.getUserReceiverId(), token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            gameService.rejectGameInvite(request);
        } catch (SQLException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<List<UserInviteDto>> fetchGameInvite (
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    ) {
        if (!validatorService.validateUserId(id, token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<UserInviteDto> response;
        try {
            response = gameService.fetchGameInvite(id);
            if (response.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
